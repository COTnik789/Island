package com.island.logging;

import com.island.domain.animal.AnimalType;
import com.island.domain.world.Cell;
import com.island.domain.world.Island;
import com.island.logging.dto.AnimalDto;
import com.island.logging.dto.CellLogDto;
import com.island.logging.dto.IslandLogDto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DefaultLifeLoggerService implements LifeLoggerService {
    private final List<LogObserver> observers = new CopyOnWriteArrayList<>();

    private final Map<AnimalType, Long> animalCounts = new ConcurrentHashMap<>();
    private final Map<AnimalType, Long> eatenAnimals = new ConcurrentHashMap<>();
    private final Map<AnimalType, Long> bornAnimals  = new ConcurrentHashMap<>();
    private final AtomicInteger eatenPlants = new AtomicInteger();
    private final AtomicInteger bornPlants  = new AtomicInteger();

    private final Map<String, Map<AnimalType, Long>> eatenAnimalsByCell = new ConcurrentHashMap<>();
    private final Map<String, Map<AnimalType, Long>> bornAnimalsByCell  = new ConcurrentHashMap<>();
    private final Map<String, Integer>               eatenPlantsByCell  = new ConcurrentHashMap<>();
    private final Map<String, Integer>               bornPlantsByCell   = new ConcurrentHashMap<>();
    private final Map<String, Map<AnimalType, Integer>> migrationsInByCell  = new ConcurrentHashMap<>();
    private final Map<String, Map<AnimalType, Integer>> migrationsOutByCell = new ConcurrentHashMap<>();

    private final Map<String, List<AnimalDto>> lastAnimalsByCell = new ConcurrentHashMap<>();
    private volatile int lastTotalPlantCount = 0;

    public DefaultLifeLoggerService() {
        for (AnimalType t : AnimalType.values()) {
            animalCounts.put(t, 0L);
            eatenAnimals.put(t, 0L);
            bornAnimals.put(t, 0L);
        }
    }

    @Override public void registerObserver(LogObserver observer) { observers.add(observer); }
    @Override public void unregisterObserver(LogObserver observer) { observers.remove(observer); }

    @Override
    public void logSpawn(int row, int col, AnimalType type, int count) {
        animalCounts.merge(type, (long) count, Long::sum);
    }

    @Override
    public void logPlantSpawn(int row, int col, int count) {
        bornPlants.addAndGet(count);
        bornPlantsByCell.merge(key(row, col), count, Integer::sum);
    }

    @Override
    public void logMigration(int fromRow, int fromCol, int toRow, int toCol, AnimalType type) {
        String fromKey = key(fromRow, fromCol);
        String toKey   = key(toRow, toCol);
        migrationsOutByCell.computeIfAbsent(fromKey, k -> new EnumMap<>(AnimalType.class))
                .merge(type, 1, Integer::sum);
        migrationsInByCell.computeIfAbsent(toKey, k -> new EnumMap<>(AnimalType.class))
                .merge(type, 1, Integer::sum);
    }

    @Override
    public void logEating(int row, int col, AnimalType victimType, int plants, int animals) {
        String k = key(row, col);
        if (plants > 0) {
            eatenPlants.addAndGet(plants);
            eatenPlantsByCell.merge(k, plants, Integer::sum);
        }
        if (animals > 0 && victimType != null) {
            eatenAnimals.merge(victimType, (long) animals, Long::sum);
            eatenAnimalsByCell.computeIfAbsent(k, kk -> new EnumMap<>(AnimalType.class))
                    .merge(victimType, (long) animals, Long::sum);
        }
    }

    @Override
    public void logReproduction(int row, int col, AnimalType type, int count) {
        if (count <= 0) return;
        String k = key(row, col);
        bornAnimals.merge(type, (long) count, Long::sum);
        bornAnimalsByCell.computeIfAbsent(k, kk -> new EnumMap<>(AnimalType.class))
                .merge(type, (long) count, Long::sum);
    }

    @Override
    public void logPlantBirth(int row, int col, int count) {
        if (count <= 0) return;
        bornPlants.addAndGet(count);
        bornPlantsByCell.merge(key(row, col), count, Integer::sum);
    }

    @Override
    public void logAnimalCount(Island island) {
        Map<AnimalType, Long> totals = new EnumMap<>(AnimalType.class);
        for (AnimalType t : AnimalType.values()) totals.put(t, 0L);

        Map<String, List<AnimalDto>> animalsByCell = new HashMap<>();

        for (int r = 0; r < island.getRows(); r++) {
            for (int c = 0; c < island.getCols(); c++) {
                Cell cell = island.getCell(r, c);
                Map<AnimalType, Integer> perType = cell.getAnimals().stream()
                        .collect(Collectors.toMap(
                                a -> AnimalType.fromClass(a.getClass()),
                                a -> 1,
                                Integer::sum,
                                () -> new EnumMap<>(AnimalType.class)
                        ));
                perType.forEach((t, n) -> totals.merge(t, n.longValue(), Long::sum));
                List<AnimalDto> list = perType.entrySet().stream()
                        .map(e -> new AnimalDto(e.getKey().name(), e.getValue()))
                        .toList();
                animalsByCell.put(key(r, c), list);
            }
        }
        animalCounts.clear();
        animalCounts.putAll(totals);
        lastAnimalsByCell.clear();
        lastAnimalsByCell.putAll(animalsByCell);
    }

    @Override
    public void logPlantCount(Island island) {
        int sum = 0;
        for (int r = 0; r < island.getRows(); r++) {
            for (int c = 0; c < island.getCols(); c++) {
                sum += island.getCell(r, c).getPlants().size();
            }
        }
        lastTotalPlantCount = sum;
    }

    @Override
    public LogResult flushAndGetLogResult() {
        Set<String> keys = new HashSet<>();
        keys.addAll(lastAnimalsByCell.keySet());
        keys.addAll(eatenAnimalsByCell.keySet());
        keys.addAll(bornAnimalsByCell.keySet());
        keys.addAll(eatenPlantsByCell.keySet());
        keys.addAll(bornPlantsByCell.keySet());
        keys.addAll(migrationsInByCell.keySet());
        keys.addAll(migrationsOutByCell.keySet());

        List<CellLogDto> cells = new ArrayList<>();
        for (String k : keys) {
            int[] rc = parseKey(k);
            List<AnimalDto> animals = lastAnimalsByCell.getOrDefault(k, List.of());
            CellLogDto dto = new CellLogDto(rc[0], rc[1], animals);

            Map<AnimalType, Long> born = bornAnimalsByCell.get(k);
            if (born != null) born.forEach((t, n) -> dto.addNewborns(t.name(), n.intValue()));

            Map<AnimalType, Long> eaten = eatenAnimalsByCell.get(k);
            if (eaten != null) eaten.forEach((t, n) -> dto.incrementEatenAnimals(t.name(), n.intValue()));

            Integer ep = eatenPlantsByCell.get(k);
            if (ep != null && ep > 0) dto.incrementEatenPlants(ep);

            Integer bp = bornPlantsByCell.get(k);
            if (bp != null && bp > 0) dto.addBornPlants(bp);

            Map<AnimalType, Integer> min = migrationsInByCell.get(k);
            if (min != null) min.forEach((t, n) -> { for (int i = 0; i < n; i++) dto.addMigrationIn(t.name()); });

            Map<AnimalType, Integer> mout = migrationsOutByCell.get(k);
            if (mout != null) mout.forEach((t, n) -> { for (int i = 0; i < n; i++) dto.addMigrationOut(t.name()); });

            cells.add(dto);
        }

        IslandLogDto islandLog = new IslandLogDto(cells, lastTotalPlantCount);
        notifyObservers(islandLog);

        eatenAnimalsByCell.clear();
        bornAnimalsByCell.clear();
        eatenPlantsByCell.clear();
        bornPlantsByCell.clear();
        migrationsInByCell.clear();
        migrationsOutByCell.clear();
        eatenPlants.set(0);
        bornPlants.set(0);

        return islandLog;
    }

    private void notifyObservers(LogResult log) {
        for (LogObserver o : observers) o.onLog(log);
    }

    private static String key(int row, int col) { return row + "," + col; }
    private static int[] parseKey(String k) {
        String[] p = k.split(",");
        return new int[]{Integer.parseInt(p[0]), Integer.parseInt(p[1])};
    }
}

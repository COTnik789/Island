package com.island.logging.dto;


import com.island.domain.animal.AnimalType;
import com.island.logging.LogResult;

import java.util.*;

public class IslandLogDto implements LogResult {
    private final List<CellLogDto> cellLogs;
    private final int totalPlantCount;
    private final Map<AnimalType, Long> animalCounts;
    private final Map<AnimalType, Long> eatenAnimals;
    private final long eatenPlants;
    private final Map<AnimalType, Long> bornAnimals;
    private final long bornPlants;

    public IslandLogDto(List<CellLogDto> cellLogs, int totalPlantCount) {
        this.cellLogs = List.copyOf(cellLogs);
        this.totalPlantCount = totalPlantCount;
        this.animalCounts = new EnumMap<>(AnimalType.class);
        this.eatenAnimals = new EnumMap<>(AnimalType.class);
        this.bornAnimals = new EnumMap<>(AnimalType.class);

        for (AnimalType t : AnimalType.values()) {
            animalCounts.put(t, 0L);
            eatenAnimals.put(t, 0L);
            bornAnimals.put(t, 0L);
        }

        this.eatenPlants = cellLogs.stream().mapToLong(CellLogDto::getEatenPlants).sum();
        this.bornPlants = cellLogs.stream().mapToLong(CellLogDto::getBornPlants).sum();

        for (CellLogDto cell : cellLogs) {
            for (AnimalDto dto : cell.getAnimals()) {
                AnimalType type = AnimalType.valueOf(dto.type());
                animalCounts.merge(type, (long) dto.count(), Long::sum);
            }
            cell.getNewborns().forEach((type, count) -> {
                if (!"Plant".equals(type)) {
                    bornAnimals.merge(AnimalType.valueOf(type), (long) count, Long::sum);
                }
            });
            cell.getEatenAnimalsByType().forEach((type, count) ->
                    eatenAnimals.merge(AnimalType.valueOf(type), (long) count, Long::sum));
        }
    }

    public List<CellLogDto> getCellLogs() {
        return Collections.unmodifiableList(cellLogs);
    }

    public int getTotalEatenAnimals() {
        return eatenAnimals.values().stream().mapToInt(Long::intValue).sum();
    }

    public int getTotalEatenPlants() {
        return (int) eatenPlants;
    }

    public Map<String, Integer> getTotalBornAnimals() {
        Map<String, Integer> total = new HashMap<>();
        bornAnimals.forEach((type, count) -> total.put(type.name(), count.intValue()));
        return Collections.unmodifiableMap(total);
    }

    public int getTotalBornPlants() {
        return (int) bornPlants;
    }

    public Map<String, Integer> getTotalAnimalCount() {
        Map<String, Integer> total = new HashMap<>();
        animalCounts.forEach((type, count) -> total.put(type.name(), count.intValue()));
        return Collections.unmodifiableMap(total);
    }

    public int getTotalPlantCount() {
        return totalPlantCount;
    }

    public Map<AnimalType, Long> getAnimalCounts() {
        return Collections.unmodifiableMap(animalCounts);
    }

    public Map<AnimalType, Long> getEatenAnimals() {
        return Collections.unmodifiableMap(eatenAnimals);
    }

    public Map<AnimalType, Long> getBornAnimals() {
        return Collections.unmodifiableMap(bornAnimals);
    }

    public long getEatenPlants() {
        return eatenPlants;
    }

    public long getBornPlants() {
        return bornPlants;
    }
}

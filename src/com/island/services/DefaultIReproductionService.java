package com.island.services;

import com.island.domain.animal.AnimalType;
import com.island.domain.animal.BaseAnimal;
import com.island.domain.animal.Predator;
import com.island.domain.plant.Plant;
import com.island.domain.world.Cell;
import com.island.domain.world.Island;
import com.island.factory.AnimalFactory;
import com.island.factory.AnimalRegistry;
import com.island.factory.PlantRegistry;
import com.island.logging.LifeLoggerService;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.concurrent.ThreadLocalRandom.current;

public class DefaultIReproductionService implements IReproductionService {
    private static final float HERB_SATIETY_THRESHOLD = 0.50f;
    private static final float PRED_SATIETY_THRESHOLD = 0.80f;
    private static final int   PRED_MIN_PREY_NEAR     = 3;

    private final IslandService islandService;
    private final LifeLoggerService logger;
    private final AnimalFactory animalFactory;
    private final AnimalRegistry animalRegistry;
    private final PlantRegistry plantRegistry;

    private final ExecutorService executor =
            Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors()));

    public DefaultIReproductionService(IslandService islandService,
                                       LifeLoggerService logger,
                                       AnimalFactory animalFactory,
                                       AnimalRegistry animalRegistry,
                                       PlantRegistry plantRegistry) {
        this.islandService = islandService;
        this.logger = logger;
        this.animalFactory = animalFactory;
        this.animalRegistry = animalRegistry;
        this.plantRegistry = plantRegistry;
    }

    @Override
    public void processReproduction(Island island) {
        List<Future<?>> futures = new ArrayList<>();
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                final int r = row, c = col;
                futures.add(executor.submit(() -> reproduceInCell(island.getCell(r, c), r, c)));
            }
        }
        for (Future<?> f : futures) {
            try { f.get(); }
            catch (InterruptedException ie) { Thread.currentThread().interrupt(); return; }
            catch (ExecutionException ee) { throw new RuntimeException("Reproduction task failed", ee.getCause()); }
        }
    }

    public void shutdown() { executor.shutdownNow(); }

    private void reproduceInCell(Cell cell, int row, int col) {
        reproducePlants(cell, row, col);
        reproduceAnimals(cell, row, col);
    }

    private void reproducePlants(Cell cell, int row, int col) {
        int plantsNow = cell.getPlants().size();
        if (plantsNow == 0) return;

        int capacity = plantCapacityForCell(cell);
        int free = Math.max(0, capacity - plantsNow);
        if (free == 0) return;

        int born = Math.min(plantsNow / 2, free);
        if (born <= 0) return;

        List<Supplier<Plant>> suppliers = plantRegistry.getAllPlants();
        if (suppliers.isEmpty()) return;

        for (int i = 0; i < born; i++) {
            Supplier<Plant> s = suppliers.get(current().nextInt(suppliers.size()));
            cell.addPlant(s.get());
        }
        logger.logPlantBirth(row, col, born);
    }

    private int plantCapacityForCell(Cell cell) {
        if (!cell.getPlants().isEmpty()) {
            return cell.getPlants().get(0).getMaxCountOnCell();
        }
        var suppliers = plantRegistry.getAllPlants();
        if (!suppliers.isEmpty()) {
            return suppliers.get(0).get().getMaxCountOnCell();
        }
        return 1000; // фолбэк, если вдруг реестр пуст
    }

    private void reproduceAnimals(Cell cell, int row, int col) {
        var snapshot = cell.animalsSnapshot();
        if (snapshot.isEmpty()) return;

        Map<Class<? extends BaseAnimal>, List<BaseAnimal>> byClass = snapshot.stream()
                .collect(Collectors.groupingBy(a -> (Class<? extends BaseAnimal>) a.getClass()));

        for (var e : byClass.entrySet()) {
            Class<? extends BaseAnimal> clazz = e.getKey();
            List<BaseAnimal> list = e.getValue();
            if (list.size() < 2) continue;

            boolean predator = Predator.class.isAssignableFrom(clazz);
            float threshold = predator ? PRED_SATIETY_THRESHOLD : HERB_SATIETY_THRESHOLD;

            List<BaseAnimal> fertile = list.stream()
                    .filter(a -> a.getCurrentSaturation() >= a.getMaxSaturation() * threshold)
                    .toList();
            if (fertile.size() < 2) continue;

            if (predator) {
                long preyCount = snapshot.stream()
                        .filter(x -> !x.getClass().equals(clazz))
                        .count();
                if (preyCount < PRED_MIN_PREY_NEAR) continue;
            }

            int currentSame = list.size();
            int maxOnCell = list.get(0).getMaxCountOnCell();
            int freeSlots = Math.max(0, maxOnCell - currentSame);
            if (freeSlots == 0) continue;

            int offspring = Math.min(fertile.size() / 2, freeSlots);
            if (offspring <= 0) continue;

            Supplier<BaseAnimal> supplier = findSupplier(clazz);
            if (supplier == null) continue;

            int born = 0;
            for (int i = 0; i < offspring; i++) {
                BaseAnimal child = animalFactory.createAnimalWithRandomSaturation(supplier);
                cell.addAnimal(child);
                born++;
            }
            if (born > 0) {
                logger.logReproduction(row, col, AnimalType.fromClass(clazz), born);
            }
        }
    }

    private Supplier<BaseAnimal> findSupplier(Class<? extends BaseAnimal> clazz) {
        for (Supplier<BaseAnimal> s : animalRegistry.getAllAnimals()) {
            BaseAnimal probe = s.get();
            if (probe.getClass().equals(clazz)) return s;
        }
        return null;
    }
}

package com.island.services;

import com.island.config.EatingProbabilityMap;
import com.island.domain.animal.Animal;
import com.island.domain.animal.AnimalType;
import com.island.domain.animal.BaseAnimal;
import com.island.domain.animal.Herbivore;
import com.island.domain.animal.Predator;
import com.island.domain.plant.Plant;
import com.island.domain.world.Cell;
import com.island.domain.world.Island;
import com.island.logging.LifeLoggerService;

import java.util.*;
import java.util.concurrent.*;

public class DefaultEatingService implements IEatingService {
    private final IslandService islandService;
    private final LifeLoggerService logger;
    private final EatingProbabilityMap probabilityMap;
    private final ExecutorService executor = Executors.newFixedThreadPool(
            Math.max(1, Runtime.getRuntime().availableProcessors())
    );

    public DefaultEatingService(IslandService islandService, LifeLoggerService logger, EatingProbabilityMap probabilityMap) {
        this.islandService = islandService;
        this.logger = logger;
        this.probabilityMap = probabilityMap;
    }

    @Override
    public void processEating(Island island) {
        List<Future<?>> futures = new ArrayList<>();
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                final int r = row, c = col;
                futures.add(executor.submit(() -> processEatingForCell(island.getCell(r, c), r, c)));
            }
        }
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return;
            } catch (ExecutionException ee) {
                throw new RuntimeException("Eating task failed", ee.getCause());
            }
        }
    }

    public void shutdown() {
        executor.shutdownNow();
    }

    private void processEatingForCell(Cell cell, int row, int col) {
        var snapshot = cell.animalsSnapshot();
        if (snapshot.isEmpty()) return;

        var removed = new HashSet<BaseAnimal>();

        for (BaseAnimal animal : snapshot) {
            if (removed.contains(animal)) continue;

            if (animal instanceof Herbivore) {
                int eatenPlants = eatPlants(animal, cell);
                if (eatenPlants > 0) {
                    logger.logEating(row, col, null, eatenPlants, 0);
                }
            } else if (animal instanceof Predator) {
                BaseAnimal prey = eatAnimal(animal, snapshot, cell, removed);
                if (prey != null) {
                    logger.logEating(row, col, AnimalType.fromClass(prey.getClass()), 0, 1);
                }
            }
        }
    }

    private int eatPlants(BaseAnimal herbivore, Cell cell) {
        var plants = new ArrayList<>(cell.getPlants());
        float need = herbivore.getMaxSaturation() - herbivore.getCurrentSaturation();
        if (need <= 0) return 0;

        int eaten = 0;
        for (Plant p : plants) {
            if (need <= 0) break;

            if (!cell.removePlantIfPresent(p)) continue;

            float gain = Math.min(need, p.getWeight());
            herbivore.increaseSaturation(gain);
            need -= gain;
            eaten++;
        }
        return eaten;
    }

    private BaseAnimal eatAnimal(BaseAnimal predator, List<BaseAnimal> snapshot, Cell cell, Set<BaseAnimal> removed) {
        Map<Class<? extends Animal>, Integer> preyMap =
                probabilityMap.getProbabilitiesFor(predator.getClass());
        if (preyMap == null || preyMap.isEmpty()) return null;

        var candidates = new ArrayList<BaseAnimal>();
        for (BaseAnimal a : snapshot) {
            if (a == predator || removed.contains(a)) continue;
            int chance = probabilityMap.getChance(
                    predator.getClass(),
                    a.getClass()
            );
            if (chance > 0) candidates.add(a);
        }
        if (candidates.isEmpty()) return null;

        BaseAnimal prey = candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
        int chance = probabilityMap.getChance(
                predator.getClass(),
                prey.getClass()
        );
        if (ThreadLocalRandom.current().nextInt(100) < chance) {
            if (!cell.removeAnimalIfPresent(prey)) return null;

            float need = predator.getMaxSaturation() - predator.getCurrentSaturation();
            if (need > 0) {
                float gain = Math.min(need, prey.getWeight() * 0.5f);
                predator.increaseSaturation(gain);
            }
            removed.add(prey);
            return prey;
        }
        return null;
    }

}

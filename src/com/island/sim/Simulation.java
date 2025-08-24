package com.island.sim;

import com.island.config.EatingProbabilityMap;
import com.island.domain.animal.AnimalType;
import com.island.domain.animal.BaseAnimal;
import com.island.domain.plant.Plant;
import com.island.domain.world.Cell;
import com.island.domain.world.Island;
import com.island.factory.AnimalFactory;
import com.island.factory.AnimalRegistry;
import com.island.factory.PlantRegistry;
import com.island.logging.DefaultLifeLoggerService;
import com.island.logging.IslandLogPrinterTask;
import com.island.services.AnimalLifeService;
import com.island.services.DefaultEatingService;
import com.island.services.DefaultIMovementService;
import com.island.services.DefaultIReproductionService;
import com.island.services.IslandService;
import com.island.spawn.AnimalSpawner;
import com.island.spawn.PlantSpawner;

import java.util.List;

public class Simulation {
    private final Island island;
    private final DefaultLifeLoggerService logger;
    private final AnimalLifeService animalLifeService;
    private final DefaultEatingService eatingService;
    private final DefaultIMovementService movementService;
    private final DefaultIReproductionService reproductionService;
    private final AnimalRegistry animalRegistry;
    private final PlantRegistry plantRegistry;
    private final IslandService islandService;
    private final AnimalSpawner animalSpawner;
    private final PlantSpawner plantSpawner;
    private final IslandLogPrinterTask logPrinter;
    private int currentDay = 0;

    public Simulation(int rows, int cols,
                      DefaultLifeLoggerService logger,
                      AnimalRegistry animalRegistry,
                      PlantRegistry plantRegistry) {

        this.island = new Island(rows, cols);
        this.logger = logger;
        this.animalRegistry = animalRegistry;
        this.plantRegistry = plantRegistry;

        this.islandService = new IslandService();
        this.animalSpawner = new AnimalSpawner(AnimalFactory.getInstance());
        this.plantSpawner = new PlantSpawner();
        this.animalLifeService = new AnimalLifeService();

        this.eatingService = new DefaultEatingService(islandService, logger, EatingProbabilityMap.getInstance());
        this.movementService = new DefaultIMovementService(islandService, logger);
        this.reproductionService = new DefaultIReproductionService(
                islandService, logger, AnimalFactory.getInstance(), animalRegistry, plantRegistry
        );

        this.logPrinter = new IslandLogPrinterTask();
        initializeIsland();
        startLogging();
    }

    private void initializeIsland() {
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);

                List<BaseAnimal> animals = animalSpawner.spawn(animalRegistry.getAllAnimals());
                for (BaseAnimal animal : animals) {
                    cell.addAnimal(animal);
                    logger.logSpawn(row, col, AnimalType.fromClass(animal.getClass()), 1);
                }

                List<Plant> plants = plantSpawner.spawn(plantRegistry.getAllPlants());
                for (Plant plant : plants) {
                    cell.addPlant(plant);
                    logger.logPlantSpawn(row, col, 1);
                }
            }
        }
        logger.logAnimalCount(island);
        logger.logPlantCount(island);
    }

    private void startLogging() {
        logger.registerObserver(logPrinter);
    }

    public void runDays(int days) {
        try {
            for (int i = 0; i < days; i++) {
                currentDay++;
                runDay();
                logger.flushAndGetLogResult();

                String end = checkEndCondition();
                if (end != null) break;
            }
        } finally {
            logger.unregisterObserver(logPrinter);
            logPrinter.stop();

            eatingService.shutdown();
            movementService.shutdown();
            reproductionService.shutdown();
        }
    }

    public void runUntilExtinction() {
        try {
            while (true) {
                currentDay++;
                runDay();
                logger.flushAndGetLogResult();

                String end = checkEndCondition();
                if (end != null) {
                    System.out.println("Simulation finished: " + end);
                    break;
                }
            }
        } finally {
            logger.unregisterObserver(logPrinter);
            logPrinter.stop();

            eatingService.shutdown();
            movementService.shutdown();
            reproductionService.shutdown();
        }
    }

    private void runDay() {
        eatingService.processEating(island);
        movementService.processMovement(island);
        reproductionService.processReproduction(island);
        animalLifeService.processSaturationDecrease(island);
        logger.logAnimalCount(island);
        logger.logPlantCount(island);
    }

    private String checkEndCondition() {
        long predators = 0, herbivores = 0;
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                for (BaseAnimal a : cell.getAnimals()) {
                    if (AnimalLifeService.isPredator(a.getClass())) predators++;
                    else herbivores++;
                }
            }
        }
        if (predators == 0 && herbivores == 0) return "All animals have ceased to exist";
        if (predators == 0) return "Only herbivores remain";
        if (herbivores == 0) return "Only predators remain";
        return null;
    }
}

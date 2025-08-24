package app;

import com.island.factory.AnimalRegistry;
import com.island.factory.PlantRegistry;
import com.island.logging.DefaultLifeLoggerService;
import com.island.sim.Simulation;

public class Main {
    public static void main(String[] args) {
        DefaultLifeLoggerService logger = new DefaultLifeLoggerService();
        AnimalRegistry animalRegistry = AnimalRegistry.getInstance();
        PlantRegistry plantRegistry = PlantRegistry.getInstance();
        Simulation simulation = new Simulation(10, 10, logger, animalRegistry, plantRegistry);
        simulation.runUntilExtinction();
    }
}
package service;

import factory.PlantSpawner;
import factory.Spawner;
import model.animal.Animal;
import factory.AnimalRegistry;
import factory.AnimalSpawner;
import model.core.Island;
import model.plant.Plant;

import java.util.List;
import java.util.function.Supplier;


public class Simulation {

    private final Island island;
    private final IslandService islandService = new IslandService();
    private final Spawner<Animal> animalSpawner = new AnimalSpawner();
    private final Spawner<Plant> plantSpawner = new PlantSpawner();

    public Simulation(int rows, int cols) {
        island = new Island(rows, cols);
    }

    public Island getIsland() {
        return island;
    }

    public void populateIsland(List<Supplier<Animal>> animalSupplier, List<Supplier<Plant>> plantSupplier) {
        for (int i = 0; i < island.getRows(); i++) {
            for (int j = 0; j < island.getCols(); j++) {
                List<Animal> animals = animalSpawner.spawn(animalSupplier);
                for(Animal animal : animals) {
                    islandService.addAnimal(island, animal, i, j);
                }

                List<Plant> plants = plantSpawner.spawn(plantSupplier);
                for(Plant plant : plants) {
                    islandService.addPlant(island, plant, i, j);
                }

            }
            
        }
    }
}

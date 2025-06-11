import data.Animal;
import factory.AnimalRegistry;
import factory.AnimalSpawner;

import java.util.List;

public class Simulation {
    private final AnimalSpawner animalSpawner = new AnimalSpawner();

    public void populateIsland(Island island){
        for (int row = 0; row < island.getGrid().length; row++) {
            for (int col = 0; col < island.getGrid()[row].length; col++) {

                List<Animal> animals = animalSpawner.spawnerRandomAnimals(AnimalRegistry.getAllAnimals());
                for (Animal animal : animals){
                    island.addAnimal(row, col, animal);
                }
            }
        }
    }
}

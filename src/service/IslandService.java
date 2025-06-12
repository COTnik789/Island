package service;

import model.animal.Animal;
import model.core.Island;
import model.plant.Plant;

import java.util.List;

public class IslandService {

    public void addAnimal(Island island, Animal animal, int row, int col) {
        island.getCell(row, col).addAnimal(animal);
    }

    public void removeAnimal(Island island, Animal animal, int row, int col) {
        island.getCell(row, col).removeAnimal(animal);
    }

    public void addPlant(Island island, Plant plant, int row, int col) {
        island.getCell(row, col).addPlant(plant);
    }

    public void removePlant(Island island, Plant plant, int row, int col) {
        island.getCell(row, col).removePlant(plant);
    }

    public List<Animal> getAnimals(Island island, int row, int col) {
        return island.getCell(row, col).getAnimals();
    }

    public List<Plant> getPlants(Island island, int row, int col) {
        return island.getCell(row, col).getPlants();
    }
}

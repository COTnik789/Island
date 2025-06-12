package model.core;

import model.animal.Animal;
import model.plant.Plant;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private final List<Animal> animals = new ArrayList<>();
    private final List<Plant> plants = new ArrayList<>();

    public List<Animal> getAnimals(){
        return animals;
    }

    public void addAnimal(Animal animal){
        animals.add(animal);
    }

    public void removeAnimal(Animal animal){
        animals.remove(animal);
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public void addPlant(Plant plant){
        plants.add(plant);
    }

    public void removePlant(Plant plant){
        plants.remove(plant);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "animals=" + animals +
                ", plants=" + plants +
                '}';
    }
}

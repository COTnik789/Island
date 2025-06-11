import data.Animal;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private final List<Animal> animals = new ArrayList<>();
    private int countPlants;

    public Cell(int countPlants) {
        this.countPlants = countPlants;
    }

    public List<Animal> getAnimals(){
        return animals;
    }

    public int getCountPlants() {
        return countPlants;
    }
}

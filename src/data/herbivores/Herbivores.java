package data.herbivores;

import data.Animal;

public abstract class Herbivores extends Animal {

    public Herbivores(int maxCountOnCell, float weight, int speed, float countFood) {
        super(maxCountOnCell, weight, speed, countFood);
    }
}

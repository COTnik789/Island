package model.animal.herbivores;

import model.animal.Animal;

public abstract class Herbivores extends Animal {

    public Herbivores(int maxCountOnCell, float weight, int speed, float countFood) {
        super(maxCountOnCell, weight, speed, countFood);
    }
}

package data.predators;

import data.Animal;

public abstract class Predator extends Animal {

    protected Predator(int maxCountOnCell, float weight, int speed, float countFood) {
        super(maxCountOnCell, weight, speed, countFood);
    }

    @Override
    public void eat() {

    }

    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }
}

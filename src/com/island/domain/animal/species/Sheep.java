package com.island.domain.animal.species;

import com.island.domain.animal.Herbivore;
import com.island.domain.animal.BaseAnimal;

public class Sheep extends BaseAnimal implements Herbivore {

    public Sheep() {
        super(140, 70, 3,15);
    }

    public Sheep(float currentSaturation) {
        super(140, 70, 3,15, currentSaturation);
    }


    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public String toString() {
        return "Овца";
    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Sheep(saturation);
    }
}

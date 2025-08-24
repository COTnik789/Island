package com.island.domain.animal.species;

import com.island.domain.animal.Herbivore;
import com.island.domain.animal.BaseAnimal;

public class Horse extends BaseAnimal implements Herbivore {

    public Horse() {
        super(20,400,4,60);
    }

    public Horse(float currentSaturation) {
        super(20,400,4,60, currentSaturation);
    }


    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public String toString() {
        return "Лошадь";
    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Horse(saturation);
    }
}

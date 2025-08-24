package com.island.domain.animal.species;

import com.island.domain.animal.Herbivore;
import com.island.domain.animal.BaseAnimal;

public class Goat extends BaseAnimal implements Herbivore {

    public Goat() {
        super(140,60,3,10);
    }

    public Goat(float currentSaturation) {
        super(140,60,3,10, currentSaturation);
    }


    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public String toString() {
        return "Козел";
    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Goat(saturation);
    }
}

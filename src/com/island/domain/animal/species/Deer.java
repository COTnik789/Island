package com.island.domain.animal.species;

import com.island.domain.animal.Herbivore;
import com.island.domain.animal.BaseAnimal;

public class Deer extends BaseAnimal implements Herbivore {

    public Deer() {
        super(20, 300, 4, 100);
    }

    public Deer(float currentSaturation) {
        super(20, 300, 4, 100, currentSaturation);
    }

    @Override
    public String toString() {
        return "Лось";
    }

    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Deer(saturation);
    }
}

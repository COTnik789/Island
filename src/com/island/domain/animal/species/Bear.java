package com.island.domain.animal.species;

import com.island.domain.animal.Predator;
import com.island.domain.animal.BaseAnimal;

public class Bear extends BaseAnimal implements Predator {

    public Bear() {
        super(5, 500, 2, 80);
    }

    public Bear(float currentSaturation) {
        super(5, 500, 2, 80, currentSaturation);
    }

    @Override
    public void multiply() {
    }

    @Override
    public void go() {
    }

    @Override
    public String toString() {
        return "Медведь";
    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Bear(saturation);
    }
}
package com.island.domain.animal.species;

import com.island.domain.animal.Predator;
import com.island.domain.animal.BaseAnimal;

public class Wolf extends BaseAnimal implements Predator {

    public Wolf(float currentSaturation) {
        super(30,50,3,8, currentSaturation);
    }

    public Wolf() {
        super(30, 50.0f, 3, 8.0f);
    }

    @Override
    public String toString() {
        return "Волк";
    }

    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Wolf(saturation);
    }
}

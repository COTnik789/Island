package com.island.domain.animal.species;

import com.island.domain.animal.Predator;
import com.island.domain.animal.BaseAnimal;

public class Boa extends BaseAnimal implements Predator {

    public Boa() {
        super(30, 15, 1,3);
    }

    public Boa(float currentSaturation) {
        super(30, 15, 1,3, currentSaturation);
    }

    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public String toString() {
        return "Удав";
    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Boa(saturation);
    }
}

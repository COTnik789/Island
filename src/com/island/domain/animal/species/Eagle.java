package com.island.domain.animal.species;

import com.island.domain.animal.Predator;
import com.island.domain.animal.BaseAnimal;

public class Eagle extends BaseAnimal implements Predator {

    public Eagle() {
        super(20,6,3,2);
    }

    public Eagle(float currentSaturation) {
        super(20,6,3,2, currentSaturation);
    }

    @Override
    public String toString() {
        return "Орел";
    }


    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Eagle(saturation);
    }
}

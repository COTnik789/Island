package com.island.domain.animal.species;

import com.island.domain.animal.Predator;
import com.island.domain.animal.BaseAnimal;

public class Fox extends BaseAnimal implements Predator {

    public Fox() {
        super(30,8,2,2);
    }

    public Fox(float currentSaturation){
        super(30,8,2,2, currentSaturation);
    }

    @Override
    public String toString() {
        return "Лиса";
    }


    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Fox(saturation);
    }
}

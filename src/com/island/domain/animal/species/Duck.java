package com.island.domain.animal.species;

import com.island.domain.animal.Herbivore;
import com.island.domain.animal.BaseAnimal;

public class Duck extends BaseAnimal implements Herbivore {

    public Duck() {
        super(200,1,4,0.15f);
    }

    public Duck(float currentSaturation) {
        super(200,1,4,0.15f, currentSaturation);
    }


    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public String toString() {
        return "Утка";
    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Duck(saturation);
    }
}

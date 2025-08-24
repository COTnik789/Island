package com.island.domain.animal.species;

import com.island.domain.animal.Herbivore;
import com.island.domain.animal.BaseAnimal;

public class Caterpillar extends BaseAnimal implements Herbivore {

    public Caterpillar() {
        super(1000, 0.01f, 0,0.005f);
    }

    public Caterpillar(float currentSaturation) {
        super(1000, 0.01f, 0,0.005f, currentSaturation);
    }


    @Override
    public String toString() {
        return "Гусиница";
    }

    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Caterpillar(saturation);
    }
}

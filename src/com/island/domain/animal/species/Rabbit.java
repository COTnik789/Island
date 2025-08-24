package com.island.domain.animal.species;

import com.island.domain.animal.Herbivore;
import com.island.domain.animal.BaseAnimal;

public class Rabbit extends BaseAnimal implements Herbivore {

    public Rabbit() {
        super(150, 2, 2,0.45f);
    }

    public Rabbit(float currentSaturation) {
        super(150, 2, 2,0.45f, currentSaturation);
    }


    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public String toString() {
        return "Кролик";
    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Rabbit(saturation);
    }
}

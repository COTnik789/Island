package com.island.domain.animal.species;

import com.island.domain.animal.Herbivore;
import com.island.domain.animal.BaseAnimal;

public class Buffalo extends BaseAnimal implements Herbivore {

    public Buffalo(float currantSaturation) {
        super(10, 700, 3, 100, currantSaturation);
    }

    public Buffalo() {
        super(10, 700, 3, 100);
    }


    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Buffalo(saturation);
    }

    @Override
    public String toString() {
        return "Буйвол";
    }


    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }
}

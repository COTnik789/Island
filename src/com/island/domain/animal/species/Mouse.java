package com.island.domain.animal.species;

import com.island.domain.animal.Herbivore;
import com.island.domain.animal.BaseAnimal;

public class Mouse extends BaseAnimal implements Herbivore {

    public Mouse() {
        super(500, 0.05f,1,0.01f);
    }


    public Mouse(float currentSaturation) {
        super(500, 0.05f,1,0.01f, currentSaturation);
    }


    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public String toString() {
        return "Мышь";
    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new Mouse(saturation);
    }
}

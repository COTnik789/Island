package com.island.domain.animal.species;

import com.island.domain.animal.Predator;
import com.island.domain.animal.BaseAnimal;

public class WildBoar extends BaseAnimal implements Predator {

    public WildBoar() {
        super(50,400,2, 50);
    }

    public WildBoar(float currentSaturation) {
        super(50,400,2, 50, currentSaturation);
    }

    @Override
    public String toString() {
        return "Кабан";
    }

    @Override
    public void multiply() {

    }

    @Override
    public void go() {

    }

    @Override
    public BaseAnimal createNewWithSaturation(float saturation) {
        return new WildBoar(saturation);
    }
}

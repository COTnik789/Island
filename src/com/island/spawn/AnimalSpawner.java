package com.island.spawn;

import com.island.domain.animal.BaseAnimal;
import com.island.factory.AnimalFactory;

import java.util.function.Supplier;

public class AnimalSpawner extends AbstractSpawner<BaseAnimal> {
    private final AnimalFactory factory;

    public AnimalSpawner(AnimalFactory factory) {
        this.factory = factory;
    }

    @Override
    protected BaseAnimal createInstance(Supplier<BaseAnimal> supplier) {
        return factory.createAnimalWithRandomSaturation(supplier);
    }
}
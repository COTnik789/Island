package com.island.factory;

import com.island.domain.animal.BaseAnimal;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class AnimalFactory{
    private static final AnimalFactory INSTANCE = new AnimalFactory();

    public static AnimalFactory getInstance() {
        return INSTANCE;
    }

    private AnimalFactory() {}

    public BaseAnimal createAnimalWithRandomSaturation(Supplier<BaseAnimal> supplier) {
        BaseAnimal animal = supplier.get();
        float maxSaturation = animal.getMaxSaturation();
        float randomSaturation = maxSaturation * (0.2f + ThreadLocalRandom.current().nextFloat() * 0.3f); // 20–50%
        return animal.createNewWithSaturation(randomSaturation);
    }
}
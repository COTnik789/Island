package com.island.services;

import com.island.domain.animal.AnimalType;
import com.island.factory.AnimalFactory;
import com.island.domain.animal.BaseAnimal;
import com.island.domain.world.Cell;
import com.island.domain.world.Island;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalLifeService {
    private static final float DAILY_SATURATION_DECREASE = 0.15f; // 15% уменьшение
    private final Map<AnimalType, Long> eatenAnimals = new HashMap<>();
    private final Map<AnimalType, Long> bornAnimals = new HashMap<>();
    private long eatenPlants = 0;
    private long bornPlants = 0;
    private final AnimalFactory animalFactory = AnimalFactory.getInstance();

    public void processSaturationDecrease(Island island) {
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                synchronized (cell) {
                    List<BaseAnimal> animalsToRemove = new ArrayList<>();
                    for (BaseAnimal animal : cell.getAnimals()) {
                        float newSaturation = animal.getCurrentSaturation() - (animal.getMaxSaturation() * DAILY_SATURATION_DECREASE);
                        animal.setCurrentSaturation(Math.max(newSaturation, 0));
                        if (newSaturation <= 0) {
                            animalsToRemove.add(animal);
                        }
                    }
                    animalsToRemove.forEach(cell::removeAnimal);
                }
            }
        }
    }

    public static boolean isPredator(Class<?> animalClass) {
        AnimalType type = AnimalType.fromClass(animalClass);
        return switch (type) {
            case Bear, Boa, Eagle, Fox, Wolf -> true;
            default -> false;
        };
    }

    public BaseAnimal createAnimalWithRandomSaturation(java.util.function.Supplier<BaseAnimal> supplier) {
        return animalFactory.createAnimalWithRandomSaturation(supplier);
    }

    public Map<AnimalType, Long> getEatenAnimals() {
        return new HashMap<>(eatenAnimals);
    }

    public Map<AnimalType, Long> getBornAnimals() {
        return new HashMap<>(bornAnimals);
    }

    public long getEatenPlants() {
        return eatenPlants;
    }

    public long getBornPlants() {
        return bornPlants;
    }
}
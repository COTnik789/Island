package com.island.factory;

import com.island.domain.plant.Plant;

import java.util.List;
import java.util.function.Supplier;

public class PlantRegistry {
    private static final PlantRegistry INSTANCE = new PlantRegistry();
    private static final List<Supplier<Plant>> ALL_PLANTS = List.of(
            Plant::new
    );

    private PlantRegistry() {}

    public static PlantRegistry getInstance() {
        return INSTANCE;
    }

    public List<Supplier<Plant>> getAllPlants() {
        return ALL_PLANTS;
    }
}
package com.island.spawn;

import com.island.domain.plant.Plant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class PlantSpawner extends AbstractSpawner<Plant> {

    @Override
    protected Plant createInstance(Supplier<Plant> supplier) {
        return supplier.get();
    }

    @Override
    public List<Plant> spawn(List<Supplier<Plant>> suppliers) {
        List<Plant> result = new ArrayList<>();
        for (Supplier<Plant> supplier : suppliers) {
            Plant temp = supplier.get();
            int maxCount = temp.getMaxCountOnCell();
            int count = ThreadLocalRandom.current().nextInt(1, maxCount + 1); // Минимум 1 растение
            for (int i = 0; i < count; i++) {
                result.add(createInstance(supplier));
            }
        }
        return Collections.unmodifiableList(result);
    }
}
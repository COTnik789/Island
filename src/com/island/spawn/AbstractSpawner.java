package com.island.spawn;

import com.island.domain.animal.Locatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public abstract class AbstractSpawner<T extends Locatable> implements Spawner<T> {
    protected final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Override
    public List<T> spawn(List<Supplier<T>> suppliers) {
        List<T> result = new ArrayList<>();
        for (Supplier<T> supplier : suppliers) {
            T temp = supplier.get();
            int count = random.nextInt(temp.getMaxCountOnCell() + 1);
            for (int i = 0; i < count; i++) {
                result.add(createInstance(supplier));
            }
        }
        return Collections.unmodifiableList(result);
    }

    protected abstract T createInstance(Supplier<T> supplier);
}
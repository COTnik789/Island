
package com.island.factory;

import com.island.domain.animal.BaseAnimal;
import com.island.domain.animal.species.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;


public class AnimalRegistry {
    private static final AnimalRegistry INSTANCE = new AnimalRegistry();
    private final List<Supplier<BaseAnimal>> animals = new CopyOnWriteArrayList<>();

    private AnimalRegistry() {
        animals.addAll(List.of(
                Bear::new,
                Boa::new,
                Eagle::new,
                Fox::new,
                Wolf::new,
                WildBoar::new,
                Buffalo::new,
                Caterpillar::new,
                Deer::new,
                Duck::new,
                Goat::new,
                Horse::new,
                Mouse::new,
                Rabbit::new,
                Sheep::new
        ));
    }

    public static AnimalRegistry getInstance() {
        return INSTANCE;
    }

    public List<Supplier<BaseAnimal>> getAllAnimals() {
        return Collections.unmodifiableList(animals);
    }
}
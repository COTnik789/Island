package com.island.config;

import com.island.domain.animal.Animal;
import com.island.domain.animal.species.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class EatingProbabilityMap {
    private final Map<Class<? extends Animal>, Map<Class<? extends Animal>, Integer>> probabilities =
            new ConcurrentHashMap<>();

    private static final EatingProbabilityMap INSTANCE = new EatingProbabilityMap();
    public static EatingProbabilityMap getInstance() { return INSTANCE; }

    private EatingProbabilityMap() { load(); }

    private void load() {
        // Волк
        add(Wolf.class, Goat.class, 60);
        add(Wolf.class, Deer.class, 10);
        add(Wolf.class, Buffalo.class, 5);
        add(Wolf.class, Horse.class, 10);
        add(Wolf.class, Rabbit.class, 70);
        add(Wolf.class, Mouse.class, 90);
        add(Wolf.class, Sheep.class, 70);
        add(Wolf.class, Duck.class, 80);

        // Удав
        add(Boa.class, Rabbit.class, 50);
        add(Boa.class, Mouse.class, 90);
        add(Boa.class, Duck.class, 50);

        // Лиса
        add(Fox.class, Rabbit.class, 70);
        add(Fox.class, Deer.class, 5);
        add(Fox.class, Mouse.class, 90);
        add(Fox.class, Duck.class, 80);

        // Медведь
        add(Bear.class, Boa.class, 80);
        add(Bear.class, Deer.class, 30);
        add(Bear.class, Buffalo.class, 20);
        add(Bear.class, Goat.class, 70);
        add(Bear.class, Horse.class, 40);
        add(Bear.class, Rabbit.class, 90);
        add(Bear.class, Mouse.class, 90);
        add(Bear.class, Sheep.class, 70);
        add(Bear.class, Duck.class, 80);

        // Кабан
        add(WildBoar.class, Mouse.class, 80);
        add(WildBoar.class, Caterpillar.class, 90);
        add(WildBoar.class, Rabbit.class, 50);
        add(WildBoar.class, Duck.class, 30);

        // Орёл
        add(Eagle.class, Fox.class, 10);
        add(Eagle.class, Rabbit.class, 90);
        add(Eagle.class, Mouse.class, 90);
        add(Eagle.class, Duck.class, 85);

        // Утка — ест гусеницу
        add(Duck.class, Caterpillar.class, 20);
    }

    private void add(Class<? extends Animal> predator, Class<? extends Animal> prey, int chance) {
        probabilities.computeIfAbsent(predator, k -> new ConcurrentHashMap<>()).put(prey, chance);
    }

    public int getChance(Class<? extends Animal> predator, Class<? extends Animal> prey) {
        return probabilities.getOrDefault(predator, Collections.emptyMap()).getOrDefault(prey, 0);
    }

    public Map<Class<? extends Animal>, Integer> getProbabilitiesFor(Class<? extends Animal> predator) {
        return Collections.unmodifiableMap(
                probabilities.getOrDefault(predator, Collections.emptyMap())
        );
    }
}

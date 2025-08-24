package com.island.domain.world;



import com.island.domain.animal.BaseAnimal;
import com.island.domain.plant.Plant;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Cell {
    private final List<BaseAnimal> animals = new CopyOnWriteArrayList<>();
    private final List<Plant> plants = new CopyOnWriteArrayList<>();

    // Новый «почтовый ящик» для мигрантов + лок клетки
    private final Queue<BaseAnimal> incoming = new ConcurrentLinkedQueue<>();
    private final ReentrantLock lock = new ReentrantLock();

    public List<BaseAnimal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public List<Plant> getPlants() {
        return Collections.unmodifiableList(plants);
    }

    public void addAnimal(BaseAnimal animal) {
        lock.lock();
        try {
            animals.add(animal);
        } finally {
            lock.unlock();
        }
    }

    public void removeAnimal(BaseAnimal animal) {
        lock.lock();
        try {
            animals.remove(animal);
        } finally {
            lock.unlock();
        }
    }

    public boolean removePlantIfPresent(Plant plant) {
        lock.lock();
        try {
            return plants.remove(plant);
        } finally {
            lock.unlock();
        }
    }

    public boolean removeAnimalIfPresent(BaseAnimal animal) {
        lock.lock();
        try {
            return animals.remove(animal);
        } finally {
            lock.unlock();
        }
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public void removePlant(Plant plant) {
        plants.remove(plant);
    }

    // Снимок для безопасной итерации в фазах
    public List<BaseAnimal> animalsSnapshot() {
        lock.lock();
        try {
            return new ArrayList<>(animals);
        } finally {
            lock.unlock();
        }
    }

    // Положить мигранта в очередь (вызывается в фазе движения)
    public void enqueueIncoming(BaseAnimal animal) {
        incoming.offer(animal);
    }

    // Коммит миграций (вызывается централизованно после фазы движения)
    public void commitIncoming() {
        lock.lock();
        try {
            BaseAnimal a;
            while ((a = incoming.poll()) != null) {
                animals.add(a);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Cell{animals=" + animals + ", plants=" + plants + '}';
    }
}

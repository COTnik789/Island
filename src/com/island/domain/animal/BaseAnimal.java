package com.island.domain.animal;

import java.util.concurrent.locks.ReentrantLock;

public abstract class BaseAnimal implements Animal, Movable, Reproducible {
    private final float weight;
    private final int maxCountOnCell;
    private final int speed;
    private float currentSaturation;
    private final float maxSaturation;
    private final ReentrantLock lock = new ReentrantLock();

    protected BaseAnimal(int maxCountOnCell, float weight, int speed, float maxSaturation, float currentSaturation) {
        this.maxCountOnCell = maxCountOnCell;
        this.weight = weight;
        this.speed = speed;
        this.maxSaturation = maxSaturation;
        this.currentSaturation = currentSaturation;
    }

    protected BaseAnimal(int maxCountOnCell, float weight, int speed, float maxSaturation) {
        this(maxCountOnCell, weight, speed, maxSaturation, 0f);
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public int getMaxCountOnCell() {
        return maxCountOnCell;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public float getCurrentSaturation() {
        lock.lock();
        try {
            return currentSaturation;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public float getMaxSaturation() {
        return maxSaturation;
    }

    @Override
    public void setCurrentSaturation(float currentSaturation) {
        lock.lock();
        try {
            this.currentSaturation = Math.max(0, Math.min(currentSaturation, maxSaturation));
        } finally {
            lock.unlock();
        }
    }

    public void increaseSaturation(float foodWeight) {
        lock.lock();
        try {
            currentSaturation = Math.min(currentSaturation + foodWeight, maxSaturation);
        } finally {
            lock.unlock();
        }
    }

    public abstract BaseAnimal createNewWithSaturation(float saturation);
}
package com.island.domain.plant;

import com.island.domain.animal.Locatable;

public class Plant implements Locatable {
    private final float weight;
    private final int maxCountOnCell;

    public Plant() {
        this.weight = 1;
        this.maxCountOnCell = 2000;
    }

    public float getWeight() {
        return weight;
    }

    public int getMaxCountOnCell() {
        return maxCountOnCell;
    }

    public Plant multiply() {
        return new Plant();
    }

    @Override
    public String toString() {
        return "Растение";
    }
}
package com.island.domain.animal;


public interface Animal extends Locatable {

    float getWeight();

    float getCurrentSaturation();

    void setCurrentSaturation(float currentSaturation);

    float getMaxSaturation();
}
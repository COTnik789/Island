/**
 * Сервис для управления операциями на острове.
 */
package com.island.services;

import com.island.domain.animal.BaseAnimal;
import com.island.domain.plant.Plant;
import com.island.domain.world.Island;

public class IslandService {

    public void addAnimal(Island island, BaseAnimal animal, int row, int col) {
        if (isValidCoordinate(island, row, col)) {
            island.getCell(row, col).addAnimal(animal);
        }
    }

    public void removeAnimal(Island island, BaseAnimal animal, int row, int col) {
        if (isValidCoordinate(island, row, col)) {
            island.getCell(row, col).removeAnimal(animal);
        }
    }

    public void addPlant(Island island, Plant plant, int row, int col) {
        if (isValidCoordinate(island, row, col)) {
            island.getCell(row, col).addPlant(plant);
        }
    }

    private boolean isValidCoordinate(Island island, int row, int col) {
        return row >= 0 && row < island.getRows() && col >= 0 && col < island.getCols();
    }
}
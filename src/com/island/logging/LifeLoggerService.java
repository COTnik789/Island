package com.island.logging;

import com.island.domain.animal.AnimalType;
import com.island.domain.world.Island;

public interface LifeLoggerService {
    interface LogObserver { void onLog(LogResult log); }

    void registerObserver(LogObserver observer);
    void unregisterObserver(LogObserver observer);

    void logSpawn(int row, int col, AnimalType type, int count);
    void logPlantSpawn(int row, int col, int count);

    void logMigration(int fromRow, int fromCol, int toRow, int toCol, AnimalType type);

    void logEating(int row, int col, AnimalType victimType, int plants, int animals);
    void logReproduction(int row, int col, AnimalType type, int count);
    void logPlantBirth(int row, int col, int count);

    void logAnimalCount(Island island);
    void logPlantCount(Island island);

    LogResult flushAndGetLogResult();
}

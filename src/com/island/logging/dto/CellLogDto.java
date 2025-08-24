package com.island.logging.dto;

import com.island.logging.LogResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CellLogDto implements LogResult {
    private final int row;
    private final int col;
    private final List<AnimalDto> animals;
    private final Map<String, Integer> newborns = new ConcurrentHashMap<>();
    private final Map<String, Integer> migrationsIn = new ConcurrentHashMap<>();
    private final Map<String, Integer> migrationsOut = new ConcurrentHashMap<>();
    private final Map<String, Integer> eatenAnimalsByType = new ConcurrentHashMap<>();
    private final AtomicInteger eatenPlants = new AtomicInteger();
    private final AtomicInteger bornPlants = new AtomicInteger();

    public CellLogDto(int row, int col, List<AnimalDto> animals) {
        this.row = row;
        this.col = col;
        this.animals = List.copyOf(animals);
    }

    public int getRow() { return row; }

    public int getCol() { return col; }

    public List<AnimalDto> getAnimals() { return Collections.unmodifiableList(animals); }

    public void incrementEatenAnimals(String type, int count) { eatenAnimalsByType.merge(type, count, Integer::sum); }

    public void incrementEatenPlants(int count) { eatenPlants.addAndGet(count); }

    public void addNewborns(String type, int count) {
        newborns.merge(type, count, Integer::sum);
    }

    public void addMigrationIn(String type) {
        migrationsIn.merge(type, 1, Integer::sum);
    }

    public void addMigrationOut(String type) {
        migrationsOut.merge(type, 1, Integer::sum);
    }

    public void addBornPlants(int count) {
        bornPlants.addAndGet(count);
    }

    public Map<String, Integer> getEatenAnimalsByType() { return Collections.unmodifiableMap(eatenAnimalsByType); }

    public int getEatenPlants() { return eatenPlants.get(); }

    public Map<String, Integer> getNewborns() { return Collections.unmodifiableMap(newborns); }

    public int getBornPlants() { return bornPlants.get(); }
}
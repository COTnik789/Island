package com.island.domain.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Island {
    private final Map<String, Cell> cells = new ConcurrentHashMap<>();
    private final int rows;
    private final int cols;

    public Island(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells.put(key(r, c), new Cell());
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell getCell(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Invalid coordinates: row=" + row + ", col=" + col);
        }
        return cells.get(key(row, col));
    }

    public List<Cell> getAllCells() {
        return new ArrayList<>(cells.values());
    }

    public void commitAllMigrations() {
        for (Cell cell : cells.values()) {
            cell.commitIncoming();
        }
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private String key(int row, int col) {
        return row + ":" + col;
    }
}

import data.Animal;
import data.Plant;

import java.util.ArrayList;
import java.util.List;

public class Island {
    private final Cell[][] grid;

    public Island(int rows, int cols) {
        grid = new Cell[rows][cols]();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    public List<Animal>[][] getGrid() {
        return grid;
    }

    public void addAnimal(int row, int col, Animal animal){
        grid[row][col].add(animal);
    }

    public void removeAnimal(int row, int col, Animal animal){
        grid[row][col].remove(animal);
    }

    public List<Animal> getAnimals(int row, int col){
        return grid[row][col];
    }
}

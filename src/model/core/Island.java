package model.core;

public class Island {
    private final Cell[][] cell;

    public Island(int rows, int cols) {
        cell = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cell[i][j] = new Cell();
            }
        }
    }

    public Cell getCell(int row, int col) {
        return cell[row][col];
    }

    public int getRows(){
        return cell.length;
    }

    public int getCols() {
        return cell[0].length;
    }
}

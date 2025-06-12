import factory.AnimalRegistry;
import factory.PlantRegistry;
import service.Simulation;
import model.core.Cell;

public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation(1, 1);
        simulation.populateIsland(AnimalRegistry.getAllAnimals(), PlantRegistry.getAllAnimals());
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 1; j++) {
                Cell cell = simulation.getIsland().getCell(i, j);
                System.out.println(cell.toString());
            }
        }
    }
}
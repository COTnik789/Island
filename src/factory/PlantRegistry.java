package factory;

import model.plant.Plant;

import java.util.List;
import java.util.function.Supplier;

public class PlantRegistry {
    private static final List<Supplier<Plant>> ALL_PLANT = List.of(
            Plant::new
    );

    public static List<Supplier<Plant>> getAllAnimals(){
        return ALL_PLANT;
    }
}

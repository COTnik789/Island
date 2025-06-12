package factory;

import model.plant.Plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class PlantSpawner implements Spawner<Plant>{

    private final Random random = new Random();

    @Override
    public List<Plant> spawn(List<Supplier<Plant>> suppliers) {
        List<Plant> result = new ArrayList<>();

        for (Supplier<Plant> supplier : suppliers) {
            Plant temp = supplier.get();
            int count = random.nextInt(temp.getMaxCountOnCell() + 1);
            for (int i = 0; i < count; i++) {
                result.add(supplier.get());
            }
        }
        return result;
    }
}

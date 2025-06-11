package factory;

import data.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class AnimalSpawner {

    private final Random random = new Random();

    public List<Animal> spawnerRandomAnimals(List<Supplier<Animal>> supplierList){
        List<Animal> result = new ArrayList<>();

        for (Supplier<Animal> supplier : supplierList){
            Animal animal = supplier.get();
            int count = random.nextInt(animal.getMaxCountOnCell() + 1);
            for (int i = 0; i < count; i++) {
                result.add(supplier.get());
            }
        }
        return result;
    }
}

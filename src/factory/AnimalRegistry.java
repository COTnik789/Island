package factory;

import data.Animal;
import data.predators.*;
import data.herbivores.*;

import java.util.List;
import java.util.function.Supplier;

public class AnimalRegistry  {
    private static final List<Supplier<Animal>> ALL_ANIMALS = List.of(
            Bear::new,
            Boa::new,
            Eagle::new,
            Fox::new,
            WildBoar::new,
            Wolf::new,
            Buffalo::new,
            Caterpillar::new,
            Deer::new,
            Duck::new,
            Goat::new,
            Horse::new,
            Mouse::new,
            Rabbit::new,
            Sheep::new
    );

    public static List<Supplier<Animal>> getAllAnimals(){
        return ALL_ANIMALS;
    }
}

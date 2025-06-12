package factory;

import java.util.List;
import java.util.function.Supplier;

public interface Spawner<T> {
    List<T> spawn(List<Supplier<T>> suppliers);
}

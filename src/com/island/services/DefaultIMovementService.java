package com.island.services;

import com.island.domain.animal.AnimalType;
import com.island.domain.animal.BaseAnimal;
import com.island.domain.animal.Movable;
import com.island.domain.world.Cell;
import com.island.domain.world.Island;
import com.island.logging.LifeLoggerService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Predicate;

import static java.util.concurrent.ThreadLocalRandom.current;

public class DefaultIMovementService implements IMovementService {
    private static final double MOVE_PROBABILITY = 0.6; // не все двигаются каждый тик

    private final IslandService islandService;
    private final LifeLoggerService logger;
    private final ExecutorService executor =
            Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors()));

    public DefaultIMovementService(IslandService islandService, LifeLoggerService logger) {
        this.islandService = islandService;
        this.logger = logger;
    }

    @Override
    public void processMovement(Island island) {
        List<Future<?>> futures = new ArrayList<>();
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                final int r = row, c = col;
                futures.add(executor.submit(() -> moveFromCell(island, r, c)));
            }
        }
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return;
            } catch (ExecutionException ee) {
                throw new RuntimeException("Movement task failed", ee.getCause());
            }
        }
        island.commitAllMigrations();
    }

    public void shutdown() {
        executor.shutdownNow();
    }

    private void moveFromCell(Island island, int row, int col) {
        Cell from = island.getCell(row, col);
        var snapshot = from.animalsSnapshot();
        if (snapshot.isEmpty()) return;

        List<BaseAnimal> toRemove = new ArrayList<>();

        for (BaseAnimal a : snapshot) {
            if (!(a instanceof Movable)) continue;

            // не каждая особь движется каждый тик
            if (current().nextDouble() > MOVE_PROBABILITY) continue;

            int speed = ((Movable) a).getSpeed();
            if (speed <= 0) continue;

            // выбираем цель с учётом границ и лимита по виду в целевой клетке
            int[] target = pickTarget(island, row, col, speed,
                    toCell -> canEnter(toCell, a.getClass(), a.getMaxCountOnCell()));
            if (target == null) continue;

            int newRow = target[0], newCol = target[1];
            Cell to = island.getCell(newRow, newCol);

            // помечаем к удалению из источника и ставим в очередь целевой клетки
            toRemove.add(a);
            to.enqueueIncoming(a);
            logger.logMigration(row, col, newRow, newCol, AnimalType.fromClass(a.getClass()));
        }

        for (BaseAnimal a : toRemove) {
            from.removeAnimal(a);
        }
    }

    private boolean canEnter(Cell to, Class<?> species, int maxOnCell) {
        // приблизительная проверка по текущему состоянию клетки
        long same = to.animalsSnapshot().stream()
                .filter(x -> x.getClass().equals(species))
                .count();
        return same < maxOnCell;
    }

    private int[] pickTarget(Island island, int row, int col, int speed, Predicate<Cell> accept) {
        // до 8 направлений, длина шага 1..speed (Чебышёв)
        for (int tries = 0; tries < 8; tries++) {
            int dx = rndStep(speed);
            int dy = rndStep(speed);
            if (dx == 0 && dy == 0) continue;

            int nr = row + dx;
            int nc = col + dy;
            if (nr < 0 || nr >= island.getRows() || nc < 0 || nc >= island.getCols()) continue;

            Cell to = island.getCell(nr, nc);
            if (accept.test(to)) {
                return new int[]{nr, nc};
            }
        }
        return null;
    }

    private int rndStep(int speed) {
        int mag = current().nextInt(0, speed + 1); // 0..speed
        int sign = mag == 0 ? 0 : (current().nextBoolean() ? 1 : -1);
        return mag * sign;
    }
}

package com.island.logging;

import com.island.logging.dto.AnimalDto;
import com.island.logging.dto.CellLogDto;
import com.island.logging.dto.IslandLogDto;

import java.util.Comparator;
import java.util.Map;

public class IslandLogPrinterTask implements LifeLoggerService.LogObserver {

    @Override
    public void onLog(LogResult log) {
        if (!(log instanceof IslandLogDto dto)) return;

        System.out.println("\n=== DAY REPORT ===");

        // 1) По клеткам
        dto.getCellLogs().stream()
                .sorted(Comparator.comparingInt(CellLogDto::getRow).thenComparingInt(CellLogDto::getCol))
                .forEach(cell -> {
                    StringBuilder line = new StringBuilder();
                    line.append("Cell[").append(cell.getRow()).append(",").append(cell.getCol()).append("]: ");
                    if (cell.getAnimals().isEmpty()) {
                        line.append("—");
                    } else {
                        boolean first = true;
                        for (AnimalDto a : cell.getAnimals()) {
                            if (!first) line.append(", ");
                            line.append(a.type()).append("=").append(a.count());
                            first = false;
                        }
                    }
                    System.out.println(line);
                });

        // 2) Сводка за тик
        System.out.println("\n--- Summary ---");

        Map<String, Integer> totalAnimals = dto.getTotalAnimalCount();
        if (!totalAnimals.isEmpty()) {
            System.out.print("Animals total: ");
            System.out.println(mapToLine(totalAnimals));
        }

        Map<String, Integer> bornAnimals = dto.getTotalBornAnimals();
        if (!bornAnimals.isEmpty()) {
            System.out.print("Born animals:   ");
            System.out.println(mapToLine(bornAnimals));
        }

        int eatenAnimals = dto.getTotalEatenAnimals();
        System.out.println("Eaten animals:  " + eatenAnimals);

        int eatenPlants = dto.getTotalEatenPlants();
        System.out.println("Eaten plants:   " + eatenPlants);

        int bornPlants = dto.getTotalBornPlants();
        System.out.println("Born plants:    " + bornPlants);

        System.out.println("-----------------\n");
    }

    private static String mapToLine(Map<String, Integer> m) {
        return m.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + ", " + b)
                .orElse("—");
    }

    public void stop() {

    }
}

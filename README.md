# Island Simulation

Многопоточная симуляция экосистемы острова на **чистой Java** (без Maven/Gradle).
Животные и растения взаимодействуют: хищники охотятся, травоядные едят растения, все двигаются, размножаются и умирают от голода. Есть подробное логирование по тикам.

---

## Возможности
- Реалистичное взаимодействие видов:
  - Вероятности охоты заданы матрицей (`EatingProbabilityMap`).
  - Насыщение хищников зависит от **веса жертвы**, травоядных — от **веса растения**.
  - Без «перекорма»: прирост сытости ограничен текущей потребностью.
- Многопоточность:
  - Еда, движение и размножение выполняются параллельно через `ExecutorService`, с ожиданием завершения задач.
  - Миграции коммитятся централизованно во избежание конфликтов.
- Логирование:
  - По каждой клетке: количество животных по видам.
  - Сводка за тик: кто родился/съеден (животные/растения).
- Архитектура:
  - Пакеты по слоям: `domain`, `services`, `logging`, `factory`, `config`, `spawn`, `sim`.
  - Принципы **SOLID / DRY / KISS / YAGNI**.
  - Паттерны: **Singleton**, **Factory Method**, **Strategy**, **Observer**, **Facade** (через `Simulation`).

---

## Как запустить
### IntelliJ IDEA
- Открыть проект как обычный Java-проект.
- Убедиться, что модульная папка — `src/`.
- Запустить конфигурацию: `app.Main`.

### CLI (Linux/macOS)
```bash
javac -d out $(find src -name "*.java")
java -cp out app.Main
```

### CLI (Windows PowerShell)
```powershell
Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName } | `
  javac -d out @-
java -cp out app.Main
```

---

## Жизненный цикл “дня”
Порядок вызовов в `Simulation`:
1. **Еда** (`DefaultEatingService.processEating`)
2. **Движение** (`DefaultMovementService.processMovement`)  
   — после завершения задач: `island.commitAllMigrations()`
3. **Размножение** (`DefaultReproductionService.processReproduction`)  
   — растения: `born = min(plantsNow / 2, free)` (лимит берётся из `Plant.getMaxCountOnCell()`)  
   — животные: по парам, с учётом сытости и лимита на клетке
4. **Естественная убыть/голод** (`AnimalLifeService.processSaturationDecrease`)
5. **Логирование** (`DefaultLifeLoggerService.logAnimalCount/logPlantCount` → `flushAndGetLogResult()`)

---

## Правила модели (ключевые моменты)
### Поедание (`DefaultEatingService`)
- **Травоядные**: едят растения из клетки, прирост сытости = `min(need, plant.getWeight())`.
  Если параллельно кто-то уже съел растение — попытка пропускается (через `Cell.removePlantIfPresent(...)`).
- **Хищники**: выбирают подходящую жертву с шансом из `EatingProbabilityMap`.
  Прирост сытости = `min(need, prey.getWeight() * 0.5f)`. Удаление через `Cell.removeAnimalIfPresent(...)`.

### Движение (`DefaultMovementService`)
- Перемещение в пределах скорости.
- Миграции добавляются в очередь и коммитятся централизованно.
- Нельзя переполнить клетку по виду (проверка лимита).

### Размножение (`DefaultReproductionService`)
- **Растения**: каждый тик `born = min(current/2, free)`, где `free` вычисляется из `Plant.getMaxCountOnCell()`.
- **Животные**: учитываются только «плодные» (сытость ≥ порога), `offspring = min(fertile/2, freeSlots)`.
  Для хищников требуется минимальное число потенциальной добычи в клетке (для устойчивости).

---

## Логирование
`IslandLogPrinterTask` печатает за тик:
- по клеткам: `Cell[r,c]: Wolf=3, Rabbit=12, ...`
- сводка:
  - `Animals total`,
  - `Born animals` (по видам),
  - `Eaten animals` (сумма),
  - `Eaten plants`, `Born plants`.

- Логгер сбрасывает тик-метрики после `flushAndGetLogResult()`, чтобы отчёты были «за текущий день».

---

## ️ Где менять “баланс”
- Порог сытости для размножения, ограничения хищников — `DefaultReproductionService`.
- Вероятность/логика движения — `DefaultMovementService`.
- Скорость убывания сытости/смерти — `AnimalLifeService`.
- Вероятности охоты — `EatingProbabilityMap` (по условию задачи не менялись).

---

## Цели проекта
- Практика многопоточности и конкурентного доступа к общим структурам.
- Демонстрация архитектуры и паттернов в чистой Java.
- Портфолио-проект для backend-разработки.

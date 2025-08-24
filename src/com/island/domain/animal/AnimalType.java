package com.island.domain.animal;

public enum AnimalType {
    Bear("Медведь"),
    Boa("Удав"),
    Eagle("Орел"),
    Fox("Лиса"),
    Wolf("Волк"),
    WildBoar("Кабан"),
    Buffalo("Буйвол"),
    Sheep("Овца"),
    Rabbit("Кролик"),
    Deer("Олень"),
    Goat("Коза"),
    Horse("Лошадь"),
    Duck("Утка"),
    Mouse("Мышь"),
    Caterpillar("Гусеница");

    private final String displayName;

    AnimalType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AnimalType fromClass(Class<?> animalClass) {
        String name = animalClass.getSimpleName();
        for (AnimalType t : values()) {
            if (t.name().equalsIgnoreCase(name)) return t;
        }
        throw new IllegalArgumentException("Unknown animal class: " + name);
    }
}

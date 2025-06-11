package data.predators;

import data.herbivores.Herbivores;

public class WildBoar extends Predator {
    public WildBoar() {
        super(50,400,2,50);
    }

    @Override
    public String toString() {
        return "Кабан";
    }
}

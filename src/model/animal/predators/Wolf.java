package model.animal.predators;

public class Wolf extends Predator{
    public Wolf() {
        super(30,50,3,8);
    }

    @Override
    public String toString() {
        return "Волк";
    }
}

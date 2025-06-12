package model.plant;

public class Plant {
    private float weight;
    private int maxCountOnCell;

    public Plant() {
        this.weight = 1;
        this.maxCountOnCell = 200;
    }

    @Override
    public String toString() {
        return "Растение";
    }

    public float getWeight() {
        return weight;
    }

    public int getMaxCountOnCell() {
        return maxCountOnCell;
    }

    public Plant multiply(){
        return new Plant();
    }
}

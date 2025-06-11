package data;

public class Plant {
    private float weight;
    private double maxCountOnCell;

    public Plant() {
        this.weight = 1;
        this.maxCountOnCell = 200;
    }

    public Plant multiply(){
        return new Plant();
    }
}

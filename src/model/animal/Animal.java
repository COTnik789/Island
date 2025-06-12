package model.animal;

public abstract class Animal {
    private float weight;
    private int maxCountOnCell;
    private int speed;
    private float saturation;

    protected Animal(int maxCountOnCell, float weight, int speed, float saturation) {
        this.maxCountOnCell = maxCountOnCell;
        this.weight = weight;
        this.speed = speed;
        this.saturation = saturation;
    }

    public float getWeight() {
        return weight;
    }

    public int getMaxCountOnCell() {
        return maxCountOnCell;
    }

    public int getSpeed() {
        return speed;
    }

    public float getCountFood() {
        return saturation;
    }

    public abstract void eat();
    public abstract void multiply();
    public abstract void go();
}


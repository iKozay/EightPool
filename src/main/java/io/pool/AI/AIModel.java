package io.pool.AI;
import io.pool.controller.PoolCueController;

public class AIModel {
    public final static int EASY_AI=10;
    public final static int MEDIUM_AI=50;
    public final static int HARD_AI=100;

    private double power;
    private double rotation;

    public AIModel() {
        this.power = (Math.random() * PoolCueController.MAX_DISTANCE + 1);
        this.rotation = (Math.random()*(2*Math.PI))-Math.PI;
    }

    public double getPower() {
        return power;
    }

    public double getRotation() {
        return rotation;
    }
}

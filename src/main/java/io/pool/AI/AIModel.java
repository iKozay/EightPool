package io.pool.AI;
import io.pool.controller.PoolCueController;

import java.util.Random;

public class AIModel {
    public final static int EASY_AI=10;
    public final static int MEDIUM_AI=50;
    public final static int HARD_AI=100;

    private double power;
    private double rotation;

    public AIModel() {
        Random rnd = new Random();
        this.power = rnd.nextInt(PoolCueController.MAX_DISTANCE-30)+31;
        this.rotation = rnd.nextInt(360)+1;
    }

    public double getPower() {
        return power;
    }

    public double getRotation() {
        return rotation;
    }

    @Override
    public String toString() {
        return "AIModel{" +
                "power=" + power +
                ", rotation=" + rotation +
                '}';
    }
}

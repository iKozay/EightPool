package io.pool.AI;

import java.util.Random;

public class AIModel {

    /**
     * We will put all the constants here as well as
     * the randomiser for the shots.
     *
     */

    private final int MAX_POWER = 100;
    private final int MAX_ANGLE = 360;
    private int score;
    private final int reps = 100;
    private double finalPower;
    private double finalAngle;


    public AIModel() {
////
        play();

    }


    public void play() {

        for (int i=0; i < reps; i++) {

            Random rnd = new Random();

            double testPower;
            do {

                testPower = rnd.nextDouble();
            } while (testPower >= MAX_POWER);


        }

    }


}

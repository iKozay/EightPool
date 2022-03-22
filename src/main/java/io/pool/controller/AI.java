package io.pool.controller;

import java.util.Random;

public class AI {

    private final int MAX_POWER = 100;
    private final int MAX_ANGLE = 360;
    private int score;
    private final int reps = 100;
    private double finalPower;
    private double finalAngle;


    public AI() {

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

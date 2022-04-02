package io.pool.controller;

import io.pool.eightpool.ResourcesLoader;

public class SoundController {
    public static void BallsCollide(){
        ResourcesLoader.soundFiles.get(0).play();
    }
    public static void BallInHole(){
        ResourcesLoader.soundFiles.get(1).play();
    }
    public static void BallBounce(){
        ResourcesLoader.soundFiles.get(2).play();
    }
    public static void BallHit(){
        ResourcesLoader.soundFiles.get(3).play();
    }
}

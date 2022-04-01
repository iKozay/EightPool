package io.pool.controller;

import io.pool.eightpool.ResourcesLoader;

public class SoundController {
    protected static void BallsCollide(){
        ResourcesLoader.soundFiles.get(0).play();
    }
    protected static void BallInHole(){
        ResourcesLoader.soundFiles.get(1).play();
    }
    protected static void BallBounce(){
        ResourcesLoader.soundFiles.get(2).play();
    }
    protected static void BallHit(){
        ResourcesLoader.soundFiles.get(3).play();
    }
}

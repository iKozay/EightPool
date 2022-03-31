package io.pool.controller;

import io.pool.eightpool.ResourcesLoader;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class SoundController {
    protected static void BallsCollide(){
        playAudio(ResourcesLoader.soundFiles.get(0));
    }
    private static void playAudio(AudioInputStream audioFile){
        try {
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioFile);
            audioClip.loop(1);
            audioClip.start();
            audioClip.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

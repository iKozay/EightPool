package io.pool.eightpool;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourcesLoader {
    public static final List<Image> ballImages = new ArrayList<>();
    public static final List<Image> tableImages = new ArrayList<>();
    public static final List<Image> iconImages = new ArrayList<>();
    public static final List<Image> tableTextures = new ArrayList<>();
    public static final List<Image> poolCueImages = new ArrayList<>();
    public static final List<AudioClip> soundFiles = new ArrayList<>();

    public static void load(){
        try {
            loadBallImages();
            loadTableImages();
            loadIcons();
            loadTableTextures();
            loadPoolCues();
            loadSoundFiles();
        } catch (MalformedURLException | FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    private static void loadBallImages() throws MalformedURLException {
        for (int i = 1; i <= 16; i++) {
            if (i != 16) {
                
                ballImages.add(new Image(ResourcesLoader.class.getResource("/billiards3D/ball" + i + ".jpg").toExternalForm()));
            }else{
                ballImages.add(new Image(ResourcesLoader.class.getResource("/billiards3D/white.jpg").toExternalForm()));
            }
        }
    }
    private static void loadTableImages() throws MalformedURLException{
        for(int i=0;i<8;i++){
            tableImages.add(new Image(ResourcesLoader.class.getResource("/tableImage/" + (i+1) + ".png").toExternalForm()));
        }
    }
    private static void loadIcons() throws MalformedURLException{
        Image menu = new Image(ResourcesLoader.class.getResource("/UI icons/menu_white.png").toExternalForm());
        Image back1 = new Image(ResourcesLoader.class.getResource("/UI icons/simpleArrowLeft_white.png").toExternalForm());
        Image next1 = new Image(ResourcesLoader.class.getResource("/UI icons/simpleArrowRight_white.png").toExternalForm());
        Image back2 = new Image(ResourcesLoader.class.getResource("/UI icons/left_back_Yellow.png").toExternalForm());
        Image next2 = new Image(ResourcesLoader.class.getResource("/UI icons/right_next_Yellow.png").toExternalForm());
        Image leaveImage = new Image(ResourcesLoader.class.getResource("/UI icons/arrow.png").toExternalForm());
        iconImages.addAll(Arrays.asList(menu,back1,next1,back2,next2,leaveImage));
    }
    private static void loadTableTextures() throws MalformedURLException{
        tableTextures.add(new Image(ResourcesLoader.class.getResource("/MainMenu/TableTexture.jpg").toExternalForm()));
        tableTextures.add(new Image(ResourcesLoader.class.getResource("/MainMenu/RedTableTexture.jpg").toExternalForm()));
        tableTextures.add(new Image(ResourcesLoader.class.getResource("/MainMenu/BlueTableTexture.jpg").toExternalForm()));
    }
    private static void loadPoolCues() throws MalformedURLException{
        for(int i=1;i<=6;i++){
            poolCueImages.add(new Image(ResourcesLoader.class.getResource("/cueImages/cue" + i + ".png").toExternalForm()));

            
        }

    }
    private static void loadSoundFiles() throws IOException, UnsupportedAudioFileException {
        soundFiles.add(new AudioClip(ResourcesLoader.class.getResource("/SoundFiles/BallsCollide.wav").toExternalForm()));
        soundFiles.add(new AudioClip(ResourcesLoader.class.getResource("/SoundFiles/Hole.wav").toExternalForm()));
        soundFiles.add(new AudioClip(ResourcesLoader.class.getResource("/SoundFiles/Side.wav").toExternalForm()));
        soundFiles.add(new AudioClip(ResourcesLoader.class.getResource("/SoundFiles/Strike.wav").toExternalForm()));
    }
}

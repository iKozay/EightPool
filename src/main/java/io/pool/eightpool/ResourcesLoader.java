package io.pool.eightpool;

import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class ResourcesLoader {
    public static ArrayList<Image> ballImages = new ArrayList<>();

    public static void load(){
        try {
            loadBallImages();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    private static void loadBallImages() throws MalformedURLException {
        for (int i = 1; i <= 16; i++) {
            if (i != 16) {
                ballImages.add(new Image(new File("src/main/resources/billiards3D/ball" + i + ".jpg").toURI().toURL().toExternalForm()));
            }else{
                ballImages.add(new Image(new File("src/main/resources/billiards3D/white.jpg").toURI().toURL().toExternalForm()));
            }
        }
    }
}

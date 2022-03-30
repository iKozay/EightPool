package io.pool.eightpool;

import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ResourcesLoader {
    public static ArrayList<Image> ballImages = new ArrayList<>();
    public static ArrayList<Image> tableImages = new ArrayList<>();
    public static ArrayList<Image> iconImages = new ArrayList<>();

    public static void load(){
        try {
            loadBallImages();
            loadTableImages();
            loadIcons();
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
    private static void loadTableImages() throws MalformedURLException{
        for(int i=0;i<8;i++){
            tableImages.add(new Image(new File("src/main/resources/tableImage/" + (i+1) + ".png").toURI().toURL().toExternalForm()));
        }
    }
    private static void loadIcons() throws MalformedURLException{
        Image menu = new Image(new File("src/main/resources/UI icons/menu_white.png").toURI().toURL().toExternalForm());
        Image back1 = new Image(new File("src/main/resources/UI icons/simpleArrowLeft_white.png").toURI().toURL().toExternalForm());
        Image next1 = new Image(new File("src/main/resources/UI icons/simpleArrowRight_white.png").toURI().toURL().toExternalForm());
        Image back2 = new Image(new File("src/main/resources/UI icons/left_back_Yellow.png").toURI().toURL().toExternalForm());
        Image next2 = new Image(new File("src/main/resources/UI icons/right_next_Yellow.png").toURI().toURL().toExternalForm());
        Image leaveImage = new Image(new File("src/main/resources/UI icons/arrow.png").toURI().toURL().toExternalForm());
        iconImages.addAll(Arrays.asList(menu,back1,next1,back2,next2,leaveImage));
    }
}

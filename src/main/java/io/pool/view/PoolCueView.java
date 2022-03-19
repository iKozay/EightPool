package io.pool.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.MalformedURLException;

public class PoolCueView {

    /** The cylinder that will represent the pool cue */
    private static ImageView cue;

    double previousAngle = 0;

    public PoolCueView() {
        try {
            cue = new ImageView(new Image(new File("src/main/resources/cueImages/cue1.png").toURI().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        cue.setX(-100);
        cue.setY(-100);
    }

    public ImageView getCue() {
        return cue;
    }

    public double getPreviousAngle() {
        return previousAngle;
    }

    public void setPreviousAngle(double angle) {
        this.previousAngle = angle;
    }


}

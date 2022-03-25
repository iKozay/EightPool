package io.pool.view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.net.MalformedURLException;

public class PoolCueView {

    /** The cylinder that will represent the pool cue */
    private static ImageView cue;
    private Line poolLine;

    double previousAngle = 0;

    public PoolCueView() {
        try {
            cue = new ImageView(new Image(new File("src/main/resources/cueImages/cue1.png").toURI().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        cue.setX(0);
        cue.setY(0);
        poolLine = new Line();
        poolLine.setStroke(Color.WHITE);
        poolLine.setStrokeWidth(3);
        poolLine.setFill(Color.WHITE);
        this.testLineView();
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

    public Line getPoolLine() {
        return poolLine;
    }

    public Line testLineView(){
        Line testLine = new Line();
        testLine.setStrokeWidth(3);
        testLine.setFill(Color.ORANGE);

        return testLine;
    }

}

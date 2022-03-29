package io.pool.view;

import io.pool.controller.BallController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.net.MalformedURLException;

public class PoolCueView {

    /** The cylinder that will represent the pool cue */
    private static ImageView cue;
    private Line poolLine;
    private Path path;


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
        path = new Path();
        path.setFill(Color.TRANSPARENT);
        path.setStroke(Color.TRANSPARENT);
        poolLine.setStroke(Color.WHITE);
        poolLine.setStrokeWidth(1);
        poolLine.setFill(Color.WHITE);

        //this.testLineView();
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
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

//    public Line testLineView(){
//        Line testLine = new Line();
//        testLine.setStrokeWidth(3);
//        testLine.setFill(Color.ORANGE);
//
//        return testLine;
//    }

    public void setPoolLine(Line poolLine) {
        this.poolLine = poolLine;
    }
}

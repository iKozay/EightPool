package io.pool.view;

import io.pool.controller.BallController;
import io.pool.controller.PoolCueController;
import io.pool.eightpool.ResourcesLoader;
import io.pool.model.BallModel;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.io.File;

public class PoolCueView {

    /** The cylinder that will represent the pool cue */
    private static ImageView cue;
    private Line poolLine;
    private Path path;


    double previousAngle = 0;

    public PoolCueView() {

        cue = new ImageView(ResourcesLoader.poolCueImages.get(0));
        cue.setX(0);
        cue.setY(0);
        poolLine = new Line();
        path = new Path();
        //poolCueView.getCue().getTransforms().add(rotate);
        //poolCueView.getPath().getTransforms().add(rotate);
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

    public void setRotationTransform(PoolCueController pcc) {
        cue.getTransforms().add(pcc.getRotate());
        path.getTransforms().add(pcc.getRotate());
        path.setStrokeWidth(BallModel.RADIUS);
        path.getElements().addAll(pcc.getMoveTo(),pcc.getLineTo());

    }
}

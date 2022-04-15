package io.pool.view;

import io.pool.controller.PoolCueController;
import io.pool.eightpool.ResourcesLoader;
import io.pool.model.BallModel;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;

public class PoolCueView {

    /** The cylinder that will represent the pool cue */
    private static ImageView cue;
    private Line poolLine;
    private Path path;
    private double scale = 0.30;
    private Circle ballCollisionCircle;

    double previousAngle = 0;

    public PoolCueView() {

        cue = new ImageView(ResourcesLoader.poolCueImages.get(0));

        cue.setFitWidth(TableView.width/2.);
        cue.setPreserveRatio(true);

        cue.setX(0);
        cue.setY(0);

        poolLine = new Line();
        path = new Path();
        path.setFill(Color.TRANSPARENT);
        path.setStroke(Color.GREEN);
        path.setStrokeWidth(BallModel.RADIUS*2);
        poolLine.setStroke(Color.WHITE);
        poolLine.setStrokeWidth(1);
        poolLine.setFill(Color.WHITE);
        ballCollisionCircle = new Circle();
        ballCollisionCircle.setRadius(BallModel.RADIUS);
        ballCollisionCircle.setStroke(Color.WHITE);
        ballCollisionCircle.setFill(Color.TRANSPARENT);
    }


    public Circle getBallCollisionCircle() {return ballCollisionCircle;}

    public void setBallCollisionCircle(Circle ballCollisionCircle) {this.ballCollisionCircle = ballCollisionCircle;}
    public Path getPath() {
        return path;
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

    public void setRotationTransform(PoolCueController pcc) {
        cue.getTransforms().add(pcc.getRotate());
        path.getTransforms().add(pcc.getRotate());
        path.getElements().addAll(pcc.getMoveTo(),pcc.getLineTo());

    }
}

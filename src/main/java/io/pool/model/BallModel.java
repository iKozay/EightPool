package io.pool.model;
import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


public class BallModel {


    private Point2D ballPosition;
    private Point2D ballVector;
    private int radius;
    private Paint color;

    public BallModel(int radius){
        this.radius = radius;
        Circle circle = new Circle(radius);
        ballPosition = new Point2D(700,700);
        ballVector = new Point2D(1,2);
    }

    public Point2D getBallPosition() {
        return ballPosition;
    }

    public void setBallPosition(Point2D ballPosition) {
        this.ballPosition = ballPosition;
    }

    public Point2D getBallVector() {
        return ballVector;
    }

    public void setBallVector(Point2D ballVector) {
        this.ballVector = ballVector;
    }

    public int getRadius() {
        return radius;
    }
}

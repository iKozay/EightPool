package io.pool.model;
import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Objects;
import java.util.Random;


public class BallModel {

    private Point2D ballPosition;
    private Point2D ballVector;
    private int radius;
    private Paint color;
    private int number;

    public BallModel(int radius, int number, Paint color){
        this.radius = radius;
        this.number = number;
        Circle circle = new Circle(radius);
        ballPosition = new Point2D(700,700);
        Random rnd = new Random();
        ballVector = new Point2D(rnd.nextInt(7)+1, rnd.nextInt(7)+1);
        this.color  = color;
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

    public Paint getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BallModel ballModel = (BallModel) o;
        return number == ballModel.number;
    }

}

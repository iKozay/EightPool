package io.pool.model;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Objects;
import java.util.Random;


public class BallModel {

    private Point3D ballPosition;
    private Point3D ballVector;
    private int radius;
    private Paint color;
    private int number;
    private Image img;

    public BallModel(int radius, int number, Image img){
        this.radius = radius;
        this.number = number;
        Circle circle = new Circle(radius);
        ballPosition = new Point3D(700,700,0);
        Random rnd = new Random();
        ballVector = new Point3D(rnd.nextInt(7)+1, rnd.nextInt(7)+1,0);
        this.img  = img;
    }

    public Point3D getBallPosition() {
        return ballPosition;
    }

    public void setBallPosition(Point3D ballPosition) {
        this.ballPosition = ballPosition;
    }

    public Point3D getBallVector() {
        return ballVector;
    }

    public void setBallVector(Point3D ballVector) {
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

    public Image getImg() {
        return img;
    }
}

package io.pool.model;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;

import java.util.Objects;
import java.util.Random;


public class BallModel {

    private double acceleration;
    private Point2D ballPosition;
    private VelocityVector ballVector;
    private Point2D previousPosition;
    public static int radius;
    private int number;
    private Image img;
    public final static double GRAVITATIONAL_FORCE = 9.8;
    public final static double MASS_BALL_KG = 0.16;
    private boolean movingBall;


    public BallModel(int radius, int number, Image img){
        this.radius = radius;
        this.number = number;
        ballPosition = new Point2D(500,250);
        ballVector = new VelocityVector(7,5);
        acceleration = 0.99;
        this.img  = img;
        this.movingBall=true;// because it has a velocity
    }



    public Point2D getBallPosition() {
        return ballPosition;
    }

    public void setBallPosition(Point2D ballPosition) {
        this.previousPosition = this.ballPosition;
        this.ballPosition = ballPosition;
    }

    public VelocityVector getBallVector() {
        return ballVector;
    }

    public void setBallVector(VelocityVector ballVector) {
        this.ballVector = ballVector;
    }

    public static int getRadius() {
        return radius;
    }

    public double ballDistance(){
        return previousPosition.distance(ballPosition);
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
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

    public boolean isMovingBall() {
        return movingBall;
    }

    public void setMovingBall(boolean movingBall) {
        this.movingBall = movingBall;
    }

}

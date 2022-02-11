package io.pool.model;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Sphere;

import java.util.Objects;
import java.util.Random;


public class BallModel extends Shape {

    private Point2D ballForce;
    private Point2D acceleration;
    private Point2D ballPosition;
    private Point2D ballVector;
    private Point2D previousBallVector;
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
        Random rnd = new Random();
        this.ballPosition = new Point2D(900,250);
        this.previousBallVector = new Point2D(0,0);
        this.ballVector = new Point2D(-7,-5);
        this.ballForce = new Point2D(0,0);
        this.acceleration= new Point2D(0,0);
        this.img  = img;
        this.movingBall=true;// because it has a velocity
    }

    public Point2D getBallForce() {
        return ballForce;
    }

    public void setBallForce(Point2D ballForce, double time) {
        this.ballForce = ballForce;
        this.acceleration = this.ballForce.multiply(1 / MASS_BALL_KG);
        Point2D newVelocity = ballVector.add((this.acceleration.multiply(time)));
        if(Math.signum(newVelocity.getX())!=Math.signum(ballVector.getX())){
            newVelocity = newVelocity.subtract(newVelocity.getX(),0);
        }
        if(Math.signum(newVelocity.getY())!=Math.signum(ballVector.getY())){
            newVelocity = newVelocity.subtract(0,newVelocity.getY());
        }
        System.out.println(newVelocity);
        this.setBallVector(newVelocity);
    }

    public Point2D getBallPosition() {
        return ballPosition;
    }

    public void setBallPosition(Point2D ballPosition) {
        this.previousPosition = this.ballPosition;
        this.ballPosition = ballPosition;
    }

    public Point2D getBallVector() {
        return ballVector;
    }

    public void setBallVector(Point2D ballVector) {
        this.previousBallVector = this.ballVector;
        this.ballVector = ballVector;
    }

    public Point2D getPreviousBallVector() {
        return previousBallVector;
    }

    public static int getRadius() {
        return radius;
    }

    public double ballDistance(){
        return previousPosition.distance(ballPosition);
    }

    public Point2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Point2D acceleration) {
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

    public double getBallPositionX() {
        return ballPosition.getX();
    }

    public double getBallPositionY() {
        return ballPosition.getY();
    }
}

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
    private int radius;
    private int number;
    private Image img;

    public BallModel(int radius, int number, Image img){
        this.radius = radius;
        this.number = number;
        ballPosition = new Point2D(500,250);
        ballVector = new VelocityVector(10,10);
        acceleration = 0.99;
        this.img  = img;
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

    public int getRadius() {
        return radius;
    }

    public double ballDistance(){
        return previousPosition.distance(ballPosition);
    }

    public void applyFriction(){
        ballVector.setX(ballVector.getX()*acceleration);
        ballVector.setY(ballVector.getY()*acceleration);
    }
    public void checkIfStopped(){

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

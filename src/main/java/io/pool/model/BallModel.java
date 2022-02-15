package io.pool.model;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;


public class BallModel extends Shape {

    private CustomPoint2D ballForce;
    private CustomPoint2D acceleration;
    private CustomPoint2D ballPosition;
    private CustomPoint2D ballVelocity;
    private CustomPoint2D previousBallVelocity;
    private CustomPoint2D previousBallPosition;
    public static int radius;
    private int number;
    private Image img;
    public final static double GRAVITATIONAL_FORCE = 9.8;
    public final static double MASS_BALL_KG = 0.16;
    private boolean movingBall;
    File file;
    FileWriter writer;
    BigDecimal time;


    public BallModel(int radius, int number, Image img){
        this.radius = radius;
        this.number = number;
        Random rnd = new Random();
        this.ballPosition = new CustomPoint2D(400,250);
        this.previousBallVelocity = new CustomPoint2D(0,0);
        this.ballVelocity = new CustomPoint2D(12,4);
        this.ballForce = new CustomPoint2D(0,0);
        this.acceleration= new CustomPoint2D(0,0);
        this.img  = img;
        this.movingBall=true;// because it has a velocity
        ////
        file = new File("filename.txt");
        try {
            writer = new FileWriter("filename.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        time= new BigDecimal(0);
        ////
    }

    public CustomPoint2D getBallForce() {
        return ballForce;
    }

    public void setBallForce(CustomPoint2D ballForce, double time) {
        this.ballForce = ballForce;
        this.acceleration = this.ballForce.multiply(1 / MASS_BALL_KG);
        CustomPoint2D finalV = this.acceleration.multiply(time);
        CustomPoint2D newVelocityMag = new CustomPoint2D(ballVelocity.getAbsX().subtract(finalV.getAbsX()),ballVelocity.getAbsY().subtract(finalV.getAbsY()));
        //newVelocityMag.subtract((this.acceleration.multiply(time)));

        if(newVelocityMag.getX().doubleValue()<0){
            newVelocityMag = new CustomPoint2D(0,newVelocityMag.getY().doubleValue());
        }
        if(newVelocityMag.getY().doubleValue()<0){
            newVelocityMag = new CustomPoint2D(newVelocityMag.getX().doubleValue(),0);
        }

        CustomPoint2D newVelocity = new CustomPoint2D(newVelocityMag.getX().multiply(new BigDecimal(ballVelocity.getX().signum()),CustomPoint2D.DECIMAL8),  newVelocityMag.getY().multiply(new BigDecimal(ballVelocity.getY().signum(),CustomPoint2D.DECIMAL8)));

        this.time = this.time.add(new BigDecimal(time));
        try {
            writer.write(this.time.doubleValue()+","+newVelocityMag.getX().doubleValue()+","+newVelocityMag.getY().doubleValue()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(newVelocity);

        this.setBallVelocity(newVelocity);
    }

    public CustomPoint2D getBallPosition() {
        return ballPosition;
    }

    public void setBallPosition(CustomPoint2D ballPosition) {
        this.previousBallPosition = this.ballPosition;
        this.ballPosition = ballPosition;
    }

    public CustomPoint2D getBallVelocity() {
        return ballVelocity;
    }

    public void setBallVelocity(CustomPoint2D ballVelocity) {
        this.previousBallVelocity = this.ballVelocity;
        this.ballVelocity = ballVelocity;
    }

    public CustomPoint2D getPreviousBallVelocity() {
        return previousBallVelocity;
    }

    public static int getRadius() {
        return radius;
    }

    public BigDecimal ballDistance(){
        return previousBallPosition.distance(ballPosition);
    }

    public CustomPoint2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(CustomPoint2D acceleration) {
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
        if(movingBall==false){
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BigDecimal getBallPositionX() {
        return ballPosition.getX();
    }

    public BigDecimal getBallPositionY() {
        return ballPosition.getY();
    }
}

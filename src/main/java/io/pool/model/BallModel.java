package io.pool.model;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;


public class BallModel {

    public static int radius;
    private int number;
    private Image img;
    public final static double GRAVITATIONAL_FORCE = 9.8;
    public final static double MASS_BALL_KG = 0.16;
    private boolean movingBall;

    /**
     * VARIABLES WITHOUT Point2D
     */

    //Ball Position
    private BigDecimal ballPositionX;
    private BigDecimal ballPositionY;

    //Ball Velocity
    private BigDecimal ballVelocityX;
    private BigDecimal ballVelocityY;

    //Ball Acceleration
    private BigDecimal ballAccelerationX;
    private BigDecimal ballAccelerationY;

    public BallModel(int radius, int number, Image img){
        this.radius = radius;
        this.number = number;

        // Setting ball Velocity
        Random rnd = new Random();
        this.ballVelocityX = new BigDecimal(rnd.nextInt(5)+1);
        this.ballVelocityY = new BigDecimal(rnd.nextInt(5)+1);

        // Getting the position of the ball
        this.ballPositionX = new BigDecimal(rnd.nextInt(700)+200);
        this.ballPositionY = new BigDecimal(rnd.nextInt(400)+200);

        this.img  = img;
        this.movingBall=true;// because it has a velocity
    }

    public static int getRadius() {
        return radius;
    }

    public BigDecimal getBallPositionX() {
        return ballPositionX;
    }

    public BigDecimal getBallPositionY() {
        return ballPositionY;
    }

    public void setBallPositionX(BigDecimal ballPositionX) {
        this.ballPositionX = ballPositionX;
    }

    public void setBallPositionY(BigDecimal ballPositionY) {
        this.ballPositionY = ballPositionY;
    }

    public BigDecimal getBallVelocityX() {
        return ballVelocityX;
    }

    public void setBallVelocityX(BigDecimal ballVelocityX) {
        this.ballVelocityX = ballVelocityX;
    }

    public BigDecimal getBallVelocityY() {
        return ballVelocityY;
    }

    public void setBallVelocityY(BigDecimal ballVelocityY) {
        this.ballVelocityY = ballVelocityY;
    }

    public BigDecimal getBallAccelerationX() {
        return ballAccelerationX;
    }

    public void setBallAccelerationX(BigDecimal ballAccelerationX) {
        this.ballAccelerationX = ballAccelerationX;
    }

    public BigDecimal getBallAccelerationY() {
        return ballAccelerationY;
    }

    public void setBallAccelerationY(BigDecimal ballAccelerationY) {
        this.ballAccelerationY = ballAccelerationY;
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

    public void setMovingBall() {
        if(ballVelocityY.doubleValue() == 0 && ballVelocityX.doubleValue() == 0){
            movingBall = false;
        }else {
            movingBall = true;
        }
    }

    public int getNumber() {
        return this.number;
    }
}

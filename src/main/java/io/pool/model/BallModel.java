package io.pool.model;

import javafx.scene.image.Image;

import java.math.BigDecimal;

public class BallModel extends PhysicsModule{

    /** The radius of the ball */
    public static int RADIUS = 15;
    /** The number of the ball */
    private int number;
    /** The image that will be on the sphere */
    private Image img;


    /**
     * Main constructor of BallModel
     * */
    public BallModel(int number, Image img){
        super();
        this.number = number;
        this.img  = img;
        /** The BallModel does not need the use of force
         * Setting them to zero by calling the overridden methods
         * */
        setForceX(null);
        setForceY(null);
    }

    /**
     * Checks if this ball is the same as the one given in the parameter.
     * @param otherBall Second ball
     * @return <code>true</code> if Both balls have the same number. <code>false</code> otherwise
     */
    @Override
    public boolean equals(Object otherBall) {
        if (this == otherBall) return true;
        if (otherBall == null || getClass() != otherBall.getClass()) return false;
        BallModel ballModel = (BallModel) otherBall;
        return number == ballModel.number;
    }

    /**
     * Gets the image that will be used for this ball
     * @return Path to image
     */
    public Image getImg() {
        return img;
    }

    /**
     * Gets the ball number
     * @return the ball number
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Overriding the Force setter methods
     * because the ball does not need them. These properties are always
     * equal to zero. The BallModel only uses the acceleration.
     */
    @Override
    public void setForceX(BigDecimal forceX) {
        super.setForceX(ZERO);
    }

    @Override
    public void setForceY(BigDecimal forceY) {
        super.setForceY(ZERO);
    }
}

package io.pool.model;

public class BallModel extends PhysicsModule{

    /** The radius of the ball */
    public static int RADIUS;
    /** The number of the ball */
    private int number;


    /**
     * Main constructor of BallModel
     * */
    public BallModel(int number){
        super();
        this.number = number;
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
     * Gets the ball number
     * @return the ball number
     */
    public int getNumber() {
        return this.number;
    }
}

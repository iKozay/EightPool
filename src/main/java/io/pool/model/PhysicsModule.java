package io.pool.model;

import io.pool.view.BallView;
import javafx.scene.transform.Rotate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

public class PhysicsModule {
    /**
     * Gravitational constant
     */
    public final static double GRAVITATIONAL_FORCE = 9.8;
    /**
     * Ball mass
     */
    public final static double MASS_BALL_KG = 0.16;
    /**
     * Friction coefficient for ball-table
     */
    private static final double FRICTION_COEFFIECIENT = 0.01;


    /**
     * Position in component form
     */
    private BigDecimal ballPositionX;
    private BigDecimal ballPositionY;

    /**
     * Velocity in component form
     */
    private BigDecimal ballVelocityX;
    private BigDecimal ballVelocityY;

    /**
     * Boolean that tells if the ball has a velocity vector equal to zero
     * it is <>true</> if both velocity X and Y are not zero
     * it is <>false</> otherwise
     */

    private boolean movingBall;

    /**
     * Acceleration in component form
     */
    private BigDecimal ballAccelerationX;
    private BigDecimal ballAccelerationY;

    /**
     * Main constructor of the Physics Module
     */
    public PhysicsModule() {
        randomizePosition();
        randomizeVelocity();
    }

    /**
     * Assigns a random position on the table for the ball
     */
    private void randomizePosition() {
        Random rnd = new Random();
        this.ballPositionX = new BigDecimal(rnd.nextInt(700) + 200);
        this.ballPositionY = new BigDecimal(rnd.nextInt(400) + 200);
    }

    /**
     * Assigns a random velocity for the ball
     */
    private void randomizeVelocity() {
        Random rnd = new Random();
        this.ballVelocityX = new BigDecimal(rnd.nextInt(5) + 1);
        this.ballVelocityY = new BigDecimal(rnd.nextInt(5) + 1);
        this.movingBall = true;
    }

    /**
     * Updates the BallModel and the BallView positions
     *
     * @param bView The BallView that represents this ball
     * @param time  Time in seconds
     */
    public void updateBallPosition(BallView bView, double time) {
        setMovingBall();
        /** Updates the position of the ball only if it is moving */
        if (isMovingBall()) {
            applyFriction(FRICTION_COEFFIECIENT, time);
            /** Updates the BallModel position */
            setBallPositionX(getBallPositionX().add(getBallVelocityX()));
            setBallPositionY(getBallPositionY().add(getBallVelocityY()));
            /** Updates the BallView position */
            bView.getBall().setLayoutX(getBallPositionX().doubleValue());
            bView.getBall().setLayoutY(getBallPositionY().doubleValue());
            /** Add rotation animation */
            Rotate rx = new Rotate(-getBallVelocityY().doubleValue(), 0, 0, 0, Rotate.X_AXIS);
            Rotate ry = new Rotate(getBallVelocityX().doubleValue(), 0, 0, 0, Rotate.Y_AXIS);
            bView.getBall().getTransforms().addAll(rx, ry);
        }
    }

    /**
     * Applies friction to the ball
     *
     * @param frictionCoefficient Friction coefficient depending on the situation
     * @param time                Time in seconds
     */
    private void applyFriction(double frictionCoefficient, double time) {
        /** Get the friction magnitude */
        BigDecimal frictionForceMag = new BigDecimal(frictionCoefficient * PhysicsModule.MASS_BALL_KG * PhysicsModule.GRAVITATIONAL_FORCE, MathContext.DECIMAL32);
        /** Calculate the velocity ratio */
        double velocityRatio = getBallVelocityX().abs().doubleValue() / getBallVelocityY().abs().doubleValue();
        /** Assign the new acceleration using the ratio:
         * <img src="docs-files/ratio.png"/>
         *  <p>
         *      Let R be the ratio between V<sub>x</sub> and V<sub>y</sub>. R = V<sub>x</sub> / V<sub>y</sub>
         *      That same ratio has to apply for the acceleration. R = a<sub>x</sub> / a<sub>y</sub>
         *
         *      From the image above, the following relation can be drawn: a<sup>2</sup> = (a<sub>x</sub>)<sup>2</sup> + (a<sub>y</sub>)<sup>2</sup>
         *      With some algebraic manipulations, we can find the new values of a<sub>x</sub> and a<sub>y</sub>:
         *      a<sup>2</sup> = (a<sub>x</sub>)<sup>2</sup> + (a<sub>y</sub>)<sup>2</sup>
         *      a<sup>2</sup> = (R*a<sub>y</sub>)<sup>2</sup> + (a<sub>y</sub>)<sup>2</sup>
         *      a<sup>2</sup> = (R+1) * (a<sub>y</sub>)<sup>2</sup>
         *      a<sup>2</sup> / (R+1) =  (a<sub>y</sub>)<sup>2</sup>
         *      ---
         *      a<sub>y</sub> = sqrt [a<sup>2</sup> / (R+1)]
         *      a<sub>x</sub> = R * a<sub>y</sub>
         *  </p>
         * */
        setBallAccelerationY((new BigDecimal(Math.sqrt(Math.pow(frictionForceMag.doubleValue(), 2)) / (Math.pow(velocityRatio, 2) + 1), MathContext.DECIMAL32)));
        setBallAccelerationX(new BigDecimal(velocityRatio * getBallAccelerationY().doubleValue(), MathContext.DECIMAL32));

        /**
         * Make the acceleration opposite to the velocity if it is positive
         * Acceleration is negative by default
         */
        if (getBallVelocityX().doubleValue() > 0) {
            setBallAccelerationX(getBallAccelerationX().multiply(BigDecimal.valueOf(-1)));
        }
        if (getBallVelocityY().doubleValue() > 0) {
            setBallAccelerationY(getBallAccelerationY().multiply(BigDecimal.valueOf(-1)));
        }

        /**
         * Checks if the acceleration is bigger than the velocity.
         * This means that the ball will become be stationary.
         * If it is not, it assigns the new velocity
         */
        if (getBallVelocityX().abs().doubleValue() < getBallAccelerationX().abs().doubleValue()) {
            setBallVelocityX(new BigDecimal(0));
        }
        if (getBallVelocityY().abs().doubleValue() < getBallAccelerationY().abs().doubleValue()) {
            setBallVelocityY(new BigDecimal(0));
        }
    }

    /**
     * Handles all the momentum calculation.
     *
     * @param otherBall The second ball that will interact with this one
     * @see <a href="https://vobarian.com/collisions/2dcollisions2.pdf">2-Dimensional Elastic Collisions without Trigonometry</a>
     */
    public void handleMomentum(BallModel otherBall, double distance) {
        //
        /**1
         * find unit normal and unit tanget vector
         */
        PhysicsModule ball1 = this;
        PhysicsModule ball2 = otherBall;

        BigDecimal normalXComponent = ball2.getBallVelocityX().subtract(ball1.getBallVelocityX());
        BigDecimal normalYComponent = ball2.getBallVelocityY().subtract(ball1.getBallVelocityY());

        BigDecimal magnitude = (normalYComponent.pow(2).add(normalXComponent.pow(2))).sqrt(MathContext.DECIMAL32);
        BigDecimal unitNormalX = normalXComponent.divide(magnitude, MathContext.DECIMAL32);
        BigDecimal unitNormalY = normalYComponent.divide(magnitude, MathContext.DECIMAL32);

        BigDecimal unitTangentX = unitNormalY.negate();
        BigDecimal unitTangentY = unitNormalX;


        /**
         * Find the minimum distance X and Y to prevent overlapping
         */
        BigDecimal distanceX = normalXComponent.multiply(new BigDecimal((2*BallModel.RADIUS-distance)/distance));
        BigDecimal distanceY = normalYComponent.multiply(new BigDecimal((2*BallModel.RADIUS-distance)/distance));

        /**
         * Push-Pull Balls apart
         */
        ball1.setBallPositionX(ball1.getBallPositionX().add(distanceX.divide(new BigDecimal(2))));
        ball1.setBallPositionY(ball1.getBallPositionY().add(distanceY.divide(new BigDecimal(2))));
        ball2.setBallPositionX(ball2.getBallPositionX().subtract(distanceX.divide(new BigDecimal(2))));
        ball2.setBallPositionY(ball2.getBallPositionY().subtract(distanceY.divide(new BigDecimal(2))));


        /**2 (step 2 is skipped because we already have the balls vectors
         * Resolve velocity vectors of ball 1 and 2 into normal and tangential components
         * this is done by using the dot product of the balls initial velocity and using the unitVectors
         */
        BigDecimal v1n = (unitNormalX.multiply(ball1.getBallVelocityX()).add(unitNormalY.multiply(ball1.getBallVelocityY())));
        BigDecimal v1t = (unitTangentX.multiply(ball1.getBallVelocityX())).add(unitTangentY.multiply(ball1.getBallVelocityY()));
        BigDecimal v2n = (unitNormalX.multiply(ball2.getBallVelocityX())).add(unitNormalY.multiply(ball2.getBallVelocityY()));
        BigDecimal v2t = (unitTangentX.multiply(ball2.getBallVelocityX())).add(unitTangentY.multiply(ball2.getBallVelocityY()));

        /**3
         * Find new tangential velocities
         * they are equal to the initial ones
         */
        BigDecimal v1tp = v1t;
        BigDecimal v2tp = v2t;

        /**4
         * Find new normal velocities
         * all the instances of 1 in this equation are substitutes for mass
         * this is assuming all the balls have equal mass
         */
        BigDecimal v1np = v2n;
        BigDecimal v2np = v1n;

        /**5
         * Convert scalar normal and tangential velocites into vectors
         */
        BigDecimal normalXFinalBall1 = unitNormalX.multiply(v1np);
        BigDecimal normalYFinalBall1 = unitNormalY.multiply(v1np);
        BigDecimal normalXFinalBall2 = unitNormalX.multiply(v2np);
        BigDecimal normalYFinalBall2 = unitNormalY.multiply(v2np);

        BigDecimal tangentialXFinalBall1 = unitTangentX.multiply(v1tp);
        BigDecimal tangentialYFinalBall1 = unitTangentY.multiply(v1tp);
        BigDecimal tangentialXFinalBall2 = unitTangentX.multiply(v2tp);
        BigDecimal tangentialYFinalBall2 = unitTangentY.multiply(v2tp);

        /**6
         * Add normal and tangential vectors for each ball
         */
        BigDecimal finalBall1X = normalXFinalBall1.add(tangentialXFinalBall1, MathContext.DECIMAL32);
        BigDecimal finalBall1Y = normalYFinalBall1.add(tangentialYFinalBall1, MathContext.DECIMAL32);
        BigDecimal finalBall2X = normalXFinalBall2.add(tangentialXFinalBall2, MathContext.DECIMAL32);
        BigDecimal finalBall2Y = normalYFinalBall2.add(tangentialYFinalBall2, MathContext.DECIMAL32);

        ball1.setBallVelocityX(finalBall1X);
        ball1.setBallVelocityY(finalBall1Y);
        ball2.setBallVelocityX(finalBall2X);
        ball2.setBallVelocityY(finalBall2Y);
    }

    /**
     * Gets the distance between this ball and another ball
     *
     * @param otherBall Another ball
     * @return The distance between the two balls
     */
    public BigDecimal distance(BallModel otherBall) {
        /**
         * Assigns the x and y variables
         */
        BigDecimal x2, y2, x1, y1;
        x2 = otherBall.getBallPositionX();
        y2 = otherBall.getBallPositionY();
        x1 = getBallPositionX();
        y1 = getBallPositionY();
        /**
         * Finds distance using the following equation:
         * <p>
         *     D = sqrt [ (x<sub>2</sub>-x<sub>1</sub>)<sup>2</sup> + (y<sub>2</sub>-y<sub>1</sub>)<sup>2</sup> ]
         * </p>
         */
        BigDecimal a = x2.subtract(x1, MathContext.DECIMAL32);
        BigDecimal b = y2.subtract(y1, MathContext.DECIMAL32);
        a = a.pow(2, MathContext.DECIMAL32);
        b = b.pow(2, MathContext.DECIMAL32);
        BigDecimal subtotal = a.add(b, MathContext.DECIMAL32);
        return subtotal.sqrt(MathContext.DECIMAL32);
    }

    /**
     * Gets the component X of the ball position
     *
     * @return component X of Position
     */
    public BigDecimal getBallPositionX() {
        return ballPositionX;
    }

    /**
     * Gets the component Y of the ball position
     *
     * @return component Y of Position
     */
    public BigDecimal getBallPositionY() {
        return ballPositionY;
    }

    /**
     * Sets the component X of the ball position
     *
     * @param ballPositionX The new X component of Position
     */
    public void setBallPositionX(BigDecimal ballPositionX) {
        this.ballPositionX = ballPositionX;
    }

    /**
     * Sets the component Y of the ball position
     *
     * @param ballPositionY The new Y component of Position
     */
    public void setBallPositionY(BigDecimal ballPositionY) {
        this.ballPositionY = ballPositionY;
    }

    /**
     * Gets the component X of the ball velocity
     *
     * @return component X of Velocity
     */
    public BigDecimal getBallVelocityX() {
        return ballVelocityX;
    }

    /**
     * Sets the component X of the ball velocity
     *
     * @param ballVelocityX The new X component of Velocity
     */
    public void setBallVelocityX(BigDecimal ballVelocityX) {
        this.ballVelocityX = ballVelocityX;
    }

    /**
     * Gets the component Y of the ball velocity
     *
     * @return component Y of Velocity
     */
    public BigDecimal getBallVelocityY() {
        return ballVelocityY;
    }

    /**
     * Sets the component Y of the ball velocity
     *
     * @param ballVelocityY The new Y component of Velocity
     */
    public void setBallVelocityY(BigDecimal ballVelocityY) {
        this.ballVelocityY = ballVelocityY;
    }

    /**
     * Gets the component X of the ball acceleration
     *
     * @return component X of Acceleration
     */
    public BigDecimal getBallAccelerationX() {
        return ballAccelerationX;
    }

    /**
     * Sets the component X of the ball acceleration
     *
     * @param ballAccelerationX The new X component of Acceleration
     */
    public void setBallAccelerationX(BigDecimal ballAccelerationX) {
        this.ballAccelerationX = ballAccelerationX;
    }

    /**
     * Gets the component Y of the ball acceleration
     *
     * @return component Y of Acceleration
     */
    public BigDecimal getBallAccelerationY() {
        return ballAccelerationY;
    }

    /**
     * Sets the component Y of the ball acceleration
     *
     * @param ballAccelerationY The new Y component of Acceleration
     */
    public void setBallAccelerationY(BigDecimal ballAccelerationY) {
        this.ballAccelerationY = ballAccelerationY;
    }

    /**
     * Checks if the ball is moving.
     *
     * @return <code>true</code> if it has a velocity not equal to zero. <code>false</code> otherwise.
     */
    public boolean isMovingBall() {
        return movingBall;
    }

    /**
     * Sets the <code>movingBall</code> variable depending on if the ball has a velocity higher than 0.
     */
    public void setMovingBall() {
        if (ballVelocityY.doubleValue() == 0 && ballVelocityX.doubleValue() == 0) {
            movingBall = false;
        } else {
            movingBall = true;
        }
    }

}

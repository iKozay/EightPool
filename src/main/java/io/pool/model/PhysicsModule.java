package io.pool.model;

import io.pool.controller.BallController;
import javafx.scene.transform.Rotate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

public class PhysicsModule {
    /**
     * Zero in BigDecimal. To be used to define anything to zero
     */
    public static BigDecimal ZERO = new BigDecimal(0);
    /**
     * Gravitational constant
     */
    public final static double GRAVITATIONAL_FORCE = 9.8;
    /**
     * Ball mass
     */
    public final static double MASS_BALL_KG = 0.16;
    /**
     * Friction coefficient for ball-table rolling
     */
    private static final double ROLLING_FRICTION_COEFFICIENT = 0.02;
    private static final double KINETIC_ENERGY_LOSS_RATIO = 0.95;


    /**
     * Position in component form
     */
    private BigDecimal positionX;
    private BigDecimal positionY;

    /**
     * Velocity in component form
     */
    private BigDecimal velocityX;
    private BigDecimal velocityY;

    /**
     * Boolean that tells if the object has a velocity vector equal to zero
     * it is <code>true</code> if both velocity X and Y are not zero
     * it is <code>false</code> otherwise
     */

    public boolean isMoving;

    /**
     * Acceleration in component form
     */
    private BigDecimal accelerationX;
    private BigDecimal accelerationY;

    /**
     * Main constructor of the Physics Module
     * Randomizes the Position and Velocity
     */
    public PhysicsModule() {
        randomizePosition();
        randomizeVelocity();
    }

    /**
     * Constructor that defines the position and sets the velocity to zero
     *
     * @param positionX Position X of the object
     * @param positionY Position X of the object
     */
    public PhysicsModule(BigDecimal positionX, BigDecimal positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = ZERO;
        this.velocityY = ZERO;
        this.isMoving = false;
    }

    /**
     * Assigns a random position on the table for the object
     */
    private void randomizePosition() {
        Random rnd = new Random();
        this.positionX = new BigDecimal(rnd.nextInt(800) + 100);
        this.positionY = new BigDecimal(rnd.nextInt(300) + 300);
    }

    /**
     * Assigns a random velocity for the object
     */
    private void randomizeVelocity() {
        Random rnd = new Random();
        this.velocityX = new BigDecimal(rnd.nextInt(10) + 1);
        this.velocityY = new BigDecimal(rnd.nextInt(10) + 1);
        this.accelerationY = ZERO;
        this.accelerationX = ZERO;
        this.isMoving = true;
    }

    /**
     * Updates the BallModel and the BallView positions
     */
    public void updatePosition() {
        setIsMoving();
        /** Updates the position of the ball only if it is moving */
        if (isMoving) {
            applyFriction(ROLLING_FRICTION_COEFFICIENT);
            /** Updates the BallModel position */
            setPositionX(getPositionX().add(getVelocityX()));
            setPositionY(getPositionY().add(getVelocityY()));
            /** Add rotation animation */
            //BigDecimal vectorLength = (getVelocityX().pow(2).add(getVelocityY().pow(2))).sqrt(MathContext.DECIMAL32);
            Rotate rx = new Rotate(-getAccelerationX().doubleValue(),0,0,0,Rotate.Y_AXIS);
            Rotate ry = new Rotate(-getAccelerationY().doubleValue(), 0, 0,0,Rotate.X_AXIS);
            BallController.getBallViewFromBallModel((BallModel)this).getBall().getTransforms().addAll(rx,ry);
        }
    }

    /**
     * Applies friction to the ball
     *
     * <br><br>
     * Assign the new acceleration using the ratio:
     * <p>
     * Let R be the ratio between V<sub>x</sub> and V<sub>y</sub>. R = V<sub>x</sub> / V<sub>y</sub>
     * <br>
     * That same ratio has to apply for the acceleration. R = a<sub>x</sub> / a<sub>y</sub>
     * <br>
     * The following relation can be drawn using the Pythagorean theorem: a<sup>2</sup> = (a<sub>x</sub>)<sup>2</sup> + (a<sub>y</sub>)<sup>2</sup>
     * <br>
     * With some algebraic manipulations, we can find the new values of a<sub>x</sub> and a<sub>y</sub>:
     * <br>
     * a<sup>2</sup> = (a<sub>x</sub>)<sup>2</sup> + (a<sub>y</sub>)<sup>2</sup>
     * <br>
     * a<sup>2</sup> = (R*a<sub>y</sub>)<sup>2</sup> + (a<sub>y</sub>)<sup>2</sup>
     * <br>
     * a<sup>2</sup> = (R+1) * (a<sub>y</sub>)<sup>2</sup>
     * <br>
     * a<sup>2</sup> / (R+1) =  (a<sub>y</sub>)<sup>2</sup>
     * <br>
     * a<sub>y</sub> = sqrt [a<sup>2</sup> / (R+1)]
     * <br>
     * a<sub>x</sub> = R * a<sub>y</sub>
     * </p>
     *
     * @param frictionCoefficient Friction coefficient depending on the situation
     */
    private void applyFriction(double frictionCoefficient) {
        /** Get the friction magnitude */
        BigDecimal frictionForceMag = new BigDecimal(frictionCoefficient * PhysicsModule.MASS_BALL_KG * PhysicsModule.GRAVITATIONAL_FORCE, MathContext.DECIMAL32);
        BigDecimal ratio;
        /** Calculate the ratio */
        BigDecimal VecMag = new BigDecimal(Math.sqrt((getVelocityX().pow(2).add(getVelocityY().pow(2)).doubleValue())));
        if ((VecMag.doubleValue() > frictionForceMag.doubleValue()) && (VecMag.doubleValue() != 0)) {
            ratio = VecMag.subtract(frictionForceMag);
            ratio = ratio.divide(VecMag, MathContext.DECIMAL32);
            /**
             * frictionForceMag is basically the acceleration magnitude
             *              (VecMag - frictionForceMag)
             * ratio = 1 -  ___________________________  = 1 - 0.988 = 0.012 (for example)
             *                       VecMag
             * ratio is the percentage at which the velocity will get reduced. Example to show my point:
             * The simplest way to apply friction: setVelocityX(getVelocityX().multiply(0.988))
             * I am doing that but by incorporating the friction(or acceleration) magnitude
             * If the new velocity is 0.988 of the original, then it got reduced by a ratio of 0.012
             * The acceleration is simply that reduction that happened with the velocity.
             * Another important point, the ratio will be negative to simplify the computations
             * Therefore,
             *              AccelerationX is equal to VelocityX * ratio
             *              AccelerationY is equal to VelocityY * ratio
             * Down further in the code, the new Velocity will be equal to oldVelocity+Acceleration
             * which is the same thing as doing setVelocityX(getVelocityX().multiply(0.988))
             */
            ratio = new BigDecimal(1).subtract(ratio);
            ratio = ratio.negate();
            setAccelerationX(getVelocityX().multiply(ratio));
            setAccelerationY(getVelocityY().multiply(ratio));
        } else {
            setAccelerationX(ZERO);
            setAccelerationY(ZERO);
            setVelocityX(ZERO);
            setVelocityY(ZERO);
        }
        setVelocityX(getVelocityX().add(getAccelerationX(), MathContext.DECIMAL32));
        setVelocityY(getVelocityY().add(getAccelerationY(), MathContext.DECIMAL32));
    }

    /**
     * Handles all the momentum calculation.
     *
     * @param module Second object that extends PhysicsModule
     * @see <a href="https://vobarian.com/collisions/2dcollisions2.pdf">2-Dimensional Elastic Collisions without Trigonometry</a>
     */
    public boolean handleMomentum(PhysicsModule module) {
        /**
         * It is the ball hitting another ball
         * pm1 is the first ball
         * pm2 is the second ball
         */
        PhysicsModule pm1 = this, pm2 = module;
        double normalXComponent, normalYComponent, unitNormalX, unitNormalY, unitTangentX, unitTangentY;
        /**1
         * find unit normal and unit tangent vector
         */
        normalXComponent = pm2.getPositionX().doubleValue() - pm1.getPositionX().doubleValue();
        normalYComponent = pm2.getPositionY().doubleValue() - pm1.getPositionY().doubleValue();


        double distance = Math.sqrt(Math.pow(normalXComponent,2)+Math.pow(normalYComponent,2));
        unitNormalX = normalXComponent / distance;
        unitNormalY = normalYComponent / distance;
        if (distance < (2 * BallModel.RADIUS)) {

            /**
             * Find the minimum distance X and Y to prevent overlapping
             */
            double distanceX = normalXComponent * ((2 * BallModel.RADIUS - distance) / (distance));
            double distanceY = normalYComponent * ((2 * BallModel.RADIUS - distance) / (distance));
            /**
             * Push-Pull Balls apart
             */
            distanceX = distanceX/2;
            distanceY = distanceY/2;
            pm1.setPositionX(pm1.getPositionX().subtract(new BigDecimal(distanceX)));
            pm1.setPositionY(pm1.getPositionY().subtract(new BigDecimal(distanceY)));
            pm2.setPositionX(pm2.getPositionX().add(new BigDecimal(distanceX)));
            pm2.setPositionY(pm2.getPositionY().add(new BigDecimal(distanceY)));

            BallController.updateBallViewPosition((BallModel) pm1);
            BallController.updateBallViewPosition((BallModel) pm2);

            unitTangentX = -unitNormalY;
            unitTangentY = unitNormalX;

            /**2 (step 2 is skipped because we already have the balls vectors
             * Resolve velocity vectors of ball 1 and 2 into normal and tangential components
             * this is done by using the dot product of the balls initial velocity and using the unitVectors
             */
            double v1n = (unitNormalX * (pm1.getVelocityX().doubleValue())) + (unitNormalY * (pm1.getVelocityY().doubleValue()));
            double v1t = (unitTangentX * (pm1.getVelocityX().doubleValue())) + (unitTangentY * (pm1.getVelocityY().doubleValue()));
            double v2n = (unitNormalX * (pm2.getVelocityX().doubleValue())) + (unitNormalY * (pm2.getVelocityY().doubleValue()));
            double v2t = (unitTangentX * (pm2.getVelocityX().doubleValue())) + (unitTangentY * (pm2.getVelocityY().doubleValue()));

            /**3
             * Find new tangential velocities
             * they are equal to the initial ones
             */
            double v1tp = v1t;
            double v2tp = v2t;

            /**4
             * Find new normal velocities
             * all the instances of 1 in this equation are substitutes for mass
             * this is assuming all the balls have equal mass
             */
            double v1np = v2n;
            double v2np = v1n;

            /**5
             * Convert scalar normal and tangential velocites into vectors
             */
            double normalXFinalBall1 = unitNormalX * (v1np);
            double normalYFinalBall1 = unitNormalY * (v1np);
            double normalXFinalBall2 = unitNormalX * (v2np);
            double normalYFinalBall2 = unitNormalY * (v2np);

            double tangentialXFinalBall1 = unitTangentX * (v1tp);
            double tangentialYFinalBall1 = unitTangentY * (v1tp);
            double tangentialXFinalBall2 = unitTangentX * (v2tp);
            double tangentialYFinalBall2 = unitTangentY * (v2tp);

            /**6
             * Add normal and tangential vectors for each ball
             */
            double finalBall1X = (normalXFinalBall1 + (tangentialXFinalBall1))*KINETIC_ENERGY_LOSS_RATIO;
            double finalBall1Y = (normalYFinalBall1 + (tangentialYFinalBall1))*KINETIC_ENERGY_LOSS_RATIO;
            double finalBall2X = (normalXFinalBall2 + (tangentialXFinalBall2))*KINETIC_ENERGY_LOSS_RATIO;
            double finalBall2Y = (normalYFinalBall2 + (tangentialYFinalBall2))*KINETIC_ENERGY_LOSS_RATIO;


            pm1.setVelocityX(new BigDecimal(finalBall1X));
            pm1.setVelocityY(new BigDecimal(finalBall1Y));
            pm2.setVelocityX(new BigDecimal(finalBall2X));
            pm2.setVelocityY(new BigDecimal(finalBall2Y));
            return true;
        }
        return false;
    }


    /**
     *
     * Getters and Setters
     *
     */


    /**
     * Gets the component X of position
     *
     * @return component X of Position
     */
    public BigDecimal getPositionX() {
        return positionX;
    }


    /**
     * Sets the component X of position
     *
     * @param positionX The new X component of Position
     */
    public void setPositionX(BigDecimal positionX) {
        this.positionX = positionX;
    }

    /**
     * Gets the component Y of position
     *
     * @return component Y of Position
     */
    public BigDecimal getPositionY() {
        return positionY;
    }

    /**
     * Sets the component Y of position
     *
     * @param positionY The new Y component of Position
     */
    public void setPositionY(BigDecimal positionY) {
        this.positionY = positionY;
    }

    /**
     * Gets the component X of velocity
     *
     * @return component X of Velocity
     */
    public BigDecimal getVelocityX() {
        return velocityX;
    }

    /**
     * Sets the component X of velocity
     *
     * @param velocityX The new X component of Velocity
     */
    public void setVelocityX(BigDecimal velocityX) {
        this.velocityX = velocityX;
    }

    /**
     * Gets the component Y of velocity
     *
     * @return component Y of Velocity
     */
    public BigDecimal getVelocityY() {
        return velocityY;
    }

    /**
     * Sets the component Y of velocity
     *
     * @param velocityY The new Y component of Velocity
     */
    public void setVelocityY(BigDecimal velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * Gets the component X of acceleration
     *
     * @return component X of Acceleration
     */
    public BigDecimal getAccelerationX() {
        return accelerationX;
    }

    /**
     * Sets the component X of acceleration
     *
     * @param accelerationX The new X component of Acceleration
     */
    public void setAccelerationX(BigDecimal accelerationX) {
        this.accelerationX = accelerationX;
    }

    /**
     * Gets the component Y of acceleration
     *
     * @return component Y of Acceleration
     */
    public BigDecimal getAccelerationY() {
        return accelerationY;
    }

    /**
     * Sets the component Y of acceleration
     *
     * @param accelerationY The new Y component of Acceleration
     */
    public void setAccelerationY(BigDecimal accelerationY) {
        this.accelerationY = accelerationY;
    }

    /**
     * Sets the <code>movingBall</code> variable depending on if the object has a velocity higher than 0.
     */
    public void setIsMoving() {
        if (velocityY.doubleValue() == 0 && velocityX.doubleValue() == 0) {
            isMoving = false;
        } else {
            isMoving = true;
        }
    }

}

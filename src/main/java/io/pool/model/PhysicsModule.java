package io.pool.model;

import io.pool.view.BallView;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.Normalizer;
import java.util.Random;

public class PhysicsModule {
    /** Zero in BigDecimal. To be used to define anything to zero */
    protected static BigDecimal ZERO = new BigDecimal(0);
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
    private static final double FRICTION_COEFFIECIENT = 0.01;


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
     * Force in component form
     */
    private BigDecimal forceX;
    private BigDecimal forceY;

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
     * @param positionX Position X of the object
     * @param positionY Position X of the object
     */
    public PhysicsModule(BigDecimal positionX, BigDecimal positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = ZERO;
        this.velocityY = ZERO;
        this.isMoving=false;
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
        this.velocityX = new BigDecimal(rnd.nextInt(5) + 1);
        this.velocityY = new BigDecimal(rnd.nextInt(5) + 1);
        this.isMoving = true;
    }

    /**
     * Updates the BallModel and the BallView positions
     *
     * @param bView The BallView that represents this ball
     */
    public void updatePosition(BallView bView) {
        /** This method only applies to BallModel
         * Must check is it is called from BallModel
         * */
        if(this.getClass().equals(BallModel.class)) {
            setIsMoving();
            /** Updates the position of the ball only if it is moving */
            if (isMoving) {
                applyFriction(FRICTION_COEFFIECIENT);
                /** Updates the BallModel position */
                setPositionX(getPositionX().add(getVelocityX()));
                setPositionY(getPositionY().add(getVelocityY()));
                /** Updates the BallView position */
                bView.getBall().setLayoutX(getPositionX().doubleValue());
                bView.getBall().setLayoutY(getPositionY().doubleValue());
                /** Add rotation animation */
                Rotate rx = new Rotate(-getVelocityY().doubleValue(), 0, 0, 0, Rotate.X_AXIS);
                Rotate ry = new Rotate(getVelocityX().doubleValue(), 0, 0, 0, Rotate.Y_AXIS);
                bView.getBall().getTransforms().addAll(rx, ry);
            }
        }
    }

    /**
     * Applies friction to the ball
     *
     * <br><br>
     * Assign the new acceleration using the ratio:
     * <p>
     *      Let R be the ratio between V<sub>x</sub> and V<sub>y</sub>. R = V<sub>x</sub> / V<sub>y</sub>
     *      <br>
     *      That same ratio has to apply for the acceleration. R = a<sub>x</sub> / a<sub>y</sub>
     *      <br>
     *      The following relation can be drawn using the Pythagorean theorem: a<sup>2</sup> = (a<sub>x</sub>)<sup>2</sup> + (a<sub>y</sub>)<sup>2</sup>
     *      <br>
     *      With some algebraic manipulations, we can find the new values of a<sub>x</sub> and a<sub>y</sub>:
     *      <br>
     *      a<sup>2</sup> = (a<sub>x</sub>)<sup>2</sup> + (a<sub>y</sub>)<sup>2</sup>
     *      <br>
     *      a<sup>2</sup> = (R*a<sub>y</sub>)<sup>2</sup> + (a<sub>y</sub>)<sup>2</sup>
     *      <br>
     *      a<sup>2</sup> = (R+1) * (a<sub>y</sub>)<sup>2</sup>
     *      <br>
     *      a<sup>2</sup> / (R+1) =  (a<sub>y</sub>)<sup>2</sup>
     *      <br>
     *      a<sub>y</sub> = sqrt [a<sup>2</sup> / (R+1)]
     *      <br>
     *      a<sub>x</sub> = R * a<sub>y</sub>
     *  </p>
     * @param frictionCoefficient Friction coefficient depending on the situation
     */
    private void applyFriction(double frictionCoefficient) {
        /** This method only applies to BallModel
         * Must check is it is called from BallModel
         * */
        if(this.getClass().equals(BallModel.class)) {
            /** Get the friction magnitude */
            BigDecimal frictionForceMag = new BigDecimal(frictionCoefficient * PhysicsModule.MASS_BALL_KG * PhysicsModule.GRAVITATIONAL_FORCE, MathContext.DECIMAL32);
            /** Calculate the velocity ratio */
            double velocityRatio = getVelocityX().abs().doubleValue() / getVelocityY().abs().doubleValue();

            setAccelerationY((new BigDecimal(Math.sqrt(Math.pow(frictionForceMag.doubleValue(), 2)) / (Math.pow(velocityRatio, 2) + 1), MathContext.DECIMAL32)));
            setAccelerationX(new BigDecimal(velocityRatio * getAccelerationY().doubleValue(), MathContext.DECIMAL32));

            /**
             * Make the acceleration opposite to the velocity if it is positive
             * Acceleration is negative by default
             */
            if (getVelocityX().doubleValue() > 0) {
                setAccelerationX(getAccelerationX().multiply(BigDecimal.valueOf(-1)));
            }
            if (getVelocityY().doubleValue() > 0) {
                setAccelerationY(getAccelerationY().multiply(BigDecimal.valueOf(-1)));
            }

            /**
             * Checks if the acceleration is bigger than the velocity.
             * This means that the ball will become be stationary.
             * If it is not, it assigns the new velocity
             */
            if (getVelocityX().abs().doubleValue() < getAccelerationX().abs().doubleValue()) {
                setVelocityX(ZERO);
            } else {
                setVelocityX(getVelocityX().add(getAccelerationX()));
            }
            if (getVelocityY().abs().doubleValue() < getAccelerationY().abs().doubleValue()) {
                setVelocityY(ZERO);
            } else {
                setVelocityY(getVelocityY().add(getAccelerationY()));
            }
        }
    }

    /**
     * Handles all the momentum calculation.
     *
     * @param module Second object that extends PhysicsModule
     * @see <a href="https://vobarian.com/collisions/2dcollisions2.pdf">2-Dimensional Elastic Collisions without Trigonometry</a>
     */
    public void handleMomentum(PhysicsModule module, double distance) {
        // TODO Generalize the handle momentum to include table and Pool Cue
        /**1
         * find unit normal and unit tanget vector
         */
        PhysicsModule pm1 = this, pm2=module;
        BigDecimal normalXComponent,normalYComponent;
        if((this.getClass().equals(BallModel.class))&&(module.getClass().equals(BallModel.class))){
            /**
             * It is the ball hitting another ball
             * pm1 is the first ball
             * pm2 is the second ball
             */
            normalXComponent = pm2.getVelocityX().subtract(pm1.getVelocityX());
            normalYComponent = pm2.getVelocityY().subtract(pm1.getVelocityY());
        }else if(module.getClass().equals(TableModel.class)){
            /**
             * It is the ball hitting the table border
             * pm1 is the table
             * pm2 is the ball
             */
            normalXComponent = pm1.getVelocityX();
            normalYComponent = pm1.getVelocityY();
        }else{
            /**
             * It is the pool cue hitting the ball
             * pm1 is the pool cue
             * pm2 is the ball
             * */
            pm2.setAccelerationX(pm1.getForceX().divide(new BigDecimal(MASS_BALL_KG)));
            pm2.setAccelerationY(pm1.getForceY().divide(new BigDecimal(MASS_BALL_KG)));
            normalXComponent = pm2.getVelocityX().add(pm2.getAccelerationX());
            normalYComponent = pm2.getVelocityY().add(pm2.getAccelerationY());
        }


        BigDecimal magnitude = (normalYComponent.pow(2).add(normalXComponent.pow(2))).sqrt(MathContext.DECIMAL32);
        if(magnitude.compareTo(ZERO)==0){
            System.out.println("PROBLEM");
        }
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
        pm1.setPositionX(pm1.getPositionX().add(distanceX.divide(new BigDecimal(2))));
        pm1.setPositionY(pm1.getPositionY().add(distanceY.divide(new BigDecimal(2))));
        pm2.setPositionX(pm2.getPositionX().subtract(distanceX.divide(new BigDecimal(2))));
        pm2.setPositionY(pm2.getPositionY().subtract(distanceY.divide(new BigDecimal(2))));


        /**2 (step 2 is skipped because we already have the balls vectors
         * Resolve velocity vectors of ball 1 and 2 into normal and tangential components
         * this is done by using the dot product of the balls initial velocity and using the unitVectors
         */
        BigDecimal v1n = (unitNormalX.multiply(pm1.getVelocityX()).add(unitNormalY.multiply(pm1.getVelocityY())));
        BigDecimal v1t = (unitTangentX.multiply(pm1.getVelocityX())).add(unitTangentY.multiply(pm1.getVelocityY()));
        BigDecimal v2n = (unitNormalX.multiply(pm2.getVelocityX())).add(unitNormalY.multiply(pm2.getVelocityY()));
        BigDecimal v2t = (unitTangentX.multiply(pm2.getVelocityX())).add(unitTangentY.multiply(pm2.getVelocityY()));

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

        pm1.setVelocityX(finalBall1X);
        pm1.setVelocityY(finalBall1Y);
        pm2.setVelocityX(finalBall2X);
        pm2.setVelocityY(finalBall2Y);
    }

    /**
     * Gets the distance between this object and another object
     * <p>
     *     D = sqrt [ (x<sub>2</sub>-x<sub>1</sub>)<sup>2</sup> + (y<sub>2</sub>-y<sub>1</sub>)<sup>2</sup> ]
     * </p>
     * @param module Another object that extends PhysicsModule
     * @return The distance between two objects
     */
    public BigDecimal distance(PhysicsModule module) {
        /** This method only applies to BallModel
         * Must check is it is called from BallModel
         * */
        BigDecimal distance;
        BigDecimal x2=ZERO, y2=ZERO, x1=ZERO, y1=ZERO;
        /**
         * Assigns the x and y variables depending on the object
         */
        if(this.getClass().equals(BallModel.class)&&module.getClass().equals(BallModel.class)) {
            BallModel otherBall = (BallModel) module;
            x2 = otherBall.getPositionX();
            y2 = otherBall.getPositionY();
            x1 = getPositionX();
            y1 = getPositionY();

        }else{
            Shape intersect=null;
            if(this.getClass().equals(TableModel.class)){
                TableModel t = (TableModel) this;
                intersect = t.getCollisionOverlap();
            }else if(this.getClass().equals(PoolCueModel.class)){
                PoolCueModel p = (PoolCueModel) this;
                intersect = p.getCollisionOverlap();
            }
            if(intersect!=null){
                x2 = new BigDecimal(intersect.getBoundsInLocal().getHeight());
                y2 = new BigDecimal(intersect.getBoundsInLocal().getWidth());
            }
        }
            /**
             * Finds distance
             */
            BigDecimal a = x2.subtract(x1, MathContext.DECIMAL32);
            BigDecimal b = y2.subtract(y1, MathContext.DECIMAL32);
            a = a.pow(2, MathContext.DECIMAL32);
            b = b.pow(2, MathContext.DECIMAL32);
            BigDecimal subtotal = a.add(b, MathContext.DECIMAL32);
            distance = subtotal.sqrt(MathContext.DECIMAL32);
        return distance;
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
     * Gets the component X of the force
     *
     * @return component X of the force
     */
    public BigDecimal getForceX() {
        return forceX;
    }

    /**
     * Sets the component X of the force
     *
     * @param forceX The new X component of the force
     */
    public void setForceX(BigDecimal forceX) {
        this.forceX = forceX;
    }

    /**
     * Gets the component Y of the force
     *
     * @return component Y of the force
     */
    public BigDecimal getForceY() {
        return forceY;
    }

    /**
     * Sets the component Y of the force
     *
     * @param forceY The new Y component of the force
     */
    public void setForceY(BigDecimal forceY) {
        this.forceY = forceY;
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

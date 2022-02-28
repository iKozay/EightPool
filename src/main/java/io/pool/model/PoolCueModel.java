package io.pool.model;

import javafx.scene.shape.Shape;

import java.math.BigDecimal;

public class PoolCueModel extends PhysicsModule{

    private Shape collisionOverlap=null;

    public PoolCueModel(){
        setForceX(ZERO);
        setForceY(ZERO);
        /** The pool cue does not need any of these properties
         * Setting them to zero by calling the overridden methods
         * */
        setPositionX(null);
        setPositionY(null);
        setVelocityX(null);
        setVelocityY(null);
        setAccelerationX(null);
        setAccelerationY(null);
        /** The pool cue has no velocity therefore it will never move */
        isMoving=false;
    }

    public Shape getCollisionOverlap() {
        return collisionOverlap;
    }

    /**
     *
     * Overriding the Position,Velocity, and Acceleration setter methods
     * because the pool cue does not need any of them. These properties are always
     * equal to zero.
     *
     */

    @Override
    public void setPositionX(BigDecimal positionX) {
        super.setPositionX(ZERO);
    }

    @Override
    public void setPositionY(BigDecimal positionY) {
        super.setPositionY(ZERO);
    }

    @Override
    public void setVelocityX(BigDecimal velocityX) {
        super.setVelocityX(ZERO);
    }

    @Override
    public void setVelocityY(BigDecimal velocityY) {
        super.setVelocityY(ZERO);
    }

    @Override
    public void setAccelerationX(BigDecimal accelerationX) {
        super.setAccelerationX(ZERO);
    }

    @Override
    public void setAccelerationY(BigDecimal accelerationY) {
        super.setAccelerationY(ZERO);
    }

}

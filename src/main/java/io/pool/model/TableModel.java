package io.pool.model;

import java.math.BigDecimal;

public class TableModel extends PhysicsModule{

    public TableModel() {

        /** The table does not need any of these properties
         * Setting them to zero by calling the overridden methods
         * */
        setPositionX(null);
        setPositionY(null);
        setVelocityX(null);
        setVelocityY(null);
        setAccelerationX(null);
        setAccelerationY(null);
        setForceX(null);
        setForceY(null);
        /** The table has no velocity therefore it will never move */
        isMoving=false;
    }


    /**
     *
     * Overriding the Position,Velocity, Acceleration and Force setter methods
     * because the table does not need any of them. These properties are always
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

    @Override
    public void setForceX(BigDecimal forceX) {
        super.setForceX(ZERO);
    }

    @Override
    public void setForceY(BigDecimal forceY) {
        super.setForceY(ZERO);
    }
/*
    I will give each hole a number
    1               2                  3


    4               5                   6
     */

}

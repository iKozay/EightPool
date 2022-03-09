package io.pool.view;

import javafx.scene.shape.Rectangle;

public class PoolCueView {

    /** The cylinder that will represent the pool cue */
    private static Rectangle cue;

    double previousAngle = 0;

    public PoolCueView() {
        cue = new Rectangle();
        cue.setHeight(10);
        cue.setWidth(400);
        cue.setX(-100);
        cue.setY(-100);
    }

    public Rectangle getCue() {
        return cue;
    }

    public double getPreviousAngle() {
        return previousAngle;
    }

    public void setPreviousAngle(double angle) {
        this.previousAngle = angle;
    }


}

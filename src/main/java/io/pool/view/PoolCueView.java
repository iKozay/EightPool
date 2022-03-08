package io.pool.view;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;

public class PoolCueView {

    /** The cylinder that will represent the pool cue */
    private static Rectangle cue;

    double previousAngle = 0;

    public PoolCueView() {

        cue = new Rectangle();
        cue.setHeight(10);
        cue.setWidth(400);
        cue.setX(0);
        cue.setY(0);
        //cue.setRotationAxis(Point3D.ZERO);
        //PhongMaterial material = new PhongMaterial();
        //material.setDiffuseColor(Color.GOLD);
        //cue.setMaterial(material);

    }

    public Rectangle getCue() {
        return cue;
    }
    public double getXPos(){return cue.getX();}
    public double getYPos(){return cue.getY();}


    public double getPreviousAngle() {
        return previousAngle;
    }

    public void setPreviousAngle(double angle) {
        this.previousAngle = angle;
    }


}

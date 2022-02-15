package io.pool.view;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class PoolCueView {

    private static Cylinder cue;
    double xPos = 500;
    double yPos = 300;
    double centerX = xPos;
    double centerY = yPos;

    public PoolCueView() {

        cue = new Cylinder();
        cue.setRadius(10);
        cue.setHeight(400);
        cue.translateXProperty().set(xPos);
        cue.translateYProperty().set(yPos);
        cue.setRotationAxis(Point3D.ZERO);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.GOLD);
        cue.setMaterial(material);

    }

    double previousAngle = 0;


    public Cylinder getCue() {
        return cue;
    }
    public double getXPos(){return xPos;}
    public double getYPos(){return yPos;}

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

//    public double getCueAngle() {
//        return cue.getTransforms();
//    }


    public double getPreviousAngle() {
        return previousAngle;
    }
}

package io.pool.view;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

public class PoolCueView {

    private static Cylinder cue;
    double xPos = 500;
    double yPos = 100;
    double centerX = xPos;
    double centerY = yPos;
    double previousAngle = 0;

    public PoolCueView() {

        cue = new Cylinder();
        cue.setRadius(10);
        cue.setHeight(300);
        cue.translateXProperty().set(xPos);
        cue.translateYProperty().set(yPos);
        cue.setRotationAxis(Point3D.ZERO);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.GOLD);
        cue.setMaterial(material);

    }

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

    public double getPreviousAngle() {
        return previousAngle;
    }

    public void setPreviousAngle(double angle) {
        this.previousAngle = angle;
    }
    //    public double getCueAngle() {
//        return cue.getTransforms();
//    }

}

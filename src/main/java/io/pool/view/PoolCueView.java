package io.pool.view;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class PoolCueView {

    private static Cylinder cue;
    double xPos = 500;
    double yPos = 300;
    double centerX = xPos + 100;
    double centerY = yPos + 5;

    public PoolCueView() {

        cue = new Cylinder();
        cue.setRadius(10);
        cue.setHeight(200);
        cue.translateXProperty().set(xPos);
        cue.translateYProperty().set(yPos);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.GOLD);
        cue.setMaterial(material);
    }

    Rotate rotate;
    public void updateRotation(double rotation){

        rotate = new Rotate();
        rotate.setAngle(rotation);
        rotate.setPivotX(centerX);
        rotate.setPivotY(centerY);
        this.cue.getTransforms().add(rotate);
    }

    public Cylinder getCue() {
        return cue;
    }
    public double getxPos(){return xPos;}
    public double getyPos(){return yPos;}

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }
}

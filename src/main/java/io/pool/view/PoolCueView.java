package io.pool.view;

import io.pool.model.PoolCueModel;
import javafx.animation.RotateTransition;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class PoolCueView {

    private Cylinder cue;
    private double xPosition = 150;
    private double yPosition = 150;
    private double width = 10;
    private double length = 200;
    private PhongMaterial material = new PhongMaterial(Color.GREENYELLOW);
    private Point2D cueCenter= new Point2D(150, (yPosition+length)/2);


    public PoolCueView() {
        cue = new Cylinder();
        cue.setLayoutX(xPosition);
        cue.setLayoutY(yPosition);
        cue.setRadius(width);
        cue.setHeight(length);
        cue.setMaterial(material);
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(this.cue);
        rotate.setByAngle(100);
        rotate.play();

    }

    public Cylinder getCue() {
        return cue;
    }

    public Point2D getCueCenter() {return cueCenter;}

    public void setCueCenter(Point2D cueCenter) {this.cueCenter = cueCenter;}

    public void rotateCue(PoolCueModel cueModel){
        /*
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(this.cue);
        rotate.setByAngle(cueModel.getRotation());
        System.out.println(cueModel.getRotation()+ "ROTATION");
        rotate.play();

         */
        Rotate rotate = new Rotate();
        rotate.setAngle(cueModel.getRotation());
        rotate.setPivotX(this.cueCenter.getX());
        rotate.setPivotY(this.cueCenter.getY());
        this.cue.getTransforms().add(rotate);
    }
}

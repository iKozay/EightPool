package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.model.PoolCueModel;
import io.pool.view.PoolCueView;

import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class PoolCueController {

    PoolCueView poolCueView;
    PoolCueModel poolCueModel;

    public PoolCueController(PoolCueView poolCueView) {
        this.poolCueView = poolCueView;
        this.poolCueModel = new PoolCueModel();
    }

    boolean isPressed = false;

    public void handleRotateCue(Scene scene){

        scene.setOnMouseMoved(event -> {
            double deltaX = event.getX() - poolCueView.getXPos();
            double deltaY = event.getY() - (poolCueView.getYPos() + poolCueView.getCue().getHeight()/2);

            if (deltaX != 0) {
                double newAngleDegrees = Math.toDegrees(Math.atan2(deltaY, deltaX)) + 90;
                //System.out.println(newAngleDegrees);



                Rotate rotate = new Rotate(newAngleDegrees - poolCueView.getPreviousAngle());
               // rotate.setPivotX(BallController.bModelList.get(15).getPositionX().doubleValue());
               // rotate.setPivotY(BallController.bModelList.get(15).getPositionY().doubleValue());
                //rotate.setAngle();
                poolCueView.getCue().getTransforms().add(rotate);
                poolCueView.setPreviousAngle(newAngleDegrees);
                //System.out.println(newAngleDegrees);
            }
        });

    }

    double mouseAnchorX;
    double mouseAnchorY;
    double draggedDistanceX;
    double draggedDistanceY;
    double draggedDistance;
    public void hit(Scene scene){
        scene.setOnMousePressed(event -> {
            mouseAnchorX = event.getSceneX();
            mouseAnchorY = event.getSceneY();
            isPressed = true;
        });
        scene.setOnMouseDragged(event -> {
            draggedDistanceX = event.getSceneX() - mouseAnchorX;
            draggedDistanceY = event.getSceneY() - mouseAnchorY;
            draggedDistance = Math.sqrt(Math.pow(draggedDistanceX, 2) + Math.pow(draggedDistanceY, 2));
            Translate translate = new Translate();

            translate.setX(draggedDistance * Math.cos(poolCueView.getPreviousAngle() +2.7));
            translate.setY(draggedDistance * Math.sin(poolCueView.getPreviousAngle() +2.7));
            poolCueView.getCue().getTransforms().addAll(translate);
        });
        scene.setOnMouseReleased(event -> {
            isPressed = false;
        });

    }

    public void resetEventHandler(Scene scene) {
        scene.setOnMouseMoved(null);
        scene.setOnMousePressed(null);
        scene.setOnMouseDragged(null);
        scene.setOnMouseReleased(null);

    }
}

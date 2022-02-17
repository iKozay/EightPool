package io.pool.controller;

import io.pool.view.PoolCueView;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class PoolCueController {

    PoolCueView pcv = new PoolCueView();

    boolean isPressed = false;
    public void handleRotateCue(Scene scene){

        scene.setOnMouseMoved(event -> {
            double deltaX = event.getX() - pcv.getXPos();
            double deltaY = event.getY() - (pcv.getYPos() + pcv.getCue().getHeight()/2);

            if (deltaX != 0) {
                double newAngleDegrees = Math.toDegrees(Math.atan2(deltaY, deltaX)) + 90;
                System.out.println(newAngleDegrees);
                Rotate rotate = new Rotate();
                rotate.setPivotY(pcv.getCue().getHeight()/2);
                rotate.setAngle(newAngleDegrees - pcv.getPreviousAngle());
                pcv.getCue().getTransforms().add(rotate);
                pcv.setPreviousAngle(newAngleDegrees);
                System.out.println(newAngleDegrees);
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

            translate.setX(draggedDistance * Math.cos(pcv.getPreviousAngle() +2.7));
            translate.setY(draggedDistance * Math.sin(pcv.getPreviousAngle() +2.7));
            pcv.getCue().getTransforms().addAll(translate);
        });
        scene.setOnMouseReleased(event -> {
            isPressed = false;
        });

    }
}

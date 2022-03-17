package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.model.PoolCueModel;
import io.pool.view.PoolCueView;

import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.math.BigDecimal;

public class PoolCueController {

    PoolCueView poolCueView;
    PoolCueModel poolCueModel;
    private static final int MAX_POWER=300;
    private boolean enablePoolCueControl=false;

    public PoolCueController(PoolCueView poolCueView) {
        this.poolCueView = poolCueView;
        this.poolCueModel = new PoolCueModel();
    }

    boolean isPressed = false;

    public void handleRotateCue(Scene scene){

        scene.setOnMouseMoved(event -> {
            if(enablePoolCueControl) {
                if (!isPressed) {
                    poolCueView.getCue().setX(BallController.whiteBallModel.getPositionX().doubleValue() + (BallModel.RADIUS));
                    poolCueView.getCue().setY(BallController.whiteBallModel.getPositionY().doubleValue() - (poolCueView.getCue().getHeight() / 2));

                    double deltaX = event.getX() - BallController.whiteBallModel.getPositionX().doubleValue();
                    double deltaY = event.getY() - BallController.whiteBallModel.getPositionY().doubleValue();
                    if (deltaX != 0) {
                        double newAngleDegrees = Math.toDegrees(Math.atan2(deltaY, deltaX));
                        Rotate rotate = new Rotate(newAngleDegrees - poolCueView.getPreviousAngle());
                        rotate.setPivotX(BallController.whiteBallModel.getPositionX().doubleValue());
                        rotate.setPivotY(BallController.whiteBallModel.getPositionY().doubleValue());
                        poolCueView.getCue().getTransforms().add(rotate);
                        poolCueView.setPreviousAngle(newAngleDegrees);
                    }
                }
            }
        });

    }

    double draggedX;//
    double draggedY;//
    double mouseXLock;//
    double mouseYLock;//
    double draggedTotal;//
    double deltaX;
    double deltaY;
    double maxDisplacementX;
    double maxDisplacementY;

    public void hit(Scene scene){
        scene.setOnMousePressed(event -> {
            if(enablePoolCueControl) {
                deltaX = (event.getX() - poolCueView.getCue().getLayoutX());
                deltaY = (event.getY() - poolCueView.getCue().getLayoutY());
                mouseXLock = event.getX();
                mouseYLock = event.getY();
                double poolCueAngle = Math.toRadians(poolCueView.getPreviousAngle());
                maxDisplacementX = MAX_POWER * Math.cos(poolCueAngle);
                maxDisplacementY = MAX_POWER * Math.sin(poolCueAngle);
                isPressed = true;
            }
        });
        scene.setOnMouseDragged(event -> {
            if(enablePoolCueControl){
                    draggedX = Math.abs(event.getX()-mouseXLock);
                    draggedY = Math.abs(event.getY()-mouseYLock);
                    draggedTotal = Math.sqrt(Math.pow(draggedX,2)+ Math.pow(draggedY,2));
                    poolCueView.getCue().setLayoutX(draggedTotal*Math.cos(Math.toRadians(poolCueView.getPreviousAngle())));
                    poolCueView.getCue().setLayoutY(draggedTotal*Math.sin(Math.toRadians(poolCueView.getPreviousAngle())));
            }
        });

        scene.setOnMouseReleased(event -> {
            isPressed = false;
            poolCueView.getCue().setLayoutX(0);
            poolCueView.getCue().setLayoutY(0);
        });

    }

    public void resetEventHandler(Scene scene) {
        scene.setOnMouseMoved(null);
        scene.setOnMousePressed(null);
        scene.setOnMouseDragged(null);
        scene.setOnMouseReleased(null);

    }

    public void enablePoolCueControl() {
        enablePoolCueControl=true;
    }
    public void disablePoolCueControl() {
        enablePoolCueControl=false;
    }
}

package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.model.PoolCueModel;
import io.pool.view.PoolCueView;

import javafx.scene.Scene;
import javafx.scene.transform.Rotate;

import java.math.BigDecimal;

public class PoolCueController {

    PoolCueView poolCueView;
    PoolCueModel poolCueModel;
    private static final int MAX_DISTANCE =100;
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
                    poolCueView.getCue().setX(BallController.whiteBallModel.getPositionX().doubleValue());// + (BallModel.RADIUS));
                    poolCueView.getCue().setY(BallController.whiteBallModel.getPositionY().doubleValue());// - (poolCueView.getCue().getImage().getHeight() / 2));

                    System.out.println(BallController.whiteBallModel.getPositionX().doubleValue()+" ; "+BallController.whiteBallModel.getPositionY().doubleValue());
                    System.out.println(BallController.whiteBallView.getBall().getLayoutX()+" ; "+BallController.whiteBallView.getBall().getLayoutY());
                    System.out.println(poolCueView.getCue().getX()+" ; "+poolCueView.getCue().getY());
                    System.out.println(poolCueView.getCue().getLayoutX()+" ; "+poolCueView.getCue().getLayoutY());
                    System.out.println("/////////////////////////////////////");
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
                maxDisplacementX = MAX_DISTANCE * Math.cos(poolCueAngle);
                maxDisplacementY = MAX_DISTANCE * Math.sin(poolCueAngle);
                isPressed = true;
            }
        });
        scene.setOnMouseDragged(event -> {
            if(enablePoolCueControl){
                    draggedX = event.getX()-mouseXLock;
                    draggedY = event.getY()-mouseYLock;
                    if(draggedX<0) draggedX=0;
                    if(draggedY<0) draggedY=0;
                    draggedTotal = Math.sqrt(Math.pow(draggedX,2)+ Math.pow(draggedY,2));
                    if(draggedTotal> MAX_DISTANCE) draggedTotal= MAX_DISTANCE;
                /**
                 * make poolCueView an actual image of pool cue in resources
                 * ---------------------------
                 * Debug poolcue positioning
                 */
                        poolCueView.getCue().setLayoutX(draggedTotal * Math.cos(Math.toRadians(poolCueView.getPreviousAngle())));
                        poolCueView.getCue().setLayoutY(draggedTotal * Math.sin(Math.toRadians(poolCueView.getPreviousAngle())));
            }
        });

        scene.setOnMouseReleased(event -> {
            isPressed = false;
            if(poolCueView.getCue().getLayoutX()!=0&&poolCueView.getCue().getLayoutY()!=0) {
                BallController.whiteBallModel.setVelocityX(new BigDecimal(-poolCueView.getCue().getLayoutX() / 10));
                BallController.whiteBallModel.setVelocityY(new BigDecimal(-poolCueView.getCue().getLayoutY() / 10));
                poolCueView.getCue().setLayoutX(0);
                poolCueView.getCue().setLayoutY(0);
                disablePoolCueControl();
            }
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

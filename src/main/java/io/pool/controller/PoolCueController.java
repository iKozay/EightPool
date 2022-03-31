package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.model.PoolCueModel;
import io.pool.model.TableBorderModel;
import io.pool.view.BallView;
import io.pool.view.GameView;
import io.pool.view.PoolCueView;

import io.pool.view.TableView;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

import java.math.BigDecimal;

public class PoolCueController {

    PoolCueView poolCueView;
    PoolCueModel poolCueModel;
    private static final int MAX_DISTANCE =100;
    private boolean enablePoolCueControl=false;

    public PoolCueController(PoolCueView poolCueView) {
        this.poolCueView = poolCueView;
        this.poolCueView.setRotationTransform(this);
        this.poolCueModel = new PoolCueModel();
    }
    Rotate rotate = new Rotate();
    boolean isPressed = false;

    public void handleRotateCue(Scene scene){

        scene.setOnMouseMoved(event -> {
            if (enablePoolCueControl) {
            if (GameController.gameLoopTimer.isActive){
                    if (!isPressed) {
                        double deltaX = event.getX() - BallController.whiteBallModel.getPositionX().doubleValue();
                        double deltaY = event.getY() - BallController.whiteBallModel.getPositionY().doubleValue();
                        if (deltaX != 0) {
                            double newAngleDegrees = Math.toDegrees(Math.atan2(deltaY, deltaX));
                            rotate.setAngle(newAngleDegrees);
                            //Rotate rotate = new Rotate(newAngleDegrees - poolCueView.getPreviousAngle());
                            poolCueView.setPreviousAngle(newAngleDegrees);
                            poolCueDirectionLine();
                        }
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
    double poolCueAngle;
    boolean rightButtonClicked;

    public void hit(Scene scene){
        scene.setOnMousePressed(event -> {
            if (enablePoolCueControl) {
                if (GameController.gameLoopTimer.isActive) {
                if (event.getButton() == MouseButton.PRIMARY) {
                        mouseXLock = event.getX();
                        mouseYLock = event.getY();
                        poolCueAngle = Math.toRadians(poolCueView.getPreviousAngle());
                        isPressed = true;
                    }
                }
            }
        });
        scene.setOnMouseDragged(event -> {
            if (enablePoolCueControl) {
            if (GameController.gameLoopTimer.isActive) {
                    draggedX = Math.abs(event.getX() - mouseXLock);
                    draggedY = Math.abs(event.getY() - mouseYLock);
                    draggedTotal = Math.sqrt(Math.pow(draggedX, 2) + Math.pow(draggedY, 2));
                    if (draggedTotal > MAX_DISTANCE) draggedTotal = MAX_DISTANCE;
                    poolCueView.getCue().setLayoutX(draggedTotal * Math.cos(Math.toRadians(poolCueView.getPreviousAngle())));
                    poolCueView.getCue().setLayoutY(draggedTotal * Math.sin(Math.toRadians(poolCueView.getPreviousAngle())));
                }
            }
        });

            scene.setOnMouseReleased(event -> {
                if (GameController.gameLoopTimer.isActive) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        isPressed = false;
                        if (poolCueView.getCue().getLayoutX() != 0 && poolCueView.getCue().getLayoutY() != 0) {
                            //poolCueView.getCue().getTransforms().clear();
                            //poolCueView.getPoolLine().getTransforms().clear();
                            poolCueView.setPreviousAngle(0);
                            BallController.whiteBallModel.setVelocityX(new BigDecimal(-poolCueView.getCue().getLayoutX() / 10));
                            BallController.whiteBallModel.setVelocityY(new BigDecimal(-poolCueView.getCue().getLayoutY() / 10));
                            poolCueView.getCue().setLayoutX(0);
                            poolCueView.getCue().setLayoutY(0);
                            disablePoolCueControl();
                            //poolCueView.getPath().getElements().clear();
                            //poolCueView.getPath().getTransforms().clear();
                            GameController.waitingForInput = false;
                        }
                    }
                }
            });

        scene.setOnMouseClicked(event -> {
            if (GameController.gameLoopTimer.isActive) {
                isPressed = false;
                poolCueView.getCue().setLayoutX(0);
                poolCueView.getCue().setLayoutY(0);
                poolCueView.getCue().setLayoutX(0);
                poolCueView.getCue().setLayoutY(0);
                rightButtonClicked = true;
            }
        });

    }

    public void resetEventHandler(Scene scene) {
        //scene.removeEventHandler(MouseEvent.MOUSE_MOVED,this);
        //scene.setOnMouseMoved(null);
        //scene.setOnMousePressed(null);
        //scene.setOnMouseDragged(null);
        //scene.setOnMouseReleased(null);
        //poolCueView.getCue().getTransforms().clear();
        poolCueView.setPreviousAngle(0);
        //poolCueView.getPoolLine().getTransforms().clear();
        //poolCueView.getPath().getTransforms().clear();
        disablePoolCueControl();

    }
    MoveTo moveTo = new MoveTo();
    LineTo lineTo = new LineTo();
    public void poolCueDirectionLine(){

        moveTo.setX(BallController.whiteBallView.getBall().getLayoutX());
        moveTo.setY(BallController.whiteBallView.getBall().getLayoutY());
        lineTo.setX(poolCueView.getCue().getX()-1000);
        lineTo.setY(poolCueView.getCue().getY());

        // TABLE BORDER
        for(TableBorderModel tbm : TableBorderModel.tableBorder){
            Shape intersect = Shape.intersect(poolCueView.getPath(),tbm);
            if(intersect.getBoundsInLocal().getWidth()!=-1){
                poolCueView.getPoolLine().setEndX(intersect.getBoundsInLocal().getCenterX()-intersect.getBoundsInLocal().getWidth()/2);
                poolCueView.getPoolLine().setEndY(intersect.getBoundsInLocal().getCenterY()-intersect.getBoundsInLocal().getHeight()/2);
                poolCueView.getPoolLine().toFront();
            }
        }

        // BALL

        for(BallView bView : BallController.bViewList){
            if(BallController.getBallModelFromBallView(bView).equals(BallController.whiteBallModel)){
                break;
            }
            Shape intersect = Shape.intersect(poolCueView.getPath(),bView.getCircleFromSphere());
            if(intersect.getBoundsInLocal().getWidth()!=-1){
                double currentDistance = Math.hypot((poolCueView.getPoolLine().getStartX()-poolCueView.getPoolLine().getEndX()),(poolCueView.getPoolLine().getStartY()-poolCueView.getPoolLine().getEndY()));
                double newDistance = Math.hypot((poolCueView.getPoolLine().getStartX()-intersect.getBoundsInLocal().getCenterX()),(poolCueView.getPoolLine().getStartY()-intersect.getBoundsInLocal().getCenterY()));
                if(newDistance<currentDistance) {
                    poolCueView.getPoolLine().setEndX(intersect.getBoundsInLocal().getCenterX());
                    poolCueView.getPoolLine().setEndY(intersect.getBoundsInLocal().getCenterY());
                }
                poolCueView.getPoolLine().toFront();
            }
        }
    }


    public void enablePoolCueControl() {
        enablePoolCueControl=true;
    }
    public void disablePoolCueControl() {
        enablePoolCueControl=false;
    }

    public PoolCueView getCueView() {
        return poolCueView;
    }

    public boolean isEnablePoolCueControl() {
        return enablePoolCueControl;
    }

    public MoveTo getMoveTo() {
        return moveTo;
    }

    public LineTo getLineTo() {
        return lineTo;
    }

    public Rotate getRotate() {
        return rotate;
    }
}

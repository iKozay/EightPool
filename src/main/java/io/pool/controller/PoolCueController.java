package io.pool.controller;

import io.pool.Database.BallConfigurationDB;
import io.pool.model.PoolCueModel;
import io.pool.model.TableBorderModel;
import io.pool.view.BallView;
import io.pool.view.PoolCueView;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class PoolCueController {

    private static final Set<KeyCode> keysCurrentlyDown = new HashSet<>();
    public static boolean keyboardOnly = false;
    public static boolean cueHelperEnabled=true;
    private GameController gameController;

    PoolCueView poolCueView;
    PoolCueModel poolCueModel;
    public static final int MAX_DISTANCE = 100;
    private boolean enablePoolCueControl = false;

    public PoolCueController(PoolCueView poolCueView, GameController gameController) {
        this.gameController = gameController;
        this.poolCueView = poolCueView;
        this.poolCueView.setRotationTransform(this);
        this.poolCueModel = new PoolCueModel();
    }

    Rotate rotate = new Rotate();

    public void handleRotateCue(Scene scene) {
            scene.setOnMouseMoved(event -> {
                if (!keyboardOnly) {
                    if (enablePoolCueControl) {
                        if (gameController.gameLoopTimer.isActive) {
                            double deltaX = event.getX() - BallController.whiteBallModel.getPositionX().doubleValue();
                            double deltaY = event.getY() - BallController.whiteBallModel.getPositionY().doubleValue();
                            if (deltaX != 0) {
                                double newAngleDegrees = Math.toDegrees(Math.atan2(deltaY, deltaX));
                                setRotation(newAngleDegrees);
                            }
                        }
                    }
                }
            });
    }

    public void hit(Scene scene) {
        scene.setOnKeyPressed((keyEvent -> {
            if (gameController.gameLoopTimer.isActive) {
                keysCurrentlyDown.add(keyEvent.getCode());
                if (keyboardOnly) {
                    if (keysCurrentlyDown.contains(KeyCode.A) || keysCurrentlyDown.contains(KeyCode.D)) {
                        double newAngle = poolCueView.getPreviousAngle();
                        if (keysCurrentlyDown.contains(KeyCode.A)) newAngle -= 2;
                        if (keysCurrentlyDown.contains(KeyCode.D)) newAngle += 2;
                        setRotation(newAngle);
                    } else if (keysCurrentlyDown.contains(KeyCode.SHIFT)) {
                        shoot();
                    }
                }
                if (keysCurrentlyDown.contains(KeyCode.W) || keysCurrentlyDown.contains(KeyCode.S)) {
                    double draggedTotal = Math.sqrt(Math.pow(poolCueView.getCue().getLayoutX(), 2) + Math.pow(poolCueView.getCue().getLayoutY(), 2));
                    if (keysCurrentlyDown.contains(KeyCode.W)) draggedTotal -= 3;
                    if (keysCurrentlyDown.contains(KeyCode.S)) draggedTotal += 3;
                    if (draggedTotal < 0) draggedTotal = 0;
                    if (draggedTotal > MAX_DISTANCE) draggedTotal = MAX_DISTANCE;
                    setPower(draggedTotal);
                }
            }
        }));
        scene.setOnKeyReleased((keyEvent -> {
            keysCurrentlyDown.remove(keyEvent.getCode());
        }));

            scene.setOnMouseReleased(event -> {
                        if (!keyboardOnly) {
                            shoot();
                        }
            });

    }

    public void resetPoolCue() {
        poolCueView.setPreviousAngle(0);
        disablePoolCueControl();
    }

    MoveTo moveTo = new MoveTo();
    LineTo lineTo = new LineTo();

    public void poolCueDirectionLine() {
        if(cueHelperEnabled) {
            moveTo.setX(BallController.whiteBallView.getBall().getLayoutX());
            moveTo.setY(BallController.whiteBallView.getBall().getLayoutY());
            lineTo.setX(poolCueView.getCue().getX() - 1000);
            lineTo.setY(poolCueView.getCue().getY());

            // TABLE BORDER
            for (TableBorderModel tbm : TableBorderModel.tableBorder) {
                Shape intersect = Shape.intersect(poolCueView.getPath(), tbm);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    poolCueView.getPoolLine().setEndX(intersect.getBoundsInLocal().getCenterX() - intersect.getBoundsInLocal().getWidth() / 2);
                    poolCueView.getPoolLine().setEndY(intersect.getBoundsInLocal().getCenterY() - intersect.getBoundsInLocal().getHeight() / 2);
                    poolCueView.getPoolLine().toFront();
                }
            }

            // BALL

            for (BallView bView : BallController.bViewList) {
                if (BallController.getBallModelFromBallView(bView).equals(BallController.whiteBallModel)) {
                    break;
                }
                Shape intersect = Shape.intersect(poolCueView.getPath(), bView.getCircleFromSphere());
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    double currentDistance = Math.hypot((poolCueView.getPoolLine().getStartX() - poolCueView.getPoolLine().getEndX()), (poolCueView.getPoolLine().getStartY() - poolCueView.getPoolLine().getEndY()));
                    double newDistance = Math.hypot((poolCueView.getPoolLine().getStartX() - intersect.getBoundsInLocal().getCenterX()), (poolCueView.getPoolLine().getStartY() - intersect.getBoundsInLocal().getCenterY()));
                    if (newDistance < currentDistance) {
                        poolCueView.getPoolLine().setEndX(intersect.getBoundsInLocal().getCenterX());
                        poolCueView.getPoolLine().setEndY(intersect.getBoundsInLocal().getCenterY());
                    }
                    poolCueView.getPoolLine().toFront();
                }
            }
        }
    }

    private void setRotation(double rotation){
        poolCueView.setPreviousAngle(rotation);
        rotate.setAngle(rotation);
        poolCueDirectionLine();
        double draggedTotal = Math.sqrt(Math.pow(poolCueView.getCue().getLayoutX(), 2) + Math.pow(poolCueView.getCue().getLayoutY(), 2));
        poolCueView.getCue().setLayoutX(draggedTotal * Math.cos(Math.toRadians(poolCueView.getPreviousAngle())));
        poolCueView.getCue().setLayoutY(draggedTotal * Math.sin(Math.toRadians(poolCueView.getPreviousAngle())));
    }

    private void setPower(double draggedTotal){
        poolCueView.getCue().setLayoutX(draggedTotal * Math.cos(Math.toRadians(poolCueView.getPreviousAngle())));
        poolCueView.getCue().setLayoutY(draggedTotal * Math.sin(Math.toRadians(poolCueView.getPreviousAngle())));
    }

    private void shoot(){
        if (gameController.gameLoopTimer.isActive) {
                if (poolCueView.getCue().getLayoutX() != 0 && poolCueView.getCue().getLayoutY() != 0) {
                    poolCueView.setPreviousAngle(0);
                    double newVelocityX = -poolCueView.getCue().getLayoutX() / 7;
                    double newVelocityY = -poolCueView.getCue().getLayoutY() / 7;
                    BallController.whiteBallModel.setVelocityX(new BigDecimal(newVelocityX));
                    BallController.whiteBallModel.setVelocityY(new BigDecimal(newVelocityY));
                    BallController.whiteBallModel.setIsMoving();
                    poolCueView.getCue().setLayoutX(0);
                    poolCueView.getCue().setLayoutY(0);
                    disablePoolCueControl();
                    SoundController.BallHit();
                    gameController.setWaitingForInput(false);
                    BallConfigurationDB.hasBeenCalled=false;
                    gameController.getBallController().makeUnDraggable();
                }
        }
    }

    public void setPoolCue(double power, double rotation){
        setRotation(rotation);
        setPower(power);
        shoot();
    }

    public void enablePoolCueControl() {
        enablePoolCueControl = true;
    }

    public void disablePoolCueControl() {
        enablePoolCueControl = false;
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



package io.pool.controller;

import io.pool.model.PoolCueModel;
import io.pool.model.TableBorderModel;
import io.pool.view.BallView;
import io.pool.view.PoolCueView;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class PoolCueController {

    private static final Set<KeyCode> keysCurrentlyDown = new HashSet<>();


    PoolCueView poolCueView;
    PoolCueModel poolCueModel;
    private static final int MAX_DISTANCE = 100;
    private boolean enablePoolCueControl = false;

    public PoolCueController(PoolCueView poolCueView) {
        this.poolCueView = poolCueView;
        this.poolCueView.setRotationTransform(this);
        this.poolCueModel = new PoolCueModel();
    }

    Rotate rotate = new Rotate();

    boolean keyboardOnly = false;


    public void handleRotateCue(Scene scene) {
        if (!keyboardOnly) {
            scene.setOnMouseMoved(event -> {
                if (enablePoolCueControl) {
                    if (GameController.gameLoopTimer.isActive) {
                        double deltaX = event.getX() - BallController.whiteBallModel.getPositionX().doubleValue();
                        double deltaY = event.getY() - BallController.whiteBallModel.getPositionY().doubleValue();
                        if (deltaX != 0) {
                            double newAngleDegrees = Math.toDegrees(Math.atan2(deltaY, deltaX));
                            rotate.setAngle(newAngleDegrees);
                            poolCueView.setPreviousAngle(newAngleDegrees);
                            poolCueDirectionLine();
                            double draggedTotal = Math.sqrt(Math.pow(poolCueView.getCue().getLayoutX(), 2) + Math.pow(poolCueView.getCue().getLayoutY(), 2));
                            poolCueView.getCue().setLayoutX(draggedTotal * Math.cos(Math.toRadians(poolCueView.getPreviousAngle())));
                            poolCueView.getCue().setLayoutY(draggedTotal * Math.sin(Math.toRadians(poolCueView.getPreviousAngle())));
                        }
                    }
                }
            });
        }
    }

    //    double draggedX;//
//    double draggedY;//
//    double mouseXLock;//
//    double mouseYLock;//
//    //double draggedTotal;//
//    double poolCueAngle;
    boolean rightButtonClicked;

    public void hit(Scene scene) {
//        scene.setOnMousePressed(event -> {
//            if (enablePoolCueControl) {
//                if (GameController.gameLoopTimer.isActive) {
//                if (event.getButton() == MouseButton.PRIMARY) {
//                        mouseXLock = event.getX();
//                        mouseYLock = event.getY();
//                        poolCueAngle = Math.toRadians(poolCueView.getPreviousAngle());
//                        isPressed = true;
//                    }
//                }
//            }
//        });
//        scene.setOnMouseDragged(event -> {
//            if (enablePoolCueControl) {
//            if (GameController.gameLoopTimer.isActive) {
//                    draggedX = Math.abs(event.getX() - mouseXLock);
//                    draggedY = Math.abs(event.getY() - mouseYLock);
//                    draggedTotal = Math.sqrt(Math.pow(draggedX, 2) + Math.pow(draggedY, 2));
//                    if (draggedTotal > MAX_DISTANCE) draggedTotal = MAX_DISTANCE;
//                    poolCueView.getCue().setLayoutX(draggedTotal * Math.cos(Math.toRadians(poolCueView.getPreviousAngle())));
//                    poolCueView.getCue().setLayoutY(draggedTotal * Math.sin(Math.toRadians(poolCueView.getPreviousAngle())));
//                }
//            }
//        });
        scene.setOnKeyPressed((keyEvent -> {
            if (GameController.gameLoopTimer.isActive) {
                keysCurrentlyDown.add(keyEvent.getCode());
                double draggedTotal = Math.sqrt(Math.pow(poolCueView.getCue().getLayoutX(), 2) + Math.pow(poolCueView.getCue().getLayoutY(), 2));
                if (keyboardOnly) {
                    if (keysCurrentlyDown.contains(KeyCode.A) || keysCurrentlyDown.contains(KeyCode.D)) {
                        if (keysCurrentlyDown.contains(KeyCode.A))
                            poolCueView.setPreviousAngle(poolCueView.getPreviousAngle() - 1);
                        if (keysCurrentlyDown.contains(KeyCode.D))
                            poolCueView.setPreviousAngle(poolCueView.getPreviousAngle() + 1);
                        rotate.setAngle(poolCueView.getPreviousAngle());
                        poolCueDirectionLine();
                        poolCueView.getCue().setLayoutX(draggedTotal * Math.cos(Math.toRadians(poolCueView.getPreviousAngle())));
                        poolCueView.getCue().setLayoutY(draggedTotal * Math.sin(Math.toRadians(poolCueView.getPreviousAngle())));
                    } else if (keysCurrentlyDown.contains(KeyCode.SHIFT)) {
                        if (poolCueView.getCue().getLayoutX() != 0 && poolCueView.getCue().getLayoutY() != 0) {
                            poolCueView.setPreviousAngle(0);
                            BallController.whiteBallModel.setVelocityX(new BigDecimal(-poolCueView.getCue().getLayoutX() / 8));
                            BallController.whiteBallModel.setVelocityY(new BigDecimal(-poolCueView.getCue().getLayoutY() / 8));
                            BallController.whiteBallModel.setIsMoving();
                            poolCueView.getCue().setLayoutX(0);
                            poolCueView.getCue().setLayoutY(0);
                            disablePoolCueControl();
                            SoundController.BallHit();
                            GameController.waitingForInput = false;
                        }
                    }
                }
                if (keysCurrentlyDown.contains(KeyCode.W)) {
                    draggedTotal -= 2;
                    if (draggedTotal < 0) {
                        draggedTotal = 0;
                    }
                    poolCueView.getCue().setLayoutX(draggedTotal * Math.cos(Math.toRadians(poolCueView.getPreviousAngle())));
                    poolCueView.getCue().setLayoutY(draggedTotal * Math.sin(Math.toRadians(poolCueView.getPreviousAngle())));

                } else if (keysCurrentlyDown.contains(KeyCode.S)) {
                    draggedTotal += 2;
                    if (draggedTotal > MAX_DISTANCE) draggedTotal = MAX_DISTANCE;
                    poolCueView.getCue().setLayoutX(draggedTotal * Math.cos(Math.toRadians(poolCueView.getPreviousAngle())));
                    poolCueView.getCue().setLayoutY(draggedTotal * Math.sin(Math.toRadians(poolCueView.getPreviousAngle())));

                }
            }
        }));
        scene.setOnKeyReleased((keyEvent -> {
            keysCurrentlyDown.remove(keyEvent.getCode());
        }));

        if (!keyboardOnly) {
            scene.setOnMouseReleased(event -> {
                if (GameController.gameLoopTimer.isActive) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (poolCueView.getCue().getLayoutX() != 0 && poolCueView.getCue().getLayoutY() != 0) {
                            poolCueView.setPreviousAngle(0);
                            BallController.whiteBallModel.setVelocityX(new BigDecimal(-poolCueView.getCue().getLayoutX() / 8));
                            BallController.whiteBallModel.setVelocityY(new BigDecimal(-poolCueView.getCue().getLayoutY() / 8));
                            BallController.whiteBallModel.setIsMoving();
                            poolCueView.getCue().setLayoutX(0);
                            poolCueView.getCue().setLayoutY(0);
                            disablePoolCueControl();
                            SoundController.BallHit();
                            GameController.waitingForInput = false;
                        }
                    }
                }
            });
        }
//        scene.setOnMouseClicked(event -> {
//            if (GameController.gameLoopTimer.isActive) {
//                poolCueView.getCue().setLayoutX(0);
//                poolCueView.getCue().setLayoutY(0);
//                poolCueView.getCue().setLayoutX(0);
//                poolCueView.getCue().setLayoutY(0);
//                rightButtonClicked = true;
//            }
//        });

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

    public void poolCueDirectionLine() {

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



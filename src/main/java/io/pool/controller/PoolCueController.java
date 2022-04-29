package io.pool.controller;

import io.pool.Database.BallConfigurationDB;
import io.pool.model.BallModel;
import io.pool.model.TableBorderModel;
import io.pool.view.BallView;
import io.pool.view.PoolCueView;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class PoolCueController {

    private static final Set<KeyCode> keysCurrentlyDown = new HashSet<>();
    public static boolean keyboardOnly = false;
    public static boolean mouseOnly = true;
    public static boolean cueHelperEnabled=true;
    private GameController gameController;

    PoolCueView poolCueView;
    public static final int MAX_DISTANCE = 100;
    private boolean enablePoolCueControl = false;

    public PoolCueController(PoolCueView poolCueView, GameController gameController) {
        this.gameController = gameController;
        this.poolCueView = poolCueView;
        this.poolCueView.setRotationTransform(this);
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

    double mouseXLock;
    double mouseYLock;
    boolean isPressed=false;
    double draggedX;
    double draggedY;
    double draggedTotal;

    public void hit(Scene scene) {
        AtomicBoolean resetMouseLock= new AtomicBoolean(false);
        scene.setOnKeyPressed((keyEvent -> {
            if(!mouseOnly) {
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
            }else{
                if (gameController.gameLoopTimer.isActive) {
                    if(keyEvent.getCode().equals(KeyCode.CONTROL)){
                        if(!resetMouseLock.get()) resetMouseLock.set(true);
                        setPoolCue(0,gameController.getPoolCueController().poolCueView.getPreviousAngle());
                    }
                }
            }
        }));
        scene.setOnKeyReleased((keyEvent -> {
            if(!mouseOnly) keysCurrentlyDown.remove(keyEvent.getCode());
        }));

        scene.setOnMouseReleased(event -> {
                    if (!keyboardOnly) {
                        shoot();
                    }
        });
        scene.setOnMousePressed(event -> {
            if(mouseOnly) {
                if (enablePoolCueControl) {
                    if (gameController.gameLoopTimer.isActive) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            mouseXLock = event.getX();
                            mouseYLock = event.getY();
                            isPressed = true;
                        }
                    }
                }
            }
        });
        scene.setOnMouseDragged(event -> {
            if(mouseOnly) {
                if (enablePoolCueControl) {
                    if (gameController.gameLoopTimer.isActive) {
                        if(resetMouseLock.get()){
                            mouseXLock=event.getX();
                            mouseYLock=event.getY();
                            resetMouseLock.set(false);
                        }else {
                            draggedX = Math.abs(event.getX() - mouseXLock);
                            draggedY = Math.abs(event.getY() - mouseYLock);
                            draggedTotal = Math.sqrt(Math.pow(draggedX, 2) + Math.pow(draggedY, 2));
                            if (draggedTotal > MAX_DISTANCE) draggedTotal = MAX_DISTANCE;
                            setPower(draggedTotal);
                        }
                    }
                }
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
            poolCueView.getPath().getElements().clear();
            moveTo.setX(BallController.whiteBallView.getBall().getLayoutX() - BallModel.RADIUS);
            moveTo.setY(BallController.whiteBallView.getBall().getLayoutY());
            lineTo.setX(BallController.whiteBallView.getBall().getLayoutX() - 2000);
            lineTo.setY(BallController.whiteBallView.getBall().getLayoutY());
            poolCueView.getPath().getElements().addAll(moveTo, lineTo);

            // TABLE BORDER
            for (TableBorderModel tbm : TableBorderModel.tableBorder) {
                Shape intersect = Shape.intersect(poolCueView.getPath(), tbm);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    double newDistance = Math.hypot((intersect.getBoundsInLocal().getCenterX()-poolCueView.getPoolLine().getStartX()), (intersect.getBoundsInLocal().getCenterY()-poolCueView.getPoolLine().getStartY()));
                    poolCueView.getPoolLine().setEndX(poolCueView.getPoolLine().getStartX()-newDistance+BallModel.RADIUS);
                    poolCueView.getPoolLine().setEndY(poolCueView.getPoolLine().getStartY());
                }
            }

            // BALL

            for (BallView bView : BallController.bViewList) {
                if (BallController.getBallModelFromBallView(bView).equals(BallController.whiteBallModel)) {
                    continue;
                }
                Shape intersect = Shape.intersect(poolCueView.getPath(), bView.getCircleFromSphere());
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    double currentDistance = Math.hypot(poolCueView.getPoolLine().getEndX()-poolCueView.getPoolLine().getStartX(),poolCueView.getPoolLine().getEndY()-poolCueView.getPoolLine().getStartY());
                    double newDistance = Math.hypot((intersect.getBoundsInLocal().getCenterX()-poolCueView.getPoolLine().getStartX()), (intersect.getBoundsInLocal().getCenterY()-poolCueView.getPoolLine().getStartY()));
                    if (newDistance < currentDistance) {
                        poolCueView.getPoolLine().setEndX(poolCueView.getPoolLine().getStartX()-newDistance+2*BallModel.RADIUS);
                        poolCueView.getPoolLine().setEndY(poolCueView.getPoolLine().getStartY());
                    }
                }
            }
            poolCueView.getPoolLine().toFront();
            poolCueView.getBallCollisionCircle().toFront();
            poolCueView.getBallCollisionCircle().setCenterX(poolCueView.getPoolLine().getEndX());
            poolCueView.getBallCollisionCircle().setCenterY(poolCueView.getPoolLine().getEndY());
        }
    }

    private void setRotation(double rotation){
        poolCueView.setPreviousAngle(rotation);
        rotate.setAngle(rotation);
        if(!gameController.getAiController().isAITraining()) poolCueDirectionLine();
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
                    gameController.setFirstPlay(false);
                    gameController.getBallController().makeUnDraggable();
                    poolCueView.setPreviousAngle(0);
                    double newVelocityX = -poolCueView.getCue().getLayoutX() / 5;
                    double newVelocityY = -poolCueView.getCue().getLayoutY() / 5;
                    BallController.whiteBallModel.setVelocityX(new BigDecimal(newVelocityX));
                    BallController.whiteBallModel.setVelocityY(new BigDecimal(newVelocityY));
                    BallController.whiteBallModel.setIsMoving();
                    poolCueView.getCue().setLayoutX(0);
                    poolCueView.getCue().setLayoutY(0);
                    disablePoolCueControl();
                    poolCueView.getPoolLine().setVisible(false);
                    poolCueView.getBallCollisionCircle().setVisible(false);
                    SoundController.BallHit();
                    //if(!gameController.getAiController().isAITraining())SoundController.BallHit();
                    gameController.setWaitingForInput(false);
                    BallConfigurationDB.hasBeenCalled=false;
                    gameController.playerShot();
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



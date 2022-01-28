package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.view.BallView;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class BallController {
    private BallView bView;
    private BallModel bModel;

    public BallController(BallView bView) {
        this.bView = bView;
        bModel = new BallModel(20);
    }
    public void detectCollisionWithTable(Rectangle table){
        if(((bModel.getBallPosition().getX()-bModel.getRadius())<=table.getX())||((bModel.getBallPosition().getX()+bModel.getRadius())>=(table.getX()+table.getWidth()))){
            double newXVelocity = -bModel.getBallVector().getX();
            bModel.setBallVector(new Point2D(newXVelocity,bModel.getBallVector().getY()));
        }
        if(((bModel.getBallPosition().getY()-bModel.getRadius())<=table.getY())||((bModel.getBallPosition().getY()+bModel.getRadius())>=(table.getY()+table.getHeight()))){
            double newYVelocity = -bModel.getBallVector().getY();
            bModel.setBallVector(new Point2D(bModel.getBallVector().getX(),newYVelocity));
        }
    }

    public void updateBallPosition() {
        bModel.setBallPosition(bModel.getBallPosition().add(bModel.getBallVector()));
        bView.getBall().setLayoutX(bModel.getBallPosition().getX());
        bView.getBall().setLayoutY(bModel.getBallPosition().getY());

    }

    public BallModel getbModel() {
        return bModel;
    }
}

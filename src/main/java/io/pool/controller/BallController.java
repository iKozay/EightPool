package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.view.BallView;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class BallController {

    private static ArrayList<BallView> bViewList = new ArrayList<>();
    private static ArrayList<BallModel> bModelList = new ArrayList<>();

    public BallController(Pane root) {
        prepareGame(root);
    }

    public void prepareGame(Pane root) {
        for (int i=1;i<=16;i++){
            if(i<7) {
                bModelList.add(new BallModel(20, i, Color.RED));
            }else if(i==8){
                bModelList.add(new BallModel(20, i, Color.BLACK));
            }else if(i==16){
                bModelList.add(new BallModel(20, i, Color.WHITE));
            }else{
                bModelList.add(new BallModel(20, i,Color.BLUE));
            }
            bViewList.add(new BallView(this,bModelList.get(i-1).getColor(),bModelList.get(i-1).getRadius()));
            root.getChildren().add(bViewList.get(i-1).getBall());
        }
    }

    public void detectCollision(Rectangle table){
        detectCollisionWithTable(table);
        //detectCollisionWithOtherBalls();
    }

    private void detectCollisionWithTable(Rectangle table){
        for (BallModel bModel : bModelList) {
            if (((bModel.getBallPosition().getX() - bModel.getRadius()) <= table.getX()) || ((bModel.getBallPosition().getX() + bModel.getRadius()) >= (table.getX() + table.getWidth()))) {
                double newXVelocity = -bModel.getBallVector().getX();
                bModel.setBallVector(new Point2D(newXVelocity, bModel.getBallVector().getY()));
            }
            if (((bModel.getBallPosition().getY() - bModel.getRadius()) <= table.getY()) || ((bModel.getBallPosition().getY() + bModel.getRadius()) >= (table.getY() + table.getHeight()))) {
                double newYVelocity = -bModel.getBallVector().getY();
                bModel.setBallVector(new Point2D(bModel.getBallVector().getX(), newYVelocity));
            }
        }
        updateBallPosition();
    }
    private void detectCollisionWithOtherBalls(){
        ArrayList<BallModel> affectedBalls = new ArrayList<>();
        do {
            for (BallModel ballA : bModelList) {
                for (BallModel ballB : bModelList) {
                    if (!ballA.equals(ballB)) {
                        //TODO Implement Physics
                        System.out.println("TODO");
                    }
                }
            }
        }while(ballCollisions(affectedBalls));
    }

    private boolean ballCollisions(ArrayList<BallModel> affectedBalls){

        return false;
    }

    private void updateBallPosition() {
        for (BallModel bModel : bModelList) {
            BallView bView = bViewList.get(bModelList.indexOf(bModel));
            bModel.setBallPosition(bModel.getBallPosition().add(bModel.getBallVector()));
            bView.getBall().setLayoutX(bModel.getBallPosition().getX());
            bView.getBall().setLayoutY(bModel.getBallPosition().getY());
        }
    }
}

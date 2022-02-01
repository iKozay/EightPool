package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.model.TableModel;
import io.pool.view.BallView;
import io.pool.view.TableView;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class BallController {

    private static ArrayList<BallView> bViewList = new ArrayList<>();
    private static ArrayList<BallModel> bModelList = new ArrayList<>();

    public BallController(Pane root) {
        try {
            //prepareGame(root);
            bModelList.add(new BallModel(20, 1, new Image(new File("resources/billiards/ball1.jpg").toURI().toURL().toExternalForm())));
            bViewList.add(new BallView(this,bModelList.get(0).getImg(),bModelList.get(0).getRadius()));
            root.getChildren().add(bViewList.get(0).getBall());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void prepareGame(Pane root) throws MalformedURLException {
        for (int i=1;i<=16;i++){
            if(i==16){
                bModelList.add(new BallModel(20, i, new Image(new File("resources/billiards/white.jpg").toURI().toURL().toExternalForm())));
                bModelList.get(i-1).setBallVector(new Point2D(0,0));
            }else{
                bModelList.add(new BallModel(20, i, new Image(new File("resources/billiards/ball"+i+".jpg").toURI().toURL().toExternalForm())));
            }
            bViewList.add(new BallView(this,bModelList.get(i-1).getImg(),bModelList.get(i-1).getRadius()));
            root.getChildren().add(bViewList.get(i-1).getBall());
        }
    }

    public void detectCollision(TableView table){
        detectCollisionWithTable(table);
        detectCollisionWithOtherBalls();

    }

    private void detectCollisionWithTable(TableView table){
        for (BallModel bModel : bModelList) {
            if (((bModel.getBallPosition().getX() - bModel.getRadius()) <= table.getTable().getLayoutX()) || ((bModel.getBallPosition().getX() + bModel.getRadius()) >= (table.getTable().getLayoutX() + table.getTable().getWidth()))) {
                double newXVelocity = -bModel.getBallVector().getX();
                bModel.setBallVector(new Point2D(newXVelocity, bModel.getBallVector().getY()));
            }
            if (((bModel.getBallPosition().getY() - bModel.getRadius()) <= table.getTable().getLayoutY()) || ((bModel.getBallPosition().getY() + bModel.getRadius()) >= (table.getTable().getLayoutY() + table.getTable().getHeight()))) {
                double newYVelocity = -bModel.getBallVector().getY();
                bModel.setBallVector(new Point2D(bModel.getBallVector().getX(), newYVelocity));
            }
        }
        updateBallPosition();
    }
    private void detectCollisionWithOtherBalls(){
        for (BallModel ballA : bModelList) {
            for (BallModel ballB : bModelList) {
                if (!ballA.equals(ballB)) {
                    if(ballA.getBallPosition().distance(ballB.getBallPosition())<=(2*ballA.getRadius())){

                    }
                }
            }
        }
        updateBallPosition();
        }

    private void updateBallPosition() {
        for (BallModel bModel : bModelList) {
            BallView bView = bViewList.get(bModelList.indexOf(bModel));
            bModel.setBallPosition(bModel.getBallPosition().add(bModel.getBallVector()));
            bView.getBall().setLayoutX(bModel.getBallPosition().getX());
            bView.getBall().setLayoutY(bModel.getBallPosition().getY());
            Rotate rx = new Rotate(bModel.getBallVector().normalize().getY(), 0,0,0,Rotate.X_AXIS);
            Rotate ry = new Rotate(-bModel.getBallVector().normalize().getX(), 0,0,0, Rotate.Y_AXIS);
            Rotate rz = new Rotate(0, 0,0,0, Rotate.Z_AXIS);
            bView.getBall().getTransforms().addAll(rx,ry,rz);
        }
    }
}

package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.model.VelocityVector;
import io.pool.view.BallView;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import java.math.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class BallController {

    private static ArrayList<BallView> bViewList = new ArrayList<>();
    private static ArrayList<BallModel> bModelList = new ArrayList<>();

    public BallController(Pane root) {
        try {
            //prepareGame(root);
            bModelList.add(new BallModel(10, 1, new Image(new File("resources/billiards3D/ball1.jpg").toURI().toURL().toExternalForm())));
            bViewList.add(new BallView(this,bModelList.get(0).getImg(),bModelList.get(0).getRadius()));

            bModelList.add(new BallModel(10, 2, new Image(new File("resources/billiards3D/ball2.jpg").toURI().toURL().toExternalForm())));
            bViewList.add(new BallView(this,bModelList.get(1).getImg(),bModelList.get(1).getRadius()));
            bModelList.get(1).setBallPosition(new Point2D(600,350));

            root.getChildren().addAll(bViewList.get(0).getBall(),bViewList.get(1).getBall());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void prepareGame(Pane root) throws MalformedURLException {
        for (int i=1;i<=16;i++){
            if(i==16){
                bModelList.add(new BallModel(20, i, new Image(new File("resources/billiards/white.jpg").toURI().toURL().toExternalForm())));
                bModelList.get(i-1).setBallVector(new VelocityVector(0,0));
            }else{
                bModelList.add(new BallModel(20, i, new Image(new File("resources/billiards/ball"+i+".jpg").toURI().toURL().toExternalForm())));
            }
            bViewList.add(new BallView(this,bModelList.get(i-1).getImg(),bModelList.get(i-1).getRadius()));
            root.getChildren().add(bViewList.get(i-1).getBall());
        }
    }

    public void detectCollision(Rectangle tableBorders){
        detectCollisionWithTable(tableBorders);
        detectCollisionWithOtherBalls();

    }

    private void detectCollisionWithTable(Rectangle tableBorders){
        for (BallModel bModel : bModelList) {
            if ((bModel.getBallPosition().getX() - bModel.getRadius()) <= (tableBorders.getX() + 300) || (bModel.getBallPosition().getX() + bModel.getRadius()) >= ((tableBorders.getX() + 300) + tableBorders.getWidth())) {
                double newXVelocity = -bModel.getBallVector().getX();
                bModel.setBallVector(new VelocityVector(newXVelocity, bModel.getBallVector().getY()));
            }
            if (((bModel.getBallPosition().getY() - bModel.getRadius()) <= (tableBorders.getY() + 100)) || ((bModel.getBallPosition().getY() + bModel.getRadius()) >= ((tableBorders.getY() + 100) + tableBorders.getHeight()))) {
                double newYVelocity = -bModel.getBallVector().getY();
                bModel.setBallVector(new VelocityVector(bModel.getBallVector().getX(), newYVelocity));
            }
        }
        updateBallPosition();
    }
    private void detectCollisionWithOtherBalls(){
        for (BallModel ballA : bModelList) {
            for (BallModel ballB : bModelList) {
                if (!ballA.equals(ballB)) {
                    if((ballA.isMovingBall())||(ballB.isMovingBall())){
                        collisionHandler(ballA,ballB);
                    }
                }
            }
        }
        updateBallPosition();
        }

    private void updateBallPosition() {
        for (BallModel bModel : bModelList) {
            // TODO Friction is too high. To Adjust
            //applyFriction(bModel);
            BallView bView = bViewList.get(bModelList.indexOf(bModel));
            bModel.setBallPosition(bModel.getBallPosition().add(bModel.getBallVector().getX(),bModel.getBallVector().getY()));
            bView.getBall().setLayoutX(bModel.getBallPosition().getX());
            bView.getBall().setLayoutY(bModel.getBallPosition().getY());
            Rotate rx = new Rotate(bModel.getBallVector().getY(), 0,0,0,Rotate.X_AXIS);
            Rotate ry = new Rotate(-bModel.getBallVector().getX(), 0,0,0, Rotate.Y_AXIS);
            bView.getBall().getTransforms().addAll(rx,ry);
        }
    }
    public void applyFriction(BallModel bModel){
        bModel.getBallVector().setX(bModel.getBallVector().getX()*bModel.getAcceleration());
        bModel.getBallVector().setY(bModel.getBallVector().getY()*bModel.getAcceleration());
    }


    private void collisionHandler(BallModel ball1, BallModel ball2){
        //Balls positions
        Point2D ball1Pos = ball1.getBallPosition();
        Point2D ball2Pos = ball2.getBallPosition();

        //perpendicular line between point of contact ball 1 and ball 2
        double xSlope = ball2Pos.getX()-ball1Pos.getX();
        double ySlope = ball2Pos.getY()-ball1Pos.getY();
        double slope = ySlope/xSlope;

        //find angle between perpendicular line and x axis
        double anglePerpendicularBetweenBalls = Math.atan(slope);



        //Slope of ball and direction
        //ball1
        double magnitudeBall1 = ball1.getBallVector().getMagnitude();
        double angleOfDirectionBall1 = ball1.getBallVector().getAngle();
        //rotate vector so that it is parallel to the perpendicular line
        double tempAngleBall1 = angleOfDirectionBall1 - anglePerpendicularBetweenBalls;
        double rotatedXComponentBall1 = Math.cos(tempAngleBall1)*magnitudeBall1;
        double rotatedYComponentBall1 = Math.sin(tempAngleBall1)*magnitudeBall1;

        //ball2
        double magnitudeBall2 = ball2.getBallVector().getMagnitude();
        double angleOfDirectionBall2 = ball2.getBallVector().getAngle();
        //rotate vector so that it is parallel to the perpendicular line
        double tempAngleBall2 = angleOfDirectionBall2 - anglePerpendicularBetweenBalls;
        double rotatedXComponentBall2 = Math.cos(tempAngleBall2)*magnitudeBall2;
        double rotatedYComponentBall2 = Math.sin(tempAngleBall2)*magnitudeBall1;

        //switch these the two x components of each ball(the components parallel to the perpendicular line)
        double tempRotatedXBall2 = rotatedXComponentBall2;
        rotatedXComponentBall2 = rotatedXComponentBall1;
        rotatedXComponentBall1 = tempRotatedXBall2;

        //find the magnitude of each new vector
        //ball1
        double newMagnitudeBall1 = Math.sqrt(Math.pow(rotatedXComponentBall1, 2)+Math.pow(rotatedYComponentBall1, 2));
        //ball2
        double newMagnitudeBall2 = Math.sqrt(Math.pow(rotatedXComponentBall2, 2)+Math.pow(rotatedYComponentBall2, 2));

        //rotate back the problem to its initial state
        //use initial angles
        double newXBall1 = Math.cos(angleOfDirectionBall1)*newMagnitudeBall1;
        double newYBall1 = Math.sin(angleOfDirectionBall1)*newMagnitudeBall1;
        double newXBall2 = Math.cos(angleOfDirectionBall2)*newMagnitudeBall2;
        double newYBall2 = Math.sin(angleOfDirectionBall2)*newMagnitudeBall2;

        //setting the new vectors to each ball
        ball1.setBallVector(new VelocityVector(newXBall1, newYBall1));
        ball2.setBallVector(new VelocityVector(newXBall2, newYBall2));
    }


    public ArrayList<BallView> ballViewArrayList() {
        return bViewList;
    }
}

package io.pool.controller;

import io.pool.eightpool.game;
import io.pool.model.BallModel;
import io.pool.view.BallView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class BallController {

    /**ArrayList that contains all the BallViews*/
    private static ArrayList<BallView> bViewList = new ArrayList<>();
    /**ArrayList that contains all the BallModels*/
    private static ArrayList<BallModel> bModelList = new ArrayList<>();

    private int tableX = game.eightPoolTableX;
    private int tableY = game.eightPoolTableY;

    double mouseAnchorX,mouseAnchorY;

    /**
     * Main constructor of BallController
     * @param root The parent window that will contain the balls
     * */
    public BallController(Pane root) {
        try {
            prepareGame(root);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Prepares the game by creating the BallModels and the BallViews. Adds the BallViews to the pane
     * @param root Pane where the balls will be added
     * @exception MalformedURLException Throws this exception if the path to the ball image is incorrect
     * */
    public void prepareGame(Pane root) throws MalformedURLException {
        BallModel bModel;
        BallView bView;
        for (int i=1;i<=16;i++){
            if(i==16){
                bModel = new BallModel(i, new Image(new File("src/main/resources/billiards3D/white.jpg").toURI().toURL().toExternalForm()));
            }else{
                bModel = new BallModel(i, new Image(new File("src/main/resources/billiards3D/ball"+i+".jpg").toURI().toURL().toExternalForm()));
            }
            bView = new BallView(bModel.getImg(),BallModel.RADIUS);

            if(i==16) {
                /**
                 * Getting the position of the mouse to set the new position of the white ball when dragged
                */
                bView.getBall().setOnMousePressed(mouseEvent -> {

                    mouseAnchorX = mouseEvent.getX();
                    mouseAnchorY = mouseEvent.getY();
                });

                /** Assigning new variables because it will show an error if we use the same variables
                    that are outside the lambda
                */
                 BallModel finalBModel = bModel;
                BallView finalBView = bView;
                bView.getBall().setOnMouseDragged(mouseEvent -> {
                    if(!finalBModel.isMovingBall()) {
                        BigDecimal newPositionX = new BigDecimal(mouseEvent.getSceneX() - mouseAnchorX);
                        BigDecimal newPositionY = new BigDecimal(mouseEvent.getSceneY() - mouseAnchorY);
                        finalBModel.setBallPositionX(newPositionX);
                        finalBModel.setBallPositionY(newPositionY);
                        finalBView.getBall().setLayoutX(finalBModel.getBallPositionX().doubleValue());
                        finalBView.getBall().setLayoutY(finalBModel.getBallPositionY().doubleValue());
                    }
                });
            }

            /**
             * Adding the bModel and bView to their respective ArrayList
             */
            bModelList.add(bModel);
            bViewList.add(bView);

            /**
             * Adding the BallView to the Pane
             */
             root.getChildren().add(bView.getBall());
        }
    }
    /**
     * Detects all the collisions and updates the ball position
     * */
    public void detectCollision(Rectangle tableBorders, double time){
        detectCollisionWithTable(tableBorders);
        detectCollisionWithOtherBalls();
        for (BallModel bModel : bModelList) {
            bModel.updateBallPosition(bViewList.get(bModelList.indexOf(bModel)),time);
        }
    }
    /**
     * Detects any collision between a ball and the table
     * @param tableBorders Shape that defines the table borders
     * */
    private void detectCollisionWithTable(Rectangle tableBorders){
        for (BallModel bModel : bModelList) {
            BigDecimal radius = new BigDecimal(BallModel.RADIUS);
            if ((bModel.getBallPositionX().subtract(radius)).compareTo(new BigDecimal(tableBorders.getX() + tableX))<=0) {
                bModel.setBallVelocityX(bModel.getBallVelocityX().negate());
            }else if((bModel.getBallPositionX().add(radius)).compareTo(new BigDecimal((tableBorders.getX() + tableX) + tableBorders.getWidth()))>=0){
                bModel.setBallVelocityX(bModel.getBallVelocityX().negate());
            }
            if ((bModel.getBallPositionY().subtract(radius)).compareTo(new BigDecimal(tableBorders.getY() + tableY))<=0) {
                bModel.setBallVelocityY(bModel.getBallVelocityY().negate());
            }else if ((bModel.getBallPositionY().add(radius)).compareTo(new BigDecimal((tableBorders.getY() + tableY) + tableBorders.getHeight()))>=0){
                bModel.setBallVelocityY(bModel.getBallVelocityY().negate());
            }
        }
    }
    /**
     * Detects all the collisions between the balls
     * */
    private void detectCollisionWithOtherBalls(){
        ArrayList<String> collisionChecked = new ArrayList<>();
        BigDecimal distance;
        boolean foundInArray=false;
        for (BallModel ballA : bModelList) {
            for (BallModel ballB : bModelList) {
                if (!ballA.equals(ballB)) {
                    if((ballA.isMovingBall())||(ballB.isMovingBall())){
                        distance = ballA.distance(ballB);
                        if(distance.compareTo(new BigDecimal(2*BallModel.RADIUS))<0){
                            if(collisionChecked.size()==0){
                                collisionChecked.add(ballA.getNumber() + "," + ballB.getNumber());
                            }else {
                                String collision = "";
                                for (String id : collisionChecked) {
                                    if (!(id.contains(String.valueOf(ballA.getNumber())) && id.contains(String.valueOf(ballB.getNumber())))) {
                                        collision = ballA.getNumber() + "" + ballB.getNumber();
                                    } else {
                                        foundInArray = true;
                                        break;
                                    }
                                }
                                if(!collision.isEmpty()){
                                    collisionChecked.add(collision);
                                }
                            }
                            if(!foundInArray){
                                ballA.handleMomentum(ballB, distance.doubleValue());
                            }
                            foundInArray=false;
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets all the BallViews
     * @return the ArrayList that contains all the BallViews
     */
    public ArrayList<BallView> ballViewArrayList() {
        return bViewList;
    }

    /**
     * Gets all the BallModels
     * @return the ArrayList that contains all the BallModels
     */
    public ArrayList<BallModel> ballModelArrayList() {return bModelList; }


}

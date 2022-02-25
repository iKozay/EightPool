package io.pool.controller;

import io.pool.eightpool.game;
import io.pool.model.BallModel;
import io.pool.view.BallView;
import io.pool.view.GameView;
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
     * */
    public BallController() {}


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
                    if(!finalBModel.isMoving) {
                        BigDecimal newPositionX = new BigDecimal(mouseEvent.getSceneX() - mouseAnchorX);
                        BigDecimal newPositionY = new BigDecimal(mouseEvent.getSceneY() - mouseAnchorY);
                        finalBModel.setPositionX(newPositionX);
                        finalBModel.setPositionY(newPositionY);
                        finalBView.getBall().setLayoutX(finalBModel.getPositionX().doubleValue());
                        finalBView.getBall().setLayoutY(finalBModel.getPositionY().doubleValue());
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
    public void detectCollision(Rectangle tableBorders){
        detectCollisionWithTable(tableBorders);
        detectCollisionWithOtherBalls();
        for (BallModel bModel : bModelList) {
            bModel.updatePosition(bViewList.get(bModelList.indexOf(bModel)));
        }
    }
    /**
     * Detects any collision between a ball and the table
     * @param tableBorders Shape that defines the table borders
     * */
    private void detectCollisionWithTable(Rectangle tableBorders){
        for (BallModel bModel : bModelList) {
            BigDecimal radius = new BigDecimal(BallModel.RADIUS);
            if ((bModel.getPositionX().subtract(radius)).compareTo(new BigDecimal(tableBorders.getX() + tableX))<=0) {
                bModel.setVelocityX(bModel.getVelocityX().negate());
            }else if((bModel.getPositionX().add(radius)).compareTo(new BigDecimal((tableBorders.getX() + tableX) + tableBorders.getWidth()))>=0){
                bModel.setVelocityX(bModel.getVelocityX().negate());
            }
            if ((bModel.getPositionY().subtract(radius)).compareTo(new BigDecimal(tableBorders.getY() + tableY))<=0) {
                bModel.setVelocityY(bModel.getVelocityY().negate());
            }else if ((bModel.getPositionY().add(radius)).compareTo(new BigDecimal((tableBorders.getY() + tableY) + tableBorders.getHeight()))>=0){
                bModel.setVelocityY(bModel.getVelocityY().negate());
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
                    if((ballA.isMoving)||(ballB.isMoving)){
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

    /**
     * Removes all the BallViews from the Pane then Clears the BallView ArrayList
     * @param gameView The Pane that contains the BallViews
     */
    public void destroyViews(GameView gameView) {
        for(BallView bView : bViewList){
            gameView.getChildren().remove(bView.getBall());
        }
        bViewList.clear();
        System.out.println("Cleared All BallViews: "+bViewList.size());
    }

    /**
     * Clears the BallModels ArrayList
     */
    public void destroyModels() {
        bModelList.clear();
        System.out.println("Cleared All BallModels: "+bModelList.size());
    }
}

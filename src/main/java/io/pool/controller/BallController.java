package io.pool.controller;

import io.pool.eightpool.game;
import io.pool.model.BallModel;
import io.pool.model.PhysicsModule;
import io.pool.model.TableBorderModel;
import io.pool.view.BallView;
import io.pool.view.GameView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class BallController {

    /**ArrayList that contains all the BallViews*/
    public static ArrayList<BallView> bViewList = new ArrayList<>();
    /**ArrayList that contains all the BallModels*/
    public static ArrayList<BallModel> bModelList = new ArrayList<>();

    private int tableX = game.eightPoolTableX;
    private int tableY = game.eightPoolTableY;

    double mouseAnchorX,mouseAnchorY;

    public BallController() {
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
            bModel.setPositionX(new BigDecimal(100+50*i));
            bModel.setPositionY(new BigDecimal(300));

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
    public void testingBallController(Pane root) throws MalformedURLException {
        BallModel bModel1 = new BallModel(1, new Image(new File("src/main/resources/billiards3D/ball1.jpg").toURI().toURL().toExternalForm()));
        BallModel bModel2 = new BallModel(2, new Image(new File("src/main/resources/billiards3D/ball2.jpg").toURI().toURL().toExternalForm()));
        bModelList.add(bModel1);
        bModelList.add(bModel2);
        BallView bView1 = new BallView(bModel1.getImg(),BallModel.RADIUS);
        BallView bView2 = new BallView(bModel2.getImg(),BallModel.RADIUS);
        bViewList.add(bView1);
        bViewList.add(bView2);
        root.getChildren().addAll(bView1.getBall(),bView2.getBall());
    }

    /**
     * Detects all the collisions and updates the ball position
     * */
    public void detectCollision(){
        detectCollisionWithTable();
        detectCollisionWithOtherBalls();
        for (BallModel bModel : bModelList) {
            bModel.updatePosition(bViewList.get(bModelList.indexOf(bModel)));
        }
    }
    /**
     * Detects any collision between a ball and the table
     * */
    private void detectCollisionWithTable(){
            for (BallModel bModel : bModelList) {
                for (TableBorderModel line : TableBorderModel.tableBorder) {
                    // TODO work on collision with table
                    BigDecimal radius = new BigDecimal(BallModel.RADIUS);
                    Shape intersect = Shape.intersect(line,bViewList.get(bModelList.indexOf(bModel)).getCircleFromSphere());

                    if ((intersect.getBoundsInLocal().getWidth()!=-1)||(intersect.getBoundsInLocal().getHeight()!=-1)) {
                        bModel.setVelocityX(bModel.getVelocityX().multiply(line.getReflectionXFactor()));
                        bModel.setVelocityY(bModel.getVelocityY().multiply(line.getReflectionYFactor()));
                        //tableModel.handleMomentum(bModel);
                    }
                    // else if ((bModel.getPositionX().add(radius)).compareTo(new BigDecimal((tableBorders.getX() + tableX) + tableBorders.getWidth())) >= 0) {
                    //    bModel.setVelocityX(bModel.getVelocityX().negate());
                    //}
                    //if ((bModel.getPositionY().subtract(radius)).compareTo(new BigDecimal(tableBorders.getY() + tableY)) <= 0) {
                    //    bModel.setVelocityY(bModel.getVelocityY().negate());
                    //} else if ((bModel.getPositionY().add(radius)).compareTo(new BigDecimal((tableBorders.getY() + tableY) + tableBorders.getHeight())) >= 0) {
                    //    bModel.setVelocityY(bModel.getVelocityY().negate());
                   // }
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
                        if(distance.compareTo(new BigDecimal(2*BallModel.RADIUS))<=0){
                            if(collisionChecked.size()==0){
                                collisionChecked.add(ballA.getNumber() + "," + ballB.getNumber());
                            }else {
                                String collision = "";
                                for (String id : collisionChecked) {
                                    if (!(id.contains(String.valueOf(ballA.getNumber())) && id.contains(String.valueOf(ballB.getNumber())))) {
                                        collision = ballA.getNumber() + "," + ballB.getNumber();
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

                                ballA.handleMomentum(ballB);
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

    public ArrayList<Double> xballPos = new ArrayList<>();
    public ArrayList<Double> yballPos = new ArrayList<>();
    public void addBallsPosition(){
        for(BallView bv: bViewList){
            xballPos.add(bv.getBall().getLayoutX());
            yballPos.add(bv.getBall().getLayoutY());
        }
    }
}

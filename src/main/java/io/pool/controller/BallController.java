package io.pool.controller;

import io.pool.eightpool.game;
import io.pool.model.BallModel;
import io.pool.model.PhysicsModule;
import io.pool.model.TableBorderModel;
import io.pool.view.BallView;
import io.pool.view.GameView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class BallController {

    /**ArrayList that contains all the BallViews*/
    public static ArrayList<BallView> bViewList = new ArrayList<>();
    public static ArrayList<BallView> stripeBViewList = new ArrayList<>();
    public static ArrayList<BallView> solidBViewList = new ArrayList<>();
    /**ArrayList that contains all the BallModels*/
    public static ArrayList<BallModel> bModelList = new ArrayList<>();
    /**ArrayList that contains all the stripe/solid*/
    public static ArrayList<BallModel> stripeBModelList = new ArrayList<>();
    public static ArrayList<BallModel> solidBModelList = new ArrayList<>();

    public static BallModel whiteBallModel;
    public static BallView whiteBallView;
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
                whiteBallModel = bModel;
            }else{
                bModel = new BallModel(i, new Image(new File("src/main/resources/billiards3D/ball"+i+".jpg").toURI().toURL().toExternalForm()));
            }
            bModel.setPositionX(new BigDecimal(i*3*BallModel.RADIUS));
            bModel.setPositionY(new BigDecimal(300));

            bView = new BallView(bModel.getImg(),BallModel.RADIUS);
            if(i==16) {
                whiteBallView = bView;
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

        for(int i = 8;i<15;i++){
            stripeBModelList.add(bModelList.get(i));
            stripeBViewList.add(bViewList.get(i));
        }
        for(int i = 0;i<7;i++){
            solidBModelList.add(bModelList.get(i));
            solidBViewList.add(bViewList.get(i));
        }

        for (int i = 0; i < 7; i++) {
            System.out.println("Stripe: "+stripeBModelList.get(i).getNumber());
            System.out.println("Solid " + solidBModelList.get(i).getNumber());

        }

        System.out.println("Model "+ballModelArrayList().size());
        System.out.println("Solid "+solidBModelList.size());
        System.out.println("Stripe "+stripeBViewList.size());
        System.out.println("View "+ballViewArrayList().size());
        System.out.println("Nodes " + root.getChildren().size());
    }

//    public void makeWhiteBallDraggable(){
//        whiteBallView.getBall().setOnMousePressed(event -> {
//            double xPosMouse = event.getX();
//            double yPosMouse = event.getY();
//            double xPosSphere = whiteBallView.getBall().getLayoutX();
//            double yPosSphere = whiteBallView.getBall().getLayoutY();
//            System.out.println("xPos Mouse: " + xPosMouse + "\n" + "yPos Mouse: " + yPosMouse + "\n" + "xPos Sphere: " + xPosSphere + "\n"  + "yPos Sphere: " + yPosSphere + "\n" );
//
//            whiteBallView.getBall().setTranslateX(xPosSphere+xPosMouse);
//            whiteBallView.getBall().setTranslateY(yPosSphere+yPosMouse);
//
//        });
//    }

    public void testingBallController(Pane root) throws MalformedURLException {
        BallModel bModel1 = new BallModel(1, new Image(new File("src/main/resources/billiards3D/ball1.jpg").toURI().toURL().toExternalForm()));
        BallModel bModel2 = new BallModel(2, new Image(new File("src/main/resources/billiards3D/ball2.jpg").toURI().toURL().toExternalForm()));
        bModel1.setPositionX(new BigDecimal(500));
        bModel1.setPositionY(new BigDecimal(200));
        bModel2.setPositionX(new BigDecimal(500));
        bModel2.setPositionY(new BigDecimal(500));
        bModel1.setVelocityX(PhysicsModule.ZERO);
        bModel1.setVelocityY(new BigDecimal(10));
        bModel2.setVelocityX(PhysicsModule.ZERO);
        bModel2.setVelocityY(PhysicsModule.ZERO);
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
            bModel.updatePosition();
            updateBallViewPosition(bModel);
        }
    }

    public static void updateBallViewPosition(BallModel bModel) {
        BallView bView = bViewList.get(bModelList.indexOf(bModel));
        bView.getBall().setLayoutX(bModel.getPositionX().doubleValue());
        bView.getBall().setLayoutY(bModel.getPositionY().doubleValue());
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

    public static ArrayList<BallView> getStripeBViewList() {
        return stripeBViewList;
    }

    public static ArrayList<BallView> getSolidBViewList() {
        return solidBViewList;
    }

    public static ArrayList<BallModel> getStripeBModelList() {
        return stripeBModelList;
    }

    public static ArrayList<BallModel> getSolidBModelList() {
        return solidBModelList;
    }

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

    public void ballInHole(BallModel ballModel,GameView gameView) {
        if(ballModelArrayList().contains(ballModel)){
            ballModel.setVelocityX(PhysicsModule.ZERO);
            ballModel.setVelocityY(PhysicsModule.ZERO);
            BallView bView1 = ballViewArrayList().get(ballModelArrayList().indexOf(ballModel));
            gameView.getChildren().remove(bView1.getBall());
            ballModelArrayList().remove(ballModel);
            ballViewArrayList().remove(bView1);
        }
    }
}

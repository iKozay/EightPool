package io.pool.controller;

import io.pool.eightpool.game;
import io.pool.model.BallModel;
import io.pool.view.BallView;

import java.math.MathContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class BallController {

    private static ArrayList<BallView> bViewList = new ArrayList<>();
    private static ArrayList<BallModel> bModelList = new ArrayList<>();

    double mouseAnchorX;
    double mouseAnchorY;

    private int tableX = game.eightPoolTableX;
    private int tableY = game.eightPoolTableY;


    public BallController(Pane root) {
        try {
            prepareGame(root);
            //BallModel ballModel = new BallModel(15, 1, new Image(new File("src/main/resources/billiards3D/ball1.jpg").toURI().toURL().toExternalForm()));
            //bModelList.add(ballModel);
            //BallView ballView = new BallView(bModelList.get(0).getImg(),BallModel.getRadius());
            //ballView.getBall().translateXProperty().bind(ballModel.getBall);
            //BallModel ballModel2 = new BallModel(15, 2, new Image(new File("src/main/resources/billiards3D/ball2.jpg").toURI().toURL().toExternalForm()));
            //bModelList.add(ballModel2);
            //BallView ballView2 = new BallView(bModelList.get(1).getImg(),BallModel.getRadius());
            //bViewList.add(ballView);
            //bViewList.add(ballView2);
            //ballModel2.setBallPosition(new CustomPoint2D(500,500));
            //ballModel2.setBallVelocity(new CustomPoint2D(-1,-1));
            //makeDraggable();
//            BallModel ballModel2 = new BallModel(15, 2, new Image(new File("resources/billiards3D/ball2.jpg").toURI().toURL().toExternalForm()));
//            bModelList.add(ballModel2);
//            BallView ballView2 = new BallView(bModelList.get(1).getImg(),BallModel.getRadius());
//            bViewList.add(ballView);
//            bViewList.add(ballView2);
//            ballModel2.setBallPosition(new CustomPoint2D(900,250));
//            ballModel2.setBallVelocity(new CustomPoint2D(-1,0));
//            makeDraggable();
            //bModelList.add(new BallModel(15, 2, new Image(new File("resources/billiards3D/ball2.jpg").toURI().toURL().toExternalForm())));
            //bViewList.add(new BallView(this,bModelList.get(1).getImg(),BallModel.getRadius()));
            ///bModelList.get(1).setBallPosition(new Point2D(600,350));
            //bModelList.get(1).setBallVector(new VelocityVector(0,0));
            //bModelList.get(1).setMovingBall(false);
            //root.getChildren().addAll(bViewList.get(0).getBall(),bViewList.get(1).getBall());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

//
//    public void makeDraggable() {
//
//        for (BallView bView : bViewList) {
//            bView.getBall().setOnMousePressed(mouseEvent -> {
//                mouseAnchorX = mouseEvent.getX();
//                mouseAnchorY = mouseEvent.getY();
//            });
//            bView.getBall().setOnMouseDragged(mouseEvent -> {
//                CustomPoint2D newPosition = new CustomPoint2D(mouseEvent.getSceneX() - mouseAnchorX, mouseEvent.getSceneY() - mouseAnchorY);
//                BallModel bModel = bModelList.get(bViewList.indexOf(bView));
//                bModel.setBallPosition(newPosition);
//                bView.getBall().setLayoutX(bModel.getBallPosition().getX().doubleValue());
//                bView.getBall().setLayoutY(bModel.getBallPosition().getY().doubleValue());
//            });
//
//        }
//
//    }


    public void prepareGame(Pane root) throws MalformedURLException {
        for (int i=1;i<=16;i++){
            if(i==16){
                bModelList.add(new BallModel(15, i, new Image(new File("src/main/resources/billiards3D/white.jpg").toURI().toURL().toExternalForm())));
            }else{
                bModelList.add(new BallModel(15, i, new Image(new File("src/main/resources/billiards3D/ball"+i+".jpg").toURI().toURL().toExternalForm())));
            }
            bViewList.add(new BallView(bModelList.get(i-1).getImg(),BallModel.getRadius()));
            root.getChildren().add(bViewList.get(i-1).getBall());
        }
    }

    public void detectCollision(Rectangle tableBorders, double time){
        detectCollisionWithTable(tableBorders);
        detectCollisionWithOtherBalls();
        updateBallPosition(time);
    }

    private void detectCollisionWithTable(Rectangle tableBorders){
        for (BallModel bModel : bModelList) {
            BigDecimal newXVelocity = bModel.getBallVelocityX();
            BigDecimal newYVelocity = bModel.getBallVelocityY();
            BigDecimal radius = new BigDecimal(BallModel.getRadius());
            if ((bModel.getBallPositionX().subtract(radius)).compareTo(new BigDecimal(tableBorders.getX() + tableX))<=0) {
                bModel.setBallVelocityX(newXVelocity.negate());
            }else if((bModel.getBallPositionX().add(radius)).compareTo(new BigDecimal((tableBorders.getX() + tableX) + tableBorders.getWidth()))>=0){
                bModel.setBallVelocityX(newXVelocity.negate());
            }
            if ((bModel.getBallPositionY().subtract(radius)).compareTo(new BigDecimal(tableBorders.getY() + tableY))<=0) {
                bModel.setBallVelocityY(newYVelocity.negate());
            }else if ((bModel.getBallPositionY().add(radius)).compareTo(new BigDecimal((tableBorders.getY() + tableY) + tableBorders.getHeight()))>=0){
                bModel.setBallVelocityY(newYVelocity.negate());
            }
        }
    }
    private void detectCollisionWithOtherBalls(){
        ArrayList<String> collisionChecked = new ArrayList<>();
        BigDecimal distance;
        boolean foundInArray=false;
        for (BallModel ballA : bModelList) {
            for (BallModel ballB : bModelList) {
                if (!ballA.equals(ballB)) {
                    if((ballA.isMovingBall())||(ballB.isMovingBall())){
                        distance = distance(ballA.getBallPositionX(),ballA.getBallPositionY(),ballB.getBallPositionX(),ballB.getBallPositionY());
                        if(distance.compareTo(new BigDecimal(2*BallModel.getRadius()))<=0){
                            if(collisionChecked.size()==0){
                                collisionChecked.add(ballA.getNumber() + "" + ballB.getNumber());
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
                                if ((ballA.isMovingBall()) && (ballB.isMovingBall())) {
                                    //Point2D newCoordinateA = new Point2D(ballA_NextFramePos.getX()*proportionFactor,ballA_NextFramePos.getY()*proportionFactor);
                                    //Point2D newCoordinateB = new Point2D(ballB_NextFramePos.getX()*proportionFactor,ballB_NextFramePos.getY()*proportionFactor);
                                    //ballA.setBallPosition(newCoordinateA);
                                    //ballB.setBallPosition(newCoordinateB);
                                    //BigDecimal distance = distance(ballA.getBallPositionX(),ballA.getBallPositionY(),ballB.getBallPositionX(),ballB.getBallPositionY()).divide(new BigDecimal(2));

                               // ballA.setBallPosition(ballA.getBallPositionX().subtract(distance),ballA.getBallPositionY().subtract(distance));
                               // ballB.setBallPosition(ballB.getBallPositionX().add(distance),ballB.getBallPositionY().add(distance));

                                } else {
                                    BallModel movingBall;
                                    BallModel restingBall;
                                    if (ballA.isMovingBall()) {
                                        movingBall = ballA;
                                        restingBall = ballB;
                                    } else{
                                        movingBall = ballB;
                                        restingBall = ballA;
                                    }
                                    // Check angle
                                    //BigDecimal angle = movingBall.getBallVelocity().getAngle();
                                    //CustomPoint2D newCoordinate = new CustomPoint2D(new BigDecimal(Math.cos(angle.doubleValue()) * (2 * BallModel.getRadius())), new BigDecimal(Math.sin(angle.doubleValue()) * (2 * BallModel.getRadius())));
                                    //.setBallPosition(newCoordinate);

                                }
                                System.out.println("Collision");
                                //ballA.setBallVelocity(new CustomPoint2D(1,0));
                                //ballB.setBallVelocity(new CustomPoint2D(-1,0));
                                //System.out.println(ballA.getBallVelocity()+" ; "+ballB.getBallVelocity());
                                collisionHandler(ballA,ballB);
                            }
                            foundInArray=false;
                        }
                    }
                }
            }
        }
        }

    private void updateBallPosition(double time) {
        for (BallModel bModel : bModelList) {
            if(bModel.isMovingBall()) {
                applyFriction(bModel, time);
                bModel.setBallPositionX(bModel.getBallPositionX().add(bModel.getBallVelocityX()));
                bModel.setBallPositionY(bModel.getBallPositionY().add(bModel.getBallVelocityY()));
//                bModel.translateXProperty().set(bModel.getBallPositionX().doubleValue());
//                bModel.translateYProperty().set(bModel.getBallPositionY().doubleValue());
                BallView bView = bViewList.get(bModelList.indexOf(bModel));
                bModel.setMovingBall();
                //bModel.setBallPosition(bModel.getBallPosition().add(bModel.getBallVelocityX(), bModel.getBallVelocityY()));
                bView.getBall().setLayoutX(bModel.getBallPositionX().doubleValue());
                bView.getBall().setLayoutY(bModel.getBallPositionY().doubleValue());
                Rotate rx = new Rotate(-bModel.getBallVelocityY().doubleValue(), 0, 0, 0, Rotate.X_AXIS);
                Rotate ry = new Rotate(bModel.getBallVelocityX().doubleValue(), 0, 0, 0, Rotate.Y_AXIS);
                bView.getBall().getTransforms().addAll(rx, ry);
            }
        }
    }

    public void applyFriction(BallModel bModel, double time){
       BigDecimal frictionForceMag = new BigDecimal(0.01*BallModel.MASS_BALL_KG*BallModel.GRAVITATIONAL_FORCE,MathContext.DECIMAL32);

        double velocityRatio = bModel.getBallVelocityX().abs().doubleValue() / bModel.getBallVelocityY().abs().doubleValue();

        bModel.setBallAccelerationY((new BigDecimal(Math.sqrt(Math.pow(frictionForceMag.doubleValue(),2)) / (Math.pow(velocityRatio,2) + 1),MathContext.DECIMAL32)));
        bModel.setBallAccelerationX(new BigDecimal(velocityRatio*bModel.getBallAccelerationY().doubleValue(),MathContext.DECIMAL32));

        if(bModel.getBallVelocityX().doubleValue() > 0){
            bModel.setBallAccelerationX(bModel.getBallAccelerationX().multiply(BigDecimal.valueOf(-1)));
        }
        if(bModel.getBallVelocityY().doubleValue() > 0){
            bModel.setBallAccelerationY(bModel.getBallAccelerationY().multiply(BigDecimal.valueOf(-1)));
        }

        if(!(bModel.getBallVelocityX().abs().doubleValue() <= frictionForceMag.doubleValue()) && !(bModel.getBallVelocityY().abs().doubleValue() <= frictionForceMag.doubleValue()) ){
            System.out.println("");
            bModel.setBallVelocityX(bModel.getBallVelocityX().add(bModel.getBallAccelerationX()));
            bModel.setBallVelocityY(bModel.getBallVelocityY().add(bModel.getBallAccelerationY()));
        }else {
            bModel.setBallVelocityX(new BigDecimal(0));
            bModel.setBallVelocityY(new BigDecimal(0));
        }

        solvingRoundingProblem(bModel);
    }

    private void collisionHandler(BallModel ball1, BallModel ball2){
        //https://vobarian.com/collisions/2dcollisions2.pdf
        /**1
         * find unit normal and unit tanget vector
         */
        //FIX
        BigDecimal normalXComponent = ball2.getBallVelocityX().subtract(ball1.getBallVelocityX());
        BigDecimal normalYComponent = ball2.getBallVelocityY().subtract(ball1.getBallVelocityY());

        BigDecimal magnitude = (normalYComponent.pow(2).add(normalXComponent.pow(2))).sqrt(MathContext.DECIMAL32);
        BigDecimal unitNormalX = normalXComponent.divide(magnitude,MathContext.DECIMAL32);
        BigDecimal unitNormalY = normalYComponent.divide(magnitude,MathContext.DECIMAL32);

        BigDecimal unitTangentX = unitNormalY.negate();
        BigDecimal unitTangentY = unitNormalX;


        /**2 (step 2 is skipped because we already have the balls vectors
         * Resolve velocity vectors of ball 1 and 2 into normal and tangential components
         * this is done by using the dot product of the balls initial velocity and using the unitVectors
         */
        BigDecimal v1n = (unitNormalX.multiply(ball1.getBallVelocityX()).add(unitNormalY.multiply(ball1.getBallVelocityY())));
        BigDecimal v1t = (unitTangentX.multiply(ball1.getBallVelocityX())).add(unitTangentY.multiply(ball1.getBallVelocityY()));
        BigDecimal v2n = (unitNormalX.multiply(ball2.getBallVelocityX())).add(unitNormalY.multiply(ball2.getBallVelocityY()));
        BigDecimal v2t = (unitTangentX.multiply(ball2.getBallVelocityX())).add(unitTangentY.multiply(ball2.getBallVelocityY()));

        /**3
         * Find new tangential velocities
         * they are equal to the initial ones
         */
        BigDecimal v1tp = v1t;
        BigDecimal v2tp = v2t;

        /**4
         * Find new normal velocities
         * all the instances of 1 in this equation are substitutes for mass
         * this is assuming all the balls have equal mass
         */
        BigDecimal v1np = v2n;
        BigDecimal v2np = v1n;

        /**5
         * Convert scalar normal and tangential velocites into vectors
         */
        BigDecimal normalXFinalBall1 = unitNormalX.multiply(v1np);
        BigDecimal normalYFinalBall1 = unitNormalY.multiply(v1np);
        BigDecimal normalXFinalBall2 = unitNormalX.multiply(v2np);
        BigDecimal normalYFinalBall2 = unitNormalY.multiply(v2np);

        BigDecimal tangentialXFinalBall1 = unitTangentX.multiply(v1tp);
        BigDecimal tangentialYFinalBall1 = unitTangentY.multiply(v1tp);
        BigDecimal tangentialXFinalBall2 = unitTangentX.multiply(v2tp);
        BigDecimal tangentialYFinalBall2 = unitTangentY.multiply(v2tp);

        /**6
         * Add normal and tangential vectors for each ball
         */
        BigDecimal finalBall1X = normalXFinalBall1.add(tangentialXFinalBall1,MathContext.DECIMAL32);
        BigDecimal finalBall1Y = normalYFinalBall1.add(tangentialYFinalBall1,MathContext.DECIMAL32);
        BigDecimal finalBall2X = normalXFinalBall2.add(tangentialXFinalBall2,MathContext.DECIMAL32);
        BigDecimal finalBall2Y = normalYFinalBall2.add(tangentialYFinalBall2,MathContext.DECIMAL32);

        ball1.setBallVelocityX(finalBall1X);
        ball1.setBallVelocityY(finalBall1Y);
        ball2.setBallVelocityX(finalBall2X);
        ball2.setBallVelocityY(finalBall2Y);
    }

    public BigDecimal distance(BigDecimal x2, BigDecimal y2, BigDecimal x1, BigDecimal y1) {
        BigDecimal a = x2.subtract(x1, MathContext.DECIMAL32);
        BigDecimal b = y2.subtract(y1, MathContext.DECIMAL32);
        a = a.pow(2, MathContext.DECIMAL32);
        b = b.pow(2, MathContext.DECIMAL32);
        BigDecimal subtotal = a.add(b, MathContext.DECIMAL32);
        return subtotal.sqrt(MathContext.DECIMAL32);
    }
    public ArrayList<BallView> ballViewArrayList() {
        return bViewList;
    }

    public ArrayList<BallModel> ballModelArrayList() {return bModelList; }

    private void solvingRoundingProblem(BallModel bModel) {

        //the next variables are the absolute magnitudes of the velocity components
        double ballSpeedX = bModel.getBallVelocityX().doubleValue();
        double ballSpeedY = bModel.getBallVelocityY().doubleValue();

        if (ballSpeedX < 0) ballSpeedX = ballSpeedX * -1;
        if (ballSpeedY < 0) ballSpeedY = ballSpeedY * -1;

        //the next variables are the absolute magnitudes of the acceleration components
        double accX = bModel.getBallAccelerationX().doubleValue();
        double accY = bModel.getBallAccelerationY().doubleValue();

        if (accX < 0) accX = accX * -1;
        if (accY < 0) accY = accY * -1;

        if (ballSpeedX < accX) {
            bModel.setBallVelocityX(new BigDecimal("0"));
        }
        if (ballSpeedY < accY) {
            bModel.setBallVelocityX(new BigDecimal("0"));
        }

        if(bModel.getBallVelocityX().compareTo(BigDecimal.ZERO) == 0) {

        }

    }

}

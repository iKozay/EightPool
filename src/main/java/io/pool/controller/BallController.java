package io.pool.controller;

import io.pool.eightpool.game;
import io.pool.model.BallModel;
import io.pool.model.CustomPoint2D;
import io.pool.view.BallView;
import javafx.geometry.Point2D;
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
            //BallModel ballModel = new BallModel(15, 1, new Image(new File("resources/billiards3D/ball1.jpg").toURI().toURL().toExternalForm()));
            //bModelList.add(ballModel);
            //BallView ballView = new BallView(bModelList.get(0).getImg(),BallModel.getRadius());
            //ballView.getBall().translateXProperty().bind(ballModel.getBall);
            //bViewList.add(ballView);
            makeDraggable();
            //bModelList.add(new BallModel(15, 2, new Image(new File("resources/billiards3D/ball2.jpg").toURI().toURL().toExternalForm())));
            //bViewList.add(new BallView(this,bModelList.get(1).getImg(),BallModel.getRadius()));
            ///bModelList.get(1).setBallPosition(new Point2D(600,350));
            //bModelList.get(1).setBallVector(new VelocityVector(0,0));
            //bModelList.get(1).setMovingBall(false);
            //root.getChildren().addAll(bViewList.get(0).getBall());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void makeDraggable() {

        for (BallView bView : bViewList) {
            bView.getBall().setOnMousePressed(mouseEvent -> {
                mouseAnchorX = mouseEvent.getX();
                mouseAnchorY = mouseEvent.getY();
            });
            bView.getBall().setOnMouseDragged(mouseEvent -> {
                CustomPoint2D newPosition = new CustomPoint2D(mouseEvent.getSceneX() - mouseAnchorX, mouseEvent.getSceneY() - mouseAnchorY);
                BallModel bModel = bModelList.get(bViewList.indexOf(bView));
                bModel.setBallPosition(newPosition);
                bView.getBall().setLayoutX(bModel.getBallPosition().getX().doubleValue());
                bView.getBall().setLayoutY(bModel.getBallPosition().getY().doubleValue());
            });

        }

    }


    public void prepareGame(Pane root) throws MalformedURLException {
        for (int i=1;i<=16;i++){
            if(i==16){
                bModelList.add(new BallModel(15, i, new Image(new File("src/main/resources/billiards3D/white.jpg").toURI().toURL().toExternalForm())));
                bModelList.get(i-1).setBallVelocity(new CustomPoint2D(0,0));
            }else{
                bModelList.add(new BallModel(15, i, new Image(new File("src/main/resources/billiards3D/ball"+i+".jpg").toURI().toURL().toExternalForm())));
            }
            bViewList.add(new BallView(bModelList.get(i-1).getImg(),BallModel.getRadius()));
            root.getChildren().add(bViewList.get(i-1).getBall());
        }
    }

    public void detectCollision(Rectangle tableBorders, double time){
        detectCollisionWithTable(tableBorders);
        //detectCollisionWithOtherBalls();
        updateBallPosition(time);
    }

    private void detectCollisionWithTable(Rectangle tableBorders){
        for (BallModel bModel : bModelList) {
            BigDecimal newXVelocity = bModel.getBallVelocity().getX();
            BigDecimal newYVelocity = bModel.getBallVelocity().getY();
            BigDecimal radius = new BigDecimal(BallModel.getRadius());
            if ((bModel.getBallPosition().getX().subtract(radius)).compareTo(new BigDecimal(tableBorders.getX() + tableX))<=0) {
                newXVelocity = bModel.getBallVelocity().getX().negate();
            }else if((bModel.getBallPosition().getX().add(radius)).compareTo(new BigDecimal((tableBorders.getX() + tableX) + tableBorders.getWidth()))>=0){
                newXVelocity = bModel.getBallVelocity().getX().negate();
            }
            if ((bModel.getBallPosition().getY().subtract(radius)).compareTo(new BigDecimal(tableBorders.getY() + tableY))<=0) {
                newYVelocity = bModel.getBallVelocity().getY().negate();
            }else if ((bModel.getBallPosition().getY().add(radius)).compareTo(new BigDecimal((tableBorders.getY() + tableY) + tableBorders.getHeight()))>=0){
                newYVelocity = bModel.getBallVelocity().getY().negate();
            }
            //bModel.setBallPosition(new CustomPoint2D(newXPos, newYPos));
            bModel.setBallVelocity(new CustomPoint2D(newXVelocity, newYVelocity));
        }
    }
    private void detectCollisionWithOtherBalls(){
        for (BallModel ballA : bModelList) {
            for (BallModel ballB : bModelList) {
                if (!ballA.equals(ballB)) {
                    if((ballA.isMovingBall())||(ballB.isMovingBall())){
                        if(ballA.getBallPosition().distance(ballB.getBallPosition()).compareTo(new BigDecimal(2*BallModel.getRadius()))<=0){
                            if ((ballA.isMovingBall()) && (ballB.isMovingBall())) {
                                //Point2D newCoordinateA = new Point2D(ballA_NextFramePos.getX()*proportionFactor,ballA_NextFramePos.getY()*proportionFactor);
                                //Point2D newCoordinateB = new Point2D(ballB_NextFramePos.getX()*proportionFactor,ballB_NextFramePos.getY()*proportionFactor);
                                //ballA.setBallPosition(newCoordinateA);
                                //ballB.setBallPosition(newCoordinateB);
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
                                BigDecimal angle = movingBall.getBallVelocity().getAngle();
                                CustomPoint2D newCoordinate = new CustomPoint2D(new BigDecimal(Math.cos(angle.doubleValue()) * (2 * BallModel.getRadius())), new BigDecimal(Math.sin(angle.doubleValue()) * (2 * BallModel.getRadius())));
                                movingBall.setBallPosition(newCoordinate);

                            }
//                            System.out.println("Collision");
                            collisionHandler(ballA,ballB);
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
                BallView bView = bViewList.get(bModelList.indexOf(bModel));
                bModel.setMovingBall(!bModel.getBallVelocity().equals(Point2D.ZERO));
                bModel.setBallPosition(bModel.getBallPosition().add(bModel.getBallVelocity().getX(), bModel.getBallVelocity().getY()));
                bView.getBall().setLayoutX(bModel.getBallPosition().getX().doubleValue());
                bView.getBall().setLayoutY(bModel.getBallPosition().getY().doubleValue());
                Rotate rx = new Rotate(bModel.getBallVelocity().getY().doubleValue(), 0, 0, 0, Rotate.X_AXIS);
                Rotate ry = new Rotate(bModel.getBallVelocity().getX().doubleValue(), 0, 0, 0, Rotate.Y_AXIS);
                bView.getBall().getTransforms().addAll(rx, ry);
            }
        }
    }

    public void applyFriction(BallModel bModel, double time){
       BigDecimal frictionForceMag = new BigDecimal(0.1*BallModel.MASS_BALL_KG*BallModel.GRAVITATIONAL_FORCE,CustomPoint2D.DECIMAL8);
        // TODO Fix the angle.
        BigDecimal vectorAngleDeg = bModel.getBallVelocity().getAngle();
        CustomPoint2D frictionForce = CustomPoint2D.ZERO;
        if(vectorAngleDeg!=null) {
            BigDecimal angleDeg = vectorAngleDeg.add(CustomPoint2D.ONE_EIGHTY_BD);
            // TODO Fix the angle.
            frictionForce = new CustomPoint2D(frictionForceMag.multiply(new BigDecimal(Math.cos(Math.toRadians(angleDeg.doubleValue()))),CustomPoint2D.DECIMAL8),  frictionForceMag.multiply(new BigDecimal(Math.sin(Math.toRadians(angleDeg.doubleValue()))),CustomPoint2D.DECIMAL8));
        }
//        System.out.println("Friction: "+frictionForce);
        bModel.setBallForce(frictionForce, time);
    }

    private void collisionHandler(BallModel ball1, BallModel ball2){
        //https://vobarian.com/collisions/2dcollisions2.pdf
        /**1
         * find unit normal and unit tanget vector
         */
        BigDecimal normalXComponent = ball2.getBallPosition().getX().subtract(ball1.getBallPosition().getX());
        BigDecimal normalYComponent = ball2.getBallPosition().getY().subtract(ball1.getBallPosition().getY());
        CustomPoint2D normalVectorInitial = new CustomPoint2D(normalXComponent, normalYComponent);
        //double normalMagnitude = Math.sqrt(Math.pow(normalXComponent, 2)+Math.pow(normalYComponent, 2));
        //double normalAngle = Math.atan(normalYComponent/normalXComponent);
        CustomPoint2D unitNormalVector = new CustomPoint2D(normalXComponent.divide(normalXComponent.abs()), normalYComponent.divide(normalYComponent.abs()));
        CustomPoint2D unitTangentVector = new CustomPoint2D(unitNormalVector.getY().negate(), unitNormalVector.getX());

        /**2 (step 2 is skipped because we already have the balls vectors
         * Resolve velocity vectors of ball 1 and 2 into normal and tangential components
         * this is done by using the dot product of the balls initial velocity and using the unitVectors
         */
        BigDecimal v1n = (unitNormalVector.getX().multiply(ball1.getBallVelocity().getX()).add(unitNormalVector.getY().multiply(ball1.getBallVelocity().getY())));
        BigDecimal v1t = (unitTangentVector.getX().multiply(ball1.getBallVelocity().getX())).add(unitTangentVector.getY().multiply(ball1.getBallVelocity().getY()));
        BigDecimal v2n = (unitNormalVector.getX().multiply(ball2.getBallVelocity().getX())).add(unitNormalVector.getY().multiply(ball2.getBallVelocity().getY()));
        BigDecimal v2t = (unitTangentVector.getX().multiply(ball2.getBallVelocity().getX())).add(unitTangentVector.getY().multiply(ball2.getBallVelocity().getY()));

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
        BigDecimal v1np = (v1n).add(v2n.multiply(CustomPoint2D.TWO_BD)).divide(CustomPoint2D.TWO_BD);
        BigDecimal v2np = (v2n).add(v1n.multiply(CustomPoint2D.TWO_BD)).divide(CustomPoint2D.TWO_BD);

        /**5
         * Convert scalar normal and tangential velocites into vectors
         */
        CustomPoint2D normalVectorFinalBall1 = unitNormalVector.multiply(v1np);
        CustomPoint2D normalVectorFinalBall2 = unitNormalVector.multiply(v2np);
        CustomPoint2D tangentialVectorFinalBall1 = unitTangentVector.multiply(v1tp);
        CustomPoint2D tangentialVectorFinalBall2 = unitTangentVector.multiply(v2tp);

        /**6
         * Add normal and tangential vectors for each ball
         */
        CustomPoint2D finalVectorBall1 = normalVectorFinalBall1.add(tangentialVectorFinalBall1);
        CustomPoint2D finalVectorBall2 = normalVectorFinalBall2.add(tangentialVectorFinalBall2);

        ball1.setBallVelocity(finalVectorBall1);
        ball2.setBallVelocity(finalVectorBall2);
    }


    public ArrayList<BallView> ballViewArrayList() {
        return bViewList;
    }

    public ArrayList<BallModel> ballModelArrayList() {return bModelList; }
}

package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.model.VelocityVector;
import io.pool.view.BallView;
import javafx.geometry.Point2D;
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
            bModelList.add(new BallModel(15, 1, new Image(new File("resources/billiards3D/ball1.jpg").toURI().toURL().toExternalForm())));
            bViewList.add(new BallView(this,bModelList.get(0).getImg(),BallModel.getRadius()));

            //bModelList.add(new BallModel(15, 2, new Image(new File("resources/billiards3D/ball2.jpg").toURI().toURL().toExternalForm())));
            //bViewList.add(new BallView(this,bModelList.get(1).getImg(),BallModel.getRadius()));
            ///bModelList.get(1).setBallPosition(new Point2D(600,350));
            //bModelList.get(1).setBallVector(new VelocityVector(0,0));
            //bModelList.get(1).setMovingBall(false);
            root.getChildren().addAll(bViewList.get(0).getBall());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void prepareGame(Pane root) throws MalformedURLException {
        for (int i=1;i<=16;i++){
            if(i==16){
                bModelList.add(new BallModel(15, i, new Image(new File("resources/billiards3D/white.jpg").toURI().toURL().toExternalForm())));
                bModelList.get(i-1).setBallVector(new VelocityVector(0,0));
            }else{
                bModelList.add(new BallModel(15, i, new Image(new File("resources/billiards3D/ball"+i+".jpg").toURI().toURL().toExternalForm())));
            }
            bViewList.add(new BallView(this,bModelList.get(i-1).getImg(),BallModel.getRadius()));
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
            if ((bModel.getBallPosition().getX() - BallModel.getRadius()) <= (tableBorders.getX() + 300) || (bModel.getBallPosition().getX() + BallModel.getRadius()) >= ((tableBorders.getX() + 300) + tableBorders.getWidth())) {
                double newXVelocity = -bModel.getBallVector().getX();
                bModel.setBallVector(new VelocityVector(newXVelocity, bModel.getBallVector().getY()));
            }
            if (((bModel.getBallPosition().getY() - BallModel.getRadius()) <= (tableBorders.getY() + 100)) || ((bModel.getBallPosition().getY() + BallModel.getRadius()) >= ((tableBorders.getY() + 100) + tableBorders.getHeight()))) {
                double newYVelocity = -bModel.getBallVector().getY();
                bModel.setBallVector(new VelocityVector(bModel.getBallVector().getX(), newYVelocity));
                System.out.println("Vector: " + bModel.getBallVector());
            }
        }
    }
    private void detectCollisionWithOtherBalls(){
        for (BallModel ballA : bModelList) {
            for (BallModel ballB : bModelList) {
                if (!ballA.equals(ballB)) {
                    if((ballA.isMovingBall())||(ballB.isMovingBall())){
                        if(ballA.getBallPosition().distance(ballB.getBallPosition())<=(2*BallModel.getRadius())){
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
                                double angle = movingBall.getBallVector().getAngle();
                                Point2D newCoordinate = new Point2D(Math.cos(angle) * (2 * BallModel.getRadius()), Math.sin(angle) * (2 * BallModel.getRadius()));
                                movingBall.setBallPosition(newCoordinate);

                            }
                            System.out.println("Collision");
                            collisionHandler(ballA,ballB);
                        }
                    }
                }
            }
        }
        }

    private void updateBallPosition(double time) {
        for (BallModel bModel : bModelList) {
            // TODO Friction is too high. To Adjust
            applyFriction(bModel,time);
            BallView bView = bViewList.get(bModelList.indexOf(bModel));
            bModel.setMovingBall(!bModel.getBallVector().isZero());
            bModel.setBallPosition(bModel.getBallPosition().add(bModel.getBallVector().getX(),bModel.getBallVector().getY()));
            bView.getBall().setLayoutX(bModel.getBallPosition().getX());
            bView.getBall().setLayoutY(bModel.getBallPosition().getY());
            Rotate rx = new Rotate(bModel.getBallVector().getY(), 0,0,0,Rotate.X_AXIS);
            Rotate ry = new Rotate(-bModel.getBallVector().getX(), 0,0,0, Rotate.Y_AXIS);
            bView.getBall().getTransforms().addAll(rx,ry);
        }
    }
    public void applyFriction(BallModel bModel, double time){
        //TODO friction headache
        double frictionForce = 0.01*BallModel.MASS_BALL_KG*BallModel.GRAVITATIONAL_FORCE;
        double ballForce = (BallModel.MASS_BALL_KG*bModel.getDeltaV())/time;
        System.out.println("Friction: "+frictionForce);
        System.out.println("BallForce: "+ballForce);
        double newBallForce = ballForce+frictionForce;
        double velocityMag = (newBallForce*time)/BallModel.MASS_BALL_KG;
        double angle = bModel.getBallVector().getAngle();
        VelocityVector newVelocity = new VelocityVector(Math.cos(angle)*velocityMag-bModel.getBallVector().getX(),Math.sin(angle)*velocityMag-bModel.getBallVector().getY());
        bModel.setBallVector(newVelocity);
    }


    private void collisionHandler(BallModel ball1, BallModel ball2){
        //https://vobarian.com/collisions/2dcollisions2.pdf
        /**1
         * find unit normal and unit tanget vector
         */
        double normalXComponent = ball2.getBallPosition().getX()-ball1.getBallPosition().getX();
        double normalYComponent = ball2.getBallPosition().getY()-ball1.getBallPosition().getY();
        VelocityVector normalVectorInitial = new VelocityVector(normalXComponent, normalYComponent);
        //double normalMagnitude = Math.sqrt(Math.pow(normalXComponent, 2)+Math.pow(normalYComponent, 2));
        //double normalAngle = Math.atan(normalYComponent/normalXComponent);
        VelocityVector unitNormalVector = new VelocityVector(normalXComponent/Math.abs(normalXComponent), normalYComponent/Math.abs(normalYComponent));
        VelocityVector unitTangentVector = new VelocityVector(-unitNormalVector.getY(), unitNormalVector.getX());

        /**2 (step 2 is skipped because we already have the balls vectors
         * Resolve velocity vectors of ball 1 and 2 into normal and tangential components
         * this is done by using the dot product of the balls initial velocity and using the unitVectors
         */
        double v1n = (unitNormalVector.getX()*ball1.getBallVector().getX())+(unitNormalVector.getY()*ball1.getBallVector().getY());
        double v1t = (unitTangentVector.getX()*ball1.getBallVector().getX())+(unitTangentVector.getY()*ball1.getBallVector().getY());
        double v2n = (unitNormalVector.getX()*ball2.getBallVector().getX())+(unitNormalVector.getY()*ball2.getBallVector().getY());
        double v2t = (unitTangentVector.getX()*ball2.getBallVector().getX())+(unitTangentVector.getY()*ball2.getBallVector().getY());

        /**3
         * Find new tangential velocities
         * they are equal to the initial ones
         */
        double v1tp = v1t;
        double v2tp = v2t;

        /**4
         * Find new normal velocities
         * all the instances of 1 in this equation are substitutes for mass
         * this is assuming all the balls have equal mass
         */
        double v1np = ((v1n*1)+(2*1*v2n))/(1+1);
        double v2np = ((v2n*1)+(2*1*v1n))/(1+1);

        /**5
         * Convert scalar normal and tangential velocites into vectors
         */
        VelocityVector normalVectorFinalBall1 = unitNormalVector.mul(v1np);
        VelocityVector normalVectorFinalBall2 = unitNormalVector.mul(v2np);
        VelocityVector tangentialVectorFinalBall1 = unitTangentVector.mul(v1tp);
        VelocityVector tangentialVectorFinalBall2 = unitTangentVector.mul(v2tp);

        /**6
         * Add normal and tangential vectors for each ball
         */
        VelocityVector finalVectorBall1 = normalVectorFinalBall1.add(tangentialVectorFinalBall1);
        VelocityVector finalVectorBall2 = normalVectorFinalBall2.add(tangentialVectorFinalBall2);

        ball1.setBallVector(finalVectorBall1);
        ball2.setBallVector(finalVectorBall2);
    }


    public ArrayList<BallView> ballViewArrayList() {
        return bViewList;
    }
}

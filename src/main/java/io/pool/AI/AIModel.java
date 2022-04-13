package io.pool.AI;
import io.pool.controller.BallController;
import io.pool.controller.PoolCueController;
import io.pool.controller.TableController;
import io.pool.model.BallModel;
import io.pool.view.TableView;
import javafx.geometry.Point2D;

import java.util.Random;

public class AIModel {
    public final static int EASY_AI=3;
    public final static int MEDIUM_AI=5;
    public final static int HARD_AI=10;

    private double evaluation=0;
    private double power;
    private double rotation;
    private static AIController aiController;

    public AIModel(int direction) {
        Random rnd = new Random();
        if(direction==-1) {
            this.power = rnd.nextInt(PoolCueController.MAX_DISTANCE-30)+31;
            this.rotation = rnd.nextInt(360) + 1;
        }else{
            BallModel selectedBall = aiController.getAIPLayer().getBallNeededIn().get(direction);
            double x = selectedBall.getPositionX().doubleValue()-BallController.whiteBallModel.getPositionX().doubleValue();
            double y = selectedBall.getPositionY().doubleValue()-BallController.whiteBallModel.getPositionY().doubleValue();
            double distance = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
            double distanceReference = TableView.getTableWidth()*(2/3)*(1/3);
            int variation=0;
            this.rotation = Math.toDegrees(Math.atan2(y, x));
            if(distance<distanceReference){
                this.power = rnd.nextInt((int)(PoolCueController.MAX_DISTANCE*0.3))+1;
                variation = rnd.nextInt(10)+1;
                this.rotation+=(175+variation);
            }else if(distance<(2*distanceReference)){
                this.power = rnd.nextInt((int)(PoolCueController.MAX_DISTANCE*0.35))+(PoolCueController.MAX_DISTANCE*0.3);
                variation = rnd.nextInt(8)+1;
                this.rotation+=(176+variation);
            }else{
                this.power = rnd.nextInt((int)(PoolCueController.MAX_DISTANCE*0.35))+(PoolCueController.MAX_DISTANCE*0.65);
                variation = rnd.nextInt(6)+1;
                this.rotation+=(177+variation);
            }
            System.out.println("Ball# "+selectedBall.getNumber()+" | Power: "+this.power+" | Rotation "+this.rotation);
        }
    }

    public double getPower() {
        return power;
    }

    public double getRotation() {
        return rotation;
    }

    public static void setAiController(AIController aiController) {
        AIModel.aiController = aiController;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        return "AIModel{" +
                "power=" + power +
                ", rotation=" + rotation +
                '}';
    }
}

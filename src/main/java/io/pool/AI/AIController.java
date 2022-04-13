package io.pool.AI;

import io.pool.controller.BallController;
import io.pool.controller.GameController;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;

public class AIController extends Thread{

    private GameController gameController;
    private int difficulty;
    private PlayerModel AIPLayer;
    private boolean AITraining = false;

    public AIController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void run() {
        train();
    }

    public void train(){
        AITraining=true;
        AIModel aiModel, bestAI=new AIModel();
        double currentEvaluation=0, bestEvaluation=-1;
        for(int i=0; i<difficulty;i++){
            System.out.println("Iteration "+i);
            aiModel = new AIModel();
            gameController.simulatePlay(aiModel);
            boolean isMoving = true;
            while(isMoving) {
                boolean foundMovement = false;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(BallModel b:BallController.bModelList){
                    if(b.isMoving){
                        isMoving=true;
                        foundMovement=true;
                    }
                }
                if(!foundMovement) isMoving=false;
            }
            currentEvaluation = evaluate();

            if(currentEvaluation>bestEvaluation) {
                bestEvaluation=currentEvaluation;
                bestAI = aiModel;
            }
            gameController.resetSimulation();
        }
        AITraining=false;
        System.out.println("Finished Training");
        System.out.println(bestAI+" --> "+bestEvaluation);
        gameController.simulatePlay(bestAI);
    }

    private double evaluate() {
        double evaluation=0;

        BallModel ballA,ballB;
        for (var i = 0; i < BallController.bModelList.size(); i++){
            for(var j = i + 1 ; j < BallController.bModelList.size() ; j++){
                ballA=BallController.bModelList.get(i);
                ballB=BallController.bModelList.get(j);
                if(ballA.equals(BallController.whiteBallModel) || ballB.equals(BallController.whiteBallModel)){
                    continue;
                }
                evaluation += ballA.distanceFrom(ballB);
            }
        }

        evaluation = evaluation/10;


        int validBalls=gameController.getbModelInEachTurn().size();
//        if(gameController.isScored()){
//            for (BallModel bModelIn : gameController.getbModelInEachTurn()){
//
//            }
//        }
//        for(BallModel bModelIn : gameController.getbModelInEachTurn()) {
//            for (BallModel bModelNeeded : AIPLayer.getBallNeededIn()) {
//                if (bModelIn.equals(bModelNeeded)) validBalls++;
//            }
////        System.out.println(gameController.getbModelInEachTurn());
//        }
        System.out.println("Valid Balls: "+validBalls);

        if(gameController.getbModelInEachTurn().contains(BallController.eightBallModel)){
            if(!AIPLayer.getBallNeededIn().contains(BallController.eightBallModel)) {
                evaluation -= 100000000;
            }else{
                evaluation += 100000000;
            }
        }


        evaluation += 20000 * validBalls;

        if(gameController.isScored()){
            if(!gameController.isFoul()){
                evaluation += 10000;
            }
            else{
                evaluation -= 10000;
            }
        }

        if(gameController.isFoul()){
            evaluation = evaluation - 30000;
        }
        System.out.println("Current Evaluation: "+evaluation);

        return evaluation;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public PlayerModel getAIPLayer() {
        return AIPLayer;
    }

    public void setAIPLayer(PlayerModel AIPLayer) {
        this.AIPLayer = AIPLayer;
    }

    public boolean isAITraining() {
        return AITraining;
    }
}

package io.pool.AI;

import io.pool.controller.BallController;
import io.pool.controller.GameController;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;

public class AIController {

    private GameController gameController;
    private int difficulty;
    private PlayerModel AIPLayer;
    private boolean AITraining = false;

    public AIController(GameController gameController) {
        this.gameController = gameController;
    }

    public void train(){
        AITraining=true;
        AIModel aiModel, bestAI=new AIModel();
        int currentEvaluation=0, bestEvaluation=-1;
        for(int i=0; i<difficulty;i++){
            aiModel = new AIModel();
            gameController.simulatePlay(aiModel);
            currentEvaluation = evaluate();
            if(currentEvaluation>bestEvaluation) {
                bestEvaluation=currentEvaluation;
                bestAI = aiModel;
            }
            gameController.resetSimulation();
        }
        AITraining=false;
        gameController.simulatePlay(bestAI);
    }

    private int evaluate() {
        int evaluation =0;

        BallModel ballA,ballB;
        for (var i = 0; i < BallController.bModelList.size(); i++){
            ballA=BallController.bModelList.get(i);
            for(var j = i + 1 ; j < BallController.bModelList.size() ; j++){
                ballB=BallController.bModelList.get(j);

                if(ballA.equals(BallController.whiteBallModel) || ballB.equals(BallController.whiteBallModel)){
                    continue;
                }
                evaluation += ballA.distanceFrom(ballB);
            }
        }

        evaluation = evaluation/5800;

//        if(!gamePolicy.firstCollision){
//            evaluation+= 100;
//        }
        int validBalls =0;
        for(BallModel bModelIn : gameController.getbModelInEachTurn()){
            for(BallModel bModelNeeded: AIPLayer.getBallNeededIn()){
                if(bModelNeeded.equals(bModelIn)) validBalls++;
            }
        }

        evaluation += 2000 * validBalls;

        if(gameController.isScored()){
            if(!gameController.isFoul()){
                evaluation += 10000;
            }
            else{
                evaluation -= 10000;
            }
        }

        if(gameController.isFoul()){
            evaluation = evaluation - 3000;
        }

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

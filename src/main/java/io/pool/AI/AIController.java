package io.pool.AI;

import io.pool.controller.BallController;
import io.pool.controller.GameController;
import io.pool.controller.TableController;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class AIController {

    private GameController gameController;
    private int difficulty;
    private PlayerModel AIPLayer;
    private boolean AITraining = false;

    public AIController(GameController gameController) {
        this.gameController = gameController;
        AIModel.setAiController(this);
    }

    public void train() {
        AITraining = true;
        AIModel bestAI = new AIModel(-1);
        double bestEvaluation = -1;
        for (int i = 0; i < difficulty; i++) {

        }
        AITraining = false;
        System.out.println("Finished Training");
        System.out.println(bestAI + " --> " + bestEvaluation);
        gameController.simulatePlay(bestAI);
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

class AITrainer extends Thread {
    private GameController gameController;
    private AIModel aiModel;
    private BallController ballController;
    private ArrayList<BallModel> bModelList;
    private BallModel whiteBallModel = null;
    private BallModel eightBallModel = null;
    private PlayerModel AIPlayer;

    public AITrainer(GameController gameController, PlayerModel aiPlayer) {
        super();
        this.AIPlayer = aiPlayer;
        this.gameController = gameController;
        this.ballController = new BallController();
        this.bModelList = (ArrayList<BallModel>) BallController.bModelList.clone();
        for (BallModel b : this.bModelList) {
            if (b.getNumber() == 16) whiteBallModel = b;
            if (b.getNumber() == 8) eightBallModel = b;
        }
    }

    @Override
    public void run() {
        aiModel = new AIModel(new Random().nextInt(AIPlayer.getBallNeededIn().size()));
        gameController.simulatePlay(aiModel);
        aiModel.setEvaluation(evaluate());
        gameController.resetSimulation();

    }

    private double evaluate() {
        double evaluation = 0;

        BallModel ballA, ballB;
        for (var i = 0; i < this.bModelList.size(); i++) {
            for (var j = i + 1; j < this.bModelList.size(); j++) {
                ballA = this.bModelList.get(i);
                ballB = this.bModelList.get(j);
                if (ballA.equals(whiteBallModel) || ballB.equals(whiteBallModel)) {
                    continue;
                }
                evaluation += ballA.distanceFrom(ballB);
            }
        }

        evaluation = evaluation / 1000;


        simulateOutcome();

        int validBalls = 0;

        for (BallModel bModel : this.bModelList) {
            if (bModel.isInHole() && AIPlayer.getBallNeededIn().contains(bModel)) validBalls++;
        }
        System.out.println("Valid Balls: " + validBalls);

        if (gameController.getbModelInEachTurn().contains(eightBallModel)) {
            if (!AIPlayer.getBallNeededIn().contains(eightBallModel)) {
                evaluation -= 100000000;
            } else {
                evaluation += 100000000;
            }
        }


        evaluation += 20000 * validBalls;


        // Change to ballcontroller
        if (gameController.isScored()) {
            if (!gameController.isFoul()) {
                evaluation += 10000;
            } else {
                evaluation -= 10000;
            }
        }

        if (gameController.isFoul()) {
            evaluation = evaluation - 30000;
        }
        System.out.println("Current Evaluation: " + evaluation);

        return evaluation;
    }

    private void simulateOutcome() {
        boolean isMoving = true;
        while (isMoving) {

            //ballController.detectCollision(gameController.getTableController());

            boolean foundMovement = false;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (BallModel b : BallController.bModelList) {
                if (b.isMoving) {
                    isMoving = true;
                    foundMovement = true;
                }
            }
            if (!foundMovement) isMoving = false;
        }
    }
}

package io.pool.AI;

import io.pool.controller.BallController;
import io.pool.controller.GameController;
import io.pool.controller.TableController;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

public class AIController {

    private GameController gameController;
    private int difficulty;
    private PlayerModel AIPLayer;
    private boolean AITraining = false;
    private ExecutorService executor;
    private ArrayList<Future<AIModel>> aiList = new ArrayList<>();
    private AIModel bestAI;

    public AIController(GameController gameController) {
        this.gameController = gameController;
        AIModel.setAiController(this);
    }

    public void train() {
        AITraining = true;
        aiList.clear();
        executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < difficulty; i++) {
            aiList.add(executor.submit(new AITrainer(gameController,AIPLayer.getBallNeededIn())));
        }
        double currentEvaluation = -1, bestEvaluation=-1;
        for(Future<AIModel> currentAI:aiList){
            try {
                currentEvaluation = currentAI.get().getEvaluation();
                if(currentEvaluation>bestEvaluation){
                    bestEvaluation=currentEvaluation;
                    bestAI=currentAI.get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        while (!executor.isTerminated()) {   }
        AITraining = false;
        System.out.println("Finished Training");
        System.out.println(bestAI + " --> " + bestEvaluation);
    }

    public AIModel getBestAI() {
        return bestAI;
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

    class AITrainer implements Callable<AIModel> {
        private GameController gameController;
        private BallController ballController;
        private ArrayList<BallModel> bModelList;
        private BallModel whiteBallModel = null;
        private BallModel eightBallModel = null;
        private ArrayList<Circle> Holes;
        private ArrayList<BallModel> ballNeededIn;

        public AITrainer(GameController gameController, ArrayList<BallModel> ballNeededIn) {
            this.ballNeededIn = ballNeededIn;
            this.gameController = gameController;
            this.bModelList = (ArrayList<BallModel>) BallController.bModelList.clone();
            this.ballController = new BallController();
            for (BallModel b : this.bModelList) {
                if (b.getNumber() == 16) whiteBallModel = b;
                if (b.getNumber() == 8) eightBallModel = b;
            }
        }

        @Override
        public AIModel call() throws Exception {
            BallModel targetedBall = AIPLayer.getBallNeededIn().get(new Random().nextInt(AIPLayer.getBallNeededIn().size()));
            AIModel aiModel = new AIModel(targetedBall);
            gameController.simulatePlay(aiModel);
            aiModel.setEvaluation(evaluate());
            return aiModel;
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
                if (bModel.isInHole() && ballNeededIn.contains(bModel)) validBalls++;
            }
            System.out.println("Valid Balls: " + validBalls);

            if (gameController.getbModelInEachTurn().contains(eightBallModel)) {
                if (!ballNeededIn.contains(eightBallModel)) {
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



}
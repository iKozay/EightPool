package io.pool.AI;

import io.pool.controller.BallController;
import io.pool.controller.GameController;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.math.BigDecimal;
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
    }

    public void train() {
        double now = System.currentTimeMillis();
        AITraining = true;
        aiList.clear();
        executor = Executors.newFixedThreadPool(difficulty);
        for (int i = 0; i < difficulty; i++) {
            aiList.add(executor.submit(new AITrainer(gameController,AIPLayer.getBallNeededIn())));
        }
        double currentEvaluation, bestEvaluation=Integer.MAX_VALUE;
        for(Future<AIModel> currentAI:aiList){
            try {
                if(bestEvaluation==Integer.MAX_VALUE){
                    bestEvaluation = currentAI.get().getEvaluation();
                    bestAI = currentAI.get();
                }else {
                    currentEvaluation = currentAI.get().getEvaluation();
                    if (currentEvaluation > bestEvaluation) {
                        bestEvaluation = currentEvaluation;
                        bestAI = currentAI.get();
                    }
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
        System.out.println("Finished Training in "+(System.currentTimeMillis()-now)/1000+" seconds.");
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
        private BallController ballController;
        private ArrayList<BallModel> bModelList = new ArrayList<>();
        private BallModel whiteBallModel = null;
        private BallModel eightBallModel = null;
        private ArrayList<BallModel> ballNeededIn;

        public AITrainer(GameController gameController, ArrayList<BallModel> ballNeededIn) {
            this.ballNeededIn = ballNeededIn;
            for (BallModel bModel : BallController.bModelList){
                bModelList.add(bModel.clone());
            }
            this.ballController = new BallController(gameController);
            for (BallModel b : this.bModelList) {
                if (b.getNumber() == 16) whiteBallModel = b;
                if (b.getNumber() == 8) eightBallModel = b;
            }
        }

        @Override
        public AIModel call() throws Exception {
            BallModel targetedBall = targetABall();
            AIModel aiModel = new AIModel(targetedBall);
            simulateShot(aiModel);
            aiModel.setEvaluation(evaluate());
            return aiModel;
        }

        private int counter=0;
        private BallModel targetABall(){
            boolean obstacle = false;
            BallModel target = AIPLayer.getBallNeededIn().get(new Random().nextInt(AIPLayer.getBallNeededIn().size()));
            Path path = new Path();
            path.setStrokeWidth(4*BallModel.RADIUS);
            MoveTo moveTo = new MoveTo();
            LineTo lineTo = new LineTo();

            moveTo.setX(whiteBallModel.getPositionX().doubleValue());
            moveTo.setY(whiteBallModel.getPositionY().doubleValue());

            lineTo.setX(target.getPositionX().doubleValue());
            lineTo.setY(target.getPositionY().doubleValue());

            if(counter<ballNeededIn.size()){
                for(BallModel ballModel : bModelList){
                    if(path.contains(ballModel.getPositionX().doubleValue(),ballModel.getPositionY().doubleValue())){
                        obstacle=true;
                        break;
                    }
                }
            }

            if(!obstacle) return target;
            counter++;
            return targetABall();
        }

        private void simulateShot(AIModel aiModel) {
            double newVelocityX = aiModel.getPower() * Math.cos(Math.toRadians(aiModel.getRotation()));
            double newVelocityY = aiModel.getPower() * Math.sin(Math.toRadians(aiModel.getRotation()));
            whiteBallModel.setVelocityX(new BigDecimal(-newVelocityX/7));
            whiteBallModel.setVelocityY(new BigDecimal(-newVelocityY/7));
            ballController.isMoving=true;
            simulateOutcome();
        }

        private void simulateOutcome() {
            boolean isMoving = ballController.isMoving;
            while (isMoving) {
                ballController.isMoving=false;
                ballController.detectCollision(bModelList);
                isMoving = ballController.isMoving;
            }
            ballController.checkFoul(whiteBallModel);
            ballController.checkScored(bModelList);
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

            evaluation = evaluation / 7000;


            int validBalls = 0;

            for (BallModel bModel : this.bModelList) {
                if (bModel.isInHole() && ballNeededIn.contains(bModel)) validBalls++;
            }
            if (eightBallModel.isInHole()) {
                if (!ballNeededIn.contains(eightBallModel)) {
                    evaluation -= 100000000;
                } else {
                    evaluation += 100000000;
                }
            }

            if(ballNeededIn.contains(ballController.getFirstCollide())) evaluation+=1000;

            evaluation += 2000 * validBalls;

            if (ballController.isScored()) {
                if (!ballController.isFoul()) {
                    evaluation += 10000;
                } else {
                    evaluation -= 10000;
                }
            }
            if (ballController.isFoul()) {
                evaluation = evaluation - 3000;
            }
            return evaluation;
        }
    }



}
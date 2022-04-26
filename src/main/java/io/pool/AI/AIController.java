package io.pool.AI;

import io.pool.controller.BallController;
import io.pool.controller.GameController;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;
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

    private double now;
    public void train() {
        int currentDifficulty = difficulty;
        // Counter effect the pigeon-hole problem only applicable for Easy and Medium
        if(difficulty!=AIModel.HARD_AI) {
            if (currentDifficulty >= AIPLayer.getBallNeededIn().size()) {
                if(AIPLayer.getBallNeededIn().size()>1){
                    currentDifficulty = AIPLayer.getBallNeededIn().size()-1;
                }else{
                    currentDifficulty=1;
                }
            }
        }

        now = System.currentTimeMillis();
        AITraining = true;
        aiList.clear();
        executor = Executors.newFixedThreadPool(currentDifficulty);
        for (int i = 0; i < currentDifficulty; i++) {
            aiList.add(executor.submit(new AITrainer(i,gameController,AIPLayer.getBallNeededIn())));
        }
        executor.shutdown();
    }

    public boolean readyToPlay(){
        if(executor!=null) {
            if (executor.isTerminated()) {
                if (isAITraining()) retrieveBestAI();
                if (!isAITraining()) return true;
            }
        }
        return false;
    }

    private void retrieveBestAI(){
        double currentEvaluation, bestEvaluation=Integer.MIN_VALUE;
        for(Future<AIModel> currentAI:aiList){
            try {
                if(bestEvaluation==Integer.MIN_VALUE){
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
        private int instanceID =-1;

        public AITrainer(int id,GameController gameController, ArrayList<BallModel> ballNeededIn) {
            this.instanceID=id;
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
            if(difficulty==AIModel.HARD_AI&&ballNeededIn.size()>7) {
                // go over every ball if Hard Difficulty
                counter=instanceID;
                while(counter>=AIPLayer.getBallNeededIn().size()) counter-=AIPLayer.getBallNeededIn().size();
                BallModel target = AIPLayer.getBallNeededIn().get(counter);
                return target;
            }else{
                boolean obstacle = false;
                BallModel target = AIPLayer.getBallNeededIn().get(new Random().nextInt(AIPLayer.getBallNeededIn().size()));
                Path path = new Path();
                path.setStrokeWidth(4 * BallModel.RADIUS);
                MoveTo moveTo = new MoveTo();
                LineTo lineTo = new LineTo();

                moveTo.setX(whiteBallModel.getPositionX().doubleValue());
                moveTo.setY(whiteBallModel.getPositionY().doubleValue());

                lineTo.setX(target.getPositionX().doubleValue());
                lineTo.setY(target.getPositionY().doubleValue());

                if (counter < ballNeededIn.size()) {
                    for (BallModel ballModel : bModelList) {
                        if (path.contains(ballModel.getPositionX().doubleValue(), ballModel.getPositionY().doubleValue())) {
                            obstacle = true;
                            break;
                        }
                    }
                }
                if (!obstacle) return target;
                counter++;
                return targetABall();
            }
        }

        private void simulateShot(AIModel aiModel) {
            double newVelocityX = aiModel.getPower() * Math.cos(Math.toRadians(aiModel.getRotation()));
            double newVelocityY = aiModel.getPower() * Math.sin(Math.toRadians(aiModel.getRotation()));
            whiteBallModel.setVelocityX(new BigDecimal(-newVelocityX/5));
            whiteBallModel.setVelocityY(new BigDecimal(-newVelocityY/5));
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
            int evaluation = 0;


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

            //Make strict foul policy

            if (ballController.isScored()) {
                if (!ballController.isFoul()) {
                    evaluation += 10000;
                } else {
                    evaluation -= 10000;
                }
            }
            if (ballController.isFoul()) {
                evaluation -= 20000;
            }
            return evaluation;
        }
    }



}
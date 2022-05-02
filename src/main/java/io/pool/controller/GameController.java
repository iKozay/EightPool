package io.pool.controller;

import io.pool.AI.AIController;
import io.pool.AI.AIModel;
import io.pool.Database.BallConfigurationDB;
import io.pool.Database.PlayerTableDB;
import io.pool.eightpool.ResourcesLoader;
import io.pool.model.BallModel;
import io.pool.model.PhysicsModule;
import io.pool.model.PlayerModel;
import io.pool.view.BallView;
import io.pool.view.GameView;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.util.ArrayList;

public class GameController {
    private AIController aiController;
    /**
     * Instance of GameView that contains all the Ball,Table and Pool Cue Views
     */
    private GameView gameView;
    /**
     * Table Controller
     */
    private TableController tableController;
    /**
     * Ball Controller
     */
    private BallController ballController;
    /**
     * Pool Cue Controller
     */
    private PoolCueController poolCueController;
    /**
     * Animation Timer that helps to update the View every frame
     */
    public GameLoopTimer gameLoopTimer;
    /**
     * GameModel that helps keep track of game status
     */
    private SettingsController settingsController;

    private PlayerModel p1 = null;
    private PlayerModel p2 = null;
    public PlayerModel currentPlayer=null;
    private int gameType;
    private int p1Score=0;
    private int p2Score=0;
    private int p1Shots=0;
    private int p2Shots=0;

    private boolean firstPlay = true;
    private ArrayList<BallModel> bModelInEachTurn = new ArrayList<>();
    private static int counter = 0;
    private boolean waitingForInput = true;
    private boolean setBallType = false;


//    private ArrayList<BallModel> bModelIn = new ArrayList<>();


    /**
     * Main Constructor of GameController
     *
     * @param gView Instance of GameView
     */
    public GameController(GameView gView, SettingsController settingsController) {
        /** Assign the GameView and Instantiate the Controllers */
        this.gameView = gView;
        tableController = new TableController(this.gameView.getTableView(), this);
        ballController = new BallController(this);
        poolCueController = new PoolCueController(this.gameView.getCueView(), this);
        this.settingsController = settingsController;
        this.aiController = new AIController(this);

        /** Instantiate the gameLoopTimer and Override the tick Method */
        final GameController gameController = this;
        gameLoopTimer = new GameLoopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {

                if (ballController.isFoul()) {
                    gameView.getTableView().getFoulNotificationLabel().setVisible(true);
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), new KeyValue(gameView.getTableView().getFoulNotificationLabel().visibleProperty(), false, Interpolator.EASE_BOTH)));
                    timeline.play();
                }

                /** The time between each frame should be less than 1 second
                 * Any value bigger than 1 second is incorrect
                 * */
                /** Detect collisions */
                tableController.turnView(gameController);
                if (gameType != 0) assignBallType();
                if(gameType>1&&p2.isTurn()){
                    if(aiController.readyToPlay()&&waitingForInput){
                        p2.setUsername(p2.getUsername().replace(" (Thinking...)",""));
                        poolCueController.setPoolCue(aiController.getBestAI().getPower(), aiController.getBestAI().getRotation());
                    }
                }
                ballController.detectCollision(null);
                if (firstPlay) ballController.makeDraggable();
                if (gameView.getClickedBallNumber() > 0 && !aiController.isAITraining()) {
                    double xSpeed = Math.round(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getVelocityX().doubleValue() * 100) / 100.;
                    double ySpeed = Math.round(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getVelocityY().doubleValue() * 100) / 100.;
                    double xAcc = Math.round(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getAccelerationX().doubleValue() * 100) / 100.;
                    double yAcc = Math.round(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getAccelerationY().doubleValue() * 100) / 100.;
                    gameView.getxSpeedField().setText(String.valueOf(xSpeed));
                    gameView.getySpeedField().setText(String.valueOf(ySpeed));
                    gameView.getxAccelerationField().setText(String.valueOf(xAcc));
                    gameView.getyAccelerationField().setText(String.valueOf(yAcc));
                }
                winnerPlayer();
                /**Check if all balls are not moving to display the poolCue and update the database*/
                if (!ballController.isMoving && !aiController.isAITraining()) {
                    /**methods when all balls have stopped moving*/
                    waitingForInput = true;
                    if (!poolCueController.isEnablePoolCueControl()) {
                        if (!firstPlay && !ballController.isDraggable()) turns();
                        updatePoolCuePosition();
                        if (!BallConfigurationDB.hasBeenCalled) {
                            BallConfigurationDB.updateLastPosition(gameType, currentPlayer.getUsername());
                            BallConfigurationDB.hasBeenCalled = true;
                        }
                        poolCueController.enablePoolCueControl();
                        ballController.setScored(false);
                    }
                } else {
                    gView.displayPoolCue(false);
                    poolCueController.disablePoolCueControl();
                }
                ballInHole();
                if (!aiController.isAITraining()) ballController.isMoving = false;
            }
        };

    }

    /**
     * Calls prepareGame method in BallController to instantiate the BallModels and BallViews
     * Then starts the gameLoopTimer
     */
    public void startGame(PlayerModel player1, PlayerModel player2) {
        if(player2==null){
            gameType=0;
        }else{
            if(PlayerModel.playersList.contains(player2)){
                gameType=1;
            }else{
                if(PlayerModel.aiList.contains(player2)){
                    gameType = 2+PlayerModel.aiList.indexOf(player2);
                }
            }
        }

        gameView.getTableView().setControlOption(settingsController.getControlOption());
        gameView.getTableView().setInformativeLabelText();
        gameLoopTimer.start();
        ballController.prepareGame(this.gameView, this.gameView.getTableView());
        gameView.clearBallViewDebug();
        gameView.ballViewDataDebug();
        p1 = player1;
        p1.setBallNeededIn((ArrayList<BallModel>) BallController.bModelList.clone());
        p1.getBallNeededIn().remove(BallController.eightBallModel);
        p1.getBallNeededIn().remove(BallController.whiteBallModel);

        if (this.gameType == 0) {
            // SOLO
            p2 = PlayerModel.notAvailablePlayer;
//            p2.setBallNeededIn((ArrayList<BallModel>) BallController.bModelList.clone());
//            p2.getBallNeededIn().remove(BallController.eightBallModel);
//            p2.getBallNeededIn().remove(BallController.whiteBallModel);
            tableController.getTableView().getPlayersScore().setText("- : -");
        } else {
            p2 = player2;
            if(gameType>1) aiController.setAIPLayer(p2);
            if (this.gameType == 2) {
                aiController.setDifficulty(AIModel.EASY_AI);
            } else if (this.gameType == 3) {
                aiController.setDifficulty(AIModel.MEDIUM_AI);
            } else if (this.gameType == 4) {
                aiController.setDifficulty(AIModel.HARD_AI);
            }
            p2.setTurn(false);
            p2.setBallNeededIn((ArrayList<BallModel>) BallController.bModelList.clone());
            p2.getBallNeededIn().remove(BallController.eightBallModel);
            p2.getBallNeededIn().remove(BallController.whiteBallModel);
            tableController.getTableView().createRemainingBallSection();
        }

        if((gameType==0)||(gameType>1)){
            currentPlayer = p1;
        }else{
            if(currentPlayer==null) currentPlayer=p1;
        }
        currentPlayer.setTurn(true);
        getNextPlayer().setTurn(false);

        poolCueController.getCueView().getCue().setImage(ResourcesLoader.poolCueImages.get(currentPlayer.getSelectedPoolCue() - 1));

        BallConfigurationDB.instantiateLastLayoutDB(gameType, p1, p2, currentPlayer.getUsername());

        //ballController.testingBallController(this.gameView);
    }

    /**
     * Stops the gameLoopTimer
     * Destroys all the instances of BallModels and BallViews by calling
     * their respective method from BallController
     */
    public void resetGame(boolean keepScore) {
        gameLoopTimer.stop();
        //bModelIn.clear();
        bModelInEachTurn.clear();
        ballController.destroyViews(this.gameView);
        ballController.destroyModels();
        ballController.setFoul(false);
        ballController.setScored(false);
        ballController.setFirstCollide(null);
        ballController.isCollide=false;
        ballController.makeUnDraggable();
        BallController.setAllInSolid(false);
        BallController.setAllInStripe(false);
        waitingForInput = true;
        firstPlay = true;
        setBallType = false;
        counter=0;
        gameView.clearBallViewDebug();
        updatePlayerStats();
        p1Shots=0;
        p2Shots=0;

        if (!keepScore) {
            p1Score=0;
            p2Score=0;
            tableController.getTableView().getPlayersScore().setText("0 : 0");
            currentPlayer=null;
        }
        tableController.getTableView().removeRemainingBallsSection();
    }

    private void updatePlayerStats() {
        if(!gameLoopTimer.isActive()) {
            int totalMatches, totalShots;
            if (currentPlayer.equals(p1)) {
                totalMatches = p1.getNumberOfWins() + p1.getNumberOfLoss();
                totalShots = p1.getAverageNumberOfShotsPerGame() * totalMatches + p1Shots;
                totalMatches++;
                p1.setNumberOfWins(p1.getNumberOfWins() + 1);
                p1.setAverageNumberOfShotsPerGame(totalShots / totalMatches);

                totalMatches = p2.getNumberOfWins() + p2.getNumberOfLoss();
                totalShots = p2.getAverageNumberOfShotsPerGame() * totalMatches + p2Shots;
                totalMatches++;
                p2.setNumberOfLoss(p2.getNumberOfLoss() + 1);
                p2.setAverageNumberOfShotsPerGame(totalShots / totalMatches);
            }else{
                totalMatches = p2.getNumberOfWins() + p2.getNumberOfLoss();
                totalShots = p2.getAverageNumberOfShotsPerGame() * totalMatches + p2Shots;
                totalMatches++;
                p2.setNumberOfWins(p2.getNumberOfWins() + 1);
                p2.setAverageNumberOfShotsPerGame(totalShots / totalMatches);

                totalMatches = p1.getNumberOfWins() + p1.getNumberOfLoss();
                totalShots = p1.getAverageNumberOfShotsPerGame() * totalMatches + p1Shots;
                totalMatches++;
                p1.setNumberOfLoss(p1.getNumberOfLoss() + 1);
                p1.setAverageNumberOfShotsPerGame(totalShots / totalMatches);
            }
            PlayerTableDB.updatePlayerTableDB(p1);
            PlayerTableDB.updatePlayerTableDB(p2);
        }
    }

    public void turns() {
        ballController.makeUnDraggable();

        if (!firstPlay) {
            if (!ballController.isCollide) {
                System.out.println("No collide");
                ballController.setFoul(true);
            }
        }
        firstCollidePlay();
        if(!ballController.gotTypeIn&&!bModelInEachTurn.isEmpty()) ballController.setFoul(true);
        checkFoul();

        if (gameType == 0) {
            ballController.setFoul(false);
            return;
        }

        if (!ballController.isScored() || ballController.isFoul()) {
            setCurrentPlayer();
            SoundController.SwitchTurn();
        }


        //setting the pool cue on each turn
        poolCueController.getCueView().getCue().setImage(ResourcesLoader.poolCueImages.get(currentPlayer.getSelectedPoolCue() - 1));
        //System.out.println(currentPlayer.getBallType());
        ballController.setFoul(false);
        ballController.gotTypeIn=false;
        ballController.setScored(false);
        ballController.setFirstCollide(null);
        ballController.setCollide(false);
        bModelInEachTurn.clear();
        System.out.println(currentPlayer.getUsername() + "," + "your turn!");
        waitingForInput = true;
        counter++;
        if(gameType>1){
            if(p2.isTurn()){
                p2.setUsername(p2.getUsername()+" (Thinking...)");
                aiController.train();
            }
        }

    }

    public void assignBallType() {
        if (counter > 0 && !setBallType) {
            for (int i = 0; i < BallController.solidFull.size(); i++) {
                BallModel ballIn = BallController.solidFull.get(i);
                if (bModelInEachTurn.contains(ballIn)) {
                    ballController.setScored(true);
                    if (!aiController.isAITraining()) {
                        currentPlayer.getBallNeededIn().removeAll(bModelInEachTurn);
                        currentPlayer.getBallNeededIn().removeAll(BallController.stripeBModelList);
                        getNextPlayer().getBallNeededIn().removeAll(bModelInEachTurn);
                        getNextPlayer().getBallNeededIn().removeAll(BallController.solidBModelList);
                        currentPlayer.setBallType(0);
                        getNextPlayer().setBallType(1);
                        setBallType = true;
                        ballController.setFirstCollide(ballIn);
                        System.out.println(currentPlayer.getUsername() + ": " + currentPlayer.getBallType());
                        System.out.println(getNextPlayer().getUsername() + ": " + getNextPlayer().getBallType());
                    }
                }
            }
            for (int i = 0; i < BallController.stripFull.size(); i++) {
                BallModel ballIn = BallController.stripFull.get(i);
                if (bModelInEachTurn.contains(ballIn)) {
                    ballController.setScored(true);
                    if (!aiController.isAITraining()) {
                        getNextPlayer().getBallNeededIn().removeAll(bModelInEachTurn);
                        getNextPlayer().getBallNeededIn().removeAll(BallController.stripeBModelList);

                        currentPlayer.getBallNeededIn().removeAll(bModelInEachTurn);
                        currentPlayer.getBallNeededIn().removeAll(BallController.solidBModelList);
                        currentPlayer.setBallType(1);
                        getNextPlayer().setBallType(0);
                        setBallType = true;
                        ballController.setFirstCollide(ballIn);
                        System.out.println(currentPlayer.getUsername() + ": " + currentPlayer.getBallType());
                        System.out.println(getNextPlayer().getUsername() + ": " + getNextPlayer().getBallType());
                    }
                }
            }
            //tableController.setBallGotInHole(true);
            if (!aiController.isAITraining())
                BallConfigurationDB.assignBallType(gameType, p1.getBallType(), p2.getBallType());
        }
    }

    private void setCurrentPlayer() {

        currentPlayer.setTurn(false);
        currentPlayer = getNextPlayer();
        currentPlayer.setTurn(true);
    }

    private PlayerModel getNextPlayer() {
        if (currentPlayer.equals(p1)) return p2;
        return p1;
    }

    public void whiteBallIn(BallView ballView) {
        BallModel bModel = BallController.getBallModelFromBallView(ballView);
        if (bModel.getNumber() == 16) {
            ballController.setFoul(true);
            if(!ballController.isMoving) {
                bModel.setPositionX(new BigDecimal(BallController.ballXPositions.get(15)));
                bModel.setPositionY(new BigDecimal(BallController.ballYPositions.get(15)));
                bModel.setVelocityX(new BigDecimal(0.1));
                bModel.setVelocityY(new BigDecimal(0.1));
                BallController.whiteBallView.getBall().setVisible(true);
                bModel.setInHole(false);
            }else{
                BallController.whiteBallView.getBall().setVisible(false);
                bModel.setVelocityX(PhysicsModule.ZERO);
                bModel.setVelocityY(PhysicsModule.ZERO);
            }
        } else {
            if (!bModelInEachTurn.contains(bModel)) bModelInEachTurn.add(bModel);
        }

    }

    public void eightBallInIllegal() {
        if (bModelInEachTurn.contains(BallController.eightBallModel)) {
            if (!gameView.getPopupWindow().isShowing() && gameType != 0) {
                if(currentPlayer.equals(p1)) p2Score++;
                if(currentPlayer.equals(p2)) p1Score++;
            }
            if (gameType != 0) {
                tableController.getTableView().getPlayersScore().setText(p1Score + " : " + p2Score);
            }
            gameView.getPopupMessage().setText(currentPlayer + " lose!");
            gameView.getPopupWindow().show();
            p1.setTurn(false);
            p2.setTurn(false);
            gameLoopTimer.stop();
            setCurrentPlayer();
        }
    }

    public void eightBallInLegal() {
        if (bModelInEachTurn.contains(BallController.eightBallModel)) {
            if (!gameView.getPopupWindow().isShowing() && gameType != 0) {
                if(currentPlayer.equals(p1)) p1Score++;
                if(currentPlayer.equals(p2)) p2Score++;
            }
            if (gameType != 0) {
                tableController.getTableView().getPlayersScore().setText(p1Score + " : " + p2Score);
            }
            gameView.getPopupMessage().setText(currentPlayer + " win!");
            gameView.getPopupWindow().show();
            p1.setTurn(false);
            p2.setTurn(false);
            gameLoopTimer.stop();
        }
    }

    public void firstCollidePlay() {
        if (ballController.getFirstCollide() != null && setBallType) {
            if (currentPlayer.isTurn() && !(ballController.getFirstCollide().getBallType() == currentPlayer.getBallType())) {
                if(!aiController.isAITraining()&&!ballController.isFoul()) SoundController.Foul();
                ballController.setFoul(true);
            }
        }
    }

    public void ballInHole() {
        if (!bModelInEachTurn.isEmpty()) {
            for (BallModel b : bModelInEachTurn) {
                if(currentPlayer.getBallNeededIn().contains(b)){
                    ballController.gotTypeIn=true;
                }
                ballController.setScored(true);
                b.setVelocityX(BigDecimal.ZERO);
                b.setVelocityY(BigDecimal.ZERO);
                if (!aiController.isAITraining()) {
                    ballController.ballInHole(b, gameView);
                        if (gameType > 0) {
                        if (p1.getBallNeededIn().contains(b)) {
                            p1.getBallNeededIn().remove(b);
                        }
                        if (p2.getBallNeededIn().contains(b)) {
                            p2.getBallNeededIn().remove(b);
                        }
                        if (p1.getBallNeededIn().isEmpty()) {
                            p1.getBallNeededIn().add(BallController.eightBallModel);
                        }
                        if (p2.getBallNeededIn().isEmpty()) {
                            p2.getBallNeededIn().add(BallController.eightBallModel);
                        }
                    }
                }
            }
            if (ballController.isScored()&&setBallType) {
                tableController.getTableView().assignBallsInTableView(1, p1.getBallNeededIn());
                tableController.getTableView().assignBallsInTableView(2, p2.getBallNeededIn());
            }

        }
    }

    public void checkFoul() {
        if (ballController.isFoul()) {
            System.out.println("Foul");
            ballController.makeUnDraggable();
            ballController.makeDraggable();
        }
    }

    public void winnerPlayer() {
        boolean typeIn=false;
        if(gameType==0){
            typeIn=BallController.getAllInSolid() && BallController.getAllInStripe();
        }else{
            if (!aiController.isAITraining()) {
                if(currentPlayer.getBallType()==0) typeIn=BallController.getAllInSolid();
                if(currentPlayer.getBallType()==1) typeIn=BallController.getAllInStripe();
            }
        }
        if (currentPlayer.isTurn()&&typeIn) {
            eightBallInLegal();
        } else {
            eightBallInIllegal();
        }
    }


    public PlayerModel getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerModel currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Gets the Table Controller
     *
     * @return Table Controller
     */
    public TableController getTableController() {
        return tableController;
    }

    /**
     * Gets the Ball Controller
     *
     * @return Ball Controller
     */
    public BallController getBallController() {
        return ballController;
    }

    /**
     * Gets the Pool Cue Controller
     *
     * @return Pool Cue Controller
     */
    public PoolCueController getPoolCueController() {
        return poolCueController;
    }

    public GameView getGameView() {
        return gameView;
    }

    public PlayerModel getP1() {
        return p1;
    }

    public void setP1(PlayerModel p1) {
        this.p1 = p1;
    }

    public PlayerModel getP2() {
        return p2;
    }

    public void setP2(PlayerModel p2) {
        this.p2 = p2;
    }

    public int getP1Score() {
        return p1Score;
    }

    public void setP1Score(int p1Score) {
        this.p1Score = p1Score;
    }

    public int getP2Score() {
        return p2Score;
    }

    public void setP2Score(int p2Score) {
        this.p2Score = p2Score;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public AIController getAiController() {
        return aiController;
    }

    public void updatePoolCuePosition() {
        boolean show = true;
        if(gameType>1){
            if(p2.isTurn()) show=false;
        }
        gameView.displayPoolCue(show);
        if(poolCueController.poolCueView.getCue().isVisible()) {
            if(!ResourcesLoader.poolCueImages.get(5).equals(poolCueController.poolCueView.getCue().getImage())) {
                poolCueController.poolCueView.getCue().setX(BallController.whiteBallModel.getPositionX().doubleValue() + (BallModel.RADIUS));
                poolCueController.poolCueView.getCue().setY(BallController.whiteBallModel.getPositionY().doubleValue() - (BallModel.RADIUS * 0.55));
            }else{
                poolCueController.poolCueView.getCue().setX(BallController.whiteBallModel.getPositionX().doubleValue()+ (BallModel.RADIUS));
                poolCueController.poolCueView.getCue().setY(BallController.whiteBallModel.getPositionY().doubleValue()-(4.15*BallModel.RADIUS));
            }
            poolCueController.getRotate().setPivotX(BallController.whiteBallModel.getPositionX().doubleValue());
            poolCueController.getRotate().setPivotY(BallController.whiteBallModel.getPositionY().doubleValue());
            poolCueController.getRotate().setAngle(poolCueController.getCueView().getPreviousAngle());
            if (settingsController.getCueHelper() == 1) {
                poolCueController.poolCueView.getPoolLine().setVisible(true);
                poolCueController.poolCueView.getBallCollisionCircle().setVisible(true);
                poolCueController.poolCueView.getPoolLine().setStartX(BallController.whiteBallModel.getPositionX().doubleValue());
                poolCueController.poolCueView.getPoolLine().setStartY(BallController.whiteBallModel.getPositionY().doubleValue());
            } else {
                poolCueController.poolCueView.getPoolLine().setVisible(false);
                poolCueController.poolCueView.getBallCollisionCircle().setVisible(false);
            }
        }
    }

    public boolean isFirstPlay() {
        return firstPlay;
    }

    public void setFirstPlay(boolean b) {
        firstPlay = b;
    }

    public void setWaitingForInput(boolean b) {
        waitingForInput = b;
    }
    public void playerShot(){
        if(currentPlayer.equals(p1)) p1Shots++;
        if(currentPlayer.equals(p2)) p2Shots++;
    }
}

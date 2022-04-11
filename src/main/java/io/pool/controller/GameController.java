package io.pool.controller;

import io.pool.AI.AIController;
import io.pool.AI.AIModel;
import io.pool.Database.BallConfigurationDB;
import io.pool.eightpool.ResourcesLoader;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;
import io.pool.view.BallView;
import io.pool.view.GameView;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class GameController {
    private AIController aiController;
    /** Instance of GameView that contains all the Ball,Table and Pool Cue Views*/
    private GameView gameView;
    /** Table Controller */
    private TableController tableController;
    /** Ball Controller */
    private BallController ballController;
    /** Pool Cue Controller */
    private PoolCueController poolCueController;
    /** Animation Timer that helps to update the View every frame */
    public GameLoopTimer gameLoopTimer;
    /** GameModel that helps keep track of game status*/
    private PlayerModel playerModel;
    private SettingsController settingsController;


    private PlayerModel p1=null;
    private PlayerModel p2=null;
    private PlayerModel currentPlayer;
    private boolean foul=false;
    private boolean firstPlay=true,setBallType=false;
    private boolean scored = false;
    private boolean waitingForInput=true;
    private int gameType;
    private int p1Score;
    private int p2Score;
    private int counter = 0;

//    private ArrayList<BallModel> bModelIn = new ArrayList<>();
    private ArrayList<BallModel> bModelInEachTurn = new ArrayList<>();

    /**
     * Main Constructor of GameController
     * @param gView Instance of GameView
     */
    public GameController(GameView gView, SettingsController settingsController) {
        /** Assign the GameView and Instantiate the Controllers */
        this.gameView = gView;
        tableController = new TableController(this.gameView.getTableView(),this);
        ballController = new BallController(this);
        poolCueController = new PoolCueController(this.gameView.getCueView(), this);
        this.settingsController = settingsController;
        this.aiController = new AIController(this);

//        ballsPositionX = new ArrayList<>();
//        ballsPositionY = new ArrayList<>();
//        ballsSpeed = new ArrayList<>();

        /** Instantiate the gameLoopTimer and Override the tick Method */
        final GameController gameController=this;
        gameLoopTimer = new GameLoopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {
                /** The time between each frame should be less than 1 second
                 * Any value bigger than 1 second is incorrect
                 * */
                if(secondsSinceLastFrame<1) {
                        /** Detect collisions */
                        firstCollidePlay();
                        if(gameType!=0) assignBallType();
                        tableController.turnView(gameController);
                        ballController.detectCollision(tableController);
                        if(firstPlay) ballController.makeDraggable();
                        if (gameView.getClickedBallNumber() > 0) {
                            double xSpeed = Math.round(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getVelocityX().doubleValue()*100)/100.;
                            double ySpeed = Math.round(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getVelocityY().doubleValue()*100)/100.;
                            double xAcc = Math.round(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getAccelerationX().doubleValue()*100)/100.;
                            double yAcc = Math.round(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getAccelerationY().doubleValue()*100)/100.;
                            gameView.getxSpeedField().setText(String.valueOf(xSpeed));
                            gameView.getySpeedField().setText(String.valueOf(ySpeed));
                            gameView.getxAccelerationField().setText(String.valueOf(xAcc));
                            gameView.getyAccelerationField().setText(String.valueOf(yAcc));
                        }
                        if(gameType==0) {
                            winnerPlayerSolo();
                        }else{
                            winnerPlayerPVP();
                        }
                        /**Check if all balls are not moving to display the poolCue and update the database*/
                        if (!ballController.isMoving) { /**methods when all balls have stopped moving*/
                            waitingForInput = true;
                            if (!poolCueController.isEnablePoolCueControl()) {
                                updatePoolCuePosition();
                                if(!firstPlay) turns();
                                if (!BallConfigurationDB.hasBeenCalled&&!aiController.isAITraining()) {
                                    BallConfigurationDB.updateLastPosition(gameType, currentPlayer.getUsername());
                                    BallConfigurationDB.hasBeenCalled = true;
                                }
                                poolCueController.enablePoolCueControl();
                                scored = false;
                            }
                        } else {
                            gView.displayPoolCue(false);
                            poolCueController.disablePoolCueControl();
                        }
                        ballInHole();
                        ballController.isMoving=false;
                }
            }
        };

    }

    /**
     * Calls prepareGame method in BallController to instantiate the BallModels and BallViews
     * Then starts the gameLoopTimer
     */
    public void startGame(int gameType, PlayerModel player1, PlayerModel player2) {
        gameLoopTimer.start();
        ballController.prepareGame(this.gameView,this.gameView.getTableView());
        gameView.clearBallViewDebug();
        gameView.ballViewDataDebug();
        this.gameType = gameType;
        if(this.gameType==0){
            // SOLO
            p1 = player1;
            p1.setBallNeededIn((ArrayList<BallModel>) BallController.bModelList.clone());
            p1.getBallNeededIn().remove(BallController.eightBallModel);
            p1.getBallNeededIn().remove(BallController.whiteBallModel);

            p2 = new PlayerModel("NOT AVAILABLE",false);
            p2.setBallNeededIn((ArrayList<BallModel>) BallController.bModelList.clone());
            p2.getBallNeededIn().remove(BallController.eightBallModel);
            p2.getBallNeededIn().remove(BallController.whiteBallModel);

            tableController.getTableView().getPlayersScore().setText("- : -");
        }else if(this.gameType==1) {
            // Instead get the selected player from the combobox
            p1 = player1;
            p2 = player2;
            p1.setTurn(true);
            p2.setTurn(false);
            p1.setBallNeededIn((ArrayList<BallModel>) BallController.bModelList.clone());
            p2.setBallNeededIn((ArrayList<BallModel>) BallController.bModelList.clone());
            p1.getBallNeededIn().remove(BallController.eightBallModel);
            p1.getBallNeededIn().remove(BallController.whiteBallModel);
            p2.getBallNeededIn().remove(BallController.eightBallModel);
            p2.getBallNeededIn().remove(BallController.whiteBallModel);
        }


        currentPlayer = p1;
        poolCueController.getCueView().getCue().setImage(ResourcesLoader.poolCueImages.get(currentPlayer.getSelectedPoolCue()-1));

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
        foul=false;
        waitingForInput=false;
        firstPlay=true;
        setBallType=false;
        gameView.clearBallViewDebug();
        if(!keepScore){
            p1.setScore(0);
            p2.setScore(0);
            tableController.getTableView().getPlayersScore().setText("0 : 0");
        }
    }

    public void turns(){
        ballController.makeUnDraggable();

        if(!firstPlay) {
            if (!ballController.isCollide) {
                System.out.println("No collide");
                foul = true;
            }
        }
        checkFoul();


        if(gameType==0) {
            foul=false;
            return;
        }

        if(gameType==1) {
            winnerPlayerPVP();
            if (!scored || foul) {
                setCurrentPlayer();
                ballController.isCollide=false;
                //System.out.println("switch");
            }
        }

        //setting the pool cue on each turn
        poolCueController.getCueView().getCue().setImage(ResourcesLoader.poolCueImages.get(currentPlayer.getSelectedPoolCue()-1));

        System.out.println(currentPlayer.getBallType());
        foul=false;
        ballController.setFirstCollide(null);
        bModelInEachTurn.clear();
        System.out.println(currentPlayer.getUsername() + "," + "your turn!");
        waitingForInput=true;
        counter++;

    }

    public void assignBallType(){

        if(counter > 0 && !setBallType){
            for(int i = 0;i<BallController.solidFull.size();i++){
                if(bModelInEachTurn.contains(BallController.solidFull.get(i))){
                    currentPlayer.getBallNeededIn().removeAll(bModelInEachTurn);
                    currentPlayer.getBallNeededIn().removeAll(BallController.stripeBModelList);
                    getNextPlayer().getBallNeededIn().removeAll(bModelInEachTurn);
                    getNextPlayer().getBallNeededIn().removeAll(BallController.solidBModelList);
                    currentPlayer.setBallType(0);
                    getNextPlayer().setBallType(1);
                    setBallType = true;
                    scored=true;
                    System.out.println(currentPlayer.getUsername() + ": "+currentPlayer.getBallType());
                    System.out.println(getNextPlayer().getUsername() + ": "+getNextPlayer().getBallType());
                }
            }
            for(int i = 0;i<BallController.stripFull.size();i++){
                if(bModelInEachTurn.contains(BallController.stripFull.get(i))){
                    getNextPlayer().getBallNeededIn().removeAll(bModelInEachTurn);
                    getNextPlayer().getBallNeededIn().removeAll(BallController.stripeBModelList);

                    currentPlayer.getBallNeededIn().removeAll(bModelInEachTurn);
                    currentPlayer.getBallNeededIn().removeAll(BallController.solidBModelList);
                    currentPlayer.setBallType(1);
                    getNextPlayer().setBallType(0);
                    setBallType = true;
                    scored=true;

                    System.out.println(currentPlayer.getUsername() + ": "+currentPlayer.getBallType());
                    System.out.println(getNextPlayer().getUsername() + ": "+getNextPlayer().getBallType());
                }
            }
            BallConfigurationDB.assignBallType(this.gameType,p1.getBallType(),p2.getBallType());
        }
    }
    public void checkFoul(){
        if(foul){
            System.out.println("Foul");
            ballController.makeDraggable();
        }
    }

    private void setCurrentPlayer(){
        currentPlayer.setTurn(false);
        currentPlayer = getNextPlayer();
        currentPlayer.setTurn(true);
    }
    private PlayerModel getNextPlayer(){
        if(currentPlayer.equals(p1)) return p2;
        return p1;
    }
    public void whiteBallIn(BallView ballView){
        BallModel bModel = BallController.getBallModelFromBallView(ballView);
        if(bModel.getNumber()==16){
            this.foul = true;
            bModel.setPositionX(new BigDecimal(tableController.getTableView().getFullTable().getWidth()/2));
            bModel.setPositionY(new BigDecimal(tableController.getTableView().getFullTable().getHeight()/2));
            bModel.setVelocityX(new BigDecimal(0.1));
            bModel.setVelocityY(new BigDecimal(0.1));
            System.out.println("whiteBall");
        }else{
            //bModelIn.add(bModel);
            bModelInEachTurn.add(bModel);
        }

    }

    public void eightBallInIllegal(int gameType){
        if(bModelInEachTurn.contains(BallController.eightBallModel)) {
            if (!gameView.getPopupWindow().isShowing()){
                getNextPlayer().setScore(getNextPlayer().getScore() + 1);
            }
            gameView.getPopupMessage().setText(currentPlayer + " lose!");
            if (!(gameType == 0)) tableController.getTableView().getPlayersScore().setText(p1.getScore() + " : " + p2.getScore());
            gameView.getPopupWindow().show();
        }
    }
    public void eightBallInLegal(int gameType){
        if(bModelInEachTurn.contains(BallController.eightBallModel)) {
            if (!gameView.getPopupWindow().isShowing()){
                currentPlayer.setScore(currentPlayer.getScore() + 1);
            }
            gameView.getPopupMessage().setText(currentPlayer + " win!");
            if (!(gameType == 0)) tableController.getTableView().getPlayersScore().setText(p1.getScore() + " : " + p2.getScore());
            gameView.getPopupWindow().show();
        }
    }
    public void firstCollidePlay(){
        if(ballController.getFirstCollide() != null && setBallType) {
            if (currentPlayer.isTurn() && !(ballController.getFirstCollide().getBallType() == currentPlayer.getBallType())) {
                foul = true;
            }
        }

    }

    public void ballInHole(){
        if(!bModelInEachTurn.isEmpty()) {
            for (BallModel b : bModelInEachTurn) {
                ballController.ballInHole(b, gameView);
                scored = true;
                if(gameType == 1) {
                    if (p1.getBallNeededIn().contains(b)) {
                        p1.getBallNeededIn().remove(b);

                    }
                    if (p2.getBallNeededIn().contains(b)) {
                        p2.getBallNeededIn().remove(b);
                    }
                }
            }
        }
    }



    public void winnerPlayerSolo(){
        if(currentPlayer.getBallNeededIn().isEmpty()){
            currentPlayer.getBallNeededIn().add(BallController.eightBallModel);
        }
        if(BallController.getAllInSolid() && BallController.getAllInStripe()){
            eightBallInLegal(0);
        }else{
            eightBallInIllegal(0);
        }
    }
    public void winnerPlayerPVP(){
        //System.out.println(currentPlayer+" : "+currentPlayer.getBallNeededIn());
        if(currentPlayer.getBallNeededIn().isEmpty()){
            currentPlayer.getBallNeededIn().add(BallController.eightBallModel);
        }
//        System.out.println(currentPlayer+" : "+currentPlayer.getBallNeededIn());
//        System.out.println(currentPlayer+" eight ball needed: "+currentPlayer.getBallNeededIn().contains(BallController.eightBallModel));
        if(currentPlayer.isTurn() && currentPlayer.getBallNeededIn().contains(BallController.eightBallModel)){
            eightBallInLegal(1);
        }else{
            eightBallInIllegal(1);
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
     * @return Table Controller
     */
    public TableController getTableController() {
        return tableController;
    }

    /**
     * Gets the Ball Controller
     * @return Ball Controller
     */
    public BallController getBallController() {
        return ballController;
    }

    /**
     * Gets the Pool Cue Controller
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

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public boolean isFirstPlay() {
        return firstPlay;
    }

    public boolean isScored() {
        return scored;
    }

    public boolean isFoul() {
        return foul;
    }

    public void setFoul(boolean foul) {
        this.foul = foul;
    }

    public ArrayList<BallModel> getbModelInEachTurn() {
        return bModelInEachTurn;
    }

//    public void simulatePlay(AIModel aiModel) {
//        poolCueController.setPoolCue(aiModel.getPower(),aiModel.getRotation());
//    }

    public void resetSimulation() {
        BallConfigurationDB.loadLastPosition(gameType);
    }

    public void updatePoolCuePosition() {
        gameView.displayPoolCue(true);
        poolCueController.poolCueView.getCue().setX(BallController.whiteBallModel.getPositionX().doubleValue() + (BallModel.RADIUS));
        poolCueController.poolCueView.getCue().setY(BallController.whiteBallModel.getPositionY().doubleValue() - (poolCueController.poolCueView.getCue().getImage().getHeight() / 5.));
        poolCueController.getRotate().setPivotX(BallController.whiteBallModel.getPositionX().doubleValue() );
        poolCueController.getRotate().setPivotY(BallController.whiteBallModel.getPositionY().doubleValue());
        poolCueController.getRotate().setAngle(poolCueController.getCueView().getPreviousAngle());
        if(settingsController.getCueHelper()==1) {
            poolCueController.poolCueView.getPoolLine().setVisible(true);
            poolCueController.poolCueView.getPoolLine().setStartX(BallController.whiteBallModel.getPositionX().doubleValue());
            poolCueController.poolCueView.getPoolLine().setStartY(BallController.whiteBallModel.getPositionY().doubleValue());
        }else{
            poolCueController.poolCueView.getPoolLine().setVisible(false);
        }
    }

    public void simulatePlay(AIModel bestOpponent) {
        poolCueController.setPoolCue(bestOpponent.getPower(),bestOpponent.getRotation());
    }

    public void setWaitingForInput(boolean status) {
        waitingForInput=status;
    }

    public void setFirstPlay(boolean b) {
        firstPlay=b;
    }
//    public ArrayList<Double> getBallsPositionX() {
//        return ballsPositionX;
//    }
//
//    public ArrayList<Double> getBallsPositionY() {
//        return ballsPositionY;
//    }
}

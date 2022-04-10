package io.pool.controller;

import io.pool.AI.AIModel;
import io.pool.Database.BallConfigurationDB;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;
import io.pool.view.BallView;
import io.pool.view.GameView;
import io.pool.model.GameModel;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class GameController {
    /** Instance of GameView that contains all the Ball,Table and Pool Cue Views*/
    private GameView gameView;
    /** Table Controller */
    private TableController tableController;
    /** Ball Controller */
    private BallController ballController;
    /** Pool Cue Controller */
    private PoolCueController poolCueController;
    /** Animation Timer that helps to update the View every frame */
    public static GameLoopTimer gameLoopTimer;
    /** GameModel that helps keep track of game status*/
    private GameModel gameModel;
    private PlayerModel playerModel;
    private SettingsController settingsController;


    private PlayerModel p1=null;
    private PlayerModel p2=null;
    private PlayerModel currentPlayer;
    public static boolean foul=false;
    private boolean firstPlay=true,setBallType=false;
    private boolean scored = false;
    public static boolean waitingForInput=true;
    private int gameType;

//    private ArrayList<BallModel> bModelIn = new ArrayList<>();
    private ArrayList<BallModel> bModelInEachTurn = new ArrayList<>();

//    private ArrayList<Double> ballsPositionX;
//    private ArrayList<Double> ballsPositionY;
//    private ArrayList<Double> ballsSpeed;

    /**
     * Main Constructor of GameController
     * @param gView Instance of GameView
     */
    public GameController(GameView gView, SettingsController settingsController) {
        /** Assign the GameView and Instantiate the Controllers */
        gameModel = new GameModel();
        playerModel = new PlayerModel();
        this.gameView = gView;
        tableController = new TableController(this.gameView.getTableView(),this);
        ballController = new BallController(this);
        poolCueController = new PoolCueController(this.gameView.getCueView(), this);
        this.settingsController = settingsController;

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
                        assignBallType();
                        tableController.turnView(gameController);
                        ballController.detectCollision(tableController);
                        if (gameView.getClickedBallNumber() > 0) {
                            gameView.getxPositionField().setText(String.valueOf(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getPositionX().doubleValue()));
                            gameView.getyPositionField().setText(String.valueOf(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getPositionY().doubleValue()));
                            gameView.getxSpeedField().setText(String.valueOf(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getVelocityX().doubleValue()));
                            gameView.getySpeedField().setText(String.valueOf(BallController.bModelList.get(gameView.getClickedBallNumber() - 1).getVelocityY().doubleValue()));
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
                                turns();
                                if (!BallConfigurationDB.hasBeenCalled) {
                                    BallConfigurationDB.updateLastPosition(playerModel.getBallType(), playerModel.getBallType(), gameModel.getPlayerTurn());
                                    BallConfigurationDB.hasBeenCalled = true;
                                }
                                poolCueController.enablePoolCueControl();
                                scored = false;
                            }
                        } else {
                            firstPlay = false;
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
    public void startGame(int gameType) {
        ballController.prepareGame(this.gameView,this.gameView.getTableView());
        this.gameType = gameType;
        if(this.gameType==0){
            // SOLO
            p1 = new PlayerModel("ABC",true);
            p1.setBallNeededIn((ArrayList<BallModel>) BallController.bModelList.clone());
            p1.getBallNeededIn().remove(BallController.eightBallModel);
            p1.getBallNeededIn().remove(BallController.whiteBallModel);
        }else if(this.gameType==1) {
            // Instead get the selected player from the combobox
            p1 = new PlayerModel("ABC",false);
            p2 = new PlayerModel("XYZ",true);
            p1.setBallNeededIn((ArrayList<BallModel>) BallController.bModelList.clone());
            p2.setBallNeededIn((ArrayList<BallModel>) BallController.bModelList.clone());
            p1.getBallNeededIn().remove(BallController.eightBallModel);
            p1.getBallNeededIn().remove(BallController.whiteBallModel);
            p2.getBallNeededIn().remove(BallController.eightBallModel);
            p2.getBallNeededIn().remove(BallController.whiteBallModel);
        }
        currentPlayer=p1;

        //ballController.testingBallController(this.gameView);
        gameLoopTimer.start();
    }
    /**
     * Stops the gameLoopTimer
     * Destroys all the instances of BallModels and BallViews by calling
     * their respective method from BallController
     */
    public void resetGame() {
        //bModelIn.clear();
        bModelInEachTurn.clear();
        gameLoopTimer.stop();
        ballController.destroyViews(this.gameView);
        ballController.destroyModels();
        foul=false;
        waitingForInput=false;
        firstPlay=true;
        setBallType=false;
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
            //winnerPlayerPVP();
            if (!scored || foul) {
                setCurrentPlayer();
                ballController.isCollide=false;
                //System.out.println("switch");
            }
        }
        System.out.println(currentPlayer.getBallType());
        foul=false;
        ballController.setFirstCollide(null);
        bModelInEachTurn.clear();
        System.out.println(currentPlayer.getUsername() + "," + "your turn!");
        waitingForInput=true;

    }

    public void assignBallType(){

        if(!firstPlay && !setBallType){
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
                    System.out.println(currentPlayer.getUsername() + ": solid");
                    System.out.println(getNextPlayer().getUsername() + ": stripe");
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

                    System.out.println(currentPlayer.getUsername() + ": stripe");
                    System.out.println(getNextPlayer().getUsername() + ": solid");
                }
            }
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
        if(currentPlayer.equals(p1)){
            return p2;
        }
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

    public void eightBallInIllegal(){
        if(bModelInEachTurn.contains(BallController.eightBallModel)) {
            System.out.println(currentPlayer + " got the 8 ball in, "+currentPlayer+" lose!");
            resetGame();
        }
    }
    public void eightBallInLegal(){
        if(bModelInEachTurn.contains(BallController.eightBallModel)) {
            System.out.println(currentPlayer + " got the 8 ball in, "+currentPlayer+" win!");
            resetGame();
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
        if(BallController.getAllInSolid() && BallController.getAllInStripe()){
            eightBallInLegal();
        }else{
            eightBallInIllegal();
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
            eightBallInLegal();
        }else{
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

    public static boolean isFoul() {
        return foul;
    }

    public ArrayList<BallModel> getbModelInEachTurn() {
        return bModelInEachTurn;
    }

//    public void simulatePlay(AIModel aiModel) {
//        poolCueController.setPoolCue(aiModel.getPower(),aiModel.getRotation());
//    }

    public void resetSimulation() {
        //TODO Call method from DBController to load BallConfiguration
    }

    public void updatePoolCuePosition() {
        gameView.displayPoolCue(true);
        poolCueController.poolCueView.getCue().setX(BallController.whiteBallModel.getPositionX().doubleValue() + (BallModel.RADIUS));
        poolCueController.poolCueView.getCue().setY(BallController.whiteBallModel.getPositionY().doubleValue() - (poolCueController.poolCueView.getCue().getImage().getHeight() / 2));
        poolCueController.getRotate().setPivotX(BallController.whiteBallModel.getPositionX().doubleValue());
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
//    public ArrayList<Double> getBallsPositionX() {
//        return ballsPositionX;
//    }
//
//    public ArrayList<Double> getBallsPositionY() {
//        return ballsPositionY;
//    }
}

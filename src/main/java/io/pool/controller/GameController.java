package io.pool.controller;

import io.pool.Database.DBConnection;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;
import io.pool.view.BallView;
import io.pool.view.GameView;
import io.pool.model.GameModel;

import java.math.BigDecimal;
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

    private PlayerModel p1=null;
    private PlayerModel p2=null;
    private PlayerModel currentPlayer;
    public static boolean foul=false;
    private boolean firstPlay=true,setBallType=false;
    public static boolean waitingForInput=false;

    private ArrayList<BallModel> bModelIn = new ArrayList<>();
    private ArrayList<BallModel> bModelInEachTurn = new ArrayList<>();

    private ArrayList<Double> ballsPositionX;
    private ArrayList<Double> ballsPositionY;
    private ArrayList<Double> ballsSpeed;

    /**
     * Main Constructor of GameController
     * @param gView Instance of GameView
     */
    public GameController(GameView gView) {
        /** Assign the GameView and Instantiate the Controllers */
        gameModel = new GameModel();
        playerModel = new PlayerModel();
        this.gameView = gView;
        tableController = new TableController(this.gameView.getTableView());
        ballController = new BallController(this);
        poolCueController = new PoolCueController(this.gameView.getCueView());

        ballsPositionX = new ArrayList<>();
        ballsPositionY = new ArrayList<>();
        ballsSpeed = new ArrayList<>();

        /** Instantiate the gameLoopTimer and Override the tick Method */
        gameLoopTimer = new GameLoopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {
                /** The time between each frame should be less than 1 second
                 * Any value bigger than 1 second is incorrect
                 * */
                if(secondsSinceLastFrame<1){
                    /** Detect collisions */
                    ballController.detectCollision();
                    /** Check if ball gets inside any of the holes */
                    boolean moving=false;
                    BallModel bModel;
                    for (BallView ballView : ballController.ballViewArrayList()) {
                        bModel = BallController.getBallModelFromBallView(ballView);
                        if(!moving){
                            moving=bModel.isMoving;
                        }
                        bModel.updatePosition();
                        BallController.updateBallViewPosition(bModel);
                        if(tableController.checkBallInHole(ballView)) {
                            whiteBallIn(ballView);
                        }
                    }
                    if (gameView.getClickedBallNumber() > 0) {
                        gameView.getxPositionField().setText(String.valueOf(BallController.bModelList.get(gameView.getClickedBallNumber()-1).getPositionX().doubleValue()));
                        gameView.getyPositionField().setText(String.valueOf(BallController.bModelList.get(gameView.getClickedBallNumber()-1).getPositionY().doubleValue()));
                        gameView.getxSpeedField().setText(String.valueOf(BallController.bModelList.get(gameView.getClickedBallNumber()-1).getVelocityX().doubleValue()));
                        gameView.getySpeedField().setText(String.valueOf(BallController.bModelList.get(gameView.getClickedBallNumber()-1).getVelocityY().doubleValue()));
                    }
                    winnerPlayerSolo();
                    /**Check if all balls are not moving to display the poolCue and update the database*/
                    if(!moving) { /**methods when all balls have stopped moving*/
                        waitingForInput = true;
                        if (!poolCueController.isEnablePoolCueControl()){
                            poolCueController.poolCueView.getCue().setX(BallController.whiteBallModel.getPositionX().doubleValue() + (BallModel.RADIUS));
                            poolCueController.poolCueView.getCue().setY(BallController.whiteBallModel.getPositionY().doubleValue() - (poolCueController.poolCueView.getCue().getImage().getHeight() / 2));
                            poolCueController.getRotate().setPivotX(BallController.whiteBallModel.getPositionX().doubleValue());
                            poolCueController.getRotate().setPivotY(BallController.whiteBallModel.getPositionY().doubleValue());
                            poolCueController.poolCueView.getPoolLine().setStartX(BallController.whiteBallModel.getPositionX().doubleValue());
                            poolCueController.poolCueView.getPoolLine().setStartY(BallController.whiteBallModel.getPositionY().doubleValue());
                        }

                        poolCueController.enablePoolCueControl();
                        gView.displayPoolCue(true);
                        if(!DBConnection.hasBeenCalled) {
                            //DBConnection.updateLastPosition(playerModel.getBallType(),playerModel.getBallType(), gameModel.getPlayerTurn());
                            DBConnection.hasBeenCalled = true;
                        }
                    }else{
                        gView.displayPoolCue(false);
                        poolCueController.disablePoolCueControl();
                    }
                    if(!waitingForInput) {
                        turns();
                    }
                    ballInHole();
                }
            }
        };

    }

    /**
     * Calls prepareGame method in BallController to instantiate the BallModels and BallViews
     * Then starts the gameLoopTimer
     */
    public void startGame(int gameType) {
        ballController.prepareGame(this.gameView);

        if(gameType==0){
            // SOLO
            p1 = new PlayerModel("ABC");
            p1.setBallNeededIn((ArrayList<BallModel>) BallController.bModelList.clone());
            p1.getBallNeededIn().remove(BallController.eightBallModel);
            p1.getBallNeededIn().remove(BallController.whiteBallModel);
        }else if(gameType==1) {
            // Instead get the selected player from the combobox
            p1 = new PlayerModel("ABC");
            p2 = new PlayerModel("XYZ");
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
        bModelIn.clear();
        bModelInEachTurn.clear();
        gameLoopTimer.stop();
        ballController.destroyViews(this.gameView);
        ballController.destroyModels();
        foul=false;
        waitingForInput=false;
        firstPlay=true;
        setBallType=false;
        System.out.println("Reset");
    }

    public void turns(){
        allSolidBallsIn();
        allStripeBallsIn();

        if(p2==null) {
            winnerPlayerSolo();
            foul=false;
            return;
        }

        if(bModelInEachTurn.isEmpty()){
            setCurrentPlayer();
            if(foul){
                //ballController.makeDraggable();
            }
        }else{
            if(foul){
                setCurrentPlayer();
                //ballController.makeDraggable();
            }else{
                firstPlay = false;
            }
        }

        if(!firstPlay && !setBallType){
            for(int i = 0;i<BallController.getSolidBModelList().size();i++){
                if(bModelInEachTurn.contains(BallController.getSolidBModelList().get(i))){
                    currentPlayer.getBallNeededIn().removeAll(bModelInEachTurn);
                    currentPlayer.getBallNeededIn().removeAll(BallController.stripeBModelList);

                    getNextPlayer().getBallNeededIn().removeAll(bModelInEachTurn);
                    getNextPlayer().getBallNeededIn().removeAll(BallController.solidBModelList);

                    setBallType = true;
                    System.out.println(currentPlayer.getUsername() + ": solid");
                    System.out.println(getNextPlayer().getUsername() + ": stripe");
                }
            }
            for(int i = 0;i<BallController.getStripeBModelList().size();i++){
                if(bModelInEachTurn.contains(BallController.getStripeBModelList().get(i))){
                    getNextPlayer().getBallNeededIn().removeAll(bModelInEachTurn);
                    getNextPlayer().getBallNeededIn().removeAll(BallController.stripeBModelList);

                    currentPlayer.getBallNeededIn().removeAll(bModelInEachTurn);
                    currentPlayer.getBallNeededIn().removeAll(BallController.solidBModelList);
                    setBallType = true;
                    System.out.println(currentPlayer.getUsername() + ": stripe");
                    System.out.println(getNextPlayer().getUsername() + ": solid");
                }
            }
        }
        foul=false;
        bModelInEachTurn.clear();
        System.out.println(currentPlayer.getUsername() + "," + "your turn!");
        waitingForInput=true;
    }


    private void setCurrentPlayer(){
        currentPlayer = getNextPlayer();
    }
    private PlayerModel getNextPlayer(){
        if(currentPlayer.equals(p1)){
            return p2;
        }
        return p1;
    }
    public void whiteBallIn(BallView ballView){
        BallModel bModel = ballController.ballModelArrayList().get(ballController.ballViewArrayList().indexOf(ballView));
        if(bModel.getNumber()==16){
            this.foul = true;
            bModel.setPositionX(new BigDecimal(tableController.getTableView().getFullTable().getWidth()/2));
            bModel.setPositionY(new BigDecimal(tableController.getTableView().getFullTable().getHeight()/2));
            bModel.setVelocityX(new BigDecimal(0.1));
            bModel.setVelocityY(new BigDecimal(0.1));
        }else{
            bModelIn.add(bModel);
            bModelInEachTurn.add(bModel);
        }
        if(foul){
            ballController.makeDraggable();
        }
    }

    public void eightBallInIllegal(){
        if(bModelIn.contains(BallController.eightBallModel)) {
            System.out.println("You got the 8 ball in, You lose!");
            resetGame();
        }
    }
    public void eightBallInLegal(){
        if(bModelIn.contains(BallController.eightBallModel)) {
            System.out.println("You got the 8 ball in, You win!");
            resetGame();
        }
    }

    public void allSolidBallsIn(){
        for(int i = 0; i < BallController.getSolidBModelList().size() ;i++){
            if(bModelIn.contains(BallController.getSolidBModelList().get(i))){
                BallController.getSolidBModelList().remove(i);
                BallController.getSolidBViewList().remove(i);
            }
            if(BallController.getSolidBModelList().isEmpty()){
                System.out.println("All in for Solid");

                BallController.setAllInSolid(true);
                break;
            }
        }
    }
    public boolean allStripeBallsIn(){
        for(int i = 0; i < BallController.getStripeBModelList().size() ;i++){
            if(bModelIn.contains(BallController.getStripeBModelList().get(i))){
                BallController.getStripeBModelList().remove(i);
                BallController.getStripeBViewList().remove(i);
            }
            if(BallController.getStripeBModelList().isEmpty()){
                System.out.println("All in for Stripe");

                BallController.setAllInStripe(true);
                break;
            }
        }
        return BallController.allInStripe;
    }

    public void ballInHole(){
        for (BallModel b:bModelIn) {
            ballController.ballInHole(b, gameView);
        }
    }

    public void winnerPlayerSolo(){
        if(BallController.getAllInSolid() && BallController.getAllInStripe()){
            eightBallInLegal();
        }else{
            eightBallInIllegal();
        }

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

    public ArrayList<Double> getBallsPositionX() {
        return ballsPositionX;
    }

    public ArrayList<Double> getBallsPositionY() {
        return ballsPositionY;
    }
}

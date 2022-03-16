package io.pool.controller;

import io.pool.Database.DBConnection;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;
import io.pool.view.BallView;
import io.pool.view.GameView;

import java.math.BigDecimal;
import java.net.MalformedURLException;
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
    private GameLoopTimer gameLoopTimer;

    private PlayerModel p1;

    private PlayerModel p2;

    private ArrayList<BallModel> bModelIn = new ArrayList<>();


    /**
     * Main Constructor of GameController
     * @param gView Instance of GameView
     */
    public GameController(GameView gView) {
        /** Assign the GameView and Instantiate the Controllers */
        this.gameView = gView;
        tableController = new TableController(this.gameView.getTableView());
        ballController = new BallController();
        poolCueController = new PoolCueController(this.gameView.getCueView());
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
                    for (BallView ballView : ballController.ballViewArrayList()) {
                        for (int i = 0; i < gView.getTableView().getHoles().size(); i++) {
                            if(tableController.checkInterBallsHoles(ballView, i)) {
                                whiteBallIn(ballView);
                                //eightBallIn(ballView);
//                                FadeTransition gettingInTheHole = new FadeTransition();
//                                gettingInTheHole.setDuration(Duration.seconds(5));
//                                gettingInTheHole.setNode(ballView.getBall());
//                                gettingInTheHole.setFromValue(1.0);
//                                gettingInTheHole.setToValue(0.0);
//                                gettingInTheHole.setOnFinished(event -> {
//                                    gameView.getChildren().remove(ballView.getBall());
//                                });
//                                gettingInTheHole.play();
                            }
                        }
                    }
                    /**Check if all balls are not moving to display the poolCue and update the database*/
                    boolean moving=false;
                    for(BallModel bModel : ballController.ballModelArrayList()){
                        moving = bModel.isMoving;
                        if(moving) break;
                    }
                    if(!moving){ /**methods when all balls have stopped moving*/
                        poolCueController.enablePoolCueControl();
                        gView.displayPoolCue(true);
                        if(!DBConnection.hasBeenCalled) {
                            DBConnection.updateLastPosition();
                            DBConnection.hasBeenCalled = true;
                        }
                    }else{
                        gView.displayPoolCue(false);
                    }
                    ballInHole();
                    allSolidBallsIn();
                    allStripeBallsIn();

                }
            }
        };

    }

    /**
     * Calls prepareGame method in BallController to instantiate the BallModels and BallViews
     * Then starts the gameLoopTimer
     * @throws MalformedURLException if the path to the ball images is incorrect
     */
    public void startGame() throws MalformedURLException {
        ballController.prepareGame(this.gameView);
        System.out.println("START");
        //ballController.testingBallController(this.gameView);
        gameLoopTimer.start();
    }

    /**
     * Stops the gameLoopTimer
     * Destroys all the instances of BallModels and BallViews by calling
     * their respective method from BallController
     */
    public void resetGame() {
        gameLoopTimer.stop();
        ballController.destroyViews(this.gameView);
        ballController.destroyModels();
        System.out.println("Reset");
    }

    public void turns(PlayerModel p1){
        System.out.println(p1.getUsername() + "," + "your turn!");
    }

    public void whiteBallIn(BallView ballView){
        BallModel bModel = ballController.ballModelArrayList().get(ballController.ballViewArrayList().indexOf(ballView));
        if(bModel.getNumber()==16){
            bModel.setPositionX(new BigDecimal(tableController.getTableView().getFullTable().getWidth()/2));
            bModel.setPositionY(new BigDecimal(tableController.getTableView().getFullTable().getHeight()/2));
            bModel.setVelocityX(new BigDecimal(0.1));
            bModel.setVelocityY(new BigDecimal(0.1));
        }else{
            bModelIn.add(bModel);
        }
    }

    public void eightBallIn(BallView ballView){
        BallModel bModel = ballController.ballModelArrayList().get(ballController.ballViewArrayList().indexOf(ballView));
        if(bModel.getNumber() == 8){
            System.out.println("You got the 8 ball in, You lose!");
            ballController.destroyModels();
            ballController.destroyViews(this.gameView);
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
                break;
            }
        }
    }
    public void allStripeBallsIn(){
        for(int i = 0; i < BallController.getStripeBModelList().size() ;i++){
            if(bModelIn.contains(BallController.getStripeBModelList().get(i))){
                BallController.getStripeBModelList().remove(i);
                BallController.getStripeBViewList().remove(i);
            }
            if(BallController.getStripeBModelList().isEmpty()){
                System.out.println("All in for Stripe");
                break;
            }
        }
    }

    public void ballInHole(){
        for (BallModel b:bModelIn) {
        //     System.out.println(b.getNumber());
            ballController.ballInHole(b, gameView);

        }
        //System.out.println();
    }

    public void winnerPlayer(){
        if(bModelIn.contains(ballController.ballModelArrayList().get(7))){

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

}

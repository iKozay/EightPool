package io.pool.controller;

import io.pool.Database.DBConnection;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;
import io.pool.view.BallView;
import io.pool.view.GameView;

import java.math.BigDecimal;
import java.net.MalformedURLException;
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
                                BallModel bModel = ballController.ballModelArrayList().get(ballController.ballViewArrayList().indexOf(ballView));
                                if(bModel.getNumber()==16){
                                    bModel.setPositionX(new BigDecimal(500));
                                    bModel.setPositionY(new BigDecimal(500));
                                }else{
                                    bModelIn.add(bModel);
                                }
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
                        gView.getCueView().getCue().setX(BallController.whiteBallModel.getPositionX().doubleValue()+(BallModel.RADIUS));
                        gView.getCueView().getCue().setY(BallController.whiteBallModel.getPositionY().doubleValue()-(gView.getCueView().getCue().getHeight()/2));
                        gView.displayPoolCue(true);

                        if(!DBConnection.hasBeenCalled) {
                            DBConnection.createNewSavedPosition();
                            DBConnection.hasBeenCalled = true;
                        }
                    }else{
                        gView.displayPoolCue(false);
                    }

                    ballInHole();
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
    }

    public void turns(PlayerModel p1){
        System.out.println(p1.getUsername() + "," + "your turn!");

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

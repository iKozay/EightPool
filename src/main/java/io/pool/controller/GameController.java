package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.view.BallView;
import io.pool.view.GameView;

import java.net.MalformedURLException;

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
                                //TODO Disabled ball in hole.
                                //ballView.getBall().setRadius(ballView.getBall().getRadius() - 0.3);
                            }
                        }
                    }
                    /**Check if all balls are not moving to display the poolCue and update the database*/
                    boolean moving=false;
                    for(BallModel bModel : ballController.ballModelArrayList()){
                        moving = bModel.isMoving;
                        if(moving) break;
                    }
                    if(!moving){
                        gView.getCueView().getCue().setLayoutX(BallController.bModelList.get(15).getPositionX().doubleValue());
                        gView.getCueView().getCue().setLayoutY(BallController.bModelList.get(15).getPositionY().doubleValue());
                        gView.displayPoolCue(true);
                    }else{
                        gView.displayPoolCue(false);
                    }
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

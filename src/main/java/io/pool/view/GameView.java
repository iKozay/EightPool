package io.pool.view;

import io.pool.controller.GameController;
import javafx.scene.layout.Pane;
import java.net.MalformedURLException;

public class GameView extends Pane {
    /** Instance of GameController that will control the Ball,Table and PoolCue Controllers */
    private GameController gameController;
    /** Table that will be displayed to the user */
    private TableView tableView;
    /** Pool Cue that will be displayed to the user */
    private PoolCueView cueView;

    /**
     * Main Constructor of GameView
     * @throws MalformedURLException if the path to the table image is incorrect
     */
    public GameView() throws MalformedURLException {
        /**
         * Instantiates the Views and GameController
         */
        tableView = new TableView(this);
        cueView = new PoolCueView();
        displayPoolCue(false);
        gameController = new GameController(this);
        this.getChildren().addAll(cueView.getCue());
    }

    /**
     * Gets the GameController
     * @return the GameController
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Gets the Table View
     * @return Table View
     */
    public TableView getTableView() {
        return tableView;
    }

    /**
     * Gets the Pool Cue View
     * @return Pool Cue View
     */
    public PoolCueView getCueView() {
        return cueView;
    }
    public void displayPoolCue(boolean visibility){
        this.cueView.getCue().setVisible(visibility);
    }
}

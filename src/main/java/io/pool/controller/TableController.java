package io.pool.controller;

import io.pool.eightpool.game;
import io.pool.model.TableBorderModel;
import io.pool.view.BallView;
import io.pool.view.TableView;
import javafx.scene.shape.Circle;
import java.util.ArrayList;

public class TableController {

    /** Instance of the table that will be shown to the user */
    private TableView tableView;
    /** ArrayList that contains all the holes */
    private ArrayList<Circle> holes;

    /** tableX and tableY */
    private int tableX = game.eightPoolTableX;
    private int tableY = game.eightPoolTableY;


    /**
     * Main constructor of TableController
     * @param tableView The Table View
     */
    public TableController(TableView tableView) {
        this.tableView = tableView;
        this.holes = tableView.getHoles();
    }

    /**
     * Gets the Table View
     * @return the Table View
     */
    public TableView getTableView() {
        return tableView;
    }

    /**
     * Checks if the BallView is inside the hole
     * @param ballView the BallView
     * @return <code>true</code> if the ball is inside the hole. <code>false</code> otherwise
     */
    public boolean checkInterBallsHoles(BallView ballView) {
        for(Circle hole:tableView.getHoles()) {
            double centerToCenter = Math.sqrt(Math.pow(ballView.getBall().getLayoutX() - tableX - (hole.getLayoutX()), 2) + Math.pow(ballView.getBall().getLayoutY() - tableY - (hole.getLayoutY()), 2));
            double rs1 = ballView.getBall().getRadius() + hole.getRadius();
            if(centerToCenter < rs1) {
                ballView.getBall().setLayoutX(hole.getCenterX());
                ballView.getBall().setLayoutY(hole.getCenterY());
                return true;
            }
        }
        return false;
    }
}

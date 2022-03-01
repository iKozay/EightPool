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
     * @param holeNum the Hole Number
     * @return <code>true</code> if the ball is inside the hole. <code>false</code> otherwise
     */
    public boolean checkInterBallsHoles(BallView ballView, int holeNum) {
        //TODO Small fix. If PosX and PosY inside hole, then return true
        Circle hole = tableView.getHoles().get(holeNum);
        double centerToCenter = Math.sqrt(Math.pow(ballView.getBall().getLayoutX() - tableX - (hole.getCenterX()), 2) + Math.pow(ballView.getBall().getLayoutY() - tableY - (hole.getCenterY()), 2));

        double rs1 = ballView.getBall().getRadius() + hole.getRadius();
        return centerToCenter <= rs1;
    }
}

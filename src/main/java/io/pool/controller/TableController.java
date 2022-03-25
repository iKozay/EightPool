package io.pool.controller;

import io.pool.eightpool.game;
import io.pool.model.BallModel;
import io.pool.model.TableBorderModel;
import io.pool.view.BallView;
import io.pool.view.TableView;
import javafx.scene.shape.Circle;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TableController {

    /** Instance of the table that will be shown to the user */
    private TableView tableView;

    /** tableX and tableY */
    private double tableX;
    private double tableY;


    /**
     * Main constructor of TableController
     * @param tableView The Table View
     */
    public TableController(TableView tableView) {
        this.tableView = tableView;
        tableX = tableView.getFullTable().getLayoutX()+(TableView.width/43.2);
        tableY = tableView.getFullTable().getLayoutY();
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
    public boolean checkBallInHole(BallView ballView) {
        for(Circle hole:tableView.getHoles()) {
            double xSquared = Math.pow((ballView.getBall().getLayoutX() - tableX - hole.getCenterX()), 2);
            double ySquared = Math.pow((ballView.getBall().getLayoutY() - tableY - hole.getCenterY()), 2);
            double centerToCenter = Math.sqrt(xSquared+ySquared);
            if(centerToCenter < hole.getRadius()) {
                System.out.println("Center2Center: "+centerToCenter+"\tRadius: "+hole.getRadius());
                return true;
            }
        }
        return false;
    }
}

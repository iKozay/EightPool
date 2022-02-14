package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.model.TableModel;
import io.pool.view.BallView;
import io.pool.view.TableView;
import javafx.scene.shape.Circle;
import java.util.ArrayList;

public class TableController {

    private TableView tableView;
    private TableModel tableModel;
    private BallController ballController;
    private ArrayList<Circle> holes;


    public TableController(TableView tableView, TableModel tableModel) {
        this.tableView = tableView;
        this.tableModel = tableModel;

        this.holes = tableView.getHoles();
    }


    public TableView getTableView() {
        return tableView;
    }

    public TableModel getTableModel() {
        return tableModel;
    }


    public void setBallController(BallController ballController) {
        this.ballController = ballController;
    }

    public BallController getBallController() {
        return ballController;
    }

    public double getInitialX(Circle c) {
        return c.getCenterX();
    }

    public double getInitialY(Circle c) {
        return c.getCenterY();
    }

    public boolean checkInterBallsHoles(BallView ballView, int holeNum) {
        Circle hole = tableView.getHoles().get(holeNum);
        double centerToCenter = Math.sqrt(Math.pow(ballView.getBall().getLayoutX() - 300 - (hole.getCenterX()), 2) + Math.pow(ballView.getBall().getLayoutY() - 100 - (hole.getCenterY()), 2));

        double rs1 = ballView.getBall().getRadius() + hole.getRadius();
        return centerToCenter <= rs1;
    }
}

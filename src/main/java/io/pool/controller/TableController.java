package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.model.TableModel;
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
    public void checkIntersection() {

        boolean isIntersecting;
        for (BallModel ballModel: getBallController().ballModelArrayList()) {
            for ( int i = 1; i <= tableView.getHoles().size(); i++) {
                ArrayList<Circle> holes = tableView.getHoles();
//                double centerToCenter = Math.sqrt(Math.pow(ballModel.getBallPositionX() - (c.getLayoutX() + getInitialX(c)), 2) + Math.pow(ballModel.getBallPositionY() - (c.getLayoutY() + getInitialY(c)), 2));

//                double rs1 = BallModel.getRadius() + c.getRadius();
//                isIntersecting = centerToCenter <= rs1;
//                if (isIntersecting) System.out.println("intersection");
            }
        }

    }
}

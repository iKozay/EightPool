package io.pool.controller;

import io.pool.model.BallModel;
import io.pool.model.TableModel;
import io.pool.view.BallView;
import io.pool.view.TableView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class TableController {

    private TableView tableView;
    private TableModel tableModel;
    private BallController ballController;
    private ArrayList<Circle> holes;


    public TableController(TableView view, TableModel model) {
        tableView = view;
        tableModel = model;

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

    public boolean isBallInHole(BallController ballController) {

        ArrayList<BallView> ballViews = ballController.ballViewArrayList();
        for (BallView ballView: ballViews) {
            for (Circle hole : holes) {
//
//                if (intersect.getBoundsInLocal().getWidth()!=-1){
//                    // if they made an intersection, do....
//                }
//
//                while (ballViews.get(i).get) {
//
//                }
            }

        }


        return true;
    }
}

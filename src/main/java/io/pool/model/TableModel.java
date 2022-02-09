package io.pool.model;

import io.pool.view.TableView;
import javafx.scene.shape.Circle;

public class TableModel {

    TableView tableView;
    public TableModel(TableView tableView) {
        this.tableView = tableView;
    }


    /*
    I will give each hole a number
    1               2                  3


    4               5                   6
     */
    public Circle getHole(int holeNum) {
        return tableView.getHoles().get(holeNum-1);
    }
    public double getHoleRadius(int holeNum) {
        return getHole(holeNum).getRadius();
    }
    public double getHolePositionX(int holeNum) {
        return getHole(holeNum).getCenterX();
    }
    public double getHolePositionY(int holeNum) {
        return getHole(holeNum).getCenterY();
    }

}

package io.pool.view;

import io.pool.controller.TableController;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class TableView {
    private TableController tableController;
    private StackPane table;
    public TableView(int width, int height, Paint color) {

        table = new StackPane();
        table.setPrefWidth(width);
        table.setPrefHeight(height);
        table.setLayoutX(300);
        table.setLayoutY(100);

        Rectangle borders = new Rectangle(width, height);
        borders.setArcHeight(30.0d);
        borders.setArcWidth(30.0d);
        borders.setFill(Color.BROWN);


        Rectangle playTable = new Rectangle((width-50), (height-40));
        playTable.setArcHeight(30.0d);
        playTable.setArcWidth(30.0d);
        playTable.setFill(color);

        table.getChildren().addAll(borders, playTable);


        tableController = new TableController();
    }

    public StackPane getTable() {
        return table;
    }
}

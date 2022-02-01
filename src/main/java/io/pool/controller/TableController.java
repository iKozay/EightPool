package io.pool.controller;

import io.pool.model.TableModel;
import io.pool.view.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class TableController {

    private TableModel tableModel;
    private TableView tableView;

    public TableController(Pane root) {
        tableModel = new TableModel(1080, 720, 10, Color.GREEN, 300, 100);
        tableView = new TableView(this, tableModel.getWidth(), tableModel.getHeight(), tableModel.getColor());
        root.getChildren().addAll(tableView.getTable());
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public TableView getTableView() {
        return tableView;
    }
}

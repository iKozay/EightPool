package io.pool.controller;

import io.pool.model.TableModel;
import javafx.scene.paint.Color;

public class TableController {

    private TableModel tableModel;

    public TableController() {
        tableModel = new TableModel(1080, 720, 10, Color.GREEN);
    }

    public TableModel getTableModel() {
        return tableModel;
    }
}

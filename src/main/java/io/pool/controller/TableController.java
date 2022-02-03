package io.pool.controller;

import io.pool.model.TableModel;
import io.pool.view.TableView;

public class TableController {

    private TableView tableView;
    private TableModel tableModel;


    public TableController(TableView view, TableModel model) {
        tableView = view;
        tableModel = model;
    }

    public TableView getTableView() {
        return tableView;
    }

    public TableModel getTableModel() {
        return tableModel;
    }


}

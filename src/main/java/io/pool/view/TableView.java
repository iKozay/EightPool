package io.pool.view;

import io.pool.controller.TableController;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class TableView {
    private TableController tableController;
    private Rectangle table;

    public TableView(TableController tableController, int width, int height, Paint color) {
        this.tableController = tableController;
        table = new Rectangle(width, height);
        table.setX(300);
        table.setY(100);
        table.setFill(color);
    }

    public Rectangle getTable() {
        return table;
    }
}

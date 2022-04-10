module io.pool.eightpool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    requires javafx.media;
    requires com.jfoenix;



    opens io.pool.controller to javafx.fxml;
    opens io.pool.model to javafx.base;
    exports io.pool.eightpool;
    opens io.pool.AI to javafx.fxml;
}
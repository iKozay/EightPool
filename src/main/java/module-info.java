module io.pool.eightpool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    requires javafx.media;


    opens io.pool.controller to javafx.fxml;
    exports io.pool.eightpool;
    opens io.pool.AI to javafx.fxml;
}
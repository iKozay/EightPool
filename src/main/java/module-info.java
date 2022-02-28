module io.pool.eightpool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;


    opens io.pool.controller to javafx.fxml;
    exports io.pool.eightpool;
}
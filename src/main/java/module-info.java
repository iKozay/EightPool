module io.pool.eightpool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens io.pool.controller to javafx.fxml;
    exports io.pool.eightpool;
}
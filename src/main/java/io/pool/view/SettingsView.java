package io.pool.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SettingsView {
    private Parent newLoadedPane;
    public SettingsView() throws IOException {
        newLoadedPane = null;
        URL url = new File("src/main/resources/settings.fxml").toURI().toURL();
        newLoadedPane = FXMLLoader.load(url);
    }

    public Parent getNewLoadedPane() {
        return newLoadedPane;
    }
}

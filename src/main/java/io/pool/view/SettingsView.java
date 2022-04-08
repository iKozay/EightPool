package io.pool.view;

import io.pool.controller.MainMenuController;
import io.pool.controller.SettingsController;
import io.pool.eightpool.game;
import io.pool.model.PlayerModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsView extends BorderPane {
    ObservableList<String> option1List = FXCollections.observableList(new ArrayList<String>(Arrays.asList("Mouse + Keyboard","Keyboard Only")));
    ObservableList<String> option2List = FXCollections.observableList(new ArrayList<String>(Arrays.asList("No","Yes")));
    private SettingsController settingsController;

    public SettingsView() {
        settingsController = new SettingsController(this);

        VBox main = new VBox();
        main.setAlignment(Pos.CENTER);
        this.setCenter(main);

        HBox settings1Box = new HBox();
        settings1Box.setAlignment(Pos.CENTER);
        settings1Box.setPadding(new Insets(0.02*game.eightPoolTableHeight,0.01*game.eightPoolTableWidth,0.02*game.eightPoolTableHeight,0.01*game.eightPoolTableWidth));
        settings1Box.setSpacing(0.02*game.eightPoolTableWidth);

        Text option1Text = new Text("Control Option:");
        ComboBox<String> option1 = new ComboBox<String>(option1List);
        option1.getSelectionModel().select(settingsController.getControlOption());
        option1.setOnAction(e->{
            settingsController.setControlOption(option1.getSelectionModel().getSelectedIndex());
        });

        Text option2Text = new Text("Pool Cue Helper:");
        ComboBox<String> option2 = new ComboBox<String>(option2List);
        option2.getSelectionModel().select(settingsController.getCueHelper());
        option2.setOnAction(e->{
            settingsController.setCueHelper(option2.getSelectionModel().getSelectedIndex());
        });

        Text option3Text = new Text("Friction Percentage: ");
        Slider option3 = new Slider();
        option3.setMin(25);
        option3.setMax(175);
        option3.setValue(settingsController.getFrictionPercentage());
        option3.setBlockIncrement(25);
        option3.setShowTickLabels(true);
        option3.setShowTickMarks(true);
        option3.setMajorTickUnit(25);
        option3.setMinorTickCount(0);
        option3.setSnapToTicks(true);
        option3.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return (object.intValue())+"%";
            }

            @Override
            public Double fromString(String string) {
                return Double.valueOf(string.substring(0,string.length()-1));
            }
        });
        option3.setPrefWidth(0.25*game.eightPoolTableWidth);
        option3.valueProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                settingsController.setFrictionPercentage(newValue.doubleValue());
            }
        });

        Separator sep1 = new Separator();

        settings1Box.getChildren().addAll(option1Text,option1,option2Text,option2,option3Text,option3,sep1);
        main.getChildren().add(settings1Box);


        VBox settings2Box = new VBox();
        settings2Box.setAlignment(Pos.CENTER);
        settings2Box.setPadding(new Insets(0,0.01*game.eightPoolTableWidth,0.02*game.eightPoolTableHeight,0.01*game.eightPoolTableWidth));
        settings2Box.setSpacing(0.01*game.eightPoolTableWidth);





        HBox profileBox = new HBox();
        profileBox.setAlignment(Pos.CENTER);
        profileBox.setPadding(new Insets(0,0,0.02*game.eightPoolTableHeight,0));
        profileBox.setSpacing(0.01*game.eightPoolTableWidth);
        TableView<PlayerModel> profilesTable = new TableView<>();

        TableColumn<PlayerModel, String> tc1 = new TableColumn();
        TableColumn<PlayerModel,Integer> tc2 = new TableColumn();
        TableColumn<PlayerModel, Integer> tc3 = new TableColumn();
        TableColumn<PlayerModel, Integer> tc4 = new TableColumn();
        TableColumn<PlayerModel, Integer> tc5 = new TableColumn();

        tc1.setText("Username");
        tc1.setCellValueFactory(new PropertyValueFactory<>("username"));

        tc2.setText("Selected Pool Cue");
        tc2.setCellValueFactory(new PropertyValueFactory<>("selectedPoolCue"));

        tc3.setText("# Wins");
        tc3.setCellValueFactory(new PropertyValueFactory<>("numberOfWins"));

        tc4.setText("# Losses");
        tc4.setCellValueFactory(new PropertyValueFactory<>("numberOfLoss"));

        tc5.setText("Average Number of Shots");
        tc5.setCellValueFactory(new PropertyValueFactory<>("averageNumberOfShotsPerGame"));

        profilesTable.getColumns().addAll(tc1, tc2 , tc3, tc4,tc5);
        // set all columns to the same size to fit in tableView
        profilesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        profilesTable.setEditable(false);
        profilesTable.setPrefWidth(0.70*game.eightPoolTableWidth);
        profilesTable.setItems(PlayerModel.playersList);

        VBox profileControlsBox = new VBox();
        profileControlsBox.setAlignment(Pos.CENTER);
        profileControlsBox.setSpacing(0.02*game.eightPoolTableHeight);

        VBox newPlayerBox = new VBox();
        newPlayerBox.setAlignment(Pos.CENTER);
        newPlayerBox.setPadding(new Insets(0.05*game.eightPoolTableHeight,0,0.05*game.eightPoolTableHeight,0));
        Text newPlayerText = new Text("New Username: ");
        TextField newPlayerTextField = new TextField();
        newPlayerTextField.setPromptText("Enter Username");
        Button newPlayerButton = new Button("Add Player");
        newPlayerButton.setTextAlignment(TextAlignment.CENTER);
        newPlayerButton.setPrefWidth(newPlayerBox.getMinWidth());
        newPlayerBox.getChildren().addAll(newPlayerText,newPlayerTextField,newPlayerButton);

        Separator sep2 = new Separator();

        Button deletePlayer = new Button("Delete Selected Player");
        deletePlayer.setTextAlignment(TextAlignment.CENTER);
        Button renamePlayer = new Button("Rename Selected Player");
        renamePlayer.setTextAlignment(TextAlignment.CENTER);
        Button backButton = new Button("Back to Main Menu");
        backButton.setTextAlignment(TextAlignment.CENTER);
        backButton.setOnAction(e->{
            MainMenuController.gotoMainMenu();
        });

        profileControlsBox.getChildren().addAll(newPlayerBox,deletePlayer,renamePlayer,backButton);

        profileBox.getChildren().addAll(profilesTable,profileControlsBox);

        settings2Box.getChildren().addAll(sep2,profileBox);


        main.getChildren().add(settings2Box);

    }

    public SettingsController getSettingsController() {
        return settingsController;
    }
}

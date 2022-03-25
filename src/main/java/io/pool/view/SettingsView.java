package io.pool.view;

import io.pool.controller.MainMenuController;
import io.pool.eightpool.game;
import io.pool.model.PlayerModel;
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

    public SettingsView() {
        VBox main = new VBox();
        main.setAlignment(Pos.CENTER);
        this.setCenter(main);

        HBox option1Box = new HBox();
        option1Box.setAlignment(Pos.CENTER);
        option1Box.setPadding(new Insets(0.02*game.eightPoolTableHeight,0.01*game.eightPoolTableWidth,0.02*game.eightPoolTableHeight,0.01*game.eightPoolTableWidth));
        option1Box.setSpacing(0.01*game.eightPoolTableWidth);
        //option1Box.setPrefWidth(0.95*game.eightPoolTableWidth);
        Text option1Text = new Text("Control Option:");
        ComboBox<String> option1 = new ComboBox<String>(option1List);
        option1.getSelectionModel().select(0);
        option1Box.getChildren().addAll(option1Text,option1);
        main.getChildren().add(option1Box);


        VBox option2Box = new VBox();
        option2Box.setAlignment(Pos.CENTER);
        option2Box.setPadding(new Insets(0,0.01*game.eightPoolTableWidth,0.02*game.eightPoolTableHeight,0.01*game.eightPoolTableWidth));
        option2Box.setSpacing(0.01*game.eightPoolTableWidth);

        Separator sep1 = new Separator();

        Text option2Text = new Text("Friction Percentage: ");
        Slider option2 = new Slider();
        option2.setMin(50);
        option2.setMax(150);
        option2.setValue(100);
        option2.setBlockIncrement(25);
        option2.setShowTickLabels(true);
        option2.setShowTickMarks(true);
        option2.setMajorTickUnit(25);
        option2.setMinorTickCount(0);
        option2.setSnapToTicks(true);
        option2.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return (object.intValue())+"%";
            }

            @Override
            public Double fromString(String string) {
                return Double.valueOf(string.substring(0,string.length()-1));
            }
        });

        Separator sep2 = new Separator();

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

        Separator sep3 = new Separator();

        Button deletePlayer = new Button("Delete Selected Player");
        deletePlayer.setTextAlignment(TextAlignment.CENTER);
        Button renamePlayer = new Button("Rename Selected Player");
        renamePlayer.setTextAlignment(TextAlignment.CENTER);
        Button backButton = new Button("Back to Main Menu");
        backButton.setTextAlignment(TextAlignment.CENTER);
        backButton.setOnAction(e->{
            MainMenuController.gotoMainMenu();
        });

        profileControlsBox.getChildren().addAll(newPlayerBox,sep3,deletePlayer,renamePlayer,backButton);

        profileBox.getChildren().addAll(profilesTable,profileControlsBox);

        option2Box.getChildren().addAll(sep1,option2Text,option2,sep2,profileBox);


        main.getChildren().add(option2Box);

    }
}

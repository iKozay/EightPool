package io.pool.view;

import io.pool.controller.GameController;
import io.pool.controller.MainMenuController;
import io.pool.controller.SettingsController;
import io.pool.eightpool.ResourcesLoader;
import io.pool.model.BallModel;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.jfoenix.controls.JFXSlider;
import javafx.util.StringConverter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameView extends Pane {

    private Stage popupWindow;
    private Label popupMessage;
    private Button backToMainMenuButton, keepPlayingButton;

    private ArrayList<StackPane> balls = new ArrayList<>();
    private  ArrayList<BallView> ballViews = new ArrayList<>();
    /** Instance of GameController that will control the Ball,Table and PoolCue Controllers */
    private GameController gameController;
    /** Table that will be displayed to the user */
    private TableView tableView;
    /** Pool Cue that will be displayed to the user */
    private PoolCueView cueView;

    private AnchorPane gamePane;

    private AtomicBoolean principalBarIsVisible;
    private GridPane principalBar;

    private Button menuButton, backButton, ballsButton, tableButton, cueButton, backTableButton, nextTableButton, backCueButton, nextCueButton;

    private VBox ballsSettingsPane;

    private FlowPane ballsPrimaryPane;
    private VBox tableThemesPane;
    private GridPane ballsDataPane;
    private VBox cueThemePane;

    private JFXSlider frictionSlider;
    private TextField xAccelerationField;
    private TextField yAccelerationField;
    private TextField xSpeedField;
    private TextField ySpeedField;

    private ImageView menuIconImageView, leftArrowWhiteImageView, rightArrowWhiteImageView, tableLeftArrowImageView, tableRightArrowImageView, cueLeftArrowImageView, cueRightArrowImageView, leaveArrowImageView;
    private ImageView tablePreviewImageView, cuePreviewImageView;
    private int clickedBallNumber = -1;
    private ArrayList<Circle> selectionCircles = new ArrayList<>();
    private String[] colors = {"#1d6809", "#3677a1", "#248710", "#ba5050", " #02474b", "#d99e32", "#5e3b29", "#adeafa"};

    /**
     * Main Constructor of GameView
     */
    public GameView(SettingsController settingsController) {
        /**
         * Instantiates the Views and GameController
         */
        tableView = new TableView(this);
        cueView = new PoolCueView();
        displayPoolCue(false);

        this.getChildren().addAll(cueView.getCue(), cueView.getPoolLine(),cueView.getPath());
        gamePane = tableView.getGamePane();

        ArrayList<String> backgroundColors = new ArrayList<>(Arrays.asList(colors));

        // Making the popup window that will give the user the option to keep playing or stopping

        popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);

        popupMessage = new Label("");
        popupMessage.setTextFill(Color.RED);
        popupMessage.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, TableView.width/55.));

        backToMainMenuButton = new Button("Back to Main Menu");
        backToMainMenuButton.setTextFill(Color.BLACK);
        backToMainMenuButton.setFont(Font.font("Verdana", FontWeight.NORMAL, TableView.width/65.));
        backToMainMenuButton.setOnAction(e -> {
            MainMenuController.gotoMainMenu();
            popupWindow.close();
        });

        keepPlayingButton = new Button("Keep playing");
        keepPlayingButton.setTextFill(Color.BLACK);
        keepPlayingButton.setFont(Font.font("Verdana", FontWeight.NORMAL, TableView.width/65.));
        keepPlayingButton.setOnAction(e -> {
//            gameController.startGame(0);
        });

        HBox choiceButtonsPane = new HBox();
        choiceButtonsPane.setAlignment(Pos.CENTER);
        choiceButtonsPane.setSpacing(25);

        choiceButtonsPane.getChildren().addAll(backToMainMenuButton, keepPlayingButton);

        VBox popupWindowPane = new VBox(25);
        popupWindowPane.setAlignment(Pos.CENTER);
        popupWindowPane.getChildren().addAll(popupMessage, choiceButtonsPane);

        Scene scene1= new Scene(popupWindowPane, 400, 250);

        popupWindow.setScene(scene1);
        popupWindow.setOnCloseRequest(event -> {
            MainMenuController.gotoMainMenu();
            popupWindow.close();
        });

        /** working on the right corner setting section **/

        principalBarIsVisible = new AtomicBoolean(false);
        principalBar = new GridPane();
        principalBar.setAlignment(Pos.CENTER_LEFT);
        principalBar.setMaxHeight(TableView.height/7.8);
        principalBar.setMaxWidth(TableView.width/2.15);
        principalBar.setStyle("-fx-background-color: #3D4956; -fx-background-radius: 15px;");
        principalBar.translateXProperty().set(TableView.width/2.4);


        ballsSettingsPane = new VBox();
        ballsSettingsPane.setSpacing(TableView.width/43.2);
        ballsSettingsPane.setMaxWidth(TableView.width/2.15);
        ballsSettingsPane.setAlignment(Pos.TOP_CENTER);

        ballsPrimaryPane = new FlowPane();
        ballsPrimaryPane.setVisible(false);
        ballsPrimaryPane.setMinWidth(TableView.width/2.15);
        ballsPrimaryPane.setPrefWidth(TableView.width/2.15);
        ballsPrimaryPane.setAlignment(Pos.CENTER);
        ballsPrimaryPane.setStyle("-fx-background-color: #3D4956; -fx-background-radius: 15");
        ballsPrimaryPane.setVgap(TableView.height/23.4);
        ballsPrimaryPane.setHgap(TableView.width/25.);
        ballsPrimaryPane.setPadding(new Insets(25));


        ballsDataPane = new GridPane();
        ballsDataPane.setStyle("-fx-background-color: #3D4956; -fx-background-radius: 15");
        ballsDataPane.setMinWidth(TableView.width/2.15);
        ballsDataPane.setPrefHeight(TableView.height/4.);
        ballsDataPane.setVisible(false);
        ballsDataPane.setPadding(new Insets(25));
        ballsDataPane.setHgap(TableView.width/30.);
        ballsDataPane.setVgap(TableView.height/20.);
        // creating the components of the data Pane

        Label xSpeedLabel = new Label("X:");
        xSpeedLabel.setTextFill(Color.WHITE);
        xSpeedLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));

        Label ySpeedLabel = new Label("Y:");
        ySpeedLabel.setTextFill(Color.WHITE);
        ySpeedLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));

        Label speedLabel = new Label("Speed ");
        speedLabel.setTextFill(Color.WHITE);
        speedLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));

        xSpeedField = new TextField();
        xSpeedField.setEditable(false);
        xSpeedField.setAlignment(Pos.CENTER_LEFT);
        xSpeedField.setText("xxxx");
        xSpeedField.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        xSpeedField.setMaxWidth(TableView.width/15.);

        ySpeedField = new TextField();
        ySpeedField.setEditable(false);
        ySpeedField.setAlignment(Pos.CENTER_LEFT);
        ySpeedField.setText("yyyy");
        ySpeedField.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        ySpeedField.setMaxWidth(TableView.width/15.);

        //
        Label xAccelerationLabel = new Label("X:");
        xAccelerationLabel.setTextFill(Color.WHITE);
        xAccelerationLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));

        Label yAccelerationLabel = new Label("Y:");
        yAccelerationLabel.setTextFill(Color.WHITE);
        yAccelerationLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));

        Label accelerationLabel = new Label("Acceleration ");
        accelerationLabel.setTextFill(Color.WHITE);
        accelerationLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));

        xAccelerationField = new TextField();
        xAccelerationField.setEditable(false);
        xAccelerationField.setAlignment(Pos.CENTER_LEFT);
        xAccelerationField.setText("xxxx");
        xAccelerationField.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        xAccelerationField.setMaxWidth(TableView.width/15.);

        yAccelerationField = new TextField();
        yAccelerationField.setEditable(false);
        yAccelerationField.setAlignment(Pos.CENTER_LEFT);
        yAccelerationField.setText("yyyy");
        yAccelerationField.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        yAccelerationField.setMaxWidth(TableView.width/15.);

        //adding Speed components
        ballsDataPane.add(speedLabel, 0, 0);
        ballsDataPane.add(xSpeedLabel, 1, 0);
        ballsDataPane.add(ySpeedLabel, 3, 0);
        ballsDataPane.add(xSpeedField, 2, 0);
        ballsDataPane.add(ySpeedField, 4, 0);

        //adding Acceleration components
        ballsDataPane.add(accelerationLabel, 0, 1);
        ballsDataPane.add(xAccelerationLabel, 1, 1);
        ballsDataPane.add(yAccelerationLabel, 3, 1);
        ballsDataPane.add(xAccelerationField, 2, 1);
        ballsDataPane.add(yAccelerationField, 4, 1);


        /////////////////////////////////////////////////
        createImageViews();
        /////////////////////////////////////////////////

        menuButton = new Button("");
        menuButton.setGraphic(menuIconImageView);
        menuButton.setStyle("-fx-background-color: transparent; ");
        menuButton.setContentDisplay(ContentDisplay.CENTER);
        menuButton.setMaxWidth(TableView.width/36.);
        menuButton.setMaxHeight(TableView.height/7.8);
        menuButton.setPrefWidth(75);
        menuButton.setPrefHeight(75);

        menuButton.setOnMouseEntered(event -> {
            if (principalBarIsVisible.get()) menuButton.setGraphic(rightArrowWhiteImageView);
            else menuButton.setGraphic(leftArrowWhiteImageView);
        });

        menuButton.setOnMouseExited(event -> menuButton.setGraphic(menuIconImageView));

        menuButtonSetOnAction();

        ballsButton = new Button("Balls");
        ballsButton.setTextFill(Color.WHITE);
        ballsButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        ballsButton.setStyle("-fx-background-color: transparent");
        ballsButton.setPrefWidth(TableView.width/10.8);
        ballsButton.setOnMouseEntered(event -> {
            ballsButton.setTextFill(Color.LIGHTPINK);
            ballsButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/65.));
        });
        ballsButton.setOnMouseExited(event -> {
            ballsButton.setTextFill(Color.WHITE);
            ballsButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        });

        ballsButtonSetOnAction();


        tableButton = new Button("Table");
        tableButton.setTextFill(Color.WHITE);
        tableButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        tableButton.setStyle("-fx-background-color: transparent");
        tableButton.setPrefWidth(TableView.width/10.8);

        cueButton = new Button("Cue");
        cueButton.setTextFill(Color.WHITE);
        cueButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        cueButton.setStyle("-fx-background-color: transparent");
        cueButton.setPrefWidth(TableView.width/10.8);

        cueButton.setOnMouseEntered(event -> {
            cueButton.setTextFill(Color.LIGHTPINK);
            cueButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/65.));
        });
        cueButton.setOnMouseExited(event -> {
            cueButton.setTextFill(Color.WHITE);
            cueButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        });

        cueButton.setOnAction(event -> {
            cueThemePane.setVisible(true);
            if (tableThemesPane.isVisible()) tableThemesPane.setVisible(false);
            if (ballsPrimaryPane.isVisible()) ballsPrimaryPane.setVisible(false);
        });

        backButton = new Button("Main Menu", leaveArrowImageView);
        backButton.setTextFill(Color.WHITE);
        backButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        backButton.setGraphic(leaveArrowImageView);
        backButton.setGraphicTextGap(10);
        backButton.setStyle("-fx-background-color: red; -fx-background-radius: 0 15 15 0");
        backButton.setPrefWidth(TableView.width/6.);
        backButton.setMaxHeight(TableView.height/7.5);
        backButton.setOnAction(e->{
            MainMenuController.gotoMainMenu();
        });
        backButton.setOnMouseEntered(event -> backButton.setStyle("-fx-background-color: pink; -fx-background-radius: 0 15 15 0"));
        backButton.setOnMouseExited(event -> backButton.setStyle("-fx-background-color: red; -fx-background-radius: 0 15 15 0"));

        principalBar.add(menuButton, 0, 0);
        principalBar.add(ballsButton, 1,0);
        principalBar.add(tableButton, 2,0);
        principalBar.add(cueButton, 3,0);
        principalBar.add(backButton, 4,0);


        for (int i=1;i<=16;i++) {
            BallView bView;
            bView = new BallView(ResourcesLoader.ballImages.get(i-1),BallModel.RADIUS + (TableView.width/150));

            Circle selectionCircle = getCircleFromSphere(bView.getBall());
            selectionCircle.setFill(Color.TRANSPARENT);
            selectionCircle.setStroke(Color.WHITE);
            selectionCircle.setStrokeWidth(2);
            selectionCircle.setVisible(false);
            bView.getBall().setOnMouseEntered(event -> {
                selectionCircle.setVisible(true);
                selectionCircle.setStroke(Color.WHITE);
            });
            bView.getBall().setOnMouseExited(event -> {
                if (!selectionCircle.getStroke().equals(Color.GREENYELLOW)) selectionCircle.setVisible(false);
            });
            ballViews.add(bView);
            selectionCircles.add(selectionCircle);
            StackPane ball = new StackPane();
            ball.getChildren().addAll(selectionCircle, bView.getBall());
            balls.add(ball);
            ballsPrimaryPane.getChildren().add(ball);

        }

        updateOnBallsClickedListener();

        //working on table Theme section
        tableThemesPane = new VBox();
        tableThemesPane.setVisible(false);
        tableThemesPane.setAlignment(Pos.TOP_LEFT);
        tableThemesPane.setSpacing(40);
        tableThemesPane.setMaxWidth(TableView.width/2.15);
        tableThemesPane.setPrefHeight(TableView.height/1.5);
        tableThemesPane.setStyle("-fx-background-color: #3D4956; -fx-background-radius: 15");

        int[] currentTableImageView = {1};

        Label tableThemeLabel = new Label("Choose a table theme:");
        tableThemeLabel.setPadding(new Insets(20, 20, 0, 20));
        tableThemeLabel.setTextFill(Color.WHITE);
        tableThemeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/55.));

        backTableButton = new Button();
        backTableButton.setGraphic(tableLeftArrowImageView);
        backTableButton.setStyle("-fx-background-color: transparent");
        backTableButton.setContentDisplay(ContentDisplay.CENTER);
        backTableButton.setMaxWidth(TableView.width/36.);
        backTableButton.setMaxHeight(TableView.height/7.8);
        backTableButton.setPrefWidth(100);
        backTableButton.setPrefHeight(100);

        nextTableButton = new Button();
        nextTableButton.setGraphic(tableRightArrowImageView);
        nextTableButton.setStyle("-fx-background-color: transparent");
        nextTableButton.setContentDisplay(ContentDisplay.CENTER);
        nextTableButton.setMaxWidth(TableView.width/36.);
        nextTableButton.setMaxHeight(TableView.height/7.8);
        nextTableButton.setPrefWidth(100);
        nextTableButton.setPrefHeight(100);

        tablePreviewImageView = new ImageView();
        tablePreviewImageView.setImage(ResourcesLoader.tableImages.get(0));
        tablePreviewImageView.setFitWidth(TableView.width/4.5);
        tablePreviewImageView.setPreserveRatio(true);

        Rectangle applyTableDesignRec = new Rectangle();
        applyTableDesignRec.setWidth(TableView.width/5.5);
        applyTableDesignRec.setHeight(TableView.height/11.);
        applyTableDesignRec.setStroke(Color.WHITE);
        applyTableDesignRec.setStrokeWidth(3);
        applyTableDesignRec.setArcWidth(20);
        applyTableDesignRec.setArcHeight(20);
        applyTableDesignRec.setFill(Color.valueOf("#3D4956"));

        Button applyTableThemeButton = new Button("Apply Theme");
        applyTableThemeButton.setTextFill(Color.WHITE);
        applyTableThemeButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/58.));
        applyTableThemeButton.setStyle("-fx-background-color: transparent");
        applyTableThemeButton.setPrefWidth(TableView.width/4.5);
        applyTableThemeButton.setMinHeight(TableView.height/9.);

        applyTableThemeButton.setOnMouseEntered(event -> {
            applyTableThemeButton.setTextFill(Color.LIGHTPINK);
            applyTableThemeButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/52.));
        });
        applyTableThemeButton.setOnMouseExited(event -> {
            applyTableThemeButton.setTextFill(Color.WHITE);
            applyTableThemeButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/58.));
        });

        applyTableThemeButton.setOnAction(event -> {
            tableView.getTableImageView().setImage(tablePreviewImageView.getImage());

            getGamePane().setStyle("-fx-background-color: " + backgroundColors.get(currentTableImageView[0]-1));
        });

        StackPane applyTablePane = new StackPane();
        applyTablePane.getChildren().addAll(applyTableDesignRec, applyTableThemeButton);
        applyTablePane.setStyle("-fx-background-color: transparent");
        
        backTableButton.setOnAction(event -> {
            if (currentTableImageView[0] == 1) currentTableImageView[0] = 8;
            else currentTableImageView[0]--;
            tablePreviewImageView.setImage(ResourcesLoader.tableImages.get(currentTableImageView[0]-1));
        });

        nextTableButton.setOnAction(event -> {
            if (currentTableImageView[0] == 8) currentTableImageView[0] = 1;
            else currentTableImageView[0]++;
            tablePreviewImageView.setImage(ResourcesLoader.tableImages.get(currentTableImageView[0]-1));
        });

        GridPane tableSwitchPane = new GridPane();
        tableSwitchPane.setPadding(new Insets(0,20,20,20));
        tableSwitchPane.setAlignment(Pos.CENTER);
        tableSwitchPane.setHgap(40);
        tableSwitchPane.setVgap(20);
//        tableSwitchPane.getChildren().addAll(backTableButton, tablePreviewImageView, nextTableButton);

        tableSwitchPane.add(backTableButton, 0, 1);
        tableSwitchPane.add(tablePreviewImageView, 1, 1);
        tableSwitchPane.add(nextTableButton, 2, 1);
        tableSwitchPane.add(applyTablePane, 1, 2);

        ///////////////////////////////////////////////////

        AnchorPane frictionSettingsPane = new AnchorPane();
        frictionSettingsPane.setPadding(new Insets(0, 0, 10, 0));
        Label instructiveFrictionLabel = new Label("Choose table's friction:");
        instructiveFrictionLabel.setTextFill(Color.WHITE);
        instructiveFrictionLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));

        frictionSlider = new JFXSlider(25, 175, 100);
        frictionSlider.setPrefWidth(TableView.width/2.15);
        frictionSlider.setIndicatorPosition(JFXSlider.IndicatorPosition.LEFT);
        frictionSlider.setBlockIncrement(25);
        frictionSlider.setShowTickLabels(true);
        frictionSlider.setShowTickMarks(true);
        frictionSlider.setMajorTickUnit(25);
        frictionSlider.setMinorTickCount(0);
        frictionSlider.setSnapToTicks(true);
        frictionSlider.setPadding(new Insets(20, 0, 0, 0));
        frictionSlider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return (object.intValue())+"%";
            }

            @Override
            public Double fromString(String string) {
                return Double.valueOf(string.substring(0,string.length()-1));
            }
        });
        frictionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            settingsController.setFrictionPercentage(newValue.doubleValue());
        });

        frictionSettingsPane.getChildren().addAll(instructiveFrictionLabel, frictionSlider);
        AnchorPane.setTopAnchor(instructiveFrictionLabel, 0.);
        AnchorPane.setLeftAnchor(instructiveFrictionLabel, 0.);
        AnchorPane.setTopAnchor(frictionSlider, TableView.width/25.);
        AnchorPane.setLeftAnchor(frictionSlider, 0.);

        /////////////////////////////////////////////////////////////

        //working on table Theme section
        cueThemePane = new VBox();
        cueThemePane.setVisible(false);
        cueThemePane.setAlignment(Pos.TOP_LEFT);
        cueThemePane.setSpacing(40);
        cueThemePane.setPrefWidth(TableView.width/2.15);
        cueThemePane.setMaxWidth(TableView.width/2.15);
        cueThemePane.setPrefHeight(TableView.height/1.5);
        cueThemePane.setStyle("-fx-background-color: #3D4956; -fx-background-radius: 15");
        cueThemePane.setPadding(new Insets(20));

        int[] currentCueImageView = {1};

        Label cueThemeLabel = new Label("Choose a cue theme:");
        cueThemeLabel.setTextFill(Color.WHITE);
        cueThemeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));

        backCueButton = new Button();
        backCueButton.setGraphic(cueLeftArrowImageView);
        backCueButton.setStyle("-fx-background-color: transparent");
        backCueButton.setContentDisplay(ContentDisplay.CENTER);
        backCueButton.setMaxWidth(TableView.width/36.);
        backCueButton.setMaxHeight(TableView.height/7.8);
        backCueButton.setPrefWidth(100);
        backCueButton.setPrefHeight(100);

        nextCueButton = new Button();
        nextCueButton.setGraphic(cueRightArrowImageView);
        nextCueButton.setStyle("-fx-background-color: transparent");
        nextCueButton.setContentDisplay(ContentDisplay.CENTER);
        nextCueButton.setMaxWidth(TableView.width/36.);
        nextCueButton.setMaxHeight(TableView.height/7.8);
        nextCueButton.setPrefWidth(100);
        nextCueButton.setPrefHeight(100);

        cuePreviewImageView = new ImageView();
        cuePreviewImageView.setImage(ResourcesLoader.tableImages.get(0));
        cuePreviewImageView.setFitWidth(TableView.width/3.6);
        cuePreviewImageView.setPreserveRatio(true);

        cuePreviewImageView.setOnMouseClicked(event -> {
//            tableView.getTableImageView().setImage(tablePreviewImageView.getImage());
        });

        backCueButton.setOnAction(event -> {
            if (currentCueImageView[0] == 1) currentCueImageView[0] = 7;
            else currentCueImageView[0]--;
            cuePreviewImageView.setImage(ResourcesLoader.tableImages.get(currentCueImageView[0]));
        });

        nextCueButton.setOnAction(event -> {
            if (currentCueImageView[0] == 7) currentCueImageView[0] = 1;
            else currentCueImageView[0]++;
            cuePreviewImageView.setImage(ResourcesLoader.tableImages.get(currentCueImageView[0]));
        });

        HBox cueSwitchPane = new HBox();
        cueSwitchPane.setAlignment(Pos.CENTER);
        cueSwitchPane.setSpacing(40);

        cueSwitchPane.getChildren().addAll(backCueButton, cuePreviewImageView, nextCueButton);
        cueThemePane.getChildren().add(cueSwitchPane);

        tableThemesPane.getChildren().addAll(tableThemeLabel, tableSwitchPane, frictionSettingsPane);

        ballsSettingsPane.getChildren().addAll(ballsPrimaryPane, ballsDataPane);


        tableButtonSetListener();


        gamePane.getChildren().addAll(principalBar, ballsSettingsPane, tableThemesPane, cueThemePane);

        gameController = new GameController(this, settingsController);

        AnchorPane.setTopAnchor(principalBar, TableView.height/35.0);
        AnchorPane.setRightAnchor(principalBar, 7.);
        AnchorPane.setTopAnchor(ballsSettingsPane, TableView.height/5.5);
        AnchorPane.setRightAnchor(ballsSettingsPane,7.0);
        AnchorPane.setTopAnchor(tableThemesPane, TableView.height/5.5);
        AnchorPane.setRightAnchor(tableThemesPane, 7.0);
        AnchorPane.setTopAnchor(cueThemePane, TableView.height/5.5);
        AnchorPane.setRightAnchor(cueThemePane, 7.0);

    }


    public void updateOnBallsClickedListener() {
        for (int i = 0; i < ballViews.size(); i++) {
            int finalI = i;
            ballViews.get(i).getBall().setOnMouseClicked(event -> {
                for (Circle circle : selectionCircles) circle.setVisible(false);
                selectionCircles.get(finalI).setVisible(true);
                selectionCircles.get(finalI).setStroke(Color.GREENYELLOW);
                ballsDataPane.setVisible(true);
                clickedBallNumber = finalI + 1;
            });
        }
    }

    private void createImageViews() {
        menuIconImageView = new ImageView();
        menuIconImageView.setImage(ResourcesLoader.iconImages.get(0));
        menuIconImageView.setFitWidth(TableView.width/36.);
        menuIconImageView.setPreserveRatio(true);

        leftArrowWhiteImageView = new ImageView();
        leftArrowWhiteImageView.setImage(ResourcesLoader.iconImages.get(1));
        leftArrowWhiteImageView.setFitWidth(TableView.width/36.);
        leftArrowWhiteImageView.setPreserveRatio(true);


        rightArrowWhiteImageView = new ImageView();
        rightArrowWhiteImageView.setImage(ResourcesLoader.iconImages.get(2));
        rightArrowWhiteImageView.setFitWidth(TableView.width/36.);
        rightArrowWhiteImageView.setPreserveRatio(true);

        tableLeftArrowImageView = new ImageView();
        tableLeftArrowImageView.setImage(ResourcesLoader.iconImages.get(3));
        tableLeftArrowImageView.setFitWidth(TableView.width/30.);
        tableLeftArrowImageView.setPreserveRatio(true);

        tableRightArrowImageView = new ImageView();
        tableRightArrowImageView.setImage(ResourcesLoader.iconImages.get(4));
        tableRightArrowImageView.setFitWidth(TableView.width/30.);
        tableRightArrowImageView.setPreserveRatio(true);


        cueLeftArrowImageView = new ImageView();
        cueLeftArrowImageView.setImage(ResourcesLoader.iconImages.get(3));
        cueLeftArrowImageView.setFitWidth(TableView.width/30.);
        cueLeftArrowImageView.setPreserveRatio(true);


        cueLeftArrowImageView = new ImageView();
        cueLeftArrowImageView.setImage(ResourcesLoader.iconImages.get(4));
        cueLeftArrowImageView.setFitWidth(TableView.width/30.);
        cueLeftArrowImageView.setPreserveRatio(true);

        leaveArrowImageView = new ImageView();
        leaveArrowImageView.setImage(ResourcesLoader.iconImages.get(5));
        leaveArrowImageView.setFitWidth(TableView.width/36.);
        leaveArrowImageView.setPreserveRatio(true);
    }

    private void ballsButtonSetOnAction() {
        ballsButton.setOnAction(event -> {
            ballsPrimaryPane.setVisible(!ballsPrimaryPane.isVisible());
            if (tableThemesPane.isVisible()) tableThemesPane.setVisible(false);
            if (cueThemePane.isVisible()) cueThemePane.setVisible(false);
            ballsDataPane.setVisible(false);
        });

    }

    private void tableButtonSetListener() {

        tableButton.setOnMouseEntered(event -> {
            tableButton.setTextFill(Color.LIGHTPINK);
            tableButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/65.));
        });
        tableButton.setOnMouseExited(event -> {
            tableButton.setTextFill(Color.WHITE);
            tableButton.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        });

        tableButton.setOnAction(event -> {

            ballsDataPane.setVisible(false);
            if (ballsPrimaryPane.isVisible()) ballsPrimaryPane.setVisible(false);
            if (cueThemePane.isVisible()) cueThemePane.setVisible(false);
            tableThemesPane.setVisible(!tableThemesPane.isVisible());

        });
    }

    private void menuButtonSetOnAction() {
        menuButton.setOnAction(event -> {

            ballsPrimaryPane.setVisible(false);
            ballsDataPane.setVisible(false);
            tableThemesPane.setVisible(false);

            if (principalBarIsVisible.get()) {
                Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), new KeyValue(principalBar.translateXProperty(), (TableView.width/2.4), Interpolator.EASE_IN)));
                timeline1.play();
                principalBarIsVisible.set(false);
            } else {
                principalBarIsVisible.set(true);
                Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(1), new KeyValue(principalBar.translateXProperty(), 0, Interpolator.EASE_IN)));
                timeline2.play();
            }

        });
    }

    /**
     * Gets the GameController
     * @return the GameController
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Gets the Table View
     * @return Table View
     */
    public TableView getTableView() {
        return tableView;
    }

    /**
     * Gets the Pool Cue View
     * @return Pool Cue View
     */
    public PoolCueView getCueView() {
        return cueView;
    }
    public void displayPoolCue(boolean visibility){
        this.cueView.getCue().toFront();
        this.cueView.getCue().setVisible(visibility);
        this.cueView.getPoolLine().setVisible(visibility);
    }

    public AnchorPane getGamePane() {
        return gamePane;
    }

    public AtomicBoolean getPrincipalBarIsVisible() {
        return principalBarIsVisible;
    }

    public GridPane getPrincipalBar() {
        return principalBar;
    }

    public Button getMenuButton() {
        return menuButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getBallsButton() {
        return ballsButton;
    }

    public Button getTableButton() {
        return tableButton;
    }

    public Button getCueButton() {
        return cueButton;
    }

    public VBox getBallsSettingsPane() {
        return ballsSettingsPane;
    }

    public FlowPane getBallsPrimaryPane() {
        return ballsPrimaryPane;
    }

    public VBox getTableThemesPane() {
        return tableThemesPane;
    }

    public TextField getxAccelerationField() {
        return xAccelerationField;
    }

    public TextField getyAccelerationField() {
        return yAccelerationField;
    }

    public TextField getxSpeedField() {
        return xSpeedField;
    }

    public TextField getySpeedField() {return ySpeedField;}

    public void setxAccelerationField(TextField positionValueField) {
        this.xAccelerationField = positionValueField;
    }

    public void setyAccelerationField(TextField yAccelerationField) {
        this.yAccelerationField = yAccelerationField;
    }

    public void setxSpeedField(TextField xSpeedField) {
        this.xSpeedField = xSpeedField;
    }



    public Circle getCircleFromSphere(Sphere ball) {
        return new Circle(ball.getLayoutX(),ball.getLayoutY(),ball.getRadius());
    }

    public int getClickedBallNumber() {
        return clickedBallNumber;
    }

    public ArrayList<BallView> getBallViews() {
        return ballViews;
    }

    public ArrayList<Circle> getSelectionCircles() {
        return selectionCircles;
    }

    public ArrayList<StackPane> getBalls() {
        return balls;
    }

    public void setClickedBallNumber(int clickedBallNumber) {
        this.clickedBallNumber = clickedBallNumber;
    }

    public Stage getPopupWindow() {
        return popupWindow;
    }

    public Label getPopupMessage() {
        return popupMessage;
    }
}

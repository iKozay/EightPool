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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameView extends Pane {
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

    private Button menuButton, backButton, ballsButton, tableButton, cueButton, backTableButton, nextTableButton;

    private VBox ballsSettingsPane;

    private FlowPane ballsPrimaryPane;
    private VBox tableThemesPane;
    private GridPane ballsDataPane;

    private Pane cueSettingsPane;

    private TextField xPositionField;
    private TextField yPositionField;
    private TextField xSpeedField;
    private TextField ySpeedField;

    private ImageView menuIconImageView, leftArrowWhiteImageView, rightArrowWhiteImageView, leftArrowYellowImageView, rightArrowYellowImageView, leaveArrowImageView;
    private ImageView tablePreviewImageView;
    private int clickedBallNumber = -1;
    private ArrayList<Circle> selectionCircles = new ArrayList<>();

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

        Label positionLabel = new Label("Position ");
        positionLabel.setTextFill(Color.WHITE);
        positionLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));


        Label xPosLabel = new Label("X:");
        xPosLabel.setTextFill(Color.WHITE);
        xPosLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));

        Label yPosLabel = new Label("Y:");
        yPosLabel.setTextFill(Color.WHITE);
        yPosLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));

        Label xSpeedLabel = new Label("X:");
        xSpeedLabel.setTextFill(Color.WHITE);
        xSpeedLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));

        Label ySpeedLabel = new Label("Y:");
        ySpeedLabel.setTextFill(Color.WHITE);
        ySpeedLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));


        Label speedLabel = new Label("Speed ");
        speedLabel.setTextFill(Color.WHITE);
        speedLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));

        xPositionField = new TextField();
        xPositionField.setEditable(false);
        xPositionField.setAlignment(Pos.CENTER_LEFT);
        xPositionField.setText("xxxx");
        xPositionField.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        xPositionField.setMaxWidth(TableView.width/15.);

        yPositionField = new TextField();
        yPositionField.setEditable(false);
        yPositionField.setAlignment(Pos.CENTER_LEFT);
        yPositionField.setText("yyyy");
        yPositionField.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/72.));
        yPositionField.setMaxWidth(TableView.width/15.);

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

        //adding Position components
        ballsDataPane.add(positionLabel, 0, 0);
        ballsDataPane.add(xPosLabel, 1, 0);
        ballsDataPane.add(yPosLabel, 3, 0);
        ballsDataPane.add(xPositionField, 2, 0);
        ballsDataPane.add(yPositionField, 4, 0);

        //adding Speed components
        ballsDataPane.add(speedLabel, 0, 1);
        ballsDataPane.add(xSpeedLabel, 1, 1);
        ballsDataPane.add(ySpeedLabel, 3, 1);
        ballsDataPane.add(xSpeedField, 2, 1);
        ballsDataPane.add(ySpeedField, 4, 1);

        createImageViews();

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

        /**
        ToggleGroup rbGroup = new ToggleGroup();

        RadioButton defaultRb1 = new RadioButton();
        RadioButton blueRb2 = new RadioButton();
        RadioButton greenRb3 = new RadioButton();
        RadioButton redRb4 = new RadioButton();
        RadioButton turquoiseRb5 = new RadioButton();
        RadioButton purpleRb6 = new RadioButton();
        RadioButton brownRb7 = new RadioButton();
        RadioButton glaceRb8 = new RadioButton();


        defaultRb1.setToggleGroup(rbGroup);
        blueRb2.setToggleGroup(rbGroup);
        greenRb3.setToggleGroup(rbGroup);
        redRb4.setToggleGroup(rbGroup);
        turquoiseRb5.setToggleGroup(rbGroup);
        purpleRb6.setToggleGroup(rbGroup);
        brownRb7.setToggleGroup(rbGroup);
        glaceRb8.setToggleGroup(rbGroup);

        defaultRb1.setSelected(true);

        Image defaultTableImage = new Image(new File("src/main/resources/tableImage/1.png").toURI().toURL().toExternalForm());
        Image blueTableImage = new Image(new File("src/main/resources/tableImage/2.png").toURI().toURL().toExternalForm());
        Image greenTableImage = new Image(new File("src/main/resources/tableImage/3.png").toURI().toURL().toExternalForm());
        Image redTableImage = new Image(new File("src/main/resources/tableImage/4.png").toURI().toURL().toExternalForm());
        Image turquoiseTableImage = new Image(new File("src/main/resources/tableImage/5.png").toURI().toURL().toExternalForm());
        Image purpleTableImage = new Image(new File("src/main/resources/tableImage/6.png").toURI().toURL().toExternalForm());
        Image brownTableImage = new Image(new File("src/main/resources/tableImage/7.png").toURI().toURL().toExternalForm());
        Image glaceTableImage = new Image(new File("src/main/resources/tableImage/8.png").toURI().toURL().toExternalForm());

        rbGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            if (defaultRb1.isSelected()) tableView.getTableImageView().setImage(defaultTableImage);
            else if (blueRb2.isSelected()) tableView.getTableImageView().setImage(blueTableImage);
            else if (greenRb3.isSelected()) tableView.getTableImageView().setImage(greenTableImage);
            else if (redRb4.isSelected()) tableView.getTableImageView().setImage(redTableImage);
            else if (turquoiseRb5.isSelected()) tableView.getTableImageView().setImage(turquoiseTableImage);
            else if (purpleRb6.isSelected()) tableView.getTableImageView().setImage(purpleTableImage);
            else if (brownRb7.isSelected()) tableView.getTableImageView().setImage(brownTableImage);
            else if (glaceRb8.isSelected()) tableView.getTableImageView().setImage(glaceTableImage);

        });
         */

        //working on table Theme section
        tableThemesPane = new VBox();
        tableThemesPane.setVisible(false);
        tableThemesPane.setAlignment(Pos.TOP_LEFT);
        tableThemesPane.setSpacing(40);
        tableThemesPane.setPrefWidth(TableView.width/2.15);
        tableThemesPane.setMaxWidth(TableView.width/2.15);
        tableThemesPane.setPrefHeight(TableView.height/1.5);
        tableThemesPane.setStyle("-fx-background-color: #3D4956; -fx-background-radius: 15");
        tableThemesPane.setPadding(new Insets(20));

        int[] currentTableImageView = {1};

        Label tableThemeLabel = new Label("Choose a table theme:");
        tableThemeLabel.setTextFill(Color.WHITE);
        tableThemeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, TableView.width/60.));

        backTableButton = new Button();
        backTableButton.setGraphic(leftArrowYellowImageView);
        backTableButton.setStyle("-fx-background-color: transparent");
        backTableButton.setContentDisplay(ContentDisplay.CENTER);
        backTableButton.setMaxWidth(TableView.width/36.);
        backTableButton.setMaxHeight(TableView.height/7.8);
        backTableButton.setPrefWidth(100);
        backTableButton.setPrefHeight(100);

        nextTableButton = new Button();
        nextTableButton.setGraphic(rightArrowYellowImageView);
        nextTableButton.setStyle("-fx-background-color: transparent");
        nextTableButton.setContentDisplay(ContentDisplay.CENTER);
        nextTableButton.setMaxWidth(TableView.width/36.);
        nextTableButton.setMaxHeight(TableView.height/7.8);
        nextTableButton.setPrefWidth(100);
        nextTableButton.setPrefHeight(100);

        tablePreviewImageView = new ImageView();
        tablePreviewImageView.setImage(ResourcesLoader.tableImages.get(0));
        tablePreviewImageView.setFitWidth(TableView.width/3.6);
        tablePreviewImageView.setPreserveRatio(true);

        tablePreviewImageView.setOnMouseClicked(event -> {
            tableView.getTableImageView().setImage(tablePreviewImageView.getImage());
        });

        backTableButton.setOnAction(event -> {
            if (currentTableImageView[0] == 1) currentTableImageView[0] = 7;
            else currentTableImageView[0]--;
            tablePreviewImageView.setImage(ResourcesLoader.tableImages.get(currentTableImageView[0]));
        });

        nextTableButton.setOnAction(event -> {
            if (currentTableImageView[0] == 7) currentTableImageView[0] = 1;
            else currentTableImageView[0]++;
            tablePreviewImageView.setImage(ResourcesLoader.tableImages.get(currentTableImageView[0]));
        });

        HBox tableSwitchPane = new HBox();
        tableSwitchPane.setAlignment(Pos.CENTER);
        tableSwitchPane.setSpacing(40);

        tableSwitchPane.getChildren().addAll(backTableButton, tablePreviewImageView, nextTableButton);

        tableThemesPane.getChildren().addAll(tableThemeLabel, tableSwitchPane);

        ballsSettingsPane.getChildren().addAll(ballsPrimaryPane, ballsDataPane);


        tableButtonSetListener();


        gamePane.getChildren().addAll(principalBar, ballsSettingsPane, tableThemesPane);

        gameController = new GameController(this, settingsController);

        AnchorPane.setTopAnchor(principalBar, TableView.height/35.0);
        AnchorPane.setRightAnchor(principalBar, 7.);
        AnchorPane.setTopAnchor(ballsSettingsPane, TableView.height/5.5);
        AnchorPane.setRightAnchor(ballsSettingsPane,7.0);
        AnchorPane.setTopAnchor(tableThemesPane, TableView.height/5.5);
        AnchorPane.setRightAnchor(tableThemesPane, 7.0);
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

        leftArrowYellowImageView = new ImageView();
        leftArrowYellowImageView.setImage(ResourcesLoader.iconImages.get(3));
        leftArrowYellowImageView.setFitWidth(TableView.width/30.);
        leftArrowYellowImageView.setPreserveRatio(true);


        rightArrowYellowImageView = new ImageView();
        rightArrowYellowImageView.setImage(ResourcesLoader.iconImages.get(4));
        rightArrowYellowImageView.setFitWidth(TableView.width/30.);
        rightArrowYellowImageView.setPreserveRatio(true);

        leaveArrowImageView = new ImageView();
        leaveArrowImageView.setImage(ResourcesLoader.iconImages.get(5));
        leaveArrowImageView.setFitWidth(TableView.width/36.);
        leaveArrowImageView.setPreserveRatio(true);
    }

    private void ballsButtonSetOnAction() {
        ballsButton.setOnAction(event -> {
            ballsPrimaryPane.setVisible(!ballsPrimaryPane.isVisible());
            if (tableThemesPane.isVisible()) tableThemesPane.setVisible(false);
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

    public Pane getCueSettingsPane() {
        return cueSettingsPane;
    }

    public TextField getxPositionField() {
        return xPositionField;
    }

    public TextField getyPositionField() {
        return yPositionField;
    }

    public TextField getxSpeedField() {
        return xSpeedField;
    }

    public TextField getySpeedField() {return ySpeedField;}

    public void setxPositionField(TextField positionValueField) {
        this.xPositionField = positionValueField;
    }

    public void setyPositionField(TextField yPositionField) {
        this.yPositionField = yPositionField;
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


    //                /** creating animation */
//
//                Timeline animation1 = new Timeline(
//                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(0).fillProperty(), Color.SADDLEBROWN)),
//                        new KeyFrame(Duration.seconds(1), new KeyValue(circles.get(0).fillProperty(), Color.SADDLEBROWN)),
//                        new KeyFrame(Duration.seconds(2.5), new KeyValue(circles.get(0).fillProperty(), Color.YELLOW)),
//                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(0).fillProperty(), Color.GREEN))); // at the end of the animation the circle should reach the corners -> diameter = diagonale of rect
//
//
//                Timeline animation2 = new Timeline(
//                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(1).fillProperty(), Color.BLACK)),
//                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(1).fillProperty(), Color.SKYBLUE)));
//
//                Timeline animation3 = new Timeline(
//                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(2).fillProperty(), Color.BLACK)),
//                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(2).fillProperty(), Color.GREEN)));
//
//                Timeline animation4 = new Timeline(
//                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(3).fillProperty(), Color.BLUE)),
//                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(3).fillProperty(), Color.RED)));
//
//                Timeline animation5 = new Timeline(
//                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(4).fillProperty(), Color.BLUE)),
//                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(4).fillProperty(), Color.RED)));
//
//                Timeline animation6 = new Timeline(
//                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(5).fillProperty(), Color.BLUE)),
//                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(5).fillProperty(), Color.RED)));
//
//                Timeline animation7 = new Timeline(
//                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(6).fillProperty(), Color.BLUE)),
//                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(6).fillProperty(), Color.RED)));
//
//
//                animation1.playFromStart();
//                animation2.playFromStart();
//                animation3.playFromStart();
//                animation4.playFromStart();
//                animation5.playFromStart();
//                animation6.playFromStart();
//                animation7.playFromStart();

}

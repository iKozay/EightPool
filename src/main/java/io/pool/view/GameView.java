package io.pool.view;

import io.pool.controller.GameController;
import io.pool.controller.MainMenuController;
import io.pool.model.BallModel;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameView extends Pane {
    /** Instance of GameController that will control the Ball,Table and PoolCue Controllers */
    private GameController gameController;
    /** Table that will be displayed to the user */
    private TableView tableView;
    /** Pool Cue that will be displayed to the user */
    private PoolCueView cueView;

    private AnchorPane gamePane;

    private AtomicBoolean principalBarIsVisible;
    private GridPane principalBar;

    private Button menuButton, backButton, ballsButton, tableButton, cueButton;

    private VBox ballsSettingsPane;

    private FlowPane ballsPrimaryPane;
    private GridPane tableThemesPane;

    private Pane cueSettingsPane;

    private ImageView menuIconImageView, leftArrowImageView, rightArrowImageView, leaveArrowImageView;

    private Rectangle bounds;

    /**
     * This is the arrayList that would hold the rectangles
     * Note: they are circles actually, but with the animation, they would transform into rectangles
     */
    private ArrayList<Circle> circles;

    private ArrayList<Timeline> animations;
    /**
     * Main Constructor of GameView
     * @throws MalformedURLException if the path to the table image is incorrect
     */
    public GameView() throws MalformedURLException {
        /**
         * Instantiates the Views and GameController
         */
        tableView = new TableView(this);
        cueView = new PoolCueView();
        displayPoolCue(false);
        gameController = new GameController(this);
        this.getChildren().addAll(cueView.getCue());

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
            if (principalBarIsVisible.get()) menuButton.setGraphic(rightArrowImageView);
            else menuButton.setGraphic(leftArrowImageView);
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
        ///////

//        FlowPane ballsStrokePane = new FlowPane();
//        ballsPrimaryPane.setLayoutX(0);
//        ballsPrimaryPane.setLayoutY(0);
//        ballsStrokePane.setPadding(new Insets(25,0,25,0));
//        ballsStrokePane.setAlignment(Pos.CENTER);
//        ballsStrokePane.setStyle("-fx-background-color: #3D4956");
//        ballsStrokePane.setVgap(TableView.height/23.4);
//        ballsStrokePane.setHgap(TableView.width/30.857);



        for (int i=1;i<=16;i++) {
            BallModel bModel;
            BallView bView;
            if (i==16) bModel = new BallModel(i, new Image(new File("src/main/resources/billiards3D/white.jpg").toURI().toURL().toExternalForm()));
            else bModel = new BallModel(i, new Image(new File("src/main/resources/billiards3D/ball" + i + ".jpg").toURI().toURL().toExternalForm()));
            bView = new BallView(bModel.getImg(),BallModel.RADIUS + (TableView.width/150));

            Circle selectionCircle = getCircleFromSphere(bView.getBall());
            selectionCircle.setFill(Color.TRANSPARENT);
            selectionCircle.setStroke(Color.WHITE);
            selectionCircle.setStrokeWidth(2);
            selectionCircle.setVisible(false);
            bView.getBall().setOnMouseEntered(event -> selectionCircle.setVisible(true));
            bView.getBall().setOnMouseExited(event -> selectionCircle.setVisible(false));
            bView.getBall().setOnMouseClicked(event -> {
                selectionCircle.setStroke(Color.AZURE);
            });
            StackPane ball = new StackPane();
            ball.getChildren().addAll(selectionCircle, bView.getBall());
            ballsPrimaryPane.getChildren().add(ball);


        }

        tableThemesPane = new GridPane();
        tableThemesPane.setVisible(false);
        tableThemesPane.setPrefWidth(TableView.width/2.15);
        tableThemesPane.setPrefHeight(TableView.height/2.15);
        tableThemesPane.setStyle("-fx-background-color: #3D4956; -fx-background-radius: 15");
        tableThemesPane.setAlignment(Pos.CENTER);
        tableThemesPane.setHgap(25);
        tableThemesPane.setVgap(25);

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

        circles = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Circle circle = new Circle(30);
            circle.setStroke(Color.WHITE);
            circle.setStrokeWidth(3);
            circles.add(circle);
        }

        for (int i = 1; i <= circles.size(); i++) {
            setOnCircleListener(circles.get(i-1), i);
        }

        ballsSettingsPane.getChildren().addAll(ballsPrimaryPane);

        tableThemesPane.addRow(0, circles.get(0), circles.get(1), circles.get(2));
        tableThemesPane.addRow(1, circles.get(3), circles.get(4), circles.get(5));
        tableThemesPane.addRow(2,circles.get(6));



        tableButtonSetListener();


        gamePane.getChildren().addAll(principalBar, ballsSettingsPane, tableThemesPane);

        AnchorPane.setTopAnchor(principalBar, TableView.height/35.0);
        AnchorPane.setRightAnchor(principalBar, 7.);
        AnchorPane.setTopAnchor(ballsSettingsPane, TableView.height/5.5);
        AnchorPane.setRightAnchor(ballsSettingsPane,7.0);
        AnchorPane.setTopAnchor(tableThemesPane, TableView.height/5.5);
        AnchorPane.setRightAnchor(tableThemesPane, 7.0);
    }

    private void setOnCircleListener(Circle circle, int index) {
        circle.setOnMouseClicked(event -> {

            try {
                tableView.getTableImageView().setImage(new Image(new File("src/main/resources/tableImage/" +index+".png").toURI().toURL().toExternalForm()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            for (Circle c: circles) {
                c.setStroke(Color.WHITE);
            }
            circle.setStroke(Color.GREENYELLOW);
        });
    }

    private void createImageViews() throws MalformedURLException {
        menuIconImageView = new ImageView();
        Image menu = new Image(new File("src/main/resources/UI icons/menu_white.png").toURI().toURL().toExternalForm());
        menuIconImageView.setImage(menu);
        menuIconImageView.setFitWidth(TableView.width/36.);
        menuIconImageView.setPreserveRatio(true);

        leftArrowImageView = new ImageView();
        Image back = new Image(new File("src/main/resources/UI icons/simpleArrowLeft_white.png").toURI().toURL().toExternalForm());
        leftArrowImageView.setImage(back);
        leftArrowImageView.setFitWidth(TableView.width/36.);
        leftArrowImageView.setPreserveRatio(true);

        rightArrowImageView = new ImageView();
        Image next = new Image(new File("src/main/resources/UI icons/simpleArrowRight_white.png").toURI().toURL().toExternalForm());
        rightArrowImageView.setImage(next);
        rightArrowImageView.setFitWidth(TableView.width/36.);
        rightArrowImageView.setPreserveRatio(true);

        leaveArrowImageView = new ImageView();
        Image leaveImage = new Image(new File("src/main/resources/UI icons/arrow.png").toURI().toURL().toExternalForm());
        leaveArrowImageView.setImage(leaveImage);
        leaveArrowImageView.setFitWidth(TableView.width/36.);
        leaveArrowImageView.setPreserveRatio(true);
    }

    private void ballsButtonSetOnAction() {
        ballsButton.setOnAction(event -> {
            ballsPrimaryPane.setVisible(!ballsPrimaryPane.isVisible());
            if (tableThemesPane.isVisible()) tableThemesPane.setVisible(false);

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

            if (ballsPrimaryPane.isVisible()) ballsPrimaryPane.setVisible(false);

            if (!tableThemesPane.isVisible()) {
                tableThemesPane.setVisible(true);

                /** creating animation */

                Timeline animation1 = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(0).fillProperty(), Color.SADDLEBROWN)),
                        new KeyFrame(Duration.seconds(1), new KeyValue(circles.get(0).fillProperty(), Color.SADDLEBROWN)),
                        new KeyFrame(Duration.seconds(2.5), new KeyValue(circles.get(0).fillProperty(), Color.YELLOW)),
                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(0).fillProperty(), Color.GREEN))); // at the end of the animation the circle should reach the corners -> diameter = diagonale of rect


                Timeline animation2 = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(1).fillProperty(), Color.BLACK)),
                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(1).fillProperty(), Color.SKYBLUE)));

                Timeline animation3 = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(2).fillProperty(), Color.BLACK)),
                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(2).fillProperty(), Color.GREEN)));

                Timeline animation4 = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(3).fillProperty(), Color.BLUE)),
                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(3).fillProperty(), Color.RED)));

                Timeline animation5 = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(4).fillProperty(), Color.BLUE)),
                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(4).fillProperty(), Color.RED)));

                Timeline animation6 = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(5).fillProperty(), Color.BLUE)),
                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(5).fillProperty(), Color.RED)));

                Timeline animation7 = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(circles.get(6).fillProperty(), Color.BLUE)),
                        new KeyFrame(Duration.seconds(5), new KeyValue(circles.get(6).fillProperty(), Color.RED)));


                animation1.playFromStart();
                animation2.playFromStart();
                animation3.playFromStart();
                animation4.playFromStart();
                animation5.playFromStart();
                animation6.playFromStart();
                animation7.playFromStart();

            } else tableThemesPane.setVisible(false);




        });
    }

    private void menuButtonSetOnAction() {
        menuButton.setOnAction(event -> {

            ballsPrimaryPane.setVisible(false);
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
        this.cueView.getCue().setVisible(visibility);
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

    public GridPane getTableThemesPane() {
        return tableThemesPane;
    }

    public Pane getCueSettingsPane() {
        return cueSettingsPane;
    }

    public Circle getCircleFromSphere(Sphere ball) {
        return new Circle(ball.getLayoutX(),ball.getLayoutY(),ball.getRadius());
    }
}

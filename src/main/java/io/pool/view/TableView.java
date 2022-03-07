package io.pool.view;

import io.pool.controller.MainMenuController;
import io.pool.eightpool.game;
import io.pool.model.BallModel;
import io.pool.model.TableBorderModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class TableView {

    private final BorderPane borderPane;
    private final Pane table; // all components of the table
    private final ArrayList<Circle> holes = new ArrayList<>();
    public final static int cornerHoleRadius = game.eightPoolTableWidth/64;
    public final static int centerHoleRadius = (int) (game.eightPoolTableWidth/76.8);
    private final int width = (int) (game.eightPoolTableWidth/1.777777777); // the width of the pane
    private final int height = (int) (game.eightPoolTableHeight/1.777777777); // the height of the pane

    public TableView(Pane root) throws MalformedURLException {

        double layoutX = game.eightPoolTableX + 0.1*game.eightPoolTableX; // the XPosition in the general pane
        double layoutY = game.eightPoolTableY + 0.1*game.eightPoolTableY; // the YPosition in the general pane

        borderPane = new BorderPane();

        borderPane.setPrefWidth(1500);
        borderPane.setPrefHeight(height);

        table = new Pane();
        table.setPrefWidth(width);
        table.setPrefHeight(height);
        table.setLayoutX(layoutX);
        table.setLayoutY(layoutY);

        Label player1Lbl = new Label("player1");
        player1Lbl.setStyle("-fx-font-size: 24");
        Label player2Lbl = new Label("player2");
        player2Lbl.setStyle("-fx-font-size: 24");
        HBox playersIcon = new HBox(700,player1Lbl,player2Lbl);

        playersIcon.setAlignment(Pos.CENTER);
        playersIcon.setStyle("-fx-border-color: black");
        playersIcon.setPrefHeight(50);

//        Text information = new Text();
//        information.setText("Ball 1: " + "" + "\n" + "Ball 1: " + "" + "\n" + "Ball 2: " + "" + "\n" + "Ball 3: " + ""
//                + "\n" + "Ball 4: " + "" + "\n" + "Ball 5: " + "" + "\n" + "Ball 6: " + "" + "\n" + "Ball 7: " + "" + "\n"
//                + "Ball 8: " + "" + "\n" + "Ball 9: " + "" + "\n" + "Ball 10: " + "" + "\n" + "Ball 11: " + "" + "\n"
//                + "Ball 12: " + "" + "\n" + "Ball 13: " + "" + "\n" + "Ball 14: " + "" + "\n" + "Ball 15: " + ""
//                + "\n" + "Ball 16: " + "" + "\n" + "Ball Cue: " + "" + "\n");
//        information.setStyle("-fx-font-size: 15");
//        Slider sldFriction = new Slider();
//        sldFriction.setShowTickLabels(true);
//        Label sldLblFriction = new Label("Friction");
//        Slider sldForce = new Slider();
//        sldForce.setShowTickLabels(true);
//        Label sldLblForce = new Label("Force");
//        Button goToMenu = new Button("Go to main menu");
//        goToMenu.setOnAction(e->{
//            MainMenuController.gotoMainMenu();
//        });
//        VBox ballInformation = new VBox(information,sldLblForce,sldForce,sldLblFriction,sldFriction,goToMenu);
//
//        ballInformation.setAlignment(Pos.CENTER_LEFT);
//        ballInformation.setPrefHeight(500);
//        ballInformation.setPrefWidth(220);
//        ballInformation.setStyle("-fx-border-color: black");
//
//        borderPane.setRight(ballInformation);
//        borderPane.setTop(playersIcon);
//        borderPane.setCenter(table);

        VBox rightContainer = new VBox();
        rightContainer.setSpacing(25);
        rightContainer.setAlignment(Pos.TOP_CENTER);

        HBox principalBar = new HBox();
        principalBar.setAlignment(Pos.CENTER_LEFT);
        principalBar.setSpacing(25);
        principalBar.setMaxHeight(75);
        principalBar.setPrefWidth(350);
        principalBar.setStyle("-fx-background-color: #3D4956");

        Button backButton, ballsButton, tableButton, cueButton;

        ImageView leaveArrowImageView = new ImageView();
        Image leaveImage = new Image(new File("src/main/resources/UI icons/arrow.png").toURI().toURL().toExternalForm());
        leaveArrowImageView.setImage(leaveImage);
        leaveArrowImageView.setFitWidth(30);
        leaveArrowImageView.setPreserveRatio(true);
        backButton = new Button("");
        backButton.setGraphic(leaveArrowImageView);
        backButton.setContentDisplay(ContentDisplay.CENTER);
        backButton.setStyle("-fx-background-color: red");
        backButton.setMaxWidth(30);
        backButton.setMaxHeight(75);

        ballsButton = new Button("Balls");
        ballsButton.setTextFill(Color.WHITE);
        ballsButton.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        ballsButton.setStyle("-fx-background-color: transparent");

        tableButton = new Button("Table");
        tableButton.setTextFill(Color.WHITE);
        tableButton.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        tableButton.setStyle("-fx-background-color: transparent");

        cueButton = new Button("Cue");
        cueButton.setTextFill(Color.WHITE);
        cueButton.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        cueButton.setStyle("-fx-background-color: transparent");

        principalBar.getChildren().addAll(backButton, ballsButton, tableButton, cueButton);
        ///////

        FlowPane ballsPane = new FlowPane();
        ballsPane.setPadding(new Insets(30,30,30,30));
        ballsPane.setAlignment(Pos.CENTER);
        ballsPane.setStyle("-fx-background-color: #3D4956");
        ballsPane.setVgap(25);
        ballsPane.setHgap(38);

        for (int i=1;i<16;i++) {
            BallModel bModel;
            BallView bView;
            bModel = new BallModel(i, new Image(new File("src/main/resources/billiards3D/ball" + i + ".jpg").toURI().toURL().toExternalForm()));
            bView = new BallView(bModel.getImg(),BallModel.RADIUS);
            ballsPane.getChildren().add(bView.getBall());
        }


        rightContainer.getChildren().addAll(principalBar, ballsPane);

        borderPane.setRight(rightContainer);
        borderPane.setTop(playersIcon);
        borderPane.setCenter(table);

        ImageView tableImageView = new ImageView(); // the image view of the table
        Image image = new Image(new File("src/main/resources/tableImage/finalTable.png").toURI().toURL().toExternalForm());
        tableImageView.setImage(image);
        tableImageView.setFitWidth(width);
        tableImageView.setPreserveRatio(true);

        Line whiteLine = new Line();
        whiteLine.setStartX(width-(width/3.6));
        whiteLine.setStartY(height/8.934);
        whiteLine.setEndX(width-(width/3.6));
        whiteLine.setEndY(height-(height/8.4375));
        whiteLine.setStroke(Color.WHITE);
        whiteLine.setStrokeWidth(3);

        table.getChildren().addAll(tableImageView);//, whiteLine); // adding the components to the table

        root.getChildren().add(borderPane); // adding the table to the main pain of the project.

        createHoles();
        createLines();

    }
    public void createHoles(){
        Circle upLeftCorner = new Circle();
        upLeftCorner.setRadius(cornerHoleRadius);
        upLeftCorner.setCenterX(48);
        upLeftCorner.setCenterY(55);
        upLeftCorner.setFill(Color.BLUE);
        //upLeftCorner.setVisible(false);
        holes.add(upLeftCorner);

        Circle downLeftCorner = new Circle();
        downLeftCorner.setRadius(cornerHoleRadius);
        downLeftCorner.setCenterX(48);
        downLeftCorner.setCenterY(height-57);
        downLeftCorner.setFill(Color.BLUE);
//        downLeftCorner.setVisible(false);
        holes.add(downLeftCorner);

        Circle upRightCorner = new Circle();
        upRightCorner.setRadius(cornerHoleRadius);
        upRightCorner.setCenterX(width-58);
        upRightCorner.setCenterY(55);
        upRightCorner.setFill(Color.BLUE);
        //upRightCorner.setVisible(false);
        holes.add(upRightCorner);

        Circle downRightCorner = new Circle();
        downRightCorner.setRadius(cornerHoleRadius);
        downRightCorner.setCenterX(width - 58);
        downRightCorner.setCenterY(height- 57);
        downRightCorner.setFill(Color.BLUE);
        //downRightCorner.setVisible(false);
        holes.add(downRightCorner);

        Circle upCenterCorner = new Circle();
        upCenterCorner.setRadius(centerHoleRadius);
        upCenterCorner.setCenterX((width/2)-7);
        upCenterCorner.setCenterY(41);
        upCenterCorner.setFill(Color.BLUE);
        //upCenterCorner.setVisible(false);
        holes.add(upCenterCorner);

        Circle downCenterCorner = new Circle();
        downCenterCorner.setRadius(centerHoleRadius);
        downCenterCorner.setCenterX((width/2) - 7);
        downCenterCorner.setCenterY(height-47);
        //downCenterCorner.setVisible(false);
        downCenterCorner.setFill(Color.BLUE);
        holes.add(downCenterCorner);

        table.getChildren().addAll(holes);

    }
    public void createLines(){
        //TODO Add the rest of the lines for the rest of the table border lines
        TableBorderModel upLeftLine = new TableBorderModel(95, 65, 500, 65,1,-1);
        upLeftLine.setStroke(Color.WHITE);
        upLeftLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(upLeftLine);
        //lines.add(upLeftLine);

        TableBorderModel upRightLine = new TableBorderModel(565, 65, 975, 65,1,-1);
        upRightLine.setStroke(Color.WHITE);
        upRightLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(upRightLine);

        TableBorderModel downLeftLine = new TableBorderModel(95, height-70, 500, height-70,1,-1);
        downLeftLine.setStroke(Color.WHITE);
        downLeftLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(downLeftLine);

        TableBorderModel downRightLine = new TableBorderModel(565, height-70, 975, height-70,1,-1);
        downRightLine.setStroke(Color.WHITE);
        downRightLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(downRightLine);

        TableBorderModel centerLeftLine = new TableBorderModel(65, 103, 65, height-110,-1,1);
        centerLeftLine.setStroke(Color.WHITE);
        centerLeftLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(centerLeftLine);

        TableBorderModel centerRightLine = new TableBorderModel(1010, 103, 1010, height-110,-1,1);
        centerRightLine.setStroke(Color.WHITE);
        centerRightLine.setStrokeWidth(3);
        TableBorderModel.addTableBorders(centerRightLine);



        table.getChildren().addAll(TableBorderModel.tableBorder);
    }
    public Pane getFullTable() {
        return table;
    }

    public ArrayList<Circle> getHoles() {
        return holes;
    }
}


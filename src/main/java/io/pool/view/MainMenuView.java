package io.pool.view;

import io.pool.controller.MainMenuController;
import io.pool.eightpool.ResourcesLoader;
import io.pool.model.PlayerModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;


public class MainMenuView extends GridPane{

    private Button backBtn;
    private Button PVPstartBtn;
    private BorderPane pvpRootMenu;
    private Scene pvpSubMenu;
    private Text pvpText;
    private Text versusAIText;
    private Group buttonGroup;
    private Polygon p3Sub;
    private Polygon p3Sub1;
    private Polygon p3Sub2;
    Rectangle2D screen = Screen.getPrimary().getBounds();
    private final double screenWidth = screen.getWidth();
    private final double screenHeight = screen.getHeight();
    private Polygon p2Sub;
    private Polygon p1,p2,p3;
    private MenuBar menuBar;
    private Menu menuFile;
    private Menu menuEdit;
    private Menu menuHelp;
    private MenuItem menuItem1;
    private MenuItem menuItem2;
    private MenuItem menuItem3;
    private MenuItem menuItem4;
    private MainMenuController mainMenuController;
    private Button solo1Btn;
    private Button pvp1Btn;
    private Button pve1Btn;
    private Button pve2Btn;
    private Button pve3Btn;
    private Text soloText;
    private ComboBox comboBoxP1;
    private ComboBox comboBoxP2;
    private Pane secondRowPane;

    public static ObservableList<PlayerModel> Player1List = FXCollections.observableArrayList();
    public static ObservableList<PlayerModel> Player2List = FXCollections.observableArrayList();
    public final static ObservableList<String> aiList = FXCollections.observableArrayList("Easy AI","Medium AI","Hard AI");


    public MainMenuView(Stage stage) throws IOException {
        this.setStyle("-fx-background-color: White");
        this.getColumnConstraints().add(new ColumnConstraints());
        this.prefHeight(this.getScreenHeight());
        this.prefWidth(this.getScreenWidth());

        this.getRowConstraints().add(new RowConstraints());
        this.getRowConstraints().add(new RowConstraints());

        //Adding texture
        //Green

        //Menu Bar
        menuBar = new MenuBar();
        menuBar.setMinWidth(screenWidth);
        menuBar.setMinHeight(screenHeight*0.035);
        //menuBar.setStyle("-fx-background-color: transparent");

        //Menu File
        menuFile = new Menu("File");
        //MenuFile Items
        menuItem1 = new MenuItem("Close");
        menuItem1.setOnAction(e->{
            Platform.exit();
        });
        menuFile.getItems().addAll(menuItem1);

        //Menu Edit
        menuEdit = new Menu("Edit");
        menuItem2 = new MenuItem("Game Settings");
        menuItem2.setOnAction(e->{
            mainMenuController.gotoSettings();
        });
        menuEdit.getItems().add(menuItem2);
        //Menu Help
        menuHelp = new Menu("About");
        menuItem3 = new MenuItem("Info");
        menuItem3.setOnAction(e->{
            mainMenuController.gotoHelp();
        });
        menuHelp.getItems().add(menuItem3);
        menuBar.getMenus().addAll(menuFile,menuEdit,menuHelp);
        this.getChildren().add(menuBar);
        this.setRowIndex(menuBar, 0);

        secondRowPane = new Pane();
        this.getChildren().add(secondRowPane);
        this.setRowIndex(secondRowPane, 1);

        p1 = new Polygon();
        secondRowPane.getChildren().add(p1);
        p1.getPoints().addAll(0.0, 0.0,
                this.getScreenWidth()/3, 0.0,
                this.getScreenWidth()/4, this.getScreenHeight(),
                0.0, this.getScreenHeight());

        ImagePattern tableTexturePattern;
        tableTexturePattern = new ImagePattern(ResourcesLoader.tableTextures.get(0));
        p1.setFill(tableTexturePattern);
        //Red
        p2 = new Polygon();
        secondRowPane.getChildren().add(p2);
        p2.getPoints().addAll(this.getScreenWidth()/3, 0.0,
                this.getScreenWidth()*2/3, 0.0,
                this.getScreenWidth()*7/12, this.getScreenHeight(),
                this.getScreenWidth()/4, this.getScreenHeight());

        ImagePattern RedtableTexturePattern;
        RedtableTexturePattern = new ImagePattern(ResourcesLoader.tableTextures.get(1));
        p2.setFill(RedtableTexturePattern);

        //Blue
        p3 = new Polygon();
        secondRowPane.getChildren().add(p3);
        p3.getPoints().addAll(this.getScreenWidth()*2/3, 0.0,
                this.getScreenWidth(), 0.0,
                this.getScreenWidth(), this.getScreenHeight(),
                this.getScreenWidth()*7/12, this.getScreenHeight());

        ImagePattern BluetableTexturePattern;
        BluetableTexturePattern = new ImagePattern(ResourcesLoader.tableTextures.get(2));
        p3.setFill(BluetableTexturePattern);

        //adding text
        Font font = new Font(this.getScreenWidth()*0.05);
        soloText = new Text();
        soloText.setText("SOLO");
        soloText.setFont(font);
        secondRowPane.getChildren().add(soloText);
        soloText.setLayoutX(this.getScreenWidth()*0.08);
        soloText.setLayoutY(this.getScreenHeight()*0.45);

        pvpText = new Text();
        pvpText.setText("P\n  v\n    P");
        pvpText.setFont(font);
        secondRowPane.getChildren().add(pvpText);
        pvpText.setLayoutX(this.getScreenWidth()*0.42);
        pvpText.setLayoutY(this.getScreenHeight()*0.35);

        versusAIText = new Text();
        versusAIText.setText("CHALLENGE\n    THE AI");
        versusAIText.setFont(font);
        secondRowPane.getChildren().add(versusAIText);
        versusAIText.setLayoutX(this.getScreenWidth()*0.68);
        versusAIText.setLayoutY(this.getScreenHeight()*0.4);



        //Polygon Test
        p3Sub = new Polygon();
        //this.getChildren().add(p3Sub);
        p3Sub.getPoints().addAll(
                this.getScreenWidth()*2/3, 0.0,
                this.getScreenWidth(), 0.0,
                this.getScreenWidth(), this.getScreenHeight()/3,
                this.getScreenWidth()*63.89/100, this.getScreenHeight()/3);

        p3Sub1 = new Polygon();
        //this.getChildren().add(p3Sub1);
        p3Sub1.getPoints().addAll(
                this.getScreenWidth()*63.89/100,this.getScreenHeight()/3,
                this.getScreenWidth(),this.getScreenHeight()/3,
                this.getScreenWidth(),this.getScreenHeight()*2/3,
                this.getScreenWidth()*61.1/100,this.getScreenHeight()*2/3);

        p3Sub2 = new Polygon();
        //this.getChildren().add(p3Sub2);
        p3Sub2.getPoints().addAll(
                this.getScreenWidth()*61.1/100,this.getScreenHeight()*2/3,
                this.getScreenWidth(),this.getScreenHeight()*2/3,
                this.getScreenWidth(),this.getScreenHeight(),
                this.getScreenWidth()*58.37/100,this.getScreenHeight());



        /**
         * Buttons Hover for each polygon
         */

        // Solo Button 1
        solo1Btn = new Button();
        solo1Btn.setFont(font);
        solo1Btn.setStyle("-fx-background-color: rgba(255,255,255,.3)");
        solo1Btn.setShape(p1);
        solo1Btn.setPrefSize(getScreenWidth()/3,getScreenHeight());

        // PVP Button 1
        pvp1Btn = new Button();
        pvp1Btn.setShape(p2);
        pvp1Btn.setStyle("-fx-background-color: rgba(255,255,255,.3)");
        pvp1Btn.setFont(font);
        pvp1Btn.setPrefSize(p2.getLayoutBounds().getWidth(),p2.getLayoutBounds().getHeight());
        pvp1Btn.setLayoutX(p2.getLayoutBounds().getMinX());


        // PVE Button 1
        pve1Btn = new Button("EASY");
        pve1Btn.setFont(font);
        pve1Btn.setStyle("-fx-background-color: rgba(255,255,255,.3)");
        pve1Btn.setShape(p3Sub);
        pve1Btn.setPrefWidth(p3Sub.getLayoutBounds().getWidth());
        pve1Btn.setPrefHeight(p3Sub.getLayoutBounds().getHeight());
        pve1Btn.setLayoutX(p3Sub.getLayoutBounds().getMinX());

        // PVE Button 2
        pve2Btn = new Button("MEDIUM");
        pve2Btn.setFont(font);
        pve2Btn.setStyle("-fx-background-color: rgba(255,255,255,.3)");
        pve2Btn.setShape(p3Sub1);
        pve2Btn.setPrefWidth(p3Sub1.getLayoutBounds().getWidth());
        pve2Btn.setPrefHeight(p3Sub1.getLayoutBounds().getHeight());
        pve2Btn.setLayoutX(p3Sub1.getLayoutBounds().getMinX());
        pve2Btn.setLayoutY(p3Sub1.getLayoutBounds().getMinY());

        // PVE Button 3
        pve3Btn = new Button("HARD");
        pve3Btn.setFont(font);
        pve3Btn.setStyle("-fx-background-color: rgba(255,255,255,.3)");
        pve3Btn.setShape(p3Sub2);
        pve3Btn.setPrefWidth(p3Sub2.getLayoutBounds().getWidth());
        pve3Btn.setPrefHeight(p3Sub2.getLayoutBounds().getHeight());
        pve3Btn.setLayoutX(p3Sub2.getLayoutBounds().getMinX());
        pve3Btn.setLayoutY(p3Sub2.getLayoutBounds().getMinY());

        buttonGroup = new Group();
        buttonGroup.getChildren().addAll(pve1Btn,pve2Btn,pve3Btn);

        PVPstartBtn = new Button("Start");

        mainMenuController = new MainMenuController(this, stage);

        // PVP Sub MENU
        pvpRootMenu = new BorderPane();

        // Top Border Pane
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.BOTTOM_CENTER);
        topBox.setSpacing(500);
        topBox.setMinHeight(400);
        Label player2Lbl = new Label("Player 2");
        player2Lbl.setFont(new Font(20));
        Label player1Lbl = new Label("Player 1");
        player1Lbl.setFont(new Font(20));

        // Center Border Pane
        HBox centerBox = new HBox();
        centerBox.setPadding(new Insets(20));
        centerBox.setMinHeight(100);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setSpacing(30);

        // ComboBox Player 1

        comboBoxP1 = new ComboBox();
        comboBoxP2 = new ComboBox();


        Player1List = FXCollections.observableArrayList(PlayerModel.playersList);

        comboBoxP1.setItems(Player1List);
        comboBoxP1.setOnAction(e->{
            if(mainMenuController.isPVP()) {
                comboBoxP2.setDisable(false);
                PVPstartBtn.setDisable(true);
                comboBoxP2.getItems().clear();
                Player2List = FXCollections.observableArrayList(PlayerModel.playersList);
                Player2List.remove(comboBoxP1.getSelectionModel().getSelectedItem());
                comboBoxP2.setItems(Player2List);
            }else{
                PVPstartBtn.setDisable(false);
            }
        });

        comboBoxP1.setPrefSize(300,50);
        DropShadow dp1 = new DropShadow();
        dp1.setColor(Color.RED);
        dp1.setHeight(50);
        comboBoxP1.setEffect(dp1);
        comboBoxP1.setStyle("-fx-background-color: #78282b");
        backBtn = new Button("BACK");
        backBtn.setPrefSize(300,50);
        backBtn.setOnAction(event -> {
            stage.getScene().setRoot(this);
        });

        //Bottom Border Pane
        HBox bottomBox = new HBox();
        bottomBox.setMinHeight(700);
        bottomBox.setAlignment(Pos.TOP_CENTER);
        bottomBox.setSpacing(30);

        // ComboBox Player 2

        Player2List = FXCollections.observableArrayList(PlayerModel.playersList);


        comboBoxP2.setOnAction(e->{
            if(comboBoxP2.getSelectionModel().getSelectedIndex()!=-1&& mainMenuController.isPVP()){
                PVPstartBtn.setDisable(false);
            }else if(!mainMenuController.isPVP()){
                int i = comboBoxP2.getSelectionModel().getSelectedIndex();
                MainMenuController.getGameView().getGameController().setGameType(i+2);
            }
        });
        comboBoxP2.setDisable(false);
        PVPstartBtn.setDisable(true);

        comboBoxP2.setItems(Player2List);
        comboBoxP2.getSelectionModel().select(0);

        comboBoxP2.setPrefSize(300,50);
        DropShadow dp2 = new DropShadow();
        dp2.setColor(Color.BLUE);
        dp2.setHeight(50);
        comboBoxP2.setEffect(dp2);
        comboBoxP2.setStyle("-fx-background-color: blue");
        PVPstartBtn.setPrefSize(300,50);

        topBox.getChildren().addAll(player1Lbl,player2Lbl);
        centerBox.getChildren().addAll(comboBoxP1,comboBoxP2);
        bottomBox.getChildren().addAll(PVPstartBtn,backBtn);
        pvpRootMenu.setStyle("-fx-background-color: #2b7828");
        pvpRootMenu.setTop(topBox);
        pvpRootMenu.setCenter(centerBox);
        pvpRootMenu.setBottom(bottomBox);
//        menuBar.toFront();
//        this.getChildren().add(menuBar);

    }

    public Button getBackBtn() {
        return backBtn;
    }

    public Button getPVPstartBtn() {
        return PVPstartBtn;
    }

    public ComboBox getComboBoxP1() {
        return comboBoxP1;
    }

    public ComboBox getComboBoxP2() {
        return comboBoxP2;
    }

    public BorderPane getPvpRootMenu() {
        return pvpRootMenu;
    }

    public Text getPvpText() {
        return pvpText;
    }

    public void setPvpText(Text pvpText) {
        this.pvpText = pvpText;
    }

    public Text getVersusAIText() {
        return versusAIText;
    }

    public void setVersusAIText(Text versusAIText) {
        this.versusAIText = versusAIText;
    }

    public Group getButtonGroup() {
        return buttonGroup;
    }

    public void setButtonGroup(Group buttonGroup) {
        this.buttonGroup = buttonGroup;
    }

    public Polygon getP3Sub() {
        return p3Sub;
    }

    public void setP3Sub(Polygon p3Sub) {
        this.p3Sub = p3Sub;
    }

    public Polygon getP3Sub1() {
        return p3Sub1;
    }

    public void setP3Sub1(Polygon p3Sub1) {
        this.p3Sub1 = p3Sub1;
    }

    public Polygon getP3Sub2() {
        return p3Sub2;
    }

    public void setP3Sub2(Polygon p3Sub2) {
        this.p3Sub2 = p3Sub2;
    }

    public Button getSolo1Btn() {
        return solo1Btn;
    }

    public void setSolo1Btn(Button solo1Btn) {
        this.solo1Btn = solo1Btn;
    }

    public Button getPvp1Btn() {
        return pvp1Btn;
    }

    public void setPvp1Btn(Button pvp1Btn) {
        this.pvp1Btn = pvp1Btn;
    }

    public Button getPve1Btn() {
        return pve1Btn;
    }

    public void setPve1Btn(Button pve1Btn) {
        this.pve1Btn = pve1Btn;
    }

    public Button getPve2Btn() {
        return pve2Btn;
    }

    public void setPve2Btn(Button pve2Btn) {
        this.pve2Btn = pve2Btn;
    }

    public Button getPve3Btn() {
        return pve3Btn;
    }

    public void setPve3Btn(Button pve3Btn) {
        this.pve3Btn = pve3Btn;
    }

    public Menu getMenuFile() {
        return menuFile;
    }

    public void setMenuFile(Menu menuFile) {
        this.menuFile = menuFile;
    }

    public Menu getMenuEdit() {
        return menuEdit;
    }

    public void setMenuEdit(Menu menuEdit) {
        this.menuEdit = menuEdit;
    }

    public Menu getMenuHelp() {
        return menuHelp;
    }

    public void setMenuHelp(Menu menuHelp) {
        this.menuHelp = menuHelp;
    }

    public MenuItem getMenuItem1() {
        return menuItem1;
    }

    public void setMenuItem1(MenuItem menuItem1) {
        this.menuItem1 = menuItem1;
    }

    public MenuItem getMenuItem2() {
        return menuItem2;
    }

    public void setMenuItem2(MenuItem menuItem2) {
        this.menuItem2 = menuItem2;
    }

    public MenuItem getMenuItem3() {
        return menuItem3;
    }

    public void setMenuItem3(MenuItem menuItem3) {
        this.menuItem3 = menuItem3;
    }

    public MenuItem getMenuItem4() {
        return menuItem4;
    }

    public void setMenuItem4(MenuItem menuItem4) {
        this.menuItem4 = menuItem4;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public Polygon getP1() {
        return p1;
    }

    public void setP1(Polygon p1) {
        this.p1 = p1;
    }

    public Polygon getP2() {
        return p2;
    }

    public void setP2(Polygon p2) {
        this.p2 = p2;
    }

    public Polygon getP3() {
        return p3;
    }

    public void setP3(Polygon p3) {
        this.p3 = p3;
    }

    public Polygon getP2Sub() {
        return p2Sub;
    }

    public void setP2Sub(Polygon p2Sub) {
        this.p2Sub = p2Sub;
    }

    public Text getSoloText() {
        return soloText;
    }

    public void setSoloText(Text soloText) {
        this.soloText = soloText;
    }

    public double getScreenWidth() {return screenWidth;}

    public double getScreenHeight() {return screenHeight;}

    public void setController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    public MainMenuController getMainMenuController() {
        return this.mainMenuController;
    }

    public Pane getSecondRowPane() {
        return secondRowPane;
    }

    public void setSecondRowPane(Pane secondRowPane) {
        this.secondRowPane = secondRowPane;
    }
}

package io.pool.view;

import io.pool.controller.MainMenuController;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class MainMenuView extends Pane{

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

    public MainMenuView(Stage stage) throws IOException {
        this.setStyle("-fx-background-color: White");
        mainMenuController = new MainMenuController(this,stage);
        //Adding texture
        //Green
        p1 = new Polygon();
        this.getChildren().add(p1);
        p1.getPoints().addAll(0.0, 0.0,
                this.getScreenWidth()/3, 0.0,
                this.getScreenWidth()/4, this.getScreenHeight(),
                0.0, this.getScreenHeight());

        Image tableTextureImage;
        ImagePattern tableTexturePattern;
        try {
            InputStream tableTextureStream = new FileInputStream("src/main/resources/MainMenu/TableTexture.jpg");
            tableTextureImage = new Image(tableTextureStream);
            tableTexturePattern = new ImagePattern(tableTextureImage);
            p1.setFill(tableTexturePattern);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Red
        p2 = new Polygon();
        this.getChildren().add(p2);
        p2.getPoints().addAll(this.getScreenWidth()/3, 0.0,
                this.getScreenWidth()*2/3, 0.0,
                this.getScreenWidth()*7/12, this.getScreenHeight(),
                this.getScreenWidth()/4, this.getScreenHeight());

        Image RedtableTextureImage;
        ImagePattern RedtableTexturePattern;
        try {
            InputStream RedtableTextureStream = new FileInputStream("src/main/resources/MainMenu/RedTableTexture.jpg");
            RedtableTextureImage = new Image(RedtableTextureStream);
            RedtableTexturePattern = new ImagePattern(RedtableTextureImage);
            p2.setFill(RedtableTexturePattern);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Blue
        p3 = new Polygon();
        this.getChildren().add(p3);
        p3.getPoints().addAll(this.getScreenWidth()*2/3, 0.0,
                this.getScreenWidth(), 0.0,
                this.getScreenWidth(), this.getScreenHeight(),
                this.getScreenWidth()*7/12, this.getScreenHeight());

        Image BluetableTextureImage;
        ImagePattern BluetableTexturePattern;
        try {
            InputStream BluetableTextureStream = new FileInputStream("src/main/resources/MainMenu/BlueTableTexture.jpg");
            BluetableTextureImage = new Image(BluetableTextureStream);
            BluetableTexturePattern = new ImagePattern(BluetableTextureImage);
            p3.setFill(BluetableTexturePattern);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //adding text
        Font font = new Font(this.getScreenWidth()*0.05);
        soloText = new Text();
        soloText.setText("SOLO");
        soloText.setFont(font);
        this.getChildren().add(soloText);
        soloText.setLayoutX(this.getScreenWidth()*0.08);
        soloText.setLayoutY(this.getScreenHeight()*0.45);

        pvpText = new Text();
        pvpText.setText("P\n  v\n    P");
        pvpText.setFont(font);
        this.getChildren().add(pvpText);
        pvpText.setLayoutX(this.getScreenWidth()*0.42);
        pvpText.setLayoutY(this.getScreenHeight()*0.35);

        versusAIText = new Text();
        versusAIText.setText("CHALLENGE\n    THE AI");
        versusAIText.setFont(font);
        this.getChildren().add(versusAIText);
        versusAIText.setLayoutX(this.getScreenWidth()*0.68);
        versusAIText.setLayoutY(this.getScreenHeight()*0.4);

        //Menu Bar
        menuBar = new MenuBar();
        menuBar.setMinSize(100,10);
        menuBar.setStyle("-fx-background-color: transparent");
        this.getChildren().add(menuBar);
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
        //MenuEdit Items
        menuItem3 = new MenuItem("Game Settings");
        menuItem3.setOnAction(e->{
            mainMenuController.gotoSettings();
        });
        menuItem4 = new MenuItem("Profiles");
        menuEdit.getItems().addAll(menuItem3,menuItem4);
        //Menu Help
        menuHelp = new Menu("About");
        menuBar.getMenus().addAll(menuFile,menuEdit,menuHelp);

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

        initializeViews();

    }

    private void initializeViews() throws IOException {
        this.mainMenuController.setSettingsView(new SettingsView());
        this.mainMenuController.setGameView(new GameView());
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
}

package io.pool.view;

import io.pool.controller.MainMenuController;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class MainMenuView extends Pane{

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

    public MainMenuView(){
        this.setStyle("-fx-background-color: White");

        /*
        //Separating regions
        Path path1 = new Path();
        MoveTo mt1 = new MoveTo(this.getScreenWidth()/3, 0);
        LineTo line1 = new LineTo(this.getScreenWidth()/4, this.getScreenHeight());
        path1.getElements().add(mt1);
        path1.getElements().add(line1);
        this.getChildren().add(path1);

        Path path2 = new Path();
        MoveTo mt2 = new MoveTo(this.getScreenWidth()*2/3, 0);
        LineTo line2 = new LineTo(this.getScreenWidth()*7/12, this.getScreenHeight());
        path2.getElements().add(mt2);
        path2.getElements().add(line2);
        this.getChildren().add(path2);
         */

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
            InputStream tableTextureStream = new FileInputStream("resources/MainMenu/TableTexture.jpg");
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
            InputStream RedtableTextureStream = new FileInputStream("resources/MainMenu/RedTableTexture.jpg");
            RedtableTextureImage = new Image(RedtableTextureStream);
            RedtableTexturePattern = new ImagePattern(RedtableTextureImage);
            p2.setFill(RedtableTexturePattern);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        p2Sub = new Polygon();
        p2Sub.getPoints().addAll(this.getScreenWidth()/3, 0.0,
                this.getScreenWidth()*2/3, 0.0,
                this.getScreenWidth()*7/12, this.getScreenHeight(),
                this.getScreenWidth()/4, this.getScreenHeight());
        try {
            InputStream RedtableTextureStream = new FileInputStream("resources/MainMenu/RedTableTexture.jpg");
            RedtableTextureImage = new Image(RedtableTextureStream);
            RedtableTexturePattern = new ImagePattern(RedtableTextureImage);
            p2Sub.setFill(RedtableTexturePattern);
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
            InputStream BluetableTextureStream = new FileInputStream("resources/MainMenu/BlueTableTexture.jpg");
            BluetableTextureImage = new Image(BluetableTextureStream);
            BluetableTexturePattern = new ImagePattern(BluetableTextureImage);
            p3.setFill(BluetableTexturePattern);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //adding text
        Font font = new Font(this.getScreenWidth()*0.05);
        Text soloText = new Text();
        soloText.setText("SOLO");
        soloText.setFont(font);
        this.getChildren().add(soloText);
        soloText.setLayoutX(this.getScreenWidth()*0.08);
        soloText.setLayoutY(this.getScreenHeight()*0.45);

        Text pvpText = new Text();
        pvpText.setText("P\n  v\n    P");
        pvpText.setFont(font);
        this.getChildren().add(pvpText);
        pvpText.setLayoutX(this.getScreenWidth()*0.42);
        pvpText.setLayoutY(this.getScreenHeight()*0.35);

        Text versusAIText = new Text();
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

    public double getScreenWidth() {return screenWidth;}

    public double getScreenHeight() {return screenHeight;}

    public void setController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }
}

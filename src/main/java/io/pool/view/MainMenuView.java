package io.pool.view;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class MainMenuView extends Pane{

    Rectangle2D screen = Screen.getPrimary().getBounds();
    private final double screenWidth = screen.getWidth();
    private final double screenHeight = screen.getHeight();


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
        Polygon p1 = new Polygon();
        this.getChildren().add(p1);
        p1.getPoints().addAll(new Double[]{
                0.0 , 0.0,
                this.getScreenWidth()/3, 0.0,
                this.getScreenWidth()/4, this.getScreenHeight(),
                0.0, this.getScreenHeight()
        });

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
        Polygon p2 = new Polygon();
        this.getChildren().add(p2);
        p2.getPoints().addAll(new Double[]{
                this.getScreenWidth()/3 , 0.0,
                this.getScreenWidth()*2/3, 0.0,
                this.getScreenWidth()*7/12, this.getScreenHeight(),
                this.getScreenWidth()/4, this.getScreenHeight()
        });

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

        //Blue
        Polygon p3 = new Polygon();
        this.getChildren().add(p3);
        p3.getPoints().addAll(new Double[]{
                this.getScreenWidth()*2/3 , 0.0,
                this.getScreenWidth(), 0.0,
                this.getScreenWidth(), this.getScreenHeight(),
                this.getScreenWidth()*7/12, this.getScreenHeight()
        });

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

    }


    public double getScreenWidth() {return screenWidth;}

    public double getScreenHeight() {return screenHeight;}
}

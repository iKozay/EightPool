package io.pool.view;

import io.pool.controller.BallController;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class BallView {
    BallController ballController;
    private Sphere ball;
    public BallView(BallController ballController, Image img, int radius) {
        this.ballController = ballController;
        ball = new Sphere(radius);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(img);
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(30);
        ball.setMaterial(material);
    }


    public Sphere getBall() {
        return ball;
    }
}

package io.pool.view;

import io.pool.model.BallModel;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;

public class BallView {
    /**
     * The sphere that represents the BallModel
     * */
    private Sphere ball;
    private Circle ballCircle;
    /**
     * Main constructor of BallView
     * @param img The image that will be applied on the sphere
     * @param radius The radius of the sphere
     * */
    public BallView(Image img, int radius) {
        ball = new Sphere(radius);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(img);
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(100);
        ball.setMaterial(material);
        ballCircle = new Circle(ball.getRadius());
    }

    /**
     * Gets the sphere
     * @return The sphere
     * */
    public Sphere getBall() {
        return ball;
    }
    public void setBallNull() {
        this.ball=null;
    }

    /**
     * Gets the sphere
     * @return The sphere
     * */
    public Circle getCircleFromSphere() {
        ballCircle.setCenterX(ball.getLayoutX());
        ballCircle.setCenterY(ball.getLayoutY());
        return ballCircle;
    }
}

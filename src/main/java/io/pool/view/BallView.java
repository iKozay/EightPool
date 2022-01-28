package io.pool.view;

import io.pool.controller.BallController;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class BallView {
    private BallController ballController;
    private Circle ball;
    public BallView(BallController ballController, Paint color, int radius) {
        this.ballController = ballController;
        ball = new Circle(radius);
        ball.setFill(color);
    }


    public Circle getBall() {
        return ball;
    }
}

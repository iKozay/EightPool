package io.pool.view;

import io.pool.controller.BallController;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BallView extends Pane {
    private BallController ballController;
    private Circle ball;
    public BallView() {
        ballController = new BallController(this);
        ball = new Circle(ballController.getbModel().getRadius());
        ball.setFill(Color.RED);
        this.getChildren().add(ball);
    }

    public Circle getBall() {
        return ball;
    }

    public BallController getBallController() {
        return ballController;
    }
}

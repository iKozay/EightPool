package io.pool.model;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


public class BallModel extends Circle {

    int number;
    Paint color;

    public BallModel(int number){
        Circle circle = new Circle(30);

    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}

package io.pool.view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Corner {
    private int radius;
    private int length;
    private Circle circle;
    private Rectangle rectangle;
    private Paint color;

    public Corner(Pane root, int radius, int length, int circleCenterX, int circleCenterY, int angle) {

        this.radius = radius;
        this.length = length;

        circle = new Circle(radius);
        circle.setFill(Color.BLUE);
        circle.setCenterX(circleCenterX);
        circle.setCenterY(circleCenterY);

        rectangle = new Rectangle(length, radius*2);
        rectangle.setFill(Color.BLUE);
        rectangle.setX(circleCenterX);
        rectangle.setY(circleCenterY - radius);


        Rotate rotate = new Rotate();
        rotate.setAngle(-angle);
        rotate.setPivotX(circleCenterX);
        rotate.setPivotY(circleCenterY);

        rectangle.getTransforms().addAll(rotate);

        root.getChildren().addAll(circle, rectangle);
    }

    public void setColor(Paint color) {
        circle.setFill(color);
        rectangle.setFill(color);
        this.color = color;
    }

    public Paint getColor() {
        return color;
    }

    public int getRadius() {
        return radius;
    }

    public int getLength() {
        return length;
    }
}

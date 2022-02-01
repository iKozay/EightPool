package io.pool.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TableModel {
    private int width;
    private int height;
    private Circle hole;
    private double friction;
    private Color color;

    public TableModel(int width, int height, double friction, Color color) {
        this.width = width;
        this.height = height;
        this.friction = friction;
        this.color = color;

    }
    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }

    public Circle getHole() {
        return hole;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}

package io.pool.model;

public class VelocityVector {
    private double x;
    private double y;
    // TODO I just added these. I think they will be useful
    private double angle;
    private double vector;
    private double magnitude;

    public VelocityVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(VelocityVector vector){
        this.x += vector.getX();
        this.y += vector.getY();
    }
    public void mul(VelocityVector vector){
        this.x *= vector.getX();
        this.y *= vector.getY();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}

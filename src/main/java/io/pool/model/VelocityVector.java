package io.pool.model;

public class VelocityVector {
    private double x;
    private double y;
    private double angle;
    private double direction;
    private double magnitude;

    public VelocityVector(double x, double y) {
        this.x = x;
        this.y = y;
        updateVector();
    }

    /*
    public void add(VelocityVector vector){
        this.x += vector.getX();
        this.y += vector.getY();
        updateVector();
    }
     */
    public VelocityVector add(VelocityVector vector){
        this.x += vector.getX();
        this.y += vector.getY();
        return new VelocityVector(this.x, this.y);
    }
    public void mul(VelocityVector vector){
        this.x *= vector.getX();
        this.y *= vector.getY();
        updateVector();
    }
    public VelocityVector mul(double number){
        this.x *= number;
        this.y *= number;
        return new VelocityVector(this.x, this.y);
    }
    public void sub(VelocityVector vector){
        this.x -= vector.getX();
        this.y -= vector.getY();
        updateVector();
    }

    public void div(VelocityVector vector){
        this.x /= vector.getX();
        this.y /= vector.getY();
        updateVector();
    }
    public void div(double number){
        this.x /= number;
        this.y /= number;
        updateVector();
    }

    public double getAngle() {
        return angle;
    }

    public double getDirection() {
        return direction;
    }

    public double getMagnitude() {
        return magnitude;
    }

    private void updateVector(){
        this.magnitude = Math.sqrt(Math.pow(this.x, 2)+Math.pow(this.y, 2));
        this.direction =this.y/this.x;
        this.angle =Math.atan(this.direction);
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

    @Override
    public String toString() {
        return "VelocityVector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

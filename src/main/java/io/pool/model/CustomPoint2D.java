package io.pool.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CustomPoint2D {
    public static final MathContext DECIMAL8 = new MathContext(8, RoundingMode.HALF_EVEN);
    public static final BigDecimal ZERO_BD = new BigDecimal(0.0, DECIMAL8);
    public static final BigDecimal TWO_BD = new BigDecimal(2.0, DECIMAL8);
    public static final BigDecimal NINETY_DEGREES_BD = new BigDecimal(90, DECIMAL8);
    public static final BigDecimal ONE_EIGHTY_BD = new BigDecimal(180, DECIMAL8);
    public static final BigDecimal THREE_SIXTY_BD = new BigDecimal(360, DECIMAL8);
    public static final CustomPoint2D ZERO = new CustomPoint2D(ZERO_BD, ZERO_BD);

    private final BigDecimal x;
    private final BigDecimal y;

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public CustomPoint2D(double x, double y) {
        this.x = new BigDecimal(x, DECIMAL8);
        this.y = new BigDecimal(y, DECIMAL8);
    }
    public CustomPoint2D(BigDecimal x, BigDecimal y) {
        this.x = x;
        this.y = y;
    }

    public BigDecimal distance(BigDecimal x1, BigDecimal y1) {
        BigDecimal a = getX().subtract(x1, DECIMAL8);
        BigDecimal b = getY().subtract(y1, DECIMAL8);
        a = a.pow(2, DECIMAL8);
        b = b.pow(2, DECIMAL8);
        BigDecimal subtotal = a.add(b, DECIMAL8);
        return subtotal.sqrt(DECIMAL8);
    }
    public BigDecimal distance(CustomPoint2D point) {
        return distance(point.getX(), point.getY());
    }
    public CustomPoint2D add(BigDecimal x, BigDecimal y) {
        return new CustomPoint2D(getX().add(x, DECIMAL8), getY().add(y, DECIMAL8));
    }
    public CustomPoint2D add(CustomPoint2D point) {
        return add(point.getX(), point.getY());
    }

    public CustomPoint2D subtract(BigDecimal x, BigDecimal y) {
        return new CustomPoint2D(getX().subtract(x, DECIMAL8), getY().subtract(y, DECIMAL8));
    }
    public CustomPoint2D multiply(double factor) {
        BigDecimal factorBD = new BigDecimal(factor);
        return multiply(factorBD);
    }
    public CustomPoint2D multiply(BigDecimal factor) {
        return new CustomPoint2D(getX().multiply(factor, DECIMAL8), getY().multiply(factor, DECIMAL8));
    }
    public CustomPoint2D subtract(CustomPoint2D point) {
        return subtract(point.getX(), point.getY());
    }

    public CustomPoint2D normalize() {
        final BigDecimal mag = magnitude();

        if (mag.equals(ZERO_BD)) {
            return ZERO;
        }

        return new CustomPoint2D(
                getX().divide(mag, DECIMAL8),
                getY().divide(mag, DECIMAL8));
    }

    public CustomPoint2D midpoint(BigDecimal x, BigDecimal y) {
        BigDecimal totalX = x.add(getX().subtract(x, DECIMAL8), DECIMAL8);
        BigDecimal totalY = y.add(getY().subtract(y, DECIMAL8), DECIMAL8);
        return new CustomPoint2D(
                totalX.divide(TWO_BD, DECIMAL8),
                totalY.divide(TWO_BD, DECIMAL8));
    }
    public CustomPoint2D midpoint(CustomPoint2D point) {
        return midpoint(point.getX(), point.getY());
    }

    public BigDecimal getAngle(){
        try {
            BigDecimal subtotal = getY().divide(getX(), DECIMAL8);
            BigDecimal total = new BigDecimal(Math.toDegrees(Math.atan(subtotal.abs().doubleValue())), DECIMAL8);
            return total;
        }catch (ArithmeticException e){
            return null;
        }
    }

    public BigDecimal angle(BigDecimal x, BigDecimal y) {
        BigDecimal ax = getX();
        BigDecimal ay = getY();
        BigDecimal partA = ax.multiply(x).add(ay.multiply(y));
        BigDecimal partB = ax.pow(2, DECIMAL8).add(ay.pow(2, DECIMAL8), DECIMAL8);
        BigDecimal partC = x.pow(2, DECIMAL8).add(y.pow(2, DECIMAL8), DECIMAL8);
        partB = partB.multiply(partC, DECIMAL8);

        BigDecimal delta = partA.divide(partB.sqrt(DECIMAL8), DECIMAL8);

        if (delta.compareTo(new BigDecimal(1.0))==1) {
            return ZERO_BD;
        }
        if (delta.compareTo(new BigDecimal(-1.0))==-1) {
            return THREE_SIXTY_BD;
        }

        return new BigDecimal(Math.toDegrees(Math.acos(delta.doubleValue())), DECIMAL8);
    }

    public BigDecimal angle(CustomPoint2D point) {
        return angle(point.getX(), point.getY());
    }

    public BigDecimal magnitude() {
        BigDecimal x = getX();
        BigDecimal y = getY();
        x = x.pow(2, DECIMAL8);
        y = y.pow(2, DECIMAL8);
        BigDecimal subtotal = x.add(y, DECIMAL8);
        return subtotal.sqrt(DECIMAL8);
    }
    @Override public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof CustomPoint2D) {
            CustomPoint2D point = (CustomPoint2D) obj;
            return (getX().compareTo(point.getX())==0) && (getY().compareTo(point.getY())==0);
        } else return false;
    }

    @Override public String toString() {
        return "CustomPoint2D [x = " + getX() + ", y = " + getY() + "]";
    }

}

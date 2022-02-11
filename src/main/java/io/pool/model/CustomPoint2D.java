package io.pool.model;

import java.math.BigDecimal;
import java.math.MathContext;

public class CustomPoint2D {
    private static final BigDecimal ZERO_BD = new BigDecimal(0.0);
    private static final BigDecimal TWO_BD = new BigDecimal(2.0);
    private static final BigDecimal ONE_HUNDRED_EIGHTY_BD = new BigDecimal(180.0);
    public static final CustomPoint2D ZERO = new CustomPoint2D(ZERO_BD, ZERO_BD);

    private final BigDecimal x;
    private final BigDecimal y;

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public CustomPoint2D(BigDecimal x, BigDecimal y) {
        this.x = x;
        this.y = y;
    }

    public BigDecimal distance(BigDecimal x1, BigDecimal y1) {
        BigDecimal a = getX().subtract(x1);
        BigDecimal b = getY().subtract(y1);
        a = a.pow(2);
        b = b.pow(2);
        BigDecimal subtotal = a.add(b);
        return subtotal.sqrt(MathContext.DECIMAL32);
    }
    public BigDecimal distance(CustomPoint2D point) {
        return distance(point.getX(), point.getY());
    }
    public CustomPoint2D add(BigDecimal x, BigDecimal y) {
        return new CustomPoint2D(getX().add(x), getY().add(y));
    }
    public CustomPoint2D add(CustomPoint2D point) {
        return add(point.getX(), point.getY());
    }

    public CustomPoint2D subtract(BigDecimal x, BigDecimal y) {
        return new CustomPoint2D(getX().subtract(x), getY().subtract(y));
    }
    public CustomPoint2D multiply(double factor) {
        BigDecimal factorBD = new BigDecimal(factor);
        return new CustomPoint2D(getX().multiply(factorBD), getY().multiply(factorBD));
    }
    public CustomPoint2D subtract(CustomPoint2D point) {
        return subtract(point.getX(), point.getY());
    }

    public CustomPoint2D normalize() {
        final BigDecimal mag = magnitude();

        if (mag.equals(ZERO_BD)) {
            return new CustomPoint2D(ZERO_BD, ZERO_BD);
        }

        return new CustomPoint2D(
                getX().divide(mag,MathContext.DECIMAL32),
                getY().divide(mag,MathContext.DECIMAL32));
    }

    public CustomPoint2D midpoint(BigDecimal x, BigDecimal y) {
        BigDecimal totalX = x.add(getX().subtract(x));
        BigDecimal totalY = y.add(getY().subtract(y));
        return new CustomPoint2D(
                totalX.divide(TWO_BD,MathContext.DECIMAL32),
                totalY.divide(TWO_BD,MathContext.DECIMAL32));
    }
    public CustomPoint2D midpoint(CustomPoint2D point) {
        return midpoint(point.getX(), point.getY());
    }

    public BigDecimal angle(BigDecimal x, BigDecimal y) {
        BigDecimal ax = getX();
        BigDecimal ay = getY();
        BigDecimal partA = ax.multiply(x).add(ay.multiply(y));
        BigDecimal partB = ax.pow(2).add(ay.pow(2));
        BigDecimal partC = x.pow(2).add(y.pow(2));
        partB = partB.multiply(partC);

        BigDecimal delta = partA.divide(partB.sqrt(MathContext.DECIMAL32),MathContext.DECIMAL32);

        if (delta.compareTo(new BigDecimal(1.0))==1) {
            return ZERO_BD;
        }
        if (delta.compareTo(new BigDecimal(-1.0))==-1) {
            return ONE_HUNDRED_EIGHTY_BD;
        }

        return new BigDecimal(Math.acos(delta.doubleValue()));
    }

    public BigDecimal angle(CustomPoint2D point) {
        return angle(point.getX(), point.getY());
    }

    public BigDecimal magnitude() {
        BigDecimal x = getX();
        BigDecimal y = getY();
        x = x.pow(2);
        y = y.pow(2);
        BigDecimal subtotal = x.add(y);
        return subtotal.sqrt(MathContext.DECIMAL32);
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

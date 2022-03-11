package io.pool.model;

import javafx.scene.shape.Line;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TableBorderModel extends Line {
    public static ArrayList<TableBorderModel> tableBorder = new ArrayList<>();
    private BigDecimal reflectionXFactor;
    private BigDecimal reflectionYFactor;
    private String description;

    public TableBorderModel(String description, double startX, double startY, double endX, double endY, double xFactor, double yFactor) {
        super(startX,startY,endX,endY);
        this.description = description;
        this.reflectionXFactor = new BigDecimal(xFactor);
        this.reflectionYFactor = new BigDecimal(yFactor);
        tableBorder.add(this);
    }

    public BigDecimal getReflectionXFactor() {
        return reflectionXFactor;
    }

    public BigDecimal getReflectionYFactor() {
        return reflectionYFactor;
    }

    public String getDescription() {
        return description;
    }
}

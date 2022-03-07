package io.pool.model;

import javafx.scene.shape.Line;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TableBorderModel extends Line {
    public static ArrayList<TableBorderModel> tableBorder = new ArrayList<>();
    private BigDecimal reflectionXFactor;
    private BigDecimal reflectionYFactor;

    public static void addTableBorders(TableBorderModel model){
        tableBorder.add(model);
    }

    public TableBorderModel(double startX, double startY, double endX, double endY, double xFactor, double yFactor) {
        super(startX,startY,endX,endY);
        this.reflectionXFactor = new BigDecimal(xFactor);
        this.reflectionYFactor = new BigDecimal(yFactor);
    }

    public BigDecimal getReflectionXFactor() {
        return reflectionXFactor;
    }

    public BigDecimal getReflectionYFactor() {
        return reflectionYFactor;
    }
}

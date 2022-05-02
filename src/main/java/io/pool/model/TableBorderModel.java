package io.pool.model;

import io.pool.controller.BallController;
import io.pool.controller.SoundController;
import io.pool.view.BallView;
import io.pool.view.TableView;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

import java.math.BigDecimal;
import java.util.ArrayList;

import static io.pool.view.TableView.width;

public class TableBorderModel extends Line {
    public static ArrayList<TableBorderModel> tableBorder = new ArrayList<>();
    private BigDecimal reflectionXFactor;
    private BigDecimal reflectionYFactor;
    private String description;
    /**
     * Hole number
     * 1    6   5
     *
     * 2    3   4
     */
    private int type;
    public static Shape tableBorderArea = new Path();

    public TableBorderModel(String description, double startX, double startY, double endX, double endY, double xFactor, double yFactor) {
        super(startX,startY,endX,endY);
        this.type=0;
        this.description = description;
        this.reflectionXFactor = new BigDecimal(xFactor);
        this.reflectionYFactor = new BigDecimal(yFactor);
        tableBorder.add(this);
    }

    public TableBorderModel(String description, double startX, double startY, double endX, double endY, int type) {
        super(startX,startY,endX,endY);
        this.description = description;
        this.type = type;
        tableBorder.add(this);
    }


    public static void applyReflection(BallModel bModel, boolean aiTraining){
        TableBorderModel reflectionLine=null;
        Bounds intersect;
        double area=Integer.MAX_VALUE;
        for(TableBorderModel line : tableBorder) {
            Circle ballShadow = new Circle(bModel.getPositionX().doubleValue(), bModel.getPositionY().doubleValue(), BallModel.RADIUS);
            intersect = Shape.intersect(ballShadow,line).getBoundsInLocal();
            if(intersect.getWidth()!=-1){
                double newarea = intersect.getWidth() * intersect.getHeight();
                if(area==Integer.MAX_VALUE){
                    area=newarea;
                    reflectionLine=line;
                }else {
                    if (newarea > area) {
                        area = newarea;
                        reflectionLine = line;
                    }
                }
            }
        }
        if (reflectionLine != null) {
            if (reflectionLine.type == 0) {
                bModel.setVelocityX(bModel.getVelocityX().multiply(reflectionLine.getReflectionXFactor()));
                bModel.setVelocityY(bModel.getVelocityY().multiply(reflectionLine.getReflectionYFactor()));
                if (!aiTraining) SoundController.BallBounce();
            } else {
                Point2D line = new Point2D(reflectionLine.getEndX() - reflectionLine.getStartX(), reflectionLine.getEndY() - reflectionLine.getStartY());
                Point2D vec = new Point2D(bModel.getVelocityX().doubleValue(), bModel.getVelocityY().doubleValue());
                double angleBetween = line.angle(vec);
                double angle = Math.atan2(bModel.getVelocityY().doubleValue(), bModel.getVelocityX().doubleValue());
                angle = Math.toDegrees(angle);

                if (angleBetween < 90) {
                    if (reflectionLine.type == 1) angle -= 180-(2*angleBetween);
                    if (reflectionLine.type == 2) angle += 180-(2*angleBetween);
                } else if (angleBetween > 90) {
                    if (reflectionLine.type == 1) angle += angleBetween-(180-angleBetween);
                    if (reflectionLine.type == 2) angle -= angleBetween-(180-angleBetween);
                } else {
                    bModel.setVelocityX(bModel.getVelocityX().multiply(new BigDecimal(-0.9)));
                    bModel.setVelocityY(bModel.getVelocityY().multiply(new BigDecimal(-0.9)));
                    return;
                }

                double velocityMag = Math.sqrt(Math.pow((bModel.getVelocityX().doubleValue()), 2) + Math.pow((bModel.getVelocityY().doubleValue()), 2)) * 0.9;
                bModel.setVelocityX(new BigDecimal(velocityMag * Math.cos(Math.toRadians(angle))));
                bModel.setVelocityY(new BigDecimal(velocityMag * Math.sin(Math.toRadians(angle))));
            }
        }
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

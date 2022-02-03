package io.pool.view;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

public class PoolCueView extends Node {

    private Cylinder cue;

    public PoolCueView() {

        cue = new Cylinder();
        cue.setRadius(10);
        cue.setHeight(200);
        cue.setRotate(45);
        cue.setLayoutX(100);
        cue.setLayoutY(100);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.GOLD);
        cue.setMaterial(material);
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}

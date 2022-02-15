package io.pool.controller;

import io.pool.view.PoolCueView;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

import java.util.HashSet;
import java.util.Set;

public class PoolCueController {

    PoolCueView pcv = new PoolCueView();

    public void handleRotateCue(MouseEvent e, double oldPosX, double oldPosY){

        double deltaX = e.getX() - pcv.getXPos();
        double deltaY = e.getY() - pcv.getYPos();

        if (deltaX != 0) {
            double newAngleDegrees = Math.toDegrees(Math.atan2(deltaY, deltaX)) + 90;
            System.out.println(newAngleDegrees);
            Rotate rotate = new Rotate();
            rotate.setAngle(newAngleDegrees - pcv.getPreviousAngle());
            pcv.getCue().getTransforms().add(rotate);
            pcv.setPreviousAngle(newAngleDegrees);
        }

    }

    static class keyHandler {
        /**The set of keys that are currently pressed down*/
        private static final Set<KeyCode> keysCurrentlyDown = new HashSet<>();

        private static void resetKeyHandler(Scene scene){
            keysCurrentlyDown.clear();
            if (scene != null) {
                scene.setOnKeyPressed(null);
                scene.setOnKeyReleased(null);
            }
        }

        /**
         * Set the main Scene key handlers. When a key is pressed,
         * it is added to the Set and when it gets released, it
         * is removed from the Set. That feature helps to add thrust
         * to the spaceship while turning it at the same time.
         */
        private static void setSceneKeyHandler(Scene scene) {
            scene.setOnKeyPressed((keyEvent -> {
                keysCurrentlyDown.add(keyEvent.getCode());
                if(keyEvent.getCode().equals(KeyCode.SPACE)){
                    // Pause the game
                }
            }));
            scene.setOnKeyReleased((keyEvent -> {
                keysCurrentlyDown.remove(keyEvent.getCode());
            }));
        }

        /**
         * Check if a specific key is pressed down.
         * @param keyCode code of the pressed key
         * @return <code>true</code> - If <code>keyCode</code> is in the Set <code>keysCurrentlyDown</code>;
         *         <code>false</code> - Otherwise.
         */
        private static boolean isDown(KeyCode keyCode) {
            return keysCurrentlyDown.contains(keyCode);
        }
    }
}

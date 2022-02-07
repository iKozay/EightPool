package io.pool.eightpool;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

abstract class GameLoopTimer extends AnimationTimer {
    /**Animation Duration Double Property. Keeps track of animation time to the nanosecond*/
    DoubleProperty animationDuration = new SimpleDoubleProperty(0L);
    /**The time that the animation started*/
    long animationStartTime=0;
    /**The time  of the last frame in nanoseconds*/
    long lastFrameTime=0;
    /**Boolean to see if the game is paused*/
    boolean isPaused;
    /**Boolean to see if the game is active*/
    boolean isActive;

    /**
     * Gets the paused state of the game
     * @return the boolean <code>isPaused</code>
     */
    public boolean isPaused() {
        return isPaused;
    }
    /**
     * Gets the active state of the game
     * @return the boolean <code>isActive</code>
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Gets the animationDuration Double Property.
     * @return <code>animationDuration</code> DoubleProperty
     */
    public DoubleProperty animationDurationProperty() {
        return animationDuration;
    }

    /**
     * Pause the game by stopping the animation and
     * setting <code>isPaused=true;</code>
     * <code>isActive=false;</code>
     */
    public void pause() {
        super.stop();
        isPaused = true;
        isActive = false;
    }
    /**
     * Unpause the game by starting the animation and
     * setting <code>isPaused=false;</code>
     * <code>isActive=true;</code>
     */
    public void play() {
        super.start();
        isPaused = false;
        isActive = true;
    }

    /**
     * Override of the AnimationTimer start() method.
     * Sets <code>isActive</code> to true and
     * Sets <code>isPaused</code> to false.
     */
    @Override
    public void start() {
        super.start();
        isActive = true;
        isPaused = false;
        animationDuration.set(0);
    }
    /**
     * Override of the AnimationTimer stop() method.
     * Sets <code>isActive</code> to false and
     * Sets <code>isPaused</code> to false.
     */
    @Override
    public void stop() {
        super.stop();
        isPaused = false;
        isActive = false;
        animationDuration.set(0);
    }
    /**
     * Override of the AnimationTimer handle() method.
     * If the game is not paused, it calculates the time
     * and calls the <code>tick</code> method with
     * the last frame time.
     */
    @Override
    public void handle(long now) {
        if (!isPaused) {
            long animDuration = now - animationStartTime;
            animationDuration.set(animDuration);
            float secondsSinceLastFrame = (float) ((now - lastFrameTime));
            lastFrameTime = now;
            tick(secondsSinceLastFrame);
        }
    }

    /**
     * Abstract method must be overridden to accomplish
     * a specific task every frame.
     * @param secondsSinceLastFrame time since last frame.
     */
    public abstract void tick(float secondsSinceLastFrame);
}
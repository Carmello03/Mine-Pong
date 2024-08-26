package com.example.pingpong.Model;

/**
 * Interface defining resizable components in the game, allowing them to adjust their size dynamically based on a given factor.
 */
public interface Resizable {
    /**
     * Resizes the object along the X-axis by a specified factor.
     * @param factor The factor by which to scale the object's size on the X-axis.
     */
    void resizeX(double factor);
    /**
     * Resizes the object along the Y-axis by a specified factor.
     * @param factor The factor by which to scale the object's size on the Y-axis.
     */
    void resizeY(double factor);
}
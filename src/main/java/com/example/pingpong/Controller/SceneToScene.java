package com.example.pingpong.Controller;

/**
 * Interface defining navigation between different scenes in the application, such as moving from the game scene to the menu scene.
 */
public interface SceneToScene {

    /**
     * Navigate to the game scene.
     */
    void toGame();
    /**
     * Navigate to the menu scene.
     */
    void toMenu();
    /**
     * Navigate to the load menu scene.
     */
    void toLoadMenu();
}

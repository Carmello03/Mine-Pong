package com.example.pingpong.Controller;

import com.example.pingpong.Model.Game;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles keyboard events for the game, enabling player control over their rackets using the keyboard.
 * This class facilitates the interactive aspect of the game by responding to specific key presses,
 * which are mapped to game functions such as moving rackets and pausing or resuming the game.
 */
public class KeyboardListener implements EventHandler<KeyEvent> {
    private final Game game;

    /**
     * Constructs a new KeyboardListener for the specified game.
     * This listener will handle key events specifically for controlling the game elements.
     *
     * @param game The game instance that this listener will manipulate based on key inputs.
     */
    public KeyboardListener(Game game) {
        this.game = game;
    }

    /**
     * Handles key events during the game. The method maps specific key presses to control actions
     * like moving rackets or pausing the game. This method is called whenever a key event is generated
     * in the game view.
     *
     * @param keyEvent The key event to handle, providing details of the key interaction.
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        KeyCode key = keyEvent.getCode();

        // Resume game if it is paused and ENTER is pressed
        if (KeyCode.ENTER.equals(key) && !game.isBallMoving()) {
            game.resumeGame();
        }

        // Only allow control inputs if the game is not paused
        if (!game.isGamePaused()) {
            System.out.println(keyEvent);
            if (KeyCode.ESCAPE.equals(key) && game.isBallMoving()) {
                game.pauseGame();
            }
            double gameHeight = game.getHeight();

            // Player 2 controls
            if (KeyCode.UP.equals(key)) {
                game.getPlayer2().getRacket().moveUp();
            }
            if (KeyCode.DOWN.equals(key)) {
                game.getPlayer2().getRacket().moveDown(gameHeight);
            }

            // Player 1 controls
            if (KeyCode.A.equals(key)) {
                game.getPlayer1().getRacket().moveUp();
            }
            if (KeyCode.Z.equals(key)) {
                game.getPlayer1().getRacket().moveDown(gameHeight);
            }

            System.out.println("Player 1 Racket Y Position: " + game.getPlayer1().getRacket().getPosY());
            System.out.println("Player 2 Racket Y Position: " + game.getPlayer2().getRacket().getPosY());
        }
    }
}
package com.example.pingpong.Controller;

import com.example.pingpong.Model.Game;
import com.example.pingpong.Model.GameSettings;
import com.example.pingpong.Model.Player;
import com.example.pingpong.View.GameView;
import javafx.application.Platform;

/**
 * Controls the flow and logic of the game, coordinating interactions between the model and the view.
 */
public class GameController {
    private Game game;
    private SceneToScene sceneController;
    private GameView gameView;
    private GameSettings settings;

    /**
     * Constructs a GameController with references to the scene controller and the game view,
     * initializing the game with default settings.
     *
     * @param sceneController The scene controller for navigating between scenes.
     * @param gameView        The game view to update the UI based on game state changes.
     */
    public GameController(SceneToScene sceneController, GameView gameView) {
        this.game = new Game();
        this.sceneController = sceneController;
        this.gameView = gameView;
        this.settings = new GameSettings("Player 1", "Player 2", 0, 0, 1.0, 0.1, 10.0, 100.0, 21, 2, 2);
    }

    /**
     * Gets the current game instance.
     * @return The current game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the current game to a new game instance.
     * @param game The new game instance to set.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Resets the game to its initial state.
     */
    public void resetGame() {
        game.resetGame();
    }

    /**
     * Pauses the game.
     */
    public void pauseGame() {
        game.pauseGame();
    }

    /**
     * Resumes the game from a paused state.
     */
    public void resumeGame() {
        game.resumeGame();
    }

    /**
     * Retrieves the current game settings.
     * @return The current settings of the game.
     */
    public GameSettings getSetting() {
        return settings;
    }

    /**
     * Sets the game to use specified settings.
     * @param settings The settings to apply to the game.
     */
    public void setSetting(GameSettings settings) {
        this.settings = settings;
    }

    /**
     * Applies the current settings to the game and retrieves them.
     * @return The applied settings.
     */
    public GameSettings applyAndGetSettings() {
        this.getSettings();
        return settings;
    }

    /**
     * Adjusts the game components sizes and positions based on the current window size.
     * @param game The game whose components will be adjusted.
     */
    public void adjustGameComponentsToCurrentWindowSize(Game game) {
        double currentWidth = gameView.getWidth();
        double currentHeight = gameView.getHeight();
        double defaultWidth = 1100;
        double defaultHeight = 650;

        double scaleX = currentWidth / defaultWidth;
        double scaleY = currentHeight / defaultHeight;

        game.setHeight(gameView.getHeight());
        game.setWidth(gameView.getWidth());
        game.resizeX(scaleX);
        game.resizeY(scaleY);
    }

    /**
     * Applies the settings from the GameSettings object to the game.
     * @param settings The settings to be applied to the game.
     */
    public void applySettings(GameSettings settings) {
        game.getPlayer1().setName(settings.getPlayer1Name());
        game.getPlayer2().setName(settings.getPlayer2Name());
        game.getPlayer1().setScore(settings.getPlayer1Score());
        game.getPlayer2().setScore(settings.getPlayer2Score());
        game.getBall().setSpeed(settings.getGameSpeed());
        game.getBall().setSpeedTemp(settings.getGameSpeed());
        game.getBall().setSpeedIncreaseFrequency(settings.getSpeedIncreaseFrequency());
        game.getPlayer1().getRacket().setLength(settings.getRacketLength());
        game.getPlayer1().getRacket().setWidth(settings.getRacketWidth());
        game.getPlayer2().getRacket().setLength(settings.getRacketLength());
        game.getPlayer2().getRacket().setWidth(settings.getRacketWidth());
        game.setMaxScore(settings.getWinningScore());
        game.getPlayer1().getRacket().setSize(settings.getRacketSize());
        game.getPlayer1().getRacket().setThickness(settings.getRacketThickness());
    }

    /**
     * Retrieves settings from the current game and updates the GameSettings object.
     */
    public void getSettings() {
        settings.setPlayer1Name(game.getPlayer1().getName());
        settings.setPlayer2Name(game.getPlayer2().getName());
        settings.setPlayer1Score(game.getPlayer1().getScore());
        settings.setPlayer2Score(game.getPlayer2().getScore());
        settings.setGameSpeed(game.getBall().getSpeed());
        settings.setSpeedIncreaseFrequency(game.getBall().getSpeedIncreaseFrequency());
        settings.setRacketLength(game.getPlayer1().getRacket().getLength());
        settings.setRacketWidth(game.getPlayer1().getRacket().getWidth());
        settings.setWinningScore( game.getMaxScore());
        settings.setRacketSize(game.getPlayer1().getRacket().getSize());
        settings.setRacketThickness(game.getPlayer1().getRacket().getThickness());
    }

    /**
     * Checks if a player has won the game by reaching the maximum score.
     *
     * @param player The player to check for a win condition.
     * @return True if the player has won, false otherwise.
     */
    public boolean checkForWin(Player player) {
        if (player.getScore() >= game.getMaxScore()) {
            gameView.drawGame(game);
            gameView.drawWin(player, game);
            return true;
        }
        return false;
    }

    /**
     * Handles actions to take when the game is won by navigating to the menu.
     */
    void onGameWon() {
        Platform.runLater(() -> {
            sceneController.toMenu();
        });
    }

    /**
     * Awards a point to the scoring player.
     *
     * @param scorer The player who scored.
     */
    public void scorePoint(Player scorer) {
        scorer.scorePoint();
    }

    /**
     * Handles the logic when a goal is scored in the game, including updating the score and pausing the game briefly.
     *
     * @param scorer The player who scored the goal.
     */
    public void handleGoal(Player scorer) {

        game.getPlayer1().getRacket().resetPosition(gameView.getHeight());
        game.getPlayer2().getRacket().resetPosition(gameView.getHeight());

        gameView.drawGoal(scorer, game);
        game.stopBallMovement();
    }

}
package com.example.pingpong.Controller;

import com.example.pingpong.Model.Game;
import com.example.pingpong.Model.GameSettings;
import com.example.pingpong.View.GameView;
import javafx.application.Platform;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Handles menu interactions and manages game control functions such as starting, pausing, and exiting the game.
 * This class integrates actions triggered from the user interface with game logic and state management.
 */
public class MenuListener {
    private Game game;
    private SceneToScene sceneController;
    private GameController gameController;
    private GameView canvas;
    private GameSaver gameSaver;
    private GameDAO gameDAO;

    /**
     * Constructs a MenuListener with dependencies needed for menu interactions.
     *
     * @param game            The game model for managing game states.
     * @param toScene         The controller for managing scene transitions.
     * @param canvas          The game view to interact with graphical components.
     * @param gameController  The controller for managing game logic and updates.
     */
    public MenuListener(Game game, SceneToScene toScene, GameView canvas, GameController gameController)
    {
        this.game = game;
        this.sceneController = toScene;
        this.canvas = canvas;
        this.gameController = gameController;
        this.gameSaver = GameSaver.getInstance();
        this.gameDAO = new GameDAO();
    }

    /**
     * Exits the application.
     */
    public void setExit() {
        Platform.exit();
    }

    /**
     * Navigates back to the main menu.
     */
    public void setBack() {
        sceneController.toMenu();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameSettings applyAndGetSettings() {
        return gameController.applyAndGetSettings();
    }

    public GameController getGameController() {
        return gameController;
    }

    /**
     * Starts the game.
     */
    public void start() {
        game.resetGame();
        sceneController.toGame();
    }

    /**
     * Starts a saved game by applying previously saved settings and transitioning to the game scene.
     */
    public void startSaved() {
        gameController.applySettings(gameController.getSetting());
        sceneController.toGame();
    }

    /**
     * Initiates the process to save the current game state either to the database or a file, as chosen by the user.
     */
    public void saveGame() {
        game.pauseGame();
        gameController.getSettings();
        this.showSaveMethodChoice();
    }

    /**
     * Shows a choice dialog for selecting the method of saving the game.
     */
    public void showSaveMethodChoice() {
        List<String> options = Arrays.asList("Save to Database", "Save to File");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Save to Database", options);
        dialog.setTitle("Save Game");
        dialog.setHeaderText("Select the save method:");
        dialog.setContentText("Choose your save method:");

        // Process the user's choice
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(choice -> {
            if ("Save to Database".equals(choice)) {
                saveGameToDatabase();
            } else {
                showSaveDialog();
            }
        });
    }

    /**
     * Presents a confirmation dialog to the user with options to choose a save slot.
     * This dialog provides options to save the game in one of five slots. If the user
     * selects a slot, the game settings are saved to that specified slot. If the user
     * cancels, no action is taken.
     */
    public void showSaveDialog() {
        Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        saveAlert.setTitle("Save Game");
        saveAlert.setHeaderText("Choose a slot to save your game:");
        saveAlert.setContentText("Select one of the slots:");

        ButtonType[] buttonTypes = new ButtonType[6];
        for (int i = 1; i <= 5; i++) {
            buttonTypes[i - 1] = new ButtonType("Save " + i);
        }
        buttonTypes[5] = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        saveAlert.getButtonTypes().setAll(buttonTypes);

        Optional<ButtonType> result = saveAlert.showAndWait();
        if (result.isPresent() && result.get() != buttonTypes[5]) {
            for (int i = 0; i < 5; i++) {
                if (result.get() == buttonTypes[i]) {
                    saveGameSettings(i + 1);
                    break;
                }
            }
        }
    }

    /**
     * Presents a dialog to the user for inputting a name under which the game will be saved.
     * This method checks if a game with the given name already exists in the database:
     * - If it exists, the game is updated under the same name.
     * - If it does not exist, a new game record is created.
     * The user is provided feedback on the success or failure of the save operation.
     *
     * @throws SQLException if there is a database access error or other errors.
     */
    public void saveGameToDatabase() {
        TextInputDialog dialog = new TextInputDialog("Default Game Name");
        dialog.setTitle("Save Game");
        dialog.setHeaderText("Enter a name for your game:");
        dialog.setContentText("Game name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(gameName -> {
            try {
                if (gameDAO.gameExists(gameName)) {
                    gameDAO.updateGame(game, gameName);
                    showFeedback("Game Updated", "Your game has been successfully updated as '" + gameName + "'.", Alert.AlertType.INFORMATION);
                } else {
                    gameDAO.saveGame(game, gameName);
                    showFeedback("Game Saved", "Your game has been successfully saved as '" + gameName + "'.", Alert.AlertType.INFORMATION);
                }
            } catch (SQLException e) {
                showFeedback("Error", "Failed to save the game: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    /**
     * Saves the game settings to a specific file slot selected by the user.
     */
    private void saveGameSettings(int slot) {
        try {
            String filename = "settingsSave" + slot + ".ser";
            gameSaver.saveGameSettings(filename, gameController.getSetting());
            showFeedback("Game Saved", "Your game has been successfully saved to slot " + slot, Alert.AlertType.INFORMATION);
        } catch (IOException ex) {
            showFeedback("Error", "Failed to save the game: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Opens the load menu to select a game to load.
     */
    public void load() {
        sceneController.toLoadMenu();
    }

    /**
     * Loads a game from the database by name, prompts user for selection, and transitions to the game scene.
     * @throws SQLException If there is an error loading the game from the database.
     */
    public void loadDb() throws SQLException {
        List<String> gameNames = gameDAO.loadGameNames();
        if (gameNames.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No games available to load.");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(gameNames.get(0), gameNames);
        dialog.setTitle("Load Game");
        dialog.setHeaderText("Select a game to load:");
        dialog.setContentText("Choose your game:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(gameName -> {
            Game game;
            try {
                game = gameDAO.loadGame(gameName);
                System.out.println("Game loaded successfully: " + gameName);
            } catch (SQLException e) {
                System.out.println("Failed to load game settings from: " + gameName);
                throw new RuntimeException(e);
            }
            int playerRacketSize = gameController.getGame().getPlayer1().getRacket().getSize();
            int playerRacketThick = gameController.getGame().getPlayer1().getRacket().getThickness();
            double speed = gameController.getGame().getBall().getSpeed();
            double speedFrequency = gameController.getGame().getBall().getSpeedIncreaseFrequency();
            gameController.setGame(game);
            gameController.adjustGameComponentsToCurrentWindowSize(game);
            this.setRacketSize(playerRacketSize);
            this.setRacketSize(playerRacketThick);
            this.setSpeedIncreaseFrequency(speedFrequency);
            this.setBallSpeed(speed);
            sceneController.toGame(); // Transition to game scene
        });
    }

    /**
     * Loads game settings from a file and sets them in the game controller.
     * @param filename The name of the file to load settings from.
     */
    public void loadGameSettings(String filename) {
        try {
            gameController.setSetting(gameSaver.loadSettings(filename));
            System.out.println("Game loaded successfully from " + filename);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game settings from " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Loads the latest game from the database and resumes it by setting the game state and adjusting game components.
     */
    public void loadAndResumeLatestGame() {
        try {
            Game latestGame = gameDAO.loadLatestGame();
            if (latestGame != null) {
                gameController.setGame(latestGame);
                gameController.adjustGameComponentsToCurrentWindowSize(latestGame);
                sceneController.toGame(); // Switch to the game scene
            } else {
                System.out.println("No saved games to load.");
            }
        } catch (SQLException e) {
            System.err.println("Error loading the latest game: " + e.getMessage());
        }
    }

    /**
     * Displays feedback to the user based on actions such as saving or updating the game.
     *
     * @param title      The title of the feedback dialog.
     * @param message    The message to display.
     * @param alertType  The type of alert to show.
     */
    public void showFeedback(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait().ifPresent((btnType) -> {
            canvas.requestFocus(); // Request focus back to the canvas
        });
    }

    /**
     * Shows an about dialog with game instructions and credits.
     */
    public void setAbout() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mine-Pong");
        alert.setHeaderText("Made by Tadhg");
        alert.setContentText("INSTRUCTIONS: Press play to start Game.");
        alert.showAndWait().ifPresent((btnType) -> {
            canvas.requestFocus(); // Request focus back to the canvas
        });
    }

    /**
     * Restarts the game.
     */
    public void restart() {
        game.resetGame();
        canvas.requestFocus();
    }

    /**
     * Pauses the game.
     */
    public void pause() {
        gameController.pauseGame();
        canvas.requestFocus();
    }

    /**
     * Resumes the game.
     */
    public void play() {
        gameController.resumeGame();
        canvas.requestFocus();
    }

    public void setPlayer1Name(String name) {
        game.getPlayer1().setName(name);
    }

    public void setPlayer2Name(String name) {
        game.getPlayer2().setName(name);
    }

    /**
     * Adjusts the speed of the ball based on a slider value.
     *
     * @param speed The value from the slider indicating the speed.
     */
    public void setBallSpeed(double speed) {
        game.getBall().setSpeed(speed);
        game.getBall().setSpeedTemp(speed);
    }

    /**
     * Adjusts the size of the racket based on a selected option.
     *
     * @param size The size of the racket: 1 for large, 2 for medium, 3 for small.
     */
    public void setRacketSize(int size) {
        switch (size) {
            case 1: // large
                game.getPlayer1().getRacket().setLength(game.getHeight()/100*24);
                game.getPlayer2().getRacket().setLength(game.getHeight()/100*24);
                game.getPlayer1().getRacket().setSize(3);
                game.getPlayer2().getRacket().setSize(3);
                break;
            case 2: // medium
                game.getPlayer1().getRacket().setLength(game.getHeight()/100*14);
                game.getPlayer2().getRacket().setLength(game.getHeight()/100*14);
                game.getPlayer1().getRacket().setSize(2);
                game.getPlayer2().getRacket().setSize(2);
                break;
            case 3: // small
                game.getPlayer1().getRacket().setLength(game.getHeight()/100*5);
                game.getPlayer2().getRacket().setLength(game.getHeight()/100*5);
                game.getPlayer1().getRacket().setSize(1);
                game.getPlayer2().getRacket().setSize(1);
                break;
        }
    }

    /**
     * Adjusts the thickness of the racket based on a selected option.
     *
     * @param size The thickness of the racket: 1 for thick, 2 for average, 3 for slim.
     */
    public void setRacketThickness(int size) {
        switch (size) {
            case 1: // thick
                game.getPlayer1().getRacket().setWidth(game.getWidth()/100*2);
                game.getPlayer2().getRacket().setWidth(game.getWidth()/100*2);
                game.getPlayer1().getRacket().setThickness(3);
                game.getPlayer2().getRacket().setThickness(3);
                break;
            case 2: // average
                game.getPlayer1().getRacket().setWidth(game.getWidth()/100*1.36);
                game.getPlayer2().getRacket().setWidth(game.getWidth()/100*1.36);
                game.getPlayer1().getRacket().setThickness(2);
                game.getPlayer2().getRacket().setThickness(2);
                break;
            case 3: // slim
                game.getPlayer1().getRacket().setWidth(game.getWidth()/100*0.72);
                game.getPlayer2().getRacket().setWidth(game.getWidth()/100*0.72);
                game.getPlayer1().getRacket().setThickness(1);
                game.getPlayer2().getRacket().setThickness(1);
                break;
        }
    }

    /**
     * Sets the winning score for the game.
     *
     * @param score The score to set as the winning condition.
     */
    public void setWinningScore(int score) {
        game.setMaxScore(score);
    }

    /**
     * Adjusts the speed increase frequency of the ball after each bounce.
     *
     * @param frequency The value from the slider indicating the frequency.
     */
    public void setSpeedIncreaseFrequency(double frequency) {
        game.getBall().setSpeedIncreaseFrequency(frequency);
    }
}
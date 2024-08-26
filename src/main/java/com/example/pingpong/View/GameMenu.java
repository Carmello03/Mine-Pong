package com.example.pingpong.View;

import com.example.pingpong.Controller.GameSaver;
import com.example.pingpong.Controller.MenuListener;
import com.example.pingpong.Model.GameSettings;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;

/**
 * The GameMenu class is responsible for creating and managing the user interface for the game menu.
 * It handles the interaction with various game settings and control elements, allowing the user
 * to start new games, load existing games, adjust game settings, and access other game-related information.
 */
public class GameMenu {
    private MenuListener menuListener;
    private VBox menuMain, loadMenu;
    private Button menuStartButton, menuLoadButton, menuLoadFromDBButton, menuExitButton, menuAboutButton, exitButton, saveButton, settingsButton, pauseButton, restartButton, playButton, exitLoadButton, firstGameSlot, secondGameSlot, thirdGameSlot, fourthGameSlot, fifthGameSlot, loadStartButton, loadLatestStartButton;
    private HBox racketSizeOptions, racketThicknessOptions;
    private RadioButton largeRacket , mediumRacket, smallRacket, slimRacket, averageRacket, thickRacket;
    private HBox gameMenu;
    private ToggleGroup racketSizeGroup, racketThickGroup;
    private TextField setPlayer1Name, setPlayer2Name;
    private Slider setBallSpeed, setWinningScore, setSpeedIncreaseFrequency;

    /**
     * Constructs a new GameMenu with the specified menuListener which handles actions triggered by the menu.
     *
     * @param menuListener The listener for menu actions.
     */
    public GameMenu(MenuListener menuListener) {
        this.menuListener = menuListener;

        this.gameMenu = makeGameMenu();


        // Initialize menu items
        initializeMenu();

        loadMenu();

        // Add menu items to menu
        menuMain();

        // Handle menu item actions
        handleMenuAction();
    }

    /**
     * Initializes all menu items and setting controls required for the game's menu.
     */
    public void initializeMenu() {
        setPlayer1Name = new TextField();
        setPlayer2Name = new TextField();

        menuStartButton = new Button("START");

        menuLoadButton = new Button("LOAD GAME");
        menuLoadFromDBButton = new Button("LOAD FROM DB");

        setBallSpeed = new Slider(0.5, 3.0, 1.5);

        racketSizeGroup = new ToggleGroup();

        racketThickGroup = new ToggleGroup();

        setWinningScore = new Slider(1, 20, 10);

        setSpeedIncreaseFrequency = new Slider(0.1, 1.0, 0.5);

        handleRadioButtons();

        exitButton.setOnAction(e -> {
            menuListener.setBack();
            this.resetMenu();
        });

        settingsButton.setOnAction(e -> menuListener.setAbout());

        restartButton.setOnAction(e -> menuListener.restart());

        pauseButton.setOnAction(e -> menuListener.pause());

        saveButton.setOnAction(e -> menuListener.saveGame());

        playButton.setOnAction(e -> menuListener.play());

        // Buttons for about and exit
        exitLoadButton = new Button("BACK");

        firstGameSlot = new Button("Game1");
        secondGameSlot = new Button("Game2");
        thirdGameSlot = new Button("Game3");
        fourthGameSlot = new Button("Game4");
        fifthGameSlot = new Button("Game5");


        loadStartButton = new Button("LOAD");
        loadLatestStartButton = new Button("LOAD LATEST DB GAME");
        menuAboutButton = new Button("ABOUT");
        menuExitButton = new Button("EXIT");

    }

    /**
     * Configures the radio buttons for racket size and thickness settings.
     */
    public void handleRadioButtons() {
        smallRacket = new RadioButton("Small");
        smallRacket.setUserData(3);
        smallRacket.setToggleGroup(racketSizeGroup);

        mediumRacket = new RadioButton("Medium");
        mediumRacket.setUserData(2);
        mediumRacket.setToggleGroup(racketSizeGroup);
        mediumRacket.setSelected(true); // Set as default selection

        largeRacket = new RadioButton("Large");
        largeRacket.setUserData(1);
        largeRacket.setToggleGroup(racketSizeGroup);

        smallRacket.getStyleClass().add("setting-label");
        mediumRacket.getStyleClass().add("setting-label");
        largeRacket.getStyleClass().add("setting-label");

        slimRacket = new RadioButton("Slim");
        slimRacket.setUserData(3);
        slimRacket.setToggleGroup(racketThickGroup);

        averageRacket = new RadioButton("Average");
        averageRacket.setUserData(2);
        averageRacket.setToggleGroup(racketThickGroup);
        averageRacket.setSelected(true); // Set as default selection

        thickRacket = new RadioButton("Thick");
        thickRacket.setUserData(1);
        thickRacket.setToggleGroup(racketThickGroup);

        slimRacket.getStyleClass().add("setting-label");
        averageRacket.getStyleClass().add("setting-label");
        thickRacket.getStyleClass().add("setting-label");

        // Add the radio buttons to the HBox
        racketSizeOptions = new HBox(10);
        racketSizeOptions.getChildren().addAll(smallRacket, mediumRacket, largeRacket);
        racketSizeOptions.setAlignment(Pos.CENTER);

        // Add the radio buttons to the HBox
        racketThicknessOptions = new HBox(10);
        racketThicknessOptions.getChildren().addAll(slimRacket, averageRacket, thickRacket);
        racketThicknessOptions.setAlignment(Pos.CENTER);

        handleRadioSelection (2, 2);
    }

    /**
     * Sets the selected state of the radio buttons based on the provided racket size and thickness.
     * This method is used to synchronize the radio button selections with current game settings, such as
     * when loading saved settings or resetting the menu to reflect updated values.
     *
     * @param selectedSize The selected size of the racket, where 1 represents "Large", 2 "Medium", and 3 "Small".
     * @param selectedThickness The thickness of the racket, where 1 represents "Thick", 2 "Average", and 3 "Slim".
     */
    public void handleRadioSelection (int selectedSize, int selectedThickness) {
        switch (selectedSize) {
            case 1:
                smallRacket.setSelected(true);
                break;
            case 2:
                break;
            case 3:
                largeRacket.setSelected(true);
                break;
        }

        // Update radio buttons for Racket Thickness
        switch (selectedThickness) {
            case 1:
                slimRacket.setSelected(true);
                break;
            case 2:
                break;
            case 3:
                thickRacket.setSelected(true);
                break;
        }
    }

    /**
     * Handles user interactions from the menu, including starting games, loading games,
     * adjusting settings, and accessing additional information.
     */
    public void handleMenuAction() {
        setPlayer1Name.textProperty().addListener((observable, oldValue, newValue) -> {
            menuListener.setPlayer1Name(newValue);
        });

        setPlayer2Name.textProperty().addListener((observable, oldValue, newValue) -> {
            menuListener.setPlayer2Name(newValue);
        });

        setBallSpeed.valueProperty().addListener((obs, oldVal, newVal) ->
                menuListener.setBallSpeed(newVal.doubleValue()));

        racketSizeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int racketSize = (int) newValue.getUserData();
                menuListener.setRacketSize(racketSize);
            }
        });

        racketThickGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int racketThickness = (int) newValue.getUserData();
                menuListener.setRacketThickness(racketThickness);
            }
        });

        setWinningScore.valueProperty().addListener((obs, oldVal, newVal) ->
                menuListener.setWinningScore(newVal.intValue()));

        setSpeedIncreaseFrequency.valueProperty().addListener((obs, oldVal, newVal) ->
                menuListener.setSpeedIncreaseFrequency(newVal.doubleValue()));

        menuExitButton.setOnAction(e -> menuListener.setExit());

        menuAboutButton.setOnAction(e -> menuListener.setAbout());

        menuStartButton.setOnAction(e -> menuListener.start());

        menuLoadButton.setOnAction(e -> menuListener.load());

        menuLoadFromDBButton.setOnAction(e -> {
            try {
                menuListener.loadDb();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        exitLoadButton.setOnAction(e -> {
            menuListener.setBack();
            this.resetMenu();});

        loadStartButton.setOnAction(e -> menuListener.startSaved());

        loadLatestStartButton.setOnAction(e -> menuListener.loadAndResumeLatestGame());

    }

    /**
     * Constructs the main menu layout, grouping all settings and control elements.
     */
    public void menuMain() {
        // Create the title label
        Label titleLabel = new Label("Ping-Pong");
        titleLabel.getStyleClass().add("title-label");

        // Style the text fields
        setPlayer1Name.getStyleClass().add("text-field");
        setPlayer2Name.getStyleClass().add("text-field");

        // Style the sliders
        setBallSpeed.getStyleClass().add("slider");
        setWinningScore.getStyleClass().add("slider");
        setSpeedIncreaseFrequency.getStyleClass().add("slider");

        // Style the buttons
        menuStartButton.getStyleClass().add("menu-button");
        menuLoadButton.getStyleClass().add("menu-button");
        menuLoadFromDBButton.getStyleClass().add("menu-button");
        menuAboutButton.getStyleClass().add("menu-button");
        menuExitButton.getStyleClass().add("menu-button");

        // Create labels for player names
        Label player1Label = new Label("Player 1: ");
        Label player2Label = new Label("Player 2: ");
        player1Label.getStyleClass().add("player-label");
        player2Label.getStyleClass().add("player-label");

        // Create the player names HBox
        HBox player1Box = new HBox(10, menuMainPlayers(player1Label, setPlayer1Name));
        HBox player2Box = new HBox(10, menuMainPlayers(player2Label, setPlayer2Name));
        HBox playerNamesBox = new HBox(10, player1Box, player2Box);
        playerNamesBox.setAlignment(Pos.CENTER);
        player1Box.setAlignment(Pos.CENTER);
        player2Box.setAlignment(Pos.CENTER);
        playerNamesBox.getStyleClass().add("player-container");
        player1Box.getStyleClass().add("player1-container");
        player2Box.getStyleClass().add("player2-container");

        // Labels for settings
        Label ballSpeedLabel = new Label("Set Ball Speed: ");
        ballSpeedLabel.getStyleClass().add("setting-label");
        Label winningScoreLabel = new Label("Set Winning Score: ");
        winningScoreLabel.getStyleClass().add("setting-label");
        Label speedIncreaseFrequencyLabel = new Label("Set Speed Increase Frequency: ");
        speedIncreaseFrequencyLabel.getStyleClass().add("setting-label");
        Label racketSizeLabel = new Label("Set Racket Size: ");
        racketSizeLabel.getStyleClass().add("setting-label");
        Label racketThicknessLabel = new Label("Set Racket Thickness: ");
        racketThicknessLabel.getStyleClass().add("setting-label");

        // Create the settings VBox for sliders
        VBox settingsBox = new VBox(
                menuMainSettings(ballSpeedLabel, setBallSpeed),
                menuMainSettings(winningScoreLabel, setWinningScore),
                menuMainSettings(speedIncreaseFrequencyLabel, setSpeedIncreaseFrequency)
        );
        settingsBox.setAlignment(Pos.CENTER);
        settingsBox.getStyleClass().add("form-container");

        // Create the HBox for racket size options
        HBox racketSizeOptionsBox = menuMainSettings(racketSizeLabel, racketSizeOptions);
        racketSizeOptionsBox.setAlignment(Pos.CENTER);
        racketSizeOptionsBox.getStyleClass().add("form-container");

        // Create the HBox for racket size options
        HBox racketThickOptionsBox = menuMainSettings(racketThicknessLabel, racketThicknessOptions);
        racketThickOptionsBox.setAlignment(Pos.CENTER);
        racketThickOptionsBox.getStyleClass().add("form-container");

        // Container for all form elements, centered and spaced appropriately
        VBox formBox = new VBox(20, titleLabel, menuStartButton, menuLoadButton, menuLoadFromDBButton, menuAboutButton, menuExitButton, playerNamesBox, settingsBox, racketSizeOptionsBox, racketThickOptionsBox);
        formBox.setAlignment(Pos.CENTER);
        formBox.getStyleClass().add("form-container");

        // The main menu VBox
        this.menuMain = new VBox(formBox);
        this.menuMain.setAlignment(Pos.CENTER);
        this.menuMain.getStyleClass().add("root");
    }

    /**
     * Creates a settings layout with given label and action control.
     *
     * @param settingsLabel Label for the setting.
     * @param settingAction The control associated with the setting (e.g., slider, button).
     * @return An HBox containing both the label and the control.
     */
    public HBox menuMainSettings(Label settingsLabel, javafx.scene.Node settingAction) {
        HBox settingsBox = new HBox(10, settingsLabel, settingAction);
        settingsBox.setAlignment(Pos.CENTER);
        settingsBox.getStyleClass().add("settings-box");
        return settingsBox;
    }

    /**
     * Loads and configures the game loading menu with options to load various saved games.
     */
    public void loadMenu() {
        // Create the title label
        Label titleLabel = new Label("Ping-Pong");
        titleLabel.getStyleClass().add("title-label");

        // Style the buttons
        loadStartButton.getStyleClass().add("load-menu-button");
        loadLatestStartButton.getStyleClass().add("load-menu-button");
        exitLoadButton.getStyleClass().add("load-menu-button");

        firstGameSlot.getStyleClass().add("load-button");
        secondGameSlot.getStyleClass().add("load-button");
        thirdGameSlot.getStyleClass().add("load-button");
        fourthGameSlot.getStyleClass().add("load-button");
        fifthGameSlot.getStyleClass().add("load-button");


        // Create the HBox for racket size options
        HBox loadGameOptions = new HBox(10, loadStartButton, exitLoadButton);
        loadGameOptions.setAlignment(Pos.CENTER);
        loadGameOptions.getStyleClass().add("form-container");

        VBox loadGameBox = new VBox(20, firstGameSlot, secondGameSlot, thirdGameSlot, fourthGameSlot, fifthGameSlot, loadLatestStartButton);
        loadGameBox.setAlignment(Pos.CENTER);
        loadGameBox.getStyleClass().add("form-container-load");

        // Container for all form elements, centered and spaced appropriately
        VBox loadOptionsBox = new VBox(20, titleLabel, loadGameBox, loadGameOptions);
        loadOptionsBox.setAlignment(Pos.CENTER);
        loadOptionsBox.getStyleClass().add("form-container");

        // The main menu VBox
        this.loadMenu = new VBox(loadOptionsBox);
        this.loadMenu.setAlignment(Pos.CENTER);
        this.loadMenu.getStyleClass().add("root");
    }

    /**
     * Helper method for creating player name input fields layout.
     *
     * @param label      The label for the player name field.
     * @param textField  The text field for entering the player name.
     * @return An HBox containing the label and the text field.
     */
    public HBox menuMainPlayers(Label label, TextField textField) {
        HBox playersBox = new HBox(10, label, textField);
        playersBox.setAlignment(Pos.CENTER);
        return playersBox;
    }

    /**
     * Constructs a control menu with game control buttons like pause, restart, and settings.
     *
     * @return An HBox containing the control buttons.
     */
    public HBox makeGameMenu() {
        exitButton = createButtonWithIcon("/com/example/pingpong/exit.png");

        settingsButton = createButtonWithIcon("/com/example/pingpong/settings.png");

        restartButton = createButtonWithIcon("/com/example/pingpong/restart.jpg");

        pauseButton = createButtonWithIcon("/com/example/pingpong/pause.png");

        saveButton = createButtonWithIcon("/com/example/pingpong/save.png");

        playButton = createButtonWithIcon("/com/example/pingpong/play.png");

        HBox iconButtonContainer = new HBox(10);
        iconButtonContainer.setAlignment(Pos.TOP_CENTER);
        iconButtonContainer.getChildren().addAll(playButton, saveButton, pauseButton, settingsButton, restartButton, exitButton);

        return iconButtonContainer;
    }

    /**
     * Helper method to create a button with an icon.
     *
     * @param pathName The path to the icon image.
     * @return A Button with the specified icon.
     */
    private Button createButtonWithIcon(String pathName) {
        URL url = getClass().getResource(pathName);
        if (url == null) {
            throw new IllegalArgumentException("Resource not found: " + pathName);
        }
        Image image = new Image(url.toExternalForm());
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(15);
        imageView.setFitHeight(15);

        Button buttonWithIcon = new Button();
        buttonWithIcon.setGraphic(imageView);
        buttonWithIcon.getStyleClass().add("game-button");

        return buttonWithIcon;
    }

    /**
     * Initializes the buttons for loading saved games.
     */
    private void initializeLoadButtons() {
        String basePath = "C:/Users/mghos/MTU/Year2-Semester2/OO_Prog/testing/Ping-Pong/Saves/";
        Button[] buttons = {firstGameSlot, secondGameSlot, thirdGameSlot, fourthGameSlot, fifthGameSlot};
        for (int i = 0; i < buttons.length; i++) {
            String filename = "settingsSave" + (i + 1) + ".ser";
            String fullPath = basePath + filename;

            if (GameSaver.isFileValid(fullPath)) {
                buttons[i].setText("Load Game " + (i + 1));
            } else {
                buttons[i].setText("Empty");
            }

            buttons[i].setOnAction(e -> {
                if (!GameSaver.isFileValid(fullPath)) {
                    // Show error if the file does not exist or is not valid
                    menuListener.showFeedback("Error", "No game saved in this slot.", Alert.AlertType.ERROR);
                } else {
                    // Proceed with loading if the file is valid
                    menuListener.loadGameSettings(fullPath);
                }
            });
        }
    }

    /**
     * Resets the menu to reflect the current game settings.
     */
    public void resetMenu() {
        GameSettings settings = menuListener.applyAndGetSettings();

        // Reset player names
        setPlayer1Name.setText(settings.getPlayer1Name());
        setPlayer2Name.setText(settings.getPlayer2Name());

        // Reset game settings sliders
        setBallSpeed.setValue(settings.getGameSpeed());
        setWinningScore.setValue(settings.getWinningScore());
        setSpeedIncreaseFrequency.setValue(settings.getSpeedIncreaseFrequency());

        handleRadioSelection (menuListener.getGameController().getGame().getPlayer1().getRacket().getSize(), menuListener.getGameController().getGame().getPlayer1().getRacket().getThickness());
    }

    // Getter for menu
    public VBox getMenuMain() {
        return menuMain;
    }
    // Getter for LOAD menu
    public VBox getLoadMenu() {
        initializeLoadButtons();
        return loadMenu;
    }
    // Getter for gaem menu
    public HBox getGameMenu() {
        return gameMenu;
    }
}

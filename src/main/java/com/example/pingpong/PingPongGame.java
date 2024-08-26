package com.example.pingpong;

import com.example.pingpong.Controller.*;
import com.example.pingpong.View.GameMenu;
import com.example.pingpong.View.GameView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The main application class for the Ping Pong game. This class sets up the game environment, initializes controllers,
 * and manages the primary stage and scene of the application. It serves as the entry point for the JavaFX application,
 * handling the transition between different views (menus and game view) and coordinating the overall game control flow.
 */
public class PingPongGame extends Application implements SceneToScene {
    private GameController game;
    private MenuListener menuListener;
    private GameMenu gameMenu;
    private Thread thread;
    private BallManager ballManager;
    private GameView canvas;
    private final String title = "Mine-Pong";
    private StackPane rootPane;
    private KeyboardListener keyboardListener;

    /**
     * Starts the primary stage of the application, setting up the game environment and user interface components.
     * This method is called as the entry point for the JavaFX application.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        setupGameControllers(primaryStage);
        setupUIComponents(primaryStage);
        initialSetup(primaryStage);

        toMenu();

        setupSceneListeners(primaryStage);

        ballManager = new BallManager(game.getGame(), canvas, game);

        primaryStage.show();
    }

    /**
     * Sets up the game controllers, including the game controller, menu listener, and game view.
     * This method initializes the components that control game logic and interaction.
     *
     * @param primaryStage The primary stage of the application for reference.
     */
    private void setupGameControllers(Stage primaryStage) {
        canvas = new GameView(1100, 690);
        game = new GameController(this, canvas);
        menuListener = new MenuListener(game.getGame(), this, canvas, game);
        gameMenu = new GameMenu(menuListener);
    }

    /**
     * Performs initial setup for the primary stage, including setting the title and minimum dimensions.
     * It ensures the primary stage is properly configured with basic window properties and styles.
     *
     * @param primaryStage The primary stage of the application for reference.
     */
    private void initialSetup(Stage primaryStage) {
        primaryStage.setTitle(title);
        String css = this.getClass().getResource("/com/example/pingpong/style.css").toExternalForm();
        primaryStage.getScene().getStylesheets().add(css);
        primaryStage.setMinHeight(690);
        primaryStage.setMinWidth(800);
    }

    /**
     * Sets up the UI components of the game, including the canvas for drawing the game and the keyboard listener for input.
     * This method prepares the visual and interactive elements of the game.
     *
     * @param primaryStage The primary stage of the application for reference.
     */
    private void setupUIComponents(Stage primaryStage) {
        rootPane = new StackPane();
        rootPane.setAlignment(Pos.CENTER);
        rootPane.getChildren().add(canvas);

        Scene scene = new Scene(rootPane, 1100, 690);
        primaryStage.setScene(scene);

        keyboardListener = new KeyboardListener(game.getGame());
        canvas.setOnKeyPressed(keyboardListener);
        canvas.setOnKeyReleased(keyboardListener);
        canvas.setFocusTraversable(true);
    }

    /**
     * Sets up listeners for the primary stage's width and height properties to dynamically resize the game.
     * This method is responsible for adjusting the game's components in response to changes in window size.
     *
     * @param primaryStage The primary stage of the application for reference.
     */
    private void setupSceneListeners(Stage primaryStage) {
        primaryStage.widthProperty().addListener(observable -> {
            double factor = primaryStage.getWidth() / game.getGame().getWidth();
            System.out.println("Width changed: " + primaryStage.getWidth() + ", Factor: " + factor);
            game.getGame().setWidth(primaryStage.getWidth());
            game.getGame().resizeX(factor);
            canvas.drawGame(game.getGame());
        });

        primaryStage.heightProperty().addListener(observable -> {
            double factor = primaryStage.getHeight() / game.getGame().getHeight();
            System.out.println("Height changed: " + primaryStage.getHeight() + ", Factor: " + factor);
            game.getGame().setHeight(primaryStage.getHeight());
            game.getGame().resizeY(factor);
            canvas.drawGame(game.getGame());
        });
    }

    /**
     * Switches the view to the main menu. It clears previous UI components and displays the menu UI.
     */
    @Override
    public void toMenu() {
        rootPane.getChildren().clear(); // Clear the previous UI components
        VBox menuRoot = gameMenu.getMenuMain();
        rootPane.getChildren().add(menuRoot);
        game.resetGame();
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    /**
     * Switches the view to the load menu. It prepares the environment for loading a game from saved states.
     */
    @Override
    public void toLoadMenu() {
        rootPane.getChildren().clear(); // Clear the previous UI components
        VBox menuRoot = gameMenu.getLoadMenu();
        rootPane.getChildren().add(menuRoot);
        if(game.getGame() != null) {
            game.resetGame();
            if (thread != null) {
                thread.interrupt();
                thread = null;
            }
        }
    }

    /**
     * Switches the view to the game view, starting or resuming gameplay. It ensures the game components are active and responsive.
     */
    @Override
    public void toGame() {
        rootPane.getChildren().clear(); // Clear the previous UI components
        canvas.requestFocus();

        menuListener.setGame(game.getGame());

        keyboardListener = new KeyboardListener(game.getGame());
        canvas.setOnKeyPressed(keyboardListener);
        canvas.setOnKeyReleased(keyboardListener);


        ballManager = new BallManager(game.getGame(), canvas, game);
        thread = new Thread(ballManager);
        thread.start();
        thread.yield();

        // Add the canvas and game menu to the rootPane
        StackPane.setAlignment(canvas, Pos.CENTER);
        rootPane.getChildren().add(canvas);
        canvas.drawGame(game.getGame());

        HBox gameMenuHBox = this.gameMenu.getGameMenu();
        StackPane.setAlignment(gameMenuHBox, Pos.TOP_RIGHT);
        rootPane.getChildren().add(gameMenuHBox);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public GameMenu getGameMenu() {
        return gameMenu;
    }

    public void setGameMenu(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
    }

    public GameView getCanvas() {
        return canvas;
    }

    public void setCanvas(GameView canvas) {
        this.canvas = canvas;
    }

}
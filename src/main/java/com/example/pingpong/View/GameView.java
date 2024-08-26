package com.example.pingpong.View;

import com.example.pingpong.Model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Handles the graphical representation of the Ping Pong game. This class extends Canvas and is responsible for drawing
 * the game elements such as the ball, rackets, score, and other UI components related to the game's visual aspect.
 * It provides functionality for updating the game's display during gameplay and responding to changes in game state.
 */
public class GameView extends Canvas {
    private Image backgroundImage; // The background image

    /**
     * Initializes a new GameView with specified dimensions and loads the background image.
     *
     * @param width  The width of the canvas.
     * @param height The height of the canvas.
     */
    public GameView(double width, double height) {
        super(width, height);
        // Initialize the background image
        backgroundImage = new Image(getClass().getResourceAsStream("/com/example/pingpong/gameBackground.jpg"));
    }

    /**
     * Draws the current state of the game including all game elements such as the ball, rackets, and the score.
     * This method is called repeatedly to refresh the game view during gameplay.
     *
     * @param game The game model to be drawn.
     */
    public void drawGame(Game game) {
        if (game == null || backgroundImage == null) return; // Don't draw if the game or image is not set
        resetSize(game);
        GraphicsContext gc = this.getGraphicsContext2D();

        // Draw the various components of the game
        drawBackground(gc, game);
        drawMiddleLine(gc, game);
        drawBall(gc, game.getBall());
        drawRacket(gc, game.getPlayer1().getRacket());
        drawRacket(gc, game.getPlayer2().getRacket());
        drawScoreBox(gc, game.getPlayer1(), game.getPlayer2(), game);

    }

    /**
     * Draws the game in its initial state, typically used at the start of the game.
     *
     * @param game The game model to be drawn.
     */
    public void initialDrawGame(Game game) {
        if (game == null || backgroundImage == null) return; // Don't draw if the game or image is not set
        resetSize(game);
        GraphicsContext gc = this.getGraphicsContext2D();

        // Draw the various components of the game
        drawBackground(gc, game);
        drawMiddleLine(gc, game);
        drawBall(gc, game.getBall());
        drawRacket(gc, game.getPlayer1().getRacket());
        drawRacket(gc, game.getPlayer2().getRacket());
        drawScoreBox(gc, game.getPlayer1(), game.getPlayer2(), game);
        drawInstructions();

    }

    /**
     * Resets the size of the canvas to match the game dimensions, ensuring the UI elements scale correctly
     * with the window or canvas size changes.
     *
     * @param game The game model containing the current dimensions.
     */
    private void resetSize(Game game) {
        // Set the canvas size based on the game dimensions
        this.setWidth(game.getWidth());
        this.setHeight(game.getHeight());
    }

    /**
     * Draws the background image onto the canvas.
     *
     * @param gc   The GraphicsContext of the canvas.
     * @param game The game model for reference.
     */
    private void drawBackground(GraphicsContext gc, Game game) {
        // Draw the background image scaled to the game
        gc.drawImage(backgroundImage, 0, 0, game.getWidth(), game.getHeight());
    }

    /**
     * Draws each player's racket on the canvas. This method is responsible for rendering the rackets
     * based on their current position and size within the game model.
     *
     * @param gc     The GraphicsContext of the canvas.
     * @param racket The racket to draw.
     */
    private void drawRacket(GraphicsContext gc, Racket racket) {
        double yPosition = racket.getPosY();
        double xPosition = racket.getPosX();

        Color racketColor = Color.LIGHTGRAY;
        gc.setFill(racketColor);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeRect(xPosition, yPosition, racket.getWidth(), racket.getLength());
        gc.fillRect(xPosition, yPosition, racket.getWidth(), racket.getLength());
    }

    /**
     * Draws the ball on the canvas. The ball is rendered according to its current position and size
     * within the game model, with a glowing effect to enhance visibility.
     *
     * @param gc   The GraphicsContext of the canvas.
     * @param ball The ball to draw.
     */
    public void drawBall(GraphicsContext gc, Ball ball) {
        double radius = ball.getRadius();
        double centerX = ball.getPosX() - radius;
        double centerY = ball.getPosY() - radius;
        Color ballColor = Color.RED;

        gc.setFill(ballColor);
        gc.setEffect(new Glow(1.0));
        gc.fillOval(centerX, centerY, radius * 2, radius * 2);
        gc.setEffect(null);
    }

    /**
     * Draws the score box at the top of the canvas. This area displays the current scores of both players
     * and updates dynamically as the game progresses.
     *
     * @param gc       The GraphicsContext of the canvas.
     * @param player1  The first player's game model.
     * @param player2  The second player's game model.
     * @param game     The game model for reference.
     */
    private void drawScoreBox(GraphicsContext gc, Player player1, Player player2, Game game) {
        double boxHeight = 70;
        Color boxColor = new Color(0.5, 0.5, 0.5, 0.2);
        gc.setFill(boxColor);
        gc.fillRect(0, 0, game.getWidth(), boxHeight);
        gc.setFont(new Font("Serif", 20));

        // Determine score text based on player names and scores
        String player1Text = player1.getName().isEmpty() ? "Player 1 - " : player1.getName() + " - ";
        player1Text += player1.getScore();
        String player2Text = player2.getName().isEmpty() ? "Player 2 - " : player2.getName() + " - ";
        player2Text += player2.getScore();

        gc.setFill(Color.rgb(0, 255, 0));
        gc.fillText(player1Text, 20, boxHeight / 2 + 10);

        gc.setFill(Color.rgb(0, 0, 255));
        Text measurementText = new Text(player2Text);
        measurementText.setFont(new Font("Serif", 20));
        double textWidth = measurementText.getLayoutBounds().getWidth();
        gc.fillText(player2Text, game.getWidth() - textWidth - 20, boxHeight / 2 + 10);
    }

    /**
     * Draws a vertical line down the middle of the canvas to visually divide the play area for each player.
     * This line helps players visually align their movements and serves as a central reference during gameplay.
     *
     * @param gc   The GraphicsContext of the canvas.
     * @param game The game model for reference.
     */
    public void drawMiddleLine(GraphicsContext gc, Game game) {
        double lineWidth = 0.4;
        double midX = game.getWidth() / 2;

        // Set the color for the line
        Color lineColor = Color.WHITE;

        gc.setStroke(lineColor);
        gc.setLineWidth(lineWidth);
        gc.strokeLine(midX, 0, midX, game.getHeight());
    }

    /**
     * Displays a message when a player scores a goal.
     *
     * @param player The player who scored.
     * @param game   The current game state for reference.
     */
    public void drawGoal(Player player, Game game) {
        GraphicsContext gc = super.getGraphicsContext2D();
        int fontSize = (int) getHeight() / 10;

        if(player.getName().equals(game.getPlayer1().getName())) {
            gc.setStroke(Color.rgb(0, 255, 0));
        } else {
            gc.setStroke(Color.rgb(0, 0, 255));
        }
        gc.setLineWidth(3);

        gc.setFont(new Font("sans-serif", fontSize));
        String playerName = player.getName();
        String goal = playerName + " has scored!";

        double textWidth = goal.length() * fontSize * 0.6;

        double textOffsetX = (getWidth() - textWidth) / 2;

        double textOffsetY = getHeight() / 7 * 3;
        gc.strokeText(goal, textOffsetX, textOffsetY);

        fontSize = (int) getHeight() / 30;
        gc.setFont(new Font("sans-serif", fontSize));
        gc.setLineWidth(1);
        gc.strokeText("Game will start again!" , textOffsetX, textOffsetY + 50);
    }

    /**
     * Displays a winning message when a player wins the game.
     *
     * @param player The player who won.
     * @param game   The current game state for reference.
     */
    public void drawWin(Player player, Game game) {
        GraphicsContext gc = super.getGraphicsContext2D();
        int fontSize = (int) getHeight() / 10;

        if(player.getName().equals(game.getPlayer1().getName())) {
            gc.setStroke(Color.rgb(0, 255, 0));
        } else {
            gc.setStroke(Color.rgb(0, 0, 255));
        }
        gc.setLineWidth(3);

        gc.setFont(new Font("sans-serif", fontSize));
        String playerName = player.getName();
        String goal = playerName + " has Won!";

        String score = "Score = " + game.getPlayer1().getScore() + " - " + game.getPlayer2().getScore();

        double textWidth = goal.length() * fontSize * 0.6;

        double textOffsetX = (getWidth() - textWidth) / 2;

        double textOffsetY = getHeight() / 7 * 3;

        gc.strokeText(goal, textOffsetX, textOffsetY);
        gc.strokeText(score, textOffsetX, textOffsetY + 70);

        fontSize = (int) getHeight() / 30;
        gc.setFont(new Font("sans-serif", fontSize));
        gc.setLineWidth(1);
        gc.strokeText("Game will return to menu!", textOffsetX, textOffsetY + 120);
    }

    /**
     * Draws a countdown timer on the canvas.
     *
     * @param i The current countdown number.
     */
    public void drawTimer(int i) {
        GraphicsContext gc = this.getGraphicsContext2D();
        int fontSize = (int) getHeight() / 10;

        // Set fill and stroke colors for contrast
        Color fillColor;
        Color strokeColor;
        String countdownText = "";
        switch(i) {
            case 3:
                fillColor = Color.RED;
                strokeColor = Color.BLACK;
                countdownText = String.valueOf(i);
                break;
            case 2:
                fillColor = Color.ORANGE;
                strokeColor = Color.BLACK;
                countdownText = String.valueOf(i);
                break;
            case 1:
                fillColor = Color.GREEN;
                strokeColor = Color.BLACK;
                countdownText = String.valueOf(i);
                break;
            default:
                fillColor = Color.GREEN;
                strokeColor = Color.BLACK;
                countdownText = "Go";
                break;
        }

        gc.setFont(new Font("sans-serif", fontSize));


        // Measure text
        Text text = new Text(countdownText);
        text.setFont(gc.getFont());
        double textWidth = text.getLayoutBounds().getWidth();

        // Calculate positions
        double textOffsetX = (getWidth() - textWidth) / 2;
        double textOffsetY = (getHeight()) / 2;

        // Draw text with outline for visibility
        gc.setStroke(strokeColor);
        gc.setLineWidth(1);
        gc.strokeText(countdownText, textOffsetX, textOffsetY);

        gc.setFill(fillColor);
        gc.fillText(countdownText, textOffsetX, textOffsetY);
    }

    /**
     * Draws instructional text on the canvas when the game is initially drawn.
     * This method provides players with guidance on how to start the game.
     */
    public void drawInstructions() {
        GraphicsContext gc = this.getGraphicsContext2D();
        int fontSize = (int) getHeight() / 20;

        // Set fill and stroke colors for contrast
        Color fillColor = Color.WHITE;
        Color strokeColor = Color.BLACK;

        gc.setFont(new Font("sans-serif", fontSize));
        String start = "Press Enter to start game!!";

        // Measure text
        Text text = new Text(start);
        text.setFont(gc.getFont());
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeihgt = text.getLayoutBounds().getHeight();

        // Calculate positions
        double textOffsetX = (getWidth() - textWidth) / 2;
        double textOffsetY = (getHeight() + (textHeihgt * 5)) / 2;

        // Draw text with outline for visibility
        gc.setStroke(strokeColor);
        gc.setLineWidth(1);
        gc.strokeText(start, textOffsetX, textOffsetY);

        gc.setFill(fillColor);
        gc.fillText(start, textOffsetX, textOffsetY);
    }

}

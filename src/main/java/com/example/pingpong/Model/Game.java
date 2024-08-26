package com.example.pingpong.Model;
/**
 * Represents the entire game state for a Ping-Pong game. This class encapsulates all aspects of the game,
 * including players, ball, game area dimensions, and scoring. It provides methods to manage game flow, such as
 * start, pause, and reset functionalities.
 */
public class Game implements Resizable {
    // Game settings
    private Player player1;
    private Player player2;
    private Ball ball;

    private double width;
    private double height;
    private int maxScore;

    private boolean ballMoving;
    private boolean isPaused;

    /**
     * Initializes a new Game instance with default settings.
     */
    public Game() {
        this.maxScore = 10;
        this.width = 1100;
        this.height = 650;
        this.ball = new Ball(this.height, this.width);
        this.player1 = new Player("PLAYER 1", this.height);
        this.player2 = new Player("PLAYER 2", this.height);
        this.ballMoving = false;
        this.isPaused = true;

        this.player2.getRacket().setPosX(this.width - this.player2.getRacket().getWidth() - 20);
    }

    /**
     * Resets the game to its initial state, including resetting the positions of the ball and the players' rackets,
     * and setting both players' scores to zero. Also, stops the ball's movement and marks the game as paused.
     */
    public void resetGame() {
        // Reset ball pos
        this.ball.resetPosition(this.width, this.height);
        this.ballMoving = false;

        // Reset player score
        this.player1.setScore(0);
        this.player2.setScore(0);

        // Reset player racket positions
        this.player1.getRacket().setPosY(this.height / 2.0 - this.player1.getRacket().getLength() / 2.0);
        this.player2.getRacket().setPosX(this.width - this.player2.getRacket().getWidth() - 20);
        this.player2.getRacket().setPosY(this.height / 2.0 - this.player2.getRacket().getLength() / 2.0);

        // Reset ball speed/dir
        this.ball.setSpeed(ball.getSpeedTemp());
        this.ball.setDirectionX(1);
        this.ball.setDirectionY(1);

        this.isPaused = true;
    }

    /**
     * Pauses the game, stopping the ball's movement.
     */
    public void pauseGame() {
        this.isPaused = true;
        this.ballMoving = false; // Ball should not move when the game is paused
    }

    /**
     * Resumes the game, allowing the ball to move again.
     */
    public void resumeGame() {
        this.isPaused = false;
        this.ballMoving = true; // Ball can move again when the game resumes
    }

    /**
     * Checks if the game is currently paused.
     *
     * @return True if the game is paused, false otherwise.
     */
    public boolean isGamePaused() {
        return isPaused;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player) {
        this.player1 = player;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player) {
        this.player2 = player;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void startBallMovement() {
        this.ballMoving = true;
    }

    public void stopBallMovement() {
        this.ballMoving = false;
    }

    public boolean isBallMoving() {
        return ballMoving;
    }

    /**
     * Updates game components proportionally based on a change in the X-axis dimension.
     * @param factor The scaling factor for resizing along the X-axis.
     */
    @Override
    public void resizeX(double factor) {
        // Resize logic for the game along the X-axis
        player1.getRacket().resizeX(factor);
        player2.getRacket().resizeX(factor);
        ball.resizeX(factor);
    }

    /**
     * Updates game components proportionally based on a change in the Y-axis dimension.
     * @param factor The scaling factor for resizing along the Y-axis.
     */
    @Override
    public void resizeY(double factor) {
        // Resize logic for the game along the Y-axis
        player1.getRacket().resizeY(factor);
        player2.getRacket().resizeY(factor);
        ball.resizeY(factor);
    }
}
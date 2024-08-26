package com.example.pingpong.Model;

import java.io.Serializable;

/**
 * Stores and manages all settings for a Ping-Pong game. This includes player names, scores, and various game
 * configurations such as ball speed, racket dimensions, and the winning score criteria.
 */
public class GameSettings implements Serializable {
    private String player1Name;
    private String player2Name;
    private int player1Score;
    private int player2Score;
    private double gameSpeed; // Example of a game setting
    private double speedIncreaseFrequency;
    private double racketWidth;
    private double racketLength;
    private int winningScore;
    private int racketSize; // Size category of the racket
    private int racketThickness; // Thickness of the racket

    /**
     * Constructs a new GameSettings object with initial values for all game settings.
     *
     * @param player1Name  Name of player 1.
     * @param player2Name  Name of player 2.
     * @param player1Score Initial score of player 1.
     * @param player2Score Initial score of player 2.
     * @param gameSpeed    Initial speed of the game ball.
     * @param speedIncreaseFrequency Rate at which the game ball's speed increases.
     * @param racketWidth  Width of the player's racket.
     * @param racketLength Length of the player's racket.
     * @param winningScore Score required to win the game.
     * @param racketSize   Size category of the racket.
     * @param racketThickness Thickness of the racket.
     */
    public GameSettings(String player1Name, String player2Name, int player1Score, int player2Score, double gameSpeed, double speedIncreaseFrequency, double racketWidth, double racketLength, int winningScore,int racketSize, int racketThickness) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.gameSpeed = gameSpeed;
        this.speedIncreaseFrequency = speedIncreaseFrequency;
        this.racketWidth = racketWidth;
        this.racketLength = racketLength;
        this.winningScore = winningScore;
        this.racketSize = racketSize;
        this.racketThickness = racketThickness;
    }

    // Getters and Setters
    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public double getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(double gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public double getSpeedIncreaseFrequency() {
        return speedIncreaseFrequency;
    }

    public void setSpeedIncreaseFrequency(double speedIncreaseFrequency) {
        this.speedIncreaseFrequency = speedIncreaseFrequency;
    }

    public double getRacketWidth() {
        return racketWidth;
    }

    public void setRacketWidth(double racketWidth) {
        this.racketWidth = racketWidth;
    }

    public double getRacketLength() {
        return racketLength;
    }

    public void setRacketLength(double racketLength) {
        this.racketLength = racketLength;
    }

    public int getWinningScore() {
        return winningScore;
    }

    public void setWinningScore(int winningScore) {
        this.winningScore = winningScore;
    }

    public int getRacketSize() {
        return racketSize;
    }

    public void setRacketSize(int racketSize) {
        this.racketSize = racketSize;
    }

    public int getRacketThickness() {
        return racketThickness;
    }

    public void setRacketThickness(int racketThickness) {
        this.racketThickness = racketThickness;
    }
}

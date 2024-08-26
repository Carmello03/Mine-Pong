package com.example.pingpong.Controller;

import com.example.pingpong.Model.Game;

/**
 * Builder class for creating Game objects.
 * Allows for step-by-step construction of a game with specific attributes.
 */
public class GameBuilder {
    private String player1Name;
    private int player1Score;
    private String player2Name;
    private int player2Score;
    private int target;

    /**
     * Sets the name of player 1.
     *
     * @param player1Name The name to set for player 1.
     * @return The current instance of GameBuilder for chaining method calls.
     */
    public GameBuilder withPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
        return this;
    }

    /**
     * Sets the score of player 1.
     *
     * @param player1Score The score to set for player 1.
     * @return The current instance of GameBuilder for chaining method calls.
     */
    public GameBuilder withPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
        return this;
    }

    /**
     * Sets the name of player 2.
     *
     * @param player2Name The name to set for player 2.
     * @return The current instance of GameBuilder for chaining method calls.
     */
    public GameBuilder withPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
        return this;
    }

    /**
     * Sets the score of player 2.
     *
     * @param player2Score The score to set for player 2.
     * @return The current instance of GameBuilder for chaining method calls.
     */
    public GameBuilder withPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
        return this;
    }

    /**
     * Sets the target score for the game.
     *
     * @param target The target score to end the game.
     * @return The current instance of GameBuilder for chaining method calls.
     */
    public GameBuilder withTarget(int target) {
        this.target = target;
        return this;
    }

    /**
     * Builds and returns a Game object configured with the set parameters.
     *
     * @return A new Game instance with the configured attributes.
     */
    public Game build() {
        Game game = new Game();
        game.getPlayer1().setName(player1Name);
        game.getPlayer2().setName(player2Name);
        game.getPlayer1().setScore(player1Score);
        game.getPlayer2().setScore(player2Score);
        game.setMaxScore(target);
        return game;
    }
}

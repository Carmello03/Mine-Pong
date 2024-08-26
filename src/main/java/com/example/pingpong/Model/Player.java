package com.example.pingpong.Model;

/**
 * Represents a player in the game. Each player has a name, score, and a racket.
 */
public class Player {
    private String name;
    private int score;
    private Racket racket;

    /**
     * Constructs a new Player with the specified name and initial racket height.
     * Initializes the score to 0 and creates a new racket with the given height.
     *
     * @param name   The name of the player.
     * @param height The initial height of the canvas.
     */
    public Player(String name, double height) {
        this.name = name;
        this.score = 0;
        this.racket = new Racket(height);
    }

    /**
     * Increments the player's score by 1.
     */
    public void scorePoint() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Racket getRacket() {
        return racket;
    }

    public void setRacket(Racket racket) {
        this.racket = racket;
    }

}
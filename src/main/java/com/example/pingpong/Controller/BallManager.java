package com.example.pingpong.Controller;

import com.example.pingpong.Model.Ball;
import com.example.pingpong.Model.Game;
import com.example.pingpong.Model.Player;
import com.example.pingpong.Model.Racket;
import com.example.pingpong.View.GameView;
import javafx.application.Platform;

/**
 * Manages the ball's movements and collisions within the game.
 * This includes handling ball movement, detecting collisions with rackets and walls, and scoring.
 */
public class BallManager implements Runnable {
    private Game game;
    private GameView gameView;
    private GameController gameController;

    private boolean hasScored;
    private Player scoringPlayer;

    /**
     * Constructs a BallManager with specified game, game view, and game controller.
     *
     * @param game            The game model containing all game data.
     * @param gameView        The view responsible for rendering the game.
     * @param gameController  The controller managing game logic and interactions.
     */
    public BallManager(Game game, GameView gameView, GameController gameController) {
        this.game = game;
        this.gameView = gameView;
        this.gameController = gameController;
    }

    /**
     * The main run loop of the ball manager. Handles ball movement and checks for collisions and scoring.
     */
    @Override
    public void run() {
        Ball ball = game.getBall();
        while (true) {
            try {
                Thread.sleep(10);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return; // Exit loop
            }

            if (game.isBallMoving()) {
                ball.move();
                if (ball.checkCollisionWithCanvas(game.getHeight())) ball.setDirectionY(-ball.getDirectionY());;

                // Reset scoring state
                hasScored = false;
                scoringPlayer = null;

                // Check for scoring
                checkForScore(ball);

                // Handle any scoring event
                if (handleScoring(ball)) continue;

                // Check and handle racket collisions
                checkRacketCollision(ball);
            }
            // drawgame
            updateGameView();
        }
    }

    private void handleRacketCollision(Ball ball, Racket racket) {
        double hitPositionY = ball.getPosY() - racket.getPosY();
        if (hitPositionY <= 0 || hitPositionY >= racket.getLength()) {
            ball.setDirectionY(-ball.getDirectionY());
        } else {
            ball.bounce(racket);
        }
    }

    private void checkRacketCollision(Ball ball) {
        if (ball.isColliding(game.getPlayer1().getRacket())) {
            handleRacketCollision(ball, game.getPlayer1().getRacket());
        } else if (ball.isColliding(game.getPlayer2().getRacket())) {
            handleRacketCollision(ball, game.getPlayer2().getRacket());
        }
    }

    private void checkForScore(Ball ball) {
        if (ball.getPosX() < 10 || ball.getPosX() > game.getWidth() - 10) {
            scoringPlayer = ball.getPosX() < 10 ? game.getPlayer2() : game.getPlayer1();
            gameController.scorePoint(scoringPlayer);
            hasScored = true;
        }
    }

    private boolean handleScoring(Ball ball) {
        if (hasScored) {
            if (gameController.checkForWin(scoringPlayer)) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                gameController.onGameWon();
                return true;
            } else {
                gameController.pauseGame();
                gameController.handleGoal(scoringPlayer);
                ball.resetPosition(game.getWidth(), game.getHeight());
                ball.resetSpeed();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
                startTimer();
                gameController.resumeGame();
            }
        }
        return false;
    }

    private void startTimer() {
        for (int i = 3; i >= 0; i--) {
            int timer = i;
            Platform.runLater(() -> gameView.drawGame(game));
            Platform.runLater(() -> gameView.drawTimer(timer));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void updateGameView() {
        if (game.isGamePaused()) {
            // If the game hasn't started, draw instructions
            Platform.runLater(() -> gameView.initialDrawGame(game));
        } else {
            // If the game has started, draw the game
            Platform.runLater(() -> gameView.drawGame(game));
        }
    }
}

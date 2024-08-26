import com.example.pingpong.Controller.BallManager;
import com.example.pingpong.Model.Ball;
import com.example.pingpong.Model.Game;
import com.example.pingpong.Model.Racket;
import com.example.pingpong.View.GameView;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BallManagerTest {
    private Game game;
    private BallManager manager;
    private GameView gameView;

    @Before
    public void setUp() {
        game = new Game();
        gameView = new GameView(600, 600);
        manager = new BallManager(game, gameView, null); // GameController is not used in these tests, so it's null.

        // Setup for a default game scenario.
        game.setWidth(600);
        game.setHeight(600);

    }

    @Test
    public void testBallCollisionWithTopBoundary() {
        Ball ball = game.getBall();
        ball.setPosX(300);
        ball.setPosY(0 + ball.getRadius());
        ball.setDirectionY(-1); // Ball moving upwards.

        assertTrue("Ball should collide with the top boundary", ball.checkCollisionWithCanvas(600));
    }

    @Test
    public void testBallCollisionWithBottomBoundary() {
        Ball ball = game.getBall();
        ball.setPosX(300);
        ball.setPosY(600 - ball.getRadius());
        ball.setDirectionY(1); // Ball moving downwards.

        assertTrue("Ball should collide with the bottom boundary", ball.checkCollisionWithCanvas(600));
    }

    @Test
    public void testBallCollisionWithRacket() {
        Ball ball = game.getBall();
        Racket racket = game.getPlayer1().getRacket();
        ball.setPosX(racket.getPosX() + racket.getWidth() + ball.getRadius());
        ball.setPosY(racket.getPosY());
        assertTrue("Ball should collide with the racket", ball.isColliding(racket));
    }

    @Test
    public void testBallNoCollisionWithRacket() {
        Ball ball = game.getBall();
        Racket racket = game.getPlayer1().getRacket();
        ball.setPosX(300);
        ball.setPosY(300);

        assertFalse("Ball should not collide with any racket", ball.isColliding(racket));
    }

}

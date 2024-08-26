package com.example.pingpong.Model;

/**
 * Represents the ball used in the game, including its movement mechanics, collision detection, and resizing capabilities.
 */
public class Ball implements Resizable {
    private double speed;
    private double speedIncreaseFrequency;
    private double radius;

    private double posX;
    private double posY;

    private double directionX = 1; // 1 for right, -1 for left

    private double directionY = 1; // 1 for down, -1 for up

    private double speedTemp; // Temporary storage for speed to reset after game pauses or resets.

    /**
     * Constructs a new Ball with initial settings.
     *
     * @param height The height of the initial canvas.
     * @param width  The width of the initial canvas.
     */
    public Ball(double height, double width) {
        this.speed = 1.5;
        this.speedIncreaseFrequency = 0.5;
        this.radius = 15;

        this.posX = width/2;
        this.posY = height/2;

        this.speedTemp = speed;
    }

    /**
     * Moves the ball according to its speed and direction.
     */
    public void move() {
        this.posX += this.directionX * this.speed;
        this.posY += this.directionY * this.speed;
    }

    /**
     * Checks for collision with the top and bottom edges of the canvas and adjusts direction accordingly.
     *
     * @param canvasHeight The height of the canvas.
     */
    public boolean checkCollisionWithCanvas(double canvasHeight) {
        if (posY - (radius * 2) <= 0) {

            return true;
        }
        if (posY + (radius * 2) >= canvasHeight) {

            return true;
        }
        return false;
    }

    /**
     * Resets the ball's position to the center of the canvas.
     *
     * @param canvasWidth  The width of the canvas.
     * @param canvasHeight The height of the canvas.
     */
    public void resetPosition(double canvasWidth, double canvasHeight) {
        this.posX = canvasWidth / 2;
        this.posY = canvasHeight / 2;
    }

    /**
     * Resets the ball's speed to its initial speed.
     */
    public void resetSpeed() {
        this.speed = speedTemp;
    }

    /**
     * Determines if the ball is colliding with the specified racket.
     *
     * @param racket The racket to check for collision.
     * @return true if colliding, false otherwise.
     */
    public boolean isColliding(Racket racket) {
        return !(this.posX + this.radius < racket.getPosX()) &&
                !(this.posX - this.radius > racket.getPosX() + racket.getWidth()) && !(this.posY + this.radius < racket.getPosY()) &&
                !(this.posY - this.radius > racket.getPosY() + racket.getLength());
    }

    /**
     * Handles the ball's behavior when it collides with a racket, including inverting the direction,
     * increasing the speed, and adjusting the vertical movement based on the impact location.
     *
     * @param racket The racket that the ball has collided with.
     */
    public void bounce(Racket racket) {
        this.directionX = -this.directionX;

        // Increase speed consistently upon bouncing.
        this.speed += this.speedIncreaseFrequency;

        double hitPositionY = this.posY - racket.getPosY();
        double racketCenterY = racket.getLength() / 2;
        double relativeHitPosition = (hitPositionY - racketCenterY) / racketCenterY;

        // Adjust vertical direction and speed based on hit position
        this.directionY += relativeHitPosition * 0.5;

        // Clamp directionY within reasonable bounds
        if (this.directionY > 1) {
            this.directionY = 1;
        } else if (this.directionY < -1) {
            this.directionY = -1;
        }
    }

    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    public double getDirectionY() {
        return directionY;
    }


    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeedIncreaseFrequency() {
        return speedIncreaseFrequency;
    }

    public void setSpeedIncreaseFrequency(double speedIncreaseFrequency) {
        this.speedIncreaseFrequency = speedIncreaseFrequency;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getSpeedTemp() {
        return speedTemp;
    }

    public void setSpeedTemp(double speedTemp) {
        this.speedTemp = speedTemp;
    }

    @Override
    public void resizeX(double factor) {
        // Resize logic for the game along the x-axis
        this.posX *= factor;
        this.radius *= factor;
    }

    @Override
    public void resizeY(double factor) {
        // Resize logic for the game along the Y-axis
        this.posY *= factor;
    }
}
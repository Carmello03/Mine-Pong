package com.example.pingpong.Model;

/**
 * Represents a racket in the game. A racket can move up and down and has a position, size, and ability to hit the ball.
 */
public class Racket implements Resizable {
    private double width;
    private double length;
    private double posX;
    private double posY;
    private int size;
    private int thickness;


    /**
     * Constructs a new Racket with specified height and default properties.
     *
     * @param height The height of the initial canvas.
     */
    public Racket(double height) {
        this.width = 15;
        this.length = 90;
        this.posX = 20;
        this.posY = height / 2.0 - length / 2.0;
        this.size = 2;
        this.thickness = 2;
    }

    /**
     * Resets the racket's position to the center of the game area.
     *
     * @param canvasHeight The height of the canvas.
     */
    public void resetPosition(double canvasHeight) {
        this.posY = canvasHeight / 2.0 - length / 2.0;
    }

    /**
     * Moves the racket up by a fixed amount.
     */
    public void moveUp() {
        if (this.posY > 0) {
            this.posY -= 10;
        }
    }

    /**
     * Moves the racket down by a fixed amount, ensuring it does not go beyond the game area.
     *
     * @param gameHeight The height of the game area.
     */
    public void moveDown(double gameHeight) {
        double lowerBound = gameHeight - this.length;
        if (this.posY < lowerBound) {
            this.posY += 10;
        }
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
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
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    @Override
    public void resizeX(double factor) {
        // Resize logic for the game along the X-axis
        this.posX *= factor;
        this.width = this.width * factor;
    }

    @Override
    public void resizeY(double factor) {
        // Resize logic for the game along the Y-axis
        this.posY *= factor;
        this.length = this.length * factor;
    }
}
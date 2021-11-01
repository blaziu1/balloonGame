package pl.blazej.balloons;

import java.awt.Image;

class Balloon {
    Colour colour;
    private Image ballonImage;
    private int xCoordinate, yCoordinate;

    /**
     * Balloon class constructor.
     * Creates a balloon giving it the color and coordinates on the map
     * @param colour colour of the ballooon.
     * @param x Coordinate x.
     * @param y Coordinate y.
     */
    Balloon(Colour colour, int x, int y) {
        this.colour = colour;
        this.xCoordinate = x;
        this.yCoordinate = y;
    }

    int getxCoordinate() {
        return this.xCoordinate;
    }

    void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    int getyCoordinate() {
        return this.yCoordinate;
    }

    void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    Image getBallonImage() {
        return this.ballonImage;
    }

    void setBallonImage(Image ballonImage) {
        this.ballonImage = ballonImage;
    }

    Colour getColour() {
        return this.colour;
    }
}

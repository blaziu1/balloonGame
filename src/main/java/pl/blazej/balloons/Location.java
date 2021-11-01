package pl.blazej.balloons;

class Location {
    private int xCoordinate;

    private int yCoordinate;

    Location(int x, int y) {
        this.xCoordinate = x;
        this.yCoordinate = y;
    }

    int getxCoordinate() {
        return this.xCoordinate;
    }

    int getyCoordinate() {
        return this.yCoordinate;
    }
}
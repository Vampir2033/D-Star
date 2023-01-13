package engine;

import map.ObstacleMap;

import java.awt.*;

public interface Robot {
    Image getImage();
    int imageSize();
    Point imagePosWithShift();
    boolean checkPointAvailableForRobot(Point point, ObstacleMap obstacleMap, Point vector);
}

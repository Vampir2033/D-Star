package engine;

import map.ObstacleMap;

import java.awt.*;

public interface Robot {
    Image getImage();
    int imageSize();
    Point imagePosWithShift();
    boolean checkPointUnderRobot(Point point);
    boolean checkPointAvailableForRobot(Point point, ObstacleMap obstacleMap);
}

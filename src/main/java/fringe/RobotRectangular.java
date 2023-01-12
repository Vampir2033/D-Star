package fringe;

import engine.Robot;
import map.ObstacleMap;

import java.awt.*;

public class RobotRectangular implements Robot {
    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public int imageSize() {
        return 0;
    }

    @Override
    public Point imagePosWithShift() {
        return null;
    }

    @Override
    public boolean checkPointUnderRobot(Point point) {
        return false;
    }

    @Override
    public boolean checkPointAvailableForRobot(Point point, ObstacleMap obstacleMap) {
        return false;
    }
}

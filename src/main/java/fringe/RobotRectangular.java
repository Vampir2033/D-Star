package fringe;

import engine.Robot;
import map.CellFreedom;
import map.ObstacleMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RobotRectangular implements Robot {
    private final int width;
    private final int height;

    public RobotRectangular(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean checkPointUnderRobot(Point robotCenter, Point p, Point vector) {
        Point calc = new Point(Math.abs(p.x-robotCenter.x), Math.abs(p.y- robotCenter.y));
        if(vector.y != 0) {
            return calc.x <= width && calc.y <= height;
        } else {
            return calc.x <= height && calc.y <= width;
        }
    }

    public List<Point> pointsUnderRobot(Point center, Point vector) {
        List<Point> result = new ArrayList<>();
        if(vector.y != 0) {
            for(int x = center.x - width; x <= center.x + width; x++) {
                for(int y = center.y - height; y <= center.y + height; y++) {
                    result.add(new Point(x,y));
                }
            }
        } else {
            for(int x = center.x - height; x <= center.x + height; x++) {
                for(int y = center.y - width; y <= center.y + width; y++) {
                    result.add(new Point(x,y));
                }
            }
        }
        return result;
    }

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
    public boolean checkPointAvailableForRobot(Point point, ObstacleMap obstacleMap, Point vector) {
        return pointsUnderRobot(point, vector).stream()
                .noneMatch(p -> obstacleMap.getCellStatus(p) != CellFreedom.EMPTY);
    }

    @Override
    public String toString() {
        return "ширина="+(width*2+1) + " высота=" + (height*2+1);
    }
}

package astar;

import engine.Robot;
import lombok.Getter;
import map.CellFreedom;
import map.ObstacleMap;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static map.CellFreedom.EMPTY;

public class RobotHexagon implements Robot {
    @Getter
    private final int radius;
    private final Image image;
    private Point position;

    public RobotHexagon(int radius, Image image, Point position) {
        this.radius = radius;
        this.image = image;
        this.position = position;
    }

    @Override
    public boolean checkPointAvailableForRobot(Point point, ObstacleMap map) {
        Robot checkedRobot = new RobotHexagon(radius,null,point);
        for(int x = point.x-radius; x <= point.x+radius; x++) {
            for(int y = point.y-radius; y <= point.y+radius; y++) {
                Point checkedPoint = new Point(x,y);
                if(checkedRobot.checkPointUnderRobot(checkedPoint)
                        && map.getCellStatus(checkedPoint) != EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkPointUnderRobot(Point point) {
        List<Line2D> lines = getHexLines(position,radius);
        Line2D centerToPointLine = new Line2D.Double(position,point);
        for(Line2D hexLine : lines) {
            if(centerToPointLine.intersectsLine(hexLine)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public int imageSize() {
        return 1 + 2*radius;
    }

    @Override
    public Point imagePosWithShift() {
        return new Point(position.x-radius, position.y-radius);
    }

    private List<Line2D> getHexLines(Point center, int radius) {
        List<Point2D> hexTops = new ArrayList<>();
        for(int grad = 0; grad <= 360; grad += 60) {
            double rad = Math.toRadians(grad);
            hexTops.add(new Point2D.Double(
                    Math.cos(rad)*radius + center.getX()
                    ,Math.sin(rad)*radius + center.getY()
            ));
        }
        List<Line2D> hexLines = new ArrayList<>();
        for(int i = 0; i < hexTops.size()-1; i++) {
            Line2D line = new Line2D.Double(hexTops.get(i), hexTops.get(i+1));
            hexLines.add(line);
        }
        return hexLines;
    }

}

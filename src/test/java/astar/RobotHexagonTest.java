package astar;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RobotHexagonTest {
    @Test
    void checkLineIntersect() {
        Line2D main = new Line2D.Double(2,7,7,4);
        Line2D first = new Line2D.Double(0,0,2,5);
        Line2D second = new Line2D.Double(0,0,4,6);
        assertFalse(first.intersectsLine(main));
        assertTrue(second.intersectsLine(main));
    }

    @Test
    void checkLinesHex() {
        List<Point2D> hexTops = new ArrayList<>();
        for(int grad = 0; grad <= 360; grad += 60) {
            System.out.println("grad="+grad);
            Double rad = Math.toRadians(grad);
            hexTops.add(new Point2D.Double(Math.cos(rad),Math.sin(rad)));
            System.out.println(hexTops.get(hexTops.size()-1));
        }
        List<Line2D> hexLines = new ArrayList<>();
        for(int i = 0; i < hexTops.size()-1; i++) {
            Line2D line = new Line2D.Double(hexTops.get(i), hexTops.get(i+1));
            System.out.println("Line "+(i+1)+" = "+ line);
            hexLines.add(line);
        }
    }

    @Test
    void hexLines() {
        assertTrue(checkPointInHex(new Point(2,2),new Point(0,0),6));
        assertFalse(checkPointInHex(new Point(6,0),new Point(0,0),6));
        assertFalse(checkPointInHex(new Point(4,4),new Point(0,0),6));
    }

    private List<Line2D> getHexLines(Point center, int radius) {
        List<Point2D> hexTops = new ArrayList<>();
        for(int grad = 0; grad <= 360; grad += 60) {
            Double rad = Math.toRadians(grad);
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

    private boolean checkPointInHex(Point point, Point hexCenter, int radius) {
        List<Line2D> lines = getHexLines(hexCenter,radius);
        Line2D centerToPointLine = new Line2D.Double(hexCenter,point);
        for(Line2D hexLine : lines) {
            if(centerToPointLine.intersectsLine(hexLine)) {
                return false;
            }
        }
        return true;
    }

}
package astar;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AStarPoint extends Point implements Comparable<AStarPoint> {
    @Setter
    private static Point endPoint;
    @Getter @Setter
    private AStarPoint previousPoint;

    public AStarPoint(Point p, AStarPoint previousPoint) {
        super(p);
        this.previousPoint = previousPoint;
    }

    public AStarPoint(Point p) {
        super(p);
        this.previousPoint = null;
    }

    public int manhattanDistanceToPoint() {
        return manhattanDistanceToPoint(endPoint);
    }

    public int manhattanDistanceToPoint(Point point) {
        return Math.abs(point.x - x) + Math.abs(point.y - y);
    }
    
    

    public double distanceToStart() {
        if(previousPoint == null) {
            return 0.0;
        } else {
            return previousPoint.distanceToStart() + distance(previousPoint);
        }
    }

    public double calcPointCost(Point point) {
        return distanceToStart() + manhattanDistanceToPoint(point);
    }

    public double calcPointCost() {
        return distanceToStart() + manhattanDistanceToPoint();
    }


    public List<Point> getStack() {
        return getStack(0);
    }

//    public Point getVector() {
//
//    }

    private List<Point> getStack(int i) {
        if(previousPoint == null) {
            return new ArrayList<>(i);
        } else if(i==0) {
            return previousPoint.getStack(i+1);
        } else {
            List<Point> result = previousPoint.getStack(i+1);
            result.add(this);
            return result;
        }
    }


    @Override
    public int compareTo(AStarPoint p) {
        double pCost = p.calcPointCost();
        double cost = calcPointCost();
        return java.lang.Double.compare(cost, pCost);
    }
}
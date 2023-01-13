package fringe;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FringeSearchPoint extends Point {
    @Setter private static Point endPoint;

    @Getter private FringeSearchPoint parentPoint;
    private int distanceToStart;

    public FringeSearchPoint(Point p, FringeSearchPoint parentPoint) {
        super(p);
        setParentPoint(parentPoint);
    }

    public void setParentPoint(FringeSearchPoint parentPoint) {
        this.parentPoint = parentPoint;
        if(parentPoint != null) {
            distanceToStart = parentPoint.distanceToStart + 1;
        } else {
            distanceToStart = 0;
        }
    }

    public List<FringeSearchPoint> getStack() {
        List<FringeSearchPoint> result;
        if(parentPoint == null) {
            result = new ArrayList<>();
        } else {
            result = parentPoint.getStack();
        }
        result.add(this);
        return result;
    }

    public int getCost() {
        return distanceToStart + manhattanDistanceToPoint() + getCostChangeDirection();
    }

    public Point getVector() {
        if(parentPoint == null) {
            return new Point(0,-1);
        } else {
            return new Point(x - parentPoint.x, y - parentPoint.y);
        }
    }

    private int getCostChangeDirection() {
        if(parentPoint == null) {
            return 0;
        } else {
            return parentPoint.getVector().equals(getVector()) ? 0:1;
        }
    }

    // алгоритм Манхеттона для приблизительной оценки расстояния до финиша
    private int manhattanDistanceToPoint() {
        return Math.abs(endPoint.x - x) + Math.abs(endPoint.y - y);
    }
}

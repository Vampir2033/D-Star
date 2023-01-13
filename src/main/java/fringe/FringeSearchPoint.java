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
        return distanceToStart + manhattanDistanceToPoint();
    }

    // алгоритм Манхеттона для приблизительной оценки расстояния до финиша
    private int manhattanDistanceToPoint() {
        return Math.abs(endPoint.x - x) + Math.abs(endPoint.y - y);
    }
}

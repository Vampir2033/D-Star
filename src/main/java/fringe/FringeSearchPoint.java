package fringe;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

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

    public int getCost() {
        return distanceToStart + manhattanDistanceToPoint();
    }

    // алгоритм Манхеттона для приблизительной оценки расстояния до финиша
    private int manhattanDistanceToPoint() {
        return Math.abs(endPoint.x - x) + Math.abs(endPoint.y - y);
    }
}

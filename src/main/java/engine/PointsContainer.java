package engine;

import java.awt.*;
import java.util.Map;

public interface PointsContainer {
    Map<Point, CellStatus> getPointStatusMap();

    Map<Point, Point> getPointsVectors();

//    void printStatus();
}

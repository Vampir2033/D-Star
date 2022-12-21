package engine;

import java.awt.*;
import java.util.Map;

public interface PointsContainer {
    //метод, который возвращает набор точек и их статус
    Map<Point, CellStatus> getPointStatusMap();
    //метод, который возвращает направления для точки (стрелочки)
    Map<Point, Point> getPointsVectors();

//    void printStatus();
}

package astar;

import engine.CellStatus;
import engine.PointsContainer;
import engine.Robot;
import map.ObstacleMap;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static astar.AlgorithmIterations.UPDATE_NEIGHBOURS;
import static astar.AlgorithmIterations.INIT_ACTIVE_POINT;
import static map.CellFreedom.EMPTY;

public class AStarAlgorithm implements PointsContainer {
    // карта препятствий
    private final ObstacleMap obstacleMap;
    // точка старта
    private final Point pointStart;
    // точка финиша
    private final Point pointEnd;
    // объект робота
    private final Robot robot;

    // openPoints - нерасчитанные точки
    private final Map<Point, AStarPoint> openPoints;
    //closePoints - точки, которые уже расчитаны и от них уже есть кратчайший путь до точки старта
    private final Set<AStarPoint> closePoints;
    //roadPoints - подсвечивает кртачайшую дорогу красным
    private final Set<AStarPoint> roadPoints;
    //activePoint - текущая активная точка, относительно которой мы просчитываем маршрут (подсвечивается желтым)
    private AStarPoint activePoint;
    //необходим для совместной работы алгоритма и отрисовщика
    private static Integer block = 0;

    // текущее состояние алгоритма
    private AlgorithmIterations activeIteration;

    // конструктор
    public AStarAlgorithm(ObstacleMap obstacleMap, Point pointStart, Point pointEnd, Robot robot) {
        this.obstacleMap = obstacleMap;
        this.pointStart = pointStart;
        this.pointEnd = pointEnd;
        this.robot = robot;

        openPoints = new HashMap<>();
        closePoints = new HashSet<>();
        roadPoints = new HashSet<>();

        activeIteration = INIT_ACTIVE_POINT;
        AStarPoint.setEndPoint(pointEnd);
    }

    // метод, управляющий последовательностью итераций
    public boolean nextIteration() {
        synchronized (block) {
            switch (activeIteration) {
                case INIT_ACTIVE_POINT -> {
                    setActivePoint();
//                System.out.println("Активная клетка: " + activePoint);
                    activeIteration = UPDATE_NEIGHBOURS;
                }
                case UPDATE_NEIGHBOURS -> {
                    getNeighbours(activePoint);
                    activeIteration = INIT_ACTIVE_POINT;
                }
            }
            return pointEnd.equals(activePoint);
        }
    }

    // Поиск новой активной точки
    private void setActivePoint() {
        if(activePoint == null) {
            activePoint = new AStarPoint(pointStart);
        } else {
            openPoints.remove(activePoint);
            closePoints.add(activePoint);
            activePoint = openPoints.values().stream()
                    .min(AStarPoint::compareTo)
                    .orElseThrow();
            activePoint.saveDistanceToStart();
        }
    }

    // Анализ соседей активной точки
    //в качестве параметра пердаем активную точку
    private void getNeighbours(AStarPoint p) {
        for(int x = p.x-1; x <= p.x+1; x++) {
            for(int y = p.y-1; y <= p.y+1; y++){
                AStarPoint neighbour = new AStarPoint(new Point(x,y));
                // проверка, действительно ли позицию можно считать соседом
                //не проверяем ли ячейку, на которой стоим
                if(!neighbour.equals(activePoint)
                        //обращаемся к карте препятствий и проверяем, не явл ли ячейка препятствием
                        && obstacleMap.getCellStatus(neighbour) == EMPTY
                        //проверяем, не находится ли ячейка уже в закрытом списке(окончательно проанализированная ячейка)
                        && !closePoints.contains(neighbour)
                        //может ли робот встать на эту ячейку, не наехав на препятствие
                        && robot.checkPointAvailableForRobot(neighbour,obstacleMap)) {
                    //если сосед находится в открытом списке, проверяем
                    //не будет ли ему ближе идти через активную точку(через ту, которую анализируем)
                    if(openPoints.containsKey(neighbour)) {
                        neighbour = openPoints.get(neighbour);
                        //кратчайшее расстояние от активной точки до старта
                        double activePointDistanceToStart = activePoint.distanceToStart();
                        //получаем текущее расстояние от соседа до точки старта
                        double neighbourDistanceToStart = neighbour.distanceToStart();
                        //считаем расстояние от активной точки до соседа
                        double distanceToNeighbour = activePoint.distance(neighbour);
                        //если расстояние до старта через активную точку будет ближе..
                        if(neighbourDistanceToStart > activePointDistanceToStart + distanceToNeighbour) {
                            //присваиваем соседу предыдущую точку себя
                            neighbour.setPreviousPoint(activePoint);
                        }
                    } else {
                        //если точка не нашлась ни вткрытом, ни в закрытом списке, и на ней потенциально может находиться робот
                        //ставим этой точке предыдущую точку себя и добавляем в открытый список
                        neighbour.setPreviousPoint(activePoint);
                        openPoints.put(neighbour,neighbour);
                    }
                }
            }
        }
    }

//    private double calcCellCost(Point cell, Point cellFrom, Point sellTo) {
//        return cellFrom.distance(cell) + Math.abs(sellTo.x-cell.x) + Math.abs(sellTo.y-cell.y);
//    }
//
//    private Point nextPointToPurpose(Point flowPoint, Point purposePoint) {
//        int maxAbs = Math.max(Math.abs(purposePoint.x- flowPoint.x), Math.abs(purposePoint.y - flowPoint.y));
//        Point shift = new Point((purposePoint.x- flowPoint.x)/maxAbs, (purposePoint.y - flowPoint.y)/maxAbs);
//        return new Point(flowPoint.x + shift.x, flowPoint.y + shift.y);
//    }

    @Override
    public Map<Point, CellStatus> getPointStatusMap() {
        synchronized (block) {
            Map<Point, CellStatus> result = new ConcurrentHashMap<>();
            openPoints.keySet().forEach(p -> result.put(p, CellStatus.OPEN));
            closePoints.forEach(p -> result.put(p, CellStatus.CLOSE));
            if (activePoint != null) {
                result.put(activePoint, CellStatus.ACTIVE);
                roadPoints.clear();
                activePoint.getStack().forEach(point -> result.put(point, CellStatus.WAY));
            }
            return result;
        }
    }

    // метод для отображения стрелок
    @Override
    public Map<Point, Point> getPointsVectors() {
        Map<Point, Point> vectors = new HashMap<>();
        Set<AStarPoint> unionPoints = new HashSet<>();
        unionPoints.addAll(openPoints.values());
        unionPoints.addAll(closePoints);

        for(AStarPoint p : unionPoints) {
            AStarPoint prevPoint = p.getPreviousPoint();
            if(prevPoint != null && !p.equals(pointEnd)) {
                vectors.put(p, new Point(prevPoint.x - p.x,prevPoint.y - p.y));
            }
        }
        return vectors;
    }

    @Override
    public Robot getRobot() {
        return robot;
    }
}

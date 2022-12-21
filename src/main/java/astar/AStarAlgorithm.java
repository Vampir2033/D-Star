package astar;

import engine.CellStatus;
import engine.PointsContainer;
import map.CellFreedom;
import map.ObstacleMap;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static astar.AlgorithmIterations.UPDATE_NEIGHBOURS;
import static astar.AlgorithmIterations.INIT_ACTIVE_POINT;
import static map.CellFreedom.EMPTY;

public class AStarAlgorithm implements PointsContainer {
    //Integer
    //block - синхронизирует обработку алгоритма и его адресовку
    private static Integer block = 0;
    private final Point pointStart;
    private final Point pointEnd;
    //хранит карту препятствий
    private final ObstacleMap obstacleMap;
    //Map - сопоставление пикселя и его данных (выдает данные)
    //openPoints - нерасчитанные точки
    private final Map<Point, AStarPoint> openPoints;
    //Set - структура данных, которая хранит уникальные значения (два одинаковых значения быть не может)
    //closePoints - точки, которые уже расчитаны и от них уже есть кратчайший путь до точки старта
    private final Set<AStarPoint> closePoints;
    //roadPoints - подсвечивает кртачайшую дорогу красным
    private final Set<AStarPoint> roadPoints;
    //activePoint - текущая активная точка, относительно которой мы просчитываем маршрут (подсвечивается желтым)
    private AStarPoint activePoint;
    //activeIteration - текущее состояние алгоритма
    private AlgorithmIterations activeIteration;

    //конструктор (=метод), инициализирует начальное состояние объекта
    public AStarAlgorithm(ObstacleMap obstacleMap, Point pointStart, Point pointEnd) {
        this.obstacleMap = obstacleMap;
        this.pointStart = pointStart;
        this.pointEnd = pointEnd;

        openPoints = new HashMap<>();
        closePoints = new HashSet<>();
        roadPoints = new HashSet<>();

        activeIteration = INIT_ACTIVE_POINT;
        AStarPoint.setEndPoint(pointEnd);
    }

    //запускает расчет текущей стадии и переключает на следующую
    public boolean nextIteration() {
        //для работы отображения и расчета
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

    //реализует поиск и установку следующей активной точки
    private void setActivePoint() {
        if(activePoint == null) {
            activePoint = new AStarPoint(pointStart);
        } else {
            openPoints.remove(activePoint);
            closePoints.add(activePoint);
            //stream -поток, который заменяет цикл for (позволяет пройтись по всему содержимому контейнера)
            activePoint = openPoints.values().stream()
                    //ищем минимальное
                    .min(AStarPoint::compareTo)
                    //иначе
                    .orElseThrow();
            activePoint.saveDistanceToStart();
        }
    }

    //анализируем соседей активной точки
    //точки из закрытого списка и препятствия в расчетах не участуют
    //не исследованные ранее точки попадают в открытый список
    //точки, уже содержащиеся в открытом списке, просчитываются заново и, в случае,
    //если это эффективно, меняют напрвление на активную точку
    private void getNeighbours(AStarPoint p) {
        for(int x = p.x-1; x <= p.x+1; x++) {
            for(int y = p.y-1; y <= p.y+1; y++){
                AStarPoint neighbour = new AStarPoint(new Point(x,y));
                // проверка, действительно ли позицию можно считать соседом
                if(!neighbour.equals(activePoint)
                        && obstacleMap.getCellStatus(neighbour) == EMPTY
                        && !closePoints.contains(neighbour)) {
                    if(openPoints.containsKey(neighbour)) {
                        neighbour = openPoints.get(neighbour);
                        double activePointDistanceToStart = activePoint.distanceToStart();
                        double neighbourDistanceToStart = neighbour.distanceToStart();
                        double distanceToNeighbour = activePoint.distance(neighbour);
                        if(neighbourDistanceToStart > activePointDistanceToStart + distanceToNeighbour) {
                            neighbour.setPreviousPoint(activePoint);
                        }
                    } else {
                        neighbour.setPreviousPoint(activePoint);
                        openPoints.put(neighbour,neighbour);
                    }
                }
            }
        }
    }

//    //
//    private double calcCellCost(Point cell, Point cellFrom, Point sellTo) {
//        return cellFrom.distance(cell) + Math.abs(sellTo.x-cell.x) + Math.abs(sellTo.y-cell.y);
//    }

//    private Point nextPointToPurpose(Point flowPoint, Point purposePoint) {
//        int maxAbs = Math.max(Math.abs(purposePoint.x- flowPoint.x), Math.abs(purposePoint.y - flowPoint.y));
//        Point shift = new Point((purposePoint.x- flowPoint.x)/maxAbs, (purposePoint.y - flowPoint.y)/maxAbs);
//        return new Point(flowPoint.x + shift.x, flowPoint.y + shift.y);
//    }

    //реализуем интерфейс
    @Override
    //CellStatus - состояние точек
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


    @Override
    public Map<Point, Point> getPointsVectors() {
        Map<Point, Point> vectors = new HashMap<>();


        // подключаем стрелочки - кратчайший путь до старта

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


}

package fringe;

import engine.CellStatus;
import engine.PointsContainer;
import engine.Robot;
import map.ObstacleMap;

import java.awt.*;
import java.util.*;
import java.util.List;

import static engine.CellStatus.OPEN;
import static fringe.AlgorithmIteration.*;
import static fringe.AlgorithmIterationResult.*;
import static map.CellFreedom.OBSTRUCTION;

public class FringeSearchAlgorithm implements PointsContainer {
    private final ObstacleMap obstacleMap;
    private final Robot robot;
    private final Point startPoint;
    private final Point endPoint;

    int limit;
    private AlgorithmIteration iteration;
    private List<FringeSearchPoint> fringeNodes;
    private List<FringeSearchPoint> closeNodes;

    public FringeSearchAlgorithm(ObstacleMap obstacleMap, Robot robot, Point startPoint, Point endPoint) {
        this.obstacleMap = obstacleMap;
        this.robot = robot;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        FringeSearchPoint.setEndPoint(endPoint);
        fringeNodes = new ArrayList<>();

        if(obstacleMap.getCellStatus(startPoint) == OBSTRUCTION) {
            throw new RuntimeException("Точка старта находится на препятствии");
        } else if (obstacleMap.getCellStatus(endPoint) == OBSTRUCTION) {
            throw new RuntimeException("Точка финиша находится на препятствии");
        } else if (startPoint.equals(endPoint)) {
            throw new RuntimeException("Точки старта и финиша имеют одинаковые кординаты");
        } else {
            FringeSearchPoint startPointFringe = new FringeSearchPoint(startPoint, null);
            fringeNodes.add(startPointFringe);
            limit = startPointFringe.getCost();
            iteration = INIT_ALGORITHM;
        }

    }

    public AlgorithmIterationResult nextIteration() {
        System.out.println("Итерация: "+iteration+" -> "+switchIteration());
        switch (iteration) {
            case CALC_LIMIT -> {
                OptionalInt opt = fringeNodes.stream().mapToInt(FringeSearchPoint::getCost).min();
                if(opt.isPresent()) {
                    limit = opt.getAsInt();
                    System.out.println("Предел = " + limit);
                    return PROCESSING;
                } else {
                    return WAY_NOT_EXIST;
                }
            } case DETECT_NODES_BY_LIMIT -> {
                checkNextNodes();
                return WAY_FOUND;
            }
        }
        return WAY_FOUND;
    }

    private void checkNextNodes() {
        Point[] vectors = {
                new Point(1,0),
                new Point(0,-1),
                new Point(-1,0),
                new Point(0,1)
        };
        List<FringeSearchPoint> newFringeNodes = new ArrayList<>();
        for(FringeSearchPoint fringeNode : fringeNodes) {
            for(Point vector : vectors) {
                Point position = new Point(fringeNode);
                position.translate(vector.x, vector.y);
                newFringeNodes.add(new FringeSearchPoint(position,fringeNode));
            }
        }
        fringeNodes = newFringeNodes;
    }

    private AlgorithmIteration switchIteration() {
        switch (iteration) {
            case INIT_ALGORITHM, DETECT_NODES_BY_LIMIT -> iteration = CALC_LIMIT;
            case CALC_LIMIT -> iteration = DETECT_NODES_BY_LIMIT;
        }
        return iteration;
    }

    @Override
    public Map<Point, CellStatus> getPointStatusMap() {
        Map<Point,CellStatus> resultMap = new HashMap<>();
        for(Point p : fringeNodes) {
            resultMap.put(p, OPEN);
        }
        return resultMap;
    }

    @Override
    public Map<Point, Point> getPointsVectors() {
        return new HashMap<>();
    }

    @Override
    public Robot getRobot() {
        return null;
    }
}

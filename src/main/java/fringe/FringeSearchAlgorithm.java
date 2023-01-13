package fringe;

import engine.CellStatus;
import engine.PointsContainer;
import engine.Robot;
import map.CellFreedom;
import map.ObstacleMap;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static engine.CellStatus.*;
import static fringe.AlgorithmIteration.*;
import static fringe.AlgorithmIterationResult.*;
import static map.CellFreedom.EMPTY;
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
    private FringeSearchPoint shortestWay;

    public FringeSearchAlgorithm(ObstacleMap obstacleMap, Robot robot, Point startPoint, Point endPoint) {
        this.obstacleMap = obstacleMap;
        this.robot = robot;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        FringeSearchPoint.setEndPoint(endPoint);
        fringeNodes = new ArrayList<>();
        closeNodes = new ArrayList<>();
        shortestWay = null;

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
                Optional<FringeSearchPoint> optP = fringeNodes.stream().filter(p -> p.equals(endPoint)).findAny();
                if (optP.isPresent()) {
                    shortestWay = optP.get();
                    return WAY_FOUND;
                } else {
                    return PROCESSING;
                }
            }
        }
        return WAY_NOT_EXIST;
    }

    private void checkNextNodes() {
        Point[] vectors = {
                new Point(1,0),
                new Point(0,-1),
                new Point(-1,0),
                new Point(0,1)
        };
        List<FringeSearchPoint> newFringeNodes = new ArrayList<>();
        List<FringeSearchPoint> passedNodes = new ArrayList<>();
        for(FringeSearchPoint fringeNode : fringeNodes.stream().filter(p -> p.getCost() <= limit).collect(Collectors.toList())) {
            for(Point vector : vectors) {
                Point position = new Point(fringeNode);
                position.translate(vector.x, vector.y);
                FringeSearchPoint newFringeNode = new FringeSearchPoint(position,fringeNode);
                if(checkPosition(newFringeNode)) {
                    Optional<FringeSearchPoint> optP = newFringeNodes.stream().filter(p -> p.equals(newFringeNode)).findAny();
                    if (optP.isPresent()) {
                        if (newFringeNode.getCost() < optP.get().getCost()) {
                            optP.get().setParentPoint(fringeNode);
                        }
                    } else {
                        newFringeNodes.add(newFringeNode);
                    }
                }
            }
            passedNodes.add(fringeNode);
        }
        closeNodes.addAll(passedNodes);
        fringeNodes = fringeNodes.stream().filter(p -> !passedNodes.contains(p)).collect(Collectors.toList());
        fringeNodes.addAll(newFringeNodes);
        fringeNodes = fringeNodes.stream().filter(p -> !closeNodes.contains(p)).collect(Collectors.toList());
    }

    private boolean checkPosition(FringeSearchPoint p) {
        CellFreedom cellFreedom = obstacleMap.getCellStatus(p);
        return cellFreedom == EMPTY;
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
        fringeNodes.forEach(n -> resultMap.put(n, OPEN));
        closeNodes.forEach(n -> resultMap.put(n, CLOSE));
        if(shortestWay != null) {
            shortestWay.getStack().forEach(n -> resultMap.put(n, WAY));
        }
        if(iteration == CALC_LIMIT) {
            fringeNodes.stream().filter(p -> p.getCost() <= limit).forEach(n -> resultMap.put(n, ACTIVE));
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

package astar;

import engine.CellStatus;
import engine.PointsContainer;
import map.CellFreedom;
import map.ObstacleMap;

import java.awt.*;
import java.util.*;
import java.util.List;

import static astar.AlgorithmIterations.UPDATE_NEIGHBOURS;
import static astar.AlgorithmIterations.INIT_ACTIVE_POINT;

public class AStarAlgorithm implements PointsContainer {
    private final ObstacleMap obstacleMap;
    private final Point pointStart;
    private final Point pointEnd;

    private final Map<Point, AStarPoint> openPoints;
    private final Set<Point> closePoints;
    private final Set<Point> roadPoints;
    private AStarPoint activePoint;

    private AlgorithmIterations activeIteration;

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

    public boolean nextIteration() {
        switch (activeIteration) {
            case INIT_ACTIVE_POINT -> {
                setActivePoint();
                activeIteration = UPDATE_NEIGHBOURS;
            }
            case UPDATE_NEIGHBOURS -> {
                getNeighbours(activePoint);
                activeIteration = INIT_ACTIVE_POINT;
            }
        }
        return pointEnd.equals(activePoint);
    }

    private void setActivePoint() {
        if(activePoint == null) {
            activePoint = new AStarPoint(pointStart);
        } else {
            openPoints.remove(activePoint);
            closePoints.add(activePoint);
            activePoint = openPoints.values().stream()
                    .min(AStarPoint::compareTo)
                    .orElseThrow();
        }
    }

//    private void updateNeighbours() {
//        openPoints.addAll(getNeighbours(activePoint));
//    }

    private List<AStarPoint> getNeighbours(AStarPoint p) {
        List<AStarPoint> result = new ArrayList<>();
        for(int x = p.x-1; x <= p.x+1; x++) {
            for(int y = p.y-1; y <= p.y+1; y++){
                if(x != p.x || y != p.y) {
                    AStarPoint neighbour = new AStarPoint(new Point(x,y), p);
                    if(obstacleMap.getCellStatus(neighbour) == CellFreedom.EMPTY && !closePoints.contains(neighbour)) {
                        if(openPoints.containsKey(neighbour) && (openPoints.get(neighbour).calcPointCost() > neighbour.calcPointCost())) {
                            openPoints.get(neighbour).setPreviousPoint(p);
                        } else {
                            openPoints.put(neighbour, neighbour);
                        }
                    }
                }
            }
        }
        return result;
    }

    private double calcCellCost(Point cell, Point cellFrom, Point sellTo) {
        return cellFrom.distance(cell) + Math.abs(sellTo.x-cell.x) + Math.abs(sellTo.y-cell.y);
    }

    private Point nextPointToPurpose(Point flowPoint, Point purposePoint) {
        int maxAbs = Math.max(Math.abs(purposePoint.x- flowPoint.x), Math.abs(purposePoint.y - flowPoint.y));
        Point shift = new Point((purposePoint.x- flowPoint.x)/maxAbs, (purposePoint.y - flowPoint.y)/maxAbs);
        return new Point(flowPoint.x + shift.x, flowPoint.y + shift.y);
    }

    @Override
    public Map<Point, CellStatus> getPointStatusMap() {
        Map<Point, CellStatus> result = new HashMap<>();
        openPoints.keySet().forEach(p -> result.put(p,CellStatus.OPEN));
        closePoints.forEach(p -> result.put(p,CellStatus.CLOSE));
        if(activePoint != null) {
            result.put(activePoint, CellStatus.ACTIVE);
            roadPoints.clear();
            activePoint.getStack().forEach(point -> result.put(point,CellStatus.WAY));
        }
        return result;
    }
}

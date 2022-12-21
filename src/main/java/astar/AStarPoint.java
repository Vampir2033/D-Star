package astar;

//создает функцию, которая позволяет получить значение переменной
import lombok.Getter;
//создает функцию, которая позволяет записать значение переменную
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//Point - хранит в себе значения Х и У
//AStarPoint - предоставляет методы для работы с Х и У (хранит указатель на предыдущую точку и на конечную точку)
//свойства Point передаем AStarPoint через extends
public class AStarPoint extends Point implements Comparable<AStarPoint> {
    // точка назначения
    @Setter
    private static Point endPoint;
    // предыдущая ячейка
    @Getter @Setter
    private AStarPoint previousPoint;
    // сохранённое реальное расстояние до точки старта
    @Getter @Setter
    private java.lang.Double bufferedDistanceToStart;

    public AStarPoint(Point p, AStarPoint previousPoint) {
        super(p);
        this.previousPoint = previousPoint;
        this.bufferedDistanceToStart = null;
    }

    public AStarPoint(Point p) {
        super(p);
        this.previousPoint = null;
    }

    // алгоритм Манхеттона для приблизительной оценки расстояния до финиша
    public int manhattanDistanceToPoint(Point point) {
        return Math.abs(point.x - x) + Math.abs(point.y - y);
    }

    //метод позволяет определить расстояние от текущей точки до старта
    public double distanceToStart() {
        // если это и есть точка старта - расстояние 0
        if(previousPoint == null) {
            return 0.0;
        }
        // если у нас уже сохранено расстояние - возвращаем его
        else if(bufferedDistanceToStart != null) {
            return bufferedDistanceToStart;
        }
        // иначе идём к предыдущей точке рекурсивно, прибавляя расстояние к соседу
        else {
            //обращаемся к предыдущей точке, берем ее расстояние до старта и прибавляем красстоянию до предыдущ точки
            return previousPoint.distanceToStart() + distance(previousPoint);
        }
    }

    // Оценка общего веса ячейки
    public double calcPointCost(Point point) {
        return distanceToStart() + manhattanDistanceToPoint(point);
    }

    public double calcPointCost() {
        return calcPointCost(endPoint);
    }

    // сохранение расстояния до старта в буфер
    public void saveDistanceToStart() {
        bufferedDistanceToStart = distanceToStart();
    }

    public List<Point> getStack() {
        return getStack(0);
    }

    // получаем последовательность ячеек до старта в виде списка
    private List<Point> getStack(int i) {
        if(previousPoint == null) {
            return new ArrayList<>(i);
        } else if(i==0) {
            return previousPoint.getStack(i+1);
        } else {
            List<Point> result = previousPoint.getStack(i+1);
            result.add(this);
            return result;
        }
    }

    @Override
    public int compareTo(AStarPoint p) {
        double pCost = p.calcPointCost();
        double cost = calcPointCost();
        return java.lang.Double.compare(cost, pCost);
    }
}

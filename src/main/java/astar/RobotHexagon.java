package astar;

import engine.Robot;
import lombok.Getter;
import map.CellFreedom;
import map.ObstacleMap;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static map.CellFreedom.EMPTY;

public class RobotHexagon implements Robot {
    // Радиус робота
    @Getter
    private final int radius;
    // картинка робота
    private final Image image;
    // текущая позиция робота
    private Point position;

    public RobotHexagon(int radius, Image image, Point position) {
        this.radius = radius;
        this.image = image;
        this.position = position;
    }

    //метод для проверки, может ли центр робота находиться в этой ячейке
    @Override
    public boolean checkPointAvailableForRobot(Point point, ObstacleMap map) {
        //создаем новый объект робота в исследуемой ячейке
        Robot checkedRobot = new RobotHexagon(radius,null,point);
        //перебираем точки, на которые робот может заехать (с учетом размера)
        for(int x = point.x-radius; x <= point.x+radius; x++) {
            for(int y = point.y-radius; y <= point.y+radius; y++) {
                Point checkedPoint = new Point(x,y);
                //проверяем, действительно ли робот заезжает на эту ячейку
                //и, если заезжает, то является ли эта ячейка пустой
                if(checkedRobot.checkPointUnderRobot(checkedPoint)
                        && map.getCellStatus(checkedPoint) != EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    // проверка, находится ли ячейка под роботом
    @Override
    public boolean checkPointUnderRobot(Point point) {
        //получаем линии робота (6 отрезков)
        //Line2D - хранит координаты начала и конца отрезков
        List<Line2D> lines = getHexLines(position,radius);
        //создаем отрезок от центра до исследуемой точки
        Line2D centerToPointLine = new Line2D.Double(position,point);
        //проверяем, не пересекает ли этот отрезок любую из 6 отрезков периметра робота
        for(Line2D hexLine : lines) {
            //intersectsLine - встроенный метод проверки, не пересекаются ли отрезки
            if(centerToPointLine.intersectsLine(hexLine)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public int imageSize() {
        return 1 + 2*radius;
    }

    @Override
    public Point imagePosWithShift() {
        return new Point(position.x-radius, position.y-radius);
    }

    // получение линий шестиугольника (вычисление периметра)
    private List<Line2D> getHexLines(Point center, int radius) {
        //объявляем массив вершин
        List<Point2D> hexTops = new ArrayList<>();
        //проходимся от 0 до 360 градуса с шагом 60 градусов
        for(int grad = 0; grad <= 360; grad += 60) {
            //переводим градусы в радианы
            double rad = Math.toRadians(grad);
            //вычисление проекций на ось Х и ось У, и находим координаты вершин
            //координаты дробные
            hexTops.add(new Point2D.Double(
                    Math.cos(rad)*radius + center.getX()
                    ,Math.sin(rad)*radius + center.getY()
            ));
        }
        //объявляем массив отрезков периметра
        List<Line2D> hexLines = new ArrayList<>();
        for(int i = 0; i < hexTops.size()-1; i++) {
            //создаем отрезки (начало+конец)
            Line2D line = new Line2D.Double(hexTops.get(i), hexTops.get(i+1));
            hexLines.add(line);
        }
        return hexLines;
    }

}

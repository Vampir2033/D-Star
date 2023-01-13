import astar.AStarAlgorithm;
import astar.RobotHexagon;
import engine.Drower;
import engine.GetImage;
import engine.Robot;
import fringe.AlgorithmIterationResult;
import fringe.FringeSearchAlgorithm;
import fringe.RobotRectangular;
import map.ObstacleMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import static fringe.AlgorithmIterationResult.PROCESSING;
import static fringe.AlgorithmIterationResult.WAY_FOUND;

public class Application {

    public static void main(String[] args) throws IOException {
        // карту устанавливать здесь
        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/32_19.bmp");
        Point firstPoint = new Point(3,10);
        Point purposePoint = new Point(28,4);
        double scale = 30;
        int radius = 2;

//        System.out.println("Введите координаты старта");
//        firstPoint = inputPoint();
//        System.out.println("Введите координаты финиша");
//        purposePoint = inputPoint();
        assert imageFileUrl != null;
        // инициализация карты
        BufferedImage bufferedImage = ImageIO.read(imageFileUrl);
        // анализ препятствий
        ObstacleMap map = new ObstacleMap(bufferedImage);
        // инициализация робота
//        RobotHexagon robotHexagon = new RobotHexagon(radius, GetImage.getHexagonImage(), firstPoint);
        Robot robot = new RobotRectangular();
        // инициализация алгоритма поиска
//        AStarAlgorithm searchAlgorithm = new AStarAlgorithm(map,firstPoint,purposePoint, robotHexagon);
        FringeSearchAlgorithm searchAlgorithm = new FringeSearchAlgorithm(map,robot,firstPoint,purposePoint);
        // инициализация отрисовщика
        Drower drower = new Drower(bufferedImage, scale, firstPoint, purposePoint, searchAlgorithm);
        drower.repaint();
        // старт алгоритма
//        boolean algorithmEnd = false;
        AlgorithmIterationResult iterationResult;
        do {
            iterationResult = searchAlgorithm.nextIteration();
//                algorithmEnd = searchAlgorithm.nextIteration();
            drower.repaint();
            // задержку регулировать здесь
//                delayStep(1);
//                delayStep(5);
//                delayStep(10);
//            delayStep(100);
                delayStep(500);
//                delayStep(1000);
        } while (iterationResult == PROCESSING);
        if(iterationResult == WAY_FOUND) {
            System.out.println("Кратчайший путь найден");
        } else {
            System.out.println("Невозможно найти кратчайший путь");
        }

    }

    private static void delayStep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Point inputPoint() {
        Scanner in = new Scanner(System.in);
        System.out.print("X > ");
        int x = in.nextInt();
        System.out.print("Y > ");
        int y = in.nextInt();
        Point p = new Point(x,y);
        System.out.println("Введённая точка: " + p);
        return p;
    }
}

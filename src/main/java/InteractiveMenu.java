import engine.Drower;
import engine.Robot;
import fringe.AlgorithmIterationResult;
import fringe.FringeSearchAlgorithm;
import fringe.RobotRectangular;
import map.ObstacleMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import static fringe.AlgorithmIterationResult.PROCESSING;
import static fringe.AlgorithmIterationResult.WAY_FOUND;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class InteractiveMenu {
    private BufferedImage mapImage;
    private ObstacleMap obstacleMap;
    private Double scale;
    private Double occupancyInPercent;
    private Robot robot = new RobotRectangular(0,0);
    private Point startPoint;
    private Point finishPoint;
    private int delay = 100;
    
    private static String menu = 
            "1 - Задать параметры карты\n" +
                    "2 - Задать параметры робота\n" +
                    "3 - Задать точку старта\n" +
                    "4 - Задать точку финиша\n" +
                    "5 - Посмотреть введённые значения\n" +
                    "6 - Выполнить алгоритм поиска\n" +
                    "7 - Показать карту\n" +
                    "8 - Задать время между итерациями\n" +
                    "0 - Выйти из программы\n";

    public void start() {
        System.out.println(menu);
        while (true) {
            try {
                System.out.println("Показать меню - 9");
                int command = inputInt("Введите команду");
                switch (command) {
                    case 1 -> inputMap();
                    case 2 -> inputRobot();
                    case 3 -> inputPointStart();
                    case 4 -> inputPointFinish();
                    case 5 -> showParameters();
                    case 6 -> runAlgorithm();
                    case 7 -> showMap();
                    case 8 -> delay = inputInt("Введите задерку (ms)");
                    case 9 -> System.out.println(menu);
                    case 0 -> System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    private void runAlgorithm() {
        if(!checkParameters()) {
            System.out.println("Не все параметры введены");
            return;
        }
        // инициализация алгоритма поиска
        FringeSearchAlgorithm searchAlgorithm = new FringeSearchAlgorithm(obstacleMap,robot,startPoint,finishPoint);
        // инициализация отрисовщика
        Drower drower = new Drower(mapImage, scale, startPoint, finishPoint, searchAlgorithm);
        drower.repaint();

        AlgorithmIterationResult iterationResult;
        do {
            iterationResult = searchAlgorithm.nextIteration();
            drower.repaint();
            delayStep();
        } while (iterationResult == PROCESSING);
        if(iterationResult == WAY_FOUND) {
            System.out.println("Кратчайший путь найден");
        } else {
            System.out.println("Невозможно найти кратчайший путь");
        }

    }

    private void delayStep() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showMap() {
        if(mapImage != null) {
            Drower drower = new Drower(mapImage, scale, startPoint, finishPoint, null);
            drower.repaint();
        } else {
            System.out.println("Необходимо сначала задать параметры карты");
        }
    }

    private void showParameters() {
        System.out.println("Карта: " + obstacleMap);
        System.out.println("Заполнение (%): " + occupancyInPercent);
        System.out.println("Масштаб: " + scale);
        System.out.println("Робот: " + robot);
        System.out.println("Точка старта: " + startPoint);
        System.out.println("Точка финиша: " + finishPoint);
        System.out.println("Время между итерациями (ms): " + delay);
    }

    private boolean checkParameters() {
        return obstacleMap!=null && robot!=null && startPoint!=null && finishPoint!=null;
    }

    private void inputMap() {
        int width = inputInt("Введите ширину");
        int height = inputInt("Введите высоту");
        occupancyInPercent = inputDouble("Введите заполнение в процентах");
        scale = inputDouble("Введите масштаб");

        mapImage = generateMap(width,height,occupancyInPercent);
        obstacleMap = new ObstacleMap(mapImage);
    }

    private void inputRobot() {
        int width = inputInt("Введите отступ от центра в ширину");
        int height = inputInt("Введите отступ от центра в высоту");

        robot = new RobotRectangular(width,height);
    }

    private void inputPointStart() {
        System.out.println("Введите кординаты точки старта");
        startPoint = inputPoint();
    }

    private void inputPointFinish() {
        System.out.println("Введите кординаты точки финиша");
        finishPoint = inputPoint();
    }

    private static Point inputPoint() {
        int x = inputInt("\tX");
        int y = inputInt("\tY");
        return new Point(x,y);
    }

    private static BufferedImage generateMap(int width, int height, double occupancyInPercent) {
        double occupancy = occupancyInPercent / 100.0;
        BufferedImage img = new BufferedImage(width,height, TYPE_INT_RGB);
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                img.setRGB(x,y,(Math.random()<occupancy ? Color.BLACK : Color.WHITE).getRGB());
            }
        }
        return img;
    }

    private static int inputInt(String text) {
        System.out.print(text + " > ");
        Scanner in = new Scanner(System.in);
        return in.nextInt();
    }

    private static double inputDouble(String text) {
        System.out.print(text + " > ");
        Scanner in = new Scanner(System.in);
        return in.nextDouble();
    }

}

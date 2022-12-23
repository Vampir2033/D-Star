import astar.AStarAlgorithm;
import astar.RobotHexagon;
import engine.Drower;
import engine.GetImage;
import map.ObstacleMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) throws IOException {
        // карту устанавливать здесь
//        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/32_19.bmp");
//        Point firstPoint = new Point(3,10);
//        Point purposePoint = new Point(28,4);
//        double scale = 50;
//        int radius = 2;

        // Изображение
//        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/7_5.bmp");
        // Точка старта
//        Point firstPoint = new Point(1,2);
        // Точка финиша
//        Point purposePoint = new Point(5,2);
        // масштаб
//        double scale = 64;
        // Радиус робота от 1 до infinity
//        int radius = 1;

//        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/7_5_v2.bmp");
//        Point firstPoint = new Point(4,0);
//        Point purposePoint = new Point(1,4);
//        double scale = 64;
//        int radius = 1;

        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/128x64_labyrinth.bmp");
        Point firstPoint = new Point(73,10);
        Point purposePoint = new Point(80,60);
        double scale = 13;
        int radius = 2;

//        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/128x64_width_lab.bmp");
//        Point firstPoint = new Point(109,35);
//        Point purposePoint = new Point(110,10);
//        double scale = 13;
//        int radius = 4;


        System.out.println("Введите координаты старта");
        firstPoint = inputPoint();
        System.out.println("Введите координаты финиша");
        purposePoint = inputPoint();
        assert imageFileUrl != null;
        // инициализация карты
        BufferedImage bufferedImage = ImageIO.read(imageFileUrl);
        // анализ препятствий
        ObstacleMap map = new ObstacleMap(bufferedImage);
        // инициализация робота
        RobotHexagon robotHexagon = new RobotHexagon(radius, GetImage.getHexagonImage(), firstPoint);
        // инициализация алгоритма поиска
        AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(map,firstPoint,purposePoint, robotHexagon);
        // инициализация отрисовщика
        Drower drower = new Drower(bufferedImage, scale, firstPoint, purposePoint, aStarAlgorithm);
        drower.repaint();
        // старт алгоритма
        boolean algorithmEnd = false;
        try {
            do {
                algorithmEnd = aStarAlgorithm.nextIteration();
                drower.repaint();
                // задержку регулировать здесь
//                delayStep(1);
//                delayStep(5);
//                delayStep(10);
//                delayStep(100);
//                delayStep(500);
//                delayStep(1000);
            } while (!algorithmEnd);
            System.out.println("Алгоритм нашёл кратчайший путь");
        } catch (RuntimeException e) {
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

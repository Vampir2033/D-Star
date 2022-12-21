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

public class Application {

    public static void main(String[] args) throws IOException {
        // карту устанавливать здесь
//        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/32_19.bmp");
//        Point firstPoint = new Point(3,10);
//        Point purposePoint = new Point(28,4);
//        double scale = 50;

//        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/7_5.bmp");
//        Point firstPoint = new Point(1,2);
//        Point purposePoint = new Point(5,2);
//        double scale = 64;

//        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/7_5_v2.bmp");
//        Point firstPoint = new Point(4,0);
//        Point purposePoint = new Point(1,4);
//        double scale = 64;

        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/128x64_labyrinth.bmp");
        Point firstPoint = new Point(73,10);
        Point purposePoint = new Point(80,60);
        double scale = 10;

        assert imageFileUrl != null;
        BufferedImage bufferedImage = ImageIO.read(imageFileUrl);
        ObstacleMap map = new ObstacleMap(bufferedImage);
        RobotHexagon robotHexagon = new RobotHexagon(4, GetImage.getHexagonImage(), firstPoint);
        AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(map,firstPoint,purposePoint, robotHexagon);
        Drower drower = new Drower(bufferedImage, scale, firstPoint, purposePoint, aStarAlgorithm);
        drower.repaint();
        boolean algorithmEnd = false;
        do {
                algorithmEnd = aStarAlgorithm.nextIteration();
                drower.repaint();
                // задержку регулировать здесь
//            try {
////                Thread.sleep(1000);
////                Thread.sleep(10);
////                Thread.sleep(5);
//                Thread.sleep(1);
////                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } while (!algorithmEnd);
//        drower.repaint();
        System.out.println("Алгоритм завершил свою работу");
    }
}

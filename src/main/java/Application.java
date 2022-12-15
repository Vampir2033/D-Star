import astar.AStarAlgorithm;
import engine.Drower;
import map.ObstacleMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Application {

    public static void main(String[] args) throws IOException {
        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/32_19.bmp");
        Point firstPoint = new Point(3,10);
        Point purposePoint = new Point(28,4);
        double scale = 32;

//        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/7_5.bmp");
//        Point firstPoint = new Point(1,2);
//        Point purposePoint = new Point(5,2);
//        double scale = 64;

//        URL imageFileUrl = Application.class.getClassLoader().getResource("images/maps/7_5_v2.bmp");
//        Point firstPoint = new Point(4,0);
//        Point purposePoint = new Point(1,4);
//        double scale = 64;

        assert imageFileUrl != null;
        BufferedImage bufferedImage = ImageIO.read(imageFileUrl);
        ObstacleMap map = new ObstacleMap(bufferedImage);
        AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(map,firstPoint,purposePoint);
        Drower drower = new Drower(bufferedImage, scale, firstPoint, purposePoint, aStarAlgorithm);
        boolean algorithmEnd = false;
        do {
            algorithmEnd = aStarAlgorithm.nextIteration();
            drower.repaint();
            try {
//                Thread.sleep(1000);
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!algorithmEnd);
        System.out.println("Алгоритм завершил свою работу");
    }
}

import engine.Drower;
import engine.PolyLine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Application {

    public static void main(String[] args) throws IOException {
//        URL imageFileUrl = Application.class.getClassLoader().getResource("2walls.bmp");
//        URL imageFileUrl = Application.class.getClassLoader().getResource("3walls_50x25.bmp");
        URL imageFileUrl = Application.class.getClassLoader().getResource("128_64.bmp");
        assert imageFileUrl != null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFileUrl);
            ObstacleMap map = new ObstacleMap(bufferedImage);
//            PolyLine polyLine = new PolyLine()
//                    .addPoint(50,50)
//                    .addPoint(200,250)
//                    .addPoint(250,50);
//            PolyLine polyLine = new PolyLine()
//                    .addPoint(5,5)
//                    .addPoint(19,5)
//                    .addPoint(19,20)
//                    .addPoint(29,20)
//                    .addPoint(31,4)
//                    .addPoint(45,12);
            PolyLine polyLine = new PolyLine();

            Point firstPoint = new Point(32,42);
            Point purposePoint = new Point(90,40);
            Drower drower = new Drower(bufferedImage, polyLine, 5.0, firstPoint, purposePoint);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

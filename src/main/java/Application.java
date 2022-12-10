import engine.Drower;
import engine.PolyLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Application {

    public static void main(String[] args) throws IOException {
//        URL imageFileUrl = Application.class.getClassLoader().getResource("2walls.bmp");
        URL imageFileUrl = Application.class.getClassLoader().getResource("3walls_50x25.bmp");
        assert imageFileUrl != null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFileUrl);
//            PolyLine polyLine = new PolyLine()
//                    .addPoint(10,10)
//                    .addPoint(200,250)
//                    .addPoint(250,10);
            PolyLine polyLine = new PolyLine()
                    .addPoint(2,2)
                    .addPoint(19,5)
                    .addPoint(19,20)
                    .addPoint(29,20)
                    .addPoint(31,4)
                    .addPoint(49,12);
            Drower drower = new Drower(bufferedImage, polyLine, 30);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

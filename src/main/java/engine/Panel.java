package engine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Panel extends JPanel {
    private final Image image;
    private final PolyLine polyLine;
    private final double scale;
    private final Point startPoint;
    private final Point purposePoint;

    public Panel(Image image, PolyLine polyLine, double scale, Point startPoint, Point purposePoint) {
        this.image = image;
        this.polyLine = polyLine;
        this.scale = scale;
        this.startPoint = startPoint;
        this.purposePoint = purposePoint;
        setPreferredSize(new Dimension((int) (image.getWidth(null)*scale), (int) (image.getHeight(null)*scale)));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(scale,scale);
        g2.drawImage(image,0,0,null);
        g2.setColor(Color.red);
        g2.drawPolyline(polyLine.getXArr(),polyLine.getYArr(),polyLine.size());
        try {
            Image startImage = getImageFromRes("start64.png");
            g2.drawImage(startImage, startPoint.x-2, startPoint.y-2, 4, 4,null);
            Image purposeImage = getImageFromRes("purpose64.png");
            g2.drawImage(purposeImage, purposePoint.x-2, purposePoint.y-2, 4, 4,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image getImageFromRes(String imgName) throws IOException {
        URL url = getClass().getClassLoader().getResource(imgName);
        assert url != null;
        return ImageIO.read(url);
    }

    private Point shiftImageToCenter(Point leftUpPoint, Image image) {
        return new Point(leftUpPoint.x - (int)Math.ceil(image.getWidth(null)/2.0),
                leftUpPoint.y - (int)Math.ceil(image.getHeight(null)/2.0));
    }
}

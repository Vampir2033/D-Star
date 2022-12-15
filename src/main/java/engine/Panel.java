package engine;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static engine.GetImage.getImageFromRes;
import static engine.GetImage.getStartImage;

public class Panel extends JPanel {
    private final Image image;
//    private final PolyLine polyLine;
    private final double scale;
    private final Point startPoint;
    private final Point purposePoint;
    private final PointsContainer pointsContainer;

    public Panel(Image image, double scale, Point startPoint, Point purposePoint, PointsContainer pointsContainer) {
        this.image = image;
//        this.polyLine = polyLine;
        this.scale = scale;
        this.startPoint = startPoint;
        this.purposePoint = purposePoint;
        this.pointsContainer = pointsContainer;
        setPreferredSize(new Dimension((int) (image.getWidth(null)*scale), (int) (image.getHeight(null)*scale)));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(scale,scale);
        g2.drawImage(image,0,0,null);
        var points = pointsContainer.getPointStatusMap();
        points.forEach((point,status) -> drawCell(g2,point,status));
        Image startImg = getStartImage();
        g2.drawImage(startImg,startPoint.x,startPoint.y,1,1,null);
        Image purposeImg = GetImage.getPurposeImage();
        g2.drawImage(purposeImg,purposePoint.x,purposePoint.y,1,1,null);
    }

    private Point shiftImageToCenter(Point leftUpPoint, Image image) {
        return new Point(leftUpPoint.x - (int)Math.ceil(image.getWidth(null)/2.0),
                leftUpPoint.y - (int)Math.ceil(image.getHeight(null)/2.0));
    }

    private void drawCell(Graphics graphics, Point point, CellStatus cellStatus) {
        graphics.drawImage(GetImage.getImageByStatus(cellStatus),point.x,point.y,1,1,null);
    }
}

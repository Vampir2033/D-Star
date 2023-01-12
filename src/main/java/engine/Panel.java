package engine;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static engine.CellStatus.UNDER_ROBOT;
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
        var vectors = pointsContainer.getPointsVectors();
        vectors.forEach((point, vector) -> drawVector(g2,point,vector));
        Robot robot = pointsContainer.getRobot();
        if(robot != null) {
            Point robotPoint = robot.imagePosWithShift();
            int robotImageSize = robot.imageSize();
//        Image underRobotCellImg = GetImage.getImageByStatus(UNDER_ROBOT);
//        for(int x = robotPoint.x; x <= robotPoint.x+robotImageSize; x++) {
//            for(int y = robotPoint.y; y <= robotPoint.y+robotImageSize; y++) {
//                Point checkedPoint = new Point(x,y);
//                if(robot.checkPointUnderRobot(checkedPoint)) {
//                    g2.drawImage(underRobotCellImg,x,y,1,1,null);
//                }
//            }
//        }
            Image robotImage = robot.getImage();
            g2.drawImage(robotImage,robotPoint.x+1,robotPoint.y+1,robotImageSize-2,robotImageSize-2,null);
        }
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
    
    private void drawVector(Graphics graphics, Point point, Point vector) {
        graphics.drawImage(GetImage.getImageByVector(vector),point.x,point.y,1,1,null);
    }
}

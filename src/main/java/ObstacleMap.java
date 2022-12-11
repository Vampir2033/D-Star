import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ObstacleMap {
    private final byte[][] data;
    private final int width;
    private final int height;

    public ObstacleMap(byte[][] data, int width, int height) {
        this.data = data;
        this.width = width;
        this.height = height;
    }

    public ObstacleMap(BufferedImage bufferedImage) {
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
        data = new byte[width][height];
        for(int x = 0; x < bufferedImage.getWidth(); x++) {
            for(int y = 0; y < bufferedImage.getHeight(); y++) {
                data[x][y] = (byte) (new Color(bufferedImage.getRGB(x,y)).equals(Color.WHITE) ? 0:1);
            }
        }
    }

    public CellStatus getCellStatus(int x, int y) {
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return CellStatus.OBSTRUCTION;
        } else {
            return data[x][y] == 0 ? CellStatus.EMPTY : CellStatus.OBSTRUCTION;
        }
    }

    public CellStatus getCellStatus(Point p){
        return getCellStatus(p.x,p.y);
    }

    public boolean checkPass(Point p1, Point p2) {
        if(p1.equals(p2)) {
            return false;
        } else if (!CellStatus.EMPTY.equals(getCellStatus(p1)) || !CellStatus.EMPTY.equals(getCellStatus(p2))) {
            return false;
        } else {
            List<Point> passCells = calcPassCells(p1,p2);
            for (Point cell : passCells) {
                if(!CellStatus.EMPTY.equals(getCellStatus(cell))) {
                    return false;
                }
            }
        }
        return true;
    }

    
    private List<Point> calcPassCells(Point p1, Point p2) {
        double cA = (p1.getY()-p2.getY()) / (p1.getX()-p2.getX());
        double cB = p1.getY()-cA*p1.getX();
        List<Point> pointList = new ArrayList<>();
        int minX = Math.min(p1.x,p2.x);
        int maxX = Math.max(p1.x,p2.x);
        int vectorY = cA >= 0 ? 1:-1;
        int previousY = (int) lineFunction(minX,cA,cB);
        for(int x = minX+1; x <= maxX; x++) {
            int flowY = (int) lineFunction(x,cA,cB);
            for(int y = previousY; vectorY > 0 ? y <= flowY : y >= flowY; y+=vectorY) {
                pointList.add(new Point(x-1,y));
            }
            previousY = flowY;
        }
        return pointList;
    }

    private double lineFunction(int x, double cA, double cB){
        return cA*x + cB;
    }
}

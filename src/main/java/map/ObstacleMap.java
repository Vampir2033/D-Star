package map;

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

    //передаем картинку
    public ObstacleMap(BufferedImage bufferedImage) {
        //вычисляем ее размер (высота и ширина)
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
        //инициализируем карту препятствий (это массив размера ширины и высоты)
        data = new byte[width][height];
        for(int x = 0; x < bufferedImage.getWidth(); x++) {
            for(int y = 0; y < bufferedImage.getHeight(); y++) {
                //заполняем массив препятствий 0 и 1
                data[x][y] = (byte) (new Color(bufferedImage.getRGB(x,y)).equals(Color.WHITE) ? 0:1);
            }
        }
    }

    //проверяем ячейки на препятсвие/свободное место
    public CellFreedom getCellStatus(int x, int y) {
        //не выходит ли анализируемая ячейка за границы карты
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return CellFreedom.OBSTRUCTION;
        } else {
            //возвращаем флаг, свободна ячейка или занята
            return data[x][y] == 0 ? CellFreedom.EMPTY : CellFreedom.OBSTRUCTION;
        }
    }

    public CellFreedom getCellStatus(Point p){
        return getCellStatus(p.x,p.y);
    }

//    public boolean checkPass(Point p1, Point p2) {
//        if(p1.equals(p2)) {
//            return false;
//        } else if (!CellFreedom.EMPTY.equals(getCellStatus(p1)) || !CellFreedom.EMPTY.equals(getCellStatus(p2))) {
//            return false;
//        } else {
//            List<Point> passCells = calcPassCells(p1,p2);
//            for (Point cell : passCells) {
//                if(!CellFreedom.EMPTY.equals(getCellStatus(cell))) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

    
//    private List<Point> calcPassCells(Point p1, Point p2) {
//        double cA = (p1.getY()-p2.getY()) / (p1.getX()-p2.getX());
//        double cB = p1.getY()-cA*p1.getX();
//        List<Point> pointList = new ArrayList<>();
//        int minX = Math.min(p1.x,p2.x);
//        int maxX = Math.max(p1.x,p2.x);
//        int vectorY = cA >= 0 ? 1:-1;
//        int previousY = (int) lineFunction(minX,cA,cB);
//        for(int x = minX+1; x <= maxX; x++) {
//            int flowY = (int) lineFunction(x,cA,cB);
//            for(int y = previousY; vectorY > 0 ? y <= flowY : y >= flowY; y+=vectorY) {
//                pointList.add(new Point(x-1,y));
//            }
//            previousY = flowY;
//        }
//        return pointList;
//    }
//
//    private double lineFunction(int x, double cA, double cB){
//        return cA*x + cB;
//    }
}

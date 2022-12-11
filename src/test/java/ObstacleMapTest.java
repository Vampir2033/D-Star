import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

class ObstacleMapTest {
    ObstacleMap obstacleMap;
    int width = 20;
    int height = 10;
    @BeforeEach
    void setUp() {
        byte[][] data = new byte[width][height];
        data[6][0] = 1;
        data[6][1] = 1;
        data[6][2] = 1;
        data[6][3] = 1;
        data[6][4] = 1;
        data[6][5] = 1;

        data[10][4] = 1;
        data[11][4] = 1;
        data[12][4] = 1;
        data[13][4] = 1;
        data[14][4] = 1;

        obstacleMap = new ObstacleMap(data, width, height);
    }

    @Test
    void checkFree() {
        Point p1 = new Point(2,7);
        Point p2 = new Point(15,7);
        Assertions.assertTrue(obstacleMap.checkPass(p1,p2));
    }

    @Test
    void checkFreeNear() {
        Point p1 = new Point(13,6);
        Point p2 = new Point(17,2);
        Assertions.assertFalse(obstacleMap.checkPass(p1,p2));
    }

    @Test
    void checkObstacleHorizontal() {
        Point p1 = new Point(2,2);
        Point p2 = new Point(8,5);
        Assertions.assertFalse(obstacleMap.checkPass(p1,p2));
    }

    @Test
    void checkObstacleVertical() {
        Point p1 = new Point(10,0);
        Point p2 = new Point(12,6);
        Assertions.assertFalse(obstacleMap.checkPass(p1,p2));
    }

}
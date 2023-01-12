import org.junit.jupiter.api.Test;

import java.awt.*;

public class TmpTest {
    @Test
    void tmp() {
        Point p1 = new Point(0,0);
        Point p2 = new Point(1,-2);

        System.out.println(p1.distance(p2));
    }
}

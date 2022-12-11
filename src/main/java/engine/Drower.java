package engine;

import javax.swing.*;
import java.awt.*;

public class Drower extends JFrame {

    public Drower(Image locationImage, PolyLine polyLine, double scale, Point startPoint, Point purposePoint) throws HeadlessException {
        super("D-Star");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel = new Panel(locationImage, polyLine, scale, startPoint, purposePoint);
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void paint(Graphics g)
    {
        super.paint(g);
    }
}

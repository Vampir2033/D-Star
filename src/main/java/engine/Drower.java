package engine;

import javax.swing.*;
import java.awt.*;

public class Drower extends JFrame {
    private final Panel panel;

    public Drower(Image locationImage, double scale, Point startPoint, Point purposePoint, PointsContainer pointsContainer) throws HeadlessException {
        super("D-Star");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new Panel(locationImage, scale, startPoint, purposePoint, pointsContainer);
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void repaint() {
        panel.repaint();
    }
}

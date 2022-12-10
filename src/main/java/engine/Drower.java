package engine;

import javax.swing.*;
import java.awt.*;

public class Drower extends JFrame {

    public Drower(Image locationImage, PolyLine polyLine, double scale) throws HeadlessException {
        super("D algorithm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel = new Panel(locationImage, polyLine, scale);
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

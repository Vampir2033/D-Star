package engine;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    private final Image image;
    private final PolyLine polyLine;
    private final double scale;

    public Panel(Image image, PolyLine polyLine, double scale) {
        this.image = image;
        this.polyLine = polyLine;
        this.scale = scale;
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
    }
}

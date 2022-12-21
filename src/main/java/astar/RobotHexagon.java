package astar;

import engine.Robot;
import lombok.Getter;

import java.awt.*;

public class RobotHexagon implements Robot {
    @Getter
    private final int radius;
    private final Image image;
    private Point position;
    private Double direction;

    public RobotHexagon(int radius, Image image, Point position, Double direction) {
        this.radius = radius;
        this.image = image;
        this.position = position;
        this.direction = direction;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public int imageSize() {
        return 1 + 2*radius;
    }

    @Override
    public Point imagePosWithShift() {
        return new Point(position.x-radius, position.y-radius);
    }
}

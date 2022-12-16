package engine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GetImage {
    private static Map<CellStatus, Image> statusImageMap;
    private static Map<Point, Image> directionMap;
    private static Image startImage;
    private static Image purposeImage;

    static {
        statusImageMap = new HashMap<>();
        directionMap = new HashMap<>();
        try {
            statusImageMap.put(CellStatus.ACTIVE, getImageFromRes("images/cells/active.png"));
            statusImageMap.put(CellStatus.OPEN, getImageFromRes("images/cells/open.png"));
            statusImageMap.put(CellStatus.CLOSE, getImageFromRes("images/cells/close.png"));
            statusImageMap.put(CellStatus.WAY, getImageFromRes("images/cells/way.png"));

            directionMap.put(new Point(0,-1), getImageFromRes("images/scratch/N.png"));
            directionMap.put(new Point(1,0), getImageFromRes("images/scratch/E.png"));
            directionMap.put(new Point(0,1), getImageFromRes("images/scratch/S.png"));
            directionMap.put(new Point(-1,0), getImageFromRes("images/scratch/W.png"));
            directionMap.put(new Point(1,-1), getImageFromRes("images/scratch/NE.png"));
            directionMap.put(new Point(1,1), getImageFromRes("images/scratch/SE.png"));
            directionMap.put(new Point(-1,1), getImageFromRes("images/scratch/SW.png"));
            directionMap.put(new Point(-1,-1), getImageFromRes("images/scratch/NW.png"));

            startImage = getImageFromRes("images/cells/start_people.png");
            purposeImage = getImageFromRes("images/cells/purpose.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image getImageByStatus(CellStatus status) {
        return statusImageMap.get(status);
    }

    public static Image getImageByVector(Point vector) {
        Image img = directionMap.getOrDefault(vector, null);
        if(img != null) {
            return img;
        } else {
            throw new RuntimeException("Не найдено изображение для вектора " + vector);
        }
    }

    public static Image getImageFromRes(String imgName) throws IOException {
        URL url = GetImage.class.getClassLoader().getResource(imgName);
        assert url != null;
        return ImageIO.read(url);
    }

    public static Image getStartImage() {
        return startImage;
    }

    public static void setStartImage(Image startImage) {
        GetImage.startImage = startImage;
    }

    public static Image getPurposeImage() {
        return purposeImage;
    }

    public Image getVectorImageByDirection(Point p) {
        return directionMap.get(p);
    }
}

package engine;

import lombok.Getter;
import lombok.ToString;

import java.awt.*;
import java.util.Arrays;

@Getter
@ToString
public class PolyLine {
    private int[] xArr;
    private int[] yArr;

    public PolyLine() {
        xArr = new int[0];
        yArr = new int[0];
    }

    public PolyLine addPoint(int x, int y) {
        xArr = addElementToArr(xArr, x);
        yArr = addElementToArr(yArr, y);
        return this;
    }

    public int size() {
        return xArr.length;
    }

    public Point getFirstPoint() {
        return new Point(xArr[0], yArr[0]);
    }

    public Point getLastPoint() {
        return new Point(xArr[size()-1], yArr[size()-1]);
    }

    private int[] addElementToArr(int[] oldArr, int element) {
        int[] newArr = Arrays.copyOf(oldArr, oldArr.length+1);
        newArr[newArr.length-1] = element;
        return newArr;
    }
}

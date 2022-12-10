package engine;

import lombok.Getter;
import lombok.ToString;

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

    private int[] addElementToArr(int[] oldArr, int element) {
        int[] newArr = Arrays.copyOf(oldArr, oldArr.length+1);
        newArr[newArr.length-1] = element;
        return newArr;
    }
}

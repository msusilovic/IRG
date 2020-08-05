package hr.fer.zemris.lab1;

import java.awt.*;

public class Line {

    public static final int INSIDE = 0;
    public static final int LEFT   = 1;
    public static final int RIGHT  = 2;
    public static final int BOTTOM = 4;
    public static final int TOP    = 8;

    private Point start;
    private Point end;

    private Point fakeStart;
    private Point fakeEnd;

    boolean visible = true;
    private int c1;
    private int c2;


    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getC1() {
        return c1;
    }

    public void setC1(int c1) {
        this.c1 = c1;
    }

    public int getC2() {
        return c2;
    }

    public void setC2(int c2) {
        this.c2 = c2;
    }

    /**
     * Returns true if line is visible in smaller area.
     * @param width original width
     * @param height original height
     * @return
     */
    public boolean calculateVisible(int width, int height) {

        int xMin = width / 4;
        int xMax = 3 * width / 4;
        int yMin = height / 4;
        int yMax = 3 * height / 4;


        this.setC1(computeOutCode(xMin, xMax, yMin, yMax, start));
        this.setC2(computeOutCode(xMin, xMax, yMin, yMax, end));

        if ((c1 & c2) == 0) { // Bitwise AND is 0. Trivially reject
            visible = false;
        }
        return visible;
    }

    public int computeOutCode(int xMin, int xMax, int yMin, int yMax, Point p) {

        int code = INSIDE;

        if (p.getX() < xMin) {
            code |= LEFT;
        } else if (p.getX() > xMax) {
            code |= RIGHT;
        }
        if (p.getY() < yMin) {
            code |= BOTTOM;
        } else if (p.getY() > yMax) {
            code |= TOP;
        }

       // if (p.equals(start)){
       //     setC1(code);
        // }else{
         //   setC2(code);
        //}
        return code;
    }

    public static int getINSIDE() {
        return INSIDE;
    }

    public Point getFakeStart() {
        return fakeStart;
    }

    public void setFakeStart(Point fakeStart) {
        this.fakeStart = fakeStart;
    }

    public Point getFakeEnd() {
        return fakeEnd;
    }

    public void setFakeEnd(Point fakeEnd) {
        this.fakeEnd = fakeEnd;
    }
}

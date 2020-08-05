package hr.fer.zemris.lab1;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LinesModel {

    private List<Line> lines = new ArrayList<>();

    private boolean kontrola;
    private boolean odsijecanje;
    private boolean firstClick;

    private int width;
    private int height;

    private Point firstVertex;
    private Point mousePosition;

    public List<Line> getLines(){
        return this.lines;
    }

    public void addLine(Line l){
        lines.add(l);
    }

    public boolean isKontrola() {
        return kontrola;
    }

    public boolean isOdsijecanje() {
        return odsijecanje;
    }

    public void setKontrola(boolean kontrola) {
        this.kontrola = kontrola;
    }

    public void setOdsijecanje(boolean odsijecanje) {
        this.odsijecanje = odsijecanje;
    }

    public boolean isFirstClick() {
        return firstClick;
    }

    public void setFirstClick(boolean firstClick) {
        this.firstClick = firstClick;
    }

    public Point getFirstVertex() {
        return firstVertex;
    }

    public void setFirstVertex(Point firstVertex) {
        this.firstVertex = firstVertex;
    }

    public Point getMousePosition() {
        return mousePosition;
    }

    public void setMousePosition(Point mousePosition) {
        this.mousePosition = mousePosition;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

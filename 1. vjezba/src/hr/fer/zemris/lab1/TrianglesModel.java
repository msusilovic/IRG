package hr.fer.zemris.lab1;

import hr.fer.zemris.linearna.IVector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TrianglesModel {

    private List<Triangle>  triangles= new LinkedList<>();

    private List<Color> allColors = new ArrayList<>();

    private Color currentColor;

    private  boolean firstClick = false;
    private  boolean secondClick = false;

    private int width;
    private int height;

    private IVector firstVertex;
    private IVector secondVertex;
    private IVector currentMousePosition;

    public TrianglesModel() {
       Color[] array =  {Color.red, Color.green, Color.blue, Color.cyan, Color.yellow, Color.magenta};

        allColors = Arrays.asList(array);
        this.currentColor = allColors.get(0);
    }

    public List<Triangle> addTriangle(Triangle t) {
        triangles.add(t);

        return triangles;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public Color setNextColor() {
        currentColor = allColors.get((allColors.indexOf(currentColor)+1)%6);

        return currentColor;
    }

    public Color setPreviousColor() {
        currentColor = allColors.get((allColors.indexOf(currentColor)+5)%6);

        return currentColor;
    }

    public boolean getFirstClick() {
        return firstClick;
    }

    public void setFirstClick(boolean firstClick) {
        this.firstClick = firstClick;
    }

    public boolean getSecondClick() {
        return secondClick;
    }

    public void setSecondClick(boolean secondClick) {
        this.secondClick = secondClick;
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

    public IVector getFirstVertex() {
        return firstVertex;
    }

    public void setFirstVertex(IVector firstVertex) {
        this.firstVertex = firstVertex;
    }

    public IVector getSecondVertex() {
        return secondVertex;
    }

    public void setSecondVertex(IVector secondVertex) {
        this.secondVertex = secondVertex;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public IVector getCurrentMousePosition() {
        return currentMousePosition;
    }

    public void setCurrentMousePosition(IVector currentMousePosition) {
        this.currentMousePosition = currentMousePosition;
    }
}

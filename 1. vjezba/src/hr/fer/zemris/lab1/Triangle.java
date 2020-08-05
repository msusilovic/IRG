package hr.fer.zemris.lab1;

import hr.fer.zemris.linearna.IVector;

import java.awt.*;

public class Triangle {

    private IVector first;
    private IVector second;
    private IVector third;

    private Color color;

    Triangle (IVector first, IVector second, IVector third, Color color) {
        this.first = first;
        this.second = second;
        this.third = third;

        this.color = color;
    }

    public IVector getFirst() {
        return first;
    }

    public void setFirst(IVector first) {
        this.first = first;
    }

    public IVector getSecond() {
        return second;
    }

    public void setSecond(IVector second) {
        this.second = second;
    }

    public IVector getThird() {
        return third;
    }

    public void setThird(IVector third) {
        this.third = third;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

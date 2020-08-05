package sesti;

import com.jogamp.opengl.awt.GLCanvas;
import linearna.IVector;
import linearna.Vector;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EyePosition {

    double angle;
    double increment;
    double r;
    double initialAngle;

    double x;
    double y;
    double z;
    GLCanvas canvas;

    public EyePosition(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = Math.toDegrees(Math.atan(z / x));
        this.initialAngle = angle;
        this.increment = 1;
        this.r = Math.sqrt(x * x + z * z);

    }

    KeyAdapter ka = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_R){
                angle -= increment;
            }else if(e.getKeyCode() == KeyEvent.VK_L){
                angle += increment;
            }else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                angle = initialAngle;
            }

            canvas.display();
        }
    };

    public double getAngle() {
        return angle;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public GLCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(GLCanvas canvas) {
        this.canvas = canvas;
    }

    public KeyAdapter getKeyAdapter() {
        return this.ka;
    }

    public IVector getEyeVector() {
        double radians = Math.toRadians(angle);
        double xValue = r * Math.cos(radians);
        double zValue = r * Math.sin(radians);
        return new Vector(new double[] {xValue, y, zValue});

    }
}

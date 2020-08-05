package osmi;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import linearna.IMatrix;
import linearna.Matrix;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public class Bezier {

    static List<Point> points = new LinkedList<>();
    static Point movingPoint;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->{

            GLProfile glProfile = GLProfile.getDefault();
            GLCapabilities glCapabilities = new GLCapabilities(glProfile);
            final GLCanvas glCanvas = new GLCanvas(glCapabilities);

            glCanvas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        points.add(new Point(e.getPoint().x, e.getPoint().y));
                    }
                    glCanvas.display();
                }
            });

            glCanvas.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(SwingUtilities.isRightMouseButton(e)) {
                        int x = e.getX();
                        int y = e.getY();
                        for (Point p : points) {
                            if (Math.abs(p.x - x) <= 5 && Math.abs(p.y - y)  <= 5) {
                                movingPoint = p;
                                break;
                            }
                        }
                        glCanvas.display();
                    }
                }
            });

            //pomicanje tocke
            glCanvas.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                   if(SwingUtilities.isRightMouseButton(e)){
                        movingPoint.setLocation(e.getX(), e.getY());
                        glCanvas.display();
                    }

                }
            });

            glCanvas.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        points = new LinkedList<>();
                        glCanvas.display();
                    }
                }
            });

            glCanvas.addGLEventListener(new GLEventListener() {

                @Override
                public void init(GLAutoDrawable glAutoDrawable) {
                }

                @Override
                public void dispose(GLAutoDrawable glAutoDrawable) {
                }

                @Override
                public void display(GLAutoDrawable glAutoDrawable) {
                    GL2 gl2 = glAutoDrawable.getGL().getGL2();

                    gl2.glClearColor(1, 1, 1, 0);
                    gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
                    gl2.glLoadIdentity();

                    drawPolygon(gl2);

                    gl2.glColor3f(0, 0, 1);
                    bezierApproximation(gl2, points);

                    if(points.size() > 1) {
                        IMatrix p = pointsToMatrix();
                        IMatrix t = getTMatrix();
                        IMatrix b = getBMatrix();

                        IMatrix tInv = t.nInvert();
                        IMatrix r = b.nInvert().nMultiply(tInv).nMultiply(p);
                        List<Point> newPoints = new LinkedList<>();
                        for (int i = 0; i < r.getRowsCount(); i++) {
                            Point point = new Point((int)r.get(i, 0), (int)r.get(i, 1));
                            newPoints.add(point);
                        }

                        gl2.glColor3f(0, 0, 0);
                        bezierApproximation(gl2, newPoints);
                    }

                }

                private IMatrix getBMatrix() {
                    int n = points.size();
                    Matrix b = new Matrix(n, n);

                    for (int i = 0; i < n; i++) {

                        double br = factorial(n - i - 1);
                        double c = factorial(n - 1) / (factorial(i) * factorial(n - i - 1));
                        for (int j = 0; j < n - i; j ++) {
                            double naz = factorial(j) * factorial(n - i - j - 1);
                            double razlomak = br/naz;
                            int sign;
                            if((i + j + n) % 2 == 1) {
                                sign = 1;
                            }else{
                                sign = -1;
                            }
                            b.set(i, j, sign * razlomak * c);
                        }
                    }
                    return b;
                }

                private int factorial(int number) {
                    int result = 1;
                    for (int i = 1; i <= number; i++) {
                        result *= i;
                    }
                    return result;
                }

                private IMatrix getTMatrix() {
                    int n = points.size();
                    Matrix t = new Matrix(n, n);
                    //korak
                    double step = 1 / (double)(n - 1);

                    for (int i = 0; i < n; i++) {
                        double value = step * i;
                        for (int j = 0; j < n; j++) {
                            if (j == n - 1 ||i == n - 1){
                                t.set(i, j, 1);
                            }
                            t.set(i, j, Math.pow(value, n - 1 - j));
                        }
                    }
                    return t;
                }

                private IMatrix pointsToMatrix() {

                    Matrix m = new Matrix(points.size(), 2);
                    for (int i = 0; i < points.size(); i++) {
                        Point2D p = points.get(i);
                        m.set(i, 0, p.getX());
                        m.set(i, 1, p.getY());
                    }
                    return m;
                }

                private void bezierApproximation(GL2 gl2, List<Point> ps) {
                    int divs = 50;
                    int n = ps.size() - 1;
                    int[] factors =  computeFactors(n);
                    double b;

                    gl2.glBegin(GL2.GL_LINE_STRIP);

                    double px = 0;
                    double py = 0;
                    for (int i = 0; i <= divs; i++) {
                        double t = 1.0 / divs * i;
                        px = 0;
                        py = 0;
                        for (int j = 0; j <= n; j++) {
                            if (j == 0) {
                                b = factors[j] * Math.pow(1 - t, n);
                            }else if(j == n) {
                                b = factors[j] * Math.pow(t, n);
                            }else {
                                b = factors[j] * Math.pow(t, j) * Math.pow(1 - t, n - j);
                            }

                            px += b * ps.get(j).getX();
                            py += b * ps.get(j).getY();
                        }
                        gl2.glVertex2f((float)px, (float)py);
                    }
                    gl2.glEnd();
                }

                private int[] computeFactors(int n) {
                    int a = 1;
                    int[] factors = new int[n+1];

                    for (int i = 1; i <= n + 1; i++){
                        factors[i-1] = a;
                        a = a * (n - i + 1) / i;
                    }
                    return factors;
                }

                private void drawPolygon(GL2 gl2) {

                    gl2.glColor3f(1, 0, 0);

                    gl2.glBegin(GL2.GL_LINE_STRIP);
                    for (Point p : points) {
                        gl2.glVertex2d(p.getX(), p.getY());
                    }
                    gl2.glEnd();
                }


                @Override
                public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                    GL2 gl2 = glAutoDrawable.getGL().getGL2();
                    gl2.glMatrixMode(GL2.GL_PROJECTION);
                    gl2.glLoadIdentity();

                    GLU glu = new GLU();
                    glu.gluOrtho2D(0.0f, width, height, 0.0f);

                    gl2.glMatrixMode(GL2.GL_MODELVIEW);
                    gl2.glLoadIdentity();
                    gl2.glViewport(0, 0, width, height);
                    gl2.glViewport(0, 0, width, height);
                }
            });

            final JFrame jFrame = new JFrame(
                    "Osmi zadatak");
            jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            jFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    jFrame.dispose();
                    System.exit(0);
                }
            });
            jFrame.getContentPane().add(glCanvas, BorderLayout.CENTER);
            jFrame.setSize(640, 480);
            jFrame.setVisible(true);
            glCanvas.requestFocusInWindow();
        });
    }
}

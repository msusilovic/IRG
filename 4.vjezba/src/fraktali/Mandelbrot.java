package fraktali;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;
import java.util.Stack;

public class Mandelbrot {

    private static boolean square = true;
    private static boolean color = false;
    private static Stack<Complex> stack = new Stack<>();

    private static double umin = -2;
    private static double umax = 1;
    private static double vmin = -1.2;
    private static double vmax = 1.2;
    private static int maxLimit = 128;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            GLProfile glProfile = GLProfile.getDefault();
            GLCapabilities glCapabilities = new GLCapabilities(glProfile);
            final GLCanvas glCanvas = new GLCanvas(glCapabilities);

            glCanvas.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_1) {
                        square = true;
                    }else if(e.getKeyCode() == KeyEvent.VK_2) {
                        square = false;
                    }else if(e.getKeyCode() == KeyEvent.VK_B) {
                        color = false;
                    }else if(e.getKeyCode() == KeyEvent.VK_C) {
                        color = true;
                    }else if(e.getKeyCode() == KeyEvent.VK_X) {
                        if (!stack.isEmpty()) {
                            Complex max = stack.pop();
                            Complex min = stack.pop();
                            umin = min.getRe();
                            vmin = min.getIm();
                            umax = max.getRe();
                            vmax = max.getIm();
                        }
                    }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        Complex min = stack.get(0);
                        Complex max = stack.get(1);

                        umin = min.getRe();
                        vmin = min.getIm();
                        umax = max.getRe();
                        vmax = max.getIm();
                        stack = new Stack<>();
                    }
                    glCanvas.display();
                }
            });

            glCanvas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();

                    stack.push(new Complex(umin, vmin));
                    stack.push(new Complex(umax, vmax));

                    double width = (umax - umin) / 16;
                    double height = (vmax - vmin) / 16;

                    Complex point = getComplexForPosition(x, y, glCanvas.getSurfaceWidth(), glCanvas.getSurfaceHeight());

                    umin = point.getRe() - (width / 2);
                    umax = point.getRe() + (width / 2);
                    vmin = point.getIm() - (height / 2);
                    vmax = point.getIm() + (height / 2);

                    glCanvas.display();
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

                    gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
                    gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

                    gl2.glBegin(GL.GL_POINTS);
                    for (int x = 0; x < glCanvas.getSurfaceWidth(); x++){
                        for (int y = 0; y < glCanvas.getSurfaceHeight(); y++) {

                            Complex c = getComplexForPosition(x, y, glCanvas.getSurfaceWidth(), glCanvas.getSurfaceHeight());
                            int n = square == true ? divergenceTest(c, maxLimit) : divergenceTest2(c, maxLimit);
                            if (color == true) {
                                colorScheme2(n, gl2);
                            } else {
                                colorScheme1(n, gl2);
                            }
                            gl2.glVertex2d(x, y);
                        }
                    }
                    gl2.glEnd();
                    
                }

                private void colorScheme1(int n, GL2 gl2) {
                    if (n == -1) {
                        gl2.glColor3f(0.0f, 0.0f, 0.0f);
                    }else{
                        gl2.glColor3f(1.0f, 1.0f, 1.0f);
                    }
                }

                private void colorScheme2(int n, GL2 gl2) {
                    if (n == -1) {
                        gl2.glColor3f(0.0f, 0.0f, 0.0f);
                    } else if(maxLimit < 16) {
                        int r = (int)((n - 1) % (double)(maxLimit - 1) * 255 + 0.5);
                        int g = 255 - r;
                        int b = ((n - 1) % (maxLimit / 2) * 255 / (maxLimit / 2));
                        gl2.glColor3f(r/255f, g/255f, b/255f);
                    }else{
                        int lim = maxLimit < 32 ? maxLimit : 32;
                        int r = (n - 1) * 255 / lim;
                        int g = ((n - 1) % (lim / 4)) * 255 / (lim / 4);
                        int b = ((n - 1) % (lim / 8)) * 255 / (lim / 8);
                        gl2.glColor3f(r/255f, g/255f, b/255f);
                    }
                }

                public int divergenceTest(Complex c, int limit) {
                    Complex z = new Complex(0, 0);

                    for (int i = 0; i <= limit; i++) {
                        Complex square = z.square();
                        double next_re = square.getRe() + c.getRe();
                        double next_im = square.getIm() + c.getIm();
                        z.setRe(next_re);
                        z.setIm(next_im);
                        double module2 = z.modulusSquare();
                        if (module2 > 4) return i;
                    }
                    return -1;
                }

                public int divergenceTest2(Complex c, int limit) {
                    Complex z = new Complex(0, 0);

                    for (int i = 0; i < limit; i ++) {
                        Complex cube = z.cube();
                        double next_re = cube.getRe() + c.getRe();
                        double next_im = cube.getIm() + c.getIm();
                        z.setRe(next_re);
                        z.setIm(next_im);
                        double module2 = z.modulusSquare();
                        if (module2 > 4) return  i;
                    }
                    return -1;
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
                }
            });

            final JFrame jFrame = new JFrame(
                    "Mandelbrot");
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


    public static Complex getComplexForPosition(int x, int y, int xmax, int ymax) {

        double re = x / (double)xmax * (umax - umin) + umin;
        double im =  y / (double)ymax * (vmax - vmin) + vmin;

        return new Complex(re, im);
    }
}

package hr.fer.zemris.lab1;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Bresenham {


    public static final int INSIDE = 0; //0000
    public static final int LEFT   = 1; //0001
    public static final int RIGHT  = 2; //0010
    public static final int BOTTOM = 4; //0100
    public static final int TOP    = 8; //1000

    private static LinesModel model = new LinesModel();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            GLProfile glProfile = GLProfile.getDefault();
            GLCapabilities glCapabilities = new GLCapabilities(glProfile);
            final GLCanvas glCanvas = new GLCanvas(glCapabilities);

            glCanvas.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(!model.isFirstClick()){
                        model.setFirstClick(true);
                        model.setFirstVertex(e.getPoint());
                    }else{
                        model.setFirstClick(false);
                        Line line = new Line(model.getFirstVertex(), e.getPoint());
                        model.addLine(line);

                        line.calculateVisible(model.getWidth(), model.getHeight());

                    }
                    glCanvas.display();
                }
            });

            glCanvas.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_O) {
                        e.consume();
                        model.setOdsijecanje(!model.isOdsijecanje());

                    }else if (e.getKeyCode() == KeyEvent.VK_K) {
                        e.consume();
                        model.setKontrola(!model.isKontrola());
                    }

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
                    int width = glAutoDrawable.getSurfaceWidth();
                    int height = glAutoDrawable.getSurfaceHeight();

                    model.setWidth(width);
                    model.setHeight(height);

                    int xmin = width / 4;
                    int xmax = 3 * width / 4;
                    int ymin = height / 4;
                    int ymax = 3 * height / 4;

                    gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
                    gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

                    if (model.isOdsijecanje()) {
                        gl2.glColor3d(0, 1, 0);


                        gl2.glBegin(GL.GL_LINE_LOOP);
                        gl2.glVertex2i(xmin, ymin);
                        gl2.glVertex2i(xmin, ymax);
                        gl2.glVertex2i(xmax, ymax);
                        gl2.glVertex2i(xmax, ymin);
                        gl2.glEnd();

                    }
                    for (Line l : model.getLines()) {
                        int xs = (int)l.getStart().getX();
                        int ys = (int)l.getStart().getY();
                        int xe = (int)l.getEnd().getX();
                        int ye = (int)l.getEnd().getY();

                        //paralelna crvena linija
                        if (model.isKontrola()) {
                            gl2.glColor3f(1, 0, 0);

                            double L = Math.sqrt((xs - xe) * (xs - xe) + (ys - ye) * (ys - ye));
                            int x1 = (int) (xs - 4 * (ye - ys)/L);
                            int x2 = (int) (xe - 4 * (ye - ys)/L);
                            int y1 = (int) (ys - 4 * (xs - xe)/L);
                            int y2 = (int) (ye - 4 * (xs - xe)/L);


                            gl2.glBegin(GL.GL_LINES);
                            gl2.glVertex2i(x1, y1);
                            gl2.glVertex2f(x2, y2);
                            gl2.glEnd();
                        }
                        if (model.isOdsijecanje()) {

                            int code1 = l.computeOutCode(xmin, xmax, ymin, ymax, l.getStart());
                            int code2 = l.computeOutCode(xmin, xmax, ymin, ymax, l.getEnd());
                            boolean accept = false;
                                while(true) {
                                    if ((code1 == 0) && (code2 == 0)) {
                                        accept = true;
                                        break;
                                    }else if ((code1 & code2) != 0){
                                        break;
                                    }else {
                                        int codeOut;
                                        int x, y;

                                        if (code1 != 0)
                                            codeOut = code1;
                                        else
                                            codeOut = code2;

                                        if ((codeOut & TOP) != 0) {
                                            x = xs + (xe - xs) * (ymax - ys) / (ye - ys);
                                            y = ymax;
                                        } else if ((codeOut & BOTTOM) != 0) {
                                            x = xs + (xe - xs) * (ymin - ys) / (ye - ys);
                                            y = ymin;
                                        } else if ((codeOut & RIGHT) != 0) {
                                            y = ys + (ye - ys) * (xmax - xs) / (xe - xs);
                                            x = xmax;
                                        } else {
                                            y = ys + (ye - ys) * (xmin - xs) / (xe - xs);
                                            x = xmin;
                                        }
                                        if (codeOut == l.getC1()) {
                                            xs = x;
                                            ys = y;
                                            code1 = l.computeOutCode(xmin, xmax, ymin, ymax, new Point(xs, ys));
                                        }else{
                                            xe = x;
                                            ye = y;
                                           code2 = l.computeOutCode(xmin, xmax, ymin, ymax, new Point(xe, ye));
                                        }
                                    }

                                }
                                if (accept) {
                                    drawLine(gl2, xs, ys, xe, ye);
                                }
                        }else{
                            drawLine(gl2, xs, ys, xe, ye);
                        }


                    }
                }

                public void drawLine(GL2 gl2, int xs, int ys, int xe, int ye) {
                    gl2.glPointSize(1.0f);
                    gl2.glColor3d(0, 0, 0);

                    if (xs <= xe) {
                        // 1. i 4. kvadrant
                        if (ys <= ye) {
                            // 1. kvadrant
                            drawLineTwo(gl2, xs, ys, xe, ye);
                        } else {
                            // 4. kvadrant
                            drawLineThree(gl2, xs, ys, xe, ye);
                        }
                    } else {

                        if (ys >= ye) {
                            // 2. kvadrant
                            drawLineTwo(gl2, xe, ye, xs, ys);
                        } else {
                            // 3. kvadrant
                            drawLineThree(gl2, xe, ye, xs, ys);
                        }
                    }

                }

                /**
                 * Crta linije pod kutovima od 0 do 90 uz xs < xe.
                 * @param gl2
                 * @param xs
                 * @param ys
                 * @param xe
                 * @param ye
                 */
                public void drawLineTwo(GL2 gl2, int xs, int ys, int xe, int ye) {
                    int x, yc, korekcija;
                    int a, yf;

                    if (ye - ys <= xe - xs) {
                        a = 2 * (ye - ys);
                        yc = ys;
                        yf = - (xe - xs);
                        korekcija = -2 * (xe - xs);

                        gl2.glBegin(GL.GL_POINTS);
                        for (x = xs; x <=  xe; x++) {
                            gl2.glVertex2i(x, yc);
                            yf  = yf + a;
                            if (yf >= 0) {
                                yf = yf + korekcija;
                                yc = yc + 1;
                            }
                        }

                    }else {
                        //zamijeni x i y koordinate
                        x = xe;
                        xe = ye;
                        ye = x;
                        x = xs;
                        xs = ys;
                        ys = x;

                        a = 2 * (ye - ys);
                        yc = ys;
                        yf = - (xe - xs);
                        korekcija = -2 * (xe - xs);

                        gl2.glBegin(GL.GL_POINTS);
                        for (x = xs; x <= xe; x++) {
                            gl2.glVertex2i(yc, x);
                            yf = yf + a;
                            if (yf >= 0) {
                                yf = yf + korekcija;
                                yc = yc + 1;
                            }
                        }
                    }
                    gl2.glEnd();
                }

                /**
                 *crta linije pod kutovima od 0◦ do -90◦, uz xs < xe
                 * @param gl2
                 * @param xs
                 * @param ys
                 * @param xe
                 * @param ye
                 */
                public void drawLineThree(GL2 gl2, int xs, int ys, int xe, int ye) {
                    int x, yc, korekcija;
                    int a, yf;

                    if (- (ye - ys) <= xe - xs) {
                        a = 2 * (ye - ys);
                        yc = ys;
                        yf = xe - xs;
                        korekcija = 2 * (xe - xs);

                        gl2.glBegin(GL.GL_POINTS);
                        for (x = xs; x <= xe; x++) {
                            gl2.glVertex2i(x, yc);
                            yf = yf + a;
                            if (yf <= 0) {
                                yf = yf + korekcija;
                                yc = yc - 1;
                            }
                        }
                    }else {
                        //zamijeni x i y koordinate
                        x = xe;
                        xe = ys;
                        ys = x;
                        x = xs;
                        xs = ye;
                        ye = x;
                        a = 2 * (ye - ys);
                        yc = ys;
                        yf = xe - xs;
                        korekcija = 2 * (xe - xs);

                        gl2.glBegin(GL.GL_POINTS);
                        for(x = xs; x <= xe; x++) {
                            gl2.glVertex2i(yc, x);
                            yf = yf + a;
                            if (yf <= 0) {
                                yf = yf + korekcija;
                                yc = yc - 1;
                            }
                        }
                    }
                    gl2.glEnd();
                }


                @Override
                public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                    GL2 gl2 = glAutoDrawable.getGL().getGL2();
                    gl2.glMatrixMode(GL2.GL_PROJECTION);
                    gl2.glLoadIdentity();

                    model.setWidth(glAutoDrawable.getSurfaceWidth());
                    model.setHeight(glAutoDrawable.getSurfaceHeight());

                    for(Line l : model.getLines()) {
                        l.calculateVisible(glAutoDrawable.getSurfaceWidth(), glAutoDrawable.getSurfaceHeight());

                    }
                    GLU glu = new GLU();
                    glu.gluOrtho2D(0.0f, width, height, 0.0f);

                    gl2.glMatrixMode(GL2.GL_MODELVIEW);
                    gl2.glLoadIdentity();
                    gl2.glViewport(0, 0, width, height);
                }
            });

            final JFrame jFrame = new JFrame(
                    "Treća laboratorijska vježba");
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

package hr.fer.zemris.lab1;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import hr.fer.zemris.linearna.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Draw {

   private static TrianglesModel model = new TrianglesModel();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            GLProfile glProfile = GLProfile.getDefault();
            GLCapabilities glCapabilities = new GLCapabilities(glProfile);
            final GLCanvas glCanvas = new GLCanvas(glCapabilities);

            glCanvas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    //nije kliknut ni jedan do sad
                    if (!model.getFirstClick() && !model.getSecondClick()){
                        model.setFirstClick(true);
                        double[] positions = {e.getX(), e.getY()};
                        model.setFirstVertex(new Vector(positions));
                        glCanvas.display();

                        //prvi je vec kliknut
                    }else if (model.getFirstClick()) {
                        model.setFirstClick(false);
                        model.setSecondClick(true);
                        double[] positions = {e.getX(), e.getY()};
                        model.setSecondVertex(new Vector(positions));
                        glCanvas.display();

                        //treci klik
                    }else{
                        model.setSecondClick(false);
                        double[] positions = {e.getX(), e.getY()};
                        Vector third = new Vector(positions);
                        model.addTriangle(new Triangle(model.getFirstVertex(), model.getSecondVertex(), third, model.getCurrentColor()));
                        model.setFirstVertex(null);
                        model.setSecondVertex(null);
                        glCanvas.display();
                    }
                }
            });

            glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    e.consume();
                   // System.out.println("Mis je pomaknut na: x=" + e.getX() + ", y=" + e.getY());
                    double[] positions = {e.getX(), e.getY()};
                    model.setCurrentMousePosition(new Vector(positions));
                    glCanvas.display();
                }
            });
            glCanvas.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_N) {
                        e.consume();
                        model.setNextColor();
                        glCanvas.display();
                    }else if(e.getKeyCode() == KeyEvent.VK_P) {
                        e.consume();
                        model.setPreviousColor();
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
                    model.setWidth(glAutoDrawable.getSurfaceWidth());
                    model.setHeight(glAutoDrawable.getSurfaceHeight());

                    gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
                    gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

                    //crtanje kvadratica
                    gl2.glBegin(GL2.GL_QUADS);
                    gl2.glColor3f(model.getCurrentColor().getRed(), model.getCurrentColor().getGreen(), model.getCurrentColor().getBlue());
                    gl2.glVertex2f(model.getWidth()-20, model.getHeight()-5);
                    gl2.glVertex2f(model.getWidth()-20, model.getHeight()-15);
                    gl2.glVertex2f(model.getWidth()-10, model.getHeight()-15);
                    gl2.glVertex2f(model.getWidth()-10, model.getHeight()-5);

                    gl2.glEnd();


                    //crtanje trokuta
                    gl2.glBegin(GL.GL_TRIANGLES);
                    for (Triangle t : model.getTriangles()) {
                        Color color = t.getColor();

                        gl2.glColor3f(color.getRed(),color.getGreen(), color.getBlue());
                        gl2.glVertex2f((float)t.getFirst().get(0), model.getHeight()-(float)t.getFirst().get(1));
                        gl2.glVertex2f((float)t.getSecond().get(0), model.getHeight()-(float)t.getSecond().get(1));
                        gl2.glVertex2f((float)t.getThird().get(0), model.getHeight()-(float)t.getThird().get(1));
                    }
                    gl2.glEnd();

                    //crtanje linija
                    if (model.getFirstClick() && model.getCurrentMousePosition() != null) {
                        Color color = model.getCurrentColor();
                        gl2.glBegin(GL.GL_LINES);
                        gl2.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
                        gl2.glVertex2f((float)model.getFirstVertex().get(0), model.getHeight()- (float)model.getFirstVertex().get(1));
                        gl2.glVertex2f((float)model.getCurrentMousePosition().get(0), model.getHeight()- (float)model.getCurrentMousePosition().get(1));
                        gl2.glEnd();
                    }else if(model.getSecondClick()) {
                        Color color = model.getCurrentColor();
                        gl2.glBegin(GL.GL_LINE_LOOP);
                        gl2.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
                        gl2.glVertex2f((float)model.getFirstVertex().get(0), model.getHeight()- (float)model.getFirstVertex().get(1));
                        gl2.glVertex2f((float)model.getSecondVertex().get(0), model.getHeight()- (float)model.getSecondVertex().get(1));
                        gl2.glVertex2f((float)model.getCurrentMousePosition().get(0), model.getHeight()- (float)model.getCurrentMousePosition().get(1));
                        gl2.glEnd();
                    }


                }

                @Override
                public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                    GL2 gl2 = glAutoDrawable.getGL().getGL2();
                    gl2.glMatrixMode(GL2.GL_PROJECTION);
                    gl2.glLoadIdentity();

                    GLU glu = new GLU();
                    glu.gluOrtho2D(0.0f, width, 0.0f, height);

                    gl2.glMatrixMode(GL2.GL_MODELVIEW);
                    gl2.glLoadIdentity();
                    gl2.glViewport(0, 0, width, height);
                }
            });

            final JFrame jFrame = new JFrame(
                    "Druga laboratorijska vježba");
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

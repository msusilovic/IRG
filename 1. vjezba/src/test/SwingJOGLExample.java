package test;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingJOGLExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GLProfile glProfile = GLProfile.getDefault();
            GLCapabilities glCapabilities = new GLCapabilities(glProfile);
            final GLCanvas glCanvas = new GLCanvas(glCapabilities);

            glCanvas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Mis je kliknut na: x=" + e.getX() + ", y=" + e.getY());
                    glCanvas.display();
                }
            });

            glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    System.out.println("Mis je pomaknut na: x=" + e.getX() + ", y=" + e.getY());
                    glCanvas.display();
                }
            });

            glCanvas.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        e.consume();
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
                    int width = glAutoDrawable.getSurfaceWidth();
                    int height = glAutoDrawable.getSurfaceHeight();

                    gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
                    gl2.glLoadIdentity();
                    gl2.glBegin(GL.GL_TRIANGLES);
                    gl2.glColor3f(1, 0, 0);
                    gl2.glVertex2f(0, 0);
                    gl2.glColor3f(0, 1, 0);
                    gl2.glVertex2f(width, 0);
                    gl2.glColor3f(0, 0, 1);
                    gl2.glVertex2f(width / 2f, height);
                    gl2.glEnd();
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
                    "Primjer prikaza obojanog trokuta");
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
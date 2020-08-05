package sesti;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import jogamp.opengl.glu.Glue;
import linearna.IVector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class Zadatak2 {

    public static void main(String[] args) {

        String name = args[0];
        ObjectModel model = new ObjectModel();
        EyePosition eye = new EyePosition(4, 3, 1);

        try (BufferedReader br = new BufferedReader(new FileReader(name))) {

            List<Vertex3D> vertexes = new ArrayList<>();
            List<Face3D> faces = new ArrayList<>();
            model.setFaces(faces);
            model.setVertexes(vertexes);
            String line = br.readLine();
            while(line != null) {
                if(line.startsWith("v")) {
                    String[] parts = line.trim().split("\\s+");
                    Vertex3D v = new Vertex3D(Double.parseDouble(parts[1]),
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]));
                    vertexes.add(v);

                }else if (line.startsWith("f")) {
                    String[] parts = line.trim().split("\\s+");
                    Face3D f = new Face3D(Integer.parseInt(parts[1])-1,
                            Integer.parseInt(parts[2])-1,
                            Integer.parseInt(parts[3])-1);
                    model.addFace3D(f);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        SwingUtilities.invokeLater(new Runnable () {
            @Override
            public void run() {
                GLProfile glProfile = GLProfile.getDefault();
                GLCapabilities glCapabilities = new GLCapabilities(glProfile);
                final GLCanvas glCanvas = new GLCanvas(glCapabilities);

                model.normalize();
                eye.setCanvas(glCanvas);
                glCanvas.addKeyListener(eye.getKeyAdapter());

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

                        GLU glu = new GLU();
                        IVector v = eye.getEyeVector();
                        glu.gluLookAt(v.get(0), v.get(1), v.get(2), 0f, 0f, 0f, 0f, 1f, 0f);

                        gl2.glColor3f(0, 0, 0);

                        for (Face3D face : model.getFaces()) {
                            Vertex3D[] vertices = face.getVertices();
                            gl2.glBegin(GL.GL_LINE_LOOP);
                            gl2.glVertex3f((float)vertices[0].getX(), (float)vertices[0].getY(), (float)vertices[0].getZ());
                            gl2.glVertex3f((float)vertices[1].getX(), (float)vertices[1].getY(), (float)vertices[1].getZ());
                            gl2.glVertex3f((float)vertices[2].getX(), (float)vertices[2].getY(), (float)vertices[2].getZ());
                            gl2.glEnd();
                        }

                    }

                    @Override
                    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                        GL2 gl2 = glAutoDrawable.getGL().getGL2();
                        gl2.glMatrixMode(GL2.GL_PROJECTION);
                        gl2.glLoadIdentity();

                        GLU glu = new GLU();
                        double angle = 2 * Math.atan(0.5) * 180 / Math.PI;
                        glu.gluPerspective(angle ,1, 1, 100);

                        gl2.glMatrixMode(GL2.GL_MODELVIEW);
                        gl2.glViewport(0,0, width, height);
                    }
                });

                final JFrame jFrame = new JFrame("Test");
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
            }
        });
    }



}

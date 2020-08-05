package sedmi;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import linearna.IMatrix;
import linearna.IRG;
import linearna.IVector;
import linearna.Vector;
import sesti.EyePosition;
import sesti.Face3D;
import sesti.ObjectModel;
import sesti.Vertex3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Zadatak2 {

    static int odbacivanje = 1;

    public static void main(String[] args) {

        double angle = 18.4349488;
        double increment = 1;
        double r = 3.16227766;

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
            model.normalize();
        } catch (IOException e) {
            e.printStackTrace();
        }


        SwingUtilities.invokeLater(new Runnable () {
            @Override
            public void run() {
                GLProfile glProfile = GLProfile.getDefault();
                GLCapabilities glCapabilities = new GLCapabilities(glProfile);
                final GLCanvas glCanvas = new GLCanvas(glCapabilities);
                eye.setCanvas(glCanvas);

                glCanvas.addKeyListener(eye.getKeyAdapter());

                glCanvas.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                       if (e.getKeyCode() == KeyEvent.VK_1) {
                           System.out.println("Bez odbacivanja");
                           odbacivanje = 1;
                       }else if (e.getKeyCode() == KeyEvent.VK_2) {
                           odbacivanje = 2;
                           System.out.println("Odbacivanja algoritmom 1");
                       }else if (e.getKeyCode() == KeyEvent.VK_3) {
                           odbacivanje = 3;
                           System.out.println("Odbacivanja algoritmom 2");
                       }else if (e.getKeyCode() == KeyEvent.VK_4){
                           odbacivanje = 4;
                           System.out.println("Odbacivanja algoritmom 3");
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

                        gl2.glClearColor(1, 1, 1, 0);
                        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

                        //postavlja jedinicnu matricu
                        gl2.glLoadIdentity();
                        gl2.glColor3f(0, 0, 0);

                        //IVector eye = new Vector(new double[]{3, 4, 1});
                        IVector center = new Vector(new double[]{0, 0, 0});
                        IVector up = new Vector(new double[]{0, 1, 0});
                        IMatrix tp = IRG.lookAtMatrix(eye.getEyeVector(), center, up);
                        IMatrix pr = IRG.buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5, 1, 100);
                        IMatrix m = tp.nMultiply(pr);

                        if (odbacivanje == 2) {
                            model.determineFaceVisibilities1(eye.getEyeVector());
                        }else if (odbacivanje == 3) {
                            model.determineFaceVisibilities2(eye.getEyeVector());
                        }else if (odbacivanje == 4) {
                            drawAlgorithm3(gl2, m);
                        }
                        //IMatrix m = tp;
                        for (Face3D face : model.getFaces()) {
                            if (odbacivanje == 1 || (odbacivanje == 2 && face.getVisible()) || (odbacivanje == 3 && face.getVisible())){
                                gl2.glBegin(GL.GL_LINE_LOOP);
                                Vertex3D[] vertices = face.getVertices();
                                for (int i = 0; i < 3; i++) {
                                    Vertex3D vert = vertices[i];
                                    IVector v = new Vector(new double[]{vert.getX(), vert.getY(), vert.getZ(), 1.0});
                                    IVector tv = v.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogenous();
                                    gl2.glVertex2d(tv.get(0), tv.get(1));
                                }
                                gl2.glEnd();
                            }
                        }
                    }

                    private void drawAlgorithm3(GL2 gl2, IMatrix m) {

                        for (Face3D face : model.getFaces()) {
                            Vertex3D[] vertices = face.getVertices();
                            IVector v1 = new Vector(new double[]{vertices[0].getX(), vertices[0].getY(), vertices[0].getZ(), 1.0});
                            IVector tv1 = v1.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogenous();
                            IVector v2 = new Vector(new double[]{vertices[1].getX(), vertices[1].getY(), vertices[1].getZ(), 1.0});
                            IVector tv2 = v2.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogenous();
                            IVector v3 = new Vector(new double[]{vertices[2].getX(), vertices[2].getY(), vertices[2].getZ(), 1.0});
                            IVector tv3 = v3.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogenous();

                            if (IRG.isAntiClockwise(tv1, tv2, tv3)) {
                                gl2.glBegin(GL.GL_LINE_LOOP);
                                gl2.glVertex2d(tv1.get(0), tv1.get(1));
                                gl2.glVertex2d(tv2.get(0), tv2.get(1));
                                gl2.glVertex2d(tv3.get(0), tv3.get(1));
                                gl2.glEnd();
                            }
                        }
                    }

                    @Override
                    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                        GL2 gl2 = glAutoDrawable.getGL().getGL2();
                        gl2.glViewport(0,0, width, height);

                        gl2.glMatrixMode(GL2.GL_PROJECTION);
                        gl2.glLoadIdentity();

                        gl2.glMatrixMode(GL2.GL_MODELVIEW);
                        gl2.glLoadIdentity();

                    }
                });

                final JFrame jFrame = new JFrame("Sedmi zadatak");
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
                jFrame.setLocationRelativeTo(null);
                jFrame.setVisible(true);
                glCanvas.requestFocusInWindow();
            }
        });
    }

}

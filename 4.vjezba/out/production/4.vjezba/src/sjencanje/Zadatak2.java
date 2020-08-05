package sjencanje;

import com.jogamp.graph.geom.Vertex;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import linearna.IMatrix;
import linearna.IRG;
import linearna.IVector;
import linearna.Vector;

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

    //odreduje vrstu sjencanje, false je za kontinuirano, true za gouraudovo
    private static boolean sjencanje = false;
    private static boolean z = false;

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
            model.calculateNormalsOfVertices();
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
                       }else if (e.getKeyCode() == KeyEvent.VK_K){
                           sjencanje = false;
                           glCanvas.display();
                       }else if (e.getKeyCode() == KeyEvent.VK_G) {
                           sjencanje = true;
                           glCanvas.display();
                       }else if (e.getKeyCode() == KeyEvent.VK_Z) {
                           z = !z;
                           if (z) {
                               System.out.println("Z-spremnik ukljucen");
                           }else{
                               System.out.println("Z-spremnik iskljucen");
                           }
                           glCanvas.display();
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

                        gl2.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);

                        //uporaba z spremnika
                        if(z) {
                            gl2.glEnable(GL2.GL_DEPTH_TEST);
                            gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
                        }else{
                            gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
                        }

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
                                gl2.glBegin(GL2.GL_POLYGON);
                                if (sjencanje) {
                                    //boja za svaki vrh
                                    Vertex3D[] vertices = face.getVertices();
                                    for (int i = 0; i < 3; i++) {
                                        Vertex3D vert = vertices[i];
                                        IVector v = new Vector(new double[]{vert.getX(), vert.getY(), vert.getZ(), 1.0});
                                        IVector tv = v.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogenous();

                                        float[] rgb = getColor(new Vector(new double[] {vert.getX(), vert.getY(), vert.getZ()}).normalize(),
                                                                          new Vector(new double[] {vert.getA(), vert.getB(), vert.getC()}).normalize(),
                                                                          eye.getEyeVector());
                                        gl2.glColor3f(rgb[0], rgb[1], rgb[2]);
                                        gl2.glVertex3d(tv.get(0), tv.get(1), tv.get(2));
                                    }
                                }else {
                                    //boja za jedan vrh
                                    Vertex3D[] vertices = face.getVertices();
                                    IVector point = face.getCenter();
                                    float[] rgb = getColor(point,
                                                           new Vector(new double[] {face.getA(), face.getB(), face.getC()}).normalize(),
                                                           eye.getEyeVector());
                                    gl2.glColor3f(rgb[0], rgb[1], rgb[2]);
                                    for (int i = 0; i < 3; i++) {
                                        Vertex3D vert = vertices[i];
                                        IVector v = new Vector(new double[]{vert.getX(), vert.getY(), vert.getZ(), 1.0});
                                        IVector tv = v.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogenous();
                                        gl2.glVertex3d(tv.get(0), tv.get(1), tv.get(2));
                                    }
                                }
                                gl2.glEnd();
                            }
                        }
                    }

                    private void drawAlgorithm3(GL2 gl2, IMatrix m) {

                        for (Face3D face : model.getFaces()) {
                            Vertex3D[] vertices = face.getVertices();
                            Vertex3D vert1 = vertices[0];
                            Vertex3D vert2 = vertices[1];
                            Vertex3D vert3 = vertices[2];
                            IVector v1 = new Vector(new double[]{vert1.getX(), vert1.getY(), vert1.getZ(), 1.0});
                            IVector tv1 = v1.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogenous();
                            IVector v2 = new Vector(new double[]{vert2.getX(), vert2.getY(), vert2.getZ(), 1.0});
                            IVector tv2 = v2.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogenous();
                            IVector v3 = new Vector(new double[]{vert3.getX(), vert3.getY(), vert3.getZ(), 1.0});
                            IVector tv3 = v3.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogenous();

                            if (IRG.isAntiClockwise(tv1, tv2, tv3)) {
                                gl2.glBegin(GL2.GL_POLYGON);
                                if(sjencanje) {
                                    float[] rgb1 = getColor(new Vector(new double[] {vert1.getX(), vert1.getY(), vert1.getZ()}),
                                                            new Vector(new double[] {vert1.getA(), vert1.getB(), vert1.getC()}),
                                                            eye.getEyeVector());
                                    gl2.glColor3f(rgb1[0], rgb1[1], rgb1[2]);
                                    gl2.glVertex3d(tv1.get(0), tv1.get(1), tv1.get(2));
                                    float[] rgb2 = getColor(new Vector(new double[] {vert2.getX(), vert2.getY(), vert2.getZ()}),
                                                            new Vector(new double[] {vert2.getA(), vert2.getB(), vert2.getC()}),
                                                            eye.getEyeVector());
                                    gl2.glColor3f(rgb2[0], rgb2[1], rgb2[2]);
                                    gl2.glVertex3d(tv2.get(0), tv2.get(1), tv2.get(2));
                                    float[] rgb3 = getColor(new Vector(new double[] {vert3.getX(), vert3.getY(), vert3.getZ()}),
                                                            new Vector(new double[] {vert3.getA(), vert3.getB(), vert3.getC()}),
                                                            eye.getEyeVector());
                                    gl2.glColor3f(rgb3[0], rgb3[1], rgb3[2]);
                                    gl2.glVertex3d(tv3.get(0), tv3.get(1), tv2.get(2));
                                }else{
                                    IVector point = face.getCenter();
                                    float[] rgb = getColor(point, new Vector(new double[] {face.getA(), face.getB(), face.getC()}).normalize(), eye.getEyeVector());
                                    gl2.glColor3f(rgb[0], rgb[1], rgb[2]);
                                    gl2.glVertex3d(tv1.get(0), tv1.get(1), tv1.get(2));
                                    gl2.glVertex3d(tv2.get(0), tv2.get(1), tv2.get(2));
                                    gl2.glVertex3d(tv3.get(0), tv3.get(1), tv2.get(2));
                                }

                                gl2.glEnd();
                            }
                        }
                    }
                    /**
                     *
                     * @param point         normalizirani vektor koji predstavlja promatranu tocku
                     * @param normal        vektor normale koji se koristi za izracun
                     * @return
                     */
                    private float[] getColor(IVector point, IVector normal, IVector eyeVector) {

                        //intenziteti izvora po komponentama
                        double iar = 0.2;
                        double iag = 0.2;
                        double iab = 0.2;
                        double idr = 0.8;
                        double idg = 0.8;
                        double idb = 0;
                        double isr = 0;
                        double isg = 0;
                        double isb = 0;

                        //konstante
                        double kar = 1;
                        double kag = 1;
                        double kab = 1;
                        double kdr = 1;
                        double kdg = 1;
                        double kdb = 1;
                        double ksr = 0.01;
                        double ksg = 0.01;
                        double ksb = 0.01;
                        double n = 96;

                        //vektor od tocke prema izvoru
                        IVector l = new Vector(new double[] {5 - point.get(0), 4 - point.get(1), 3 - point.get(2)}).normalize();
                        //reflektirani vektor
                        IVector r = normal.scalarMultiply(2).scalarMultiply(l.scalarProduct(normal)).nSub(l).normalize();
                        //vektor od tocke do oka
                        IVector v = eyeVector.nSub(point).normalize();
                        //skalarni umnozak l i n
                        double scalar = l.scalarProduct(normal);
                        if (scalar < 0) scalar = 0;

                        double ir = iar * kar + idr * kdr * scalar + isr * ksr * (r.scalarProduct(v));
                        double ig = iag * kag + idg * kdg * scalar + isg * ksg * (r.scalarProduct(v));
                        double ib = iab * kab + idb * kdb * scalar + isb * ksb * (r.scalarProduct(v));
                        return new float[] {(float)ir, (float)ig, (float)ib};
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

                final JFrame jFrame = new JFrame("Deveti zadatak");
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

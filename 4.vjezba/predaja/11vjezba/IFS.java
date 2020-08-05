package fraktali;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class IFS {

    private static int pointsNumber;
    private static int limit;
    private static int eta1;
    private static int eta2;
    private static int eta3;
    private static int eta4;

    public static void main(String[] args) {

        List<Transformation> transformations = parseInput(args[0]);

        SwingUtilities.invokeLater(() -> {

            GLProfile glProfile = GLProfile.getDefault();
            GLCapabilities glCapabilities = new GLCapabilities(glProfile);
            final GLCanvas glCanvas = new GLCanvas(glCapabilities);

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

                    Random rand = new Random();
                    gl2.glColor3f(0.0f, 0.7f, 0.3f);
                    gl2.glBegin(GL.GL_POINTS);

                    for (int brojac = 0; brojac < pointsNumber; brojac++) {
                        double x0 = 0;
                        double y0 = 0;
                        for (int iter = 0; iter < limit; iter++) {
                            double x = 0, y = 0;
                            int p = rand.nextInt(100);
                            for (Transformation t : transformations) {
                                if (p < t.getP()) {
                                    x = t.getA() * x0 + t.getB() * y0 + t.getE();
                                    y = t.getC() * x0 + t.getD() * y0 + t.getF();
                                    break;
                                }
                            }

                            x0 = x;
                            y0 = y;
                        }
                        gl2.glVertex2i((int)Math.round((x0 * eta1 + eta2)), (int)Math.round((y0 * eta3 + eta4)));
                    }
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
                    "IFS");
            jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            jFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    jFrame.dispose();
                    System.exit(0);
                }
            });
            jFrame.getContentPane().add(glCanvas, BorderLayout.CENTER);
            jFrame.setSize(640, 640);
            jFrame.setVisible(true);
            glCanvas.requestFocusInWindow();
        });
    }

    private static List<Transformation> parseInput(String filename) {

        List<Transformation> transformations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int p = 0;
            String line = br.readLine();
            while(line.startsWith("#")) {
                line = br.readLine();
            }
            pointsNumber = Integer.parseInt(line.split(" ")[0]);
            line = br.readLine();
            limit = Integer.parseInt(line.split(" ")[0]);
            line = br.readLine();
            String[] parts = line.split(" ");
            eta1 = Integer.parseInt(parts[0]);
            eta2 = Integer.parseInt(parts[1]);
            line = br.readLine();
            parts = line.split(" ");
            eta3 = Integer.parseInt(parts[0]);
            eta4 = Integer.parseInt(parts[1]);
            line = br.readLine();
            while(line.startsWith("#")) {
                line = br.readLine();
            }
            double bound = 0;
            while(line != null) {
                parts = line.strip().split(" ");
                Transformation t = new Transformation();
                t.setA(Double.parseDouble(parts[0]));
                t.setB(Double.parseDouble(parts[1]));
                t.setC(Double.parseDouble(parts[2]));
                t.setD(Double.parseDouble(parts[3]));
                t.setE(Double.parseDouble(parts[4]));
                t.setF(Double.parseDouble(parts[5]));
                bound = bound + (Double.parseDouble(parts[6]) * 100);
                t.setP(bound);
                transformations.add(t);
                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(NumberFormatException e){
            e.printStackTrace();
        }

        return transformations;
    }
}

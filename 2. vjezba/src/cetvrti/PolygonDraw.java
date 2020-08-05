package cetvrti;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

public class PolygonDraw {

	private static PolygonState state = new PolygonState();

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {

			GLProfile glProfile = GLProfile.getDefault();
			GLCapabilities glCapabilities = new GLCapabilities(glProfile);
			final GLCanvas glCanvas = new GLCanvas(glCapabilities);
			
			glCanvas.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_K) {
						state.setKonveksnost(!state.isKonveksnost());
					}else if (e.getKeyCode() == KeyEvent.VK_P && !state.isState()) {
							state.setPopunjavanje(!state.isPopunjavanje());
					}else if(e.getKeyCode() == KeyEvent.VK_N) {
						state.switchState();
						state.setCurrentPosition(null);
					}
					glCanvas.display();
				}
			});
			
			glCanvas.addMouseListener(new MouseAdapter() {
				@Override
				 public void mouseClicked(MouseEvent e) {
					Point last = e.getPoint();
					Polygon p = state.getPolygon();
					
					if (!state.isState()) {
						if(state.isKonveksnost() && p.getNumberOfPoints() > 3) {
							if(p.checkConvex(last)) {
								p.addPoint(last);
							}else {
								System.out.println(String.format("Nemoguće dodati točku (%d, %d) jer poligon više ne bi bio konveksan.",
											(int)last.getX(), (int)last.getY()));
							}
						}else {
							p.addPoint(last);
						}
						glCanvas.display();
					}else {
						if(p.getNumberOfPoints() > 0) {
							int status = checkPointStatus(last, p);
							if(status < 0) {
								System.out.println((String.format("Točka (%d, %d) se nalazi izvan poligona.", last.x, last.y)));
							}else if (status == 0) {
								System.out.println((String.format("Točka (%d, %d) se nalazi na bridu poligona.", last.x, last.y)));
							}else {
								System.out.println((String.format("Točka (%d, %d) se nalazi unutar poligona.", last.x, last.y)));
							}
						}		
					}	
				}

				private int checkPointStatus(Point last, Polygon p) {
					
					List<PolynomElement> elements = p.getElements();
					List<Point> points = p.getPoints();
					boolean orij;
					if (orij = p.checkOrientation()) {
						elements = reverseList(p.getElements());
					}
					int xmin = (int)points.get(0).getX();
					int xmax = xmin;
					int ymin= (int)points.get(0).getY();
					int ymax = ymin;
					for(Point point : points) {
						if(point.getX() < xmin) xmin = (int)point.getX();
						if(point.getX() > xmax) xmax = (int)point.getX();
						if(point.getY() < ymin) ymin = (int)point.getY();
						if(point.getY() > ymax) ymax = (int)point.getY();
					}
					
					if (last.x < xmin || last.x > xmax || last.y < ymin || last.y > ymax) {
						return -1;
					}
					
					for (int y = ymin; y <= ymax; y++ ) {
						if (last.y == y) {
							double L = xmin ; 
							double D = xmax ;
							int i0=elements.size()-1;
							int i;
							for ( i = 0; i < elements.size(); i0 = i++) {
								PolynomElement element0 = elements.get(i0);
								PolynomElement element = elements.get(i);
								//ako je brid vodoravan
								if(element0.getA() == 0) {
									if(element0.getVrh().y == y) {
										if(element0.getVrh().getX() < element.getVrh().getX()) {
											L = element0.getVrh().getX();
											D = element.getVrh().getX();
										}else {
											L = element.getVrh().getX();
											D = element0.getVrh().getX();
										}
										break;
									}
								//inače nađi sjecište za regularan brid
								}else {	
									double x = (-y * element0.getB() - element0.getC())/(double)element0.getA();
									//ovisno o smjeru zadavanja vrhova
									if(orij) {
										if(!element0.isLijevi()) {
											if (L < x) L = x;
										}else {
											if (D > x) D = x;
										}
									}else {
										if(element0.isLijevi()) {
											if (L < x) L = x;
										}else {
											if (D > x) D = x;
										}
									}
								}
							}
							if (last.x == L || last.x == D) return 0;
							else if (last.x < L || last.x > D) return -1;
							return 1;
						}
					}
					return -1;
				}
				
			});

			glCanvas.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					if (!state.isState()) {
						state.setCurrentPosition(e.getPoint());
						glCanvas.display();
					}
				}
			});

			glCanvas.addGLEventListener(new GLEventListener() {

				@Override
				public void display(GLAutoDrawable glAutoDrawable) {
					GL2 gl2 = glAutoDrawable.getGL().getGL2();

					if(state.isKonveksnost()) {
						gl2.glClearColor(0.0f, 0.8f, 0.4f, 1.0f);
					}else {
						gl2.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
					}
					gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
					
					gl2.glColor3f(0.0f, 0.0f, 0.0f);
					Polygon p = state.getPolygon();
			
					if (p.getNumberOfPoints() <= 1) {
						drawLine(gl2);
					} else {
						if (state.isPopunjavanje()) {
							fillPolygon(gl2);
						} else {
							drawLineLoop(gl2);
						}
					}
				}

				private void drawLine(GL2 gl2) {
					Polygon polygon = state.getPolygon();
					if(state.getCurrentPosition() != null && !polygon.getPoints().isEmpty()) {
						gl2.glBegin(GL.GL_LINES);
						gl2.glVertex2f((float) polygon.getPoints().get(0).getX(), (float) polygon.getPoints().get(0).getY());
						gl2.glVertex2f((float) state.getCurrentPosition().getX(), (float) state.getCurrentPosition().getY());
						gl2.glEnd();
					}
				}

				private void drawLineLoop(GL2 gl2) {
					Polygon polygon = state.getPolygon();
					gl2.glBegin(GL.GL_LINE_LOOP);
					List<Point> list = polygon.getPoints();
					for (Point p : list) {
						gl2.glVertex2f((float) p.getX(), (float) p.getY());
					}
					//zadnji pomozaj misa se dodaje samo ako je stanje 0
					if(!state.isState() ) {
						gl2.glVertex2f((float)state.getCurrentPosition().getX(), (float)state.getCurrentPosition().getY());
					}
					gl2.glEnd();
				}

				private void fillPolygon(GL2 gl2) {
					//kopija liste tocaka
					List<Point> points = new ArrayList<>(state.getPolygon().getPoints());
					//kopija liste elemenata
					List<PolynomElement> elements = new ArrayList<>(state.getPolygon().getElements()); 
					
					Polygon polygonCopy = new Polygon(points, elements);
					
					//dodaj novu točku ako je miš pomaknut otkad je zadnja definirana
					if(!state.isState() && !points.contains(state.getCurrentPosition())) {
						polygonCopy.addPoint(state.getCurrentPosition());
					}
					
					boolean orij;
					//ako je smjer suprotan od smjera kazaljke na satu, zamijenit redoslijed elemenata
					if (orij = polygonCopy.checkOrientation()) {
						elements = reverseList(polygonCopy.getElements());
					}
					
					int xmin = (int)points.get(0).getX();
					int xmax = xmin;
					int ymin= (int)points.get(0).getY();
					int ymax = ymin;
					for(Point p : points) {
						if(p.getX() < xmin) xmin = (int)p.getX();
						if(p.getX() > xmax) xmax = (int)p.getX();
						if(p.getY() < ymin) ymin = (int)p.getY();
						if(p.getY() > ymax) ymax = (int)p.getY();
					}
					
					for (int y = ymin; y <= ymax; y++ ) {
						double L = xmin ; 
						double D = xmax ;
						int i0=elements.size()-1;
						int i;
						for ( i = 0; i < elements.size(); i0 = i++) {
							PolynomElement element0 = elements.get(i0);
							PolynomElement element = elements.get(i);
							//ako je brid vodoravan
							if(element0.getA() == 0) {
								if(element0.getVrh().y == y) {
									if(element0.getVrh().getX() < element.getVrh().getX()) {
										L = element0.getVrh().getX();
										D = element.getVrh().getX();
									}else {
										L = element.getVrh().getX();
										D = element0.getVrh().getX();
									}
									break;
								}
							//inače nađi sjecište za regularan brid
							}else {	
								double x = (-y * element0.getB() - element0.getC())/(double)element0.getA();
								//ovisno o smjeru zadavanja vrhova
								if(orij) {
									if(!element0.isLijevi()) {
										if (L < x) L = x;
									}else {
										if (D > x) D = x;
									}
								}else {
									if(element0.isLijevi()) {
										if (L < x) L = x;
									}else {
										if (D > x) D = x;
									}
								}
							}
						}
						gl2.glBegin(GL.GL_LINES);
						gl2.glVertex2f((float)L, (float)y);
						gl2.glVertex2f((float)D, (float)y);
						gl2.glEnd();
					}
				}

				@Override
				public void dispose(GLAutoDrawable glAutoDrawable) {
				}

				@Override
				public void init(GLAutoDrawable glAutoDrawable) {
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
	                "Četvrta laboratorijska vježba");
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
	
	private static List<PolynomElement> reverseList(List<PolynomElement> elements) {
		List<PolynomElement> reverse = new ArrayList<>();
		for (int i = elements.size()-1; i >= 0; i--) {
			reverse.add(elements.get(i));
		}
		return reverse;
	}
}

package cetvrti;
import java.awt.Point;

public class PolynomElement {
	
	private Point vrh; 
	private int a; 
	private int b; 
	private int c;
	private boolean lijevi;
	
	
	
	public PolynomElement(Point vrh, int a, int b, int c, boolean lijevi) {
		super();
		this.vrh = vrh;
		this.a = a; 
		this.b = b;
		this.c = c;
		this.lijevi = lijevi;
	}
	
	public Point getVrh() {
		return vrh;
	}
	public void setVrh(Point vrh) {
		this.vrh = vrh;
	}

	public boolean isLijevi() {
		return lijevi;
	}
	public void setLijevi(boolean lijevi) {
		this.lijevi = lijevi;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}
	
}

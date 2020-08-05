package peti;

public class Face3D {

	private int[] indexes;
	
	//koeficijenti jednadzbe ravnine
	private double a;
	private double b;
	private double c; 
	private double d;
	
	public Face3D(int[] indexes) {
		super();
		this.indexes = indexes;
	}

	public Face3D(int a, int b, int c) {
		indexes = new int [3];
		indexes[0] = a;
		indexes[1] = b;
		indexes[2] = c;
	}

	public int[] getIndexes() {
		return indexes;
	}

	public void setIndexes(int[] indexes) {
		this.indexes = indexes;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}
}

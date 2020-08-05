package sjencanje;

import linearna.IVector;
import linearna.Vector;

import java.util.Set;

public class Face3D {

	private int[] indexes;
	private boolean visible;

	//koeficijenti jednadzbe ravnine
	private double a;
	private double b;
	private double c; 
	private double d;

	private Vertex3D[] vertices;

	private Set<Vertex3D> verticesSet;

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

	public Vertex3D[] getVertices() {
		return vertices;
	}

	public void setVertices(Vertex3D[] vertices) {
		this.vertices = vertices;
	}

	public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Set<Vertex3D> getVerticesSet() {
		return verticesSet;
	}

	public void setVerticesSet(Set<Vertex3D> verticesSet) {
		this.verticesSet = verticesSet;
	}

	/**
	 * Calculates normalised vector representing center of polygon.
	 * @return
	 */
	public IVector getCenter() {
		double x = 0;
		double y = 0;
		double z = 0;

		for (Vertex3D v : vertices){
			x += v.getX();
			y += v.getY();
			z += v.getZ();
		}

		x /= 3;
		y /= 3;
		z /= 3;

		return new Vector(new double[] {x, y, z}).normalize();
	}
}

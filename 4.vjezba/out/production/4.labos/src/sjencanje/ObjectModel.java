package sjencanje;

import linearna.IVector;
import linearna.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectModel {

	private List<Vertex3D> vertexes = new ArrayList<>();
	private List<Face3D> faces = new ArrayList<>();

	public ObjectModel() {
	}
	
	public ObjectModel(List<Vertex3D> vertexes, List<Face3D> faces) {
		this.vertexes = vertexes;
		this.faces = faces;
	}

	public ObjectModel copy() {
		return new ObjectModel(new ArrayList<Vertex3D>(this.vertexes), 
							   new ArrayList<Face3D>(this.faces));
	}
	
	public List<Vertex3D> getVertexes() {
		return vertexes;
	}

	public void setVertexes(List<Vertex3D> vertexes) {
		this.vertexes = vertexes;
	}

	public List<Face3D> getFaces() {
		return faces;
	}

	public void setFaces(List<Face3D> faces) {
		this.faces = faces;
	}

	public void addFace3D(Face3D f) {

		Vertex3D v1 = vertexes.get(f.getIndexes()[0]);
		Vertex3D v2 = vertexes.get(f.getIndexes()[1]);
		Vertex3D v3 = vertexes.get(f.getIndexes()[2]);
		
		double[] first = {v2.getX() - v1.getX(), v2.getY() - v1.getY(), v2.getZ() - v1.getZ()};
		double[] second = {v3.getX() - v1.getX(), v3.getY() - v1.getY(), v3.getZ() - v1.getZ()};
		
		double a = first[1] * second[2] - first[2] * second[1];
		f.setA(a);
		double b = first[2] * second[0] - first[0] * second[2];
		f.setB(b);
		double c = first[0] * second[1] - first[1] * second[0];
		f.setC(c);
		double d = -a * v1.getX() - b * v1.getY() - c * v1.getZ();
		f.setD(d);

		Vertex3D[] vertices = {v1, v2, v3};
		Set<Vertex3D> verticesSet = new HashSet<>();
		verticesSet.add(v1);
		verticesSet.add(v2);
		verticesSet.add(v3);

		f.setVertices(vertices);
		f.setVerticesSet(verticesSet);

		faces.add(f);
	}
	
	public String dumpToOBJ() {
		StringBuilder sb = new StringBuilder();
		
		for (Vertex3D v : vertexes) {
			sb.append(String.format("v %f %f %f\n", 
									v.getX(), 
									v.getY(),
									v.getZ()));
		}
		
		for (Face3D f : faces) {
			sb.append(String.format("f %d %d %d\n", 
									f.getIndexes()[0], 
									f.getIndexes()[1], 
									f.getIndexes()[2]));
		}
		
		return sb.toString();
	}
	
	public void normalize() {
		
		double xmin = vertexes.get(0).getX();
		double xmax = xmin; 
		double ymin = vertexes.get(0).getY();
		double ymax = ymin;
		double zmin = vertexes.get(0).getZ();
		double zmax = zmin;
		
		for (Vertex3D v : vertexes) {
			if (v.getX() < xmin) xmin = v.getX(); 
			if (v.getX() > xmax) xmax = v.getX();
			if (v.getY() < ymin) ymin = v.getY();
			if (v.getY() > ymax) ymax = v.getY();
			if (v.getZ() < zmin) zmin = v.getZ();
			if (v.getZ() > zmax) zmax = v.getZ();
		}
		
		double xm = (xmin + xmax) / 2;
		double ym = (ymin + ymax) / 2;
		double zm = (zmin + zmax) / 2;
		
		double M = Math.max(xmax - xmin, ymax - ymin);
		M = Math.max(M, zmax - zmin);
		
		for (Vertex3D v : vertexes) {
			v.setX((v.getX() - xm) * 2 / M);
			v.setY((v.getY() - ym) * 2 / M);
			v.setZ((v.getZ() - zm) * 2 / M);
		}

		for (Face3D face : faces) {

			Vertex3D v1 = face.getVertices()[0];
			Vertex3D v2 = face.getVertices()[1];
			Vertex3D v3 = face.getVertices()[2];

			double[] first = {v2.getX() - v1.getX(), v2.getY() - v1.getY(), v2.getZ() - v1.getZ()};
			double[] second = {v3.getX() - v1.getX(), v3.getY() - v1.getY(), v3.getZ() - v1.getZ()};

			double a = first[1] * second[2] - first[2] * second[1];
			face.setA(a);
			double b = first[2] * second[0] - first[0] * second[2];
			face.setB(b);
			double c = first[0] * second[1] - first[1] * second[0];
			face.setC(c);
			double d = -a * v1.getX() - b * v1.getY() - c * v1.getZ();
			face.setD(d);
		}
	}
	
	public int pointStatus(double x, double y, double z) {
		
		int ispod = 0;
		int na = 0;
		
		for (Face3D face : faces) {
			double izraz = face.getA() * x + face.getB() * y 
						 + face.getC() * z + face.getD();
			if(izraz > 0) {
				return 1;			  //izvan tijela
			} else if (izraz == 0) {
				na++;
			}else {
				ispod ++;		
			}
		}
		
		if (ispod == faces.size()) {  //unutar tijela
			return -1;
		}else if (ispod + na == faces.size()) {
			return 0;
		}
		return 1; 							//izvan tijela
	}

	//algoritam1
	public void determineFaceVisibilities1(IVector eye) {
		for (Face3D face : faces) {
			double expression = face.getA() * eye.get(0) + face.getB() * eye.get(1) +
								face.getC() * eye.get(2) + face.getD();

			face.setVisible(expression > 0 ? true : false);
		}
	}

	//algoritam2
	public void determineFaceVisibilities2(IVector eye) {
		for (Face3D face : faces) {
			Vertex3D v1 = face.getVertices()[0];
			Vertex3D v2 = face.getVertices()[1];
			Vertex3D v3 = face.getVertices()[2];

			double cx = (v1.getX() + v2.getX() + v3.getX()) / 3;
			double cy = (v1.getY() + v2.getY() + v3.getY()) / 3;
			double cz = (v1.getZ() + v2.getZ() + v3.getZ()) / 3;

			IVector c = new Vector(new double[] {cx, cy, cz});
			IVector e = eye.nSub(c);
			IVector n =new Vector(new double[] {face.getA(), face.getB(), face.getC()});
			double scalar = n.scalarProduct(e);

			face.setVisible(scalar > 0 ? true : false);
		}
	}

	public void calculateNormalsOfVertices() {
		for (Vertex3D vertex : vertexes) {
				List<Face3D> fs = new ArrayList<>();
				for (Face3D face : faces) {
					if (face.getVerticesSet().contains(vertex)) {
						fs.add(face);
					}
				}

				double a = 0;
				double b = 0;
				double c = 0;

				for (Face3D f : fs) {
					a += f.getA();
					b += f.getB();
					c += f.getC();
				}

				a /= fs.size();
				b /= fs.size();
				c /= fs.size();

				double norm = Math.sqrt(a * a + b * b + c * c);
				vertex.setA(a/norm);
				vertex.setB(b/norm);
				vertex.setC(c/norm);
		}

	}
}

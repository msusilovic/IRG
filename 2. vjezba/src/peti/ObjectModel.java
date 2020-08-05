package peti;

import java.util.ArrayList;
import java.util.List;

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
}

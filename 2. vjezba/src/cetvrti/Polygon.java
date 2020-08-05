package cetvrti;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Polygon {

	private List<Point> points = new LinkedList<>();
	private List<PolynomElement> elements = new LinkedList<>();
	
	public Polygon() {
	}
	
	public Polygon(List<Point> points, List<PolynomElement> elements) { 
		this.points = points;
		this.elements = elements;
	}
	
	public void addPoint(Point point) {
		points.add(point);
		if(elements.size() > 1) {
			//makni brid koji povezuje dosadasnju zadnju i prvu tocku 
			elements.remove(elements.size()-1);
		}
		addElement(false);
		addElement(true);
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public int getNumberOfPoints() {
		return points.size();
	}
	
	public List<PolynomElement> getElements() {
		return this.elements;
	}
	
	/**
	 * @param last	<code>true</code> ako je element koji se dodaje za zadnju točku, 
	 * 					<code>false</code> inače
	 */
	private void addElement(boolean last) {
		int n = points.size();
		if (points.size() == 1)
			return;
		
		Point current;
		Point next;

		if (last) {
			current = points.get(n - 1);
			next = points.get(0);
		} else {
			current = points.get(n - 2);
			next = points.get(n - 1);
		}

		int a = (int) (current.getY() - next.getY());
		int b = -(int) (current.getX() - next.getX());
		int c = (int) (current.getX() * next.getY() - current.getY() * next.getX());

		elements.add(new PolynomElement(current, a, b, c, next.getY() < current.getY() ? true : false));
	}
	
	/**
	 * Provjerava je li poligon i dalje konveksan ako se doda nova točka.
	 * 
	 */
	public boolean checkConvex(Point last) {
		
		int i0, r;
		int ispod = 0; 
		int iznad = 0; 
		int na = 0;
		
		//kopija trenutnog poligona s novom tockom
		List<Point> points = new ArrayList<>(this.getPoints());
		List<PolynomElement> elements = new ArrayList<>(this.getElements()); 
		Polygon polygonCopy = new Polygon(points, elements);
		polygonCopy.addPoint(last);
		
		i0 = points.size()-2;
		for (int i = 0; i < points.size(); i++, i0++) {
			if (i0 >= points.size()) {
				i0 = 0;
			}
			PolynomElement element0 = elements.get(i0);
			PolynomElement element = elements.get(i);
			
			r = element0.getA() * element.getVrh().x +
				element0.getB() * element.getVrh().y + 
				element0.getC();
			
			if (r == 0) na++;
			else if (r > 0) iznad++;
			else ispod++;
		}
		
		boolean konveksan = false;
		
		if(ispod == 0 || iznad == 0) konveksan = true;
		
		return konveksan;
	}
	
	public boolean checkOrientation() {
		int i0, r;
		int iznad = 0; 
		
		i0 = points.size()-2;
		for (int i = 0; i < points.size(); i++, i0++) {
			if (i0 >= points.size()) {
				i0 = 0;
			}
			PolynomElement element0 = elements.get(i0);
			PolynomElement element = elements.get(i);
			
			r = element0.getA() * element.getVrh().x +
				element0.getB() * element.getVrh().y + 
				element0.getC();
			
		 if (r > 0) iznad++;
		}
		
		if (iznad == 0) return true;
		
		return false;
	}

}

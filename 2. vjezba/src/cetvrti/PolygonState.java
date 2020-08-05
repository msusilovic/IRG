package cetvrti;
import java.awt.Point;

public class PolygonState {

	private boolean popunjavanje = false;
	private boolean konveksnost = false;
	private boolean state = false;
	
	private Polygon polygon = new Polygon();
	private Point currentPosition;
	
	public boolean isPopunjavanje() {
		return popunjavanje;
	}
	
	public void setPopunjavanje(boolean popunjavanje) {
		this.popunjavanje = popunjavanje;
	}
	
	public boolean isKonveksnost() {
		return konveksnost;
	}
	
	public void setKonveksnost(boolean konveksnost) {
		this.konveksnost = konveksnost;
	}
	
	public boolean isState() {
		return state;
	}
	
	public void setState(boolean state) {
		this.state = state;
	}

	public Point getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Point currentPosition) {
		this.currentPosition = currentPosition;
	}

	public void switchState() {
		state = !state;
		if (state == false) {
			popunjavanje = false;
			konveksnost = false;
			polygon = new Polygon();	
		}
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}
}

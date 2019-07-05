package distanceVector;

import java.util.HashMap;

public class Router {

	int id;
	HashMap<String, CostoRuta> tabla; //tabla de ruteo
	
	public Router (int id) {
		this.id = id;
		tabla = new HashMap<String,CostoRuta>();
	}
	
	public boolean addRuta(String red, Link link, int costo) {
		
		if (link.getId() == 0) { //LINK LOCAL
			CostoRuta cr = new CostoRuta(link, link.getCosto() + costo);
			tabla.put(red, cr);
			return true;
		}
		else {
			CostoRuta cr = new CostoRuta(link, link.getCosto() + costo);
			int id1 = link.getR1().getId();
			int id2 = link.getR2().getId();
			
			if ( (id == id1) || (id == id2)) { //si es adyacente
				tabla.put(red, cr);
				return true;
			} else
				return false;
		}
		
		
	}
	
	public int getId() {
		return this.id;
	}

}

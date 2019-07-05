package distanceVector;

import java.util.HashMap;
import java.util.Map.Entry;

public class Router {

	int id;
	HashMap<String, CostoRuta> tabla; //tabla de ruteo
	HashMap<Router, Link> adyacentes;
	
	public Router (int id) {
		this.id = id;
		tabla = new HashMap<String,CostoRuta>();
		adyacentes = new HashMap<Router,Link>();
	}
	
	public int getId() {
		return this.id;
	}
	
	public void agregarAdyacente (Router r, Link l) {
		adyacentes.put(r,l);
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
	
	
	public void intercambiarRutas() {
		
		Router router;
		int costolink;
		
		for ( Entry<Router, Link> entry : adyacentes.entrySet() ){
			router = entry.getKey();
			costolink = entry.getValue().getCosto();
			
			router.recibirTabla(tabla, costolink);
		}
	}
	
	public void recibirTabla(HashMap<String, CostoRuta> tablaRecibida, int costolink) {
	
		String red;
		CostoRuta costoRuta;
		
		for ( Entry<String, CostoRuta> entry : tablaRecibida.entrySet() ){
			
			red = entry.getKey();
			costoRuta = entry.getValue();
			
			if(tabla.containsKey(red)) { //Ya tengo la red, veo si me sirve
				
				int costoNuevo = costoRuta.getCosto() + costolink;
				int costoActual = tabla.get(red).getCosto();
				
				if (costoNuevo < costoActual) {
					tabla.put(red, costoRuta); //actualizo el costo de la red
				}
				
			}else { //No tenia como llegar a esa red, la agrego
				this.addRuta(red, costoRuta.getLink(), costolink);
			}
			
		}		
	}	
			
			
	
	
	

}

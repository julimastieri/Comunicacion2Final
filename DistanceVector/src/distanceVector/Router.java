package distanceVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Router {
	
	static final int INFINITO = 88888; 

	int id;
	HashMap<String, CostoRuta> tabla; //tabla de ruteo que contiene los valores utilizados en el intercambio
	HashMap<String, CostoRuta> tablaNueva;
	HashMap<Router, Link> adyacentes;
	HashMap<String,CostoRuta> tablaint;
	
	public Router (int id) {
		this.id = id;
		tabla = new HashMap<String,CostoRuta>();
		tablaNueva = new HashMap<String,CostoRuta>();
		adyacentes = new HashMap<Router,Link>();
		
		tablaint = new HashMap<String,CostoRuta>();
	}
	
	public int getId() {
		return this.id;
	}
	
	public HashMap<String, CostoRuta> getTabla(){
		return tabla;
	}
	
	public HashMap<String, CostoRuta> getTablaTrigger(){
		return tablaint;
	}
	
	public void agregarAdyacente (ArrayList<Link> links) {
		Link laux;
		int id1;
		int id2;
		for(int i=0; i<links.size(); i++) {
			  laux = new Link (links.get(i));
			  id1 = laux.getR1().getId();
			  id2 = laux.getR2().getId();
			  if (id == id1) {
				  adyacentes.put(laux.getR2(), laux);
			  } else if (id == id2) {
				  		adyacentes.put(laux.getR1(), laux);
			  		}
		}
	}
	
	public boolean addRuta(String red, Link link, int costoOriginal) {
		
		if (link.getId() == 0) { //LINK LOCAL
			CostoRuta cr = new CostoRuta(link, link.getCosto() + costoOriginal); //poner 0 directamente???
			tablaNueva.put(red, cr);
			return true;
		}
		else {
			CostoRuta cr = new CostoRuta(link, link.getCosto() + costoOriginal);
			int id1 = link.getR1().getId();
			int id2 = link.getR2().getId();
			
			if ( (id == id1) || (id == id2)) { //si es adyacente
				tablaNueva.put(red, cr);
				return true;
			} else
				return false;
		}
	}
	
	public void intercambiarRutas(HashMap<String,CostoRuta> tablaint) {
		
		Router router;
		Link link;
		
		for ( Entry<Router, Link> entry : adyacentes.entrySet() ){
			router = entry.getKey(); 
			link = adyacentes.get(router);
			
			if (link.isActivo())
				router.recibirTabla(tablaint, link);
		}
	}
	
	public void recibirTabla(HashMap<String, CostoRuta> tablaRecibida, Link link) {
	
		String red;
		CostoRuta costoRuta;
		
		for ( Entry<String, CostoRuta> entry : tablaRecibida.entrySet() ){
			
			red = entry.getKey(); 
			costoRuta = entry.getValue(); 
			
			if(tablaNueva.containsKey(red)) { //Ya tengo la red, veo si me sirve
				
				int costoNuevo = costoRuta.getCosto() + link.getCosto(); 
				int costoActual = tablaNueva.get(red).getCosto();
				int idActual = tablaNueva.get(red).getLink().getId();
				
				if ((costoNuevo < costoActual) || ((costoNuevo > costoActual) && (link.getId() == idActual))) {
					CostoRuta cR = new CostoRuta(link,costoNuevo);
					tablaNueva.put(red, cR); //actualizo el costo de la red
				}
				
			}else { //No tenia como llegar a esa red, la agrego
				this.addRuta(red, link, costoRuta.getCosto());
			}
		}		
	}	
			
	public void actualizarTabla() {
		tabla = new HashMap<String,CostoRuta>(tablaNueva);
	}
	
	public void imprimirTabla() {
		String red;
		CostoRuta cR;
		System.out.println("\n");
		System.out.println("Tabla del Router "+id);
		System.out.printf("%-20s%-20s%-20s\n","Red","Link","Costo");
		for ( Entry<String, CostoRuta> entry : tablaNueva.entrySet() ){
			red = entry.getKey();
			cR = entry.getValue();
			System.out.printf("%-20s%-20s%-20s\n",red,cR.getLink().getId(),cR.getCosto());
		}
	}

	public void chequearCaidaLink(int idLinkCaido) {
		int idLink;
		String red;
		CostoRuta cRuta;
		Link l;
		
		for ( Entry<String, CostoRuta> entry : tablaNueva.entrySet() ){ //Tabla y TablaNueva son iguales en este momento?
			l = entry.getValue().getLink();
			idLink = l.getId(); 
			
			if (idLink == idLinkCaido) {
				red = entry.getKey();
				cRuta = new CostoRuta(l, INFINITO);
				tablaNueva.put(red, cRuta); 
				tablaint.put(red, cRuta); //tabla auxiliar con las lineas que cambiaron solamente
			} 
		}
		
		Router r;
		
		for ( Entry<Router, Link> entry : adyacentes.entrySet() ){ 
			r = entry.getKey();
			l = entry.getValue();
			idLink = l.getId();
			
			if(idLink == idLinkCaido) {
				l.deshabilitarLink();
				adyacentes.put(r,l);
			}
		}
		
	}
	


	
	
	
}

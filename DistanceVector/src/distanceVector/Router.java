package distanceVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Router {
	
	static final int INFINITO = 88; 

	int id;
	HashMap<String, CostoRuta> tabla;//tabla de ruteo que contiene los valores utilizados en el intercambio
	HashMap<String, CostoRuta> tablaNueva;
	HashMap<Router, Link> adyacentes;
	
	public Router (int id) {
		this.id = id;
		tabla = new HashMap<String,CostoRuta>();
		tablaNueva = new HashMap<String,CostoRuta>();
		adyacentes = new HashMap<Router,Link>();

	}
	
	public int getId() {
		return this.id;
	}
	
	public HashMap<String, CostoRuta> getTabla(){
		return tabla;
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
			CostoRuta cr = new CostoRuta(link, 0); 
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
		
		Router routerAdy;
		Link linkAdy, linkTabla;
		CostoRuta cR;
		String red;
		String costo;
		HashMap<String,CostoRuta> tablaAux = new HashMap<String,CostoRuta>();
		
		for ( Entry<Router, Link> entry : adyacentes.entrySet() ){
			routerAdy = entry.getKey(); 
			linkAdy = entry.getValue();
			
			//copio contenido de la tabla de intercambio para poder modificarlo acorde a 
			for ( Entry<String,CostoRuta> entry4 : tablaint.entrySet() ){
				red = entry4.getKey();
				linkTabla = new Link(entry4.getValue().getLink());
				cR = new CostoRuta(linkTabla, entry4.getValue().getCosto());
				tablaAux.put(red, cR);
			}
			
			for ( Entry<String,CostoRuta> entry2 : tablaAux.entrySet() ){
				
				linkTabla = entry2.getValue().getLink();
				/*si el link que me conecta con el router adyacente 
				es el que uso para llegar a una determinada red,
				le debo mandar costo infinito*/
				if (linkAdy.getId() == linkTabla.getId()) {
					entry2.getValue().setCosto(INFINITO);
				}
			}
		
			//imprimo mensajes
			System.out.print("Router "+id +"->Router "+routerAdy.getId()+"(L"+linkAdy.getId()+"): ");
			for ( Entry<String,CostoRuta> entry3 : tablaAux.entrySet() ){
				red = entry3.getKey();
				cR = entry3.getValue();
				
				if (cR.getCosto() < INFINITO) {
					costo="";
					costo += cR.getCosto();
				}
				else 
					costo = "Infinito";

				System.out.print("("+ red +", "+ costo +") ");
			}
			
			System.out.println();
			if (linkAdy.isActivo())
				routerAdy.recibirTabla(tablaAux, linkAdy);
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
			
	public boolean actualizarTabla() {
		boolean res = tabla.equals(tablaNueva);
		tabla = new HashMap<String,CostoRuta>(tablaNueva);
		return res;
	}
	
	public void imprimirTabla() {
		String red;
		CostoRuta cR;
		String costo;
		String link;
		
		System.out.println("\n");
		System.out.println("Tabla del Router "+id);
		System.out.printf("%-20s%-20s%-20s\n","Red","Link","Costo");
		for ( Entry<String, CostoRuta> entry : tablaNueva.entrySet() ){
			red = entry.getKey();
			cR = entry.getValue();
			
			if (cR.getCosto() < INFINITO) {
				costo="";
				costo += cR.getCosto();
			}
			else 
				costo = "Infinito";
			
				
			if (cR.getLink().getId() != 0) {
				link="";
				link+=cR.getLink().getId();
			}
			else
				link= "Local";
			
			System.out.printf("%-20s%-20s%-20s\n",red,link,costo);

			}
		}

	public void chequearCaidaLink(int idLinkCaido) {
		int idLink;
		String red;
		CostoRuta cRuta;
		Link l;
		HashMap<String,CostoRuta> tablaint = new HashMap<String,CostoRuta>();
		
		for ( Entry<String, CostoRuta> entry : tablaNueva.entrySet() ){
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
				adyacentes.remove(r);
				adyacentes.put(r,l);
			}
		}
		
		intercambiarRutas(tablaint);
		
	}

	public String mostrarRedesLocales() {
		StringBuilder redesLoc = new StringBuilder();
		
		String red;
		for ( Entry<String, CostoRuta> entry : tablaNueva.entrySet() ){
			if  (entry.getValue().getCosto() == 0) {
				red = entry.getKey();
				redesLoc.append("Router "+this.id + " conectado a la red: "+red+" por link local \n");
			}
		}
		return redesLoc.toString();
	}
	
	public String getRedesLocales() {
		StringBuilder redesLoc = new StringBuilder();
		
		String red;
		for ( Entry<String, CostoRuta> entry : tablaNueva.entrySet() ){
			if  (entry.getValue().getCosto() == 0) {
				red = entry.getKey();
				redesLoc.append(this.id + "\n");
				redesLoc.append(red+"\n");
			}
		}
		return redesLoc.toString();
	}
	
	public void reset() {
		
		for ( Entry<String, CostoRuta> entry : tabla.entrySet() ){
			if  (entry.getValue().getCosto() > 0)
				tablaNueva.remove(entry.getKey());
		}
		tabla = new HashMap<String,CostoRuta>();
		adyacentes = new HashMap<Router,Link>();
	}

}

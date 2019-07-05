package distanceVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class main {
	
	static final Link LOCAL = new Link(0, 0, null, null);
	static ArrayList<Link> links = new ArrayList<Link>();
	static HashMap<Integer,Router> routers = new HashMap<Integer,Router>();
	static boolean stop = false;
	
	public static void main (String[] arg) {
	
		//cargar routers de un txt o manualmente por consola
		crearRouter(1);
		crearRouter(2);
		crearRouter(3);
		crearRouter(4);
		
		//CREO REDES
		crearRed(1, "2001:100A::/64");
		crearRed(2, "2001:100B::/64");
		crearRed(3, "2001:100C::/64");
		crearRed(4, "2001:100D::/64");
		
		//conecto los routers
		crearLink(1,1,1,2);
		crearLink(1,1,1,2);
		crearLink(2,1,1,3);
		crearLink(3,1,2,4);
		crearLink(4,1,3,4);
		
		//agrego los adyacentes
		Router r;
		for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			r.agregarAdyacente(links);
		}
		
		while (!stop){
			//recorrer lista routers for ()
		}
		/*
		r1.addRuta("2001:100B::/64", l1, 1); //esto se hace cdo se quiere agregar una nueva ruta
		r2.addRuta("2001:100A::/64", l1, 1);
		
		r1.addRuta("2001:100C::/64", l2, 1);
		r3.addRuta("2001:100A::/64", l2, 1);
		*/
		
		
		
		//falta hacer como hacen los routers para descubrrir nevas rutas
		
		//guardar si se quiere la topologia de la red creada
	}
	
	private static void crearRouter(int id) {
		Router r = new Router(id);
		routers.put(id, r);
	}

	public static void crearLink(int id, int costo, int id1, int id2){
		Router r1 = routers.get(id1);
		Router r2 = routers.get(id2);
		Link l = new Link(id, costo, r1, r2);//la declaracion de este link es la representacion de que están conectados
		links.add(l);
	}
	
	public static void crearRed(int idRouter, String red) {
		Router r = routers.get(idRouter);
		r.addRuta(red, LOCAL, 0);
	}
}

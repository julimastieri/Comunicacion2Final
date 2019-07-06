package distanceVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class main {
	
	static final Link LOCAL = new Link(0, 0, null, null);
	static ArrayList<Link> links = new ArrayList<Link>();
	static HashMap<Integer,Router> routers = new HashMap<Integer,Router>();
	static boolean stop = false;
	static int tiempo=0;
	
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
		crearLink(2,3,2,3);
		crearLink(3,6,1,4);
		crearLink(4,3,3,4);
		
		//agrego los adyacentes
		Router r;
		System.out.println("Tiempo: " + tiempo);
		for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			r.agregarAdyacente(links);
			r.actualizarTabla();
			r.imprimirTabla();
		}
		
		System.out.println("\n \n");
		 
		while (tiempo < 180){ //converge en t = 90
			
			tiempo+=30;
			System.out.println("Tiempo: " + tiempo);
			
			
			//Caida de un link  (ver como lo hacemos bien)
			if (tiempo == 120) {
				System.out.println("CAE EL LINK: ");
				links.get(0).deshabilitarLink(); //cae el link 1. pos (id-1) en la lista
				
			
				for ( Entry<Integer, Router> entry : routers.entrySet() ){
					r = entry.getValue();
					r.chequearCaidaLink(1); //le mando el id del link q se cayo
					r.actualizarTabla();
				}
				/*
				 * hice dos veces el mismo for porque en uno se dan cuenta que se cayó el link
				 * y en el otro se hace el intercambio del trigger update
				 */
				
				//trigger update
				for ( Entry<Integer, Router> entry : routers.entrySet() ){
					r = entry.getValue();
					r.intercambiarRutas(r.getTablaTrigger());
					r.actualizarTabla();
					r.imprimirTabla();
				}
				
			}
			
			
			//intercambio correspondiente a los 30 segundos
			
			for ( Entry<Integer, Router> entry : routers.entrySet() ){
				r = entry.getValue();
				r.intercambiarRutas(r.getTabla());
			}
			
			for ( Entry<Integer, Router> entry : routers.entrySet() ){
				r = entry.getValue();
				r.actualizarTabla();
				r.imprimirTabla();
			}
			System.out.println("\n \n");
		}
		
		
		
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

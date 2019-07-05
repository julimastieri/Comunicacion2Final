package distanceVector;

import java.util.ArrayList;

public class main {
	
	static final Link LOCAL = new Link(0, 0, null, null);
	static ArrayList<Link> links = new ArrayList<Link>();
	
	public static void main (String[] arg) {
	
		//cargar routers de un txt o manualmente por consola
		Router r1 = new Router(1);
		Router r2 = new Router(2);
		Router r3 = new Router(3);
		Router r4 = new Router(4);
		
		//asigno las redes de cada uno
		r1.addRuta("2001:100A::/64", LOCAL, 0);
		r2.addRuta("2001:100B::/64", LOCAL, 0);
		r3.addRuta("2001:100C::/64", LOCAL, 0);
		r4.addRuta("2001:100D::/64", LOCAL, 0);
		
		//conecto los routers
		crearLink(1,1,r1,r2);
		crearLink(1,1,r1,r2);
		crearLink(2,1,r1,r3);
		crearLink(3,1,r2,r4);
		crearLink(4,1,r3,r4);
		
		//agrego los adyacentes
		r1.agregarAdyacente (links);
		r2.agregarAdyacente (links);
		r3.agregarAdyacente (links);
		r4.agregarAdyacente (links);
		
		
		r1.addRuta("2001:100B::/64", l1, 1); //esto se hace cdo se quiere agregar una nueva ruta
		r2.addRuta("2001:100A::/64", l1, 1);
		
		
		r1.addRuta("2001:100C::/64", l2, 1);
		r3.addRuta("2001:100A::/64", l2, 1);
		
		//falta hacer como hacen los routers para descubrrir nevas rutas
		
		//guardar si se quiere la topologia de la red creada
	}
	
	public static void crearLink(int id, int costo, Router r1, Router r2){
		Link l = new Link(id, costo, r1, r2);//la declaracion de este link es la representacion de que están conectados
		links.add(l);
	}
}

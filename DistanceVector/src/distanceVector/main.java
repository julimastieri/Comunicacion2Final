package distanceVector;

public class main {
	
	static final Link LOCAL = new Link(0, 0, null, null);
	//ver si tener una lista con los links de la red
	
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
		Link l1 = new Link (1,1,r1,r2); //la declaracion de este link es la representacion de que están conectados
		Link l2 = new Link(2,1,r1,r3);
		Link l3 = new Link(3,1,r2,r4);
		Link l4 = new Link(4,1,r3,r4);
		
		
		r1.addRuta("2001:100B::/64", l1, 1); //esto se hace cdo se quiere agregar una nueva ruta
		r2.addRuta("2001:100A::/64", l1, 1);
		
		
		r1.addRuta("2001:100C::/64", l2, 1);
		r3.addRuta("2001:100A::/64", l2, 1);
		
		//falta hacer como hacen los routers para descubrrir nevas rutas
		
		//guardar si se quiere la topologia de la red creada
		
		
	}
}

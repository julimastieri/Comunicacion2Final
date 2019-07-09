package distanceVector;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class main {
	
	static final Link LOCAL = new Link(0, 0, null, null);
	static ArrayList<Link> links = new ArrayList<Link>();
	static HashMap<Integer,Router> routers = new HashMap<Integer,Router>();
	static int tiempo=0;
	static ArrayList<TiempoCaida> linksCaidos = new ArrayList<TiempoCaida>();
	
	public static void main (String[] arg) {
		
		int eleccion = 0;
		while (eleccion != 7) {
			System.out.println("Algoritmo Distance Vector");
			System.out.println("1- Ingresar topologia. ");
			System.out.println("2- Cargar topologia guardada. ");
			System.out.println("3- Mostrar tablas hasta que converge. ");
			System.out.println("4- Agregar caida de un link.");
			System.out.println("5- Guardar topologia. ");
			System.out.println("6- Mostrar topologia. ");
			System.out.println("7- Salir. " + "\n");
			
			Scanner reader = new Scanner(System.in);
			eleccion = reader.nextInt();
			
			int tiempoConverge;
			
			if (eleccion == 1) {
				ingresarTopologia();
				
			}else if (eleccion == 2) {
				
				try {cargarTopologia();
				}catch (IOException e) { e.printStackTrace();}
				
			}else if (eleccion == 3) {
				if (!(routers.isEmpty())) {
					agregarAdyacentes();
					System.out.println("\n \n");
					tiempoConverge = calcularConvergencia();
					System.out.println("Converge a los "+ tiempoConverge+ " segundos.\n");
				}
				else 
					System.out.println("No hay routers en la topologia \n");
				
			}else if ( (eleccion == 4)){
				
				if (!(links.isEmpty())) {
					int id, tiempoCaida;
					eleccion = 1;
					TiempoCaida tC;
					ComparadorArrayList comparador = new ComparadorArrayList();
					
					//cargo todos los links que quiero que se caigan
					while (eleccion == 1) {
						
						//pido link
						listarLinks(); //listo links en pantalla
						System.out.println("Ingrese el id del link que se cae:");
						id = reader.nextInt();
						while ( !(existeLink(id)) ){
							System.out.println("Link inexistente, ingrese link valido");
							listarLinks();
							id = reader.nextInt();
						}
						
						//pido tiempo
						System.out.println("Ingrese el tiempo en el que se cae el link: ");
						tiempoCaida = reader.nextInt();
						while (tiempoCaida<0) {
							System.out.println("Tiempo invalido, ingrese tiempo positivo: ");
							tiempoCaida=reader.nextInt();
						}
						
						//agrego link
						tC = new TiempoCaida(tiempoCaida, id);
						linksCaidos.add(tC);
						
						System.out.println("1. Ingresar otra caida de link. ");
						System.out.println("2. Terminar. \n");
						eleccion = reader.nextInt();
					}
					
					//ordeno por tiempo la lista (de menor a mayor)
					Collections.sort(linksCaidos, comparador);
				}
				else
					System.out.println("No hay links en la topologia.\n ");
				
			}else if (eleccion == 5){
				try {guardarTopologia();}
				catch (IOException e) {e.printStackTrace();}
				}
				else if (eleccion == 6) {
					listarRouters();
					listarLinks();
					listarRedesLocales();
					listarCaidaDeLinks();
					System.out.println();
				}
		}
		System.out.println("Programa finalizado.");
		
	}
	
	private static boolean existeLink(int idLink) {
		int i=0;
		int idAct;
		
		while ( i<links.size() )  {
			idAct = links.get(i).getId();
			i++;
			if (idAct == idLink)
				return true;
		}
		return false;
	}
	
	private static void listarLinks() {
		System.out.print("Lista de Links: ");
		for (int i = 0; i < links.size(); i++) {
			System.out.print(links.get(i).getId()+ " ");
		}
		if (links.isEmpty()) 
			System.out.println("No hay links en la topologia");
		else
			System.out.println("");
		
	}
	
	private static void agregarAdyacentes() {
		Router r;
		for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			r.agregarAdyacente(links);
		}
	}
	
	private static void ingresarTopologia() {
		
		int eleccion = 0;
		
		while (eleccion != 4) {

			System.out.println("Ingreso de topologia: " );
			System.out.println("1- Cargar un nuevo router. " );
			System.out.println("2- Cargar red de un router." );
			System.out.println("3- Crear un nuevo link. " );
			System.out.println("4- Volver al menu principal. \n" );
			
			int id, id1, id2, costo;
			String red;
			Scanner reader = new Scanner(System.in);
			Scanner reader2 = new Scanner(System.in);
			eleccion = reader.nextInt();
			int seguir = 1;
			
			if (eleccion == 1) {
				seguir = 1;
				while (seguir == 1) {
					listarRouters();
					System.out.println("Ingrese el id del router: ");
					id = reader.nextInt();
					
					while ( (existeRouter(id)) || (!(id>0)) ) {
						if (!(id>0)) 
							System.out.println("El id del router tiene que ser mayor a cero, reingrese un id:");
						else
							System.out.println("El id ingresado ya esta asociado a otro router, reingrese un id:");
						id = reader.nextInt();
					}
					
					crearRouter(id);
					
					System.out.println("Router " + id +" creado correctamente." + "\n");
					System.out.println("1. Ingresar otro router. ");
					System.out.println("2. Volver al menu. \n");
					seguir = reader.nextInt();
				}
				
			}else if (eleccion == 2) {
				seguir = 1;
				while (seguir == 1) {
					if ( !(routers.isEmpty()) ){
						System.out.println("\n");
						listarRouters();
						System.out.println("Ingrese el id del router conectado a esa red:");
						id = reader.nextInt();
						while (!existeRouter(id)) {
							System.out.println("Router inexistente, ingrese uno de la lista");
							id = reader.nextInt();
						}
						
						System.out.println("Ingrese nombre de la red: ");
						red = reader2.nextLine();
						crearRed(id, red);
						System.out.println("Red " + red +" creada correctamente." + "\n");
						System.out.println("1. Ingresar otra red. " );
						System.out.println("2. Volver al menu. \n" );
						seguir = reader.nextInt();
					}
					else
						System.out.println("No hay routers cargados en la topologia, ingrese uno y reintente");
					
				}	
			}else if (eleccion == 3){
				
				seguir = 1;
				while (seguir == 1) {
					
					if (routers.size()<2) {
						listarLinks();
						System.out.println("\nIngrese el numero del link: ");
						id = reader.nextInt();
						while ( (existeLink(id)) || (!(id>0)) ) {
							if ( !(id>0) )
								System.out.println("El id de un link tiene que ser mayor a cero");
							else
								System.out.println("Id de link repetido, ingrese otro: " );
							id = reader.nextInt();
						}
						
						System.out.println("Ingrese el costo del link: " );
						costo = reader.nextInt();
						while ( !(costo>0) ){
							System.out.println("El costo debe ser mayor a 0, reingrese un costo valido: ");
							costo = reader.nextInt();
						}
						
						listarRouters();
						System.out.println("Ingrese el id de uno de los routers de los extremos: " );
						id1 = reader.nextInt();
						while (!existeRouter(id1)) {
							System.out.println("El router no existe, ingrese uno de la lista");
							id1 = reader.nextInt();
						}
						
						listarRouters();
						System.out.println("Ingrese el id del router del otro extremo: ");
						id2 = reader.nextInt();
						while ( (!existeRouter(id2)) || (id1==id2) ) {
							if (id1==id2)
								System.out.println("El router coincide con el que se ingresado primero");
							else
								System.out.println("El router no existe, ingrese uno de la lista");
							
							id2 = reader.nextInt();
						}
						
						crearLink(id, costo, id1, id2);
						
						System.out.println("Link " + id +" creado correctamente." + "\n");
						System.out.println("1. Ingresar otro link. ");
						System.out.println("2. Volver al menu. \n");
						seguir = reader.nextInt();
					}
					else
						System.out.println("La cantidad de routers exisentes es menor a dos, agregue los necesarios y reintente");
				}	
			}
		}
		
	}
	
	public static boolean existeRouter(int idRouter) {
		return routers.containsKey(idRouter);
	}
	
	public static void listarRouters() {
		Router r;
		System.out.print("Lista de routers: ");
		for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			System.out.print(r.getId()+" ");
		}
		if (routers.isEmpty())
			System.out.println("No hay routers cargados");
		else
			System.out.println("");
		
	}
	
	public static void listarRedesLocales() {
		Router r;
		System.out.println("Redes: ");
		 StringBuilder redesLocales =new StringBuilder();
	        for ( Entry<Integer, Router> entry : routers.entrySet() ){
				r = entry.getValue();
				redesLocales.append(r.mostrarRedesLocales());
			}
	     if (redesLocales.toString().isEmpty())
	    	 System.out.println("No hay redes cargadas");
	     else
	    	 System.out.println(redesLocales.toString());
	}
	
	public static void listarCaidaDeLinks() {
		
		System.out.println("Lista de links caidos:");
		for (int j = 0; j < linksCaidos.size(); j++) {
			System.out.println("Link: " +  linksCaidos.get(j).getIdLinkCaido() + " con tiempo: " +linksCaidos.get(j).getTiempo());
		}
		
		if (linksCaidos.isEmpty())
			System.out.println("No se cae ningun link");
	}
	
	private static void caidaLink(int idLink, int tiempoCaida) {
		Router r;
		
		System.out.println("Cae el link: " + idLink + " en el tiempo " + tiempoCaida +"\n");
		links.get(getPosLink(idLink)).deshabilitarLink(); //se desabilita el link con id dado en la lista de links
		
		for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			//le mando el id del link para actualizar los costos a inf y realizar un intercambio correspondiente al trigger update
			r.chequearCaidaLink(idLink); 
		}
		
		for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			r.actualizarTabla();
			r.imprimirTabla();
		}
		System.out.println("\n");
	}
	
	private static int getPosLink(int idLink) {
		int i=0;
		int idAct;
		
		while ( i<links.size() )  {
			idAct = links.get(i).getId();
			if (idAct == idLink)
				return i;
			i++;
		}
		return -1;
	}
	
	public static int calcularConvergencia() {
		
		Router r;
		boolean converge = false;
		int i=0;
		int tCaida = tiempo;
		boolean caido;
		boolean c;
		
		while  ( (!converge) || (i<linksCaidos.size()) ){
			System.out.println("Tiempo: " + tiempo);
			caido = false;
		
			//caida links con tCaida=tiempo
			while ( (tCaida == tiempo) && (i < linksCaidos.size()) ) {
				tCaida = linksCaidos.get(i).getTiempo();
				if (tCaida == tiempo) {
					caidaLink(linksCaidos.get(i).getIdLinkCaido(), tCaida);
					i++;
					converge = false;
					caido = true;
				}
			}
			
			//intercambio correspondiente a los 30 segundos
			
			if ((tiempo != 0) && (!caido))
				for ( Entry<Integer, Router> entry : routers.entrySet() ){
					r = entry.getValue();
					r.intercambiarRutas(r.getTabla());
				}
			
			
			if (!caido) {
				converge = true; 
				for ( Entry<Integer, Router> entry : routers.entrySet() ){
					r = entry.getValue();
					c = r.actualizarTabla();
					r.imprimirTabla();
					if (!c)
						converge = false;
				}
				System.out.println("\n");
			}
			
			//Caida de un link (veo si corresponde que se caiga un link)
			while ( (tiempo < tCaida) && (tCaida < tiempo+30) && (i < linksCaidos.size()) ) {
				tCaida = linksCaidos.get(i).getTiempo();
				if ( (tiempo < tCaida) && (tCaida < tiempo+30) ) {
					caidaLink(linksCaidos.get(i).getIdLinkCaido(), tCaida);
					converge = false;
					i++;
				}
			}
			
			tiempo+=30;	
		}
		int tiempoConvergencia=tiempo-30;
		//reseteo estructuras por si se vuelve a usar 
		tiempo = 0;
		//resetear roouters
		for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			r.reset();
		}
		return tiempoConvergencia;
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
	
	public static void cargarTopologia() throws IOException {
	
		routers.clear();
		links.clear();
		linksCaidos.clear();
		
		File miDir = new File (".");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(miDir);
        
        fileChooser.showOpenDialog(fileChooser);
        String ruta = fileChooser.getSelectedFile().getAbsolutePath();
    	File file = new File(ruta); 
    	FileReader fr = new FileReader (file);
		BufferedReader br = new BufferedReader(fr);
		
		//leo routers
		String linea=" ";
		while (!linea.isEmpty()) {
			linea = br.readLine();
			if (!linea.isEmpty()) {
				crearRouter(Integer.parseUnsignedInt(linea));
			}	
		}
		
		//leo links
		int id=-1;
		int costo=-1;
		int id1=-1;
		int id2=-1;
		linea = br.readLine();
		while (!linea.isEmpty()) {
			
			if (!linea.isEmpty()) {
				id = Integer.parseInt(linea);
			}
			
			
			linea = br.readLine();
			if (!linea.isEmpty()) {
				costo = Integer.parseInt(linea);
			}
			
			linea = br.readLine();
			if (!linea.isEmpty()) {
				id1 = Integer.parseInt(linea);
			}
			
			linea = br.readLine();
			if (!linea.isEmpty()) {
				id2 = Integer.parseInt(linea);
			}
			
			crearLink(id, costo, id1, id2);
			linea = br.readLine();
		}
		
		//leo redes
		linea = br.readLine();
		while ( (linea != null) && (!(linea.equals(""))) ) {
		
			if (!linea.isEmpty() || (linea!=null)) {
				id = Integer.parseInt(linea);
			}
			
			linea = br.readLine();
			
			crearRed(id, linea);
			linea = br.readLine();
		}
		
		fr.close();
		br.close();
	}
	
	public static void guardarTopologia() throws IOException {
		File miDir = new File (".");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(miDir);
        
        fileChooser.showSaveDialog(fileChooser);
        String ruta = fileChooser.getSelectedFile().getAbsolutePath();
        File file = new File(ruta);
        FileWriter fw = new FileWriter(file);
        
        // guardar routers (id)
        Router r;
        for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			fw.write(r.getId()+"\n");
		}
        fw.write("\n");
        
        //guardar links (un dato por linea)

        Link laux;
        for (int i=0; i<links.size(); i++) {
        	laux = new Link (links.get(i));
        	fw.write(laux.getId()+ "\n");
        	fw.write(laux.getCosto()+ "\n");
        	fw.write(laux.getR1().getId()+ "\n");
        	fw.write(laux.getR2().getId()+ "\n");
        }
        fw.write("\n");
        
        //guardar redes int idRouter, String red
        StringBuilder redesLocales =new StringBuilder();
        for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			redesLocales.append(r.getRedesLocales());
		}
        fw.write(redesLocales.toString());

        fw.close();
	}
}

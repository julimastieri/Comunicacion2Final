package distanceVector;

import java.awt.Menu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class main {
	
	static final Link LOCAL = new Link(0, 0, null, null);
	static ArrayList<Link> links = new ArrayList<Link>();
	static HashMap<Integer,Router> routers = new HashMap<Integer,Router>();
	//static boolean stop = false;
	static int tiempo=0;
	
	public static void main (String[] arg) {
		
		menu();
		
		/*
		//topologia de prefi 2018
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
		
		*/

		 
	}
	
	
	public static void menu () {
		
		//topologia de prefi 2018
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
				
		
		int eleccion = 0;
		while (eleccion != 6) {
			System.out.println("Algoritmo Distance Vector" + "\n");
			System.out.println("1- Ingresar topologia. " + "\n");
			System.out.println("2- Cargar topologia guardada. " + "\n");
			System.out.println("3- Mostrar tablas hasta que converge. " + "\n");
			System.out.println("4- Agregar caida de un link." + "\n");
			System.out.println("5- Guardar topologia. " + "\n");
			System.out.println("6- Salir. " + "\n");
			
			Scanner reader = new Scanner(System.in);
			eleccion = reader.nextInt();
			int id, tiempoCaida;
			int tiempoConverge;
			
			if (eleccion == 1) {
				ingresarTopologia();
				
			}else if (eleccion == 2) {
				
				try {cargarTopologia();
				}catch (IOException e) { e.printStackTrace();}
				
			}else if (eleccion == 3) {	
				agregarAdyacentes();
				System.out.println("\n \n");
				tiempoConverge = calcularConvergencia(-1,-1);
				System.out.println("Converge a los "+ tiempoConverge+ " segundos.");
				
			}else if (eleccion == 4) {
				
				System.out.println("Ingrese el id del link que se cae:" + " \n");
				id = reader.nextInt();
				System.out.println("Ingrese el tiempo en el que se cae el link: " + "\n");
				tiempoCaida = reader.nextInt();
				if (tiempo != 0) {
					tiempo-=30;//arranca desde el t que convergió
				}else { 
					agregarAdyacentes(); //arranca desde 0
				}
				tiempoConverge = calcularConvergencia(id, tiempoCaida);
				System.out.println("Converge a los "+ tiempoConverge+ " segundos.");
				
			}else if (eleccion == 5){
				try {guardarTopologia();}
				catch (IOException e) {e.printStackTrace();}
			}	
		}
		System.out.println("Programa finalizado.");
		
	}
	
	private static void agregarAdyacentes() {
		Router r;
		System.out.println("Tiempo: " + tiempo);
		for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			r.agregarAdyacente(links);
			r.actualizarTabla();
			r.imprimirTabla(); //tiempo 0
		}
		tiempo+=30; //arranca desde el t 30
	}
	
	private static void ingresarTopologia() {
		
		int eleccion = 0;
		
		while (eleccion != 4) {

			System.out.println("Ingreso de topologia: " );
			System.out.println("1- Cargar un nuevo router. " );
			System.out.println("2- Cargar red de un router." );
			System.out.println("3- Crear un nuevo link. " );
			System.out.println("4- Volver al menu principal. " );
			
			int id, id1, id2, costo;
			String red;
			Scanner reader = new Scanner(System.in);
			Scanner reader2 = new Scanner(System.in);
			Scanner reader3 = new Scanner(System.in);
			Scanner reader4 = new Scanner(System.in);
			eleccion = reader.nextInt();
			int seguir = 1;
			
			if (eleccion == 1) {
				seguir = 1;
				while (seguir == 1) {
					System.out.println("\n");
					System.out.println("Ingrese el id del router: ");
					id = reader.nextInt();
					crearRouter(id);
					System.out.println("Router " + id +" creado correctamente." + "\n");
					System.out.println("1. Ingresar otro router. ");
					System.out.println("2. Volver al menu. ");
					seguir = reader.nextInt();
				}
				
			}else if (eleccion == 2) {
				seguir = 1;
				while (seguir == 1) {
					System.out.println("\n");
					System.out.println("Ingrese el id del router:");
					id = reader.nextInt();
					System.out.println("\n");
					System.out.println("Ingrese la red: ");
					red = reader2.nextLine();
					crearRed(id, red);
					System.out.println("Red " + red +" creada correctamente." + "\n");
					System.out.println("1. Ingresar otra red. " );
					System.out.println("2. Volver al menu. " );
					seguir = reader.nextInt();
				}	
			}else if (eleccion == 3){
				
				seguir = 1;
				while (seguir == 1) {
					System.out.println("\n");
					System.out.println("Ingrese el numero del link: ");
					id = reader.nextInt();
					System.out.println("\n");
					System.out.println("Ingrese el costo del link: " );
					costo = reader2.nextInt();
					System.out.println("\n");
					System.out.println("Ingrese el id de uno de los routers de los extremos: " );
					id1 = reader3.nextInt();
					System.out.println("\n");
					System.out.println("Ingrese el id del router del otro extremo: ");
					id2 = reader4.nextInt();
					crearLink(id, costo, id1, id2);
					
					System.out.println("Link " + id +" creado correctamente." + "\n");
					System.out.println("1. Ingresar otro link. ");
					System.out.println("2. Volver al menu. ");
					seguir = reader.nextInt();
				}	
			}
		}
		
	}
	
	private static void caidaLink(int idLink, int tiempoCaida) {
		Router r;
		
		System.out.println("Cae el link: " + idLink + "\n");
		System.out.println("Tiempo: "+ tiempoCaida);
		links.get(idLink-1).deshabilitarLink(); //cae el link 1. pos (id-1) en la lista
		
		for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			r.chequearCaidaLink(idLink); //le mando el id del link q se cayo
		}
		
		//trigger update
		for ( Entry<Integer, Router> entry : routers.entrySet() ){
			r = entry.getValue();
			r.actualizarTabla();
			r.imprimirTabla();
		}
	}
	
	public static int calcularConvergencia(int idLink, int tiempoCaida) {
		
		Router r;
		boolean converge = false;
		boolean c;
		
		
		while (!converge){ 
			
			System.out.println("Tiempo: " + tiempo);
			
			//intercambio correspondiente a los 30 segundos
			
			for ( Entry<Integer, Router> entry : routers.entrySet() ){
				r = entry.getValue();
				r.intercambiarRutas(r.getTabla());
			}
			
			converge = true;
			for ( Entry<Integer, Router> entry : routers.entrySet() ){
				r = entry.getValue();
				c = r.actualizarTabla();
				r.imprimirTabla();
				if (!c) {
					converge = false;}
			}
			System.out.println("\n \n");
			
			
			//Caida de un link  
			if ((tiempo < tiempoCaida) && (tiempoCaida < tiempo+30)) {
				caidaLink(idLink, tiempoCaida);
				converge = false; //para que haga el siguiente intercambio
				System.out.println("\n");
			}
			
			tiempo+=30;	
		}
		
		return tiempo-30;
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
        
        fileChooser.showOpenDialog(fileChooser);
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

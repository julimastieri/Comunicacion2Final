package distanceVector;

import java.util.Comparator;
public class ComparadorArrayList implements Comparator<TiempoCaida> {
		
		public int compare(TiempoCaida t1, TiempoCaida t2) { 
		   return new Integer(t1.getTiempo()).compareTo(new Integer (t2.getTiempo()));
		} 
}

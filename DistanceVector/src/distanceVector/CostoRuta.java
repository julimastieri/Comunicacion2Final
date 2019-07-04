package distanceVector;

public class CostoRuta {
	Link link;
	int costo;
	
	public CostoRuta(Link l, int costo){
		link = l;
		this.costo = costo;
	}
	
	public Link getLink() {
		return this.link;
	}
	
	public int getCosto() {
		return this.costo;
	}
}

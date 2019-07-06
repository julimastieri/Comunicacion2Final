package distanceVector;

public class Link {
	int id;
	int costo;
	Router r1;
	Router r2;
	boolean activo;
	
	public Link (int id, int costo, Router r1, Router r2) {
		this.id = id;
		this.costo = costo; //por convencion 1
		this.r1 = r1;
		this.r2 = r2;
		activo = true;
	}
	
	public Link (Link l) {
		id=l.getId();
		costo=l.getCosto();
		r1=l.getR1();
		r2=l.getR2();
		activo = true;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getCosto() {
		return this.costo;
	}
	
	public Router getR1(){
		return this.r1;
	}
	
	public Router getR2() {
		return this.r2;
	}
	
	public void deshabilitarLink() {
		activo = false;
	}
	
	public void habilitarLink() {
		activo = true;
	}
	
	public boolean isActivo() {
		return activo;
	}
	
}

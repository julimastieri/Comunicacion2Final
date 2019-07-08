package distanceVector;

public class TiempoCaida extends Object{
		int tiempo;
		int idLinkCaido;
		
		public TiempoCaida(int tiempo, int idLinkCaido) {
			this.tiempo=tiempo;
			this.idLinkCaido=idLinkCaido;
		}
		
		public int getTiempo() {
			return tiempo;
		}
		
		public int getIdLinkCaido() {
			return idLinkCaido;
		}
}

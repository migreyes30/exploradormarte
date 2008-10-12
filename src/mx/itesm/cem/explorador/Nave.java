package mx.itesm.cem.explorador;

public class Nave {
	private Posicion posicion;
	private String id;
	private int piedras;

	public Nave(String id, Posicion pos){
		this.setId(id);
		this.setPosicion(pos);
		this.setPiedras(0);
	}
	public Posicion getPosicion() {
		return posicion;
	}
	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public synchronized int getPiedras() {
		return piedras;
	}
	public synchronized void setPiedras(int piedras) {
		this.piedras = piedras;
	}
}

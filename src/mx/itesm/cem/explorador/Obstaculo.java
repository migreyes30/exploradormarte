package mx.itesm.cem.explorador;

public class Obstaculo {

	private Posicion posicion;
	private String id;

	public Obstaculo(String id, Posicion pos){
		this.setId(id);
		this.setPosicion(pos);
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
	
}

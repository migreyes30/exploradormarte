package mx.itesm.cem.explorador;



public class Monticulo {
	private Posicion posicion;
	private String id;
	int piedras;
	
	public Monticulo(String id, Posicion pos){
		this.setId(id);
		this.setPosicion(pos);
		this.setPiedras((int)(Math.random()*20));
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
	
	public int getPiedras() {
		return piedras;
	}
	public void setPiedras(int piedras) {
		this.piedras = piedras;
	}

}

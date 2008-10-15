package mx.itesm.cem.explorador;

import java.util.ArrayList;

public class Morona {

	private String id;
	private Posicion posicion;
	private ArrayList<String> caminos;
	
	
	
	public Morona(Posicion posicion) {
		this.id = "H" + (int)Math.random()*65536;
		this.posicion = posicion;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Posicion getPosicion() {
		return posicion;
	}
	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}
	public ArrayList<String> getCaminos() {
		return caminos;
	}
	public void setCaminos(ArrayList<String> caminos) {
		this.caminos = caminos;
	}

}

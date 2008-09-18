package mx.itesm.cem.explorador;

public class Obstaculo {

	private Posicion posicion;
	private String imagen;

	public Obstaculo(Posicion posicion){
		this.posicion = posicion;
	}
	public Posicion getPosicion() {
		return posicion;
	}
	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
}

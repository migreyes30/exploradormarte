package mx.itesm.cem.explorador;

/**
 * 
 * Clase que describe el resultado de cada
 * intento que hace un agente por caminar
 * para asi conocer a donde intento moverse,
 * si tuvo exito o no, y que cosa estaba
 * en la casilla a la que intento moverse
 *
 */

public class ResultadoCaminar {

	private Posicion posicion;
	private boolean exito;
	private String ocupacion;

	public Posicion getPosicion() {
		return posicion;
	}
	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}
	public boolean isExito() {
		return exito;
	}
	public void setExito(boolean exito) {
		this.exito = exito;
	}
	public String getOcupacion() {
		return ocupacion;
	}
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

}

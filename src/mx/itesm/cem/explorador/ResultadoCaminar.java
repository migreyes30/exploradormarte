package mx.itesm.cem.explorador;

public class ResultadoCaminar {

	private Posicion posicion;
	private boolean exito;
	private Object ocupacion;

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
	public Object getOcupacion() {
		return ocupacion;
	}
	public void setOcupacion(Object ocupacion) {
		this.ocupacion = ocupacion;
	}

}

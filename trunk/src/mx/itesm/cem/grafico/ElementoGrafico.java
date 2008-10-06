package mx.itesm.cem.grafico;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import mx.itesm.cem.explorador.Posicion;

@SuppressWarnings("serial")
public class ElementoGrafico extends JLabel{
	
	protected Posicion posicion;
	protected String id;
	protected ImageIcon imagen;
	
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

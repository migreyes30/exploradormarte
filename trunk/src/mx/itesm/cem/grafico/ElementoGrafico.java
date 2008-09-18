package mx.itesm.cem.grafico;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import mx.itesm.cem.explorador.Posicion;

public class ElementoGrafico extends JLabel{
	
	protected Posicion posicion;
	protected int id;
	protected ImageIcon imagen;
	
	public Posicion getPosicion() {
		return posicion;
	}

	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

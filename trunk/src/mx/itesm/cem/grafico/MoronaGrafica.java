package mx.itesm.cem.grafico;

import javax.swing.ImageIcon;

import mx.itesm.cem.explorador.Posicion;

@SuppressWarnings("serial")
public class MoronaGrafica extends ElementoGrafico {
	
	public MoronaGrafica(String id, Posicion pos){
		this.id = id;
		this.setPosicion(pos);
		this.imagen = new ImageIcon(getClass().getResource("hueso.png"));
		this.setIcon(imagen);		
	}
	
}

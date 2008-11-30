package mx.itesm.cem.grafico;

import javax.swing.ImageIcon;

import mx.itesm.cem.explorador.Posicion;

@SuppressWarnings("serial")
public class ObstaculoGrafico extends ElementoGrafico {
	
	public ObstaculoGrafico(String id, Posicion pos){
		this.id = id;
		this.posicion = pos;
		this.imagen = new ImageIcon(getClass().getResource("obstaculo.png"));
		this.setIcon(imagen);		
	}
	
}

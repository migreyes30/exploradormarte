package mx.itesm.cem.grafico;

import javax.swing.ImageIcon;

import mx.itesm.cem.explorador.Posicion;

@SuppressWarnings("serial")
public class MonticuloGrafico extends ElementoGrafico{
	
	public MonticuloGrafico(String id, Posicion pos){
		this.id = id;
		this.posicion = pos;
		this.imagen = new ImageIcon(getClass().getResource("monticulo.png"));
		this.setIcon(imagen);		
	}
	
}

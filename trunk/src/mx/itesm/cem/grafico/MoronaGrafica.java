package mx.itesm.cem.grafico;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import mx.itesm.cem.explorador.Posicion;

@SuppressWarnings("serial")
public class MoronaGrafica extends ElementoGrafico implements ActionListener {
	
	public MoronaGrafica(String id, Posicion pos){
		this.id = id;
		this.setPosicion(pos);
		this.imagen = new ImageIcon(getClass().getResource("hueso.png"));
		this.setIcon(imagen);		
	}
	public void actionPerformed(ActionEvent ae) {
		
	}
}

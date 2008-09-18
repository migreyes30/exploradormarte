package mx.itesm.cem.grafico;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import mx.itesm.cem.explorador.Posicion;

public class ObstaculoGrafico extends ElementoGrafico implements ActionListener {
	
	public ObstaculoGrafico(int id, Posicion pos){
		this.id = id;
		this.posicion = pos;
		this.imagen = new ImageIcon(getClass().getResource("obstaculo.png"));
		this.setIcon(imagen);		
	}
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
	}
}

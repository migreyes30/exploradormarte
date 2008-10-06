package mx.itesm.cem.grafico;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import mx.itesm.cem.explorador.Posicion;

@SuppressWarnings("serial")
public class MonticuloGrafico extends ElementoGrafico implements ActionListener {
	
	public MonticuloGrafico(String id, Posicion pos){
		this.id = id;
		this.posicion = pos;
		this.imagen = new ImageIcon(getClass().getResource("monticulo.png"));
		this.setIcon(imagen);		
	}
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
	}
}

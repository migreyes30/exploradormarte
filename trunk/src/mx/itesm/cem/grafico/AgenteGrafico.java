package mx.itesm.cem.grafico;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import mx.itesm.cem.explorador.Posicion;

@SuppressWarnings("serial")
public class AgenteGrafico extends ElementoGrafico implements ActionListener {
	
	public AgenteGrafico(String id, Posicion pos){
		this.id = id;
		this.setPosicion(pos);
		this.imagen = new ImageIcon(getClass().getResource("dog.png"));
		this.setIcon(imagen);		
	}
	public void actionPerformed(ActionEvent ae) {
		
	}
}

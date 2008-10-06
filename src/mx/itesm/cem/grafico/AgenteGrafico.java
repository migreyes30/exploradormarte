package mx.itesm.cem.grafico;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class AgenteGrafico extends ElementoGrafico implements ActionListener {
	
	public AgenteGrafico(String id){
		this.id = id;
		this.setPosicion(TableroGrafico.naveGrafica.getPosicion());
		this.imagen = new ImageIcon(getClass().getResource("dog.png"));
		this.setIcon(imagen);		
	}
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
	}
}

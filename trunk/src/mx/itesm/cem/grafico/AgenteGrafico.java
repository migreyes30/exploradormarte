package mx.itesm.cem.grafico;

import javax.swing.ImageIcon;

import mx.itesm.cem.explorador.Agente;
import mx.itesm.cem.explorador.Posicion;
import mx.itesm.cem.explorador.Tablero;

@SuppressWarnings("serial")
public class AgenteGrafico extends ElementoGrafico {
	
	public AgenteGrafico(String id, Posicion pos){
		this.id = id;
		this.setPosicion(pos);
		Agente a = (Agente)Tablero.obtenerElementoConId(id);
		if(a.getCapacidad() == 0)
			this.imagen = new ImageIcon(getClass().getResource("explorador.png"));
		else
			this.imagen = new ImageIcon(getClass().getResource("cargador.png"));
		
		this.setIcon(imagen);		
	}
	
}

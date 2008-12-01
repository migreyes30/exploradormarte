package mx.itesm.cem.explorador;

public class AgenteEspecial extends Agente {

	public AgenteEspecial(String id, Posicion pos) {
		super(id, pos);
		super.setCapacidad(5000);				
	}

}

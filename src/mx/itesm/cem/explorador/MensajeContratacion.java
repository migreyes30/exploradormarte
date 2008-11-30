package mx.itesm.cem.explorador;

public class MensajeContratacion extends  Mensaje{

	private int premio;
	private int numPiedras;
	private Posicion posicionMonticulo;


	public MensajeContratacion(String sender, String receiver, int premio, int numPiedras, Posicion posicionMonticulo){
		super(sender, receiver);
		this.setPremio(premio);
		this.setNumPiedras(numPiedras);
		this.setPosicionMonticulo(posicionMonticulo);

	}


	public int getPremio() {
		return premio;
	}


	public void setPremio(int premio) {
		this.premio = premio;
	}


	public int getNumPiedras() {
		return numPiedras;
	}


	public void setNumPiedras(int numPiedras) {
		this.numPiedras = numPiedras;
	}


	public Posicion getPosicionMonticulo() {
		return posicionMonticulo;
	}


	public void setPosicionMonticulo(Posicion posicionMonticulo) {
		this.posicionMonticulo = posicionMonticulo;
	}

}

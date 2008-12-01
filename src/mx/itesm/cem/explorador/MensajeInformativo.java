package mx.itesm.cem.explorador;

public class MensajeInformativo extends Mensaje{

	private Posicion posicionMonticulo;
	private int numPiedras;
	private int premio;

	public MensajeInformativo(Posicion posicionMonticulo, String sender, int premio){
		super(sender, "broadcast");
		this.setPosicionMonticulo(posicionMonticulo);
		this.setNumPiedras(premio);
		this.setPremio(premio);
	}

	public Posicion getPosicionMonticulo() {
		return this.posicionMonticulo;
	}

	public void setPosicionMonticulo(Posicion posicionMonticulo) {
		this.posicionMonticulo = posicionMonticulo;
	}

	public int getPremio() {
		return this.premio;
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
}

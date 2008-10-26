package mx.itesm.cem.explorador;

public class MensajeInformativo {

	private Posicion posicionMonticulo;
	private String sender;

	public MensajeInformativo(Posicion posicionMonticulo, String sender){
		this.setPosicionMonticulo(posicionMonticulo);
		this.setSender(sender);
	}

	public Posicion getPosicionMonticulo() {
		return posicionMonticulo;
	}

	public void setPosicionMonticulo(Posicion posicionMonticulo) {
		this.posicionMonticulo = posicionMonticulo;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

}

package mx.itesm.cem.explorador;

public class MensajeAceptacion extends Mensaje{

	private boolean aceptarContrato;
	private String razon;

	public MensajeAceptacion(String sender, String receiver, boolean aceptarContrato, String razon){
		super(sender, receiver);
		this.setAceptarContrato(aceptarContrato);
		this.setRazon(razon);
	}

	public boolean isAceptarContrato() {
		return aceptarContrato;
	}

	public void setAceptarContrato(boolean aceptarContrato) {
		this.aceptarContrato = aceptarContrato;
	}

	public String getRazon() {
		return razon;
	}

	public void setRazon(String razon) {
		this.razon = razon;
	}

}

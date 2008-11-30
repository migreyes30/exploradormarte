package mx.itesm.cem.explorador;

public class Mensaje {

	private String sender;
	private String receiver;

	public Mensaje(String sender, String receiver){
		this.setSender(sender);
		this.setReceiver(receiver);
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}

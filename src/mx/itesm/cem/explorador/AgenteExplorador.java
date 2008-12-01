package mx.itesm.cem.explorador;

import java.util.ArrayList;


public class AgenteExplorador extends Agente {

	public ArrayList<MensajeAceptacion> buzonAceptacion;
	MensajeInformativo mensajeInformativo;
	private boolean haveToReSend;

	public AgenteExplorador(String id, Posicion posicion) {
		super(id, posicion);
		super.setCapacidad(0);
		this.setHaveToReSend(false);
		this.setBuzonAceptacion(new ArrayList<MensajeAceptacion>());
	}

/**
 * Metodo principal que controla el comportamiento del agente Explorador
 * recibe un arreglo con las capas, el indice de cada elemento
 * del arreglo representa el orden de la capa.
 *
 * @param capas Arreglo que contiene las capas en orden de importancia
 *
 */
	public synchronized void actuar(int[] capas){
		int i =0;
		boolean exito = false;
		while(!exito){
			if(i >= capas.length)
				i = 0;
			switch (capas[i]) {

			case 1:
				if (this.getResultado().getOcupacion().startsWith("O") || this.getResultado().getOcupacion().startsWith("A")) { 
					//Si la casilla a la que quieres moverte esta ocupada
					System.out.println("Ejecutando Capa 1");
					exito = this.evitarObstaculo();
				}
				break;

			case 2:
				if (this.getBuzonAceptacion().size() != 0 && this.getMensajeInformativo() != null) {  
					//Si intentaste moverte a una casilla que tiene un monticulo
					System.out.println("Ejecutando Capa 2");
					System.out.print(this.getId() + ": ");
					exito = this.contratar();
				}
				break;

			case 3:
				if(this.ishaveToReSend()){
					System.out.println("Ejecutando Capa 3");
					System.out.print(this.getId() + ": ");
					exito = this.informar(this.mensajeInformativo);
				
				}else if (this.getResultado().getOcupacion().startsWith("M") && this.getMensajeInformativo() == null) {
					//Si intentaste moverte a una casilla que tiene un monticulo
					System.out.println("Ejecutando Capa 3");
					Monticulo monticulo = (Monticulo)(Tablero.obtenerElementoConId(this.getResultado().getOcupacion()));
					System.out.print(this.getId() + ": ");
					exito = this.informar(monticulo);
				}
				break;

			case 4:
				System.out.println("Ejecutando Capa 4");
				System.out.println("Posicion " + this.getId() + ": " + this.getPosicion().getI() + ", " + this.getPosicion().getJ());
				exito = this.explorar();
				break;

			default:
				//System.out.println("Default");
				break;
				}
			i++; //De lo contrario, intentar con la siguiente capa

		}
	}

	public synchronized boolean contratar(){

		ArrayList<MensajeAceptacion> mensajesPositivos = new ArrayList<MensajeAceptacion>();
		for (MensajeAceptacion mensajeIterado : this.getBuzonAceptacion()) {
			if (mensajeIterado.isAceptarContrato()){
				mensajesPositivos.add(mensajeIterado);
			}
		}

		if(mensajesPositivos.size() == 1){
			// winner
			String idMonticulo = Tablero.matriz[this.getMensajeInformativo().getPosicionMonticulo().getI()][this.getMensajeInformativo().getPosicionMonticulo().getJ()];
			Monticulo monticulo = (Monticulo)Tablero.obtenerElementoConId(idMonticulo);
			monticulo.setContratoAbierto(false);
			MensajeContratacion mensajeContratacion = new MensajeContratacion(
														this.getId(), mensajesPositivos.get(0).getSender(),
														this.getMensajeInformativo().getPremio(), 
														this.getMensajeInformativo().getNumPiedras(),
														this.getMensajeInformativo().getPosicionMonticulo());
			AgenteCargador cargador = (AgenteCargador)Tablero.obtenerElementoConId(mensajesPositivos.get(0).getSender());
			cargador.getBuzonContratacion().add(mensajeContratacion);
			this.setMensajeInformativo(null);
			this.getBuzonAceptacion().clear();
			return true;
		}else if(mensajesPositivos.size() == 0) {
			//more reward
			this.getMensajeInformativo().setPremio(this.getMensajeInformativo().getPremio() + 2);
			this.setHaveToReSend(true);
			return false;
		}else {
			// if(mensajesPositivos.size() > 1 )
			String idMonticulo = Tablero.matriz[this.getMensajeInformativo().getPosicionMonticulo().getI()][this.getMensajeInformativo().getPosicionMonticulo().getJ()];
			Monticulo monticulo = (Monticulo)Tablero.obtenerElementoConId(idMonticulo);
			monticulo.setContratoAbierto(false);
			AgenteCargador cargador = (AgenteCargador)Tablero.obtenerElementoConId(mensajesPositivos.get(0).getSender());
			int capacidadMaxima= cargador.getCapacidad();
			int indexMensaje = 0;
			for (MensajeAceptacion mensajeIterado : mensajesPositivos) {
				cargador = (AgenteCargador)Tablero.obtenerElementoConId(mensajeIterado.getSender());
				if(capacidadMaxima < cargador.getCapacidad()){
					capacidadMaxima = cargador.getCapacidad();
					indexMensaje = mensajesPositivos.indexOf(mensajeIterado);
				}
			}

			MensajeContratacion mensajeContratacion = new MensajeContratacion(this.getId(), mensajesPositivos.get(indexMensaje).getSender(),
					this.getMensajeInformativo().getPremio(), this.getMensajeInformativo().getNumPiedras(),
					this.getMensajeInformativo().getPosicionMonticulo());
			cargador = (AgenteCargador)Tablero.obtenerElementoConId(mensajesPositivos.get(indexMensaje).getSender());
			cargador.buzonContratacion.add(mensajeContratacion);
			
			this.setMensajeInformativo(null);
			this.getBuzonAceptacion().clear();
			return true;
		}
	}

	public synchronized boolean informar(Monticulo monticulo){
		if(monticulo.isEnContrato()){
			return false;
		}
		MensajeInformativo mensajeInformativo = new MensajeInformativo(monticulo.getPosicion(), this.getId(), monticulo.getPiedras());
		this.setMensajeInformativo(mensajeInformativo);
		for (int i = 0; i < Tablero.listaAgentes.size(); i++) {
			if (Tablero.listaAgentes.get(i).capacidad != 0){
				AgenteCargador cargador =(AgenteCargador)Tablero.listaAgentes.get(i);
				cargador.getBuzonInformativo().add(mensajeInformativo);
			}
		}
		monticulo.setContratoAbierto(true);
		monticulo.setEnContrato(true);
		return true;
	}

	public synchronized  boolean informar(MensajeInformativo mensajeInformativo){
		boolean alreadyInMailbox = false;
		for (int i = 0; i < Tablero.listaAgentes.size(); i++) {
			if (Tablero.listaAgentes.get(i).getCapacidad() != 0){
				AgenteCargador cargador = (AgenteCargador)Tablero.listaAgentes.get(i);
				if(cargador.getBuzonInformativo().indexOf(this.getMensajeInformativo()) == -1){
					cargador.getBuzonInformativo().add(this.getMensajeInformativo());
					alreadyInMailbox = true;
				}
			}
		}
		this.setHaveToReSend(false);
		return alreadyInMailbox;
	}

	public ArrayList<MensajeAceptacion> getBuzonAceptacion() {
		return buzonAceptacion;
	}

	public void setBuzonAceptacion(ArrayList<MensajeAceptacion> buzonAceptacion) {
		this.buzonAceptacion = buzonAceptacion;
	}

	public MensajeInformativo getMensajeInformativo() {
		return mensajeInformativo;
	}

	public void setMensajeInformativo(MensajeInformativo mensajeInformativo) {
		this.mensajeInformativo = mensajeInformativo;
	}

	public boolean ishaveToReSend() {
		return haveToReSend;
	}

	public void setHaveToReSend(boolean haveToReSend) {
		this.haveToReSend = haveToReSend;
	}

}

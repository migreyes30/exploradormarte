package mx.itesm.cem.explorador;

import java.util.ArrayList;


public class AgenteExplorador extends Agente {

	public ArrayList<MensajeAceptacion> buzonAceptacion;
	MensajeInformativo mensajeInformativo;

	public AgenteExplorador(String id, Posicion posicion) {
		super(id, posicion);
		super.setCapacidad(0);
		buzonAceptacion = new ArrayList<MensajeAceptacion>();
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
				if (this.resultado.getOcupacion().startsWith("O")
						|| this.resultado.getOcupacion().startsWith("A")) { //Si la casilla a la que quieres moverte esta ocupada
					System.out.println("Ejecutando Capa 1");
					exito = this.evitarObstaculo();
				}
				break;

			case 2:
				if (this.buzonAceptacion.size() != 0 && this.mensajeInformativo != null) { //Si intentaste moverte a una casilla que tiene un monticulo
					System.out.println("Ejecutando Capa 2");
					System.out.print(this.getId() + ": ");
					exito = this.contratar();
				}
				break;

			case 3:
				if(this.mensajeInformativo != null){
					System.out.println("Ejecutando Capa 3");
					System.out.print(this.getId() + ": ");
					exito = this.informar(this.mensajeInformativo);
				}else if (this.resultado.getOcupacion().startsWith("M")) {
					//Si intentaste moverte a una casilla que tiene un monticulo
					System.out.println("Ejecutando Capa 3");
					Monticulo monticulo = (Monticulo)(Tablero.obtenerElementoConId(this.resultado.getOcupacion()));
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
		for (MensajeAceptacion mensajeIterado : buzonAceptacion) {
			if (mensajeIterado.isAceptarContrato()){
				mensajesPositivos.add(mensajeIterado);
			}
		}

		if(mensajesPositivos.size() == 1){
			// winner
			String idMonticulo = Tablero.matriz[this.mensajeInformativo.getPosicionMonticulo().getI()][this.mensajeInformativo.getPosicionMonticulo().getJ()];
			Monticulo monticulo = (Monticulo)Tablero.obtenerElementoConId(idMonticulo);
			monticulo.setContratoAbierto(false);
			MensajeContratacion mensajeContratacion = new MensajeContratacion(this.id, mensajesPositivos.get(0).getSender(),
														this.mensajeInformativo.getPremio(), this.mensajeInformativo.getNumPiedras(),
														this.mensajeInformativo.getPosicionMonticulo());
			AgenteCargador cargador = (AgenteCargador)Tablero.obtenerElementoConId(mensajesPositivos.get(0).getSender());
			cargador.buzonContratacion.add(mensajeContratacion);
			this.mensajeInformativo = null;
			this.buzonAceptacion.removeAll(buzonAceptacion);
			return true;
		}else if(mensajesPositivos.size() == 0) {
			//more reward
			this.mensajeInformativo.setPremio(this.mensajeInformativo.getPremio() + 2);
			return false;
		}else {
			// if(mensajesPositivos.size() > 1 )
			String idMonticulo = Tablero.matriz[this.mensajeInformativo.getPosicionMonticulo().getI()][this.mensajeInformativo.getPosicionMonticulo().getJ()];
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

			MensajeContratacion mensajeContratacion = new MensajeContratacion(this.id, mensajesPositivos.get(indexMensaje).getSender(),
					this.mensajeInformativo.getPremio(), this.mensajeInformativo.getNumPiedras(),
					this.mensajeInformativo.getPosicionMonticulo());
			cargador = (AgenteCargador)Tablero.obtenerElementoConId(mensajesPositivos.get(indexMensaje).getSender());
			cargador.buzonContratacion.add(mensajeContratacion);
			this.mensajeInformativo = null;
			this.buzonAceptacion.removeAll(buzonAceptacion);
			return true;

		}


	}


	public synchronized boolean informar(Monticulo monticulo){

		MensajeInformativo mensajeInformativo = new MensajeInformativo(monticulo.getPosicion(), this.id, monticulo.piedras);
		this.mensajeInformativo = mensajeInformativo;
		monticulo.setContratoAbierto(true);
		for (int i = 0; i < Tablero.listaAgentes.size(); i++) {
			if (Tablero.listaAgentes.get(i).capacidad != 0){
				AgenteCargador cargador =(AgenteCargador)Tablero.listaAgentes.get(i);
				cargador.buzonInformativo.add(mensajeInformativo);
			}
		}
		return true;
	}

	public synchronized  boolean informar(MensajeInformativo mensajeInformativo){
		for (int i = 0; i < Tablero.listaAgentes.size(); i++) {
			if (Tablero.listaAgentes.get(i).capacidad != 0){
				AgenteCargador cargador =(AgenteCargador)Tablero.listaAgentes.get(i);
				cargador.buzonInformativo.add(this.mensajeInformativo);
			}
		}
		return true;
	}

}

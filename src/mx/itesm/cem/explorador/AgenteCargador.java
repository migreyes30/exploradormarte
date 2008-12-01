package mx.itesm.cem.explorador;

import java.util.ArrayList;

public class AgenteCargador extends Agente {
	
	private int salarioMinimo = 0;
	private boolean cumpliendoContrato;
	public ArrayList<MensajeInformativo> buzonInformativo;
	public ArrayList<MensajeContratacion> buzonContratacion;
	
	public AgenteCargador(String id, Posicion pos) {
		super(id, pos);
		this.setBuzonContratacion(new ArrayList<MensajeContratacion>());
		this.setBuzonInformativo(new ArrayList<MensajeInformativo>());
		this.setCumpliendoContrato(false);

		while(this.getSalarioMinimo() < 10)
			this.setSalarioMinimo((int)(Math.random()*26));
	}

	public int getSalarioMinimo() {
		return salarioMinimo;
	}

	public void setSalarioMinimo(int salarioMinimo) {
		this.salarioMinimo = salarioMinimo;
	}
	
	public boolean isCumpliendoContrato() {
		return cumpliendoContrato;
	}

	public void setCumpliendoContrato(boolean cumpliendoContrato) {
		this.cumpliendoContrato = cumpliendoContrato;
	}

	public ArrayList<MensajeInformativo> getBuzonInformativo() {
		return buzonInformativo;
	}

	public void setBuzonInformativo(ArrayList<MensajeInformativo> buzonInformativo) {
		this.buzonInformativo = buzonInformativo;
	}

	public ArrayList<MensajeContratacion> getBuzonContratacion() {
		return buzonContratacion;
	}

	public void setBuzonContratacion(
			ArrayList<MensajeContratacion> buzonContratacion) {
		this.buzonContratacion = buzonContratacion;
	}

	public synchronized void actuar(int[] capas){
		int i =0;
		boolean exito = false;
		while(!exito){
			if(i >= capas.length)
				i = 0;
			switch (capas[i]) {
			case 1:
				if (this.getResultado().getOcupacion().startsWith("O")
						|| this.getResultado().getOcupacion().startsWith("A")) { //Si la casilla a la que quieres moverte esta ocupada
					System.out.println("Ejecutando Capa 1");
					exito = this.evitarObstaculo();
				}
				break;
			case 2:
				if (this.getCargaActual() != 0) { //Si lleva piedras su prioridad es ir a la nave
					System.out.println("Ejecutando Capa 2");
					exito = this.regresarANave();
					if(this.getResultado().getOcupacion().startsWith("N")){
						//System.out.print(this.getId() + ": deje " + this.getCargaActual() + " piedras\n");
						this.dejarPiedras();
						this.setCumpliendoContrato(false);
						exito = true;
					}
				}
				break;
				
			case 3:
				if(this.getBuzonContratacion().size() != 0 && this.getCargaActual() == 0){
					System.out.println("Ejecutando Capa 3: Cumplir Contrato");
					exito = this.cumplirContrato();
				}
				break;
				
			case 4:
				if (this.getResultado().getOcupacion().startsWith("M") && this.getCargaActual() == 0) {
					Monticulo monticuloContrato = null;
					if(this.getBuzonContratacion().size() != 0){
						String idMontiContrato = Tablero.matriz[this.getBuzonContratacion().get(0).getPosicionMonticulo().getI()][this.getBuzonContratacion().get(0).getPosicionMonticulo().getJ()];
						monticuloContrato = (Monticulo) (Tablero.obtenerElementoConId(idMontiContrato));
					}
					
					if(!this.isCumpliendoContrato()){
						//Si intentaste moverte a una casilla que tiene un monticulo
						System.out.println("Ejecutando Capa 4");
						Monticulo monticulo = (Monticulo)(Tablero.obtenerElementoConId(this.getResultado().getOcupacion()));
						System.out.print(this.getId() + ": ");
						exito = this.cargar(monticulo);
					}else if(this.getBuzonContratacion().size() != 0 && monticuloContrato.getId() == this.getResultado().getOcupacion()){
						System.out.println("Ejecutando Capa 4 (Contrato)");
						Monticulo monticulo = (Monticulo)(Tablero.obtenerElementoConId(this.getResultado().getOcupacion()));
						System.out.print(this.getId() + ": ");
						monticulo.setEnContrato(false);
						exito = this.cargar(monticulo);
					}
					
				}
				break;
			case 5:
				if((!this.isCumpliendoContrato() || this.getCargaActual() == 0) && this.buzonContratacion.size() == 0){
					System.out.println("Ejecutando capa 5: Leer Solicitudes");
					exito = this.leerSolicitudes();
				}
				break;

			case 6:
				System.out.println("Ejecutando Capa 6");
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
	
public synchronized boolean leerSolicitudes(){
		
		if(this.getBuzonInformativo().size() == 0){
			return false;
		}
		
		
		int premioMayor = 0,
			iMenor = 8,
			jMenor = 8;
		
		MensajeInformativo msjConPremioMayor = null,
						   mensajeCercano = null;
		
		for (MensajeInformativo mensajeIterado : this.getBuzonInformativo()) {
			if(mensajeIterado.getPremio() >= this.getSalarioMinimo()){
				
				if((mensajeIterado.getPremio() > premioMayor)){
					msjConPremioMayor = mensajeIterado;
					premioMayor = msjConPremioMayor.getPremio();
				}	
				
				int iRelativa = Math.abs(this.getPosicion().getI() - mensajeIterado.getPosicionMonticulo().getI());
				int jRelativa = Math.abs(this.getPosicion().getJ() - mensajeIterado.getPosicionMonticulo().getJ());
	
				if (iRelativa <= 7 && jRelativa <= 7){
					if ((iRelativa + jRelativa < iMenor + jMenor)){
						iMenor = iRelativa;
						jMenor = jRelativa;
						mensajeCercano = mensajeIterado;
					}
				}
			}
		}
		
		if(mensajeCercano == null){
			if(msjConPremioMayor == null){
				for(int i=0; i < this.getBuzonInformativo().size(); i++){
					this.mandarMensajeAceptacion(this.getBuzonInformativo().get(i).getSender(), false, 
												"No cumple con salario minimo y esta fuera de rango");
				}
				this.getBuzonInformativo().clear();
			}
			else{
				this.mandarMensajeAceptacion(msjConPremioMayor.getSender(), true, 
						"Puedo ir a monticulo en posicion " + 
						msjConPremioMayor.getPosicionMonticulo().getI() + ", " +
						msjConPremioMayor.getPosicionMonticulo().getJ());
				
				this.getBuzonInformativo().remove(msjConPremioMayor);

				for(int i=0; i < this.buzonInformativo.size(); i++){
					this.mandarMensajeAceptacion(this.buzonInformativo.get(i).getSender(), false, 
							"Encontre un monticulo con mas recompensa");
				}
				this.getBuzonInformativo().clear();
			}
			
		}
		else{
			
			this.mandarMensajeAceptacion(mensajeCercano.getSender(), true, 
					"Puedo ir a monticulo en posicion " + 
					mensajeCercano.getPosicionMonticulo().getI() + ", " +
					mensajeCercano.getPosicionMonticulo().getJ());
			
			this.buzonInformativo.remove(mensajeCercano);

			for(int i=0; i < this.buzonInformativo.size(); i++){
				this.mandarMensajeAceptacion(this.buzonInformativo.get(i).getSender(), false, 
						"Encontre un monticulo mas cercano");
			}
			this.getBuzonInformativo().clear();
		}
		
		return true;
		
	
	}

	public void mandarMensajeAceptacion(String receiver, boolean aceptacion, String razon){
		
		MensajeAceptacion msj = new MensajeAceptacion(this.getId(), receiver, aceptacion, razon);
		AgenteExplorador explorador = (AgenteExplorador) Tablero.obtenerElementoConId(receiver);
		explorador.getBuzonAceptacion().add(msj);
	}
	
	public boolean cumplirContrato(){
		if(this.buzonContratacion.size() == 0)
			return false;
		
		MensajeContratacion contrato = this.buzonContratacion.get(0);
		if(!Tablero.matriz[contrato.getPosicionMonticulo().getI()]
			                 [contrato.getPosicionMonticulo().getJ()].startsWith("M")){
			this.buzonContratacion.remove(contrato);
			return false;
		}
		
		else{
			this.setCumpliendoContrato(true);
			return this.irAMonitculo(contrato.getPosicionMonticulo());
		}
		
	}

	

}

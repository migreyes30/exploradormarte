package mx.itesm.cem.explorador;

import java.util.ArrayList;

public class AgenteCargador extends Agente {
	
	private int salarioMinimo = 0;
	private ArrayList<MensajeInformativo> buzonInformativo;
	private ArrayList<MensajeContratacion> buzonContratacion;
	
	public AgenteCargador(String id, Posicion pos) {
		super(id, pos);
		this.buzonInformativo = new ArrayList<MensajeInformativo>();
		this.buzonContratacion = new ArrayList<MensajeContratacion>();
		
		while(this.getSalarioMinimo() < 10)
			this.setSalarioMinimo((int)(Math.random()*26));
	}

	public int getSalarioMinimo() {
		return salarioMinimo;
	}

	public void setSalarioMinimo(int salarioMinimo) {
		this.salarioMinimo = salarioMinimo;
	}
	
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
				if (this.cargaActual != 0) { //Si lleva piedras su prioridad es ir a la nave
					System.out.println("Ejecutando Capa 2");
					exito = this.regresarANave();
					if(this.resultado.getOcupacion().startsWith("N")){
						//System.out.print(this.getId() + ": deje " + this.getCargaActual() + " piedras\n");
						this.dejarPiedras();
						exito = true;
					}
				}
				break;
				
			case 3:
				System.out.println("Ejecutando Capa 3: Cumplir Contrato");
				exito = this.cumplirContrato();
				break;
				
			case 4:
				if (this.resultado.getOcupacion().startsWith("M")&& this.getCargaActual() == 0) { //Si intentaste moverte a una casilla que tiene un monticulo
					System.out.println("Ejecutando Capa 4");
					Monticulo monticulo = (Monticulo)(Tablero.obtenerElementoConId(this.resultado.getOcupacion()));
					System.out.print(this.getId() + ": ");
					exito = this.cargar(monticulo);
				}
				break;
			case 5:
				System.out.println("Ejecutando capa 5: Leer Solicitudes");
				exito = this.leerSolicitudes();
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
		
		if(this.buzonInformativo.size() == 0){
			return false;
		}
		
		int premioMayor = 0;
		MensajeInformativo msjConPremioMayor = null;
		
		for(int i=0; i < this.buzonInformativo.size(); i++){
			if((this.buzonInformativo.get(i).getPremio() > premioMayor)
					&& this.buzonInformativo.get(i).getPremio() >= this.getSalarioMinimo()){
				msjConPremioMayor = this.buzonInformativo.get(i);
				premioMayor = msjConPremioMayor.getPremio();
			}				
		}
		
		if(msjConPremioMayor == null){
			for(int i=0; i < this.buzonInformativo.size(); i++){
				this.mandarMensajeAceptacion(this.buzonInformativo.get(i).getSender(), false, "No cumple con salario minimo");
				this.buzonInformativo.clear();
			}
			
		}
		else{
			
			// AGREGAR RANGO
			
			this.mandarMensajeAceptacion(msjConPremioMayor.getSender(), true, "OK");
			this.buzonInformativo.remove(msjConPremioMayor);

			for(int i=0; i < this.buzonInformativo.size(); i++){
				this.mandarMensajeAceptacion(this.buzonInformativo.get(i).getSender(), false, "No cumple con salario minimo");
				this.buzonInformativo.clear();
			}
		}
		
		return true;
		
	
	}
	public void mandarMensajeAceptacion(String receiver, boolean aceptacion, String razon){
		
		MensajeAceptacion msj = new MensajeAceptacion(
										this.getId(), receiver, aceptacion, razon);
		AgenteExplorador explorador = (AgenteExplorador)Tablero.obtenerElementoConId(receiver);
		explorador.buzonAceptacion.add(msj);
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
		
		else
			return this.irAMonitculo(contrato.getPosicionMonticulo());
		
	}

	

}

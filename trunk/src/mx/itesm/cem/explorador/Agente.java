package mx.itesm.cem.explorador;

import java.util.ArrayList;
import javax.swing.JLabel;
import mx.itesm.cem.explorador.exception.NoExisteElementoException;
import mx.itesm.cem.grafico.TableroGrafico;

public class Agente {
	
	private Posicion posicion;
	private int capacidad = 0;
	private int cargaActual;
	private String id;
	private ResultadoCaminar resultado = new ResultadoCaminar();
	public static final int ABAJO = 2;
	public static final int ARRIBA = 5;
	public static final int IZQUIERDA = 7;
	public static final int DERECHA = 6;
	public static final int DIAG_SUP_DER = 3;
	public static final int DIAG_SUP_IZQ = 4;
	public static final int DIAG_INF_DER = 0;
	public static final int DIAG_INF_IZQ = 1;
	private boolean dejarMorona = false;

	public Agente(String id, Posicion pos){
		this.setId(id);
		this.setPosicion(pos);
		while(this.getCapacidad() == 0)
			this.setCapacidad((int)(Math.random()* ((Tablero.totalPiedras/4)+1)));
		this.setCargaActual(0);
		
		this.resultado.setExito(false);
		this.resultado.setOcupacion(Tablero.nave.getId());
		this.resultado.setPosicion(this.getPosicion());
	}

	public Posicion getPosicion() {
		return posicion;
	}

	public void setPosicion(Posicion posicion) {
		this.posicion = posicion;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public int getCargaActual() {
		return cargaActual;
	}

	public synchronized void setCargaActual(int cargaActual) {
		this.cargaActual = cargaActual;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public synchronized ResultadoCaminar getResultado() {
		return resultado;
	}

	public synchronized void setResultado(Posicion nuevaPosicion, boolean exito, String ocupacion) {
		this.resultado.setPosicion(nuevaPosicion);
		this.resultado.setExito(exito);
		this.resultado.setOcupacion(ocupacion);
	}

/**
 * Metodo principal que controla el comportamiento del agente
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
				if (this.cargaActual != 0) { //Si lleva piedras su prioridad es ir a la nave
					System.out.println("Ejecutando Capa 2");
					exito = this.regresarANave();
					if(this.resultado.getOcupacion().startsWith("N")){
						//System.out.print(this.getId() + ": deje " + this.getCargaActual() + " piedras\n");
						this.dejarPiedras();
						this.dejarMorona = false;
						exito = true;
					}
				}
				break;
			case 3:
				if (this.resultado.getOcupacion().startsWith("M")) { //Si intentaste moverte a una casilla que tiene un monticulo
					System.out.println("Ejecutando Capa 3");
					Monticulo monticulo = (Monticulo)(Tablero.obtenerElementoConId(this.resultado.getOcupacion()));
					System.out.print(this.getId() + ": ");
					exito = this.cargar(monticulo);
					//if(exito && monticulo.getPiedras() != 0)
						
						/*System.out.print("NOOOOOOOOOOOOOOO ");
					System.out.print("PUDE CARGAR " + monticulo.getPiedras() + " PIEDRAS!!! Aun me caben: " + (this.getCapacidad() -
							this.getCargaActual()) + "\n");
					*/
				}
				break;
			case 4:
				if(this.resultado.getOcupacion().startsWith("H")){
					System.out.println("Ejecutando capa 4");
					
					Morona moronaEncontrada = (Morona) Tablero.obtenerElementoConId(this.getResultado().getOcupacion());
					ArrayList<String> caminosMorona = moronaEncontrada.getCaminos();
					
					if (caminosMorona.size() > 0){
						
						String idCaminoASeguir = caminosMorona.get(0);
						int indexCamino = Tablero.encuentraCamino(idCaminoASeguir);
						ArrayList<String> caminoASeguir = Tablero.getListaCaminos().get(indexCamino);
						exito = this.seguirCamino(caminoASeguir, moronaEncontrada.getId());
						Monticulo monticuloACargar = (Monticulo)Tablero.obtenerElementoConId(caminoASeguir.get(0)); 
						this.caminar(monticuloACargar.getPosicion());
					}
						
					break;
				}
			
			case 5:
				System.out.println("Ejecutando Capa 5");
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

/**
 * Metodo que permite que el agente camine
 * para posteriormente analizar si pudo moverse o no,
 * esto le permite explorar su entorno.
 * @return
 */
	public synchronized boolean explorar(){
		this.caminar();
		
		return this.resultado.isExito();
	}

	/* Los movimientos correspoden a los siguientes numeros:
	 0 ++  1 +-   2 +_   3 -+    4 --  5 -_   6 _+   7 _-
	 				4 | 5 | 3
	 				---------
	 				7 | * | 6
	 				---------
	 				1 | 2 | 0
	 */
	public synchronized ResultadoCaminar caminar(){
		int i = this.posicion.getI();
		int j = this.posicion.getJ();
		
		String casillaAEvaluar = null;
		Posicion nuevaPosicion = null;
		
		while(casillaAEvaluar == null){
			int movimiento = (int)(Math.random()*8);
			switch (movimiento) {
			case DIAG_INF_DER:
				if (i<Tablero.CASILLAS-1 && j<Tablero.CASILLAS-1){
					nuevaPosicion =new Posicion(i+1,j+1);
					casillaAEvaluar = Tablero.matriz[i+1][j+1];
				}
				break;
			case DIAG_INF_IZQ:
				if(i<Tablero.CASILLAS-1 && j>0){
					nuevaPosicion = new Posicion(i+1,j-1);
					casillaAEvaluar = Tablero.matriz[i+1][j-1];
				}
				break;
			case ABAJO:
				if(i<Tablero.CASILLAS-1){
					nuevaPosicion = new Posicion(i+1,j);
					casillaAEvaluar = Tablero.matriz[i+1][j];
				}
				break;
			case DIAG_SUP_DER:
				if(i>0 && j<Tablero.CASILLAS-1){
					nuevaPosicion = new Posicion(i-1,j+1);
					casillaAEvaluar = Tablero.matriz[i-1][j+1];
				}
				break;
			case DIAG_SUP_IZQ:
				if(i>0 && j>0){
					nuevaPosicion = new Posicion(i-1,j-1);
					casillaAEvaluar = Tablero.matriz[i-1][j-1];
				}
				break;
			case ARRIBA:
				if(i>0){
					nuevaPosicion = new Posicion(i-1,j);
					casillaAEvaluar = Tablero.matriz[i-1][j];
				}
				break;
			case DERECHA:
				if(j < Tablero.CASILLAS -1 ){
	
					nuevaPosicion = new Posicion(i,j+1);
					casillaAEvaluar = Tablero.matriz[i][j+1];
				}
				break;
	
			case IZQUIERDA:
				if(j>0){
					nuevaPosicion = new Posicion(i,j-1);
					casillaAEvaluar = Tablero.matriz[i][j-1];
				}
				break;
			default:			
				casillaAEvaluar = null;
				break;
			}
		}
		if(casillaAEvaluar.startsWith("H") || casillaAEvaluar == "-"){
			if (this.resultado.isExito()){
				if (this.dejarMorona && Tablero.opcionMoronas){
					//TODO
					Morona morona = new Morona(posicion);
					Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = morona.getId();
				}else{
					Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = this.resultado.getOcupacion();	
				}
					
			}else{
				Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = "-";
			}
			Tablero.matriz[nuevaPosicion.getI()][nuevaPosicion.getJ()] = this.getId();
			this.setPosicion(nuevaPosicion); //Actualizamos la posicion del agente
			this.setResultado(nuevaPosicion, true, casillaAEvaluar);
			TableroGrafico.actualizaPosicionAgente(this.getId());

		}else{
			this.setResultado(nuevaPosicion, false, casillaAEvaluar);
		}
		return this.resultado;
	}

/**
 * Metodo sobrecargado que permite que la accion
 * caminar ya no sea aleatoria, de esta manera 
 * es posible que el agente se mueva en una direccion
 * determinada, se usa en regresarANave() 
 * 
 * @param movimiento
 * @return
 */
		
	public synchronized ResultadoCaminar caminar(int movimiento){
		int i = this.posicion.getI();
		int j = this.posicion.getJ();
	
		String casillaAEvaluar = null;
		Posicion nuevaPosicion = null;
	
		while(casillaAEvaluar == null){
			switch (movimiento) {
			case DIAG_INF_DER:
				if (i<Tablero.CASILLAS-1 && j<Tablero.CASILLAS-1){
					nuevaPosicion =new Posicion(i+1,j+1);
					casillaAEvaluar = Tablero.matriz[i+1][j+1];				
				}
				break;
			case DIAG_INF_IZQ:
				if(i<Tablero.CASILLAS-1 && j>0){
					nuevaPosicion = new Posicion(i+1,j-1);
					casillaAEvaluar = Tablero.matriz[i+1][j-1];
				}
				break;
			case ABAJO:
				if(i<Tablero.CASILLAS-1){
					nuevaPosicion = new Posicion(i+1,j);
					casillaAEvaluar = Tablero.matriz[i+1][j];
				}
				break;
			case DIAG_SUP_DER:
				if(i>0 && j<Tablero.CASILLAS-1){
					nuevaPosicion = new Posicion(i-1,j+1);
					casillaAEvaluar = Tablero.matriz[i-1][j+1];
				}
				break;
			case DIAG_SUP_IZQ:
				if(i>0 && j>0){
					nuevaPosicion = new Posicion(i-1,j-1);
					casillaAEvaluar = Tablero.matriz[i-1][j-1];
				}
				break;
			case ARRIBA:
				if(i>0){
					nuevaPosicion = new Posicion(i-1,j);
					casillaAEvaluar = Tablero.matriz[i-1][j];
				}
				break;
			case DERECHA:
				if(j < Tablero.CASILLAS-1 ){
		
					nuevaPosicion = new Posicion(i,j+1);
					casillaAEvaluar = Tablero.matriz[i][j+1];
				}
				break;
		
			case IZQUIERDA:
				if(j>0){
					nuevaPosicion = new Posicion(i,j-1);
					casillaAEvaluar = Tablero.matriz[i][j-1];
				}
				break;
			default:
				/*Nunca deberia llegar aqui, pues significaria que la validacion para no salirse del tablero fallo*/
				casillaAEvaluar = null; 
			}
		}
		
		if(casillaAEvaluar.startsWith("H") || casillaAEvaluar == "-"){
			if (this.resultado.isExito()){
				Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = this.resultado.getOcupacion();	
			}else{
				Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = "-";
			}
			Tablero.matriz[nuevaPosicion.getI()][nuevaPosicion.getJ()] = this.getId();
			this.setPosicion(nuevaPosicion);
			this.setResultado(nuevaPosicion, true, casillaAEvaluar);
			TableroGrafico.actualizaPosicionAgente(this.getId());
	
		}else{
			this.setResultado(nuevaPosicion, false, casillaAEvaluar);
		}
		return this.resultado;
	}

	public synchronized ResultadoCaminar caminar(Posicion posicion){
		
		String casillaAEvaluar = Tablero.matriz[posicion.getI()][posicion.getJ()];
		if(casillaAEvaluar.startsWith("H") || casillaAEvaluar == "-"){
			if (this.resultado.isExito()){
				Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = this.resultado.getOcupacion();	
			}else{
				Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = "-";
			}
			Tablero.matriz[posicion.getI()][posicion.getJ()] = this.getId();
			this.setPosicion(posicion);
			this.setResultado(posicion, true, casillaAEvaluar);
			TableroGrafico.actualizaPosicionAgente(this.getId());	
		}else{
			this.setResultado(posicion, false, casillaAEvaluar);
		}
		return this.resultado;
	}

	/**
	 * Metodo que permite evadir obstaculos
	 * a partir de intentar moverse en todas direcciones
	 * a su alrededor hasta que encuentra una casilla
	 * vacia a donde podra moverse
	 * 
	 * @return
	 */
	public synchronized boolean evitarObstaculo(){
		int i = this.posicion.getI();
		int j = this.posicion.getJ();
		int movimiento = (int)(Math.random()*8);
	
		String casillaAEvaluar = null;
		Posicion nuevaPosicion = null;
	
	
		while(true){
			if(movimiento > 7)
				movimiento = (int)(Math.random()*8);
			switch (movimiento) {
			case DIAG_INF_DER:
				if (i<Tablero.CASILLAS-1 && j<Tablero.CASILLAS-1){
					nuevaPosicion =new Posicion(i+1,j+1);
					casillaAEvaluar = Tablero.matriz[i+1][j+1];
				}
				break;
			case DIAG_INF_IZQ:
				if(i<Tablero.CASILLAS-1 && j>0){
					nuevaPosicion = new Posicion(i+1,j-1);
					casillaAEvaluar = Tablero.matriz[i+1][j-1];
				}
				break;
			case ABAJO:
				if(i<Tablero.CASILLAS-1){
					nuevaPosicion = new Posicion(i+1,j);
					casillaAEvaluar = Tablero.matriz[i+1][j];
				}
				break;
			case DIAG_SUP_DER:
				if(i>0 && j<Tablero.CASILLAS-1){
					nuevaPosicion = new Posicion(i-1,j+1);
					casillaAEvaluar = Tablero.matriz[i-1][j+1];
				}
				break;
			case DIAG_SUP_IZQ:
				if(i>0 && j>0){
					nuevaPosicion = new Posicion(i-1,j-1);
					casillaAEvaluar = Tablero.matriz[i-1][j-1];
				}
				break;
			case ARRIBA:
				if(i>0){
					nuevaPosicion = new Posicion(i-1,j);
					casillaAEvaluar = Tablero.matriz[i-1][j];
				}
				break;
			case DERECHA:
				if(j < Tablero.CASILLAS-1 ){
	
					nuevaPosicion = new Posicion(i,j+1);
					casillaAEvaluar = Tablero.matriz[i][j+1];
				}
				break;
	
			case IZQUIERDA:
				if(j>0){
					nuevaPosicion = new Posicion(i,j-1);
					casillaAEvaluar = Tablero.matriz[i][j-1];
				}
				break;
				
			default:
				this.setResultado(nuevaPosicion, false, casillaAEvaluar);
				return false;
			}
	
			if(casillaAEvaluar == "-"){
				Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = "-";
				Tablero.matriz[nuevaPosicion.getI()][nuevaPosicion.getJ()] = this.getId();
				this.setPosicion(nuevaPosicion);
				this.setResultado(nuevaPosicion, true, "-");
				TableroGrafico.actualizaPosicionAgente(this.getId());
				return true;
				
			}else{
				movimiento++;
			}
		}
	
	}

	/**
	 * Metodo que permite que el agente regrese a la nave
	 * a traves de ir calculando en que direccion relativa 
	 * a el se encuentra la nave e irse acercando cada vez mas
	 * a ella
	 * 
	 * @return
	 */
	public synchronized boolean regresarANave(){
		int iRelativa = this.getPosicion().getI() - Tablero.posicionNave.getI(); 
		int jRelativa = this.getPosicion().getJ() - Tablero.posicionNave.getJ();
	
		if(iRelativa == 0){
			if(jRelativa<0){
				return this.caminar(DERECHA).isExito();
			}else{
				return this.caminar(IZQUIERDA).isExito();
			}
		}
	
		if(jRelativa == 0){
			if(iRelativa<0){
				return this.caminar(ABAJO).isExito();
			}else{
				return this.caminar(ARRIBA).isExito();
			}
		}
	
		if(iRelativa < 0){
			if(jRelativa<0){
				//1,2,4
				if(this.caminar(DIAG_INF_DER).isExito()){
					return true;
				}
				else{
					if(this.getResultado().getOcupacion().startsWith("N")){
						return true;
					
					}else if (this.caminar(DERECHA).isExito()) {
						return true;
					}else{
						return this.caminar(ABAJO).isExito();
					}
				}
				
			}else{
				//2,3,5
				if(this.caminar(DIAG_INF_IZQ).isExito()){
					return true;
				}
				else{
					if(this.getResultado().getOcupacion().startsWith("N")){
						return true;
					}else if (this.caminar(IZQUIERDA).isExito()){
						return true;
					}else{
						return this.caminar(ABAJO).isExito();
					}
				}
			}
		}else {
			if(jRelativa<0){
				if(this.caminar(DIAG_SUP_DER).isExito()){
					return true;
				}
				else{
					if(this.getResultado().getOcupacion().startsWith("N")){
						return true;
					}else if(this.caminar(DERECHA).isExito()){
						return true;
					}else{
						return this.caminar(ARRIBA).isExito();
					}
				}
			}else{
				if(this.caminar(DIAG_SUP_IZQ).isExito()){
					return true;
				}
				else{
					if(this.getResultado().getOcupacion().startsWith("N")){
						return true;
					}else if(this.caminar(IZQUIERDA).isExito()){
						return true;
					}else{
						return this.caminar(ARRIBA).isExito();
					}
				}
			}
		}
	}

	public synchronized boolean cargar(Monticulo monticulo){
		int cupo = this.getCapacidad() - this.getCargaActual();
		if(cupo == 0 || monticulo.getPiedras()==0){
			return false;
		}
		
		while(cupo > 0 && monticulo.getPiedras() > 0){
			this.setCargaActual(this.getCargaActual() + 1);
			monticulo.setPiedras(monticulo.getPiedras() - 1);
			TableroGrafico.replace(TableroGrafico.panelPiedras, 
									TableroGrafico.convierteAIndice(monticulo.getPosicion().getI(), 
																	monticulo.getPosicion().getJ()),
									new JLabel(monticulo.getPiedras()+""));
			TableroGrafico.replace(TableroGrafico.panelPiedras, 
					TableroGrafico.convierteAIndice(this.getPosicion().getI(), 
													this.getPosicion().getJ()),
					new JLabel(this.getCargaActual()+""));
			try {
				Thread.sleep(100); // Para que tarde al cargar
			} catch (InterruptedException e) {
				
			}
			cupo--;
		}
		
		if(monticulo.getPiedras() == 0){
			TableroGrafico.quitaMonticulo(monticulo.getId());
			Tablero.matriz[monticulo.getPosicion().getI()][monticulo.getPosicion().getJ()] = "-";
		}else{
			this.dejarMorona = true;
		}
		return true;
	}

	public synchronized boolean dejarPiedras(){
		while(this.getCargaActual() > 0){
			this.setCargaActual(this.getCargaActual() - 1);
			Tablero.nave.setPiedras(Tablero.nave.getPiedras() + 1);
			TableroGrafico.replace(TableroGrafico.panelPiedras, 
					TableroGrafico.convierteAIndice(this.getPosicion().getI(), 
													this.getPosicion().getJ()),
					new JLabel(this.getCargaActual()+""));
			TableroGrafico.replace(TableroGrafico.panelMenu, 9, 
					new JLabel("Piedras por dejar en nave: " + 
							(Tablero.totalPiedras - Tablero.nave.getPiedras())));
			try {
				Thread.sleep(100); // Para que tarde al cargar
			} catch (InterruptedException e) {
				
			}
		}
		
		try {
			Thread.sleep(100);  //Para que tarde al dejar piedras.
		} catch (InterruptedException e) {
			
		}
		return true;
	}

	public synchronized boolean seguirCamino(ArrayList<String> caminoASeguir, String idMorona){
		
			int puntoInicial = caminoASeguir.indexOf(idMorona);
			
			try{
				for (int i = puntoInicial -1; i > 0; i--) {
					 Morona nextMorona = (Morona) Tablero.obtenerElementoConId(caminoASeguir.get(i));
					 this.caminar(nextMorona.getPosicion());
				}
			
				Tablero.obtenerElementoConId(caminoASeguir.get(0));
				return true;
			}catch (NoExisteElementoException e) {
				return false;
			}
		}
	
	public synchronized boolean comerMoronas(){
		return false;
	}

}

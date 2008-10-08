package mx.itesm.cem.explorador;

import javax.swing.JLabel;

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

	public Agente(String id){
		this.setId(id);
		this.setPosicion(Tablero.posicionNave);
		while(this.getCapacidad() == 0)
			this.setCapacidad((int)(Math.random()* ((Tablero.totalPiedras/4)+1)));
		this.setCargaActual(0);
		
		this.resultado.setExito(false);
		this.resultado.setOcupacion(Tablero.nave.getId());
		this.resultado.setPosicion(Tablero.nave.getPosicion());
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
				if (this.resultado.getOcupacion().startsWith("O")) { //Si la casilla a la que quieres moverte esta ocupada
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
				if (this.resultado.getOcupacion().startsWith("M")) { //Si intentaste moverte a una casilla que tiene un monticulo
					System.out.println("Ejecutando Capa 3");
					Monticulo monticulo = (Monticulo)(Tablero.obtenerElementoConId(this.resultado.getOcupacion()));
					System.out.print(this.getId() + ": ");
					exito = this.cargar(monticulo);
					if(exito && monticulo.getPiedras() != 0)
						TableroGrafico.replace(TableroGrafico.panelPiedras, TableroGrafico.convierteAIndice(monticulo.getPosicion().getI(), monticulo.getPosicion().getJ()), new JLabel(monticulo.getPiedras()+""));
						/*System.out.print("NOOOOOOOOOOOOOOO ");
					System.out.print("PUDE CARGAR " + monticulo.getPiedras() + " PIEDRAS!!! Aun me caben: " + (this.getCapacidad() -
							this.getCargaActual()) + "\n");
					*/
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
		if(casillaAEvaluar == "-"){
			this.setPosicion(nuevaPosicion); //Actualizamos la posicion del agente
			this.setResultado(nuevaPosicion, true, "-");
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
		
		if(casillaAEvaluar == "-"){
			this.setPosicion(nuevaPosicion);
			this.setResultado(nuevaPosicion, true, "-");
			TableroGrafico.actualizaPosicionAgente(this.getId());
	
		}else{
			this.setResultado(nuevaPosicion, false, casillaAEvaluar);
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
		int movimiento = 0;
	
		String casillaAEvaluar = null;
		Posicion nuevaPosicion = null;
	
	
		while(true){
			switch (movimiento) {
			case DIAG_INF_DER:
				if (i<Tablero.CASILLAS-1 && j<Tablero.CASILLAS-1){
					nuevaPosicion =new Posicion(i+1,j+1);
					casillaAEvaluar = Tablero.matriz[i+1][j+1];
					break;
				}
			case DIAG_INF_IZQ:
				if(i<Tablero.CASILLAS-1 && j>0){
					nuevaPosicion = new Posicion(i+1,j-1);
					casillaAEvaluar = Tablero.matriz[i+1][j-1];
					break;
				}
			case ABAJO:
				if(i<Tablero.CASILLAS-1){
					nuevaPosicion = new Posicion(i+1,j);
					casillaAEvaluar = Tablero.matriz[i+1][j];
					break;
				}
			case DIAG_SUP_DER:
				if(i>0 && j<Tablero.CASILLAS-1){
					nuevaPosicion = new Posicion(i-1,j+1);
					casillaAEvaluar = Tablero.matriz[i-1][j+1];
					break;
				}
			case DIAG_SUP_IZQ:
				if(i>0 && j>0){
					nuevaPosicion = new Posicion(i-1,j-1);
					casillaAEvaluar = Tablero.matriz[i-1][j-1];
					break;
				}
			case ARRIBA:
				if(i>0){
					nuevaPosicion = new Posicion(i-1,j);
					casillaAEvaluar = Tablero.matriz[i-1][j];
					break;
				}
			case DERECHA:
				if(j < Tablero.CASILLAS-1 ){
	
					nuevaPosicion = new Posicion(i,j+1);
					casillaAEvaluar = Tablero.matriz[i][j+1];
					break;
				}
	
			case IZQUIERDA:
				if(j>0){
					nuevaPosicion = new Posicion(i,j-1);
					casillaAEvaluar = Tablero.matriz[i][j-1];
					break;
				}
			default:
				this.setResultado(nuevaPosicion, false, casillaAEvaluar);
				return false;
			}
	
			if(casillaAEvaluar == "-"){
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
				}else if (this.caminar(DERECHA).isExito()) {
					return true;
				}else{
					return this.caminar(ABAJO).isExito();
				}
				
			}else{
				//2,3,5
				if(this.caminar(DIAG_INF_IZQ).isExito()){
					return true;
				}else if (this.caminar(IZQUIERDA).isExito()){
					return true;
				}else{
					return this.caminar(ABAJO).isExito();
				}
			}
		}else {
			if(jRelativa<0){
				if(this.caminar(DIAG_SUP_DER).isExito()){
					return true;
				}else if(this.caminar(DERECHA).isExito()){
					return true;
				}else{
					return this.caminar(ARRIBA).isExito();
				}
			}else{
				if(this.caminar(DIAG_SUP_IZQ).isExito()){
					return true;
				}else if(this.caminar(IZQUIERDA).isExito()){
					return true;
				}else{
					return this.caminar(ARRIBA).isExito();
				}
			}
		}
	}

	public synchronized boolean cargar(Monticulo monticulo){
		int cupo = this.getCapacidad() - this.getCargaActual();
		if(cupo == 0 || monticulo.getPiedras()==0){
			return false;
		}
		if(cupo >= monticulo.getPiedras()){
			this.setCargaActual(monticulo.getPiedras()+this.getCargaActual());
			monticulo.setPiedras(0);			
		}
		else{
			this.setCargaActual(cupo + this.getCargaActual());
			monticulo.setPiedras(monticulo.getPiedras() - cupo);
		}
		
		if(monticulo.getPiedras() == 0){
			TableroGrafico.quitaMonticulo(monticulo.getId());
			Tablero.matriz[monticulo.getPosicion().getI()][monticulo.getPosicion().getJ()] = "-";
		}
		return true;
	}

	public synchronized boolean dejarPiedras(){
		Tablero.piedrasNave += this.getCargaActual();
		this.setCargaActual(0);
		return true;
	}

	/* Se implementara en la segunda fase **/
	public synchronized boolean dejarMoronas(){
		return false;
	}

	public synchronized boolean seguirMoronas(){
		return false;
	}

	public synchronized boolean comerMoronas(){
		return false;
	}

}

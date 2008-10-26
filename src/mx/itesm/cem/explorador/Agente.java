package mx.itesm.cem.explorador;

import java.util.ArrayList;
import java.util.Collections;

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

	public boolean dejarMoronas;

	public Agente(String id, Posicion pos){
		this.setId(id);
		this.setPosicion(pos);
		while(this.getCapacidad() == 0)
			this.setCapacidad((int)(Math.random()* 21));
		this.setCargaActual(0);

		this.resultado.setExito(false);
		this.resultado.setOcupacion(Tablero.nave.getId());
		this.resultado.setPosicion(this.getPosicion());
		this.dejarMoronas = false;
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
						exito = true;
					}
				}
				break;
			case 3:
				if (this.resultado.getOcupacion().startsWith("M")&& this.getCargaActual() == 0) { //Si intentaste moverte a una casilla que tiene un monticulo
					System.out.println("Ejecutando Capa 3");
					Monticulo monticulo = (Monticulo)(Tablero.obtenerElementoConId(this.resultado.getOcupacion()));
					System.out.print(this.getId() + ": ");
					exito = this.cargar(monticulo);
				}
				break;
			case 4:
				if(Tablero.isComunicacionMoronas()){
					if(this.resultado.getOcupacion().startsWith("H")){
						System.out.println("Ejecutando Capa 4 Morona");
						exito = this.seguirMoronas(this.getResultado().getOcupacion());
					}
				}else if(Tablero.isComunicacionKQML()){
					if(Tablero.buzon.size()>0 & this.getCargaActual() == 0){
						System.out.println("Ejecutando Capa 4 KQML");
						exito = this.leerBuzon();
						if(!exito & this.buscarMoronaCercana(this.resultado.getOcupacion()).startsWith("M")){
								Monticulo monticuloEncontrado = 
									(Monticulo)Tablero.obtenerElementoConId(this.buscarMoronaCercana(this.resultado.getOcupacion()));
								this.caminar(monticuloEncontrado.getPosicion());
								exito = true;
						}
					}
				}
				break;

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
		if(casillaAEvaluar == "-" || casillaAEvaluar.startsWith("H")){
			if(casillaAEvaluar.startsWith("H")){
				TableroGrafico.quitaMoronaGrafica(casillaAEvaluar);
				try{
					Tablero.listaMoronas.remove(Tablero.obtenerIndiceDeObjeto(casillaAEvaluar));
				}catch(NoExisteElementoException e){
					e.printStackTrace();
				}
			}
			if(this.dejarMoronas){
				Tablero.maxIdMorona++;
				String newMoronaId = "H"+(Tablero.maxIdMorona);
				Tablero.listaMoronas.add(new Morona(newMoronaId, this.getPosicion()));
				Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = newMoronaId;
				TableroGrafico.agregaMoronaGrafica(newMoronaId);
			}
			else
				Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = "-";

			Tablero.matriz[nuevaPosicion.getI()][nuevaPosicion.getJ()] = this.getId();
			this.setPosicion(nuevaPosicion); //Actualizamos la posicion del agente
			this.setResultado(nuevaPosicion, true, casillaAEvaluar);
			TableroGrafico.actualizaPosicionAgente(this.getId());

		}else{
			if(casillaAEvaluar.startsWith("M")){
				System.out.println("------------MONTICULO------- CAMINAR RANDOM-----");
			}
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

		if(casillaAEvaluar == "-" || casillaAEvaluar.startsWith("H")){
			if(casillaAEvaluar.startsWith("H")){
				TableroGrafico.quitaMoronaGrafica(casillaAEvaluar);
				try{
					Tablero.listaMoronas.remove(Tablero.obtenerIndiceDeObjeto(casillaAEvaluar));
				}catch(NoExisteElementoException e){
					e.printStackTrace();
				}
			}
			if(this.dejarMoronas){
				Tablero.maxIdMorona++;
				String newMoronaId = "H"+(Tablero.maxIdMorona);
				Tablero.listaMoronas.add(new Morona(newMoronaId, this.getPosicion()));
				Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = newMoronaId;
				TableroGrafico.agregaMoronaGrafica(newMoronaId);
			}
			else
				Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = "-";

			Tablero.matriz[nuevaPosicion.getI()][nuevaPosicion.getJ()] = this.getId();
			this.setPosicion(nuevaPosicion);
			this.setResultado(nuevaPosicion, true, casillaAEvaluar);
			TableroGrafico.actualizaPosicionAgente(this.getId());

		}else{
			if(casillaAEvaluar.startsWith("M")){
				System.out.println("------------MONTICULO-------CAMINAR(INT)-----");
			}
			this.setResultado(nuevaPosicion, false, casillaAEvaluar);
		}
		return this.resultado;
	}

	/**
	 * Metodo caminar que recibe una posicion como parámetro.
	 * Este metodo solo es usado para seguir moronas.
	 */
	public synchronized void caminar(Posicion posicionASeguir) {

	String casillaAEvaluar =
		Tablero.matriz[posicionASeguir.getI()][posicionASeguir.getJ()];

	if(casillaAEvaluar.startsWith("H")){
		TableroGrafico.quitaMoronaGrafica(casillaAEvaluar);
		try{
			Tablero.listaMoronas.remove(Tablero.obtenerIndiceDeObjeto(casillaAEvaluar));
		}catch(NoExisteElementoException e){
			e.printStackTrace();
		}
		Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = "-";
		Tablero.matriz[posicionASeguir.getI()][posicionASeguir.getJ()] = this.getId();
		this.setPosicion(posicionASeguir);
		this.setResultado(posicionASeguir, true, casillaAEvaluar);
		TableroGrafico.actualizaPosicionAgente(this.getId());

	}else{
		this.setResultado(posicionASeguir, false, casillaAEvaluar);
	}
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

		int movimiento = (int)(Math.random()*8);
		int cantCorrecionMov = 2;

		String casillaAEvaluar = null;
		Posicion nuevaPosicion = null;


		while(cantCorrecionMov > 0){
			int i = this.posicion.getI();
			int j = this.posicion.getJ();

			if(movimiento > 7)
				movimiento = 0;
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

			if(casillaAEvaluar != null
					&& (casillaAEvaluar == "-" || casillaAEvaluar.startsWith("H"))){
				if(casillaAEvaluar.startsWith("H")){
					TableroGrafico.quitaMoronaGrafica(casillaAEvaluar);
					try{
						Tablero.listaMoronas.remove(Tablero.obtenerIndiceDeObjeto(casillaAEvaluar));
					}catch(NoExisteElementoException e){
						e.printStackTrace();
					}
				}
				if(this.dejarMoronas){
					Tablero.maxIdMorona++;
					String newMoronaId = "H"+(Tablero.maxIdMorona);
					Tablero.listaMoronas.add(new Morona(newMoronaId, this.getPosicion()));
					Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = newMoronaId;
					TableroGrafico.agregaMoronaGrafica(newMoronaId);
				}
				else
					Tablero.matriz[this.getPosicion().getI()][this.getPosicion().getJ()] = "-";

				Tablero.matriz[nuevaPosicion.getI()][nuevaPosicion.getJ()] = this.getId();
				this.setPosicion(nuevaPosicion); //Actualizamos la posicion del agente
				this.setResultado(nuevaPosicion, true, casillaAEvaluar);
				TableroGrafico.actualizaPosicionAgente(this.getId());

				cantCorrecionMov--;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else{
				if(casillaAEvaluar != null && casillaAEvaluar.startsWith("M")){
					System.out.println("-----------MONTICULO-------EVITAR OBSTACULO-----");
					System.out.println("************");
				}
				//movimiento++;
			}
			movimiento++;

		}
		return true;

	}

	/**
	 * Metodo que permite que el agente regrese a la nave
	 * a traves del metodo irAPosicion
	 *
	 * @return
	 */
	public synchronized boolean regresarANave(){
		return irAPosicion(Tablero.posicionNave);
	}

	public synchronized boolean irAMonitculo(Posicion posicionMonticulo){
		return irAPosicion(posicionMonticulo);
	}

	/**
	 * Metodo que permite que el agente valla a una posicion
	 * a traves de ir calculando en que direccion relativa
	 * a el se encuentra la posicion e irse acercando cada vez mas
	 * a ella
	 *
	 * @return
	 */
	public synchronized boolean irAPosicion(Posicion posicionALlegar){
		int iRelativa = this.getPosicion().getI() - posicionALlegar.getI();
		int jRelativa = this.getPosicion().getJ() - posicionALlegar.getJ();

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
		if(Tablero.buzon.size() > 0){
			Tablero.borrarMensaje(monticulo.getPosicion());
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
		}
		else{

			if(Tablero.comunicacionMoronas){
				this.dejarMoronas = true;
			}else if(Tablero.comunicacionKQML){
				this.mandarMensaje(monticulo.getPosicion());
			}

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
			TableroGrafico.replace(TableroGrafico.panelMenu, 10,
					new JLabel("Piedras por dejar en nave: " +
							(Tablero.totalPiedras - Tablero.nave.getPiedras())));
			try {
				Thread.sleep(100); // Para que tarde al dejar piedras
			} catch (InterruptedException e) {

			}
		}

		try {
			Thread.sleep(100);  //Para que tarde al dejar la ultima piedra.
		} catch (InterruptedException e) {

		}
		this.dejarMoronas = false;
		return true;
	}

	public synchronized String buscarMoronaCercana(String idMorona){

		int i = this.posicion.getI();
		int j = this.posicion.getJ();

		ArrayList<Integer> indicesMoronasCercanas = new ArrayList<Integer>();

		for(int movimiento = 0; movimiento <= 7; movimiento++){
			switch (movimiento) {
			case DIAG_INF_DER:
				if (i<Tablero.CASILLAS-1 && j<Tablero.CASILLAS-1
						&& (Tablero.matriz[i+1][j+1].startsWith("H")||Tablero.matriz[i+1][j+1].startsWith("M"))){
					if(Tablero.matriz[i+1][j+1].startsWith("M"))
						return Tablero.matriz[i+1][j+1];
					indicesMoronasCercanas.add(Integer.parseInt(Tablero.matriz[i+1][j+1].substring(1)));
				}
				break;
			case DIAG_INF_IZQ:
				if(i<Tablero.CASILLAS-1 && j>0
						&& (Tablero.matriz[i+1][j-1].startsWith("H")||Tablero.matriz[i+1][j-1].startsWith("M"))){
					if(Tablero.matriz[i+1][j-1].startsWith("M"))
						return Tablero.matriz[i+1][j-1];
					indicesMoronasCercanas.add(Integer.parseInt(Tablero.matriz[i+1][j-1].substring(1)));
				}
				break;
			case ABAJO:
				if(i<Tablero.CASILLAS-1 && (Tablero.matriz[i+1][j].startsWith("H")|| Tablero.matriz[i+1][j].startsWith("M"))){
					if(Tablero.matriz[i+1][j].startsWith("M"))
						return Tablero.matriz[i+1][j];
					indicesMoronasCercanas.add(Integer.parseInt(Tablero.matriz[i+1][j].substring(1)));
				}
				break;
			case DIAG_SUP_DER:
				if(i>0 && j<Tablero.CASILLAS-1
						&& (Tablero.matriz[i-1][j+1].startsWith("H")|| Tablero.matriz[i-1][j+1].startsWith("M"))){
					if(Tablero.matriz[i-1][j+1].startsWith("M"))
						return Tablero.matriz[i-1][j+1];
					indicesMoronasCercanas.add(Integer.parseInt(Tablero.matriz[i-1][j+1].substring(1)));
				}
				break;
			case DIAG_SUP_IZQ:
				if(i>0 && j>0 && (Tablero.matriz[i-1][j-1].startsWith("H") || Tablero.matriz[i-1][j-1].startsWith("M"))){
					if(Tablero.matriz[i-1][j-1].startsWith("M"))
						return Tablero.matriz[i-1][j-1];
					indicesMoronasCercanas.add(Integer.parseInt(Tablero.matriz[i-1][j-1].substring(1)));
				}
				break;
			case ARRIBA:
				if(i>0 && (Tablero.matriz[i-1][j].startsWith("H") || Tablero.matriz[i-1][j].startsWith("M"))){
					if(Tablero.matriz[i-1][j].startsWith("M"))
						return Tablero.matriz[i-1][j];
					indicesMoronasCercanas.add(Integer.parseInt(Tablero.matriz[i-1][j].substring(1)));
				}
				break;
			case DERECHA:
				if(j < Tablero.CASILLAS -1
						&& (Tablero.matriz[i][j+1].startsWith("H") || Tablero.matriz[i][j+1].startsWith("M"))){
					if(Tablero.matriz[i][j+1].startsWith("M"))
						return Tablero.matriz[i][j+1];
					indicesMoronasCercanas.add(Integer.parseInt(Tablero.matriz[i][j+1].substring(1)));
				}
				break;

			case IZQUIERDA:
				if(j>0 && (Tablero.matriz[i][j-1].startsWith("H") || Tablero.matriz[i][j-1].startsWith("M"))){
					if(Tablero.matriz[i][j-1].startsWith("M"))
						return Tablero.matriz[i][j-1];
					indicesMoronasCercanas.add(Integer.parseInt(Tablero.matriz[i][j-1].substring(1)));
				}
				break;
			default:
				break;
			}
		}

		if(indicesMoronasCercanas.size() > 0){
			Collections.sort(indicesMoronasCercanas);
			if(indicesMoronasCercanas.indexOf(Integer.parseInt(idMorona.substring(1)) - 1) != -1)
				return "H" + indicesMoronasCercanas.get(indicesMoronasCercanas.indexOf(Integer.parseInt(idMorona.substring(1))-1));
			else
				return "H" + indicesMoronasCercanas.get(0);
		}
		else{
			return "";
		}
	}

	public synchronized boolean seguirMoronas(String idMorona){

		boolean exito = false;

		while(this.buscarMoronaCercana(idMorona) != ""){
			if(this.buscarMoronaCercana(idMorona).startsWith("M")){
				Monticulo monticuloEncontrado = (Monticulo)Tablero.obtenerElementoConId(
													this.buscarMoronaCercana(idMorona));
				this.caminar(monticuloEncontrado.getPosicion());
				exito = true;
				break;
			}
			Morona moronaASeguir = (Morona)Tablero.obtenerElementoConId(this.buscarMoronaCercana(idMorona));
			this.caminar(moronaASeguir.getPosicion());
			idMorona = this.getResultado().getOcupacion();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return exito;
	}


	public synchronized void mandarMensaje(Posicion posicionMonticulo){
		Tablero.buzon.add(new MensajeInformativo(posicionMonticulo, this.id));

	}

	public synchronized boolean leerBuzon(){
		int iMenor = 8;
		int jMenor = 8;
		MensajeInformativo mensajeCercano = null;
		for (MensajeInformativo mensajeIterado : Tablero.buzon) {
			int iRelativa = Math.abs(this.getPosicion().getI() - mensajeIterado.getPosicionMonticulo().getI());
			int jRelativa = Math.abs(this.getPosicion().getJ() - mensajeIterado.getPosicionMonticulo().getJ());

			if (iRelativa <= 7 && jRelativa <= 7){
				if (iRelativa + jRelativa < iMenor + jMenor){
					iMenor = iRelativa;
					jMenor = jRelativa;
					mensajeCercano = mensajeIterado;
				}
			}
		}
		if(mensajeCercano != null){
			return this.irAMonitculo(mensajeCercano.getPosicionMonticulo());

		}else{
			return false;
		}

	}


}

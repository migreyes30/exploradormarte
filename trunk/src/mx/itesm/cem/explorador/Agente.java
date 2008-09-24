package mx.itesm.cem.explorador;


public class Agente {
	private Posicion posicion;
	private int capacidad;
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

	public Agente(){
		

	}
	public Agente(String id){
		this.setId(id);
		this.setPosicion(Tablero.posicionNave);
		this.setCapacidad((int) Math.random()* ((Tablero.totalPiedras/4)+1));
		this.setCargaActual(0);
//		this.resultado.setExito(false);
//		this.resultado.setOcupacion((Nave)(Tablero.matriz[Tablero.posicionNave.getX()][Tablero.posicionNave.getY()]));
//		this.resultado.setPosicion(Tablero.posicionNave);
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

	public void setCargaActual(int cargaActual) {
		this.cargaActual = cargaActual;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/* Los movimientos correspoden a los siguientes numeros:
	 0 ++  1 +-   2 +_   3 -+    4 --  5 -_   6 _+   7 _-
	 				4 | 5 | 3
	 				---------
	 				7 | * | 6
	 				---------
	 				1 | 2 | 0
	 */
	public ResultadoCaminar caminar(){
		int i = this.posicion.getI();
		int j = this.posicion.getJ();

		int movimiento = (int)(Math.random()*8);
		String casillaAEvaluar = null;
		Posicion nuevaPosicion = null;

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
			casillaAEvaluar = "-";
			System.out.println(movimiento);
			System.out.println("Vali pito! $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			break;
		}

		if(casillaAEvaluar == "-"){
			this.setPosicion(nuevaPosicion);
			System.out.println("ESTOY AHORA EN [" + this.getPosicion().getI() + ", " + this.getPosicion().getJ() +"]");
//			casillaAEvaluar = this;
//			Tablero.matriz[i][j] = "-";
			this.setResultado(nuevaPosicion, true, null);

		}else{
			this.setResultado(nuevaPosicion, false, casillaAEvaluar);
		}
		return this.resultado;
	}

	public boolean explorar(){
		this.caminar();
		return this.resultado.isExito();
	}

	public boolean cargar(Monticulo monticulo){
		int cupo = this.getCapacidad() - this.getCargaActual();
		if(cupo == 0 || monticulo.getPiedras()==0){
			return false;
		}
		monticulo.setPiedras(monticulo.getPiedras() - cupo);
		this.setCargaActual(this.getCargaActual() + cupo);
		return true;
	}

	/*TODO*/
	public boolean regresarANave(){
		int xRelativa = Tablero.posicionNave.getI() - this.getPosicion().getI();
		int yRelativa = Tablero.posicionNave.getJ() - this.getPosicion().getJ();

		if(xRelativa == 0){
			if(yRelativa<0){
				this.caminar(DERECHA);
			}else{
				this.caminar(IZQUIERDA);
			}
		}

		if(yRelativa == 0){
			if(xRelativa<0){
				this.caminar(ABAJO);
			}else{
				this.caminar(ARRIBA);
			}
		}

		if(xRelativa < 0){
			if(yRelativa<0){
				//1,2,4
				if(this.caminar(DIAG_SUP_IZQ).isExito()){
					return true;
				}else if (this.caminar(IZQUIERDA).isExito()) {
					return true;
				}else{
					return this.caminar(ARRIBA).isExito();
				}

			}else{
				//2,3,5
				if(this.caminar(DIAG_SUP_DER).isExito()){
					return true;
				}else if (this.caminar(ARRIBA).isExito()){
					return true;
				}else{
					return this.caminar(DERECHA).isExito();
				}
			}
		}else{
			if(yRelativa<0){
				if(this.caminar(DIAG_INF_IZQ).isExito()){
					return true;
				}else if(this.caminar(IZQUIERDA).isExito()){
					return true;
				}else{
					return this.caminar(ABAJO).isExito();
				}
			}else{
				if(this.caminar(DIAG_INF_DER).isExito()){
					return true;
				}else if(this.caminar(DERECHA).isExito()){
					return true;
				}else{
					return this.caminar(ABAJO).isExito();
				}
			}
		}
	}

	public boolean evitarObstaculo(){
		int x = this.posicion.getI();
		int y = this.posicion.getJ();
		int movimiento = 0;

		Object casillaAEvaluar = null;
		Posicion nuevaPosicion = null;


		while(true){
			switch (movimiento) {
			case 0:
				if (x<Tablero.CASILLAS && y<Tablero.CASILLAS){
					nuevaPosicion =new Posicion(x+1,y+1);
					casillaAEvaluar = Tablero.matriz[x+1][y+1];
					break;
				}
			case 1:
				if(x<Tablero.CASILLAS && y>0){
					nuevaPosicion = new Posicion(x+1,y-1);
					casillaAEvaluar = Tablero.matriz[x+1][y-1];
					break;
				}
			case 2:
				if(x<Tablero.CASILLAS){
					nuevaPosicion = new Posicion(x+1,y);
					casillaAEvaluar = Tablero.matriz[x+1][y];
					break;
				}
			case 3:
				if(x>0 && y<Tablero.CASILLAS){
					nuevaPosicion = new Posicion(x-1,y+1);
					casillaAEvaluar = Tablero.matriz[x-1][y+1];
					break;
				}
			case 4:
				if(x>0 && y>0){
					nuevaPosicion = new Posicion(x-1,y-1);
					casillaAEvaluar = Tablero.matriz[x-1][y-1];
					break;
				}
			case 5:
				if(x>0){
					nuevaPosicion = new Posicion(x-1,y);
					casillaAEvaluar = Tablero.matriz[x-1][y];
					break;
				}
			case DERECHA:
				if(y < Tablero.CASILLAS ){

					nuevaPosicion = new Posicion(x,y+1);
					casillaAEvaluar = Tablero.matriz[x][y+1];
					break;
				}

			case 7:
				if(y>0){
					nuevaPosicion = new Posicion(x,y-1);
					casillaAEvaluar = Tablero.matriz[x][y-1];
					break;
				}
			default:
				this.setResultado(nuevaPosicion, false, casillaAEvaluar);
				return false;
			}

			if(casillaAEvaluar == null){
				this.setPosicion(nuevaPosicion);
				casillaAEvaluar = this;
				Tablero.matriz[x][y] = null;
				this.setResultado(nuevaPosicion, true, null);
				return true;

			}else{
				movimiento++;
			}
		}

	}

	public boolean dejarPiedras(){
		Tablero.piedrasNave += this.getCargaActual();
		this.setCargaActual(0);
		return true;
	}

	public void setResultado(Posicion nuevaPosicion, boolean exito, Object ocupacion) {
		this.resultado.setPosicion(nuevaPosicion);
		this.resultado.setExito(exito);
		this.resultado.setOcupacion(ocupacion);
	}

	public void actuar(int[] capas){
		int i =0;
		boolean exito = false;

		while (Tablero.piedrasNave != Tablero.totalPiedras) {

			switch (capas[i]) {
			case 1:
				if (this.resultado.getOcupacion() != null) {
					exito = this.evitarObstaculo();
				}
				break;
			case 2:
				if (this.cargaActual != 0) {
					exito = regresarANave();
				}
				break;
			case 3:
				if (this.resultado.getOcupacion() instanceof Monticulo) {
					exito = this.cargar((Monticulo)this.resultado.getOcupacion());
				}
				break;
			case 4:
				this.explorar();
				break;
			default:
				break;
			}
			if (exito) {
				i = 0;
			} else {
				i++;
			}
		}
	}

	public ResultadoCaminar getResultado() {
		return resultado;
	}

	public ResultadoCaminar caminar(int movimiento){
		int i = this.posicion.getI();
		int j = this.posicion.getJ();

		Object casillaAEvaluar = null;
		Posicion nuevaPosicion = null;

		switch (movimiento) {
		case 0:
			if (i<Tablero.CASILLAS && j<Tablero.CASILLAS){
				nuevaPosicion =new Posicion(i+1,j+1);
				casillaAEvaluar = Tablero.matriz[i+1][j+1];
				break;
			}
		case 1:
			if(i<Tablero.CASILLAS && j>0){
				nuevaPosicion = new Posicion(i+1,j-1);
				casillaAEvaluar = Tablero.matriz[i+1][j-1];
				break;
			}
		case 2:
			if(i<Tablero.CASILLAS){
				nuevaPosicion = new Posicion(i+1,j);
				casillaAEvaluar = Tablero.matriz[i+1][j];
				break;
			}
		case 3:
			if(i>0 && j<Tablero.CASILLAS){
				nuevaPosicion = new Posicion(i-1,j+1);
				casillaAEvaluar = Tablero.matriz[i-1][j+1];
				break;
			}
		case 4:
			if(i>0 && j>0){
				nuevaPosicion = new Posicion(i-1,j-1);
				casillaAEvaluar = Tablero.matriz[i-1][j-1];
				break;
			}
		case 5:
			if(i>0){
				nuevaPosicion = new Posicion(i-1,j);
				casillaAEvaluar = Tablero.matriz[i-1][j];
				break;
			}
		case 6:
			if(j < Tablero.CASILLAS ){

				nuevaPosicion = new Posicion(i,j+1);
				casillaAEvaluar = Tablero.matriz[i][j+1];
				break;
			}

		case 7:
			if(j>0){
				nuevaPosicion = new Posicion(i,j-1);
				casillaAEvaluar = Tablero.matriz[i][j-1];
				break;
			}
		default:
			casillaAEvaluar = null;
		}

		if(casillaAEvaluar == null){
			this.setPosicion(nuevaPosicion);
			casillaAEvaluar = this;
			Tablero.matriz[i][j] = null;
			this.setResultado(nuevaPosicion, true, null);

		}else{
			this.setResultado(nuevaPosicion, false, casillaAEvaluar);
		}
		return this.resultado;
	}


	/* Se implementara en la segunda fase **/
	public boolean dejarMoronas(){
		return false;
	}

	public boolean seguirMoronas(){
		return false;
	}

	public boolean comerMoronas(){
		return false;
	}

}

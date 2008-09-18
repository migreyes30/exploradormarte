package mx.itesm.cem.explorador;

public class Agente {
	private Posicion posicion;
	private int capacidad;
	private int cargaActual;
	private String id;

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
	 */
	public boolean explorar(){
		int i = this.posicion.getI();
		int j = this.posicion.getJ();

		int movimiento = (int) Math.random()*9;

		switch (movimiento) {
		case 0:
			if (i<Tablero.CASILLAS && j<Tablero.CASILLAS && Tablero.matriz[i+1][j+1]=="-"){
				this.setPosicion(new Posicion(i+1,j+1));
				Tablero.matriz[i+1][j+1] = this.getId();
				Tablero.matriz[i][j] = "-";
				return true;
			}
		case 1:
			if(i<Tablero.CASILLAS && j>0 && Tablero.matriz[i+1][j-1]=="-"){
				this.setPosicion(new Posicion(i+1,j-1));
				Tablero.matriz[i+1][j-1] = this.getId();
				Tablero.matriz[i][j] = "-";
				return true;
			}
		case 2:
			if(i<Tablero.CASILLAS && Tablero.matriz[i+1][j]=="-"){
				this.setPosicion(new Posicion(i+1,j));
				Tablero.matriz[i+1][j] = this.getId();
				Tablero.matriz[i][j] = "-";
				return true;
			}
		case 3:
			if(i>0 && j<Tablero.CASILLAS && Tablero.matriz[i-1][j+1]=="-"){
				this.setPosicion(new Posicion(i-1,j+1));
				Tablero.matriz[i-1][j+1] = this.getId();
				Tablero.matriz[i][j] = "-";
				return true;
			}
		case 4:
			if(i>0 && j>0 && Tablero.matriz[i-1][j-1]=="-"){
				this.setPosicion(new Posicion(i-1,j-1));
				Tablero.matriz[i-1][j-1] = this.getId();
				Tablero.matriz[i][j] = "-";
				return true;
			}
		case 5:
			if(i>0 && Tablero.matriz[i-1][j]=="-"){
				this.setPosicion(new Posicion(i-1,j));
				Tablero.matriz[i-1][j] = this.getId();
				Tablero.matriz[i][j] = "-";
				return true;
			}
		case 6:
			if(j < Tablero.CASILLAS && Tablero.matriz[i][j+1]=="-"){
				this.setPosicion(new Posicion(i,j+1));
				Tablero.matriz[i][j+1] = this.getId();
				Tablero.matriz[i][j] = "-";
				return true;
			}

		case 7:
			if(j>0 && Tablero.matriz[i][j-1]=="-"){
				this.setPosicion(new Posicion(i,j-1));
				Tablero.matriz[i][j-1] = this.getId();
				Tablero.matriz[i][j] = "-";
				return true;
			}

		default:
			return false;
		}

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

	public boolean regresarANave(){
		return false;

	}

	public boolean evitarObstaculo(){
		return false;
	}

	public boolean dejarPiedras(){
		return false;
	}

	public boolean dejarMoronas(){
		return false;
	}

	public boolean seguirMoronas(){
		return false;
	}

	public boolean comerMoronas(){
		return false;
	}

	public boolean recogerMoronas(){
		return false;
	}

	public boolean checarAlrededores(){
		return false;
	}
}

package mx.itesm.cem.explorador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.itesm.cem.explorador.exception.NoExisteElementoException;

public class Tablero {

	/*
	 * Definicion de elementos
	 *
	 * A -> Agente
	 * M -> Monticulo
	 * O -> Obstaculo
	 * N -> Nave
	 * - -> vacio
	 *
	 * Tablero:
	 * 600*600 pixeles
	 * Cada cuadro 40 pixeles * 40 pixeles
	 * Total cuadros = 15 * 15
	 */

	public static String[][] matriz;
	public static int NUM_MONTICULOS;
	public static int NUM_OBSTACULOS;
	public static int NUM_AGENTES;
	public static int totalPiedras;
	public static final int CASILLAS = 15;
	public static Posicion posicionNave;
	public static Nave nave;
	public static ArrayList<Agente> listaAgentes;
	public static ArrayList<Monticulo> listaMonticulos;
	public static ArrayList<Obstaculo> listaObstaculos;
	public static ArrayList<Morona> listaMoronas;
	public static int[] capas;
	public static Map<Integer, String> nombresCapas;
	public static ArrayList<MensajeInformativo> buzon;
	public static boolean comunicacionMoronas;
	public static boolean comunicacionKQML;

	public static int maxIdMorona = 0;

	public Tablero(int numMonticulos, int numObstaculos, int numAgentes, int comunicacion){

		Tablero.NUM_MONTICULOS = numMonticulos;
		Tablero.NUM_OBSTACULOS = numObstaculos;
		Tablero.NUM_AGENTES = numAgentes;
		Tablero.nombresCapas = new HashMap<Integer, String>();
		nombresCapas.put(new Integer(1), "Evitar Obstaculo");
		nombresCapas.put(new Integer(2), "Regresar a Nave");
		nombresCapas.put(new Integer(3), "Cargar piedras");
		
		nombresCapas.put(new Integer(5), "Explorar");

		Tablero.listaAgentes = new ArrayList<Agente>();
		Tablero.listaMonticulos = new ArrayList<Monticulo>();
		Tablero.listaObstaculos = new ArrayList<Obstaculo>();
		Tablero.listaMoronas = new ArrayList<Morona>();
		Tablero.buzon = new ArrayList<MensajeInformativo>();
		
		switch (comunicacion) {
		case 0: 
			break;
				
		case 1: Tablero.comunicacionMoronas = true;
				nombresCapas.put(new Integer(4), "Seguir Moronas");
			break;
		case 2: Tablero.comunicacionKQML = true;
				nombresCapas.put(new Integer(4), "Comunicación KQML");
			break;
		}
		Tablero.totalPiedras = 0;
		ThreadAgente.hasNotified = false;

		Tablero.matriz = creaMatriz();
	}

	public static boolean isTerminado(){
		if(Tablero.nave.getPiedras() == totalPiedras)
			return true;
		else
			return false;
	}

	public String[][] creaMatriz(){
		// Aqui creamos la matriz al azar
		String[][] mat = new String[CASILLAS][CASILLAS];

		for(int i=0; i < mat.length; i++){
			for(int j=0; j < mat.length; j++){
				mat[i][j] = "-";
			}
		}

		int iAzar, jAzar;
		int cantAgente = Tablero.NUM_AGENTES;
		int	cantMonticulo = Tablero.NUM_MONTICULOS;
		int cantObstaculo = Tablero.NUM_OBSTACULOS;

		/*----------------------
		 *Insertando Nave
		 *---------------------
		 */
		iAzar = (int)(Math.random()* CASILLAS);
		jAzar = (int)(Math.random()* CASILLAS);

		mat[iAzar][jAzar] = "N" + (int)(Math.random()*35536);
		nave = new Nave(mat[iAzar][jAzar], new Posicion(iAzar, jAzar));
		Tablero.posicionNave = nave.getPosicion();
		/*-----------------------*/

		/*Insertando Monticulos*/
		while(cantMonticulo > 0){
			iAzar = (int)(Math.random()* CASILLAS);
			jAzar = (int)(Math.random()* CASILLAS);

			if(mat[iAzar][jAzar] == "-"){
				String id = "M" + (int)(Math.random()*35536);
				mat[iAzar][jAzar] = id;
				Tablero.listaMonticulos.add(new Monticulo(id, new Posicion(iAzar,jAzar)));
				Tablero.totalPiedras += Tablero.listaMonticulos.get(Tablero.listaMonticulos.size()-1).getPiedras();
				cantMonticulo--;
			}

		}

		/*Insertando Obstaculos*/
		while(cantObstaculo > 0){
			iAzar = (int)(Math.random()* CASILLAS);
			jAzar = (int)(Math.random()* CASILLAS);

			if(mat[iAzar][jAzar] == "-"){
				String id = "O" + (int)(Math.random()*35536);
				mat[iAzar][jAzar] = id;
				Tablero.listaObstaculos.add(new Obstaculo(id,
													new Posicion(iAzar,jAzar)));
				cantObstaculo--;
			}

		}

		/*Insertando Agentes*/
		while(cantAgente > 0){
			iAzar = (int)(Math.random()* CASILLAS);
			jAzar = (int)(Math.random()* CASILLAS);

			if(mat[iAzar][jAzar] == "-"){
				String id = "A" + (int)(Math.random()*35536);
				mat[iAzar][jAzar] = id;
				Tablero.listaAgentes.add(new Agente(id,
											new Posicion(iAzar,jAzar)));
				cantAgente--;
			}

		}

		return mat;
	}

	/** Método para obtener un elemento con su id.
	 *
	 * Se debe hacer un cast cuando se mande a llamar este metodo
	 * Por ejemplo:
	 * Obstaculo o = (Obstaculo) tablero.obtenerElementoConId("unId");
	 * Monticulo m = (Monticulo) tablero.obtenerElementoConId("otroId");
	 */
	public static Object obtenerElementoConId(String id){
		char tipo = id.charAt(0);
		switch(tipo){
			case 'A':
				for(int i=0; i < Tablero.listaAgentes.size(); i++){
					Agente temp = Tablero.listaAgentes.get(i);
					if(temp.getId().equals(id))
						return temp;
				}

			case 'M':
				for(int i=0; i < Tablero.listaMonticulos.size(); i++){
					Monticulo temp = Tablero.listaMonticulos.get(i);
					if(temp.getId().equals(id))
						return temp;
				}

			case 'O':
				for(int i=0; i < Tablero.listaObstaculos.size(); i++){
					Obstaculo temp = Tablero.listaObstaculos.get(i);
					if(temp.getId().equals(id))
						return temp;
				}

			case 'H':
				for(int i=0; i < Tablero.listaMoronas.size(); i++){
					Morona temp = Tablero.listaMoronas.get(i);
					if(temp.getId().equals(id))
						return temp;
				}

			case 'N':
				if(Tablero.nave.getId().equals(id))
					return Tablero.nave;

			default:
				throw new NoExisteElementoException("No existe elemento");
		}
	}

	public static int obtenerIndiceDeObjeto(String id){
		char tipo = id.charAt(0);
		switch(tipo){
			case 'A':
				for(int i=0; i < Tablero.listaAgentes.size(); i++){
					Agente temp = Tablero.listaAgentes.get(i);
					if(temp.getId() == id)
						return i;
				}

			case 'M':
				for(int i=0; i < Tablero.listaMonticulos.size(); i++){
					Monticulo temp = Tablero.listaMonticulos.get(i);
					if(temp.getId() == id)
						return i;
				}

			case 'O':
				for(int i=0; i < Tablero.listaObstaculos.size(); i++){
					Obstaculo temp = Tablero.listaObstaculos.get(i);
					if(temp.getId() == id)
						return i;
				}

			case 'H':
				for(int i=0; i < Tablero.listaMoronas.size(); i++){
					Morona temp = Tablero.listaMoronas.get(i);
					if(temp.getId() == id)
						return i;
				}

			default:
				throw new NoExisteElementoException("Id no existente o invalido");
		}
	}

	public String toString(){
		String res = "";
		for(int i=0; i < Tablero.matriz.length; i++){
			for(int j = 0; j < Tablero.matriz.length; j++){
				res += Tablero.matriz[i][j];
			}
			res += "\n";
		}
		return res;
	}

	public static void imprimeTablero(){
		for(int i=0; i < Tablero.matriz.length; i++){
			for(int j = 0; j < Tablero.matriz.length; j++){
				System.out.print(Tablero.matriz[i][j]);
			}
			System.out.println();
		}
	}

	public static boolean isComunicacionMoronas() {
		return comunicacionMoronas;
	}

	public static void setComunicacionMoronas(boolean comunicacionMoronas) {
		Tablero.comunicacionMoronas = comunicacionMoronas;
	}

	public static boolean isComunicacionKQML() {
		return comunicacionKQML;
	}

	public static void setComunicacionKQML(boolean comunicacionKQML) {
		Tablero.comunicacionKQML = comunicacionKQML;
	}

	public static synchronized void borrarMensaje(Posicion posicionMonticulo){
		MensajeInformativo mensajeABorrar = null; 
		for (MensajeInformativo mensajeIterado : Tablero.buzon) {
			if (mensajeIterado.getPosicionMonticulo() == posicionMonticulo){
				mensajeABorrar = mensajeIterado;
				break;
			}
		}
		if(mensajeABorrar != null){
			Tablero.buzon.remove(mensajeABorrar);
		}
	}
}

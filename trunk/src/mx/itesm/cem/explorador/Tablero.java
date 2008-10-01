package mx.itesm.cem.explorador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;

import mx.itesm.cem.explorador.exception.NoExisteElementoException;
import mx.itesm.cem.grafico.TableroGrafico;

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
	String fondo; //Imagen; despues tenemos que usar la clase Image
	int[] capas;
	public static int totalPiedras;
	public static int piedrasNave;
	public static final int CASILLAS = 15;
	public static Posicion posicionNave;
	public static Nave nave;
	public static ArrayList<Agente> listaAgentes = new ArrayList<Agente>();
	public static ArrayList<Monticulo> listaMonticulos = new ArrayList<Monticulo>();
	public static ArrayList<Obstaculo> listaObstaculos = new ArrayList<Obstaculo>();
	
	
	public Tablero(){
		//Constructor del tablero
		Tablero.matriz = creaMatriz();
		
		
	}
	
	public boolean isTerminado(){
		if(Tablero.piedrasNave == totalPiedras)
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
		
		int iAzar, jAzar,
			cantAgente = 15,
			cantMonticulo = 5,
			cantObstaculo = 10;
		
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
				Tablero.listaMonticulos.add(new Monticulo(id, 
													new Posicion(iAzar,jAzar)));
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
			String id = "A" + (int)(Math.random()*35536);
			Tablero.listaAgentes.add(new Agente(id));
			cantAgente--;
			
			
		}
		
		return mat;
	}
	
	/* Método para obtener un elemento con su id.
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
					if(temp.getId() == id)
						return temp;
				}
				
			case 'M':
				for(int i=0; i < Tablero.listaMonticulos.size(); i++){
					Monticulo temp = Tablero.listaMonticulos.get(i);
					if(temp.getId() == id)
						return temp;
				}
				
			case 'O':
				for(int i=0; i < Tablero.listaObstaculos.size(); i++){
					Obstaculo temp = Tablero.listaObstaculos.get(i);
					if(temp.getId() == id)
						return temp;
				}
				
			case 'N':
				if(Tablero.nave.getId() == id)
					return Tablero.nave;
			default:
				throw new NoExisteElementoException("No existe elemento");
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
	
	public static void main(String[] args){
		Tablero t = new Tablero();
		System.out.println(t.toString());
		Posicion p = TableroGrafico.convierteAPosicion(224);
		System.out.println("" + p.getI() + ", " + p.getJ());
		
	}
			
}

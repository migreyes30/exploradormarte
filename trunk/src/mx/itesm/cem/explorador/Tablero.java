package mx.itesm.cem.explorador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;

public class Tablero {
	
	/*
	 * Definicion de elementos
	 * 
	 * M -> Monticulo
	 * O -> Obstaculo
	 * N -> Nave
	 * - -> vacio
	 * 
	 * Tablero:
	 * 600*600 pixeles
	 * Cada cuadro 30 pixeles* 30 pixeles
	 * Total cuadros = 20 * 20
	 */
	//int[][] posicionNave;  CREO QUE NO ES ASI... MMM..
	
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
		if(this.piedrasNave == totalPiedras)
			return true;
		else
			return false;
	}
	
	public String toString(){
		String res = "";	
		for(int j=0; j < Tablero.matriz.length; j++){
			for(int i = 0; i < Tablero.matriz.length; i++){
				res += Tablero.matriz[i][j];
			}
			res += "\n";
		}
		return res;
	}
	
	public String[][] creaMatriz(){
		// Aqui creamos la matriz al azar
		String[][] mat = new String[CASILLAS][CASILLAS];
		
		for(int i=0; i < mat.length; i++){
			for(int j=0; j < mat.length; j++){
				mat[i][j] = "-";
			}
		}
		
		int xAzar, yAzar,
			cantAgente = 10,
			cantMonticulo = 10,
			cantObstaculo = 10;
		
		/*----------------------
		 *Insertando Nave
		 *---------------------
		 */
		xAzar = (int)(Math.random()* CASILLAS);
		yAzar = (int)(Math.random()* CASILLAS);
		
		mat[xAzar][yAzar] = "N" + (int)(Math.random()*35536);
		nave = new Nave(mat[xAzar][yAzar], new Posicion(xAzar, yAzar));
		Tablero.posicionNave = nave.getPosicion();
		/*-----------------------*/
		
		/*Insertando Monticulos*/
		while(cantMonticulo > 0){
			xAzar = (int)(Math.random()* CASILLAS);
			yAzar = (int)(Math.random()* CASILLAS);
			
			if(mat[xAzar][yAzar] == "-"){
				String id = "M" + (int)(Math.random()*35536);
				mat[xAzar][yAzar] = id;
				Tablero.listaMonticulos.add(new Monticulo(id, 
													new Posicion(xAzar,yAzar)));
				cantMonticulo--;
			}
			
		}
		
		/*Insertando Obstaculos*/
		while(cantObstaculo > 0){
			xAzar = (int)(Math.random()* CASILLAS);
			yAzar = (int)(Math.random()* CASILLAS);
			
			if(mat[xAzar][yAzar] == "-"){
				String id = "O" + (int)(Math.random()*35536);
				mat[xAzar][yAzar] = id;
				Tablero.listaObstaculos.add(new Obstaculo(id, 
													new Posicion(xAzar,yAzar)));
				cantObstaculo--;
			}
			
		}

		return mat;
	}
	
    //TODO: Metodos para obtener monticulos y obstaculos por su id!!
	
	public static void main(String[] args){
		Tablero t = new Tablero();
		System.out.println(t.toString());
	}
		
}

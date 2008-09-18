package mx.itesm.cem.explorador;

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
	int totalPiedras;
	int piedrasNave;
	public static final int CASILLAS = 15;
	
	
	public Tablero(){
		//Constructor del tablero
		Tablero.matriz = hazMatriz();
		
		
	}
	
	public String[][] hazMatriz(){
		// Aqui creamos la matriz al azar
		String[][] mat = new String[CASILLAS][CASILLAS];
		String[] sa = {"-", "A", "M", "O", "N"};
		int cantVacio = 0,
			cantAgente = 10,
			cantMonticulo = 10,
			cantObstaculo = 40,
			cantNave = 1;
		
		int[] na = {cantVacio, cantAgente, cantMonticulo, cantObstaculo, cantNave};
		
		final int LIMITX = CASILLAS;
		final int LIMITY = CASILLAS;
		
		
		for(int y=0; y < LIMITY; y++){
			int contx1 = 0;
			int contx2 = 0;
			
			for(int x =0; x < LIMITX; x++){	
				
				if(x < LIMITX / 2){ // SOLO DOS ELEMENTOS
					if(contx1 < 2){
						int r = (int)(Math.random()*4);
						if(na[r] > 0 && !sa[r].equals("-")){
							//System.out.print(sa[r]);
							mat[x][y] = sa[r];
							na[r]--;
							contx1++;
						}
						else if(sa[r].equals("-")){	
							//System.out.print(sa[r]);
							mat[x][y] = sa[r];
						}
						else{
							//System.out.print("-");
							mat[x][y] = "-";
						}	
					}
					else{
						//System.out.print("-");
						mat[x][y] = "-";
					}
				}
				else {
					if(contx2 < 2){
						int r = (int)(Math.random()*4);
						if(na[r] > 0 && !sa[r].equals("-")){
							//System.out.print(sa[r]);
							mat[x][y] = sa[r];
							na[r]--;
							contx2++;
						}
						else if(sa[r].equals("-")){		
							//System.out.print(sa[r]);
							mat[x][y] = sa[r];
						}
						else{
							//System.out.print("-");
							mat[x][y] = "-";
						}	
					}	
					else{
						//System.out.print("-");
						mat[x][y] = "-";
					}
				}
			}
		}
		return mat;
	}
	
	public boolean isTerminado(){
		if(this.piedrasNave == totalPiedras)
			return true;
		else
			return false;
	}
	
	public void prueba(){
		
		for(int j=0; j < Tablero.matriz.length; j++){
			for(int i = 0; i < Tablero.matriz.length; i++){
				System.out.print(Tablero.matriz[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args){
			Tablero t = new Tablero();
			t.prueba();
			
	}		
}

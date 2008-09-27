package mx.itesm.cem.grafico;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import mx.itesm.cem.explorador.Agente;
import mx.itesm.cem.explorador.Monticulo;
import mx.itesm.cem.explorador.Obstaculo;
import mx.itesm.cem.explorador.Posicion;
import mx.itesm.cem.explorador.Tablero;


public class TableroGrafico {
	
	public static JPanel panelMatriz;
	public static JFrame frame;
	public static JPanel panelMenu;
	public static JLayeredPane panelDerecha;
	
	public static NaveGrafica naveGrafica;
	public static ArrayList<AgenteGrafico> listaAgentesGraficos = 
											new ArrayList<AgenteGrafico>();
	public static ArrayList<MonticuloGrafico> listaMonticulosGraficos = 
											new ArrayList<MonticuloGrafico>();
	public static ArrayList<ObstaculoGrafico> listaObstaculosGraficos = 
											new ArrayList<ObstaculoGrafico>();
	
	public TableroGrafico(){
		frame = new JFrame("Explorador Marte");
		frame.setSize(800, 635);
		panelMenu = new JPanel();
		panelDerecha = new JLayeredPane();
		
		panelMatriz = new JPanel();
		
		frame.getContentPane().setLayout(new BorderLayout());		
		panelMatriz.setLayout(new GridLayout(15,15));
		
		ImageIcon fondo = new ImageIcon(getClass().getResource("Mars_atmosphere.jpg"));
		JLabel labelFondo = new JLabel(fondo);
		labelFondo.setSize(fondo.getIconWidth(), fondo.getIconHeight());
		
		panelMenu.setBackground(new Color(255,0,0));
		panelMenu.setSize(200,600);
		panelMatriz.setOpaque(false);
		panelMatriz.setSize(600, 600);
		
		for(int j=0; j < 225; j++){
			JLabel x = new JLabel("");
			x.setSize(40,40);
			panelMatriz.add(x);
		}
		
		agregaObjetosAListas();
		
		panelDerecha.add(labelFondo, new Integer(1));
		panelDerecha.add(panelMatriz, new Integer(2));
		
		inicializaTablero();
		
		frame.getContentPane().add(BorderLayout.CENTER, panelDerecha);
		//frame.getContentPane().add(BorderLayout.WEST, panelMenu);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static Posicion convierteAPosicion(int index){
		index = index+1;
		int i,j;
		if(index%Tablero.CASILLAS== 0){
			i = index/Tablero.CASILLAS - 1;
			j = Tablero.CASILLAS - 1;
		}
		else{
			i = index / Tablero.CASILLAS;
			j = (index%(Tablero.CASILLAS))-1;
		}
		return new Posicion(i,j);
	}
	
	public static int convierteAIndice(int i, int j){
		return i*Tablero.CASILLAS + j;
	}

	public static void replace(JPanel panel, int i, JLabel jl){
		panel.remove(i);		
		panel.add(jl,i);
		panel.validate();
		
		
	}
	
	public void inicializaTablero(){
		
		this.actualizaTablero();
		
		/*Iterar sobre lista de agentes*/
		for(int i=0; i < TableroGrafico.listaAgentesGraficos.size(); i++){
			AgenteGrafico temp = TableroGrafico.listaAgentesGraficos.get(i);
			replace(panelMatriz, convierteAIndice(temp.getPosicion().getI(), temp.getPosicion().getJ()), temp);
		}
		
		/*Iterar sobre lista de obstaculos*/
		for(int i=0; i < TableroGrafico.listaObstaculosGraficos.size(); i++){
			ObstaculoGrafico temp = TableroGrafico.listaObstaculosGraficos.get(i);			
			replace(panelMatriz, convierteAIndice(temp.getPosicion().getI(), temp.getPosicion().getJ()), temp);
		}
		/*Iterar sobre lista de monticulos*/
		for(int i=0; i < TableroGrafico.listaMonticulosGraficos.size(); i++){
			MonticuloGrafico temp = TableroGrafico.listaMonticulosGraficos.get(i);		
			replace(panelMatriz, convierteAIndice(temp.getPosicion().getI(), temp.getPosicion().getJ()), temp);
		}
	}
	
	public void actualizaTablero(){
		/*Insertando nave*/
		replace(panelMatriz, convierteAIndice(TableroGrafico.naveGrafica.getPosicion().getI(), TableroGrafico.naveGrafica.getPosicion().getJ()), TableroGrafico.naveGrafica);
		actualizaPosicionesAgentes();
	}
	public void agregaObjetosAListas(){
		
		/*Agregando nave*/
		TableroGrafico.naveGrafica = new NaveGrafica(Tablero.nave.getId(), Tablero.nave.getPosicion());
		
		/*Agregando agentes*/
		for(int i=0; i < Tablero.listaAgentes.size(); i++){
			Agente temp = Tablero.listaAgentes.get(i);
			TableroGrafico.listaAgentesGraficos.add(new AgenteGrafico(temp.getId()));
		}
		
		/*Agregando obstaculos*/
		for(int i=0; i < Tablero.listaObstaculos.size(); i++){
			Obstaculo temp = Tablero.listaObstaculos.get(i);
			TableroGrafico.listaObstaculosGraficos.add(new ObstaculoGrafico(temp.getId(), temp.getPosicion()));
		}
		/*Agregando monticulos*/
		for(int i=0; i < Tablero.listaMonticulos.size(); i++){
			Monticulo temp = Tablero.listaMonticulos.get(i);
			TableroGrafico.listaMonticulosGraficos.add(new MonticuloGrafico(temp.getId(), temp.getPosicion()));
		}
	}
	
	public static void actualizaPosicionesAgentes(){
		for(int i=0; i < Tablero.listaAgentes.size(); i++){
			
			Posicion posAnterior = TableroGrafico.listaAgentesGraficos.get(i).getPosicion();
			replace(panelMatriz, convierteAIndice(posAnterior.getI(), posAnterior.getJ()), new JLabel(""));
			
			Posicion posNueva = Tablero.listaAgentes.get(i).getPosicion();
			TableroGrafico.listaAgentesGraficos.get(i).setPosicion(Tablero.listaAgentes.get(i).getPosicion());						
			replace(panelMatriz, convierteAIndice(posNueva.getI(), posNueva.getJ()), TableroGrafico.listaAgentesGraficos.get(i));
		}
	}
	
	public static void main(String[] args){
		Tablero tb = new Tablero();
		System.out.println(tb.toString());
		TableroGrafico tg = new TableroGrafico();
		int x = 0;
		while(x<1000){
			int[] capas = {1,2,3,4};
			Tablero.listaAgentes.get(0).actuar(capas);
			tg.actualizaTablero();
			x++;
		}
		
	}		
}

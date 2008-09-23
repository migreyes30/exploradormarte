package mx.itesm.cem.grafico;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import mx.itesm.cem.explorador.Agente;
import mx.itesm.cem.explorador.Monticulo;
import mx.itesm.cem.explorador.Nave;
import mx.itesm.cem.explorador.Obstaculo;
import mx.itesm.cem.explorador.Posicion;
import mx.itesm.cem.explorador.Tablero;
import mx.itesm.cem.explorador.exception.NoExisteElementoException;


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
			JLabel x = new JLabel("" + j);
			x.setSize(40,40);
			panelMatriz.add(x);
		}
		
		agregaObjetosAListas();
		
		panelDerecha.add(labelFondo, new Integer(1));
		panelDerecha.add(panelMatriz, new Integer(2));
		
		actualizaTablero();
		
		frame.getContentPane().add(BorderLayout.CENTER, panelDerecha);
		//frame.getContentPane().add(BorderLayout.WEST, panelMenu);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public Posicion convierteAPosicion(int i){
		int x,y;
		if(i%Tablero.CASILLAS == 0){
			y = i/Tablero.CASILLAS;
			x = Tablero.CASILLAS - 1;
		}
		else{
			y = (i / Tablero.CASILLAS) + 1;
			x = (i%Tablero.CASILLAS);
		}
		return new Posicion(x,y);
	}
	
	public int convierteAIndice(int x, int y){
		return x + y*Tablero.CASILLAS;
	}

	public void replace(JPanel panel, int i, JLabel jl){
		panel.remove(i);
		panel.add(jl,i);
	}
	
	public void actualizaTablero(){
		
		/*Insertando nave*/
		System.out.println("Encontre nave con id: " + TableroGrafico.naveGrafica.getId() + " en " + TableroGrafico.naveGrafica.getPosicion().getX() + ", " + TableroGrafico.naveGrafica.getPosicion().getY());
		replace(panelMatriz, convierteAIndice(TableroGrafico.naveGrafica.getPosicion().getX(), TableroGrafico.naveGrafica.getPosicion().getY()), TableroGrafico.naveGrafica);
		
		actualizaPosicionesAgentes();
		
		/*Iterar sobre lista de agentes*/
		for(int i=0; i < TableroGrafico.listaAgentesGraficos.size(); i++){
			AgenteGrafico temp = TableroGrafico.listaAgentesGraficos.get(i);
			System.out.println("Encontre agente con id: " + temp.getId() + " en " + temp.getPosicion().getX() + ", " + temp.getPosicion().getY());
			replace(panelMatriz, convierteAIndice(temp.getPosicion().getX(), temp.getPosicion().getY()), temp);
		}
		
		/*Iterar sobre lista de obstaculos*/
		for(int i=0; i < TableroGrafico.listaObstaculosGraficos.size(); i++){
			ObstaculoGrafico temp = TableroGrafico.listaObstaculosGraficos.get(i);
			System.out.println("Encontre obstaculo con id: " + temp.getId() + " en " + temp.getPosicion().getX() + ", " + temp.getPosicion().getY());
			replace(panelMatriz, convierteAIndice(temp.getPosicion().getX(), temp.getPosicion().getY()), temp);
		}
		/*Iterar sobre lista de monticulos*/
		for(int i=0; i < TableroGrafico.listaMonticulosGraficos.size(); i++){
			MonticuloGrafico temp = TableroGrafico.listaMonticulosGraficos.get(i);
			System.out.println("Encontre monticulo con id: " + temp.getId() + " en " + temp.getPosicion().getX() + ", " + temp.getPosicion().getY());
			replace(panelMatriz, convierteAIndice(temp.getPosicion().getX(), temp.getPosicion().getY()), temp);
		}
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
			TableroGrafico.listaAgentesGraficos.get(i).setPosicion(
					Tablero.listaAgentes.get(i).getPosicion());
		}
	}
	
	public static void main(String[] args){
		Tablero tb = new Tablero();
		System.out.println(tb.toString());
		TableroGrafico tg = new TableroGrafico();
		int x = 0;
		while(x < 10){
			tb.listaAgentes.get(0).caminar();
			tg.actualizaTablero();
			x++;
		}
		
	}		
}

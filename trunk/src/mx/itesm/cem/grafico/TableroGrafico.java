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



public class TableroGrafico{
	
	public static JPanel panelMatriz,panelPiedras;
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
		frame.setSize(650,670);
		
		panelMenu = new JPanel();
		panelDerecha = new JLayeredPane();
		
		panelMatriz = new JPanel();
		panelPiedras = new JPanel();
		
		frame.getContentPane().setLayout(new BorderLayout());		
		panelMatriz.setLayout(new GridLayout(15,15));
		panelPiedras.setLayout(new GridLayout(15,15));
		
		ImageIcon fondo = new ImageIcon(getClass().getResource("Mars_atmosphere.jpg"));
		JLabel labelFondo = new JLabel(fondo);
		labelFondo.setSize(fondo.getIconWidth(), fondo.getIconHeight());
		
		panelMenu.setBackground(new Color(255,0,0));
		panelMenu.setSize(200,600);
		panelMatriz.setOpaque(false);
		panelMatriz.setSize(630, 630);
		
		panelPiedras.setOpaque(false);
		panelPiedras.setSize(panelMatriz.getSize());
		
		for(int j=0; j < 225; j++){
			JLabel x = new JLabel("");
			JLabel y = new JLabel("");
			x.setSize(40,40);
			panelMatriz.add(x);
			panelPiedras.add(y);
		}
		
		agregaObjetosAListas();
		
		panelDerecha.add(labelFondo, new Integer(1));
		panelDerecha.add(panelMatriz, new Integer(2));
		panelDerecha.add(panelPiedras,new Integer(3));
		
		inicializaTablero();
		
		frame.getContentPane().add(BorderLayout.CENTER, panelDerecha);
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

	public synchronized static void replace(JPanel panel, int i, JLabel jl){
		panel.remove(i);		
		panel.add(jl,i);
		panel.validate();

	}
	
	public void inicializaTablero(){
		
		/*Insertando nave*/
		replace(panelMatriz, convierteAIndice(TableroGrafico.naveGrafica.getPosicion().getI(), TableroGrafico.naveGrafica.getPosicion().getJ()), TableroGrafico.naveGrafica);
		
		/*Iterar sobre lista de agentes*/
		for(int i=0; i < TableroGrafico.listaAgentesGraficos.size(); i++){
			AgenteGrafico temp = TableroGrafico.listaAgentesGraficos.get(i);
			replace(panelMatriz, convierteAIndice(temp.getPosicion().getI(), temp.getPosicion().getJ()), temp);
			replace(panelPiedras, convierteAIndice(temp.getPosicion().getI(), temp.getPosicion().getJ()), new JLabel(Tablero.listaAgentes.get(i).getCargaActual()+""));
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
			replace(panelPiedras, convierteAIndice(temp.getPosicion().getI(), temp.getPosicion().getJ()), new JLabel(Tablero.listaMonticulos.get(i).getPiedras()+""));
		}
	}
	
	public synchronized void agregaObjetosAListas(){
		
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
	
	public synchronized static void actualizaPosicionAgente(String idAgente){
		int indiceAgente = Tablero.obtenerIndiceDeObjeto(idAgente);
		
		Posicion posAnterior = TableroGrafico.listaAgentesGraficos.get(indiceAgente).getPosicion();
		replace(panelMatriz, convierteAIndice(posAnterior.getI(), posAnterior.getJ()), new JLabel(""));
		
		replace(panelPiedras, convierteAIndice(posAnterior.getI(), posAnterior.getJ()), new JLabel(""));
		
		Posicion posNueva = Tablero.listaAgentes.get(indiceAgente).getPosicion();
		TableroGrafico.listaAgentesGraficos.get(indiceAgente).setPosicion(Tablero.listaAgentes.get(indiceAgente).getPosicion());						
		replace(panelMatriz, convierteAIndice(posNueva.getI(), posNueva.getJ()), TableroGrafico.listaAgentesGraficos.get(indiceAgente));

		replace(panelPiedras, convierteAIndice(Tablero.listaAgentes.get(indiceAgente).getPosicion().getI(), Tablero.listaAgentes.get(indiceAgente).getPosicion().getJ()), new JLabel(Tablero.listaAgentes.get(indiceAgente).getCargaActual()+""));
		
		/*Insertando nave*/
		replace(panelMatriz, convierteAIndice(TableroGrafico.naveGrafica.getPosicion().getI(), TableroGrafico.naveGrafica.getPosicion().getJ()), TableroGrafico.naveGrafica);
		
		panelMatriz.repaint();
		panelPiedras.repaint();
	}
	
	public synchronized static void quitaMonticulo(String idMonticulo){
		int indiceMonticulo = Tablero.obtenerIndiceDeObjeto(idMonticulo);
		
		Posicion pos = Tablero.listaMonticulos.get(indiceMonticulo).getPosicion();
		replace(panelMatriz, convierteAIndice(pos.getI(), pos.getJ()), new JLabel(""));
		replace(panelPiedras, convierteAIndice(pos.getI(), pos.getJ()), new JLabel(""));
		
		panelPiedras.repaint();
		panelMatriz.repaint();
	}
}
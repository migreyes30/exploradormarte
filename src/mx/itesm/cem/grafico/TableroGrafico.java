package mx.itesm.cem.grafico;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import mx.itesm.cem.explorador.Posicion;
import mx.itesm.cem.explorador.Tablero;


public class TableroGrafico {
	
	public TableroGrafico(){
		JFrame frame = new JFrame("Explorador Marte");
		frame.setSize(800, 635);
		JPanel panelMenu = new JPanel();
		JLayeredPane panelDerecha = new JLayeredPane();
		
		JPanel panelMatriz = new JPanel();
		
		frame.getContentPane().setLayout(new BorderLayout());		
		panelMatriz.setLayout(new GridLayout(15,15));
		
		ImageIcon fondo = new ImageIcon(getClass().getResource("Mars_atmosphere.jpg"));
		JLabel labelFondo = new JLabel(fondo);
		labelFondo.setSize(fondo.getIconWidth(), fondo.getIconHeight());
		
		
		for(int j=0; j < 225; j++){
			
			JLabel x = new JLabel("" + j);
			x.setSize(40,40);
			panelMatriz.add(x);
		}
		
/*		for(int i=0; i < 10; i++){
			int index = (int)(Math.random()*225);
			this.replace(panelMatriz, index, new AgenteGrafico(index, this.convierteAPosicion(index)));
		}
		
*/		
//			AgenteGrafico x = new AgenteGrafico(0, this.convierteAPosicion(4));
//			x.setSize(40,40);
//			x.setBorder(BorderFactory.createLineBorder(Color.black));
//			//panelMatriz.add(new AgenteGrafico(0, this.convierteAPosicion(4)), 2);
//		
		Tablero tb = new Tablero();
		
		for(int j=0; j < Tablero.matriz.length; j++){
			for(int i = 0; i < Tablero.matriz.length; i++){
				if(Tablero.matriz[i][j] == "A"){
					int id = (int)(Math.random()*35536);
					this.replace(panelMatriz, convierteAIndice(i, j), new AgenteGrafico(id, new Posicion(i,j)));;
				}
			}
			
		}
		
		tb.prueba();
		
		panelMenu.setBackground(new Color(255,0,0));
		panelMenu.setSize(200,600);
		panelMatriz.setOpaque(false);
		panelMatriz.setSize(600, 600);
		
		panelDerecha.add(labelFondo, new Integer(1));
		panelDerecha.add(panelMatriz, new Integer(2));
			
		frame.getContentPane().add(BorderLayout.CENTER, panelDerecha);
		//frame.getContentPane().add(BorderLayout.WEST, panelMenu);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
		System.out.println(panelMatriz.getComponents());
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
	
	public static void main(String[] args){
		TableroGrafico tg = new TableroGrafico();
		
	}		
}

package mx.itesm.cem.grafico;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneLayout;

import mx.itesm.cem.explorador.Agente;
import mx.itesm.cem.explorador.Monticulo;
import mx.itesm.cem.explorador.Morona;
import mx.itesm.cem.explorador.Obstaculo;
import mx.itesm.cem.explorador.Posicion;
import mx.itesm.cem.explorador.Tablero;
import mx.itesm.cem.explorador.exception.NoExisteElementoException;



public class TableroGrafico extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	public static JPanel panelMatriz,
						 panelPiedras,
						 panelDerecha;
	
	public static JFrame frame;
	public static JLayeredPane panelTablero;
	public static JScrollPane scroll, scroll2;
	public static JTextArea mensajes, info;
	private JMenuBar barraMenu;
    private JMenu archivo, ayuda;
    private JMenuItem inicio, salir, acerca;
	
	public static NaveGrafica naveGrafica;
	public static ArrayList<AgenteGrafico> listaAgentesGraficos;
	public static ArrayList<MonticuloGrafico> listaMonticulosGraficos;
	public static ArrayList<ObstaculoGrafico> listaObstaculosGraficos;
	public static ArrayList<MoronaGrafica> listaMoronasGraficas;
	
	public TableroGrafico(){
		
		TableroGrafico.listaAgentesGraficos = new ArrayList<AgenteGrafico>();
		TableroGrafico.listaMonticulosGraficos = new ArrayList<MonticuloGrafico>();
		TableroGrafico.listaObstaculosGraficos = new ArrayList<ObstaculoGrafico>();
		TableroGrafico.listaMoronasGraficas = new ArrayList<MoronaGrafica>();
		
		this.setTitle("Explorador Marte");
		this.setSize(910,695);
		
		panelDerecha = new JPanel();
		
		panelTablero = new JLayeredPane();
		
		panelMatriz = new JPanel();
		panelPiedras = new JPanel();
		
		scroll = new JScrollPane();
		
		this.getContentPane().setLayout(new BorderLayout());		
		panelMatriz.setLayout(new GridLayout(15,15));
		panelPiedras.setLayout(new GridLayout(15,15));
		scroll.setLayout(new ScrollPaneLayout());
		panelDerecha.setLayout(new BorderLayout());
		
		ImageIcon fondo = new ImageIcon(getClass().getResource("Mars_atmosphere.jpg"));
		JLabel labelFondo = new JLabel(fondo);
		labelFondo.setSize(fondo.getIconWidth(), fondo.getIconHeight());
		
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
		
		barraMenu = new JMenuBar();

        archivo = new JMenu("Archivo");
        archivo.setMnemonic(KeyEvent.VK_A);
        barraMenu.add(archivo);

        inicio = new JMenuItem("Volver a iniciar");
        inicio.setMnemonic(KeyEvent.VK_I);
        inicio.addActionListener(this);
        archivo.add(inicio);
        
        salir = new JMenuItem("Salir");
        salir.setMnemonic(KeyEvent.VK_S);
        salir.addActionListener(this);
        archivo.add(salir);
        
        ayuda = new JMenu("Ayuda");
        ayuda.setMnemonic(KeyEvent.VK_Y);
        barraMenu.add(ayuda);
        
        acerca = new JMenuItem("Acerca de...");
        acerca.setMnemonic(KeyEvent.VK_L);
        acerca.addActionListener(this);
        ayuda.add(acerca);
        
        this.setJMenuBar(barraMenu);
        
		panelTablero.add(labelFondo, new Integer(1));
		panelTablero.add(panelMatriz, new Integer(2));
		panelTablero.add(panelPiedras,new Integer(3));
		
		inicializaTablero();
		
		info = new JTextArea();
		info.append("\t<<INFORMACION>>\n");
		info.append("* Orden de capas\n");
		info.append("     1. " + Tablero.nombresCapas.get(Tablero.capas[0]) + "\n");
		info.append("     2. " + Tablero.nombresCapas.get(Tablero.capas[1]) + "\n");
		info.append("     3. " + Tablero.nombresCapas.get(Tablero.capas[2]) + "\n");
		info.append("     4. " + Tablero.nombresCapas.get(Tablero.capas[3]) + "\n");
		info.append("     5. " + Tablero.nombresCapas.get(Tablero.capas[4]) + "\n");
		info.append("* Numero de agentes: " + Tablero.listaAgentes.size() + "\n");
		info.append("* Numero de monticulos: " + Tablero.listaMonticulos.size() + "\n");
		info.append("* Piedras totales: " + Tablero.totalPiedras + "\n");
//		info.append("Piedras restantes: " + (Tablero.totalPiedras - Tablero.nave.getPiedras()) + "\n");
		info.append("\n <<MENSAJES>>");
		info.setEditable(false);
		info.setBackground(this.getBackground());
		info.setFont(new Font("Arial", Font.BOLD, 12));
		
		mensajes = new JTextArea();
		mensajes.setFont(new Font("Arial",Font.PLAIN, 11));
		mensajes.setForeground(new Color(0, 0, 255));
		mensajes.setEditable(false);
		
		
		scroll.setViewportView(mensajes);
		scroll.setPreferredSize(new Dimension(260,100));
		
		panelDerecha.add(BorderLayout.NORTH, info);
		panelDerecha.add(BorderLayout.CENTER, scroll);
		
		this.getContentPane().add(BorderLayout.CENTER, panelTablero);
		this.getContentPane().add(BorderLayout.EAST, panelDerecha);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
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
		
		/*Agregando monticulos*/
		for(int i=0; i < Tablero.listaMonticulos.size(); i++){
			Monticulo temp = Tablero.listaMonticulos.get(i);
			TableroGrafico.listaMonticulosGraficos.add(new MonticuloGrafico(temp.getId(), temp.getPosicion()));
		}
		
		/*Agregando obstaculos*/
		for(int i=0; i < Tablero.listaObstaculos.size(); i++){
			Obstaculo temp = Tablero.listaObstaculos.get(i);
			TableroGrafico.listaObstaculosGraficos.add(new ObstaculoGrafico(temp.getId(), temp.getPosicion()));
		}
		
		/*Agregando agentes*/
		for(int i=0; i < Tablero.listaAgentes.size(); i++){
			Agente temp = Tablero.listaAgentes.get(i);
			TableroGrafico.listaAgentesGraficos.add(new AgenteGrafico(temp.getId(), temp.getPosicion()));
		}
	}
	
	public synchronized static void actualizaPosicionAgente(String idAgente){
		
		try{
			int indiceAgente = Tablero.obtenerIndiceDeObjeto(idAgente);
			
			Agente a = Tablero.listaAgentes.get(indiceAgente);
			
			Posicion posAnterior = TableroGrafico.listaAgentesGraficos.get(indiceAgente).getPosicion();
			if(!a.dejarMoronas)
				replace(panelMatriz, convierteAIndice(posAnterior.getI(), posAnterior.getJ()), new JLabel(""));
			
			replace(panelPiedras, convierteAIndice(posAnterior.getI(), posAnterior.getJ()), new JLabel(""));
			
			Posicion posNueva = Tablero.listaAgentes.get(indiceAgente).getPosicion();
			TableroGrafico.listaAgentesGraficos.get(indiceAgente).setPosicion(Tablero.listaAgentes.get(indiceAgente).getPosicion());						
			replace(panelMatriz, convierteAIndice(posNueva.getI(), posNueva.getJ()), TableroGrafico.listaAgentesGraficos.get(indiceAgente));
	
			replace(panelPiedras, convierteAIndice(Tablero.listaAgentes.get(indiceAgente).getPosicion().getI(), Tablero.listaAgentes.get(indiceAgente).getPosicion().getJ()), new JLabel(Tablero.listaAgentes.get(indiceAgente).getCargaActual()+""));
		}catch(NoExisteElementoException e){
			e.printStackTrace();
		}
		/*Insertando nave*/
		replace(panelMatriz, convierteAIndice(TableroGrafico.naveGrafica.getPosicion().getI(), TableroGrafico.naveGrafica.getPosicion().getJ()), TableroGrafico.naveGrafica);
		
		panelMatriz.repaint();
		panelPiedras.repaint();
	}
	
	public synchronized static void quitaMonticulo(String idMonticulo){
		try{
			int indiceMonticulo = Tablero.obtenerIndiceDeObjeto(idMonticulo);
			
			Posicion pos = Tablero.listaMonticulos.get(indiceMonticulo).getPosicion();
			replace(panelMatriz, convierteAIndice(pos.getI(), pos.getJ()), new JLabel(""));
			replace(panelPiedras, convierteAIndice(pos.getI(), pos.getJ()), new JLabel(""));
		}catch(NoExisteElementoException e){
			e.printStackTrace();
		}
		panelPiedras.repaint();
		panelMatriz.repaint();
	}
	
	public synchronized static void agregaMoronaGrafica(String idMorona){
		Morona temp = (Morona) Tablero.obtenerElementoConId(idMorona);
		MoronaGrafica nuevaMoronaGrafica = new MoronaGrafica(temp.getId(), temp.getPosicion());
		TableroGrafico.listaMoronasGraficas.add(nuevaMoronaGrafica);
		replace(panelMatriz, convierteAIndice(nuevaMoronaGrafica.getPosicion().getI(), 
				nuevaMoronaGrafica.getPosicion().getJ()), nuevaMoronaGrafica);
	}
	
	public synchronized static void quitaMoronaGrafica(String idMorona){
		try{
			int indiceMorona = Tablero.obtenerIndiceDeObjeto(idMorona);
			Posicion pos = Tablero.listaMoronas.get(indiceMorona).getPosicion();
			replace(panelMatriz, convierteAIndice(pos.getI(), pos.getJ()), new JLabel(""));
		}catch(NoExisteElementoException e){
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == salir){
			int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro deseas cerrar la aplicación?");
			if(opcion == JOptionPane.YES_OPTION){
				System.exit(0);
			}
			
		}
		
		if(e.getSource() == inicio){
			int opcion = JOptionPane.showConfirmDialog(this, "Se cerrará esta simulacion y volverás al inicio. ¿Estás seguro?");
			if(opcion == JOptionPane.YES_OPTION){
				new PantallaDeBienvenida();
				this.setVisible(false);
			}
		}
		
		if(e.getSource() == acerca){
			String message = "Explorador de Marte \n (C) 2008" +
				"\nAutores: " + 
				"\n*Eduardo Fuentes Martínez" + 
				"\n*Jonathan Fragoso Martínez" + 
				"\n*Maricela Obeso Pulido" + 
				"\n*Miguel Angel Ramírez Reyes\n" + 
				"\n ITESM CEM." +
				"\n";
			JOptionPane.showMessageDialog(this, message);
			
		}
		
	}
}
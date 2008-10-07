package mx.itesm.cem.grafico;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import mx.itesm.cem.explorador.Tablero;
import mx.itesm.cem.explorador.ThreadAgente;


public class PantallaDeBienvenida extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private static final int ANCHO = 371;
	private static final int ALTO = 594;
	private static final int LIMITE_CARACTERES = 6;
	private static final int ANCHO_TEXTFIELD = 50;
	private static final int ALTO_TEXTFIELD = 20;
	private static final int NUM_CAPAS = 4;
	private Box tituloBox, opcionesBox, entradasBox, capasLeftBox, capasRightBox, botonesBox ;
	public JPanel botonesPanel;
	public JButton aceptarButton, borrarButton;
	public JTextField monticulosTxt, obstaculosTxt, agentesTxt  = new JTextField("");
	public int numPiedras, numMonticulos, numObstaculos, numAgentes;
	public boolean datosValidos;
	private JComboBox ordenEvitarObstaculos;
	private JComboBox ordenLlevarPiedras;
	private JComboBox ordenRecolectarMuestras;
	private JComboBox ordenExplorar;
	
	
	public PantallaDeBienvenida(){
					
		this.setTitle("Bienvenido al explorador de marte");
		this.setSize(ANCHO, ALTO);
		this.setMaximumSize(new Dimension(ANCHO,ALTO));
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.tituloBox = Box.createHorizontalBox();
		this.opcionesBox = Box.createVerticalBox();
		this.entradasBox = Box.createVerticalBox();
		this.botonesBox = Box.createHorizontalBox();
		this.capasLeftBox = Box.createVerticalBox();
		this.capasRightBox = Box.createVerticalBox();
		
		this.botonesPanel = new JPanel();
		
		JLabel tituloLabel = new JLabel();
		ImageIcon tituloIcon = new ImageIcon(getClass().getResource("bienvenido.png"));
		tituloLabel.setIcon(tituloIcon);
				
			
		JLabel monticulosLabel = new JLabel("*Cantidad de montículos: ");
		this.monticulosTxt = new JTextField();
		this.monticulosTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		this.monticulosTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
			
		JLabel obstaculosLabel = new JLabel("*Cantidad de obstáculos: ");
		this.obstaculosTxt = new JTextField();
		this.obstaculosTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		this.obstaculosTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		
		JLabel exploradoresLabel = new JLabel("*Cantidad de exploradores: ");
		this.agentesTxt = new JTextField(); 
		agentesTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		agentesTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		
			
		this.tituloBox.add(tituloLabel);
		
		this.aceptarButton = new JButton("Aceptar");
		this.aceptarButton.addActionListener(this);
		
		this.borrarButton = new JButton("Limpiar");
		this.borrarButton.addActionListener(this);
				
			
		this.opcionesBox.add(monticulosLabel);
		this.opcionesBox.add(Box.createRigidArea(new Dimension(20,45)));
		this.opcionesBox.add(obstaculosLabel);
		this.opcionesBox.add(Box.createRigidArea(new Dimension(20,45)));
		this.opcionesBox.add(exploradoresLabel);
		this.opcionesBox.add(Box.createRigidArea(new Dimension(20,45)));
		this.opcionesBox.add(new JLabel("*Ordena las capas: "));
		
				
		this.entradasBox.add(monticulosTxt);
		this.entradasBox.add(Box.createRigidArea(new Dimension(20,40)));
		this.entradasBox.add(obstaculosTxt);
		this.entradasBox.add(Box.createRigidArea(new Dimension(20,40)));
		this.entradasBox.add(agentesTxt);
		
		
		this.botonesBox.add(aceptarButton);
		this.botonesBox.add(Box.createRigidArea(new Dimension(20,40)));
		this.botonesBox.add(borrarButton);
		
		this.botonesPanel.add(botonesBox, BorderLayout.CENTER);
		
				
		String[] ordenCapaArray =  {"1","2","3","4"};
		
		ordenEvitarObstaculos = new JComboBox(ordenCapaArray);
		ordenEvitarObstaculos.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		
		
		ordenLlevarPiedras = new JComboBox(ordenCapaArray);
		ordenLlevarPiedras.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		ordenLlevarPiedras.setSelectedIndex(1);
		
		ordenRecolectarMuestras = new JComboBox(ordenCapaArray);
		ordenRecolectarMuestras.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		ordenRecolectarMuestras.setSelectedIndex(2);
		
		ordenExplorar = new JComboBox(ordenCapaArray);
		ordenExplorar.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		ordenExplorar.setSelectedIndex(3);
		
		
		JLabel evitarObstaculosLbl = new JLabel("1. Evitar obstáculos: ");
		JLabel llevarPiedrasLbl = new JLabel("2. Llevar piedras: ");
		JLabel recolectarMuestrasLbl = new JLabel("3. Recolectar piedras: ");
		JLabel explorarLbl = new JLabel("4. Explorar: ");
		
		
		this.capasLeftBox.add(evitarObstaculosLbl);
		this.capasLeftBox.add(Box.createRigidArea(new Dimension(20,45)));
		this.capasLeftBox.add(llevarPiedrasLbl);
		this.capasLeftBox.add(Box.createRigidArea(new Dimension(20,45)));
		this.capasLeftBox.add(recolectarMuestrasLbl);
		this.capasLeftBox.add(Box.createRigidArea(new Dimension(20,45)));
		this.capasLeftBox.add(explorarLbl);
		
		
		this.capasRightBox.add(Box.createVerticalStrut(20));
		this.capasRightBox.add(ordenEvitarObstaculos);
		this.capasRightBox.add(Box.createVerticalStrut(40));
		this.capasRightBox.add(ordenLlevarPiedras);
		this.capasRightBox.add(Box.createVerticalStrut(40));
		this.capasRightBox.add(ordenRecolectarMuestras);
		this.capasRightBox.add(Box.createVerticalStrut(40));
		this.capasRightBox.add(ordenExplorar);
		
		
		JPanel tituloPanel = new JPanel();
		tituloPanel.add(tituloBox);
				
		JPanel etiquetasEntradasPanel = new JPanel();
		etiquetasEntradasPanel.add(opcionesBox);
		
		JPanel camposDeTextoPanel = new JPanel();
		camposDeTextoPanel.add(entradasBox);		


		JPanel capasPanel = new JPanel();
		capasPanel.add(capasLeftBox, BorderLayout.WEST);
		capasPanel.add(capasRightBox, BorderLayout.CENTER);
		
		JPanel parametrosPanel = new JPanel();
		parametrosPanel.add(tituloPanel, BorderLayout.NORTH);
		parametrosPanel.add(etiquetasEntradasPanel, BorderLayout.WEST);
		parametrosPanel.add(camposDeTextoPanel, BorderLayout.CENTER);
		parametrosPanel.add(capasPanel, BorderLayout.SOUTH);
		
		
		this.add(parametrosPanel, BorderLayout.CENTER);
		this.add(botonesPanel, BorderLayout.SOUTH);
				
		this.setVisible(true);

	}
	
	/**
	 * Metodo que valida que no haya capas
	 * con el mismo orden de prioridad
	 * 
	 * @return si se han ordenada correctamente
	 * 		   las capas o no
	 */
	public boolean validarCapas(int[] capas){
		

		
		/*Representan un contador para ver cuantas capas se encuentran
		 * de cada prioridad, solo se acepta 1 de cada prioridad
		 */
		int capaUno = 0;
		int capaDos = 0;
		int capaTres = 0;
		int capaCuatro = 0;
		
		for(int capa : capas){
			switch (capa) {
				case 1: capaUno++;
						break;
				case 2:	capaDos++;
						break;
				case 3:	capaTres++;
						break;
				case 4: capaCuatro++;
						break;
			}
		}		
		 //Solo debe haber un orden asignada a cada capa, dos capas no pueden tener el mismo orden de importancias
		return capaUno == 1 && capaDos == 1 && capaTres == 1 && capaCuatro == 1;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == this.aceptarButton){
		
			if(this.monticulosTxt.getText().length() > 0 
					&& this.obstaculosTxt.getText().length() > 0 && this.agentesTxt.getText().length() > 0){
				
				int[] capasIntroducidas = new int[NUM_CAPAS];
				capasIntroducidas[0] = Integer.parseInt(ordenEvitarObstaculos.getSelectedItem().toString());
				capasIntroducidas[1] = Integer.parseInt(ordenLlevarPiedras.getSelectedItem().toString());
				capasIntroducidas[2] = Integer.parseInt(ordenRecolectarMuestras.getSelectedItem().toString());
				capasIntroducidas[3]= Integer.parseInt(ordenExplorar.getSelectedItem().toString());
				
				if(this.validarCapas(capasIntroducidas)){
					numMonticulos = Integer.parseInt(this.monticulosTxt.getText());
					numObstaculos = Integer.parseInt(this.obstaculosTxt.getText());
					numAgentes = Integer.parseInt(this.agentesTxt.getText());
					
					this.setVisible(false);
					
					Tablero tb = new Tablero(numMonticulos, numObstaculos, numAgentes);
					System.out.println(tb.toString());
							
					@SuppressWarnings("unused")
					TableroGrafico tg = new TableroGrafico();
					for(int i=0; i < Tablero.listaAgentes.size(); i++)
						new ThreadAgente(Tablero.listaAgentes.get(i), capasIntroducidas );
				}else{
					JOptionPane.showMessageDialog(this, "Dos capas no puden tener la misma prioridad", "Orden de capas", JOptionPane.ERROR_MESSAGE);	
				}
				
			} else {
				JOptionPane.showMessageDialog(this, "Debes llenar todos los campos", "Campos vacíos", JOptionPane.ERROR_MESSAGE);
			}
						
		}else if (e.getSource() == this.borrarButton){
			
			this.monticulosTxt.setText("");
			this.obstaculosTxt.setText("");
			this.agentesTxt.setText("");			
		}	
	}

	public static void main(String[] args) {
			@SuppressWarnings("unused")
			PantallaDeBienvenida bienvenida = new PantallaDeBienvenida();
	}
}
package mx.itesm.cem.grafico;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import mx.itesm.cem.explorador.AgenteCargador;
import mx.itesm.cem.explorador.AgenteEspecial;
import mx.itesm.cem.explorador.Tablero;
import mx.itesm.cem.explorador.ThreadAgente;


public class PantallaExploradores extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private static final int ANCHO = 371;
	private static final int ALTO = 740;
	private static final int LIMITE_CARACTERES = 6;
	private static final int ANCHO_TEXTFIELD = 50;
	private static final int ALTO_TEXTFIELD = 20;
	private static final int NUM_CAPAS = 4;
	private Box tituloBox, opcionesBox, entradasBox, capasLeftBox, capasCenterBox, botonesBox ;
	public JPanel botonesPanel, parametrosPanel, capasPanel;
	public JButton aceptarButton, borrarButton;
	public JTextField monticulosTxt, obstaculosTxt, cargadoresTxt, exploradoresTxt;
	public JLabel ordenaCapasLbl;
	public int numPiedras, numMonticulos, numObstaculos, numCargadores, numExploradores;
	public boolean datosValidos, agenteEspecial;
	private JComboBox ordenEvitarObstaculos, ordenContratar, ordenInformar, ordenExplorar;
	private int[] capasIntroducidasCargador;
	
	public PantallaExploradores(int[] capasIntroducidasCargador, int numMonticulos, int numObstaculos, int numCargadores, int numExploradores, boolean agenteEspecial){
					
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
		this.capasCenterBox = Box.createVerticalBox();
		
		
		this.botonesPanel = new JPanel();
		
		JLabel tituloLabel = new JLabel();
		ImageIcon tituloIcon = new ImageIcon(getClass().getResource("bienvenido.png"));
		tituloLabel.setIcon(tituloIcon);
				
			
		JLabel monticulosLabel = new JLabel("*Cantidad de montículos: ");
		this.monticulosTxt = new JTextField();
		this.monticulosTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		this.monticulosTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		this.monticulosTxt.setText(numMonticulos + "");
		
			
		JLabel obstaculosLabel = new JLabel("*Cantidad de obstáculos: ");
		this.obstaculosTxt = new JTextField();
		this.obstaculosTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		this.obstaculosTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		this.obstaculosTxt.setText(numObstaculos + "");
		
		
		JLabel cargadoresLbl = new JLabel("*Cantidad de cargadores: ");
		this.cargadoresTxt = new JTextField(); 
		this.cargadoresTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		this.cargadoresTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		this.cargadoresTxt.setText(numCargadores + "");
		
		JLabel exploradoresLbl = new JLabel("*Cantidad de exploradores: ");
		this.exploradoresTxt = new JTextField(); 
		this.exploradoresTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		this.exploradoresTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		this.exploradoresTxt.setText(numExploradores + "");
		
			
		this.tituloBox.add(tituloLabel);
		
		this.aceptarButton = new JButton("Aceptar");
		this.aceptarButton.addActionListener(this);
		
		this.borrarButton = new JButton("Limpiar");
		this.borrarButton.addActionListener(this);
				
			
		this.opcionesBox.add(monticulosLabel);
		this.opcionesBox.add(Box.createRigidArea(new Dimension(20,45)));
		this.opcionesBox.add(obstaculosLabel);
		this.opcionesBox.add(Box.createRigidArea(new Dimension(20,45)));
		this.opcionesBox.add(cargadoresLbl);
		this.opcionesBox.add(Box.createRigidArea(new Dimension(20,45)));
		this.opcionesBox.add(exploradoresLbl);
		this.opcionesBox.add(Box.createRigidArea(new Dimension(20,35)));
		
		this.ordenaCapasLbl = new JLabel("*Ordena las capas del explorador");
		this.opcionesBox.add(ordenaCapasLbl);
						
		this.entradasBox.add(monticulosTxt);
		this.entradasBox.add(Box.createRigidArea(new Dimension(20,40)));
		this.entradasBox.add(obstaculosTxt);
		this.entradasBox.add(Box.createRigidArea(new Dimension(20,40)));
		this.entradasBox.add(cargadoresTxt);
		this.entradasBox.add(Box.createRigidArea(new Dimension(20,40)));
		this.entradasBox.add(exploradoresTxt);
		
		
		this.botonesBox.add(aceptarButton);
		this.botonesBox.add(Box.createRigidArea(new Dimension(20,40)));
		this.botonesBox.add(borrarButton);
		
		this.botonesPanel.add(botonesBox, BorderLayout.CENTER);
		
		String[] ordenCapaArray = { "1", "2", "3", "4" };
		ordenEvitarObstaculos = new JComboBox(ordenCapaArray);
		ordenEvitarObstaculos.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		
		ordenContratar = new JComboBox(ordenCapaArray);
		ordenContratar.setMaximumSize(new Dimension(ANCHO_TEXTFIELD,ALTO_TEXTFIELD));
		ordenContratar.setSelectedIndex(1);
		
		ordenInformar = new JComboBox(ordenCapaArray);
		ordenInformar.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		ordenInformar.setSelectedIndex(2);
		
		ordenExplorar = new JComboBox(ordenCapaArray);
		ordenExplorar.setMaximumSize(new Dimension( ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		ordenExplorar.setSelectedIndex(3);
		
				
		JLabel evitarObstaculosLbl = new JLabel("1. Evitar obstáculos: ");
		JLabel contratarLbl = new JLabel("2. Contratar: ");
		JLabel informarLbl = new JLabel("3. Informar: ");
		JLabel explorarLbl = new JLabel("4. Explorar: ");
		
		this.capasLeftBox.add(evitarObstaculosLbl);
		this.capasLeftBox.add(Box.createRigidArea(new Dimension(20, 40)));
		this.capasLeftBox.add(contratarLbl);
		this.capasLeftBox.add(Box.createRigidArea(new Dimension(20, 40)));
		this.capasLeftBox.add(informarLbl);
		this.capasLeftBox.add(Box.createRigidArea(new Dimension(20, 40)));
		this.capasLeftBox.add(explorarLbl);
		
		this.capasCenterBox.add(Box.createVerticalStrut(28));
		this.capasCenterBox.add(ordenEvitarObstaculos);
		this.capasCenterBox.add(Box.createVerticalStrut(35));
		this.capasCenterBox.add(ordenContratar);
		this.capasCenterBox.add(Box.createVerticalStrut(35));
		this.capasCenterBox.add(ordenInformar);
		this.capasCenterBox.add(Box.createVerticalStrut(35));
		this.capasCenterBox.add(ordenExplorar);
		
		JPanel tituloPanel = new JPanel();
		tituloPanel.add(tituloBox);
				
		JPanel etiquetasEntradasPanel = new JPanel();
		etiquetasEntradasPanel.add(opcionesBox);
		
		JPanel camposDeTextoPanel = new JPanel();
		camposDeTextoPanel.add(entradasBox);
		

		capasPanel = new JPanel();
		capasPanel.add(capasLeftBox, BorderLayout.WEST);
		capasPanel.add(capasCenterBox, BorderLayout.CENTER);
				
		parametrosPanel = new JPanel();
		parametrosPanel.add(tituloPanel, BorderLayout.NORTH);
		parametrosPanel.add(etiquetasEntradasPanel, BorderLayout.WEST);
		parametrosPanel.add(camposDeTextoPanel, BorderLayout.CENTER);		
		parametrosPanel.add(capasPanel, BorderLayout.SOUTH);
		
		
		this.add(parametrosPanel, BorderLayout.CENTER);
		this.add(botonesPanel, BorderLayout.SOUTH);	
		
		this.capasIntroducidasCargador = capasIntroducidasCargador;
		this.agenteEspecial = agenteEspecial;
				
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
			}
		}		
		 //Solo debe haber un orden asignada a cada capa, dos capas no pueden tener el mismo orden de importancias
		return capaUno == 1 && capaDos == 1 && capaTres == 1 && capaCuatro == 1;
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == this.aceptarButton){
		
			if(this.monticulosTxt.getText().length() > 0 
					&& this.obstaculosTxt.getText().length() > 0 && this.cargadoresTxt.getText().length() > 0 
					&& this.exploradoresTxt.getText().length() > 0){
				
				int[] capasIntroducidasExplorador = new int[NUM_CAPAS];
				capasIntroducidasExplorador[Integer.parseInt(ordenEvitarObstaculos.getSelectedItem().toString())- 1] = 1;  	// 1 = Evitar Obstaculo
				capasIntroducidasExplorador[Integer.parseInt(ordenContratar.getSelectedItem().toString())- 1] = 2; 		// 2 = Contratar			
				capasIntroducidasExplorador[Integer.parseInt(ordenInformar.getSelectedItem().toString()) - 1] = 3;		// 3 = Informar
				capasIntroducidasExplorador[Integer.parseInt(ordenExplorar.getSelectedItem().toString()) - 1] = 4; 	// 4 = Explorar
				
				if(this.validarCapas(capasIntroducidasExplorador)){
					
					numMonticulos = Integer.parseInt(this.monticulosTxt.getText());
					numObstaculos = Integer.parseInt(this.obstaculosTxt.getText());
					numCargadores = Integer.parseInt(this.cargadoresTxt.getText());
					numExploradores = Integer.parseInt(this.exploradoresTxt.getText());
					
					if(numMonticulos == 0 || numCargadores == 0 || numExploradores == 0) {
						JOptionPane.showMessageDialog(this, "Debes introducir minimo 1 explorador, 1 cargador y 1 monticulo", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					else if((numMonticulos + numObstaculos + numCargadores + numExploradores) >= 225)
						JOptionPane.showMessageDialog(this, "Introdujiste demasiados elementos. La suma de obstaculos, agentes y monticulos debe ser maximo de 224 elementos",
														"Error", JOptionPane.ERROR_MESSAGE);
					else{
						this.setVisible(false);
						
						Tablero tb = new Tablero(numMonticulos, numObstaculos, numCargadores, numExploradores, 0, true, agenteEspecial);
						//TODO: Solo se despliegan las capas del cargador
						Tablero.capas = capasIntroducidasCargador;
						System.out.println(tb.toString());
						
						TableroGrafico tg = new TableroGrafico();
						for(int i=0; i < Tablero.listaAgentes.size(); i++){
							if (Tablero.listaAgentes.get(i) instanceof AgenteCargador || Tablero.listaAgentes.get(i) instanceof AgenteEspecial ){
								new ThreadAgente(Tablero.listaAgentes.get(i), capasIntroducidasCargador, tg);	
							} else {
								new ThreadAgente(Tablero.listaAgentes.get(i), capasIntroducidasExplorador, tg);
							}
						}
							
					}
				} else{
					JOptionPane.showMessageDialog(this, "Dos capas no puden tener la misma prioridad", "Orden de capas", JOptionPane.ERROR_MESSAGE);	
				}
				
			} else {
				JOptionPane.showMessageDialog(this, "Debes llenar todos los campos", "Campos vacíos", JOptionPane.ERROR_MESSAGE);
			}
						
		} else if (e.getSource() == this.borrarButton){
			
			this.monticulosTxt.setText("");
			this.obstaculosTxt.setText("");
			this.cargadoresTxt.setText("");
			this.exploradoresTxt.setText("");
			
		}  
	}
}
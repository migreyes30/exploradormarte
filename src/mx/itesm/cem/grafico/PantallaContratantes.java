package mx.itesm.cem.grafico;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class PantallaContratantes extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private static final int ANCHO = 371;
	private static final int ALTO = 740;
	private static final int LIMITE_CARACTERES = 6;
	private static final int ANCHO_TEXTFIELD = 50;
	private static final int ALTO_TEXTFIELD = 20;
	private static final int NUM_CAPAS = 6;
	private Box tituloBox, opcionesBox, entradasBox, capasLeftBox, capasCenterBox, capasRightBox, botonesBox ;
	private JCheckBox usarAgenteEspecial;
	public JPanel botonesPanel, parametrosPanel, capasPanel;
	public JButton aceptarButton, borrarButton;
	public JTextField monticulosTxt, obstaculosTxt, cargadoresTxt, exploradoresTxt;
	public JLabel ordenaCapasLbl;
	public int numPiedras, numMonticulos, numObstaculos, numCargadores, numExploradores;
	public boolean datosValidos;
	private JComboBox ordenEvitarObstaculos, ordenLlevarPiedras, 
					  ordenRecolectarMuestras,ordenCumplirContrato,
					  ordernLeerSolicitudes, ordenExplorar;
	
	public PantallaContratantes(){
					
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
		this.capasRightBox = Box.createVerticalBox();
		
		
		this.botonesPanel = new JPanel();
		
		JLabel tituloLabel = new JLabel();
		ImageIcon tituloIcon = new ImageIcon(getClass().getResource("bienvenido.png"));
		tituloLabel.setIcon(tituloIcon);
				
			
		JLabel monticulosLabel = new JLabel("*Cantidad de mont�culos: ");
		this.monticulosTxt = new JTextField();
		this.monticulosTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		this.monticulosTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
			
		JLabel obstaculosLabel = new JLabel("*Cantidad de obst�culos: ");
		this.obstaculosTxt = new JTextField();
		this.obstaculosTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		this.obstaculosTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		
		JLabel cargadoresLbl = new JLabel("*Cantidad de cargadores: ");
		this.cargadoresTxt = new JTextField(); 
		cargadoresTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		cargadoresTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		
		JLabel exploradoresLbl = new JLabel("*Cantidad de exploradores: ");
		this.exploradoresTxt = new JTextField(); 
		exploradoresTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		exploradoresTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		
			
		this.tituloBox.add(tituloLabel);
		
		this.aceptarButton = new JButton("Siguiente");
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
		
		this.ordenaCapasLbl = new JLabel("*Ordena las capas del cargador");
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
		
		String[] ordenCapaArray = { "1", "2", "3", "4", "5", "6" };
		ordenEvitarObstaculos = new JComboBox(ordenCapaArray);
		ordenEvitarObstaculos.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		
		ordenLlevarPiedras = new JComboBox(ordenCapaArray);
		ordenLlevarPiedras.setMaximumSize(new Dimension(ANCHO_TEXTFIELD,ALTO_TEXTFIELD));
		ordenLlevarPiedras.setSelectedIndex(1);
		
		ordenCumplirContrato = new JComboBox(ordenCapaArray);
		ordenCumplirContrato.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		ordenCumplirContrato.setSelectedIndex(2);
		
		ordenRecolectarMuestras = new JComboBox(ordenCapaArray);
		ordenRecolectarMuestras.setMaximumSize(new Dimension( ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		ordenRecolectarMuestras.setSelectedIndex(3);
		
		ordernLeerSolicitudes = new JComboBox(ordenCapaArray);
		ordernLeerSolicitudes.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		ordernLeerSolicitudes.setSelectedIndex(4);
		
		ordenExplorar = new JComboBox(ordenCapaArray);
		ordenExplorar.setMaximumSize(new Dimension(ANCHO_TEXTFIELD,	ALTO_TEXTFIELD));
		ordenExplorar.setSelectedIndex(5);
		
		JLabel evitarObstaculosLbl = new JLabel("1. Evitar obst�culos: ");
		JLabel llevarPiedrasLbl = new JLabel("2. Llevar piedras: ");
		JLabel cumplirContratoLbl = new JLabel("3. Cumplir contrato: ");
		JLabel recolectarMuestrasLbl = new JLabel("4. Recolectar piedras: ");
		JLabel leerSolicitudesLbl = new JLabel("5. Leer solicitudes ");
		JLabel explorarLbl = new JLabel("6. Explorar: ");
		
		this.capasLeftBox.add(evitarObstaculosLbl);
		this.capasLeftBox.add(Box.createRigidArea(new Dimension(20, 40)));
		this.capasLeftBox.add(llevarPiedrasLbl);
		this.capasLeftBox.add(Box.createRigidArea(new Dimension(20, 40)));
		this.capasLeftBox.add(cumplirContratoLbl);
		this.capasLeftBox.add(Box.createRigidArea(new Dimension(20, 40)));
		this.capasLeftBox.add(recolectarMuestrasLbl);
		this.capasLeftBox.add(Box.createRigidArea(new Dimension(20, 40)));
		this.capasLeftBox.add(leerSolicitudesLbl);
		this.capasLeftBox.add(Box.createRigidArea(new Dimension(20, 40)));
		this.capasLeftBox.add(explorarLbl);
		
		this.capasCenterBox.add(Box.createVerticalStrut(28));
		this.capasCenterBox.add(ordenEvitarObstaculos);
		this.capasCenterBox.add(Box.createVerticalStrut(35));
		this.capasCenterBox.add(ordenLlevarPiedras);
		this.capasCenterBox.add(Box.createVerticalStrut(35));
		this.capasCenterBox.add(ordenCumplirContrato);
		this.capasCenterBox.add(Box.createVerticalStrut(35));
		this.capasCenterBox.add(ordenRecolectarMuestras);
		this.capasCenterBox.add(Box.createVerticalStrut(35));
		this.capasCenterBox.add(ordernLeerSolicitudes);
		this.capasCenterBox.add(Box.createVerticalStrut(35));
		this.capasCenterBox.add(ordenExplorar);
		
		this.usarAgenteEspecial = new JCheckBox("Usar Agente Especial");
		//Para que al hacer click no se dibuje un recuadro alrededor
		this.usarAgenteEspecial.setFocusable(false);
		this.usarAgenteEspecial.addActionListener(this);
		
		this.capasRightBox.add(Box.createVerticalStrut(51));
		this.capasRightBox.add(usarAgenteEspecial);
		
		
		JPanel tituloPanel = new JPanel();
		tituloPanel.add(tituloBox);
				
		JPanel etiquetasEntradasPanel = new JPanel();
		etiquetasEntradasPanel.add(opcionesBox);
		
		JPanel camposDeTextoPanel = new JPanel();
		camposDeTextoPanel.add(entradasBox);
		

		capasPanel = new JPanel();
		capasPanel.add(capasLeftBox, BorderLayout.WEST);
		capasPanel.add(capasCenterBox, BorderLayout.CENTER);
		capasPanel.add(capasRightBox, BorderLayout.EAST);
				
		parametrosPanel = new JPanel();
		parametrosPanel.add(tituloPanel, BorderLayout.NORTH);
		parametrosPanel.add(etiquetasEntradasPanel, BorderLayout.WEST);
		parametrosPanel.add(camposDeTextoPanel, BorderLayout.CENTER);		
		parametrosPanel.add(capasPanel, BorderLayout.SOUTH);
		
		
		this.add(parametrosPanel, BorderLayout.CENTER);
		this.add(botonesPanel, BorderLayout.SOUTH);	
				
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
		int capaCinco = 0;
		int capaSeis = 0;
		
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
				case 5: capaCinco++;
						break;
				case 6: capaSeis++;
						break;
			}
		}		
		 //Solo debe haber un orden asignada a cada capa, dos capas no pueden tener el mismo orden de importancias
		return capaUno == 1 && capaDos == 1 && capaTres == 1 && capaCuatro == 1 && capaCinco == 1 && capaSeis == 1;
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == this.aceptarButton){
		
			if(this.monticulosTxt.getText().length() > 0 
					&& this.obstaculosTxt.getText().length() > 0 && this.cargadoresTxt.getText().length() > 0 
					&& this.exploradoresTxt.getText().length() > 0){
				
				int[] capasIntroducidasCargador = new int[NUM_CAPAS];
				capasIntroducidasCargador[Integer.parseInt(ordenEvitarObstaculos.getSelectedItem().toString())- 1] = 1;  	// 1 = Evitar Obstaculo
				capasIntroducidasCargador[Integer.parseInt(ordenLlevarPiedras.getSelectedItem().toString())- 1] = 2; 		// 2 = Regresar a nave				
				capasIntroducidasCargador[Integer.parseInt(ordenCumplirContrato.getSelectedItem().toString()) - 1] = 3;		// 3 = Cumplir contrato
				capasIntroducidasCargador[Integer.parseInt(ordenRecolectarMuestras.getSelectedItem().toString()) - 1] = 4; 	// 4 = Cargar
				capasIntroducidasCargador[Integer.parseInt(ordernLeerSolicitudes.getSelectedItem().toString()) - 1] = 5;	// 5 = Leer solicitudes
				capasIntroducidasCargador[Integer.parseInt(ordenExplorar.getSelectedItem().toString()) - 1] = 6;			// 6 = Explorar
				
				if(this.validarCapas(capasIntroducidasCargador)){
					
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
						
						PantallaExploradores configExploradores = new PantallaExploradores(capasIntroducidasCargador, numMonticulos, numObstaculos, numCargadores, numExploradores, usarAgenteEspecial.isSelected());
						configExploradores.setVisible(true);
						this.setVisible(false);
					}
				} else{
					JOptionPane.showMessageDialog(this, "Dos capas no puden tener la misma prioridad", "Orden de capas", JOptionPane.ERROR_MESSAGE);	
				}
				
			} else {
				JOptionPane.showMessageDialog(this, "Debes llenar todos los campos", "Campos vac�os", JOptionPane.ERROR_MESSAGE);
			}
						
		} else if (e.getSource() == this.borrarButton){
			
			this.monticulosTxt.setText("");
			this.obstaculosTxt.setText("");
			this.cargadoresTxt.setText("");
			this.exploradoresTxt.setText("");
			
		}  
	}
}
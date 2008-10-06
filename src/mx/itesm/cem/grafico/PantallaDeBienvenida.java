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
	private static final int ANCHO = 390;
	private static final int ALTO = 380;
	private static final int LIMITE_CARACTERES = 6;
	private static final int ANCHO_TEXTFIELD = 50;
	private static final int ALTO_TEXTFIELD = 20;
	public Box tituloBox, opcionesBox, entradasBox, capasBox, botonesBox ;
	public JPanel botonesPanel;
	public JButton aceptarButton, borrarButton;
	public JTextField monticulosTxt, obstaculosTxt, agentesTxt  = new JTextField("");
	public int numPiedras, numMonticulos, numObstaculos, numAgentes;
	public boolean datosValidos;
	
	
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
		this.capasBox = Box.createHorizontalBox();
		this.botonesBox = Box.createHorizontalBox();
		
		
		
		this.botonesPanel = new JPanel();
		
		JLabel tituloLabel = new JLabel();
		ImageIcon tituloIcon = new ImageIcon(getClass().getResource("bienvenido.png"));
		tituloLabel.setIcon(tituloIcon);
				
			
		JLabel monticulosLabel = new JLabel("Cantidad de montículos: ");
		this.monticulosTxt = new JTextField();
		this.monticulosTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		this.monticulosTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
			
		JLabel obstaculosLabel = new JLabel("Cantidad de obstáculos: ");
		this.obstaculosTxt = new JTextField();
		this.obstaculosTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		this.obstaculosTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		
		JLabel exploradoresLabel = new JLabel("Cantidad de exploradores: ");
		this.agentesTxt = new JTextField(); 
		agentesTxt.setDocument(new JTextFieldLimit(LIMITE_CARACTERES));
		agentesTxt.setMaximumSize(new Dimension(ANCHO_TEXTFIELD, ALTO_TEXTFIELD));
		
			
		this.tituloBox.add(Box.createVerticalStrut(60));
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
		
				
		this.entradasBox.add(monticulosTxt);
		this.entradasBox.add(Box.createRigidArea(new Dimension(20,40)));
		this.entradasBox.add(obstaculosTxt);
		this.entradasBox.add(Box.createRigidArea(new Dimension(20,40)));
		this.entradasBox.add(agentesTxt);
		
		
		
		this.botonesBox.add(aceptarButton);
		this.botonesBox.add(Box.createRigidArea(new Dimension(20,40)));
		this.botonesBox.add(borrarButton);
		
		this.botonesPanel.add(botonesBox, BorderLayout.CENTER);
		
		this.add(tituloBox, BorderLayout.NORTH);
		this.add(opcionesBox,BorderLayout.WEST);
		this.add(entradasBox, BorderLayout.CENTER);
		this.add(botonesPanel,BorderLayout.SOUTH);
		
		this.setVisible(true);

	}
	
public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == this.aceptarButton){
		
			if(this.monticulosTxt.getText().length() > 0 
					&& this.obstaculosTxt.getText().length() > 0 && this.agentesTxt.getText().length() > 0){

				numMonticulos = Integer.parseInt(this.monticulosTxt.getText());
				numObstaculos = Integer.parseInt(this.obstaculosTxt.getText());
				numAgentes = Integer.parseInt(this.agentesTxt.getText());
				
				this.setVisible(false);
				
				Tablero tb = new Tablero(numMonticulos, numObstaculos, numAgentes);
				System.out.println(tb.toString());
						
				@SuppressWarnings("unused")
				TableroGrafico tg = new TableroGrafico();
				for(int i=0; i < Tablero.listaAgentes.size(); i++)
					new ThreadAgente(Tablero.listaAgentes.get(i));
				
			} else {
				JOptionPane.showMessageDialog(this, "Debes llenar todos los campos", "Campos vacíos", JOptionPane.ERROR_MESSAGE);
			}
						
		}else if (e.getSource() == this.borrarButton){
			
			this.monticulosTxt.setText("");
			this.obstaculosTxt.setText("");
			this.agentesTxt.setText("");			
		}	
	}

}

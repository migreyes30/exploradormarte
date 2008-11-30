package mx.itesm.cem.grafico;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PantallaSeleccion extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private static final int ANCHO = 320;
	private static final int ALTO = 160;
	
	private JPanel panelTitulo, panelBotones;
	private JButton anterioresButton, contratantesButton;
	private Box botonesBox;
		
	
	public PantallaSeleccion() {
		
		this.setTitle("Bienvenido al explorador de marte");
		this.setSize(ANCHO, ALTO);
		this.setMaximumSize(new Dimension(ANCHO,ALTO));
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel tituloLabel = new JLabel();
		ImageIcon tituloIcon = new ImageIcon(getClass().getResource("rocket2.png"));
		tituloLabel.setIcon(tituloIcon);
		
		this.panelTitulo = new JPanel();
		this.panelTitulo.add(tituloLabel, BorderLayout.WEST);
		this.panelTitulo.add(new JLabel("¿Qué etapas deseas probar?"), BorderLayout.CENTER);
		
		this.anterioresButton = new JButton("Etapas anteriores");
		this.anterioresButton.addActionListener(this);
		this.anterioresButton.setFocusable(false);
		
		this.contratantesButton = new JButton("Red de contratantes");
		this.contratantesButton.addActionListener(this);
		this.contratantesButton.setFocusable(false);
		
		this.botonesBox = Box.createHorizontalBox();
		this.botonesBox.add(anterioresButton);
		this.botonesBox.add(Box.createHorizontalStrut(15));
		this.botonesBox.add(contratantesButton);
		
		this.panelBotones = new JPanel();
		this.panelBotones.add(Box.createVerticalStrut(60));
		this.panelBotones.add(botonesBox, BorderLayout.CENTER);
		
		this.add(Box.createVerticalStrut(50));
		this.add(panelTitulo, BorderLayout.NORTH);	
		this.add(panelBotones, BorderLayout.CENTER);
		
		this.setVisible(true);
		
	}


	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == this.anterioresButton) {
			
			PantallaEtapasAnteriores bienvenidaAnteriores = new PantallaEtapasAnteriores();
			bienvenidaAnteriores.setVisible(true);
			this.setVisible(false);
			
		} else if (e.getSource() == this.contratantesButton) {
		
			PantallaContratantes bienvenidaContratantes = new PantallaContratantes();
			bienvenidaContratantes.setVisible(true);
			this.setVisible(false);
			
		}
		
		
	}
	
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		PantallaSeleccion seleccion = new PantallaSeleccion();
		
	}

}

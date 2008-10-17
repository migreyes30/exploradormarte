package mx.itesm.cem.explorador;

import java.util.Date;

import javax.swing.JOptionPane;

import mx.itesm.cem.grafico.TableroGrafico;

public class ThreadAgente implements Runnable{
	Thread t;
	private Agente agente;
	private int[] capas;
	private TableroGrafico tg;
	private static boolean hasNotified = false;
	
	
	public ThreadAgente(Agente agente, int[] capas, TableroGrafico tg){
		this.agente = agente;
		this.capas = capas;
		this.tg = tg;
		t = new Thread(this);
		t.start();
	}
	
	public void run(){
		
		long start = new Date().getTime();
		while(!Tablero.isTerminado()){
			agente.actuar(capas);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
		}
		
		if(!hasNotified){
			hasNotified = true;
			JOptionPane.showMessageDialog(this.tg,
					"Mision Completada en " + ((new Date().getTime() - start) / 1000) +
					" segundos.",
					"Completado", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}

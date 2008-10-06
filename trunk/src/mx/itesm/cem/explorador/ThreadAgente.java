package mx.itesm.cem.explorador;

import java.util.Date;

public class ThreadAgente implements Runnable{
	Thread t;
	private Agente agente;
	int[] capas = {1,2,3,4};
	
	public ThreadAgente(Agente agente){
		this.agente = agente;
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
		
		System.out.println(this.agente.getId() + ": took " + 
				(new Date().getTime() - start)/1000 + " seconds");
	}
}

package mx.itesm.cem.explorador;

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
		while(Tablero.piedrasNave != Tablero.totalPiedras){
			agente.actuar(capas);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {

			}
		}
		System.out.println("LISTO!" + this.agente.getId());
	}
}

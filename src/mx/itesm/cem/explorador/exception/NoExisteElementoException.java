package mx.itesm.cem.explorador.exception;

/* Clase usada cuando no se encuentra un elemento 
 * con el id proporcionado.
 */
@SuppressWarnings("serial")
public class NoExisteElementoException extends NullPointerException {
	
	public NoExisteElementoException(String mensaje){
		super(mensaje);
	}
}

package mx.itesm.cem.grafico;

/**
 * Esta clase fue adaptada de: http://manix.oneble.com/?p=117
 * y de: http://divideyconquer.blogspot.com/2008/09/limitar-caracteres-en-un-jtextfield.html
 * y está disponible por parte del autor para su uso libre. 
 * 
 * Clase que limita el número máximo de caracteres que acepta
 * un JTextField además de impedir que se introduzca algo
 * distinto a un número.
 * 
 */

import javax.swing.text.*;

@SuppressWarnings("serial")
public class JTextFieldLimit extends PlainDocument {
	
    private int limit;
        
    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }
    
    
    @Override
    public void insertString (int offset, String  str, AttributeSet attr) throws BadLocationException {
        
    	if (str == null){
    		return;
    	}
        	
        if ((getLength() + str.length()) <= limit) {            
        	for (int i = 0; i < str.length(); i++){
        		if (!Character.isDigit(str.charAt(i))){
        			return;
        		}                 
        	}                
             super.insertString(offset, str, attr);        	
        }
    }

}


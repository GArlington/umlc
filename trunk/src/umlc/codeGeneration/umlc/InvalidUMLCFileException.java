/*
 * InvalidUMLCFileException.java
 *
 * Created on August 7, 2006, 10:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package umlc.codeGeneration.umlc;

/**
 *
 * @author ryan
 */
public class InvalidUMLCFileException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>InvalidUMLCFileException</code> without detail message.
     */
    public InvalidUMLCFileException() {
    }
    
    
    /**
     * Constructs an instance of <code>InvalidUMLCFileException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InvalidUMLCFileException(String msg) {
        super(msg);
    }
}

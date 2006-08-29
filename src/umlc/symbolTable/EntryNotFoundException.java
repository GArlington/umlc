/*
 * EntryNotFoundException.java
 *
 * Created on November 5, 2001, 7:56 PM
 */

package umlc.symbolTable;

/**
 *
 * @author  Ryan
 * @version 
 */
public class EntryNotFoundException extends java.lang.Exception {

    /**
     * Creates new <code>EntryNotFoundException</code> without detail message.
     */
    public EntryNotFoundException() {
    }


    /**
     * Constructs an <code>EntryNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public EntryNotFoundException(String msg) {
        super(msg);
    }
}



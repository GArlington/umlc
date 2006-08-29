/*
 * FreeFormTextValidationException.java
 *
 * Created on August 16, 2006, 8:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.horstmann.violet.framework;

/**
 *
 * @author ryan
 */
public class FreeFormTextValidationException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>FreeFormTextValidationException</code> without detail message.
     */
    public FreeFormTextValidationException() {
    }
    
    
    /**
     * Constructs an instance of <code>FreeFormTextValidationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public FreeFormTextValidationException(String msg) {
        super(msg);
    }
}

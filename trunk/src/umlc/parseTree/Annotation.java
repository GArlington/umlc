/*
 * Annotation.java
 *
 * Created on July 31, 2006, 2:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package umlc.parseTree;

/**
 *
 * @author ryan
 */
public class Annotation {
    
    /** Creates a new instance of Annotation */
    public Annotation() {
    }

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Holds value of property value.
     */
    private String value;

    /**
     * Getter for property value.
     * @return Value of property value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(String value) {
        this.value = value;
    }
    
}

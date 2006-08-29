/*
 * InheritanceEdge.java
 *
 * Created on August 6, 2006, 11:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.horstmann.violet;

/**
 *
 * @author ryan
 */
public class InheritanceEdge extends ClassRelationshipEdge {
    
    /** Creates a new instance of InheritanceEdge */
    public InheritanceEdge() {
        super();
        setBentStyle(BentStyle.VHV);
        setEndArrowHead(ArrowHead.TRIANGLE);
    }
    
}

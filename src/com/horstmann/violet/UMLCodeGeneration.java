/*
 * UMLCodeGeneration.java
 *
 * Created on August 5, 2006, 6:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.horstmann.violet;

import java.io.File;

/**
 *
 * @author ryan
 */
public class UMLCodeGeneration {

    private JMatterClassDiagramGraph graph;
    
    /** Creates a new instance of UMLCodeGeneration */
    public UMLCodeGeneration(JMatterClassDiagramGraph graph) {
        this.graph = graph;
    }
    
    public void generateCode(File toWrite) {
        try {
            if (!toWrite.exists())
                toWrite.createNewFile();
            
        } catch (Exception e) {
            
        }
    }
    
}

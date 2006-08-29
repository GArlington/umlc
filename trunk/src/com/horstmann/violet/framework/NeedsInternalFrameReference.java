/*
 * NeedsInternalFrameReference.java
 *
 * Created on August 17, 2006, 2:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.horstmann.violet.framework;

import javax.swing.JInternalFrame;

/**
 *
 * @author ryan
 */
public interface NeedsInternalFrameReference {
    
    public void setInternalFrame(JInternalFrame frame);
    
}

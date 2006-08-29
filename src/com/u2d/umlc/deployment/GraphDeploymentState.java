/*
 * GraphDeploymentState.java
 *
 * Created on August 10, 2006, 9:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.u2d.umlc.deployment;




/**
 *
 * @author ryan
 */
public abstract class GraphDeploymentState {

    protected String[] transitions;
    
    public abstract String[] getTransitions();
    
    public abstract GraphDeploymentState transition(String transition, DeploymentInformation info);
    
    
}

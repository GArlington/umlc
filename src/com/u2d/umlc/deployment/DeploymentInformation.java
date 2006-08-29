/*
 * DeploymentInformation.java
 *
 * Created on August 10, 2006, 9:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.u2d.umlc.deployment;

import java.util.HashMap;

/**
 *
 * @author ryan
 */
public class DeploymentInformation {
    
    public static final int STATE_NO_JMATTER = 0;
    public static final int STATE_NEW        = 1;
    public static final int STATE_PROJECT    = 2;
    public static final int STATE_CODE       = 3;
    public static final int STATE_DB         = 4;
    public static final int STATE_RUN        = 5;
    
    
    
    
    /** Creates a new instance of DeploymentInformation */
    public DeploymentInformation() {
        this.hibernateProperties = new HashMap();
        this.state = STATE_NO_JMATTER;
    }
    
    
    
    public void addHibernateProperty(String name, String value) {
        this.hibernateProperties.put(name, value);
    }
    
    

    /**
     * Holds value of property projectName.
     */
    private String projectName;

    /**
     * Getter for property projectName.
     * @return Value of property projectName.
     */
    public String getProjectName() {
        return this.projectName;
    }

    /**
     * Setter for property projectName.
     * @param projectName New value of property projectName.
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Holds value of property deploymentDirectory.
     */
    private String deploymentDirectory;

    /**
     * Getter for property deploymentDirectory.
     * @return Value of property deploymentDirectory.
     */
    public String getDeploymentDirectory() {
        return this.deploymentDirectory;
    }

    /**
     * Setter for property deploymentDirectory.
     * @param deploymentDirectory New value of property deploymentDirectory.
     */
    public void setDeploymentDirectory(String deploymentDirectory) {
        this.deploymentDirectory = deploymentDirectory;
    }

    /**
     * Holds value of property hibernateProperties.
     */
    private HashMap hibernateProperties;

    /**
     * Getter for property hibernateProperties.
     * @return Value of property hibernateProperties.
     */
    public HashMap getHibernateProperties() {
        return hibernateProperties;
    }

    /**
     * Setter for property hibernateProperties.
     * @param hibernateProperties New value of property hibernateProperties.
     */
    public void setHibernateProperties(HashMap hibernateProperties) {
        this.hibernateProperties = hibernateProperties;
    }

    /**
     * Holds value of property umlFileLocation.
     */
    private String umlFileLocation;

    /**
     * Getter for property umlFileLocation.
     * @return Value of property umlFileLocation.
     */
    public String getUmlFileLocation() {
        return this.umlFileLocation;
    }

    /**
     * Setter for property umlFileLocation.
     * @param umlFileLocation New value of property umlFileLocation.
     */
    public void setUmlFileLocation(String umlFileLocation) {
        this.umlFileLocation = umlFileLocation;
    }

    /**
     * Holds value of property jmatterDirectory.
     */
    private String jmatterDirectory;

    /**
     * Getter for property jmatterDirectory.
     * @return Value of property jmatterDirectory.
     */
    public String getJmatterDirectory() {
        return this.jmatterDirectory;
    }

    /**
     * Setter for property jmatterDirectory.
     * @param jmatterDirectory New value of property jmatterDirectory.
     */
    public void setJmatterDirectory(String jmatterDirectory) {
        this.jmatterDirectory = jmatterDirectory;
    }

    /**
     * Holds value of property state.
     */
    private int state;

    /**
     * Getter for property state.
     * @return Value of property state.
     */
    public int getState() {
        return this.state;
    }

    /**
     * Setter for property state.
     * @param state New value of property state.
     */
    public void setState(int state) {
        this.state = state;
    }
    
}

package umlc.parseTree;

import java.util.*;
import umlc.symbolTable.Symbol_table;


public class UmlPackage {

    public String name;

    /* All of the classes in this package */
    public Vector UmlClasses;
    public Vector UmlAssociations;

    UmlPackage() {
        name = "";
	UmlClasses = new Vector();
	UmlAssociations = new Vector();
    }
    
    UmlPackage (String _name) {
        _name = _name;
	UmlClasses = new Vector();
        UmlAssociations = new Vector();

    }

    public Vector getClasses() {
        return UmlClasses;
    }
    
    public void setName(String _name) {
     name = _name;   
    }

    public String getName() { return name; }
    
    
    public void semantic_check(Symbol_table st) {
	for (int i=0; i < UmlAssociations.size(); i++) {
	    ((UmlAssociation)UmlAssociations.elementAt(i)).semantic_check(st);
	}
        for (int i=0; i < UmlClasses.size(); i++) {
	    ((UmlClass)UmlClasses.elementAt(i)).setPackage_name(name);
	}
    }


    public void pretty_print() {
	System.out.println("-----------------------------------");
	System.out.println("Package: " + name);
	System.out.println("--------------Classes--------------\n");
	
	// lets print out all the info on the Classes
	for (int i=0; i<UmlClasses.size(); i++) {
	    UmlClass c = (UmlClass) UmlClasses.elementAt(i);
	    c.pretty_print();
	}
        
        System.out.println("------------Associations----------\n");
        
        // lets print out all the info on the Relationships
	for (int i=0; i<UmlAssociations.size(); i++) {
	    UmlAssociation r = (UmlAssociation) UmlAssociations.elementAt(i);
	    r.pretty_print();
        }

    }

}

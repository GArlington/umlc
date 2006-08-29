package umlc.parseTree;

import tools.*;
import umlc.symbolTable.*;
import java.util.*;

public class AssociationEnd {
    
    
    public String class_name;
    public String known_as;
    private boolean hasKnownAs;
    
    public HashMap annotations;
    
    public Multiplicity multiplicity;
    public UmlClass thisEnd;
    public AssociationEnd otherEnd;
    public UmlAssociation association;
    
    AssociationEnd() {
        annotations = new HashMap();
    }
    
    
    AssociationEnd(String _class_name) {
        this();
        class_name = _class_name;
        hasKnownAs = false;
    }
    
    AssociationEnd(String _class_name, String _known_as) {
        this();
        class_name = _class_name;
        known_as   = _known_as;
        hasKnownAs = true;
    }
    
    
    
    public void addAnnotation(String name, String value) {
        annotations.put(name, value);
    }
    
    public boolean hasAnnotation(String name) {
        return annotations.containsKey(name);
    }
    
    public String annotationValue(String name) {
        return (String) annotations.get(name);
    }

    public void setMultiplicity(Multiplicity _multiplicity) {
        multiplicity = _multiplicity;
    }
    
    public Multiplicity getMultiplicity() { return multiplicity; }


    public void setThisEnd(UmlClass _thisEnd) {
        thisEnd = _thisEnd;
    }
    

    public UmlClass getThisEnd() { return thisEnd;}

    public void setOtherEnd(AssociationEnd _otherEnd) {
        otherEnd = _otherEnd; 
    }
    

    public UmlClass getOtherEnd() { return otherEnd.thisEnd;}


    public void setAssociation(UmlAssociation _association) {
        association = _association;
    }
    
    
    public String getName() { return class_name;}
    public String getKnownAs() { 
        if (known_as != null)
            return lower(known_as); 
        else {
            StringBuffer text = new StringBuffer(lower(class_name));
            if (this.multiplicity.isManyRelationship() ) {
                text.append("s");   
            }
            
            return text.toString();
        }   
    }

    public String getKnownAsUpper() {
        if (known_as != null)
            return upper(known_as); 
        else {
            StringBuffer text = new StringBuffer(upper(class_name));
            if (this.multiplicity.isManyRelationship() ) {
                text.append("s");   
            }
            
            return text.toString();
        }        
    }
    
    
    public String getOtherEndKnownAs() {
        AssociationEnd oAss = null;
        if (association.end1 == this) oAss = association.end2;
        else oAss = association.end1;        
        return oAss.getKnownAs();       
    }
    
    public AssociationEnd getOther() {
        AssociationEnd oAss = null;
        if (association.end1 == this) oAss = association.end2;
        else oAss = association.end1;        
        return oAss;         
    }
    
    public String getOtherEndKnownAsUpper() {
        AssociationEnd oAss = null;
        if (association.end1 == this) oAss = association.end2;
        else oAss = association.end1;        
        return oAss.getKnownAsUpper();         
    }
    
    private String lower(String name) {
        char chars[] = name.toCharArray();
 	chars[0] = Character.toLowerCase(chars[0]);
 	return new String(chars);
    }
    
    private String upper(String name) {
        char chars[] = name.toCharArray();
 	chars[0] = Character.toUpperCase(chars[0]);
 	return new String(chars);
    }
    
    public boolean isManyToMany() {
	return multiplicity.isManyRelationship() && otherEnd.multiplicity.isManyRelationship();
    }

    public boolean isAssociationClass() {
	return false;
    }

    public boolean isOneToOne() {
        return !multiplicity.isManyRelationship() && !otherEnd.multiplicity.isManyRelationship();
    }

    public boolean isParent() {
	return !multiplicity.isManyRelationship() && otherEnd.multiplicity.isManyRelationship();
    }


    public void semantic_check(Symbol_table st) {
	// lets check to make sure that the classes exist in our namespace
        try {
            thisEnd = (UmlClass)st.lookup(class_name);

	    // now we put a reference to this end in the class
	    thisEnd.associations.addElement(this);
	    Debug.println(5, "ADDED ASSOCIATION");
	    

        }
        catch(EntryNotFoundException e1) {
            Debug.println(0, "Error: Class '" + class_name + "' not found in package");
            System.exit(0);
        }
    }

    void pretty_print() {
        System.out.println("Class: " + class_name);
        System.out.println("named: " + known_as);
        
    }
}


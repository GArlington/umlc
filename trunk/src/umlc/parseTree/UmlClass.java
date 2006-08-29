package umlc.parseTree;

import java.util.*;

public class UmlClass implements TreeNode {

    public String package_name;
    public String name;
    public String class_extends;
    
    public Vector attributes;
    public Vector operations;
    
    public Vector modifiers;

    // these two vectors will be filled after semantic checking
    // and the head vector contains the start of the relationship
    // holding the end that has the lowest multiplicity. The tail
    // vector holds the end of the relationship and the end that 
    // has the highest multiplicity

    public Vector associations;
    
    public HashMap annotations;



    public UmlClass() {
	associations = new Vector();
        attributes    = new Vector();
	operations    = new Vector();
	associations  = new Vector();
        modifiers     = new Vector();
        annotations    = new HashMap();
    }
    
    public UmlClass (String _name, String _extends ) {
        this();
        name = _name;
        class_extends = _extends;
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
    
    
    public void setPackage_name(String _package_name) {
        package_name = _package_name;
    }
    
    public String getPackage_name() {
        return package_name;
	//	return new String("Cool Moe DE");
    }
    
    public String getName() { return name; }
    public void setName(String _name) { name = _name; }
        
    public String getNameLower() {
        char chars[] = name.toCharArray();
 	chars[0] = Character.toLowerCase(chars[0]);
 	return new String(chars);
    }
    
    public String getExtends() {
        return class_extends;
    }
    
    public void pretty_print() {
        System.out.println("*****************************");
	System.out.println("CLASS: " + name);
    	if (class_extends != null && !class_extends.equals("")) System.out.println("EXTENDS: " + class_extends);
	System.out.println("ATTRIBUTES:");
        if (attributes != null ) {
            for (int i=0; i<attributes.size(); i++) {
                UmlAttribute a = (UmlAttribute) attributes.elementAt(i);
                a.pretty_print();
            }
        }

	System.out.println("Operations:");
        if (operations != null ) {
            for (int i=0; i<operations.size(); i++) {
                UmlOperation o = (UmlOperation) operations.elementAt(i);
                o.pretty_print();
            }
        }
        System.out.println("Annotations:");
        if (annotations != null) {
            for (Iterator i=annotations.keySet().iterator(); i.hasNext(); ) {
                String a = (String) i.next();
                System.out.print("\t" + a);
                System.out.println("\t" + annotationValue(a));
            }
        }
        
        
        System.out.println("*****************************");

    }
    
    public boolean doesExtend() {
        if (class_extends == null || class_extends.equals("")) return false;
        else return true;
    }
}

package umlc.parseTree;

import java.util.*;

public class UmlAttribute implements TreeNode {
    public UmlModifier visiblity;
    public String name;
    public UmlType type;
    public HashMap annotations;
    
    UmlAttribute (UmlModifier _visiblity, String _name, UmlType _type, HashMap _annotations) {
        visiblity = _visiblity;
        name = _name;
        type = _type;
        annotations = _annotations;
        if (visiblity == null) visiblity = new UmlModifier(UmlModifier.PUBLIC);
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
    
    
    public String getName() { return name; }
    public String getNameCapitalized() { 
 	char chars[] = name.toCharArray();
 	chars[0] = Character.toUpperCase(chars[0]);
 	return new String(chars);
    }
    public void  setName(String _name) { name = _name;}

    public UmlType getType() { return type;}
    public UmlModifier getModifier() { return visiblity;}
    
    public void pretty_print() {
	visiblity.pretty_print();
	System.out.print(" " + name + ":");
	type.pretty_print();
	System.out.println("");
    }
}

package umlc.parseTree;

import java.util.*;

public class UmlModifier {

    
    private static Hashtable languageMappings = new Hashtable();
    
    public static String PUBLIC    = "PUBLIC";
    public static String PROTECTED = "PROTECTED";
    public static String PRIVATE   = "PRIVATE";

    public String value;
    UmlModifier(){}
    UmlModifier(String _value) {
        value = _value;
    }

    
    public static void loadMapping(String language, Properties mapping) {
        // we dont check to see if the language is already there
        // we just assume that they want to overwrite the previous
        // mapping
        languageMappings.put(language, mapping);
        
    }
 
    public String getValue(String language) {
            Properties p = (Properties)languageMappings.get(language);
            return p.getProperty(value);
    }
    

    public void pretty_print() {
        System.out.print(value + " ");
    }
}

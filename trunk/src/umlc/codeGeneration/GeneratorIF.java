/*
 * GeneratorIF.java
 *
 * Created on November 8, 2001, 10:41 PM
 */

package umlc.codeGeneration;

import java.util.Map;
import java.util.Set;
import com.l2fprod.common.propertysheet.DefaultProperty;
/**
 *
 * @author  Ryan
 * @version 
 */
public interface GeneratorIF {

    public void initGenerator();    
    
    public void generateCode(java.util.Hashtable uml_packages, String output_dir);
    

    public Set<String> classAnnotationNames();
    public DefaultProperty getClassAnnotationByName(String name);
    public Set<Object> suggestedClassAnnotationValues(String annotation);
    
    public Set<String> suggestedPropertyTypes();    
    public Set<String> annotationNamesByPropertyType(String type);
    public DefaultProperty getPropertyAnnotationByName(String name);
    public Set<Object> suggestedPropertyAnnotationValues(String type, String annotation);
    
}


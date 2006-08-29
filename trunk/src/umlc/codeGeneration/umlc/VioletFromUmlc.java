/*
 * VioletFromUmlc.java
 *
 * Created on August 7, 2006, 9:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package umlc.codeGeneration.umlc;

import com.horstmann.violet.BentStyle;
import com.horstmann.violet.InheritanceEdge;
import java.io.*;
import umlc.parseTree.*;
import com.horstmann.violet.JMatterClassDiagramGraph;
import com.horstmann.violet.JMatterClassNode;
import com.horstmann.violet.PackageNode;
import com.horstmann.violet.framework.JMatterAssociationEdge;
import com.l2fprod.common.propertysheet.DefaultProperty;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.beanutils.ConvertUtils;
import umlc.codeGeneration.GeneratorIF;
import umlc.symbolTable.Symbol_table;

/**
 *
 * @author ryan
 */
public class VioletFromUmlc {
    
    GeneratorIF generator;
    
    /** Creates a new instance of VioletFromUmlc */
    public VioletFromUmlc(GeneratorIF generator) {
        this.generator = generator;
    }
    
    
    
    
    public JMatterClassDiagramGraph generateGraph(InputStream file) throws InvalidUMLCFileException {
        try {
             DataInputStream input = new DataInputStream(file);
            // attach lexer to the input stream
            UmlLexer lexer = new UmlLexer(input);

            // Create parser attached to lexer
            UmlParser parser = new UmlParser(lexer);     
            Symbol_table st = new Symbol_table();
            parser.setSymbolTable(st);
            // start up the parser by calling the rule
            // at which you want to begin parsing.
            UmlPackage p = parser.uml(); 
            
            st.setPackage(p.getName());
	    p.semantic_check(st);
            
            
            int currentx = 10;
            int currenty = 10;
            
            JMatterClassDiagramGraph g = new JMatterClassDiagramGraph();
            if (!"com.u2d.generated".equals(p.name) ){
                PackageNode pn = new PackageNode();
                pn.setName(p.name);
                g.addNode(pn, new Point(currentx, currenty));
                currentx+=210;
                currenty+=50;
            }
            HashMap clzNameToNode = new HashMap();
            HashMap inheritenceMap = new HashMap();
            for (Iterator i=p.UmlClasses.iterator(); i.hasNext();) {
                UmlClass clz = (UmlClass)i.next();                
                JMatterClassNode cn = new JMatterClassNode();
                clzNameToNode.put(clz.getName(), cn);
                cn.setName(clz.getName());
                
                if (clz.doesExtend()) inheritenceMap.put(clz.getName(), clz.getExtends());
                
                if (clz.hasAnnotation("abstract")) {
                    cn.setAbstractClass(true);
                    clz.annotations.remove("abstract"); // dont want user editable
                }
                double dx;
                double dy;
                if (clz.hasAnnotation("dx")) {
                    System.out.println("dx found");
                    try {
                        dx = Double.parseDouble(clz.annotationValue("dx"));
                        System.out.println("dx: " + dx);
                    } catch (Exception e) {
                        e.printStackTrace();
                        dx = currentx;
                        currentx += 200;
                    }
                    clz.annotations.remove("dx"); // dont want user editable
                }
                else {
                    dx = currentx;
                    currentx += 200;
                }
                if (clz.hasAnnotation("dy")) {
                    try {
                        dy = Double.parseDouble(clz.annotationValue("dy"));
                    } catch (Exception e) {
                        dy = currenty;
                        currenty += 50;
                    }
                    clz.annotations.remove("dy"); // dont want user editable
                } else {
                    dy = currenty;
                    currenty += 50;
                }
                
                if (clz.annotations.keySet().size() > 0) {
                    for (Iterator j=clz.annotations.keySet().iterator(); j.hasNext();) {
                        String key = (String) j.next();
                        String value = clz.annotationValue(key);
                        DefaultProperty prop = generator.getClassAnnotationByName(key);
                        System.out.println("should be: " + prop.getType());
                        Object obj = ConvertUtils.convert(value, prop.getType());
                        cn.getAnnotationNameValue().put(key, obj);
                        
                        System.out.println("found: " + obj + " of class: " + obj.getClass().getCanonicalName());
                        
                        
                    }
                    
                }
                
                if (clz.attributes.size() > 0) {
                    
                    for (Iterator it = clz.attributes.iterator(); it.hasNext();) {
                        UmlAttribute elem = (UmlAttribute) it.next();
                        cn.getAttributeNameType().put(elem.getName(), elem.getType().getType("java"));
                        if (elem.annotations.keySet().size() > 0) {
                            for (Iterator it2 = elem.annotations.keySet().iterator(); it2.hasNext();) {
                                String key = (String) it2.next();
                                String value = elem.annotationValue(key);
                                DefaultProperty prop = generator.getPropertyAnnotationByName(key);
                                Object obj = ConvertUtils.convert(value, prop.getType());
                                HashMap attAnnots = cn.getAttributeAnnotations().get(elem.getName());
                                if (attAnnots == null) {
                                    attAnnots = new HashMap();
                                    cn.getAttributeAnnotations().put(elem.getName(), attAnnots);
                                }
                                attAnnots.put(key, obj);
                            }
                        }
                    }
                    
                }
                
                g.add(cn, new Point.Double(dx, dy));
                cn.calculateProperties();
                
            } // end of for loop on classes
            
            
            
            // loop on associations
            for (Iterator it = p.UmlAssociations.iterator(); it.hasNext();) {
                UmlAssociation association = (UmlAssociation) it.next();
                JMatterAssociationEdge edge = new JMatterAssociationEdge();

                edge.setStartLabel(association.end1.getKnownAs());
                edge.setStartMultiplicity(association.end1.getMultiplicity() + "");
                System.out.println("Set multiplicty on start2: " + edge.getStartMultiplicity() ); 
                edge.setStartNavigable(!association.end1.hasAnnotation("no_navigate"));
                
                edge.setEndLabel(association.end2.getKnownAs());             
                edge.setEndMultiplicity(association.end2.getMultiplicity() + ""); 
                System.out.println("Set multiplicty on end: " + association.end2.getMultiplicity() ); 
     
                edge.setEndNavigable(!association.end2.hasAnnotation("no_navigate"));

                //edge.setMiddleLabel(association.name);
                edge.setBentStyle(BentStyle.HVH); // this is lossy. we dont know
  
                if (association.end1.hasAnnotation("bentStyle")) {
                    try {
                        String bndClaz = association.end1.annotationValue("bentStyle");
                        BentStyle style = BentStyle.fromString(bndClaz);
                        edge.setBentStyle(style);
                    } catch (Exception e) {
                        
                    }
                }
                
                
                JMatterClassNode n1 = (JMatterClassNode)clzNameToNode.get(association.end1.getName());
                JMatterClassNode n2 = (JMatterClassNode)clzNameToNode.get(association.end2.getName());
                if (n1==null || n2 == null) System.out.println("an edge is null");
                g.connect(edge,n1 ,n2 );  
            }
            
            // loop on inheratence
            for (Iterator it = inheritenceMap.keySet().iterator(); it.hasNext(); ) {
                String claz = (String) it.next();
                String parent = (String)inheritenceMap.get(claz);
                JMatterClassNode n1 = (JMatterClassNode)clzNameToNode.get(claz);
                JMatterClassNode n2 = (JMatterClassNode)clzNameToNode.get(parent);
                if (n1==null || n2 == null) System.out.println("an edge is null");
                
                InheritanceEdge edge = new InheritanceEdge();
       
                g.connect(edge,n1 ,n2 );              
            } 
            
            
            
            return g;
            
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidUMLCFileException();
        }
    }
    
    
    
}

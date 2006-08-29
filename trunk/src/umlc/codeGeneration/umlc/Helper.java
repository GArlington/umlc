/*
 * Helper.java
 *
 * Created on August 5, 2006, 9:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package umlc.codeGeneration.umlc;

import com.horstmann.violet.BentStyle;
import com.horstmann.violet.JMatterClassNode;
import com.horstmann.violet.framework.JMatterAssociationEdge;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.beanutils.ConvertUtils;

/**
 *
 * @author ryan
 */
public class Helper {
    
    /** Creates a new instance of Helper */
    public Helper() {
    }
    public String replaceAttributesBar(String attributes) {

        return attributes.replaceAll("\\|", "\n\t\t");
    }
    
    public String replaceBar(String bar) {
        return bar.replaceAll("\\|", "\n");
    }    
    
    public String classAnnotations(JMatterClassNode node) {
        
        
        StringBuffer buf = new StringBuffer("\n[");
        
        if (node.isAbstractClass() || node.getAnnotationNameValue().keySet().size() > 0) {
        
//        if (node.isAbstractClass() || (node.getAnnotations() != null && !"".equals(node.getAnnotations().getText()))) {
            if (node.isAbstractClass()) buf.append("abstract ");
            if (node.getAnnotationNameValue().keySet().size() > 0) {
                for (Iterator i = node.getAnnotationNameValue().keySet().iterator(); i.hasNext();) {
                    String name = (String)i.next();
                    Object obj = node.getAnnotationNameValue().get(name);
                    String value = ConvertUtils.convert(obj);
                    buf.append(name + "=\"" + value  +"\" ");
                }
            }           
        }
        buf.append("dx=\"" + node.getBounds().getX() + "\" ");
        buf.append("dy=\"" + node.getBounds().getY() + "\" ");
        buf.append("]");
        return buf.toString();
    }
    
    public String attributeAnnotations(JMatterClassNode node, String propName) {
        StringBuffer buf = new StringBuffer();
        HashMap map = node.getAttributeAnnotations().get(propName);
        if (map != null) {
            boolean hasAtts = false;
            if (map.keySet().size() > 0) {
                hasAtts = true;
                buf.append("[");
            }
            for (Iterator i = map.keySet().iterator(); i.hasNext();) {
                String name = (String)i.next();
                Object obj  = map.get(name);
                String value = ConvertUtils.convert(obj);
                buf.append(name + "=\"" + value  +"\" ");
            }
            if (hasAtts) buf.append("]");
        }
        return buf.toString();
    }
    
    
    public String getAssociationName(JMatterAssociationEdge edge) {
        String middle = edge.getMiddleLabel();
        if (middle != null && !"".equals(middle)) {
            return middle;
        } else {
            return ((JMatterClassNode)edge.getStart()).getName() +
            ((JMatterClassNode)edge.getEnd()).getName() ;      
        }
    }
    
    public String getStartClass(JMatterAssociationEdge edge) {
        return edge.className(((JMatterClassNode)edge.getStart()).getName());
    }
    public String getEndClass(JMatterAssociationEdge edge) {
        return edge.className(((JMatterClassNode)edge.getEnd()).getName());
    }    
    
    public String startAnnotation(JMatterAssociationEdge edge) {

        if (!edge.getStartNavigable() || !"".equals(edge.getStartAnnotations().getText()) || edge.getBentStyle() != BentStyle.HVH) {
            StringBuffer buf = new StringBuffer("[");
            if (!edge.getStartNavigable()) buf.append("no_navigate");
            if ( !"".equals(edge.getStartAnnotations().getText())) {
                buf.append(" " + edge.getStartAnnotations().getText());
            }

            if (edge.getBentStyle() != BentStyle.HVH) 
                buf.append(" bentStyle=\"" + edge.getBentStyle().stringValue() + "\"");      

            buf.append("]");
            return buf.toString();
        }
        else return "";
        
    }
    
    public String endAnnotation(JMatterAssociationEdge edge) {
        if (!edge.getEndNavigable() || !"".equals(edge.getEndAnnotations().getText())) {
            StringBuffer buf = new StringBuffer("[");
            if (!edge.getEndNavigable()) buf.append("no_navigate");
            if ( !"".equals(edge.getEndAnnotations().getText())) {
                buf.append(" " + edge.getEndAnnotations().getText());
            }
            buf.append("]");
            return buf.toString();
        }
        else return "";
    }
        
    
    public String getStart(JMatterAssociationEdge edge) {
        StringBuffer s = new StringBuffer(getStartClass(edge));
        if (edge.getStartLabel() != null ) s.append(" as " + edge.getStartLabel() + " ");
        s.append(startAnnotation(edge));
        return s.toString();
    }
    
    
    public String getEnd(JMatterAssociationEdge edge) {
        StringBuffer s = new StringBuffer(getEndClass(edge));
        if (edge.getEndLabel() != null ) s.append(" as " + edge.getEndLabel() + " ");
        s.append(endAnnotation(edge));
        return s.toString();
    }    
    
}

/*
 * Formatter.java
 *
 * Created on July 31, 2006, 1:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package umlc.codeGeneration.jmatter;

import umlc.parseTree.*;
import java.util.*;

/**
 *
 * @author ryan
 */
public class Formatter {
    
    /** Creates a new instance of Formatter */
    public Formatter() {
    }
    
    
    public String formatPropertyAnnotations(UmlAttribute attribute) {
        StringBuffer annotations = new StringBuffer();
        if (attribute.hasAnnotation("label") || attribute.hasAnnotation("colsize") || attribute.hasAnnotation("displaysize")) {
            boolean needComma = false;
            if (attribute.hasAnnotation("label")) {
                annotations.append("label=\"" + attribute.annotationValue("label") + "\"");
                needComma = true;
            }
            if (attribute.hasAnnotation("colsize")) {
                if (needComma) annotations.append(", ");
                annotations.append("colsize=" + attribute.annotationValue("colsize") );
                needComma = true;
            }
            if (attribute.hasAnnotation("displaysize")) {
                if (needComma) annotations.append(", ");
                annotations.append("displaysize=" + attribute.annotationValue("displaysize") );
                needComma = true;
            }
        }
        return annotations.toString();
    }
    
    
    public String formatFlatten(Collection attribtutes) {
        StringBuffer flatten = new StringBuffer("public static String[] flattenIntoParent = {");
        int flattenCount = 0;
        for (Iterator i=attribtutes.iterator(); i.hasNext();) {
            UmlAttribute attribute = (UmlAttribute) i.next();
            if (attribute.hasAnnotation("flatten")) {
                if (flattenCount > 0) flatten.append(", ");
                flatten.append("\""+ attribute.name +"\"");
                flattenCount++;
            }
        }
        if (flattenCount > 0) return flatten.toString() + "}";
        else return "";
        
    }

    public String formatIdentities(Collection attribtutes) {
        StringBuffer ident = new StringBuffer("public static String[] identities = {");
        int identCount = 0;
        for (Iterator i=attribtutes.iterator(); i.hasNext();) {
            UmlAttribute attribute = (UmlAttribute) i.next();
            if (attribute.hasAnnotation("identity") || attribute.hasAnnotation("unique")) {
                if (identCount > 0) ident.append(", ");
                ident.append("\""+ attribute.name +"\"");
                identCount++;
            }
        }
        if (identCount > 0) return ident.toString() + "}";
        else return "";
        
    }    
    
    
    
    public String formatTabs(Collection attribtutes, Collection associationEnds) {
        StringBuffer tabs = new StringBuffer("public static String[] tabViews = {");
        int tabsCount = 0;
        for (Iterator i=attribtutes.iterator(); i.hasNext();) {
            UmlAttribute attribute = (UmlAttribute) i.next();
            if (attribute.hasAnnotation("tab")) {
                if (tabsCount > 0) tabs.append(", ");
                tabs.append("\""+ attribute.name +"\"");
                tabsCount++;
            }
        }
        for (Iterator i=associationEnds.iterator(); i.hasNext();) {
            AssociationEnd end = (AssociationEnd) i.next();
            if (end.getOther().hasAnnotation("tab")) {
                if (tabsCount > 0) tabs.append(", ");
                tabs.append("\""+ end.getOtherEndKnownAs() +"\"");
                tabsCount++;
            }
        }        
          
        if (tabsCount > 0) return tabs.toString() + "}";
        else return "";
        
    }   
    
    public String formatFieldOrder(Collection attribtutes, Collection associationEnds) {
        StringBuffer tabs = new StringBuffer("public static String[] fieldOrder  = {");
        int count = 0;
        for (Iterator i=attribtutes.iterator(); i.hasNext();) {
            UmlAttribute attribute = (UmlAttribute) i.next();
            if (count > 0) tabs.append(", ");
            tabs.append("\""+ attribute.name +"\"");
            count++;

        }
        for (Iterator i=associationEnds.iterator(); i.hasNext();) {
            AssociationEnd end = (AssociationEnd) i.next();

            if (count > 0) tabs.append(", ");
            tabs.append("\""+ end.getOtherEndKnownAs() +"\"");
            count++;
            
        }        
          
        if (count > 0) return tabs.toString() + "}";
        else return "";
        
    }       
    
}

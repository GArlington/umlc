/*
 * UmlcFromVioletGenerator.java
 *
 * Created on August 5, 2006, 8:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package umlc.codeGeneration.umlc;

import com.horstmann.violet.InheritanceEdge;
import com.horstmann.violet.JMatterClassDiagramGraph;
import com.horstmann.violet.JMatterClassNode;
import com.horstmann.violet.framework.Node;
import com.horstmann.violet.PackageNode;
import com.horstmann.violet.framework.Edge;
import com.horstmann.violet.framework.JMatterAssociationEdge;
import java.io.*;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import java.util.*;

/**
 *
 * @author ryan
 */
public class UmlcFromVioletGenerator {
    
    JMatterClassDiagramGraph graph;
    
    /** Creates a new instance of UmlcFromVioletGenerator */
    public UmlcFromVioletGenerator(JMatterClassDiagramGraph graph) {
        this.graph = graph;
    }
    
    
    public void generate(File file) {
        Template template = null;
        VelocityContext context = new VelocityContext();
        
        String packageName = "com.u2d.generated";
        ArrayList clazzes = new ArrayList();
        
        for (Iterator i = graph.getNodes().iterator(); i.hasNext();) {
            Node n = (Node) i.next();
            if (n instanceof PackageNode) {
                packageName = ((PackageNode)n).getName();
            }
            if (n instanceof JMatterClassNode) {
                clazzes.add(n);
            }
        }
        
        HashMap extendsNames = new HashMap();
        ArrayList edges = new ArrayList();
        
        for (Iterator i = graph.getEdges().iterator(); i.hasNext();) {
            Edge n = (Edge) i.next();
            if (n instanceof InheritanceEdge) {
                String start = ((JMatterClassNode)((InheritanceEdge)n).getStart()).getName();
                String end   = ((JMatterClassNode)((InheritanceEdge)n).getEnd()  ).getName();
                extendsNames.put(start, end);
                //System.out.println("inher: " + start + " , " + end);
            }
            if (n instanceof JMatterAssociationEdge) {
                edges.add(n);   
            }
            
        }
        
        context.put("packageName", packageName);
        context.put("clazzes", clazzes);
        context.put("relationships", edges);
        context.put("help", new Helper());
        context.put("extendsNames", extendsNames);

        try {
            Properties p = new Properties(); 
            p.setProperty("resource.loader", "class");
            
            p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            Velocity.init( p );
            
            FileWriter w   = new FileWriter(file);
	    template = Velocity.getTemplate("umlc/codeGeneration/umlc/umlc.vm");
            template.merge( context, w );
            w.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }         
    }
    
    

    
    
}

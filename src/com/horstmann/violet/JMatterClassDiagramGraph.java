/*
 * JMatterClassDiagramGraph.java
 *
 * Created on August 2, 2006, 2:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.horstmann.violet;



import java.awt.geom.Point2D;

import com.horstmann.violet.framework.Edge;
import com.horstmann.violet.framework.Graph;
import com.horstmann.violet.framework.Node;
import com.horstmann.violet.framework.*;
/**
 *
 * @author ryan
 */
public class JMatterClassDiagramGraph extends Graph {

   public boolean connect(Edge e, Point2D p1, Point2D p2)
   {
      Node n1 = findNode(p1);
      Node n2 = findNode(p2);
      // if (n1 == n2) return false;
      return super.connect(e, p1, p2);
   }

   public Node[] getNodePrototypes()
   {
      return NODE_PROTOTYPES;
   }

   public Edge[] getEdgePrototypes()
   {
      return EDGE_PROTOTYPES;
   }

   private static final Node[] NODE_PROTOTYPES = new Node[2];

   private static final Edge[] EDGE_PROTOTYPES = new Edge[2];

   static
   {
      NODE_PROTOTYPES[0] = new JMatterClassNode();
      NODE_PROTOTYPES[1] = new PackageNode();
      //NODE_PROTOTYPES[2] = new NoteNode(); // not yet

      InheritanceEdge inheritance = new InheritanceEdge();
      EDGE_PROTOTYPES[0] = inheritance;


      JMatterAssociationEdge association = new JMatterAssociationEdge();
      association.setBentStyle(BentStyle.HVH);
      EDGE_PROTOTYPES[1] = association;



      //EDGE_PROTOTYPES[2] = new NoteEdge();
   }
    
}

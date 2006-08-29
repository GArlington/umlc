/*
 * JMatterClassNode.java
 *
 * Created on August 2, 2006, 2:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.horstmann.violet;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.framework.Graph;
import com.horstmann.violet.framework.Grid;
import com.horstmann.violet.framework.MultiLineString;

import com.horstmann.violet.framework.Attribute;
import com.horstmann.violet.framework.Node;
import com.horstmann.violet.framework.RectangularNode;
import java.awt.Color;
import java.awt.GradientPaint;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author ryan
 */
public class JMatterClassNode extends RectangularNode{
    
    /** Creates a new instance of JMatterClassNode */
    public JMatterClassNode() {
      name = new String();  
      abstractClass = false;
        
      this.annotationNameValue = new HashMap();
      this.attributeAnnotations = new HashMap<String, HashMap>();
      this.attributeNameType = new LinkedHashMap<String, String>();
      
      nameHolder = new MultiLineString();
      nameHolder.setSize(MultiLineString.SMALL);
      

      
      attributes = new Attribute();
      attributes.setJustification(MultiLineString.LEFT);
      methods = new MultiLineString();
      methods.setJustification(MultiLineString.LEFT);
      setBounds(new Rectangle2D.Double(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT));
      midHeight = DEFAULT_COMPARTMENT_HEIGHT;
      botHeight = DEFAULT_COMPARTMENT_HEIGHT;
    }
    
   public void calculateProperties() {
       StringBuffer buf = new StringBuffer();
       for (Iterator<String> i = this.attributeNameType.keySet().iterator(); i.hasNext();) {
           String name = i.next();
           String type = attributeNameType.get(name);
           buf.append(name + " " + type + ";\n");
       }
       attributes.setText(buf.toString());
          if (this.annotationNameValue.containsKey("icon16")) {
              ImageIcon small = null;
              Object f = annotationNameValue.get("icon16");
              if (f instanceof java.io.File) {
                  small = new ImageIcon(((java.io.File)f).getAbsolutePath());
              }
              if (f instanceof String) {
                  small = new ImageIcon ((String)f);
              }
              if (small != null) {
                  nameHolder.setImageIcon(small);
              }

          }       
   } 
    
    
   public void draw(Graphics2D g2)
   {
      super.draw(g2);
//      if (this.annotationNameValue.containsKey("icon16")) {
//          ImageIcon small = null;
//          Object f = annotationNameValue.get("icon16");
//          if (f instanceof java.io.File) {
//              small = new ImageIcon(((java.io.File)f).getAbsolutePath());
//          }
//          if (f instanceof String) {
//              small = new ImageIcon ((String)f);
//          }
//          if (small != null) {
//              small.paintIcon(null,g2, (int)getBounds().getX() + 2, (int)getBounds().getY() + 2);
//          }
//          
//      }
      
      
      Rectangle2D top = new Rectangle2D.Double(getBounds().getX(),
         getBounds().getY(), getBounds().getWidth(), 
         getBounds().getHeight() - midHeight - botHeight);
      
      g2.draw(top);
      if (this.annotationNameValue.containsKey("color")) {
          Color from = (Color) annotationNameValue.get("color");
          GradientPaint paint = new GradientPaint((float)getBounds().getX() + 1,(float)getBounds().getY() + 1, from, (float)getBounds().getX() + (float)getBounds().getWidth(),(float)getBounds().getY() + 1, Color.WHITE );
          g2.setPaint(paint);
          Rectangle2D top2 = new Rectangle2D.Double(getBounds().getX() + 1,
              getBounds().getY() + 1, getBounds().getWidth() -1, 
              getBounds().getHeight() - midHeight - botHeight);
          g2.fill(top2);
    
      }
      
      
      
      
      nameHolder.draw(g2, top);
      Rectangle2D mid = new Rectangle2D.Double(top.getX(),
         top.getMaxY(), top.getWidth(), midHeight);
      g2.draw(mid);
      attributes.draw(g2, mid);
      Rectangle2D bot = new Rectangle2D.Double(top.getX(),
         mid.getMaxY(), top.getWidth(), botHeight);
      g2.draw(bot);
      methods.draw(g2, bot);
   }

   public void layout(Graph g, Graphics2D g2, Grid grid)
   {
      Rectangle2D min = new Rectangle2D.Double(0, 0,
         DEFAULT_WIDTH, DEFAULT_COMPARTMENT_HEIGHT);
      Rectangle2D top = nameHolder.getBounds(g2); 
      top.add(min);
      Rectangle2D mid = attributes.getBounds(g2);
      Rectangle2D bot = methods.getBounds(g2);

      midHeight = mid.getHeight();
      botHeight = bot.getHeight();
      if (midHeight == 0 && botHeight == 0)
      {
         top.add(new Rectangle2D.Double(0, 0, 
                    DEFAULT_WIDTH, 
                    DEFAULT_HEIGHT));
      }
      else
      {
         mid.add(min);
         bot.add(min);
         midHeight = mid.getHeight();
         botHeight = bot.getHeight();
      }

      Rectangle2D b = new Rectangle2D.Double(
         getBounds().getX(), getBounds().getY(),
         Math.max(top.getWidth(), Math.max(mid.getWidth(),
            bot.getWidth())), 
         top.getHeight() + midHeight + botHeight);
      grid.snap(b);
      setBounds(b);
   }

   public boolean addNode(Node n, Point2D p)
   {
      return n instanceof PointNode;
   }
   

   public Object clone()
   {
      JMatterClassNode cloned = (JMatterClassNode)super.clone();
      cloned.attributes = (Attribute)attributes.clone();
      cloned.methods = (MultiLineString)methods.clone();

      cloned.nameHolder  = (MultiLineString)nameHolder.clone();
      cloned.name = new String(name);
      cloned.abstractClass = abstractClass;
      
      cloned.annotationNameValue = new HashMap();

      cloned.attributeAnnotations = new HashMap<String, HashMap>();
      
      
      cloned.attributeNameType = new LinkedHashMap();
      for (Iterator<String> i = attributeNameType.keySet().iterator(); i.hasNext();) {
          String name = i.next();
          String type = attributeNameType.get(name);
          cloned.attributeNameType.put(name, type);
      }
      
      
      
      
      
      return cloned;
   }

   private double midHeight;
   private double botHeight;

   private Attribute attributes;
   private MultiLineString methods;

   private static int DEFAULT_COMPARTMENT_HEIGHT = 20;
   private static int DEFAULT_WIDTH = 100;
   private static int DEFAULT_HEIGHT = 60;    

    /**
     * Holds value of property name.
     */
    private MultiLineString nameHolder;

    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public MultiLineString getNameHolder() {
        return this.nameHolder;
    }

    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setNameHolder(MultiLineString name) {
        this.nameHolder = name;
    }

    /**
     * Holds value of property abstractClass .
     */
    private boolean abstractClass ;

    /**
     * Getter for property abstractClass .
     * @return Value of property abstractClass .
     */
    public boolean isAbstractClass () {
        return this.abstractClass ;
    }

    /**
     * Setter for property abstractClass .
     * @param abstractClass  New value of property abstractClass .
     */
    public void setAbstractClass (boolean abstractClass ) {
        this.abstractClass  = abstractClass ;
        if (abstractClass) nameHolder.setText("<Abstract>\n" + name);
        else               nameHolder.setText( name);
    }

    /**
     * Holds value of property nameHolder.
     */
    private String name;

    /**
     * Getter for property nameHolder.
     * @return Value of property nameHolder.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property nameHolder.
     * @param nameHolder New value of property nameHolder.
     */
    public void setName(String nameHolder) {
        this.nameHolder.setText(nameHolder);
        this.name = nameHolder;
    }

    /**
     * Holds value of property annotationNameValue.
     */
    private java.util.HashMap annotationNameValue;

    /**
     * Getter for property annotationNameValue.
     * @return Value of property annotationNameValue.
     */
    public java.util.HashMap getAnnotationNameValue() {
        return this.annotationNameValue;
    }

    /**
     * Setter for property annotationNameValue.
     * @param annotationNameValue New value of property annotationNameValue.
     */
    public void setAnnotationNameValue(java.util.HashMap annotationNameValue) {
        this.annotationNameValue = annotationNameValue;
    }

    /**
     * Holds value of property attributeNameType.
     */
    private java.util.LinkedHashMap<String, String> attributeNameType;

    /**
     * Getter for property attributeNameType.
     * @return Value of property attributeNameType.
     */
    public java.util.Map<String, String> getAttributeNameType() {
        return this.attributeNameType;
    }

    /**
     * Setter for property attributeNameType.
     * @param attributeNameType New value of property attributeNameType.
     */
    public void setAttributeNameType(java.util.LinkedHashMap<String, String> attributeNameType) {
        this.attributeNameType = attributeNameType;
    }

    /**
     * Holds value of property attributeAnnotations.
     */
    private java.util.HashMap<String, java.util.HashMap> attributeAnnotations;

    /**
     * Getter for property attributeAnnotations.
     * @return Value of property attributeAnnotations.
     */
    public java.util.HashMap<String, java.util.HashMap> getAttributeAnnotations() {
        return this.attributeAnnotations;
    }

    /**
     * Setter for property attributeAnnotations.
     * @param attributeAnnotations New value of property attributeAnnotations.
     */
    public void setAttributeAnnotations(java.util.HashMap<String, java.util.HashMap> attributeAnnotations) {
        this.attributeAnnotations = attributeAnnotations;
    }
}

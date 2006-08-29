/*
 * JMatterAssociationEdge.java
 *
 * Created on August 2, 2006, 9:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.horstmann.violet.framework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JLabel;

import com.horstmann.violet.ArrowHead;
import com.horstmann.violet.LineStyle;
import com.horstmann.violet.BentStyle;
import com.horstmann.violet.JMatterClassNode;
/**
 *
 * @author ryan
 */
public class JMatterAssociationEdge  extends ShapeEdge{
    
    /** Creates a new instance of JMatterAssociationEdge */
    public JMatterAssociationEdge() {
       bentStyle = BentStyle.STRAIGHT;
        lineStyle = LineStyle.SOLID;
      startArrowHead = ArrowHead.V;
      endArrowHead = ArrowHead.V;
      startLabel = "";
      middleLabel = "";
      endLabel = "";
      this.startNavigable = true;
      this.endNavigable   = true;
      this.startMultiplicity = null;
      this.endMultiplicity   = null;
      this.startAnnotations = new MultiLineString();
      this.endAnnotations   = new MultiLineString();
    }
  /**
      Sets the line style property.
      @param newValue the new value
   */
   public void setLineStyle(LineStyle newValue) { lineStyle = newValue; }

   /**
      Gets the line style property.
      @return the line style
   */
   public LineStyle getLineStyle() { return lineStyle; }

   /**
      Sets the start arrow head property
      @param newValue the new value
   */
   public void setStartArrowHead(ArrowHead newValue) { startArrowHead = newValue; }

   /**
      Gets the start arrow head property
      @return the start arrow head style
   */
   public ArrowHead getStartArrowHead() { return startArrowHead; }

   /**
      Sets the end arrow head property
      @param newValue the new value
   */
   public void setEndArrowHead(ArrowHead newValue) { endArrowHead = newValue; }

   /**
      Gets the end arrow head property
      @return the end arrow head style
   */
   public ArrowHead getEndArrowHead() { return endArrowHead; }

   /**
      Sets the start label property
      @param newValue the new value
   */
   public void setStartLabel(String newValue) { startLabel = newValue; }

   /**
      Gets the start label property
      @return the label at the start of the edge
   */
   public String getStartLabel() { return startLabel; }

   /**
      Sets the middle label property
      @param newValue the new value
   */
   public void setMiddleLabel(String newValue) { middleLabel = newValue; }

   /**
      Gets the middle label property
      @return the label at the middle of the edge
   */
   public String getMiddleLabel() { return middleLabel; }

   /**
      Sets the end label property
      @param newValue the new value
   */
   public void setEndLabel(String newValue) { endLabel = newValue; }

   /**
      Gets the end label property
      @return the label at the end of the edge
   */
   public String getEndLabel() { return endLabel; }

   /**
      Draws the edge.
      @param g2 the graphics context
   */
   public void draw(Graphics2D g2)
   {
      ArrayList points = getPoints();
      
      Stroke oldStroke = g2.getStroke();
      g2.setStroke(lineStyle.getStroke());
      g2.draw(getSegmentPath());
      g2.setStroke(oldStroke);
      startArrowHead.draw(g2, (Point2D)points.get(1),
         (Point2D)points.get(0));
      endArrowHead.draw(g2, (Point2D)points.get(points.size() - 2),
         (Point2D)points.get(points.size() - 1));

      drawString(g2, (Point2D)points.get(1), (Point2D)points.get(0), 
            startArrowHead, startLabel, false);
      
      if (startMultiplicity != null)
          drawMultString(g2, (Point2D)points.get(1), (Point2D)points.get(0), 
                startArrowHead, startMultiplicity);
      
      
      drawString(g2, (Point2D)points.get(points.size() / 2 - 1),
            (Point2D)points.get(points.size() / 2),
            null, middleLabel, true);
      drawString(g2, (Point2D)points.get(points.size() - 2),
            (Point2D)points.get(points.size() - 1), 
            endArrowHead, endLabel, false);
      if (endMultiplicity != null)
          drawMultString(g2, (Point2D)points.get(points.size() - 2),
                (Point2D)points.get(points.size() - 1), 
                endArrowHead, this.endMultiplicity);
   }

   /**
      Draws a string.
      @param g2 the graphics context
      @param p an endpoint of the segment along which to
      draw the string
      @param q the other endpoint of the segment along which to
      draw the string
      @param s the string to draw
      @param center true if the string should be centered
      along the segment
   */
   private static void drawString(Graphics2D g2, 
      Point2D p, Point2D q, ArrowHead arrow, String s, boolean center)
   {
      if (s == null || s.length() == 0) return;
      label.setText("<html>" + s + "</html>");
      label.setFont(g2.getFont());
      Dimension d = label.getPreferredSize();      
      label.setBounds(0, 0, d.width, d.height);

      Rectangle2D b = getStringBounds(g2, p, q, arrow, s, center);
      
      Color oldColor = g2.getColor();
      g2.setColor(g2.getBackground());
      g2.fill(b);
      g2.setColor(oldColor);
      
      g2.translate(b.getX(), b.getY());
      label.paint(g2);
      g2.translate(-b.getX(), -b.getY());        
   }

   /**
      Draws a string.
      @param g2 the graphics context
      @param p an endpoint of the segment along which to
      draw the string
      @param q the other endpoint of the segment along which to
      draw the string
      @param s the string to draw
      @param center true if the string should be centered
      along the segment
   */
   private static void drawMultString(Graphics2D g2, 
      Point2D p, Point2D q, ArrowHead arrow, String s)
   {
      if (s == null || s.length() == 0) return;
      label.setText("<html>" + s + "</html>");
      label.setFont(g2.getFont());
      Dimension d = label.getPreferredSize();      
      label.setBounds(0, 0, d.width, d.height);

      Rectangle2D b = getMultStringBounds(g2, p, q, arrow, s);
      
      Color oldColor = g2.getColor();
      g2.setColor(g2.getBackground());
      g2.fill(b);
      g2.setColor(oldColor);
      
      g2.translate(b.getX(), b.getY());
      label.paint(g2);
      g2.translate(-b.getX(), -b.getY());        
   }   
   
   /**
      Computes the attachment point for drawing a string.
      @param g2 the graphics context
      @param p an endpoint of the segment along which to
      draw the string
      @param q the other endpoint of the segment along which to
      draw the string
      @param b the bounds of the string to draw
      @param center true if the string should be centered
      along the segment
      @return the point at which to draw the string
   */
   private static Point2D getAttachmentPoint(Graphics2D g2, 
      Point2D p, Point2D q, ArrowHead arrow, Dimension d, boolean center)
   {    
      final int GAP = 3;
      double xoff = GAP;
      double yoff = -GAP - d.getHeight();
      Point2D attach = q;
      if (center)
      {
         if (p.getX() > q.getX()) 
         { 
            return getAttachmentPoint(g2, q, p, arrow, d, center); 
         }
         attach = new Point2D.Double((p.getX() + q.getX()) / 2, 
            (p.getY() + q.getY()) / 2);
         if (p.getY() < q.getY())
            yoff =  - GAP - d.getHeight();
         else if (p.getY() == q.getY())
            xoff = -d.getWidth() / 2;
         else
            yoff = GAP;
      }
      else 
      {
         if (p.getX() < q.getX())
         {
            xoff = -GAP - d.getWidth();
         }
         if (p.getY() > q.getY())
         {
            yoff = GAP;
         }
         if (arrow != null)
         {
            Rectangle2D arrowBounds = arrow.getPath(p, q).getBounds2D();
            if (p.getX() < q.getX())
            {
               xoff -= arrowBounds.getWidth();
            }
            else
            {
               xoff += arrowBounds.getWidth();
            }
         }
      }
      return new Point2D.Double(attach.getX() + xoff, attach.getY() + yoff);
   }

      /**
      Computes the attachment point for drawing a string.
      @param g2 the graphics context
      @param p an endpoint of the segment along which to
      draw the string
      @param q the other endpoint of the segment along which to
      draw the string
      @param b the bounds of the string to draw
      @param center true if the string should be centered
      along the segment
      @return the point at which to draw the string
   */
   private static Point2D getMultAttachmentPoint(Graphics2D g2, 
      Point2D p, Point2D q, ArrowHead arrow, Dimension d)
   {    
      final int GAP = 3;
      double xoff = GAP  ;
      double yoff = +GAP;
      Point2D attach = q;

      //System.out.println("Multi attach---------");
      //System.out.println("P.X: " + p.getX() + " P.Y: " + p.getY());
      //System.out.println("Q.X: " + q.getX() + " Q.Y: " + q.getY());
      
      
     if (p.getX() < q.getX()) 
     {
        xoff = -GAP - d.getWidth() ;
     }
     if (p.getX() == q.getX()) {
          yoff = 0;
          xoff = (-GAP - d.getWidth()) * 2 ;
          if (p.getY() < q.getY()) {
              yoff = yoff - GAP - d.getHeight();
          } else {
              yoff = yoff + GAP;
          }
     }

     if (arrow != null)
     {
        Rectangle2D arrowBounds = arrow.getPath(p, q).getBounds2D();
        if (p.getX() < q.getX())
        {
           xoff -= arrowBounds.getWidth();
        }
        else
        {
           xoff += arrowBounds.getWidth();
        }
     }
      //System.out.println("xoff: " + xoff + " yoff: " + yoff);
      //System.out.println("End -----------------");
      return new Point2D.Double(attach.getX() + xoff, attach.getY() + yoff);
   }
   
   /**
      Computes the extent of a string that is drawn along a line segment.
      @param g2 the graphics context
      @param p an endpoint of the segment along which to
      draw the string
      @param q the other endpoint of the segment along which to
      draw the string
      @param s the string to draw
      @param center true if the string should be centered
      along the segment
      @return the rectangle enclosing the string
   */
   private static Rectangle2D getStringBounds(Graphics2D g2, 
      Point2D p, Point2D q, ArrowHead arrow, String s, boolean center)
   {
      if (g2 == null) return new Rectangle2D.Double();
      if (s == null || s.equals("")) return new Rectangle2D.Double(q.getX(), q.getY(), 0, 0);
      label.setText("<html>" + s + "</html>");
      label.setFont(g2.getFont());
      Dimension d = label.getPreferredSize();
      Point2D a = getAttachmentPoint(g2, p, q, arrow, d, center);
      return new Rectangle2D.Double(a.getX(), a.getY(), d.getWidth(), d.getHeight());
   }

   /**
      Computes the extent of a string that is drawn along a line segment.
      @param g2 the graphics context
      @param p an endpoint of the segment along which to
      draw the string
      @param q the other endpoint of the segment along which to
      draw the string
      @param s the string to draw
      @param center true if the string should be centered
      along the segment
      @return the rectangle enclosing the string
   */
   private static Rectangle2D getMultStringBounds(Graphics2D g2, 
      Point2D p, Point2D q, ArrowHead arrow, String s)
   {
      if (g2 == null) return new Rectangle2D.Double();
      if (s == null || s.equals("")) return new Rectangle2D.Double(q.getX(), q.getY(), 0, 0);
      label.setText("<html>" + s + "</html>");
      label.setFont(g2.getFont());
      Dimension d = label.getPreferredSize();
      Point2D a = getMultAttachmentPoint(g2, p, q, arrow, d);
      return new Rectangle2D.Double(a.getX(), a.getY(), d.getWidth(), d.getHeight());
   }   
   
   public Rectangle2D getBounds(Graphics2D g2)
   {
      ArrayList points = getPoints();
      Rectangle2D r = super.getBounds(g2);
      r.add(getStringBounds(g2, 
               (Point2D) points.get(1), (Point2D) points.get(0), 
               startArrowHead, startLabel, false));
      r.add(getStringBounds(g2, 
               (Point2D) points.get(points.size() / 2 - 1),
               (Point2D) points.get(points.size() / 2), 
               null, middleLabel, true));
      r.add(getStringBounds(g2, 
               (Point2D) points.get(points.size() - 2),
               (Point2D) points.get(points.size() - 1), 
               endArrowHead, endLabel, false));
      return r;
   }

   public Shape getShape()
   {
      GeneralPath path = getSegmentPath();
      ArrayList points = getPoints();
      path.append(startArrowHead.getPath((Point2D)points.get(1),
            (Point2D)points.get(0)), false);
      path.append(endArrowHead.getPath((Point2D)points.get(points.size() - 2),
            (Point2D)points.get(points.size() - 1)), false);
      return path;
   }

   private GeneralPath getSegmentPath()
   {
      ArrayList points = getPoints();
      
      GeneralPath path = new GeneralPath();
      Point2D p = (Point2D) points.get(points.size() - 1);
      path.moveTo((float) p.getX(), (float) p.getY());
      for (int i = points.size() - 2; i >= 0; i--)
      {
         p = (Point2D) points.get(i);
         path.lineTo((float) p.getX(), (float) p.getY());
      }
      return path;
   }
   
   public Line2D getConnectionPoints()
   {
      ArrayList points = getPoints();
      return new Line2D.Double((Point2D) points.get(0),
         (Point2D) points.get(points.size() - 1));
   }


   /**
      Sets the bentStyle property
      @param newValue the bent style
   */
   public void setBentStyle(BentStyle newValue) { bentStyle = newValue; }
   /**
      Gets the bentStyle property
      @return the bent style
   */
   public BentStyle getBentStyle() { return bentStyle; }
   
   public ArrayList getPoints()
   {
      return bentStyle.getPath(getStart().getBounds(),
         getEnd().getBounds());
   }

   private BentStyle bentStyle;


   private LineStyle lineStyle;
   private ArrowHead startArrowHead;
   private ArrowHead endArrowHead;
   private String startLabel;
   private String middleLabel;
   private String endLabel;
   
   private static JLabel label = new JLabel();    

    /**
     * Holds value of property startNavigable.
     */
    private boolean startNavigable;

    /**
     * Getter for property startNavigable.
     * @return Value of property startNavigable.
     */
    public boolean getStartNavigable() {
        return this.startNavigable;
    }

    /**
     * Setter for property startNavigable.
     * @param startNavigable New value of property startNavigable.
     */
    public void setStartNavigable(boolean startNavigable) {
        if (startNavigable) this.startArrowHead = ArrowHead.V;
        else this.startArrowHead = ArrowHead.NONE;
        this.startNavigable = startNavigable;
    }

    /**
     * Holds value of property endNavigable.
     */
    private boolean endNavigable;

    /**
     * Getter for property endNavigable.
     * @return Value of property endNavigable.
     */
    public boolean getEndNavigable() {
        return this.endNavigable;
    }

    /**
     * Setter for property endNavigable.
     * @param endNavigable New value of property endNavigable.
     */
    public void setEndNavigable(boolean endNavigable) {
        if (endNavigable) this.endArrowHead = ArrowHead.V;
        else this.endArrowHead = ArrowHead.NONE;
        System.out.println("End: " + this.endArrowHead.toString());
        this.endNavigable = endNavigable;
    }

    /**
     * Holds value of property startMultiplicity.
     */
    private String startMultiplicity;

    /**
     * Getter for property startMultiplicity.
     * @return Value of property startMultiplicity.
     */
    public String getStartMultiplicity() {
        return this.startMultiplicity;
    }

    /**
     * Setter for property startMultiplicity.
     * @param startMultiplicity New value of property startMultiplicity.
     */
    public void setStartMultiplicity(String startMultiplicity) {
        this.startMultiplicity = startMultiplicity;
        calculateStartLabel();
    }

    /**
     * Holds value of property endMultiplicity.
     */
    private String endMultiplicity;

    /**
     * Getter for property endMultiplicity.
     * @return Value of property endMultiplicity.
     */
    public String getEndMultiplicity() {
        return this.endMultiplicity;
    }

    /**
     * Setter for property endMultiplicity.
     * @param endMultiplicity New value of property endMultiplicity.
     */
    public void setEndMultiplicity(String endMultiplicity) {
        this.endMultiplicity = endMultiplicity;
        calculateEndLabel();
    }

    /**
     * Holds value of property startName.
     */
    private String startName;

    /**
     * Getter for property startName.
     * @return Value of property startName.
     */
    public String getStartName() {
        return this.startName;
    }

    /**
     * Setter for property startName.
     * @param startName New value of property startName.
     */
    public void setStartName(String startName) {
        this.startName = startName;
        calculateStartLabel();
    }

    /**
     * Holds value of property endName.
     */
    private String endName;

    /**
     * Getter for property endName.
     * @return Value of property endName.
     */
    public String getEndName() {
        return this.endName;
    }

    /**
     * Setter for property endName.
     * @param endName New value of property endName.
     */
    public void setEndName(String endName) {
        this.endName = endName;
        calculateEndLabel();
    }
    
    private void calculateStartLabel() {
        this.startLabel = this.startName;
    }
    
    private void calculateEndLabel() {
        this.endLabel = this.endName;
    }

    /**
     * Holds value of property startAnnotations.
     */
    private MultiLineString startAnnotations;

    /**
     * Getter for property startAnnotations.
     * @return Value of property startAnnotations.
     */
    public MultiLineString getStartAnnotations() {
        return this.startAnnotations;
    }

    /**
     * Setter for property startAnnotations.
     * @param startAnnotations New value of property startAnnotations.
     */
    public void setStartAnnotations(MultiLineString startAnnotations) {
        this.startAnnotations = startAnnotations;
    }

    /**
     * Holds value of property endAnnotations.
     */
    private MultiLineString endAnnotations;

    /**
     * Getter for property endAnnotations.
     * @return Value of property endAnnotations.
     */
    public MultiLineString getEndAnnotations() {
        return this.endAnnotations;
    }

    /**
     * Setter for property endAnnotations.
     * @param endAnnotations New value of property endAnnotations.
     */
    public void setEndAnnotations(MultiLineString endAnnotations) {
        this.endAnnotations = endAnnotations;
    }

    public void connect(Node s, Node e) {
        super.connect(s, e);
        if (s instanceof JMatterClassNode) {
            String label = findBestName(((JMatterClassNode)s).getName(), "1");
            if (startName == null) this.setStartName(label);
            
            if (startMultiplicity == null) this.startMultiplicity  = "1";
        }
         if (e instanceof JMatterClassNode) {
            String label = findBestName(((JMatterClassNode)e).getName(), "*");
            if (endName == null)this.setEndName(label);
            if (endMultiplicity == null)  this.endMultiplicity    = "*";
        }       
        
        
    }

    public String className(String name) {
        return name.replaceAll("(\\[([^\\[\\]])*\\])|(<[^<>]*>)", "" );
    }
    
    
    public String findBestName(String className, String multiplicity) {
        if (className != null && !"".equals(className)) {
            String lastWord = className(className);
            lastWord = getNameLower(lastWord);
            if ("1".equals(multiplicity)) return lastWord;
            if ("*".equals(multiplicity)) return lastWord + "s";

            return lastWord;
        } else return null;
    }
    
     public String getNameLower(String name) {
        char chars[] = name.toCharArray();
 	chars[0] = Character.toLowerCase(chars[0]);
 	return new String(chars);
    }
    
}

package com.horstmann.violet;

import com.horstmann.violet.framework.EditorFrame;
import com.horstmann.violet.framework.GraphPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import umlc.codeGeneration.GeneratorIF;


public class ClassEditor extends JPanel {
    JTextField m_className = new JTextField();
    JTabbedPane m_jtabbedpane1 = new JTabbedPane();
    PropertySheetPanel m_classAnnotations = new PropertySheetPanel();
    JTextField m_newAttributeName = new JTextField();
    JComboBox m_newAttributeName1 = new JComboBox();
    JButton m_addAttribute = new JButton();
    JList m_currentAttributes = new JList();
    JButton m_up = new JButton();
    JButton m_down = new JButton();
    JButton m_remove = new JButton();
    PropertySheetPanel m_attributeProperties = new PropertySheetPanel();
    JCheckBox m_abstractFlag = new JCheckBox();
    
    /**
     * Default constructor
     */
    public ClassEditor() {
        initializePanel();
        
        
        m_classAnnotations.setSorting(true);
        m_classAnnotations.setDescriptionVisible(true);
        m_classAnnotations.setMode(com.l2fprod.common.propertysheet.PropertySheet.VIEW_AS_CATEGORIES);
        m_classAnnotations.setToolBarVisible(false);

        m_attributeProperties.setSorting(true);
        m_attributeProperties.setDescriptionVisible(true);
        m_attributeProperties.setMode(com.l2fprod.common.propertysheet.PropertySheet.VIEW_AS_CATEGORIES);
        m_attributeProperties.setToolBarVisible(false);

//m_classAnnotations.setPreferredSize(new Dimension(200, 200));
        //m_attributeProperties.setSize(new Dimension(200, 200));
        m_currentAttributes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private JMatterClassNode node;
    private GraphPanel  panel;
    private GeneratorIF generator;
    private AttributeListModel attributeModel;
    
    public ClassEditor(GeneratorIF generator, JMatterClassNode node, GraphPanel panel) {
        this();
        this.node = node;
        this.panel = panel;
        this.generator = generator;
        this.m_abstractFlag.setSelected(node.isAbstractClass());
        this.m_className.setText(node.getName());
        Set<String> names = generator.classAnnotationNames();
        for (Iterator<String> i = names.iterator(); i.hasNext(); ) {
            String name = i.next();
            DefaultProperty p = generator.getClassAnnotationByName(name);
            Object obj = node.getAnnotationNameValue().get(name);
            if (obj != null) p.setValue(obj);
            m_classAnnotations.addProperty(p);
            
        }
        
        Set<String> suggestedTypes = generator.suggestedPropertyTypes();
        Vector v = new Vector(suggestedTypes);
        v.insertElementAt("", 0);
        DefaultComboBoxModel cbm = new DefaultComboBoxModel(v);
        m_newAttributeName1.setModel(cbm);
        
        attributeModel = new AttributeListModel(node);
        m_currentAttributes.setModel(attributeModel);
        
        
        m_addAttribute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAttribute();
            }
        });
        
        m_remove.setEnabled(false);
        m_remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeAttribute();
            }
        });
        
        m_currentAttributes.addListSelectionListener(new ListSelectionListener() {
           public void valueChanged(ListSelectionEvent e) {
             if (e.getValueIsAdjusting() == false) {

                if (m_currentAttributes.getSelectedIndex() == -1) {
                //No selection, disable fire button.
                    //fireButton.setEnabled(false);
                    m_remove.setEnabled(false);
                } else {
                    m_remove.setEnabled(true);
                    showAnnotationsForSelectedProperty();
                }
             }
            }     
        });
        
        
    }
    
    private String currentSelectedProp = null;
    
    private void showAnnotationsForSelectedProperty() {
        // copy all the old ones out
        copyOutAttributeAnnotaions();
        
        
        int index = m_currentAttributes.getSelectedIndex();
        String attName = attributeModel.getAttributeNameAt(index);
        String type    = node.getAttributeNameType().get(attName);
        currentSelectedProp = attName;
        
        HashMap currentValues = node.getAttributeAnnotations().get(attName);
        if (currentValues == null) {
            currentValues = new HashMap();
            node.getAttributeAnnotations().put(attName, currentValues);
        }
        
        ArrayList<DefaultProperty> props = new ArrayList<DefaultProperty>();
        for (Iterator<String> i = generator.annotationNamesByPropertyType(type).iterator(); i.hasNext(); ) {
            String propName = (String) i.next();
            DefaultProperty prop = generator.getPropertyAnnotationByName(propName);
            prop.setValue(currentValues.get(propName));
            props.add(prop);
        }
        m_attributeProperties.setProperties((DefaultProperty[])props.toArray(new DefaultProperty[0]));
        
    }
    
    
    private void copyOutAttributeAnnotaions() {
        if (currentSelectedProp != null) {
            HashMap map = node.getAttributeAnnotations().get(currentSelectedProp);
            if (map == null) {
                map = new HashMap();
                node.getAttributeAnnotations().put(currentSelectedProp, map);
            }
            Property[] properties = m_attributeProperties.getProperties();
            for (int i = 0; i < properties.length; i ++) {
                Property prop = properties[i];
                Object value = prop.getValue();
                if (value != null)
                    map.put(prop.getName(), value);
                else 
                    map.remove(prop.getName());
            }            
        }        
    }
    
    
    private void addAttribute() {
        String name = m_newAttributeName.getText();
        String type = (String)m_newAttributeName1.getSelectedItem();
        if (type == null || "".equals(type) || name == null || "".equals(name)) {
             JOptionPane.showMessageDialog(EditorFrame.getMainFrame(), "Please provide a name and a type for the new attribute.", "Info Missing...", JOptionPane.ERROR_MESSAGE);                 
             return;
        }
        attributeModel.addAttribute(name, type);
        m_newAttributeName1.setSelectedIndex(0);
        m_newAttributeName.setText("");
        m_newAttributeName.requestFocusInWindow();
        m_currentAttributes.setSelectedIndex(node.getAttributeNameType().size() - 1);
    }
    
    private void removeAttribute() {
        if (m_currentAttributes.getSelectedIndex()>= 0) {
            int option = JOptionPane.showConfirmDialog(EditorFrame.getMainFrame(), "Are you sure you want to remove the selected attribute and all its annotations?");
            if (option == JOptionPane.OK_OPTION) {
                int index = m_currentAttributes.getSelectedIndex();
                attributeModel.removeAttributeAt(index);
                m_attributeProperties.setProperties(new Property[]{});
            }
        }
    }
    
    
    
    public void editingStopped() {
        node.setName(this.m_className.getText());
        
        Property[] properties = m_classAnnotations.getProperties();
        for (int i = 0; i < properties.length; i ++) {
            Property prop = properties[i];
            Object value = prop.getValue();
            if (value != null)
                node.getAnnotationNameValue().put(prop.getName(), value);
            else 
                node.getAnnotationNameValue().remove(prop.getName());
        }
        
        node.setAbstractClass(m_abstractFlag.isSelected());
        copyOutAttributeAnnotaions();
        
    }
    
    
    /**
      Adds a change listener to the list of listeners.
      @param listener the listener to add
     */
    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }
    
    /**
      Notifies all listeners of a state change.
      @param event the event to propagate
     */
    private void fireStateChanged(ChangeEvent event) {
        for (int i = 0; i < changeListeners.size(); i++) {
            ChangeListener listener = (ChangeListener)changeListeners.get(i);
            listener.stateChanged(event);
        }
    }
    
    private ArrayList changeListeners = new ArrayList();
    /**
     * Main method for panel
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(600, 400);
        frame.setLocation(100, 100);
        frame.getContentPane().add(new ClassEditor());
        frame.setVisible(true);
        
        frame.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent evt ) {
                System.exit(0);
            }
        });
    }
    
    /**
     * Adds fill components to empty cells in the first row and first column of the grid.
     * This ensures that the grid spacing will be the same as shown in the designer.
     * @param cols an array of column indices in the first row where fill components should be added.
     * @param rows an array of row indices in the first column where fill components should be added.
     */
    void addFillComponents( Container panel, int[] cols, int[] rows ) {
        Dimension filler = new Dimension(10,10);
        
        boolean filled_cell_11 = false;
        CellConstraints cc = new CellConstraints();
        if ( cols.length > 0 && rows.length > 0 ) {
            if ( cols[0] == 1 && rows[0] == 1 ) {
                /** add a rigid area  */
                panel.add( Box.createRigidArea( filler ), cc.xy(1,1) );
                filled_cell_11 = true;
            }
        }
        
        for( int index = 0; index < cols.length; index++ ) {
            if ( cols[index] == 1 && filled_cell_11 ) {
                continue;
            }
            panel.add( Box.createRigidArea( filler ), cc.xy(cols[index],1) );
        }
        
        for( int index = 0; index < rows.length; index++ ) {
            if ( rows[index] == 1 && filled_cell_11 ) {
                continue;
            }
            panel.add( Box.createRigidArea( filler ), cc.xy(1,rows[index]) );
        }
        
    }
    
    /**
     * Helper method to load an image file from the CLASSPATH
     * @param imageName the package and name of the file to load relative to the CLASSPATH
     * @return an ImageIcon instance with the specified image file
     * @throws IllegalArgumentException if the image resource cannot be loaded.
     */
    public ImageIcon loadImage( String imageName ) {
        try {
            ClassLoader classloader = getClass().getClassLoader();
            java.net.URL url = classloader.getResource( imageName );
            if ( url != null ) {
                ImageIcon icon = new ImageIcon( url );
                return icon;
            }
        } catch( Exception e ) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException( "Unable to load image: " + imageName );
    }
    
    public JPanel createPanel() {
        JPanel jpanel1 = new JPanel();
        FormLayout formlayout1 = new FormLayout("FILL:3DLU:NONE,FILL:3DLU:NONE,FILL:DEFAULT:NONE,FILL:3DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:3DLU:NONE","CENTER:3DLU:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:DEFAULT:GROW(1.0)");
        CellConstraints cc = new CellConstraints();
        jpanel1.setLayout(formlayout1);
        
        m_className.setName("className");
        jpanel1.add(m_className,cc.xy(5,4));
        
        m_jtabbedpane1.addTab("Class Annotations",null,createPanel1());
        m_jtabbedpane1.addTab("Class Attributes",null,createPanel2());
        jpanel1.add(m_jtabbedpane1,new CellConstraints(2,6,4,1,CellConstraints.DEFAULT,CellConstraints.FILL));
        
        JLabel jlabel1 = new JLabel();
        jlabel1.setText("Class Name");
        jpanel1.add(jlabel1,cc.xy(3,4));
        
        m_abstractFlag.setActionCommand("Abstract");
        m_abstractFlag.setName("abstractFlag");
        m_abstractFlag.setRolloverEnabled(true);
        jpanel1.add(m_abstractFlag,cc.xy(5,2));
        
        JLabel jlabel2 = new JLabel();
        jlabel2.setText("Abstract");
        jpanel1.add(jlabel2,cc.xy(3,2));
        
        addFillComponents(jpanel1,new int[]{ 1,2,3,4,5,6 },new int[]{ 1,2,3,4,5,6 });
        return jpanel1;
    }
    
    public JPanel createPanel1() {
        JPanel jpanel1 = new JPanel();
        FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE","CENTER:DEFAULT:GROW(1.0)");
        CellConstraints cc = new CellConstraints();
        jpanel1.setLayout(formlayout1);
        
        m_classAnnotations.setName("classAnnotations");
        JScrollPane jscrollpane1 = new JScrollPane();
        jscrollpane1.setViewportView(m_classAnnotations);
        jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        jpanel1.add(jscrollpane1,new CellConstraints(2,1,1,1,CellConstraints.DEFAULT,CellConstraints.FILL));
        
        addFillComponents(jpanel1,new int[]{ 1,3 },new int[]{ 1 });
        return jpanel1;
    }
    
    public JPanel createPanel2() {
        JPanel jpanel1 = new JPanel();
        FormLayout formlayout1 = new FormLayout("FILL:3DLU:NONE,FILL:DEFAULT:NONE,FILL:3DLU:NONE,FILL:DEFAULT:GROW(0.5),FILL:3DLU:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(0.5),FILL:3DLU:NONE,FILL:60DLU:NONE,FILL:3DLU:NONE,FILL:3DLU:NONE","CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:DEFAULT:GROW(1.0),CENTER:3DLU:NONE");
        CellConstraints cc = new CellConstraints();
        jpanel1.setLayout(formlayout1);
        
        JLabel jlabel1 = new JLabel();
        jlabel1.setText("Name");
        jpanel1.add(jlabel1,cc.xy(2,2));
        
        m_newAttributeName.setName("newAttributeName");
        jpanel1.add(m_newAttributeName,cc.xy(4,2));
        
        JLabel jlabel2 = new JLabel();
        jlabel2.setText("Type");
        jpanel1.add(jlabel2,cc.xy(6,2));
        
        m_newAttributeName1.setEditable(true);
        m_newAttributeName1.setName("newAttributeName");
        jpanel1.add(m_newAttributeName1,cc.xy(7,2));
        
        m_addAttribute.setActionCommand("Add");
        m_addAttribute.setName("addAttribute");
        m_addAttribute.setText("Add");
        jpanel1.add(m_addAttribute,cc.xywh(9,2,2,1));
        
        jpanel1.add(createPanel3(),new CellConstraints(2,4,9,1,CellConstraints.DEFAULT,CellConstraints.FILL));
        addFillComponents(jpanel1,new int[]{ 1,2,3,4,5,6,7,8,9,10,11 },new int[]{ 1,2,3,4,5 });
        return jpanel1;
    }
    
    public JPanel createPanel3() {
        JPanel jpanel1 = new JPanel();
        TitledBorder titledborder1 = new TitledBorder(null,"Current Attributes",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,null,new Color(49,106,196));
        jpanel1.setBorder(titledborder1);
        FormLayout formlayout1 = new FormLayout("FILL:3DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:3DLU:NONE,FILL:60DLU:NONE","CENTER:3DLU:NONE,CENTER:DEFAULT:GROW(1.0),CENTER:3DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:150DLU:GROW(0.5),CENTER:3DLU:NONE");
        CellConstraints cc = new CellConstraints();
        jpanel1.setLayout(formlayout1);
        
        m_currentAttributes.setName("currentAttributes");
        JScrollPane jscrollpane1 = new JScrollPane();
        jscrollpane1.setViewportView(m_currentAttributes);
        jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jpanel1.add(jscrollpane1,cc.xywh(2,2,1,6));
        
        m_up.setActionCommand("Up");
        m_up.setName("up");
        m_up.setText("Up");
        //jpanel1.add(m_up,cc.xy(4,2));
        
        m_down.setActionCommand("Down");
        m_down.setName("down");
        m_down.setText("Down");
        //jpanel1.add(m_down,cc.xy(4,4));
        
        m_remove.setActionCommand("Remove");
        m_remove.setName("remove");
        m_remove.setText("Remove");
        jpanel1.add(m_remove,cc.xy(4,7));
        
        JLabel jlabel1 = new JLabel();
        jlabel1.setText("Selected Attribute Properties");
        jpanel1.add(jlabel1,cc.xywh(2,9,3,1));
        
        m_attributeProperties.setName("attributeProperties");
        JScrollPane jscrollpane2 = new JScrollPane();
        jscrollpane2.setViewportView(m_attributeProperties);
        jscrollpane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscrollpane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jpanel1.add(m_attributeProperties,new CellConstraints(2,11,1,1,CellConstraints.DEFAULT,CellConstraints.FILL));
        
        addFillComponents(jpanel1,new int[]{ 1,2,3,4 },new int[]{ 1,2,3,4,5,6,7,8,9,10,11,12 });
        return jpanel1;
    }
    
    /**
     * Initializer
     */
    protected void initializePanel() {
        setLayout(new BorderLayout());
        add(createPanel(), BorderLayout.CENTER);
    }
    
    private class AttributeListModel extends AbstractListModel {

        JMatterClassNode node;
        
        public AttributeListModel(JMatterClassNode node) {
            this.node = node;
        }
        
        public int getSize() { return node.getAttributeNameType().keySet().size(); }
        public Object getElementAt(int index) { 
            Object[] names = node.getAttributeNameType().keySet().toArray();
            String name = (String) names[index];
            String type = node.getAttributeNameType().get(name);
            
            return name + " " + type + ";";
        }
        
        public String getAttributeNameAt(int index) {
            Object[] names = node.getAttributeNameType().keySet().toArray();
            return (String) names[index];            
        }
        
        
        public void removeAttributeAt(int index) {
            Object[] names = node.getAttributeNameType().keySet().toArray();
            String att = (String)names[index];
            node.getAttributeNameType().remove(att);
            node.getAttributeAnnotations().remove(att);
            this.fireIntervalRemoved(this, index, index );
        }
        
        public void addAttribute(String name, String type) {
            node.getAttributeNameType().put(name, type);   
            this.fireIntervalAdded(this, getSize()-1, getSize()-1);
            
         
        }
        
    }
    
    
}

package com.u2d.umlc.deployment;

//import com.jeta.forms.components.separator.TitledSeparator;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import javax.swing.event.DocumentEvent;
import org.apache.tools.ant.BuildEvent;
import tools.AntRunner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import tools.AntRunnerInterface;



public class JMatterProjectControlForm extends JPanel
{
   JTextField m_textProjectName = new JTextField();
   JTextField m_textBaseDir = new JTextField();
   JLabel m_labelProjectDir = new JLabel();
   JLabel m_labelProjectBuildFile = new JLabel();
   JTextPane m_jtextarea1 = new JTextPane();
   JButton m_btnGenerateCode = new JButton();
   JButton m_btnSchemaExport = new JButton();
   JButton m_btnRunProject = new JButton();
   JButton m_btnBaseDirChooser = new JButton();
   JButton m_btnGenerateProject = new JButton();
   JComboBox m_comboHibernateProps = new JComboBox();
   JButton m_btnNewProperties = new JButton();
   BuildListener buildListener;
   DocumentListener projectLocationListener;
   JProgressBar progress = new JProgressBar();
   
   
   private File projectFolder;
   private File projectBuild;
   private File jmatterBuild;
   
      // some spaces for nice indenting
   private String targetIndent = "   ";
   private String taskIndent = targetIndent + targetIndent;
   private String msgIndent = taskIndent + targetIndent;
   /**
    * Time of the start of the build
    */
   private long startTime = System.currentTimeMillis();

   /**
    * Convenience for local system line separator
    */
   protected final static String lSep = System.getProperty( "line.separator" );
   
    /**
     * Green
     */
    private Color GREEN = new Color( 0, 153, 51 );
    /**
     * Current font
     */
    private Font _font = null;

   /**
    * Default constructor
    */
   public JMatterProjectControlForm(DeploymentInformation info)
   {
      this.info = info;
      
      initializePanel();
      m_jtextarea1.setSize(300,400);
      Dimension d= new Dimension(300, 400);
      m_jtextarea1.setMaximumSize(d);
      m_jtextarea1.setPreferredSize(d);
      projectLocationListener = new DocumentListener() {
            public void removeUpdate(DocumentEvent e) {
                projectLocationChanged();
            }

            public void insertUpdate(DocumentEvent e) {
                projectLocationChanged();
            }

            public void changedUpdate(DocumentEvent e) {
                projectLocationChanged();
            }
          
      };
      
      

      
      
      this.m_textBaseDir.getDocument().addDocumentListener(projectLocationListener);
      this.m_textProjectName.getDocument().addDocumentListener(projectLocationListener);
      
      
      this.m_textBaseDir.setText(info.getJmatterDirectory());
      this.m_textProjectName.setText(info.getProjectName());      
      

      this.buildListener = new BuildListener() {
            public void taskStarted(BuildEvent event) {
                   String msg = taskIndent + "<" + event.getTask().getTaskName() + "> ";
                   log(  msg, Project.MSG_INFO );   
            }

            public void taskFinished(BuildEvent event) {
                String msg = taskIndent + "</" + event.getTask().getTaskName() + "> ";
                log(  msg, Project.MSG_INFO );                
            }

            public void targetStarted(BuildEvent event) {
                String msg = targetIndent + "<" + event.getTarget().getName() + "> ";
                log( msg, Project.MSG_INFO );
            }

            public void targetFinished(BuildEvent event) {
                String msg = targetIndent + "</" + event.getTarget().getName() + "> ";
                log(  msg, Project.MSG_INFO );
            }

            public void messageLogged(BuildEvent event) {
              String msg = event.getMessage();
              if ( msg == null || msg.length() == 0 ) {
                 msg = "" ;
              }
             
             int priority = event.getPriority();
             // Filter out messages based on priority
             if ( priority == Project.MSG_INFO || priority == Project.MSG_ERR ) {
                StringBuffer message = new StringBuffer();
                message.append( msgIndent );
                message.append( msg );
                log( message.toString(), priority );
             }
              
            }

            public void buildStarted(BuildEvent buildEvent) {
                log(  "===== BUILD STARTED =====" + lSep, Project.MSG_INFO );
                startTime = System.currentTimeMillis();
            }

            public void buildFinished(BuildEvent event) {
              Throwable error = event.getException();
              StringBuffer message = new StringBuffer();

              if ( error == null ) {
                 message.append( lSep );
                 message.append( "BUILD SUCCESSFUL" );
                 log(  message.toString(), Project.MSG_WARN );
              }
              else {
                 message.append( lSep );
                 message.append( "BUILD FAILED" );
                 message.append( lSep );

                if ( error instanceof BuildException ) {
                   message.append( error.toString() ).append( lSep );
                }
                else {
                   message.append( error.getMessage() ).append( lSep );
                }
                 
                 log( message.toString(), Project.MSG_ERR );
              }
              message = new StringBuffer();
              message.append( "Total time: " );
              message.append(  System.currentTimeMillis() - startTime  );
              message.append( lSep );
              // note: ConsolePluginHandler uses this message to set focus back to the buffer,
              // so if you change this message, be sure to update the ConsolePluginHandler 
              message.append( "===== BUILD FINISHED =====" ).append( lSep );
              message.append( new java.util.Date().toString()).append(lSep);

              String msg = message.toString();
              log(  msg, Project.MSG_INFO );
            }
          
      };
      
      this.m_btnGenerateProject.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent event){
            generateProject();
          }
      });
      this.m_btnGenerateCode.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent event){
            generateCode();
          }
      });
      this.m_btnSchemaExport.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent event){
            schemaExport();
          }
      });
      this.m_btnRunProject.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent event){
            runApp();
          }
      });
   }
   
   
   private void projectLocationChanged() {
        String base = m_textBaseDir.getText();
        if (!base.endsWith("/")) base = base + "/";
        File fbase = new File(base);
        
        jmatterBuild  = new File (base + "build.xml");
        
        
        projectFolder = new File (fbase.getParentFile() + "/" + m_textProjectName.getText() + "/");
        projectBuild  = new File (fbase.getParentFile() + "/" + m_textProjectName.getText() + "/build.xml");
        
        if (!jmatterBuild.exists()) {
            info.setState(DeploymentInformation.STATE_NO_JMATTER);
        }
        else {
            this.m_labelProjectDir.setText(projectFolder.getAbsolutePath());
            if (projectBuild.exists()) {
                this.m_labelProjectBuildFile.setText(projectBuild.getAbsolutePath());
                info.setState(DeploymentInformation.STATE_PROJECT);
            }
            else {
                this.m_labelProjectBuildFile.setText("Does not exist");
                info.setState(DeploymentInformation.STATE_NEW);
            }
        }
        stateSetup();
    }
   
   
   private void spinIt(String target, AntRunner runner) {
       AntRunnerInterface runIF = (AntRunnerInterface)spin.Spin.off(runner);
       try {
           progress.setIndeterminate(true);
           progress.setString(target);
           runIF.runTarget(target);
           progress.setIndeterminate(false);
           progress.setString("Finished");
       } catch (Exception e) {
           log("There was an error: " + e.getMessage(), Project.MSG_ERR);
           e.printStackTrace();
       }
   }
   
   
   private void generateProject() {
       AntRunner runner = new AntRunner();
       try {
           String base = m_textBaseDir.getText();
           if (!base.endsWith("/")) base = base + "/";
           
           runner.init(base + "build.xml", base );
           Map properties = new HashMap();
           properties.put("new.project.name", m_textProjectName.getText());
           runner.setProperties(properties, false); 
           runner.addBuildListener(this.buildListener);
           
           maskButtons();
           
           spinIt("new-project", runner);
           
           // a bug with the build script?
           File webToolsLibFolder = new File(projectFolder + "/lib/web");
           if (!webToolsLibFolder.exists()) webToolsLibFolder.mkdirs();
           
           
           info.setState(DeploymentInformation.STATE_PROJECT);
           stateSetup();
           
       } catch (Exception e) {
           e.printStackTrace();
           log(e.getMessage(), Project.MSG_ERR);
       }
   }
   
   
   private void maskButtons() {
           this.m_btnBaseDirChooser.setEnabled(false);
           this.m_btnGenerateCode.setEnabled(false);
           this.m_btnGenerateProject.setEnabled(false);
           this.m_btnRunProject.setEnabled(false);
           this.m_btnSchemaExport.setEnabled(false);       
   }
   
   
   private void generateCode() {
       umlc.Compiler c = new umlc.Compiler();
       c.setOutputdir(new File(this.projectFolder + "/src"));
       c.setSourceFile(new File(info.getUmlFileLocation()));
       try {
            log("Code Generation started=========\n", Project.MSG_INFO);
            maskButtons();
           c.execute();
           info.setState(DeploymentInformation.STATE_PROJECT);
           stateSetup();
           log("Code Generation Complete=========\n", Project.MSG_INFO);
       } catch (Exception e) {
           log("There was an error generating the code: " + e.getMessage(), Project.MSG_ERR);
           e.printStackTrace();
       }
   }
   
   private void schemaExport() {
       AntRunner runner = new AntRunner();
       try {

           log("Starting Schema Export============\n", Project.MSG_INFO);
           runner.init(this.projectBuild.getAbsolutePath(), this.projectFolder.getAbsolutePath() );

           runner.addBuildListener(this.buildListener);
           maskButtons();
           spinIt("schema-export", runner);
           info.setState(DeploymentInformation.STATE_PROJECT);
           stateSetup();
           log("Finished Schema Export============\n", Project.MSG_INFO);
       } catch (Exception e) {
           e.printStackTrace();
           log(e.getMessage() + "\n", Project.MSG_ERR);
       }       
   }
   
   private void runApp() {
       AntRunner runner = new AntRunner();
       try {

           
           
           log("Starting app==========\n", Project.MSG_INFO);
           runner.init(this.projectBuild.getAbsolutePath(), this.projectFolder.getAbsolutePath() );

           runner.addBuildListener(this.buildListener);
           maskButtons();
           spinIt("run", runner);
           info.setState(DeploymentInformation.STATE_PROJECT);
           stateSetup();
           log("Finished app==========\n", Project.MSG_INFO);
       } catch (Exception e) {
           e.printStackTrace();
           log(e.getMessage() + "\n", Project.MSG_ERR);
       }       
   }   
   private void stateSetup() {
        if (info.getState() == DeploymentInformation.STATE_NO_JMATTER ) {
           this.m_btnBaseDirChooser.setEnabled(true);
           this.m_btnGenerateCode.setEnabled(false);
           this.m_btnGenerateProject.setEnabled(false);
           this.m_btnRunProject.setEnabled(false);
           this.m_btnSchemaExport.setEnabled(false);
       }
       if (info.getState() == DeploymentInformation.STATE_NEW ) {
           this.m_btnBaseDirChooser.setEnabled(true);
           this.m_btnGenerateCode.setEnabled(false);
           this.m_btnGenerateProject.setEnabled(true);
           this.m_btnRunProject.setEnabled(false);
           this.m_btnSchemaExport.setEnabled(false);
       }
       if (info.getState() == DeploymentInformation.STATE_PROJECT ) {
           this.m_btnBaseDirChooser.setEnabled(true);
           this.m_btnGenerateCode.setEnabled(true);
           this.m_btnGenerateProject.setEnabled(false);
           this.m_btnRunProject.setEnabled(true);
           this.m_btnSchemaExport.setEnabled(true);
       }
   }
   
   
   /**
    * Main method for panel
    */
   public static void main(String[] args)
   {
      JFrame frame = new JFrame();
      frame.setSize(600, 400);
      frame.setLocation(100, 100);
      DeploymentInformation info = new DeploymentInformation();
      info.setJmatterDirectory("/home/ryan/java/jmatter/");
      info.setProjectName("test_from_uml");
      info.setDeploymentDirectory("/home/ryan/java/");
      
      frame.getContentPane().add(new JMatterProjectControlForm(info));
      frame.setVisible(true);

      frame.addWindowListener( new WindowAdapter()
      {
         public void windowClosing( WindowEvent evt )
         {
            System.exit(0);
         }
      });
   }

   
   private void log(final String msg, final int level) {
       try {
            SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        if ( msg == null )
                            return ;
//                        if ( getFormatter() != null )
//                            msg = getFormatter().format( lr );
                        
                        if ( m_jtextarea1 == null ) {
                            return ;
                        }
                        try {
                            int index = m_jtextarea1.getDocument().getLength();
                            int caret_position = m_jtextarea1.getCaretPosition();
                            SimpleAttributeSet set = new SimpleAttributeSet();
                            if ( _font == null ) {
                                StyleConstants.setFontFamily( set, "Monospaced" );
                            }
                            else {
                                StyleConstants.setFontFamily( set, _font.getFamily() );
                                StyleConstants.setBold( set, _font.isBold() );
                                StyleConstants.setItalic( set, _font.isItalic() );
                                StyleConstants.setFontSize( set, _font.getSize() );
                            }
                            if ( level == Project.MSG_WARN  )
                                StyleConstants.setForeground( set, GREEN );
                            else if (level == Project.MSG_ERR )
                                StyleConstants.setForeground( set, Color.RED );
                            else if ( level == Project.MSG_INFO )
                                StyleConstants.setForeground( set, Color.BLUE );
                            m_jtextarea1.getDocument().insertString( index, msg + lSep, set );
                            
                            m_jtextarea1.setCaretPosition( index + msg.length() );
                        }
                    catch ( Exception e ) {
                        e.printStackTrace();
                    }
                    }
                }
            );
        }
        catch ( Exception ignored ) {
            // ignored
            ignored.printStackTrace();
        }
       
   }
   
   
   
   
   /**
    * Adds fill components to empty cells in the first row and first column of the grid.
    * This ensures that the grid spacing will be the same as shown in the designer.
    * @param cols an array of column indices in the first row where fill components should be added.
    * @param rows an array of row indices in the first column where fill components should be added.
    */
   void addFillComponents( Container panel, int[] cols, int[] rows )
   {
      Dimension filler = new Dimension(10,10);

      boolean filled_cell_11 = false;
      CellConstraints cc = new CellConstraints();
      if ( cols.length > 0 && rows.length > 0 )
      {
         if ( cols[0] == 1 && rows[0] == 1 )
         {
            /** add a rigid area  */
            panel.add( Box.createRigidArea( filler ), cc.xy(1,1) );
            filled_cell_11 = true;
         }
      }

      for( int index = 0; index < cols.length; index++ )
      {
         if ( cols[index] == 1 && filled_cell_11 )
         {
            continue;
         }
         panel.add( Box.createRigidArea( filler ), cc.xy(cols[index],1) );
      }

      for( int index = 0; index < rows.length; index++ )
      {
         if ( rows[index] == 1 && filled_cell_11 )
         {
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
   public ImageIcon loadImage( String imageName )
   {
      try
      {
         ClassLoader classloader = getClass().getClassLoader();
         java.net.URL url = classloader.getResource( imageName );
         if ( url != null )
         {
            ImageIcon icon = new ImageIcon( url );
            return icon;
         }
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }
      throw new IllegalArgumentException( "Unable to load image: " + imageName );
   }

   public JPanel createPanel()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:3DLU:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:3DLU:GROW(1.0),FILL:DEFAULT:NONE,FILL:3DLU:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:DEFAULT:GROW(1.0),CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      JLabel jlabel1 = new JLabel();
      jlabel1.setText("Project Name");
      jpanel1.add(jlabel1,cc.xy(2,2));

      m_textProjectName.setName("textProjectName");
      jpanel1.add(m_textProjectName,cc.xywh(4,2,4,1));

      m_textBaseDir.setName("textBaseDir");
      jpanel1.add(m_textBaseDir,cc.xywh(4,4,4,1));

      JLabel jlabel2 = new JLabel();
      jlabel2.setText("Project Directory");
      jpanel1.add(jlabel2,cc.xy(2,8));

      JLabel jlabel3 = new JLabel();
      jlabel3.setText("JMatter Directory");
      jlabel3.setEnabled(false);
      jpanel1.add(jlabel3,cc.xy(2,4));

      m_labelProjectDir.setName("labelProjectDir");
      m_labelProjectDir.setText("/Base Directory/Project Name");
      jpanel1.add(m_labelProjectDir,cc.xywh(4,8,4,1));

      JLabel jlabel4 = new JLabel();
      jlabel4.setText("Project Build File");
      jpanel1.add(jlabel4,cc.xy(2,10));

      m_labelProjectBuildFile.setName("labelProjectBuildFile");
      m_labelProjectBuildFile.setText("/Base Directory/Project Name/build.xml");
      jpanel1.add(m_labelProjectBuildFile,cc.xywh(4,10,3,1));

      jpanel1.add(createPanel1(),new CellConstraints(2,12,8,1,CellConstraints.FILL,CellConstraints.FILL));
      m_btnBaseDirChooser.setActionCommand("...");
      m_btnBaseDirChooser.setName("btnBaseDirChooser");
      m_btnBaseDirChooser.setText("...");
      BevelBorder bevelborder1 = new BevelBorder(BevelBorder.RAISED,null,null,null,null);
      m_btnBaseDirChooser.setBorder(bevelborder1);
      //jpanel1.add(m_btnBaseDirChooser,cc.xy(9,4));

      m_btnGenerateProject.setActionCommand("Generate Project");
      m_btnGenerateProject.setName("btnGenerateProject");
      m_btnGenerateProject.setText("Generate Project");
      jpanel1.add(m_btnGenerateProject,cc.xy(7,10));

      JLabel jlabel5 = new JLabel();
      jlabel5.setText("Hibernate Properties");
      jlabel5.setEnabled(false);
      jpanel1.add(jlabel5,cc.xy(2,6));

      m_comboHibernateProps.setName("comboHibernateProps");
      m_comboHibernateProps.setEnabled(false);
      jpanel1.add(m_comboHibernateProps,cc.xy(4,6));
      
      
      m_btnNewProperties.setActionCommand("New Properties...");
      m_btnNewProperties.setName("btnNewProperties");
      m_btnNewProperties.setText("New Properties...");
      //jpanel1.add(m_btnNewProperties,cc.xy(7,6));

      addFillComponents(jpanel1,new int[]{ 1,2,3,4,5,6,7,8,9,10 },new int[]{ 1,2,3,4,5,6,7,8,9,10,11,12,13 });
      return jpanel1;
   }

   public JPanel createPanel1()
   {
      JPanel jpanel1 = new JPanel();
      TitledBorder titledborder1 = new TitledBorder(null,"Build Process",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,null,new Color(49,106,196));
      jpanel1.setBorder(titledborder1);
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE","CENTER:3DLU:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:DEFAULT:NONE,CENTER:3DLU:NONE,CENTER:DEFAULT:GROW(1.0),CENTER:3DLU:NONE,CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      JLabel titledseparator1 = new JLabel("Output");
      jpanel1.add(titledseparator1,cc.xywh(2,4,9,1));

      JScrollPane jscrollpane1 = new JScrollPane();
      jscrollpane1.setViewportView(m_jtextarea1);
      jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      jpanel1.add(jscrollpane1,new CellConstraints(2,6,9,1,CellConstraints.DEFAULT,CellConstraints.FILL));

      jpanel1.add(progress, cc.xywh(2,8,9,1));
      
      
      
      m_btnGenerateCode.setActionCommand("Generate Code From UML");
      m_btnGenerateCode.setName("btnGenerateCode");
      m_btnGenerateCode.setText("Generate Code From UML");
      jpanel1.add(m_btnGenerateCode,cc.xy(4,2));

      m_btnSchemaExport.setActionCommand("Schema Export");
      m_btnSchemaExport.setName("btnSchemaExport");
      m_btnSchemaExport.setText("Schema Export");
      jpanel1.add(m_btnSchemaExport,cc.xy(6,2));

      m_btnRunProject.setActionCommand("Run Project");
      m_btnRunProject.setName("btnRunProject");
      m_btnRunProject.setText("Run Project");
      jpanel1.add(m_btnRunProject,cc.xy(8,2));

      addFillComponents(jpanel1,new int[]{ 1,2,3,4,5,6,7,8,9,10,11 },new int[]{ 1,2,3,4,5,6 });
      return jpanel1;
   }

   /**
    * Initializer
    */
   protected void initializePanel()
   {
      setLayout(new BorderLayout());
      add(createPanel(), BorderLayout.CENTER);
   }

    /**
     * Holds value of property jmatterDir.
     */
    private DeploymentInformation info;

    /**
     * Getter for property jmatterDir.
     * @return Value of property jmatterDir.
     */
    public DeploymentInformation getDeploymentInfo() {
        return this.info;
    }




}

package tools;

import org.apache.tools.ant.*;

import java.io.*;
import java.util.*;

/**
  * <PRE>
  *   This class is designed to call Ant targets from any Java application.
  *   1. Initialize a new Project by calling "init"
  *   2. Feed Ant with some properties by calling "setProperties" (optional)
  *   3. Run an Ant target by calling "runTarget"
  *
  *
  *   Example :
  *
  *   try
  *   {
  *       //init
  *       init("/home/me/build.xml","/home/me/");
  *       //properties
  *       HashMap m = new HashMap();
  *       m.put("event", "test");
  *       m.put("subject", "sujet java 3");
  *       m.put("message", "message java 3");
  *       setProperties(m, false);
  *       //run
  *       runTarget("test");
  *   } catch (Exception e) { e.printStackTrace(); }
  * </PRE>
  *
  * @author croisier
  */


public class AntRunner implements AntRunnerInterface
{
     private Project project;


     /**
      * Initializes a new Ant Project.
      * @param _buildFile The build File to use. If none is provided, it will be \
                defaulted to "build.xml".
      * @param _baseDir The project's base directory. If none is provided, will be \
                defaulted to "." (the current directory).
      * @throws Exception Exceptions are self-explanatory (read their Message)
      */
     public void init(String _buildFile, String _baseDir) throws Exception
     {
         // Create a new project, and perform some default initialization
         project = new Project();
         try { project.init(); }
         catch (BuildException e)
             { throw new Exception("The default task list could not be loaded."); }

         // Set the base directory. If none is given, "." is used.
         if (_baseDir == null) _baseDir=new String(".");
         try { project.setBasedir(_baseDir); }
         catch (BuildException e)
             { 
             throw new Exception("The given basedir doesn't exist, or isn't a directory."); 
         }
            

         // Parse the given buildfile. If none is given, "build.xml" is used.
         if (_buildFile == null) _buildFile=new String("build.xml");
         try { 
             ProjectHelper.getProjectHelper().parse(project, new File(_buildFile)); 
         }  
         catch (BuildException e){ 
             throw new Exception("Configuration file "+_buildFile+" is invalid, or cannot be read.");
         }
    }  



     /**
      * Sets the project's properties.
      * May be called to set project-wide properties, or just before a target call to \
                set target-related properties only.
      * @param _properties A map containing the properties' name/value couples
      * @param _overridable If set, the provided properties values may be overriden \
                by the config file's values
      * @throws Exception Exceptions are self-explanatory (read their Message)
      */
     public void setProperties(Map _properties, boolean _overridable) throws Exception  {
         // Test if the project exists
         if (project == null) throw new Exception("Properties cannot be set because the project has not been initialized. Please call the 'init' method first !");

         // Property hashmap is null
         if (_properties == null) throw new Exception("The provided property map is null.");

         // Loop through the property map
         Set propertyNames = _properties.keySet();
         Iterator iter = propertyNames.iterator();
         while (iter.hasNext())
         {
             // Get the property's name and value
             String propertyName =  (String) iter.next();
             String propertyValue = (String) _properties.get(propertyName);
             if (propertyValue == null) continue;

             // Set the properties
             if (_overridable) project.setProperty(propertyName, propertyValue);
             else project.setUserProperty(propertyName, propertyValue);
         }
     }

     public void addBuildListener(BuildListener l) throws Exception {
          // Test if the project exists
         if (project == null) throw new Exception("No target can be launched because the project has not been initialized. Please call the 'init' method first !");
         project.addBuildListener(l);
     }
     

      /**
      * Runs the given Target.
      * @param _target The name of the target to run. If null, the project's default \
                target will be used.
      * @throws Exception Exceptions are self-explanatory (read their Message)
      */
     public void runTarget(String _target) throws Exception
     {
         // Test if the project exists
         if (project == null) throw new Exception("No target can be launched because the project has not been initialized. Please call the 'init' method first !");

         // If no target is specified, run the default one.
         if (_target == null) _target = project.getDefaultTarget();

         // Run the target
         try { 
             project.executeTarget(_target);  
             
         }
         catch (BuildException e)
         { throw new Exception(e.getMessage()); }
     }

}


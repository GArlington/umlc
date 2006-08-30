========================
UltraViolet
========================

Requirements:

-Ant Installed and set up           (ant.apache.org)
-JDK 1.5 or greater installed       (java.sun.com)
-JMatter dated 2006.08.25 or later. (jmatter.org)



To Use UltraViolet:

1. Unzip it
2. run 'ant -f run.xml'


And for a sample application to deploy
3. file->open    open the supplied pet.uml

4. JMatter->Set JMatter Directory       
    In the file dialog, select the root of a distribution copy of jmatter. . 

5. JMatter->JMatter Project Control
    A dialog will come up. The project name should be pet, and the JMatter directory should be where you selected in step 4. The 'Generate Project' button should 
    be enabled. (if its not, it cant find the build.xml file in the jmatter dir).

6. Click 'Generate Project'. You should see the output from the build in the output text. Wait.

7. Click 'Generate Code From UML'. Again wait. This step compiles the .uml using the umlc compiler into the java source
8. Click 'Schema Export' This runs the schema-export ant target in the project. Again wait.
9. Click 'Run Project' . The JMatter app should run.

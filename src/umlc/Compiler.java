package umlc;

import java.io.*;
import umlc.symbolTable.*;
import umlc.parseTree.*;
import umlc.codeGeneration.*;
import tools.Debug;
import java.util.Vector;

public class Compiler {

    public static Symbol_table class_names;


    
    private Vector codeGenerators = new Vector();
    private Vector fileNames      = new Vector();
    
    private Symbol_table st;
    
    private File sourceFile;
    public void setSourceFile(File source) {
        this.sourceFile = source;
    }
    
    private File outputdir;
    public void setOutputdir(File output){
        this.outputdir = output;
    }
    
    private int debugLevel;
    public void setDebugLevel(int level) {
        this.debugLevel = level;
    }
    
    private String generatorClass;
    public void setGeneratorClass(String className) {
        this.generatorClass = className;
    }
    
    public Compiler() {
        //defaults
        debugLevel = 0;
        generatorClass = "umlc.codeGeneration.jmatter.JmatterGeneratorImpl";
        st = new Symbol_table();
    }
    
    
    public Compiler(String args[]) {
        debugLevel = 0;
        fileNames = processArgs(args);
        st = new Symbol_table();
    }
    
    
    /*
     * Our Ant interface
     */
    public void execute() {
        System.out.println("Starting umlc task");
        System.out.println("Output Directory: " + this.outputdir.getAbsolutePath());
        System.out.println("Code generator: " + this.generatorClass);
        System.out.println("Debug Level: " + this.debugLevel);
        if (generatorClass != null) {
            GeneratorInfo info = new GeneratorInfo(generatorClass, outputdir.getAbsolutePath());
            codeGenerators.add(info);
        }
         Debug.setDebug_level(debugLevel);
        if (sourceFile == null ) {
            if (!java.awt.GraphicsEnvironment.isHeadless()) {
                    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
                    javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter() {
                        public boolean accept(File file) {
                            if (file.isDirectory()) return true;
                            return file.getName().endsWith(".uml");
                        }
                        
                        public String getDescription() {
                                return "UML files (.uml)";
                        }    
                    };  
                    
                    chooser.setFileFilter(filter);
                    chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
                    int returnVal = chooser.showDialog(null, "Generate From UML file");
                    if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {                        
                        sourceFile = chooser.getSelectedFile();
                    }  else 
                        System.err.println("A source .uml file needs to be specified. ");
            } else {
                System.err.println("A source .uml file needs to be specified. ");
            }
            
        }
        if (sourceFile != null) fileNames.add(sourceFile.getAbsolutePath());
        java.util.Hashtable packages = new java.util.Hashtable();
        // parse each of the files given as arguments
        // and put the package class in the vector
        for (int i=0; i < fileNames.size(); i ++) {
            try {
		Debug.println(3, "Opening file: " + (String)fileNames.elementAt(i));
                DataInputStream input = new DataInputStream(new FileInputStream((String)fileNames.elementAt(i)));
                // attach lexer to the input stream
                UmlLexer lexer = new UmlLexer(input);

                // Create parser attached to lexer
                UmlParser parser = new UmlParser(lexer);    
                parser.setSymbolTable(this.st);
                // start up the parser by calling the rule
                // at which you want to begin parsing.
                UmlPackage p = parser.uml(); 
                

		
                // Here we put the package in the hashtable if it has
                // not been defined yet, otherwise we add the package
                // contents to the previously defined package
                if (packages.containsKey(p.name)) {
                    UmlPackage other_package = (UmlPackage) packages.get(p.name);
                    //other_package.UmlClasses.
                    other_package.UmlClasses.addAll(p.UmlClasses);
                    other_package.UmlAssociations.addAll(p.UmlAssociations);
                
                }
                else {
                    packages.put(p.name, p);
                }       
             }
             catch (Exception e) {
                    e.printStackTrace();
                    
             }

        }
        // For each package, we should now go through and do semantic checking
	// this also should sort out relationships.
        for (java.util.Enumeration e = packages.elements(); e.hasMoreElements();) {

            UmlPackage p = (UmlPackage)e.nextElement();
            if (Debug.getDebug_level() > 3) p.pretty_print();
            // semantic check package
	    st.setPackage(p.getName());
	    p.semantic_check(st);
        }
        
                
	// we must dish off the code generation to one of the implementations
	// of the CodeGeneratorIF.
	// go through the vector of codeGenerators and createte the class
	// representing it. and then call the GenerateCode method
	for (int i=0; i < codeGenerators.size(); i++) {
	    GeneratorInfo g_info = ((GeneratorInfo)codeGenerators.elementAt(i));
	    String class_name = g_info.class_name;
	    Debug.println(3, "Generating code with class: " + class_name);

	    GeneratorIF g;
	    try { 
		g = (GeneratorIF)(Class.forName(class_name)).newInstance();
                if (outputdir != null ) {
                    g.generateCode(packages, outputdir.getAbsolutePath());
                } else {
                    g.generateCode(packages, g_info.output_dir);
                }
	    }
	    catch (Exception e) {
		Debug.println(0, "We could not instanciate the class: " + class_name);
		e.printStackTrace();
	    }
	}        
    }
    
    
    
    
    
    
    public static void main (String args []) {

        Compiler c = new Compiler(args);
        c.execute();
    }



    // this method process the flags from the command line
    // and then returns a vector of strings with the 
    // files to parse
    private Vector processArgs(String args[] ) {

	Vector fileNames = new Vector();
	for (int i=0; i < args.length; i ++) {


	    /********* Debug argument     *******************/
	    if (args[i].equals("-debug")) {
		if (i + 1 >= args.length) {
		    Debug.println(0, "missing debug value with -debug option; ignoring");
		} 
		else {
		    i++;
		    Debug.setDebug_level(Integer.parseInt(args[i]));
		}
	    }
	    /********* CodeGenerator Object *****************/
	    else if (args[i].equals("-generator")) {
		// this argument requires two values, the first
		// is the class for generating the code, second
		// the output direcory
		if (i + 2 >= args.length) {
                    Debug.println(0, "missing class AND outputDIR values with -generator option.");
		    System.exit(0);
                }
		else {
		    GeneratorInfo g = new GeneratorInfo(args[i+1], args[i+2]);
		    i += 2;
		    codeGenerators.addElement(g);
		}
	    }

	    /****** Just a File Name ******************/
	    else {
		fileNames.addElement(args[i]);
	    }
	}
	return fileNames;
    }

}





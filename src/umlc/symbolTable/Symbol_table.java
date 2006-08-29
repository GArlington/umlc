package umlc.symbolTable;

import java.util.Hashtable;
import tools.Debug;

public class Symbol_table {

    Hashtable symbols;
    Hashtable packages;
    public String current_package;

    // each package has its own ST
    // that way there is no mangling the namespace
    // this requires that at the start of the file
    // the static method, setPackage(String package) be called
    
    /** Creates new Symbol_table */
    public Symbol_table() {	
        packages = new Hashtable();
    }

    public void setPackage(String name) {
	// check to see if it exists
	// if not, create it
	
	Debug.println(5, "ST setPackage() called");
	current_package = name;

	if (packages.containsKey(name)) {
	    Debug.println(5, "We have a ST for this package");
	    symbols = (Hashtable) packages.get(name);
	}
	else {
	    Debug.println(5, "We DO NOT have a ST for this package");
	    Hashtable newST = new Hashtable();
	    packages.put (name, newST);
	    symbols = newST;
	}
    }
    
    public void insert(String name, Object value) throws EntryExistsException {
        Debug.println(5, "Start of insert call to symbol table");
        if (symbols.containsKey(name)) {
            throw new EntryExistsException("Entry Exists in Symbol table");
        }
        else {
            Debug.println(5, "Putting in name: " + name + "into symbol table");
            symbols.put(name, value);
        }
    }
    
    
    public  Object lookup (String name) throws EntryNotFoundException {
        if (symbols.containsKey(name)) {
	    Debug.println(5, "Found '" + name + "' in ST");
            return symbols.get(name);
        }
        else throw new EntryNotFoundException("Symbol not found in symbol table");
    }

}

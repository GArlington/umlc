package umlc.parseTree;

import java.util.*;
import umlc.symbolTable.Symbol_table;

public class UmlAssociation implements TreeNode {

    public String name;    
    public AssociationEnd end1;
    public AssociationEnd end2;
    public Vector declarations;
    
    
    UmlAssociation(String _name, AssociationEnd _end1, Multiplicity _multiplicity_1, HashMap _ann1, AssociationEnd _end2, Multiplicity _multiplicity_2, HashMap _ann2, Vector _declarations) {
                     name = _name;
                     end1 = _end1;
                     end1.setMultiplicity(_multiplicity_1);
                     end1.annotations = _ann1;
                     end2 = _end2;
		     end2.setMultiplicity(_multiplicity_2);
                     end2.annotations = _ann2;
                     
		     end1.setAssociation(this);
		     end2.setAssociation(this);

                     declarations=_declarations;
    }


    public void semantic_check(Symbol_table st) {
	// check both ends of the association
	end1.semantic_check(st);
	end2.semantic_check(st);

	// now that we have verified both ends, cross 
	// reference both ends
	end1.setOtherEnd(end2);
	end2.setOtherEnd(end1);

    }

    
    public void pretty_print() {
        System.out.println("*****************************");
	System.out.println("RELATIONSHIP: " + name);

        end1.pretty_print();
        System.out.print(" - ");
        end2.pretty_print();
    }
}

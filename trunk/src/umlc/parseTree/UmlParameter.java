package umlc.parseTree;

public class UmlParameter {
    
    public String name;
    public UmlType type;
    
    
    UmlParameter() {}
    
    UmlParameter(String _name, UmlType _type) {
        name = _name;
        type = _type;
    }

    void pretty_print() {
	System.out.print(name + ":");
	type.pretty_print();

    }

}

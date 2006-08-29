
header {
package umlc.parseTree;

import umlc.parseTree.*;
import umlc.symbolTable.*;
import tools.Debug;
import java.util.HashMap;
}

class UmlParser extends Parser;
options {
  k = 5;
}
{
  Symbol_table st;

  public Symbol_table getSymbolTable() {
     return st;
  }
  public void setSymbolTable(Symbol_table st) {
     this.st = st;
  }

}


uml 
returns [UmlPackage package_1 = new UmlPackage();]

{
  java.util.Vector definitions;
}
:  package_1=u_package definitions=definition_list
{
	/* We need to sort out the class and relationship definitions */
	for (int i=0; i<definitions.size(); i++) {
		if (definitions.elementAt(i) instanceof UmlClass) {
			package_1.UmlClasses.addElement(definitions.elementAt(i));
		}
		else if (definitions.elementAt(i) instanceof UmlAssociation) {
                        package_1.UmlAssociations.addElement(definitions.elementAt(i));
                }

	}
}
          ;

u_package 
returns [UmlPackage uml_package = new UmlPackage()]
: "package" id:IDENT SEMI
{
    uml_package.setName(id.getText());
    Debug.println(5, "We are in package " + id.getText());
    st.setPackage(id.getText());

}
	 ;


annotations 
returns [HashMap annotations = new HashMap();] 
: (LSQR annotations=annotation_list RSQR 
  | /*  EMPTY */
  )
  ;

annotation_list 
returns [HashMap annotations = new HashMap(); ]
:
{ Annotation annotation;}
(  annotation=annotation annotations=annotation_list 
{  annotations.put(annotation.getName(), annotation.getValue()); }
   | /*EMPTY*/
);

annotation 
returns [Annotation annotation = new Annotation(); ]
:
(id:IDENT 
{  annotation.setName(id.getText());}
| id2:IDENT EQUALS lit:StringLiteral 
 { 
  String literal = lit.getText();
  literal = literal.substring(1, literal.length()-1); 
  literal = literal.replaceAll("\\\\\"", "\"");
  annotation.setName(id2.getText()); annotation.setValue(literal); 

 }
)
;

definition_list 
returns [java.util.Vector definitions = new java.util.Vector(); ] 
                   : 
{ TreeNode definition; }
                    ( definition=definition definitions=definition_list
{    definitions.insertElementAt(definition, 0);}
                   |  /* EMPTY */
		  ) 
		;

definition 
returns [ TreeNode definition=new UmlClass();]
           : definition=uml_class
	   | definition=uml_association
	   ;


uml_class 
returns [TreeNode uml_class = new UmlClass();]
{ String extends_name;
  java.util.Vector declarations;
  HashMap annotations;
}
          : "class" id:IDENT extends_name=uml_extends annotations=annotations LBRACE declarations=declaration_list RBRACE
{
    String class_name = id.getText();
    Debug.println(5, "We extend " + extends_name);
    UmlClass uml_class_2 = new UmlClass (class_name, extends_name);
    try {
                st.insert(class_name, uml_class_2);
               
    }
    catch (EntryExistsException e) {
        System.out.println("Error: The class name '" + class_name + "' already exists in the package '" + st.current_package + "'.");
       
	
    }

    uml_class_2.annotations = annotations;

    /* Now we have to sort out the declarations */
    for (int i=0; i < declarations.size() ; i++) {
	if (declarations.elementAt(i) instanceof UmlAttribute) {
		Debug.println(5, "We have attribute " + ((UmlAttribute)declarations.elementAt(i)).name);
		uml_class_2.attributes.addElement(declarations.elementAt(i));

	}
	else if (declarations.elementAt(i) instanceof UmlOperation) {
		Debug.println(5, "We have operation " + ((UmlOperation)declarations.elementAt(i)).name);
		uml_class_2.operations.addElement(declarations.elementAt(i));
	}	

    }
    uml_class = uml_class_2;
	
}
	  ;


uml_extends 
returns [String ident = new String("");]
: ("extends" id:IDENT 
    { ident = id.getText(); }
            |   /* EMPTY */
              )
	    ;

declaration_list
returns [java.util.Vector declarations = new  java.util.Vector(); ]
: 
{ TreeNode declaration; }
(  declaration=declaration declarations=declaration_list
  { declarations.insertElementAt(declaration, 0); }
                    
|     /* EMPTY */
		   )
		 ;

declaration
returns [TreeNode declaration=new UmlClass();] 
            : declaration=attribute
	    | declaration=operation
	    ;


attribute
returns [TreeNode attribute= new UmlClass();] 
{
    UmlModifier modifier;
    UmlType type;
    HashMap annotations;
}
: modifier=visibility id:IDENT type=type annotations=annotations SEMI
{
    attribute = new UmlAttribute(modifier, id.getText(), type, annotations);

}
 | id2:IDENT type=type annotations=annotations SEMI
{
   attribute = new UmlAttribute(null, id2.getText(), type, annotations);
}
	  ;



operation
returns [TreeNode operation=new UmlClass();]
{
    UmlModifier modifier;
    java.util.Vector parameters;
    UmlType return_type;
}
: modifier=visibility id:IDENT LPAR parameters=paramlist RPAR COLON return_type=type SEMI
{
    operation = (TreeNode) new UmlOperation(modifier, id.getText(), parameters, return_type);
}
| modifier=visibility id2:IDENT LPAR  RPAR COLON return_type=type SEMI
{
    operation = (TreeNode) new UmlOperation(modifier, id2.getText(), new java.util.Vector(), return_type);
}


;


paramlist
returns [java.util.Vector parameters = new  java.util.Vector();] 
{ 
UmlParameter param;     
}
    : param=param
{
    parameters.addElement(param);
}
    | param=param COMMA parameters=paramlist
{
    parameters.insertElementAt(param, 0);


}
	  


;


param
returns [UmlParameter parameter=new UmlParameter();]
{
UmlType type;
}
: id:IDENT COLON type=type 
{
    parameter = new UmlParameter(id.getText(), type);

}

;

uml_association
returns [TreeNode uml_association= new UmlClass();]
{
    Multiplicity mult_1;
    AssociationEnd  ass_1;
    HashMap ann_1;
    Multiplicity mult_2;
    AssociationEnd  ass_2;
    HashMap ann_2;
    java.util.Vector declarations;
}
: "association" id:IDENT LBRACE  
		   LPAR ass_1=associationEnd ann_1=annotations RPAR mult_1=multiplicity DASH mult_2=multiplicity LPAR ass_2=associationEnd ann_2=annotations RPAR 
		   declarations=declaration_list RBRACE
{
  uml_association = new UmlAssociation(id.getText(), ass_1, mult_1, ann_1, ass_2, mult_2, ann_2, declarations);  
}	     
;

associationEnd
returns [AssociationEnd associationEnd= new AssociationEnd();] 
    : id:IDENT aggregation
{ associationEnd = new AssociationEnd(id.getText()); }
    | id_1:IDENT "as" id_2:IDENT aggregation
{ associationEnd = new AssociationEnd(id_1.getText(), id_2.getText()); }
	    ;

multiplicity
returns [Multiplicity mult = new Multiplicity();] 
: num:NUMBER
{ mult = new Multiplicity(Integer.parseInt(num.getText())); }
             | INFINITY
{ mult = new Multiplicity(true); }
             | num_1:NUMBER RANGE num_2:NUMBER
{ mult = new Multiplicity(Integer.parseInt(num_1.getText()), Integer.parseInt(num_2.getText())); }
             | NUMBER RANGE INFINITY
{ mult = new Multiplicity(Integer.parseInt(num_1.getText()), true); }
	     ;


aggregation : ( "whole"
	      | "part"
	      |
	      )
		;


visibility
returns [UmlModifier modifier = new UmlModifier();] 
: PUBLIC
{ modifier = new UmlModifier(UmlModifier.PUBLIC); }
           | PROTECTED
{ modifier = new UmlModifier(UmlModifier.PROTECTED); }
           | DASH    /* PRIVATE */
{ modifier = new UmlModifier(UmlModifier.PRIVATE); }

	   ;


type 
returns [UmlType type= new UmlType();]
:      "int" 
{ type = new UmlType(UmlType.INTEGER); }
     | "void"
{ type = new UmlType(UmlType.VOID); }
     | "boolean"
{ type = new UmlType(UmlType.BOOLEAN); }
     | "byte"
{ type = new UmlType(UmlType.BYTE); }
     | "char"
{ type = new UmlType(UmlType.CHAR); }
     | "short"
{ type = new UmlType(UmlType.SHORT); }
     | "float"
{ type = new UmlType(UmlType.FLOAT); }
     | "long" 
{ type = new UmlType(UmlType.LONG); }
     | "double"
{ type = new UmlType(UmlType.DOUBLE); }
     | "string"
{ type = new UmlType(UmlType.STRING); }
     | "decimal"
{ type = new UmlType(UmlType.DECIMAL); }
     | id:IDENT
{ type = new UmlType(UmlType.CLASS); 
  type.class_name = id.getText();
}
     ;


class UmlLexer extends Lexer;


WS      :       (' '
        |       '\t'
        |       '\n'
        |       '\r')
                { _ttype = Token.SKIP; }
	;



PUBLIC : '+'
       ;

PROTECTED : '#'
	  ;

RANGE : ".."
      ;

INFINITY : '*'
         ;

SEMI : ';'
     ;

COLON : ':'
      ;

COMMA : ','
      ;


LPAR : '('
     ;

RPAR : ')'
     ;


LSQR : '['
     ;

RSQR : ']'
     ;
LBRACE : '{'
       ;

RBRACE : '}'
	;

LANGBRACE : '<' ;
RANGBRACE : '>' ;

EQUALS   : '=';



DASH : '-'
     ;


NUMBER : ('0'..'9')+
       ;


// an identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
IDENT           options {testLiterals=true;}
        :       ('a'..'z'|'A'..'Z'|'_'|'$') ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'$'|'.')*
        ;


EscapeSequence
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    ;

StringLiteral
    :  '"' ( EscapeSequence | ~('\\'|'"') )* '"'
    ;



       
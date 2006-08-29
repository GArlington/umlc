
header {
package umlc.parseTree;

import umlc.parseTree.*;
import umlc.symbolTable.*;
import tools.Debug;
import java.util.HashMap;
}

class AnnotationParser extends Parser;
options {
  k = 5;
}

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


class AnnotationsLexer extends Lexer;

EQUALS   : '=';



WS      :       (' '
        |       '\t'
        |       '\n'
        |       '\r')
                { _ttype = Token.SKIP; }
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

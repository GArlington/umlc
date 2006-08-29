// $ANTLR 2.7.5 (20050128): "uml.g" -> "UmlLexer.java"$

package umlc.parseTree;

import umlc.parseTree.*;
import umlc.symbolTable.*;
import tools.Debug;
import java.util.HashMap;

public interface UmlParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int LITERAL_package = 4;
	int IDENT = 5;
	int SEMI = 6;
	int LSQR = 7;
	int RSQR = 8;
	int EQUALS = 9;
	int StringLiteral = 10;
	int LITERAL_class = 11;
	int LBRACE = 12;
	int RBRACE = 13;
	int LITERAL_extends = 14;
	int LPAR = 15;
	int RPAR = 16;
	int COLON = 17;
	int COMMA = 18;
	int LITERAL_association = 19;
	int DASH = 20;
	int LITERAL_as = 21;
	int NUMBER = 22;
	int INFINITY = 23;
	int RANGE = 24;
	int LITERAL_whole = 25;
	int LITERAL_part = 26;
	int PUBLIC = 27;
	int PROTECTED = 28;
	int LITERAL_int = 29;
	int LITERAL_void = 30;
	int LITERAL_boolean = 31;
	int LITERAL_byte = 32;
	int LITERAL_char = 33;
	int LITERAL_short = 34;
	int LITERAL_float = 35;
	int LITERAL_long = 36;
	int LITERAL_double = 37;
	int LITERAL_string = 38;
	int LITERAL_decimal = 39;
	int WS = 40;
	int LANGBRACE = 41;
	int RANGBRACE = 42;
	int EscapeSequence = 43;
}

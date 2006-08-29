// $ANTLR 2.7.5 (20050128): "uml.g" -> "UmlParser.java"$

package umlc.parseTree;

import umlc.parseTree.*;
import umlc.symbolTable.*;
import tools.Debug;
import java.util.HashMap;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

public class UmlParser extends antlr.LLkParser       implements UmlParserTokenTypes
 {

  Symbol_table st;

  public Symbol_table getSymbolTable() {
     return st;
  }
  public void setSymbolTable(Symbol_table st) {
     this.st = st;
  }


protected UmlParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public UmlParser(TokenBuffer tokenBuf) {
  this(tokenBuf,5);
}

protected UmlParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public UmlParser(TokenStream lexer) {
  this(lexer,5);
}

public UmlParser(ParserSharedInputState state) {
  super(state,5);
  tokenNames = _tokenNames;
}

	public final UmlPackage  uml() throws RecognitionException, TokenStreamException {
		UmlPackage package_1 = new UmlPackage();;
		
		
		java.util.Vector definitions;
		
		
		try {      // for error handling
			package_1=u_package();
			definitions=definition_list();
			
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
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return package_1;
	}
	
	public final UmlPackage  u_package() throws RecognitionException, TokenStreamException {
		UmlPackage uml_package = new UmlPackage();
		
		Token  id = null;
		
		try {      // for error handling
			match(LITERAL_package);
			id = LT(1);
			match(IDENT);
			match(SEMI);
			
			uml_package.setName(id.getText());
			Debug.println(5, "We are in package " + id.getText());
			st.setPackage(id.getText());
			
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return uml_package;
	}
	
	public final java.util.Vector  definition_list() throws RecognitionException, TokenStreamException {
		java.util.Vector definitions = new java.util.Vector(); ;
		
		
		try {      // for error handling
			TreeNode definition;
			{
			switch ( LA(1)) {
			case LITERAL_class:
			case LITERAL_association:
			{
				definition=definition();
				definitions=definition_list();
				definitions.insertElementAt(definition, 0);
				break;
			}
			case EOF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return definitions;
	}
	
	public final HashMap  annotations() throws RecognitionException, TokenStreamException {
		HashMap annotations = new HashMap();;
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LSQR:
			{
				match(LSQR);
				annotations=annotation_list();
				match(RSQR);
				break;
			}
			case SEMI:
			case LBRACE:
			case RPAR:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return annotations;
	}
	
	public final HashMap  annotation_list() throws RecognitionException, TokenStreamException {
		HashMap annotations = new HashMap(); ;
		
		
		try {      // for error handling
			Annotation annotation;
			{
			switch ( LA(1)) {
			case IDENT:
			{
				annotation=annotation();
				annotations=annotation_list();
				annotations.put(annotation.getName(), annotation.getValue());
				break;
			}
			case RSQR:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return annotations;
	}
	
	public final Annotation  annotation() throws RecognitionException, TokenStreamException {
		Annotation annotation = new Annotation(); ;
		
		Token  id = null;
		Token  id2 = null;
		Token  lit = null;
		
		try {      // for error handling
			{
			if ((LA(1)==IDENT) && (LA(2)==IDENT||LA(2)==RSQR)) {
				id = LT(1);
				match(IDENT);
				annotation.setName(id.getText());
			}
			else if ((LA(1)==IDENT) && (LA(2)==EQUALS)) {
				id2 = LT(1);
				match(IDENT);
				match(EQUALS);
				lit = LT(1);
				match(StringLiteral);
				
				String literal = lit.getText();
				literal = literal.substring(1, literal.length()-1); 
				literal = literal.replaceAll("\\\\\"", "\"");
				annotation.setName(id2.getText()); annotation.setValue(literal); 
				
				
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		return annotation;
	}
	
	public final TreeNode  definition() throws RecognitionException, TokenStreamException {
		 TreeNode definition=new UmlClass();;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_class:
			{
				definition=uml_class();
				break;
			}
			case LITERAL_association:
			{
				definition=uml_association();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return definition;
	}
	
	public final TreeNode  uml_class() throws RecognitionException, TokenStreamException {
		TreeNode uml_class = new UmlClass();;
		
		Token  id = null;
		String extends_name;
		java.util.Vector declarations;
		HashMap annotations;
		
		
		try {      // for error handling
			match(LITERAL_class);
			id = LT(1);
			match(IDENT);
			extends_name=uml_extends();
			annotations=annotations();
			match(LBRACE);
			declarations=declaration_list();
			match(RBRACE);
			
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
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return uml_class;
	}
	
	public final TreeNode  uml_association() throws RecognitionException, TokenStreamException {
		TreeNode uml_association= new UmlClass();;
		
		Token  id = null;
		
		Multiplicity mult_1;
		AssociationEnd  ass_1;
		HashMap ann_1;
		Multiplicity mult_2;
		AssociationEnd  ass_2;
		HashMap ann_2;
		java.util.Vector declarations;
		
		
		try {      // for error handling
			match(LITERAL_association);
			id = LT(1);
			match(IDENT);
			match(LBRACE);
			match(LPAR);
			ass_1=associationEnd();
			ann_1=annotations();
			match(RPAR);
			mult_1=multiplicity();
			match(DASH);
			mult_2=multiplicity();
			match(LPAR);
			ass_2=associationEnd();
			ann_2=annotations();
			match(RPAR);
			declarations=declaration_list();
			match(RBRACE);
			
			uml_association = new UmlAssociation(id.getText(), ass_1, mult_1, ann_1, ass_2, mult_2, ann_2, declarations);  
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return uml_association;
	}
	
	public final String  uml_extends() throws RecognitionException, TokenStreamException {
		String ident = new String("");;
		
		Token  id = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_extends:
			{
				match(LITERAL_extends);
				id = LT(1);
				match(IDENT);
				ident = id.getText();
				break;
			}
			case LSQR:
			case LBRACE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		return ident;
	}
	
	public final java.util.Vector  declaration_list() throws RecognitionException, TokenStreamException {
		java.util.Vector declarations = new  java.util.Vector(); ;
		
		
		try {      // for error handling
			TreeNode declaration;
			{
			switch ( LA(1)) {
			case IDENT:
			case DASH:
			case PUBLIC:
			case PROTECTED:
			{
				declaration=declaration();
				declarations=declaration_list();
				declarations.insertElementAt(declaration, 0);
				break;
			}
			case RBRACE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		return declarations;
	}
	
	public final TreeNode  declaration() throws RecognitionException, TokenStreamException {
		TreeNode declaration=new UmlClass();;
		
		
		try {      // for error handling
			if ((_tokenSet_7.member(LA(1))) && (_tokenSet_8.member(LA(2))) && (_tokenSet_9.member(LA(3)))) {
				declaration=attribute();
			}
			else if ((LA(1)==DASH||LA(1)==PUBLIC||LA(1)==PROTECTED) && (LA(2)==IDENT) && (LA(3)==LPAR)) {
				declaration=operation();
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_10);
		}
		return declaration;
	}
	
	public final TreeNode  attribute() throws RecognitionException, TokenStreamException {
		TreeNode attribute= new UmlClass();;
		
		Token  id = null;
		Token  id2 = null;
		
		UmlModifier modifier;
		UmlType type;
		HashMap annotations;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case DASH:
			case PUBLIC:
			case PROTECTED:
			{
				modifier=visibility();
				id = LT(1);
				match(IDENT);
				type=type();
				annotations=annotations();
				match(SEMI);
				
				attribute = new UmlAttribute(modifier, id.getText(), type, annotations);
				
				
				break;
			}
			case IDENT:
			{
				id2 = LT(1);
				match(IDENT);
				type=type();
				annotations=annotations();
				match(SEMI);
				
				attribute = new UmlAttribute(null, id2.getText(), type, annotations);
				
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_10);
		}
		return attribute;
	}
	
	public final TreeNode  operation() throws RecognitionException, TokenStreamException {
		TreeNode operation=new UmlClass();;
		
		Token  id = null;
		Token  id2 = null;
		
		UmlModifier modifier;
		java.util.Vector parameters;
		UmlType return_type;
		
		
		try {      // for error handling
			if ((LA(1)==DASH||LA(1)==PUBLIC||LA(1)==PROTECTED) && (LA(2)==IDENT) && (LA(3)==LPAR) && (LA(4)==IDENT)) {
				modifier=visibility();
				id = LT(1);
				match(IDENT);
				match(LPAR);
				parameters=paramlist();
				match(RPAR);
				match(COLON);
				return_type=type();
				match(SEMI);
				
				operation = (TreeNode) new UmlOperation(modifier, id.getText(), parameters, return_type);
				
			}
			else if ((LA(1)==DASH||LA(1)==PUBLIC||LA(1)==PROTECTED) && (LA(2)==IDENT) && (LA(3)==LPAR) && (LA(4)==RPAR)) {
				modifier=visibility();
				id2 = LT(1);
				match(IDENT);
				match(LPAR);
				match(RPAR);
				match(COLON);
				return_type=type();
				match(SEMI);
				
				operation = (TreeNode) new UmlOperation(modifier, id2.getText(), new java.util.Vector(), return_type);
				
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_10);
		}
		return operation;
	}
	
	public final UmlModifier  visibility() throws RecognitionException, TokenStreamException {
		UmlModifier modifier = new UmlModifier();;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case PUBLIC:
			{
				match(PUBLIC);
				modifier = new UmlModifier(UmlModifier.PUBLIC);
				break;
			}
			case PROTECTED:
			{
				match(PROTECTED);
				modifier = new UmlModifier(UmlModifier.PROTECTED);
				break;
			}
			case DASH:
			{
				match(DASH);
				modifier = new UmlModifier(UmlModifier.PRIVATE);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_11);
		}
		return modifier;
	}
	
	public final UmlType  type() throws RecognitionException, TokenStreamException {
		UmlType type= new UmlType();;
		
		Token  id = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_int:
			{
				match(LITERAL_int);
				type = new UmlType(UmlType.INTEGER);
				break;
			}
			case LITERAL_void:
			{
				match(LITERAL_void);
				type = new UmlType(UmlType.VOID);
				break;
			}
			case LITERAL_boolean:
			{
				match(LITERAL_boolean);
				type = new UmlType(UmlType.BOOLEAN);
				break;
			}
			case LITERAL_byte:
			{
				match(LITERAL_byte);
				type = new UmlType(UmlType.BYTE);
				break;
			}
			case LITERAL_char:
			{
				match(LITERAL_char);
				type = new UmlType(UmlType.CHAR);
				break;
			}
			case LITERAL_short:
			{
				match(LITERAL_short);
				type = new UmlType(UmlType.SHORT);
				break;
			}
			case LITERAL_float:
			{
				match(LITERAL_float);
				type = new UmlType(UmlType.FLOAT);
				break;
			}
			case LITERAL_long:
			{
				match(LITERAL_long);
				type = new UmlType(UmlType.LONG);
				break;
			}
			case LITERAL_double:
			{
				match(LITERAL_double);
				type = new UmlType(UmlType.DOUBLE);
				break;
			}
			case LITERAL_string:
			{
				match(LITERAL_string);
				type = new UmlType(UmlType.STRING);
				break;
			}
			case LITERAL_decimal:
			{
				match(LITERAL_decimal);
				type = new UmlType(UmlType.DECIMAL);
				break;
			}
			case IDENT:
			{
				id = LT(1);
				match(IDENT);
				type = new UmlType(UmlType.CLASS); 
				type.class_name = id.getText();
				
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_12);
		}
		return type;
	}
	
	public final java.util.Vector  paramlist() throws RecognitionException, TokenStreamException {
		java.util.Vector parameters = new  java.util.Vector();;
		
		
		UmlParameter param;     
		
		
		try {      // for error handling
			if ((LA(1)==IDENT) && (LA(2)==COLON) && (_tokenSet_8.member(LA(3))) && (LA(4)==RPAR)) {
				param=param();
				
				parameters.addElement(param);
				
			}
			else if ((LA(1)==IDENT) && (LA(2)==COLON) && (_tokenSet_8.member(LA(3))) && (LA(4)==COMMA)) {
				param=param();
				match(COMMA);
				parameters=paramlist();
				
				parameters.insertElementAt(param, 0);
				
				
				
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_13);
		}
		return parameters;
	}
	
	public final UmlParameter  param() throws RecognitionException, TokenStreamException {
		UmlParameter parameter=new UmlParameter();;
		
		Token  id = null;
		
		UmlType type;
		
		
		try {      // for error handling
			id = LT(1);
			match(IDENT);
			match(COLON);
			type=type();
			
			parameter = new UmlParameter(id.getText(), type);
			
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_14);
		}
		return parameter;
	}
	
	public final AssociationEnd  associationEnd() throws RecognitionException, TokenStreamException {
		AssociationEnd associationEnd= new AssociationEnd();;
		
		Token  id = null;
		Token  id_1 = null;
		Token  id_2 = null;
		
		try {      // for error handling
			if ((LA(1)==IDENT) && (_tokenSet_15.member(LA(2)))) {
				id = LT(1);
				match(IDENT);
				aggregation();
				associationEnd = new AssociationEnd(id.getText());
			}
			else if ((LA(1)==IDENT) && (LA(2)==LITERAL_as)) {
				id_1 = LT(1);
				match(IDENT);
				match(LITERAL_as);
				id_2 = LT(1);
				match(IDENT);
				aggregation();
				associationEnd = new AssociationEnd(id_1.getText(), id_2.getText());
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_16);
		}
		return associationEnd;
	}
	
	public final Multiplicity  multiplicity() throws RecognitionException, TokenStreamException {
		Multiplicity mult = new Multiplicity();;
		
		Token  num = null;
		Token  num_1 = null;
		Token  num_2 = null;
		
		try {      // for error handling
			if ((LA(1)==NUMBER) && (LA(2)==LPAR||LA(2)==DASH)) {
				num = LT(1);
				match(NUMBER);
				mult = new Multiplicity(Integer.parseInt(num.getText()));
			}
			else if ((LA(1)==INFINITY)) {
				match(INFINITY);
				mult = new Multiplicity(true);
			}
			else if ((LA(1)==NUMBER) && (LA(2)==RANGE) && (LA(3)==NUMBER)) {
				num_1 = LT(1);
				match(NUMBER);
				match(RANGE);
				num_2 = LT(1);
				match(NUMBER);
				mult = new Multiplicity(Integer.parseInt(num_1.getText()), Integer.parseInt(num_2.getText()));
			}
			else if ((LA(1)==NUMBER) && (LA(2)==RANGE) && (LA(3)==INFINITY)) {
				match(NUMBER);
				match(RANGE);
				match(INFINITY);
				mult = new Multiplicity(Integer.parseInt(num_1.getText()), true);
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_17);
		}
		return mult;
	}
	
	public final void aggregation() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_whole:
			{
				match(LITERAL_whole);
				break;
			}
			case LITERAL_part:
			{
				match(LITERAL_part);
				break;
			}
			case LSQR:
			case RPAR:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_16);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"package\"",
		"IDENT",
		"SEMI",
		"LSQR",
		"RSQR",
		"EQUALS",
		"StringLiteral",
		"\"class\"",
		"LBRACE",
		"RBRACE",
		"\"extends\"",
		"LPAR",
		"RPAR",
		"COLON",
		"COMMA",
		"\"association\"",
		"DASH",
		"\"as\"",
		"NUMBER",
		"INFINITY",
		"RANGE",
		"\"whole\"",
		"\"part\"",
		"PUBLIC",
		"PROTECTED",
		"\"int\"",
		"\"void\"",
		"\"boolean\"",
		"\"byte\"",
		"\"char\"",
		"\"short\"",
		"\"float\"",
		"\"long\"",
		"\"double\"",
		"\"string\"",
		"\"decimal\"",
		"WS",
		"LANGBRACE",
		"RANGBRACE",
		"EscapeSequence"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 526338L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 69696L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 256L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 288L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 4224L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 8192L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 403701792L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 1098974756896L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 1098974757088L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 403709984L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 32L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 327872L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 65536L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 327680L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 100728960L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 65664L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 1081344L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	
	}

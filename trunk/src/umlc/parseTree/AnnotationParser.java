// $ANTLR 2.7.5 (20050128): "annotations.g" -> "AnnotationParser.java"$

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

public class AnnotationParser extends antlr.LLkParser       implements AnnotationParserTokenTypes
 {

protected AnnotationParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public AnnotationParser(TokenBuffer tokenBuf) {
  this(tokenBuf,5);
}

protected AnnotationParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public AnnotationParser(TokenStream lexer) {
  this(lexer,5);
}

public AnnotationParser(ParserSharedInputState state) {
  super(state,5);
  tokenNames = _tokenNames;
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
		return annotations;
	}
	
	public final Annotation  annotation() throws RecognitionException, TokenStreamException {
		Annotation annotation = new Annotation(); ;
		
		Token  id = null;
		Token  id2 = null;
		Token  lit = null;
		
		try {      // for error handling
			{
			if ((LA(1)==IDENT) && (LA(2)==EOF||LA(2)==IDENT)) {
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
			recover(ex,_tokenSet_1);
		}
		return annotation;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"IDENT",
		"EQUALS",
		"StringLiteral",
		"WS",
		"EscapeSequence"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 18L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	
	}

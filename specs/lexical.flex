package compiler.generated;

import java_cup.runtime.*;

%%

%class Lexer
%public
%unicode
%line
%column
%cup


%{

	/**
	 * Factory method for creating Symbols for a given type.
	 * @param type The type of this symbol
	 * @return A symbol of a specific type
	 */
	public Symbol symbol(int type) {
		return new Symbol(type, yyline, yycolumn);
	}

%}

%%

<YYINITIAL> {
	"true"		{ return symbol(sym.TRUE); }
	"false"		{ return symbol(sym.FALSE); }
}

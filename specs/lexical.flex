/**
 * Lexical Specification
 *
 * Contributors:
 *      Jeanderson Barros Canido - http://jeandersonbc.github.io
 *      Thiago Ferreira
 */
package compiler.generated;

import java_cup.runtime.*;

%%

%class Lexer
%public
%unicode
%line
%column
%cup
%implements sym


%{

  /**
   * Factory method for creating Symbols for a given type.
   * @param type The type of this symbol
   * @return A symbol of a specific type
   */
  public Symbol symbol(int type) {
      return new Symbol(type, yyline+1, yycolumn+1);
  }
  
  /**
   * Factory method for creating Symbols for a given type and its value.
   * @param type The type of this symbol
   * @param value The value of this symbol
   * @return A symbol of a specific type
   */
  public Symbol symbol(int type, Object value) {
      return new Symbol(type, yyline+1, yycolumn+1, value);
  }
  
  /**
   * Reports an error occured in a given line.
   * @param line The bad line
   * @param msg Additional information about the error
   */
  private void reportError(int line, String msg) {
      throw new RuntimeException("Lexical error at line #" + line + ": " + msg);
  }

%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

BlankSpace = {LineTerminator} | [ \t\f]

/* Comments */

Comments = {LineComment} | {BlockComment}
LineComment = "//" {InputCharacter}* {LineTerminator}?
BlockComment = "/*" [^*] ~"*/" | "/*" "*"+ "/" 

/* Identifier */
Identifier = [:jletter:][:jletterdigit:]*

%%

<YYINITIAL> {

    /* keywords */
    "namespace"             { return symbol(NAMESPACE); }
    "default"               { return symbol(DEFAULT); }
    "inline"                { return symbol(INLINE); }
    "delete"                { return symbol(DELETE); }

    /* assignment */
    "="                     { return symbol(ASSIGNMENT); }

    /* separators */
    "->"                    { return symbol(ARROW); }
    ";"                     { return symbol(SEMICOLON); }
    ":"                     { return symbol(COLON); }
    "("                     { return symbol(LPAR); }
    ")"                     { return symbol(RPAR); }
    "{"                     { return symbol(LBRK); }
    "}"                     { return symbol(RBRK); }

    {Identifier}            { return symbol(IDENTIFIER); }

    {BlankSpace}            {/* skip it */ }
    {Comments}              {/* skip it */ }

}

/* Input not matched */
[^] { reportError(yyline+1, "Illegal character \"" + yytext() + "\""); }


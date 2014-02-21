/**
 * Lexical Specification
 *
 * Contributors:
 *      Jeanderson Barros Canido - http://jeandersonbc.github.io
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

    /* Keywords */

    "namespace"             { return symbol(NAMESPACE); }
    "noexcept"              { return symbol(NOEXCEPT); }
    "alignas"               { return symbol(ALIGNAS); }
    "inline"                { return symbol(INLINE); }
    "using"                 { return symbol(USING); }
    "throw"                 { return symbol(THROW); }

    /* Class Definition */

    "struct"                { return symbol(STRUCT); }
    "class"                 { return symbol(CLASS); }
    "union"                 { return symbol(UNION); }

    /* Virt Specifiers */

    "explicit"              { return symbol(EXPLICIT); }
    "final"                 { return symbol(FINAL); }

    /* Qualifiers */

    "volatile"              { return symbol(VOLATILE); }
    "const"                 { return symbol(CONST); }

    /* Assignment */

    ">>="                   { return symbol(RSHIFTASSIGN); }
    "<<="                   { return symbol(LSHIFTASSIGN); }
    "-="                    { return symbol(MINUSASSIGN); }
    "="                     { return symbol(ASSIGNMENT); }
    "+="                    { return symbol(PLUSASSIGN); }
    "*="                    { return symbol(MULTASSIGN); }
    "/="                    { return symbol(DIVASSIGN); }
    "%="                    { return symbol(MODASSIGN); }
    "&="                    { return symbol(ANDASSIGN); }
    "^="                    { return symbol(XORASSIGN); }
    "|="                    { return symbol(ORASSIGN); }

    /* PM Operators */

    "->*"                   { return symbol(ARROWSTAR); }
    ".*"                    { return symbol(DOTSTAR); }

    /* Shift Operators */

    "<<"                    { return symbol(LSHIFT); }
    ">>"                    { return symbol(RSHIFT); }

    /* Relational and Logical Operators */

    "^"                     { return symbol(XOROP); }
    "||"                    { return symbol(OROP); }
    "|"                     { return symbol(SOROP); }
    "!="                    { return symbol(NEQOP); }
    "=="                    { return symbol(EQOP); }
    "<="                    { return symbol(LTE); }
    ">="                    { return symbol(GTE); }
    "<"                     { return symbol(LT); }
    ">"                     { return symbol(GT); }

    /* Arithmetic Operators */

    "-"                     { return symbol(MINUSOP); }
    "+"                     { return symbol(PLUSOP); }
    "/"                     { return symbol(DIVOP); }
    "%"                     { return symbol(MODOP); }

    /* Overloaded Lexemes */

    "&&"                    { return symbol(DOUBLEAND); }
    "&"                     { return symbol(SINGLEAND); }
    "*"                     { return symbol(STAR); }

    /* Separators */

    ";"                     { return symbol(SEMICOLON); }
    "?"                     { return symbol(QUESTION); }
    "["                     { return symbol(LSQRBRK); }
    "]"                     { return symbol(RSQRBRK); }
    "::"                    { return symbol(SEPPTR); }
    ","                     { return symbol(COMMA); }
    "->"                    { return symbol(ARROW); }
    ":"                     { return symbol(COLON); }
    "}"                     { return symbol(RBRK); }
    "{"                     { return symbol(LBRK); }
    "("                     { return symbol(LPAR); }
    ")"                     { return symbol(RPAR); }

    /* Others */

    "..."                   { return symbol(DOTS); }
    {Identifier}            { return symbol(IDENTIFIER); }
    {BlankSpace}            { /* skip it */ }
    {Comments}              { /* skip it */ }

}

/* Input not matched */
[^] { reportError(yyline+1, "Illegal character \"" + yytext() + "\""); }


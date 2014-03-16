/**
 * Lexical Specification
 *
 * Contributors:
 *      Jeanderson Barros Candido - http://jeandersonbc.github.io
 *      Thiago Ferreira Patricio - http://github.com/tferreirap
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
      return new Symbol(type, yyline, yycolumn);
  }
  
  /**
   * Factory method for creating Symbols for a given type and its value.
   * @param type The type of this symbol
   * @param value The value of this symbol
   * @return A symbol of a specific type
   */
  public Symbol symbol(int type, Object value) {
      return new Symbol(type, yyline, yycolumn, value);
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
/* Identifier = [:jletter:][:jletterdigit:]* */

/*-*
 * Aqui definiremos os padrões de definição:
 */
letter          = [A-Za-z]
L               = [a-zA-Z_]
digit           = [0-9]
alphanumeric    = {letter}|{digit}
other_id_char   = [_]
identifier      = {letter}({alphanumeric}|{other_id_char})*
/* integer         = {digit}* */




%%

<YYINITIAL> {

    /* Keywords */

    "static_assert"         { return symbol(STATIC_ASSERT); }
    "typename"              { return symbol(TYPENAME); }
    "decltype"              { return symbol(DECLTYPE); }
    "noexcept"              { return symbol(NOEXCEPT); }
    "default"               { return symbol(DEFAULT); }
    "alignas"               { return symbol(ALIGNAS); }
    "extern"                { return symbol(EXTERN); }
    "typeid"                { return symbol(TYPEID); }
    "sizeof"                { return symbol(SIZEOF); }
    "using"                 { return symbol(USING); }
    "this"                  { return symbol(THIS); }
    
    "bool"                  { return symbol(sym.BOOL, new String(yytext())); }
    "break"                 { return symbol(sym.BREAK, new String(yytext())); }
    "auto"                  { return symbol(sym.AUTO, new String(yytext())); }
    "break"                 { return symbol(sym.BREAK, new String(yytext())); }
    "case"                  { return symbol(sym.CASE, new String(yytext())); }
    "char"                  { return symbol(sym.CHAR, new String(yytext())); }
    "continue"              { return symbol(sym.CONTINUE, new String(yytext())); }
    "do"                    { return symbol(sym.DO, new String(yytext())); }
    "double"                { return symbol(sym.DOUBLE, new String(yytext())); }
    "else"                  { return symbol(sym.ELSE, new String(yytext())); }
    "float"                 { return symbol(sym.FLOAT, new String(yytext())); }
    "for"                   { return symbol(sym.FOR, new String(yytext())); }
    "goto"                  { return symbol(sym.GOTO, new String(yytext())); }
    "if"                    { return symbol(sym.IF, new String(yytext())); }
    "int"                   { return symbol(sym.INT, new String(yytext())); }
    "long"                  { return symbol(sym.LONG, new String(yytext())); }
    "register"              { return symbol(sym.REGISTER, new String(yytext())); }
    "return"                { return symbol(sym.RETURN, new String(yytext())); }
    "short"                 { return symbol(sym.SHORT, new String(yytext())); }
    "signed"                { return symbol(sym.SIGNED, new String(yytext())); }
    "static"                { return symbol(sym.STATIC, new String(yytext())); }
    "switch"                { return symbol(sym.SWITCH, new String(yytext())); }
    "typedef"               { return symbol(sym.TYPEDEF, new String(yytext())); }
    "unsigned"              { return symbol(sym.UNSIGNED, new String(yytext())); }
    "void"                  { return symbol(sym.VOID, new String(yytext())); }
    "while"                 { return symbol(sym.WHILE, new String(yytext())); }
    "operator"              { return symbol(sym.OPERATOR, new String(yytext())); }
    "new"                   { return symbol(sym.NEW, new String(yytext())); }

    /* Access modifiers */
    
    "protected"             { return symbol(PROTECTED); }
    "public"                { return symbol(PUBLIC); }

    /* Literals */

    "false"                 { return symbol(FALSE); }
    "true"                  { return symbol(TRUE); }
    "null"                  { return symbol(NULLPTR); }

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

    /* Unary Operators */

    "!"                     { return symbol(LOGNEGATION); }
    "++"                    { return symbol(AUTOINCRM); }
    "--"                    { return symbol(AUTODECRM); }
    "~"                     { return symbol(BINNEG); }

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
    "."                     { return symbol(DOT); }
-
    /* Others */

    "..."                   { return symbol(DOTS); }

     \"([^\\\"]|\\.)*\"     { return symbol(sym.STRING_LITERAL); }

    {identifier}            { return symbol(sym.IDENTIFIER, new String(yytext())); }

    /* {integer}            { return symbol(sym.INTEGER, new Integer(yytext())); }  */

    [1-9][0-9]*[L]?         { return symbol(sym.INTEGER); }
    0x[0-9a-f]+             { return symbol(sym.INTEGER); }
    0                       { return symbol(sym.INTEGER); }
    
    {BlankSpace}            { /* skip it */ }
    {Comments}              { /* skip it */ }



}

/* Input not matched */
[^] { reportError(yyline+1, "Illegal character \"" + yytext() + "\""); }


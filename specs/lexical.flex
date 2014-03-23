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


%{

  public static String curLine;

  /**
   * Factory method for creating Symbols for a given type.
   * @param type The type of this symbol
   * @return A symbol of a specific type
   */
  public Symbol symbol(int type) {
      curLine = "line :" + yyline;
      return new Symbol(type, yyline, yycolumn);
  }
  
  /**
   * Factory method for creating Symbols for a given type and its value.
   * @param type The type of this symbol
   * @param value The value of this symbol
   * @return A symbol of a specific type
   */
  public Symbol symbol(int type, Object value) {
      curLine = "line :" + yyline;
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

  public String current_lexeme(){
      int l = yyline+1;
      int c = yycolumn+1;
      return "line: " + l + ", column: " + c + ", with : '"+yytext()+"')";
  }

%}


/* Macros */

D = [0-9]
L = [a-zA-Z_]
H = [a-fA-F0-9]
E = [Ee][+-]?{D}+
FS = (f|F|l|L)
IS = (u|U|l|L)*
Comment = "/*" [^*] ~"*/" | "/*" "*"+ "/"

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

    "static_assert"         { return symbol(sym.STATIC_ASSERT); }
    "typename"              { return symbol(sym.TYPENAME); }
    "decltype"              { return symbol(sym.DECLTYPE); }
    "noexcept"              { return symbol(sym.NOEXCEPT); }
    "default"               { return symbol(sym.DEFAULT); }
    "alignas"               { return symbol(sym.ALIGNAS); }
    "extern"                { return symbol(sym.EXTERN); }
    "typeid"                { return symbol(sym.TYPEID); }
    "sizeof"                { return symbol(sym.SIZEOF); }
    "using"                 { return symbol(sym.USING); }
    "this"                  { return symbol(sym.THIS); }
    
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
    "new"                   { return symbol(sym.NEW, new String(yytext()) ); }
    "string"                { return symbol(sym.STRING, new String(yytext()) ); }

    /* Access modifiers */
    
    "protected"             { return symbol(sym.PROTECTED); }
    "public"                { return symbol(sym.PUBLIC); }

    /* Literals */

    "false"                 { return symbol(sym.FALSE, new String(yytext())); }
    "true"                  { return symbol(sym.TRUE, new String(yytext())); }
    "null"                  { return symbol(sym.NULLPTR); }

    /* Class Definition */

    "struct"                { return symbol(sym.STRUCT); }
    "class"                 { return symbol(sym.CLASS); }
    "union"                 { return symbol(sym.UNION); }

    /* Virt Specifiers */

    "explicit"              { return symbol(sym.EXPLICIT); }
    "final"                 { return symbol(sym.FINAL); }

    /* Qualifiers */

    "volatile"              { return symbol(sym.VOLATILE); }
    "const"                 { return symbol(sym.CONST); }

    /* Unary Operators */

    "!"                     { return symbol(sym.LOGNEGATION); }
    "++"                    { return symbol(sym.AUTOINCRM); }
    "--"                    { return symbol(sym.AUTODECRM); }
    "~"                     { return symbol(sym.BINNEG); }

    /* Assignment */

    ">>="                   { return symbol(sym.RSHIFTASSIGN, new String(yytext())); }
    "<<="                   { return symbol(sym.LSHIFTASSIGN, new String(yytext())); }
    "-="                    { return symbol(sym.MINUSASSIGN, new String(yytext())); }
    "="                     { return symbol(sym.ASSIGNMENT, new String(yytext())); }
    "+="                    { return symbol(sym.PLUSASSIGN, new String(yytext())); }
    "*="                    { return symbol(sym.MULTASSIGN); }
    "/="                    { return symbol(sym.DIVASSIGN); }
    "%="                    { return symbol(sym.MODASSIGN); }
    "&="                    { return symbol(sym.ANDASSIGN); }
    "^="                    { return symbol(sym.XORASSIGN); }
    "|="                    { return symbol(sym.ORASSIGN); }

    /* PM Operators */

    "->*"                   { return symbol(sym.ARROWSTAR); }
    ".*"                    { return symbol(sym.DOTSTAR); }

    /* Shift Operators */

    "<<"                    { return symbol(sym.LSHIFT); }
    ">>"                    { return symbol(sym.RSHIFT); }

    /* Relational and Logical Operators */

    "^"                     { return symbol(sym.XOROP, new String(yytext())); }
    "||"                    { return symbol(sym.OROP, new String(yytext())); }
    "|"                     { return symbol(sym.SOROP, new String(yytext())); }
    "!="                    { return symbol(sym.NEQOP, new String(yytext())); }
    "=="                    { return symbol(sym.EQOP, new String(yytext())); }
    "<="                    { return symbol(sym.LTE, new String(yytext())); }
    ">="                    { return symbol(sym.GTE, new String(yytext())); }
    "<"                     { return symbol(sym.LT, new String(yytext())); }
    ">"                     { return symbol(sym.GT, new String(yytext())); }

    /* Arithmetic Operators */

    "-"                     { return symbol(sym.MINUSOP); }
    "+"                     { return symbol(sym.PLUSOP); }
    "/"                     { return symbol(sym.DIVOP); }
    "%"                     { return symbol(sym.MODOP); }

    /* Overloaded Lexemes */

    "&&"                    { return symbol(sym.DOUBLEAND); }
    "&"                     { return symbol(sym.SINGLEAND); }
    "*"                     { return symbol(sym.STAR); }

    /* Separators */

    ";"                     { return symbol(sym.SEMICOLON, new String(yytext())); }
    "?"                     { return symbol(sym.QUESTION); }
    "["                     { return symbol(sym.LSQRBRK); }
    "]"                     { return symbol(sym.RSQRBRK); }
    "::"                    { return symbol(sym.SEPPTR); }
    ","                     { return symbol(sym.COMMA); }
    "->"                    { return symbol(sym.ARROW); }
    ":"                     { return symbol(sym.COLON); }
    "}"                     { return symbol(sym.RBRK, new String(yytext())); }
    "{"                     { return symbol(sym.LBRK, new String(yytext())); }
    "("                     { return symbol(sym.LPAR, new String(yytext())); }
    ")"                     { return symbol(sym.RPAR, new String(yytext())); }
    "."                     { return symbol(sym.DOT, new String(yytext())); }
-
    /* Others */

    "..."                   { return symbol(sym.DOTS); }

     \"([^\\\"]|\\.)*\"     { return symbol(sym.STRING_LITERAL, new String(yytext())); }

    {identifier}            { return symbol(sym.IDENTIFIER, new String(yytext())); }

    {D}+{IS}?       { return symbol(sym.INTEGER, new String(yytext())); }

    {BlankSpace}            { /* skip it */ }
    {Comments}              { /* skip it */ }



}

/* Input not matched */
[^] { reportError(yyline+1, "Illegal character \"" + yytext() + "\""); }


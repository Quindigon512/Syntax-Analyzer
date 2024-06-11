/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2000 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Author:     Quinn Trate                                                 *
 * Date:       March 27, 2024                                              *
 * Class:      CMPSC 470 Section 1: Compilers                              *
 * Instructor: Dr. Hyuntae Na                                              *
 * Purpose:    Generates the Lexer.java File to be the DFA for the         *
 *             Lexical Analyzer                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

%%

%class Lexer
%byaccj

%{

  public Parser   parser;
  public int      lineno;
  public int      column;
  public int	  prevCol;

  public Lexer(java.io.Reader r, Parser parser) {
    this(r);
    this.parser = parser;
    this.lineno = 1;
    this.column = 1;
    this.prevCol = 1;
  }
%}

num          = [0-9]+("."[0-9]+)?
bool         = "true"|"false"
identifier   = [a-zA-Z][a-zA-Z0-9_]*
newline      = \n
whitespace   = [ \t\r]+
linecomment  = "//".*
blockcomment = "/*"[^]*"*/"

%%

"return"                            { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.RETURN   ; }
"begin"                             { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.BEGIN    ; }
"print"                             { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.PRINT    ; }
"while"                             { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.WHILE    ; }
"bool"                              { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.BOOL     ; }
"else"                              { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.ELSE     ; }
"func"                              { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.FUNC     ; }
"size"                              { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.SIZE     ; }
"then"                              { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.THEN     ; }
"void"                              { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.VOID     ; }
"and"                               { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.TERMOP   ; }
"end"                               { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.END      ; }
"new"                               { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.NEW      ; }
"num"                               { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.NUM      ; }
"var"                               { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.VAR      ; }
"if"                                { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.IF       ; }
"or"                                { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.EXPROP   ; }
"<>"                                { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.RELOP    ; }
"<="                                { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.RELOP    ; }
"=>"                                { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.RELOP    ; }
":="                                { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.ASSIGN   ; }
"::"                                { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.TYPEOF   ; }
"("                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.LPAREN   ; }
")"                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.RPAREN   ; }
"["                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.LBRACKET ; }
"]"                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.RBRACKET ; }
"<"                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.RELOP    ; }
">"                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.RELOP    ; }
"="                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.RELOP    ; }
"+"                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.EXPROP   ; }
"-"                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.EXPROP   ; }
"*"                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.TERMOP   ; }
","                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.COMMA    ; }
"."                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.DOT      ; }
";"                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.SEMI     ; }
{num}                               { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.NUM_LIT  ; }
{bool}                              { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.BOOL_LIT ; }
{identifier}                        { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.IDENT    ; }
{newline}                           { lineno++; column = 1; prevCol = 1; /* skip */ }
{whitespace}                        { column = prevCol; prevCol += yytext().length(); /* skip */ }
{linecomment}                       { column = prevCol; prevCol += yytext().length(); /* skip */ }
{blockcomment}                      { for (int i = 0; i < yytext().length(); i++)
                                      {
                                          if (yytext().charAt(i) == '\n')
                                          {
                                              lineno++;
                                              column = 1;
                                              prevCol = 1;
                                          }
                                          else
                                          {
                                              column = prevCol;
                                              prevCol++;
                                          }
                                      }
                                      /* skip */
                                    }
"/"                                 { parser.yylval = new ParserVal((Object)yytext()); column = prevCol; prevCol += yytext().length(); return Parser.TERMOP   ; }


\b     { column = prevCol; System.out.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { column = prevCol; System.out.println("\nLexical error: unexpected character '" + yytext() + "' at " + lineno + ":" + column + "."); return -1; }


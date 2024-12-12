package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

// Space
" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

// Comment
"//" {yybegin(COMMENT);}
<COMMENT> . {yybegin(COMMENT);}
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

// Keywords
"program"               { return new Symbol(sym.PROG, yytext()); }
"read"                  { return new Symbol(sym.READ, yytext()); }
"print"                 { return new Symbol(sym.PRINT, yytext()); }
"new"                   { return new Symbol(sym.NEW, yytext()); }
"if"                    { return new Symbol(sym.IF, yytext()); }
"else"                  { return new Symbol(sym.ELSE, yytext()); }
"map"                   { return new Symbol(sym.MAP, yytext()); }
"break"                 { return new Symbol(sym.BREAK, yytext()); }
"continue"              { return new Symbol(sym.CONTINUE, yytext()); }
"return"                { return new Symbol(sym.RETURN, yytext()); }
"do"                    { return new Symbol(sym.DO, yytext()); }
"while"                 { return new Symbol(sym.WHILE, yytext()); }
"union"                 { return new Symbol(sym.UNION, yytext()); }
"void"                  { return new Symbol(sym.VOID, yytext()); }
"class"                 { return new Symbol(sym.CLASS, yytext()); }
"extends"               { return new Symbol(sym.EXTENDS, yytext()); }

// Operands
"++"                    { return new Symbol(sym.DOUBLEPLUS, yytext()); }
"+"                     { return new Symbol(sym.PLUS, yytext()); }
"--"                    { return new Symbol(sym.DOUBLEMINUS, yytext()); }
"-"                     { return new Symbol(sym.MINUS, yytext()); }
"*"                     { return new Symbol(sym.MUL, yytext()); }
"/"                     { return new Symbol(sym.DIV, yytext()); }
"%"                     { return new Symbol(sym.MOD, yytext()); }
";"                     { return new Symbol(sym.SEMI, yytext()); }
"("                     { return new Symbol(sym.LPAREN, yytext()); }
")"                     { return new Symbol(sym.RPAREN, yytext()); }
"["                     { return new Symbol(sym.LBRACKET, yytext()); }
"]"                     { return new Symbol(sym.RBRACKET, yytext()); }
"{"                     { return new Symbol(sym.LBRACE, yytext()); }
"}"                     { return new Symbol(sym.RBRACE, yytext()); }
"&&"                    { return new Symbol(sym.AND, yytext()); }
"||"                    { return new Symbol(sym.OR, yytext()); }
"="                     { return new Symbol(sym.EQUAL, yytext()); }
"!="                    { return new Symbol(sym.NOTEQUAL, yytext()); }
"<="                    { return new Symbol(sym.LESSEQUAL, yytext()); }
">="                    { return new Symbol(sym.GREATEQUAL, yytext()); }
">"                     { return new Symbol(sym.GREAT, yytext()); }
"<"                     { return new Symbol(sym.LESS, yytext()); }
","                     { return new Symbol(sym.COMMA, yytext()); }
"=="                    { return new Symbol(sym.DOUBLEEQUAL, yytext()); }
 
// Constants
[0-9]+  			{ return new_symbol(sym.CONSTNUMBER, Integer.valueOf(yytext())); }
"false" | "true"	{ return new_symbol(sym.CONSTBOOLEAN, Boolean.valueOf(yytext())); }
"'"[a-zA-Z0-9]"'" 	{ return new_symbol(sym.CONSTCHAR, yytext().charAt(1)); }

// Identifier
[a-zA-Z][a-zA-Z0-9_]* 	{return new_symbol (sym.IDENT, yytext()); }

// Other
. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }

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
"program"               { return new Symbol(sym.PROG); }
"read"                  { return new Symbol(sym.READ); }
"print"                 { return new Symbol(sym.PRINT); }
"new"                   { return new Symbol(sym.NEW); }
"if"                    { return new Symbol(sym.IF); }
"else"                  { return new Symbol(sym.ELSE); }
"map"                   { return new Symbol(sym.MAP); }
"break"                 { return new Symbol(sym.BREAK); }
"continue"              { return new Symbol(sym.CONTINUE); }
"return"                { return new Symbol(sym.RETURN); }
"do"                    { return new Symbol(sym.DO); }
"while"                 { return new Symbol(sym.WHILE); }
"union"                 { return new Symbol(sym.UNION); }
"void"                  { return new Symbol(sym.VOID); }
"class"                 { return new Symbol(sym.CLASS); }
"extends"               { return new Symbol(sym.EXTENDS); }

// Operands
"+"                     { return new Symbol(sym.PLUS); }
"++"                    { return new Symbol(sym.DOUBLEPLUS); }
"-"                     { return new Symbol(sym.MINUS); }
"--"                    { return new Symbol(sym.DOUBLEMINUS); }
"*"                     { return new Symbol(sym.MUL); }
"/"                     { return new Symbol(sym.DIV); }
"%"                     { return new Symbol(sym.MOD); }
";"                     { return new Symbol(sym.SEMI); }
"("                     { return new Symbol(sym.LPAREN); }
")"                     { return new Symbol(sym.RPAREN); }
"["                     { return new Symbol(sym.LBRACKET); }
"]"                     { return new Symbol(sym.RBRACKET); }
"{"                     { return new Symbol(sym.LBRACE); }
"}"                     { return new Symbol(sym.RBRACE); }
"&&"                    { return new Symbol(sym.AND); }
"||"                    { return new Symbol(sym.OR); }
"="                     { return new Symbol(sym.EQUAL); }
"!="                    { return new Symbol(sym.NOTEQUAL); }
"<="                    { return new Symbol(sym.LESSEQUAL); }
">="                    { return new Symbol(sym.GREATEQUAL); }
">"                     { return new Symbol(sym.GREAT); }
"<"                     { return new Symbol(sym.LESS); }
","                     { return new Symbol(sym.COMMA); }
"=="                    { return new Symbol(sym.DOUBLEEQUAL); }
 
// Identifier
[a-zA-Z][a-zA-Z0-9_]* 	{return new_symbol (sym.IDENT, yytext()); }
 
// Constants
[0-9]+  			{ return new_symbol(sym.CONSTNUMBER, Integer.valueOf(yytext())); }
"false" | "true"	{ return new_symbol(sym.CONSTBOOLEAN, Boolean.valueOf(yytext())); }
"'"[a-zA-Z0-9]"'" 	{ return new_symbol(sym.CONSTCHAR, yytext(1).charAt(1)); }

// Other
. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }

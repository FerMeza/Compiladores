import java.lang.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

%%

%line
%column
%unicode
%type Object

%{
public static void main(String args[]){
   Yylex lexer = new Yylex(new BufferedReader(new InputStreamReader(System.in)));
   try{
      lexer.yylex();
   } catch(IOException e){
      System.out.println("Ocurrio un error al intentar acceder a la consola");
   }
}
%}

id=[a-zA-Z]+
espacio=[ \t\n]+

%%

. {System.out.println("Ha ocurrido un error lexico: " + yytext());}
int {System.out.println("Encontre una palabra reservada " + yytext()); }
float {System.out.println("Encontre una palabra reservada " + yytext()); }
if {System.out.println("Encontre una palabra reservada " + yytext()); }
else {System.out.println("Encontre una palabra reservada " + yytext()); }
{espacio} {/*Ignorar los espacios en blanco*/}
{id}+ {System.out.println("Encontre un identificador " + yytext()); }

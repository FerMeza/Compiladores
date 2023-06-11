/* Meza Ortega Fernando */

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
      }catch(IOException e){
         System.out.println("Ocurrio un error al intentar acceder a la consola");
      }
   }
%}

digito=[0-9]
letra=[a-zA-Z]
espacio=[ \t\n]
esps={espacio}+
hexd=[a-fA-F]
hex=0[xX]({hexd}|{digito})+
exp=[Ee]([+-])?{digito}+
decimal={digito}*\.{digito}+|{digito}+\.{digito}*
exponencial=({decimal}{exp}|{digito}+{exp})([fFlL])?
identificadores=@({letra}|_|{digito}){5,32}
directivas=ifdef|define|undef|error|pragma


%%

{esps} {/*Ignora los espacios en blancos*/}
{directivas} {System.out.println("Directiva de preprocesamiento : " + yytext());}
{digito}+ {System.out.println("Encontre un numero " + yytext());}
{letra}+ {System.out.println("Encontre una palabra " + yytext());}
{hex} {System.out.println("Encontre un literal hexadecimal " + yytext());}
{exponencial} {System.out.println("Encontre un decimal en notacion exponencial " + yytext());}
{identificadores} {System.out.println("Encontre un identificador " + yytext());}

/* */

import java.lang.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

%%
%line 
%column
%unicode
%type int 

%xstate comentario

%{

	public static File token =null;
  public static BufferedWriter bw ;
	public static File archivo = null;
  public static int suma = 0;
  public static String cadena = "";
	public static void main(String[] args) {
        
        try {
        token= new File("resultado.tokens");
        bw = new BufferedWriter(new FileWriter(token));
        archivo = new File(args[0]);
	 			Yylex lexer = new Yylex (new BufferedReader(new FileReader(archivo+".art")));
	 			lexer.yylex();
	 			}catch (IOException e){
	 			System.out.println("Ocurrio un error al intentar acceder al archivo");
	 	}
	}

%}

%eofval{
		   bw.close(); 
            return 0;
%eofval}

digito = [0-9]
numeros = {digito}+(_{digito}+)*               	//sEgundo punro
letras = [a-zA-Z]
ex=[Ee]([+-])?{numeros}+
dec= {numeros}*"."{numeros}+|{numeros}+"."{numeros}*
decex=({dec}{ex}?|{numeros}{ex})								//tercer punto
id = ({letras}|_) (_{digito}| {letras}|_) *    // Primer punto

//cadenas = (\")[^(\")\n]*(\")
//reservadas = (if|then|else|while|do|case|is|void|true|false|begin|end|not)
espacios = [\n\t ]+
//comentario = --[^\n]*\n
//comentmulti = ("<*")([^"*"]|"*"[^>])*("*>") 
//operadores = ("+"| -| "*"| "/"| % | >| <| <>| = | :=)
//especiales = ( "(" | ")" | "{" | "}" | ; | ,)
//errores = [^]


//numero decimal con exponente
 
%%

//{comentmulti} {}
{espacios} {}
//{reservadas} {bw.write("<palabra reservada,"+yytext()+">" +"\n");}
{decex} {bw.write("3 decimal: "+yytext()+"\n");  }
{id} {bw.write("1 id: "+yytext()+"\n");  }
{numeros} {bw.write("2 numero:"+yytext()+"\n");  }
//{comentario} {}
//{cadenas} {bw.write("<cadena,"+yytext()+">\n");}
//{operadores} {bw.write("<operador,"+yytext()+">\n");}
//{especiales} {bw.write("<especiales,"+yytext()+">\n");}
//{errores} {bw.write("Error en linea " + (yyline+1) + " en el caracter: " + yytext());}

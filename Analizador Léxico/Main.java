import java.lang.*;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;

/**** Main se realizo para verificar que sí se estan leyendo los tokens y el número que debería retornar, hecho por:
        Aguilar Paulina, Aviles Bryan y Meza Ortega Fernando 16 Noviembre 2020   *****/
class Main
{
    public static void main(String[] args) {
        try {
                Yylex.archivo = new File(args[0]);
                Yylex lexer = new Yylex (new BufferedReader(new FileReader(Yylex.archivo)));
                int val = lexer.yylex();
                while (val != 0)
                {
                    System.out.println(Integer.toString(val) + " " + lexer.yytext());
                    val = lexer.yylex();
                }
                }catch (IOException e){
                System.out.println("Ocurrio un error al intentar acceder al archivo");
        }
    }
}
package compilador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

public class Main{
    public static void main(String args[]){
        /*Try para capturar la excepcion en caso de que no pueda abrir archivo... */
        try{            
            /*Para pasarlo mediante la termianl (consola) */
            File f = new File(args[0]);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            /*Creamos un lexer de tipo Yylex para el analisis lexico */
            Yylex lexer = new Yylex(br);
            /*Creamos el parser para procesar todos los tokens del lexer */
            Parser parser = new Parser(lexer);
            parser.parse();
            /*NO HAY QUE OLVIDAR CERRAR EL ARCHIVO */
            br.close();
        }catch(IOException e){
            System.out.println("Error al abrir el archivo");
        }
        
    }
}

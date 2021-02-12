package compilador;

import java.util.ArrayList;

/*Análisis Sintáctico-Semántico */
/*Tabla de símbolos: guarda información de las variables, funciones, etc. id del programa.*/

public class Symbol {
    /* Tipo, que en este caso se identifica con un entero*/
    Type tipo;
    /*Direccion local de memoria */
    int dir;
    /*El string del identificador */
    String id;
    /* */
    String clasificacion;
    /*Argumentos y sus tipos de una funcion ? */
    ArrayList<Type> args;

    public Symbol(String id, int dir, Type tipo, String clas, ArrayList<Type> args){
        this.id = id;
        this.dir = dir;
        this.tipo = tipo;
        this.clasificacion = clas;
        this.args = args;
    }
    
    public String toString(){
        String temp = " [";
        if (args == null)
        {
            temp += " nulo";
        }
        else
        {
            for (Type t: args)
            {
                temp += " " + Integer.toString(t.id);
            }
        }
        temp += " ] ";
        return id +" "+ Integer.toString(tipo.id) +" " + Integer.toString(dir) + " " + clasificacion + temp;
    }
}

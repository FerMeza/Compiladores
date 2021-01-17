package compilador;

import java.util.ArrayList;

/*Análisis Sintáctico-Semántico */
/*Tabla de símbolos: guarda información de las variables, funciones, etc. id del programa.*/

public class Symbol {
    /* Tipo, que en este caso se identifica con un entero*/
    int type;
    /*Direccion local de memoria */
    int dir;
    /*El string del identificador */
    String id;
    /* */
    int var;
    /*Argumentos y sus tipos de una funcion ? */
    ArrayList<Integer> args;

    public Symbol(String id, int dir, int type, int var, ArrayList<Integer> args){
        this.id = id;
        this.dir = dir;
        this.type = type;
        this.var = var;
        this.args = args;
    }
}

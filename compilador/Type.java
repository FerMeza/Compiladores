package compilador;

/*Analisis sintáctico-semántico */
/*Tabla de tipos: tipos nativos del lenguaje de programacion y los definidos por el 
usuario(estructuras, arreglos, clases) */

public class Type {
    /*id del tipo*/
    int id;
    /*Nombre que describe el tipo*/
    String type;
    /*Tamaño del tipo (byte's)*/ 
    int tam;
    /* Numero de elementos del arreglo*/
    int elem;
    /* Se usa para los arreglos*/
    int tipoBase;

    /*Un constructor basico para inicializar todos los atributos*/
    public Type(int id, String type, int tam, int elem, int tipoBase){
        this.id = id;
        this.type = type;
        this.tam = tam;
        this.elem = elem;
        this.tipoBase = tipoBase;
    }
}

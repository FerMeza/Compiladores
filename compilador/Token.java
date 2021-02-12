package compilador;

public class Token{
    /*Clase: (tipo), valor: valor lexico, type: (subtypo)*/
    int clase;
    String valor;
    int type;
    int linea;
    int columna;

    public Token(Token copia)
    {
        this.clase = copia.clase;
        this.valor = copia.valor;
        this.type = copia.type;
        this.linea = copia.linea;
        this.columna = copia.columna;
    }

    /* Constructor ocupado en el analizador lexico, para almacenar el valor lexico y el tipo (id) */
    public Token(int clase, String valor, int linea, int columna){
        this.clase = clase;
        this.valor = valor;
        this.linea = linea;
        this.columna = columna;
    }
   
    /* Constructor ocupado en el analizador lexico, para almacenar el valor lexico, 
    el tipo (id (3 digitos)) y el subtipo(tal vez solo es un digito) */
    public Token(int clase, String valor, int type, int linea, int columna){
        this.clase = clase;
        this.valor = valor;
        this.type = type;
        this.linea = linea;
        this.columna = columna;
    }

    /*Costrunctor por default*/
    public Token(){       
    }

    @Override
    public String toString()
    {
        return valor;
    }

    /*compara si el tipo o id del token es igual al del token que se le pasa como parametro */
    public boolean equals(Token x){
        return this.clase == x.clase;
    }

    /*compara si el tipo o id del token es igual al id o tipo(int) que se le pasa como parametro*/
    public boolean equals(int clase){
        return this.clase == clase;
    }
}
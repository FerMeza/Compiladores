package compilador;

/*Importaciones para la excepcion que es lanzada por ____, 
usamos arraylist que es como un arreglo pero mas pro*/

import java.io.IOException;
import java.util.ArrayList;

/*Clase que nos ayuda para el analis sintactico y el semantico*/
public class Parser{
	//Declaraciones
  //Por el momento la tabla de simbolos es un array list
  ArrayList<Symbol> tablaSimbolos;
  /*Por el momento la tabla de tipos es un array list, podría ser una hashtable (?)*/
  ArrayList<Type> tablaTipos;
  //Necesitamos acceder al lexer, para poder "leer" los tokens
  Yylex lexer;
  
  public static Token actual = null; 
  /*La variable actual, es un token y nos indica el token que estamos "procesando"*/
  /*Atributo direccion...*/
  int dir = 0;
  
  
  //Metodo para instaciar un Parser, recibe un lexer
  public Parser(Yylex lexer)throws IOException{
  			//Toma el lexer pasado como argumento y lo asigna
        this.lexer = lexer;
        //Recibe el primer Token en la variable global y se convierte en el token actual
        actual = lexer.yylex();
        
        //Creacion de tabla de simbolos y de tipos
        tablaSimbolos = new ArrayList<Symbol>();
        tablaTipos = new ArrayList<Type>();
        
      	/*Agregamos los tipos nativos del lenguaje*/ 
        tablaTipos.add(new Type(0, "int", 4, -1, -1));
        tablaTipos.add(new Type(1, "float", 4, -1, -1));
        tablaTipos.add(new Type(2, "char", 1, -1, -1));
        tablaTipos.add(new Type(3, "double", 8, -1, -1));
        
        /*¿void es un tipo comun? ¿Como debería procesarse? */
        //tablaTipos.add(new Type(1, "void", 4, -1, -1));
  }

		/*EL método parse es más que nada una prueba que posiblemente se irá cambiando
    en el futuro*/
	void parse()throws IOException{
        programa();	//simbolo inicial
        printTS();
        printTT();
    } 
    
    /*Eat nos permite consumir el simbolo gramatical actual*/
    void eat(int i) throws IOException{
        if(actual.equals(i)){
            actual = lexer.yylex();
        }else{
            error("Error de sintaxis");
        }
    }
    
/*Funciones que definen a los no terminales*/
	
      // FIRST(programa) = {ε, int, float, char, double, void, func} 
     
     void programa()throws IOException{
     	if(actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT) || actual.equals(Yylex.CHAR) 
        || actual.equals(Yylex.DOUBLE) || actual.equals(Yylex.VOID) || actual.equals(Yylex.FUNC)){
     		declaraciones();
            funciones();
        }
     }
     
     //FIRST(declaraciones) = {ε, int, float, char, double, void}
     void declaraciones()throws IOException{
        if (actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT)
        || actual.equals(Yylex.CHAR) || actual.equals(Yylex.DOUBLE)
        || actual.equals(Yylex.VOID))
        {
    	tipo();
        lista_var();
        eat(Yylex.PUNCOMA);
        declaraciones();
        }
     }

     //FIRST(tipo) = {int, float, char, double, void}
     void tipo()throws IOException{
        if (actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT)
        || actual.equals(Yylex.CHAR) || actual.equals(Yylex.DOUBLE)
        || actual.equals(Yylex.VOID)){
            basico();
            compuesto();
        }
        else
        {
            error("Error de sintaxis");
        }
     }

    //FIRST(basico) = {int, float, char, double, void}
    void basico()throws IOException
    {
        if (actual.equals(Yylex.INT))
        {
            eat(Yylex.INT);
        }
        else if (actual.equals(Yylex.FLOAT))
        {
            eat(Yylex.FLOAT);
        }
        else if (actual.equals(Yylex.CHAR))
        {
            eat(Yylex.CHAR);
        }
        else if (actual.equals(Yylex.DOUBLE))
        {
            eat(Yylex.DOUBLE);
        }
        else if (actual.equals(Yylex.VOID))
        {
            eat(Yylex.VOID);
        }
        else
        {
            error("Error de sintaxis");
        }
    }

    //FIRST(compuesto) = {[, ε}
    void compuesto()throws IOException
    {
        if (actual.equals(Yylex.COR_L))
        {
            eat(Yylex.COR_L);
            eat(Yylex.NUMEROS);
            eat(Yylex.COR_R);
            compuesto();
        }
    }

    //FIRST(lista_var) = {id}
    void lista_var()throws IOException
    {
        if (actual.equals(Yylex.ID))
        {
            eat(Yylex.ID);
            lista_var_1();
        }
    }

    //FIRST(lista_var’) = {”,” , ε}
    void lista_var_1()throws IOException
    {
        if (actual.equals(Yylex.COMA))
        {
            eat(Yylex.COMA);
            eat(Yylex.ID);
            lista_var_1();
        }
    }

    //FIRST(funciones) = {func, ε}
    void funciones()throws IOException
    {
        if (actual.equals(Yylex.FUNC))
        {
            eat(Yylex.FUNC);
            eat(Yylex.ID);
            eat(Yylex.PAR_L);
            argumentos();
            eat(Yylex.PAR_R);
            bloque();
            funciones();
        }
    }

    //FIRST (argumentos) = {int, float, char, double, void, ε}
    void argumentos()throws IOException
    {
        if (actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT)
        || actual.equals(Yylex.CHAR) || actual.equals(Yylex.DOUBLE)
        || actual.equals(Yylex.VOID))
        {
            lista_args();
        }
    }

    //FIRST(lista_args) = {int, float, char, double, void}
    void lista_args()throws IOException
    {
        if (actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT)
        || actual.equals(Yylex.CHAR) || actual.equals(Yylex.DOUBLE)
        || actual.equals(Yylex.VOID)){
            tipo();
            eat(Yylex.ID);
            lista_var_1();
        }
        else
        {
            error("Error de sintaxis");
        }
    }

    // FIRST(lista_args’) = {”,” , ε}
    void lista_args_1()throws IOException
    {
        if (actual.equals(Yylex.COMA))
        {
            eat(Yylex.COMA);
            eat(Yylex.ID);
            lista_var_1();
        }
    }

    //FIRST(bloque) = {“{”}
    void bloque()throws IOException
    {
        if (actual.equals(Yylex.LLA_L))
        {
            eat(Yylex.LLA_L);
            declaraciones();
            instrucciones();
            eat(Yylex.LLA_R);
        }
        else
        {
            error("Error de Sintaxis");
        }
    }
    
    //FIRST(instrucciones) = {if, id, while, do, break, “{”, switch}
    void instrucciones()throws IOException
    {
        if (actual.equals(Yylex.IF) || actual.equals(Yylex.ID) ||
            actual.equals(Yylex.WHILE) || actual.equals(Yylex.DO) ||
            actual.equals(Yylex.BREAK) || actual.equals(Yylex.LLA_L))
        {
            //sentencia();
            instrucciones_1();
        }
        else
        {
            error("Error de Sintaxis");
        }   
    }

    //FIRST(instrucciones’) = {if, id, while, do, break, “{”, switch, ε}
    void instrucciones_1()throws IOException
    {
        if (actual.equals(Yylex.IF) || actual.equals(Yylex.ID) ||
            actual.equals(Yylex.WHILE) || actual.equals(Yylex.DO) ||
            actual.equals(Yylex.BREAK) || actual.equals(Yylex.LLA_L))
        {
            //sentencia();
            instrucciones_1();
        }
    }

    /*Para imprimir msg de error */
    void error(String msg){
        System.out.println(msg);
    }

    boolean buscar(String id){
        for(Symbol s: tablaSimbolos){
            if(s.id.equals(id)){
                return true;
            }
        }
        return false;
    }

    int getTam(int id){
        for(Type t : tablaTipos){
            if(id== t.id){
                return t.tam;
            }
        }
        return -1;
    }


    void printTT(){
        System.out.println("Tabla de tipos");
        for(Type t : tablaTipos){
            System.out.println(t.id+"\t"+t.type+"\t"+t.tam+"\t"+t.elem+"\t"+t.tipoBase);
        }    
    }

    void printTS(){
        System.out.println("Tabla de símbolos");
        int i=0;
        for(Symbol s : tablaSimbolos){
            System.out.println(""+i+"\t"+s.id+"\t"+s.dir+"\t"+s.type+"\t"+s.var);
            i++;
        }    
    }

}
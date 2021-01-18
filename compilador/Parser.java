package compilador;

/*Importaciones para la excepcion que es lanzada por ____, 
usamos arraylist que es como un arreglo pero mas pro*/

import java.io.IOException;
import java.util.ArrayList;

import javax.lang.model.util.ElementScanner14;

import org.graalvm.compiler.asm.aarch64.AArch64Assembler.Instruction;

import dds.Token;

/*Clase que nos ayuda para el analis sintactico y el semantico*/
public class Parser{
	//Declaraciones
  //Por el momento la tabla de simbolos es un array list
  ArrayList<Symbol> tablaSimbolos;
  /*Por el momento la tabla de tipos es un array list, podría ser una hashtable (?)*/
  ArrayList<Type> tablaTipos;
  //Necesitamos acceder al lexer, para poder "leer" los tokens
  Yylex lexer;
  
  /*El atributo actual, es un token y nos indica el token que estamos "procesando"*/
  Token actual;
  /*Atributo direccion...*/
  int dir = 0;
  
  
  //Metodo para instaciar un Parser, recibe un lexer
  public Parser(Yylex lexer)throws IOException{
  			//Toma el lexer pasado como argumento y lo asigna
        this.lexer = lexer;
        //Recibe el primer Token y se convierte en el token actual
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
            actual =lexer.yylex();
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
        else
        {
            error("Error de sintaxis");
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
            eat(Yylex.ENTEROS);
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
            sentencia();
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
            sentencia();
            instrucciones_1();
        }
    }

}


/* De la clase Yylex, acceder como si fueran atributos estaticos
NUMEROS = 100;
DECEX = 101;
CADENAS =102;
TRUE = 103;
FALSE  = 104;
INT = 105;
FLOAT = 106;
CHAR = 107;
DOUBLE = 108;
VOID = 109;
IF = 110;
WHILE = 111;
DO = 112;
BREAK = 113;
SWITCH = 114;
CASE= 115;
FUNC = 116;
DEFAULT = 117;
PAR_L = 118;
PAR_R = 119; 
COR_L = 120;
COR_R = 121;
LLA_L = 122;
LLA_R = 123;
ADD_OP1 = 124;
ADD_OP2 = 125;
MUL_OP1 = 126;
MUL_OP2 = 127;
MUL_OP3 = 128;
UNARY_OP1 = 129;
REL_OP_1 = 130;
REL_OP_2 = 131;
REL_OP_3 = 132;
REL_OP_4 = 133;
REL_OP_5 = 134;
REL_OP_6 = 135;
OR = 136;
AND = 137;
PUNCOMA = 138;
COMA = 139;
DOSPUNTOS = 140;
ID = 141;
*/

////////////////////////////////////////////////////////////////////
/*Codigo de ejemplo con una gramatica mas simple, sin todos los elementos de la clase*/
public class Parser{

    void D()throws IOException{
        if(actual.equals(INT) || actual.equals(FLOAT) ){
            int tipo = T();
            L(tipo);
            eat(PYC);
            D();
        }
    }

    int T()throws IOException{
        int tipo;
        tipo = B();
        tipo = C(tipo);
        return tipo;
    }

    int B() throws IOException{
        if(actual.equals(INT)){
            eat(INT);
            return 0;
        }
        if(actual.equals(FLOAT)){
            eat(FLOAT);
            return 1;
        }
        error("error de sintaxis");
        return -1;
    }

    int C (int base)throws IOException{
        String valor;
        int tipo;
        int id;
        if(actual.equals(LCOR)){            
            eat(LCOR);
            valor = actual.valor;
            eat(NUM);
            eat(RCOR);
            tipo = C(base);            
            id = tablaTipos.size();
            int tam = Integer.parseInt(valor) * getTam(tipo);
            tablaTipos.add(new Type(id, "array", tam ,Integer.parseInt(valor),tipo));
            return id;
        }else{
            return base;
        }
    }

    void L(int tipo)throws IOException{
        if(actual.equals(ID)){
            if(!buscar(actual.valor)){
                tablaSimbolos.add(new Symbol(actual.valor,dir, tipo, 0, null ));
                dir += getTam(tipo);
            }else{
                error("Variable definida dos veces");
            }
            eat(ID);
            LP(tipo);
        }else{
            error("error de sintaxis");
        }
    }

    void LP(int tipo)throws IOException{
        if(actual.equals(COMA)){
            eat(COMA);
            if(!buscar(actual.valor)){
                tablaSimbolos.add(new Symbol(actual.valor,dir, tipo, 0, null ));
                dir += getTam(tipo);
            }else{
                error("Variable definida dos veces");
            }
            eat(ID);
            LP(tipo);
        }
    }

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
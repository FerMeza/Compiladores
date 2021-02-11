package compilador;

/*Importaciones para la excepcion que es lanzada por ____, 
usamos arraylist que es como un arreglo pero mas pro*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/*Clase que nos ayuda para el analis sintactico y el semantico*/
public class Parser{
	//Declaraciones

  //Por el momento la tabla de simbolos es un array list y se
  // guardan en pila
  Stack<ArrayList<Symbol>> PilaTS;
   /*Por el momento la tabla de tipos es un array list y se
   guardan en una pila*/ 

  Stack<ArrayList<Type>> PilaTT;
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
    Token eat(int i) throws IOException{
        if(actual.equals(i)){
            actual = lexer.yylex();
        }else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
        return actual;
    }
    
/*Funciones que definen a los no terminales*/
	
      // FIRST(programa) = {ε, int, float, char, double, void, func} 
    /*programa → declaraciones funciones*/
     void programa()throws IOException{
     	if(actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT) || actual.equals(Yylex.CHAR) 
        || actual.equals(Yylex.DOUBLE) || actual.equals(Yylex.VOID) || actual.equals(Yylex.FUNC)){
     		PilaTS.push(new ArrayList<Symbol>);
            ArrayList<Type> temp = new ArrayList<Type>();

            /*Agregamos los tipos nativos del lenguaje*/ 
            temp.add(new Type(Yylex.INT, "int", 4, -1, -1));
            temp.add(new Type(Yylex.FLOAT, "float", 4, -1, -1));
            temp.add(new Type(Yylex.CHAR, "char", 1, -1, -1));
            temp.add(new Type(Yylex.DOUBLE, "double", 8, -1, -1));
            PilaTT.push(temp);
            dir = 0;

            declaraciones();
            funciones();
        }
        
       
     }
     
     //FIRST(declaraciones) = {ε, int, float, char, double, void}
     /* declaraciones → tipo lista_var ; declaraciones | ε */
     void declaraciones()throws IOException{
        if (actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT)
        || actual.equals(Yylex.CHAR) || actual.equals(Yylex.DOUBLE)
        || actual.equals(Yylex.VOID)) {
        	Atributos retorno = tipo();
            lista_var(retorno); //retorno.tipo
            eat(Yylex.PUNCOMA);
            declaraciones();
        }
     }

     //FIRST(tipo) = {int, float, char, double, void}
     /*tipo → basico compuesto */
     Atributos tipo()throws IOException{
        if (actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT)
        || actual.equals(Yylex.CHAR) || actual.equals(Yylex.DOUBLE)
        || actual.equals(Yylex.VOID)){
            Atributos retorno = basico();  
            Atributos retorno2 = compuesto(retorno);
            return retorno2;
        }
        else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
     }

    //FIRST(basico) = {int, float, char, double, void}
    /*basico →  int | float | char | double | void  */
    Atributos basico()throws IOException
    {
        if (actual.equals(Yylex.INT))
        {
            eat(Yylex.INT);
            Atributos retorno = new Atributos();
            retorno.tipo = Yylex.INT;
            return retorno;
        }
        else if (actual.equals(Yylex.FLOAT))
        {
            eat(Yylex.FLOAT);
            Atributos retorno = new Atributos();
            retorno.tipo = Yylex.INT;
            return retorno;
        }
        else if (actual.equals(Yylex.CHAR))
        {
            eat(Yylex.CHAR);
            Atributos retorno = new Atributos();
            retorno.tipo = Yylex.CHAR;
            return retorno;
        }
        else if (actual.equals(Yylex.DOUBLE))
        {
            eat(Yylex.DOUBLE);
            Atributos retorno = new Atributos();
            retorno.tipo = Yylex.DOUBLE;
            return retorno;
        }
        else if (actual.equals(Yylex.VOID))
        {
            eat(Yylex.VOID);
            Atributos retorno = new Atributos();
            retorno.tipo = Yylex.VOID;
            return retorno;
        }
        else
        {
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }

    //FIRST(compuesto) = {[, ε}
    /*compuesto →[numero] compuesto | ε */
    Atributos compuesto(Atributos entrada)throws IOException
    {
        if (actual.equals(Yylex.COR_L))
        {
            eat(Yylex.COR_L);
            Token temporal = eat(Yylex.NUMEROS);
            eat(Yylex.COR_R);

            compuesto(entrada);
            ArrayList <Type> actual = PilaTT.top(); 
            Type insertar = new Type(actual.);
            entrada.tipo = actual.add(actual.size(), 
        } else
        } else
        {
            entrada.tipo = entrada.base;
        }
    }

    //FIRST(lista_var) = {id}
    /*lista_var  → id lista_var’ */
    void lista_var(int tipo)throws IOException
    {
        if (actual.equals(Yylex.ID))
        {
            eat(Yylex.ID);
            lista_var_1();
        }
        else
        {
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }

    //FIRST(lista_var’) = {”,” , ε}
    /* lista_var’ → , id lista_var’ | ε*/
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
    /*funciones  → func tipo id ( argumentos ) bloque funciones | ε */
    void funciones()throws IOException
    {
        if (actual.equals(Yylex.FUNC))
        {
            eat(Yylex.FUNC);
            tipo();
            eat(Yylex.ID);
            eat(Yylex.PAR_L);
            argumentos();
            eat(Yylex.PAR_R);
            bloque();
            funciones();
        }
    }

    //FIRST (argumentos) = {int, float, char, double, void, ε}
    /* argumentos  → lista_args | ε*/
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
    /*lista_args →tipo id lista_args’  */
    void lista_args()throws IOException
    {
        if (actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT)
        || actual.equals(Yylex.CHAR) || actual.equals(Yylex.DOUBLE)
        || actual.equals(Yylex.VOID)){
            tipo();
            eat(Yylex.ID);
            lista_args_1();
        }
        else
        {
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }

    // FIRST(lista_args’) = {”,” , ε}
    /*lista_args’→ ,tipo id lista_args’ | ε */
    void lista_args_1()throws IOException
    {
        if (actual.equals(Yylex.COMA))
        {
            eat(Yylex.COMA);
            tipo();
            eat(Yylex.ID);
            lista_args_1();
        }
    }

    //FIRST(bloque) = {“{”}
    /*bloque  → { declaraciones instrucciones } */
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
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }
    
    //FIRST(instrucciones) = {if, while, do, break, ‘{’, return, switch, print, scan, id} 
    /*instrucciones → sentencia instrucciones’ */
    void instrucciones()throws IOException
    {
        if (actual.equals(Yylex.IF) || actual.equals(Yylex.ID) ||
            actual.equals(Yylex.WHILE) || actual.equals(Yylex.DO) ||
            actual.equals(Yylex.BREAK) || actual.equals(Yylex.LLA_L) ||
            actual.equals(Yylex.SWITCH) || actual.equals(Yylex.RETURN) ||
            actual.equals(Yylex.PRINT) || actual.equals(Yylex.SCAN))
        {
            sentencia();
            instrucciones_1();
        }
        else
        {
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }   
    }

    //FIRST(instrucciones’) ={if, while, do, break, ‘{’, return, switch, print, scan, id,ε }
    /* instrucciones’ → sentencia instrucciones’ | ε*/
    void instrucciones_1()throws IOException
    {
        if (actual.equals(Yylex.IF) || actual.equals(Yylex.ID) ||
            actual.equals(Yylex.WHILE) || actual.equals(Yylex.DO) ||
            actual.equals(Yylex.BREAK) || actual.equals(Yylex.LLA_L) ||
            actual.equals(Yylex.SWITCH) || actual.equals(Yylex.RETURN) ||
            actual.equals(Yylex.PRINT) || actual.equals(Yylex.SCAN))
        {
            sentencia();
            instrucciones_1();
        }
    }

    //FIRST(sentencia) = {if, while, do, break, ‘{’, return, switch, print, scan, id }
    void sentencia()throws IOException{
        if(actual.equals(Yylex.ID)){
            parte_izquierda();
            eat(Yylex.ASIGNACION);    // "="
            bool();
            eat(Yylex.PUNCOMA);
        } else if(actual.equals(Yylex.IF)){
            eat(Yylex.IF);
            eat(Yylex.PAR_L);
            bool();
            eat(Yylex.PAR_R);
            sentencia();
            sentencia_1();
        }else if(actual.equals(Yylex.WHILE)){
            eat(Yylex.WHILE);
            eat(Yylex.PAR_L);
            bool();
            eat(Yylex.PAR_R);
            sentencia();
        }else if(actual.equals(Yylex.DO)){
            eat(Yylex.DO);
            sentencia();
            eat(Yylex.WHILE);
            eat(Yylex.PAR_L);
            bool();
            eat(Yylex.PAR_R);
        }else if(actual.equals(Yylex.BREAK)){
            eat(Yylex.BREAK);
            eat(Yylex.PUNCOMA);
        }else if(actual.equals(Yylex.LLA_L)){
            bloque();
        }else if(actual.equals(Yylex.RETURN)){
            eat(Yylex.RETURN);
            sentencia_2();
        }else if(actual.equals(Yylex.SWITCH)){
            eat(Yylex.SWITCH);
            eat(Yylex.PAR_L);
            bool();
            eat(Yylex.PAR_R);
            eat(Yylex.LLA_L);
            casos();
            eat(Yylex.LLA_R);
        }else if(actual.equals(Yylex.PRINT)){
            eat(Yylex.PRINT);
            exp();
            eat(Yylex.PUNCOMA);
        }else if(actual.equals(Yylex.SCAN)){
            eat(Yylex.SCAN);
            parte_izquierda();
            eat(Yylex.PUNCOMA);
        }else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }

    //FIRST(sentencia’) = {else, ε}
    void sentencia_1()throws IOException{
        if(actual.equals(Yylex.ELSE)){
            eat(Yylex.ELSE);
            sentencia();
        }
    }

    //FIRST(sentencia’’) = {‘;’, !, -, id, ‘(’, numero, cadena, true, false} 
    void sentencia_2()throws IOException{
        if( actual.equals(Yylex.NEGACION) || actual.equals(Yylex.MENOS) || actual.equals(Yylex.PAR_L) 
        || actual.equals(Yylex.NUMEROS) || actual.equals(Yylex.CADENAS) || actual.equals(Yylex.TRUE) 
        || actual.equals(Yylex.FALSE) || actual.equals(Yylex.ID) ){
            exp();
            eat(Yylex.PUNCOMA);
        }else if(actual.equals(Yylex.PUNCOMA)){
            eat(Yylex.PUNCOMA);
        }else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }

    void casos()throws IOException{
        if(actual.equals(Yylex.CASE)){
            caso();
            casos();
        } else if(actual.equals(Yylex.DEFAULT)){
            predeterminado();
        }
    }

    void caso()throws IOException{
        if(actual.equals(Yylex.CASE)){
            eat(Yylex.CASE);
            eat(Yylex.NUMEROS);
            eat(Yylex.DOSPUNTOS);
            instrucciones();
        } else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }

    void predeterminado()throws IOException{
        if(actual.equals(Yylex.DEFAULT)){
            eat(Yylex.DEFAULT);
            eat(Yylex.DOSPUNTOS);
            instrucciones();
        } else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }

    void parte_izquierda()throws IOException{
        if(actual.equals(Yylex.ID)){
           eat(Yylex.ID);
           parte_izquierda_1();
        } else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }

     void parte_izquierda_1()throws IOException{
        if(actual.equals(Yylex.COR_L)){
           localizacion();
        }
    }

    void bool()throws IOException{
        if( actual.equals(Yylex.NEGACION) || actual.equals(Yylex.MENOS) || actual.equals(Yylex.PAR_L) 
        || actual.equals(Yylex.NUMEROS) || actual.equals(Yylex.CADENAS) || actual.equals(Yylex.TRUE) 
        || actual.equals(Yylex.FALSE) || actual.equals(Yylex.ID) ){
            comb();
            bool_1();
        } else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }    

    void bool_1()throws IOException{
        if(actual.equals(Yylex.OR)){
            eat(Yylex.OR);
            comb();
            bool_1();
        }
    }    

     void comb()throws IOException{
        if( actual.equals(Yylex.NEGACION) || actual.equals(Yylex.MENOS) || actual.equals(Yylex.PAR_L) 
        || actual.equals(Yylex.NUMEROS) || actual.equals(Yylex.CADENAS) || actual.equals(Yylex.TRUE) 
        || actual.equals(Yylex.FALSE) || actual.equals(Yylex.ID) ){
            igualdad();
            comb_1();
        } else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }

    void comb_1()throws IOException{
        if(actual.equals(Yylex.AND)){
            eat(Yylex.AND);
            igualdad();
            comb_1();
        }
    }    

    void igualdad()throws IOException{
        if( actual.equals(Yylex.NEGACION) || actual.equals(Yylex.MENOS) || actual.equals(Yylex.PAR_L) 
        || actual.equals(Yylex.NUMEROS) || actual.equals(Yylex.CADENAS) || actual.equals(Yylex.TRUE) 
        || actual.equals(Yylex.FALSE) || actual.equals(Yylex.ID) ){
            rel();
            igualdad_1();
        } else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }

    void igualdad_1()throws IOException{
        if( actual.equals(Yylex.COMPARACION))
        {
            eat(Yylex.COMPARACION);
            rel();
            igualdad_1();
        }
        else if (actual.equals(Yylex.DIFERENTEDE) )
        {
            eat(Yylex.DIFERENTEDE);
            rel();
            igualdad_1();
        }
        else
        {
            //Prodcuccion vacia
        }
    }

    void rel()throws IOException{
        if( actual.equals(Yylex.NEGACION) || actual.equals(Yylex.MENOS) || actual.equals(Yylex.PAR_L) 
        || actual.equals(Yylex.NUMEROS) || actual.equals(Yylex.CADENAS) || actual.equals(Yylex.TRUE) 
        || actual.equals(Yylex.FALSE) || actual.equals(Yylex.ID) ){
            exp();
            rel_1();
        } else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }  

    void rel_1()throws IOException{
        if(actual.equals(Yylex.MENOR)){
            eat(Yylex.MENOR);
            exp();
        }else if(actual.equals(Yylex.MENORIGUAL)){
            eat(Yylex.MENORIGUAL);
            exp();
        }else if(actual.equals(Yylex.MAYORIGUAL)){
            eat(Yylex.MAYORIGUAL);
            exp();
        }else if(actual.equals(Yylex.MAYOR)){
            eat(Yylex.MAYOR);
            exp();
        }
    }

    void exp() throws IOException{
        if( actual.equals(Yylex.NEGACION) || actual.equals(Yylex.MENOS) ||
            actual.equals(Yylex.PAR_L) || actual.equals(Yylex.NUMEROS) || 
            actual.equals(Yylex.CADENAS) || actual.equals(Yylex.TRUE) || 
            actual.equals(Yylex.FALSE) || actual.equals(Yylex.ID) ){
            term();
            exp_1();
        } else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }  

    void exp_1()throws IOException{
        if( actual.equals(Yylex.MAS)){
            eat(Yylex.MAS);
            term();
            exp_1();
        } else if(actual.equals(Yylex.MENOS)){
            eat(Yylex.MENOS);
            term();
            exp_1();
        } else{
           
        }
    }

    void term()throws IOException{
            if( actual.equals(Yylex.NEGACION) || actual.equals(Yylex.MENOS) || 
                actual.equals(Yylex.PAR_L)  || actual.equals(Yylex.NUMEROS) || 
                actual.equals(Yylex.CADENAS) || actual.equals(Yylex.TRUE) || 
                actual.equals(Yylex.FALSE) || actual.equals(Yylex.ID) ){
                unario();
                term_1();
            } else{
                error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
                " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
            }
    }  

    void term_1()throws IOException{
            if( actual.equals(Yylex.ASTERISCO)){
                eat(Yylex.ASTERISCO);
                unario();
                term_1();
            } else if(actual.equals(Yylex.DIVISION)){
                eat(Yylex.DIVISION);
                unario();
                term_1();
            } else if(actual.equals(Yylex.MODULO)){
                eat(Yylex.MODULO);
                unario();
                term_1();
            } else{
               
            }
    }


    void unario()throws IOException{
            if( actual.equals(Yylex.NEGACION)){
                eat(Yylex.NEGACION);
                unario();
            } else if(actual.equals(Yylex.MENOS)){
                eat(Yylex.MENOS);
                unario();
            } else if(actual.equals(Yylex.PAR_L)  || actual.equals(Yylex.NUMEROS) || 
                actual.equals(Yylex.CADENAS) || actual.equals(Yylex.TRUE) || 
                actual.equals(Yylex.FALSE) || actual.equals(Yylex.ID) ){
                factor();
            } else{
                error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
                " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
            }
    }

    void factor()throws IOException{
            if( actual.equals(Yylex.ID)){
                eat(Yylex.ID);
                factor_1();
            } else if(actual.equals(Yylex.PAR_L)){
                eat(Yylex.PAR_L);
                bool();
                eat(Yylex.PAR_R);
            } else if(actual.equals(Yylex.NUMEROS)){
                eat(Yylex.NUMEROS);
            } else if(actual.equals(Yylex.CADENAS)){
                eat(Yylex.CADENAS);
            } else if(actual.equals(Yylex.TRUE)){
                eat(Yylex.TRUE);
            } else if(actual.equals(Yylex.FALSE)){
                eat(Yylex.FALSE);
            } else{
                error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
                " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
            }
    }

     void factor_1()throws IOException{
        if(actual.equals(Yylex.COR_L)){
            localizacion();
        } else if(actual.equals(Yylex.PAR_L)){
            eat(Yylex.PAR_L);
            parametros();
            eat(Yylex.PAR_R);
        }
    }

    void parametros()throws IOException{
            if( actual.equals(Yylex.NEGACION) || actual.equals(Yylex.MENOS) ||
                actual.equals(Yylex.PAR_L) || actual.equals(Yylex.NUMEROS) || 
                actual.equals(Yylex.CADENAS) || actual.equals(Yylex.TRUE) || 
                actual.equals(Yylex.FALSE) || actual.equals(Yylex.ID) ){
                lista_param();
            } 
        }  
        
    void lista_param()throws IOException{
    if(actual.equals(Yylex.NEGACION) || actual.equals(Yylex.MENOS) ||
        actual.equals(Yylex.PAR_L) || actual.equals(Yylex.NUMEROS) || 
        actual.equals(Yylex.CADENAS) || actual.equals(Yylex.TRUE) || 
        actual.equals(Yylex.FALSE) || actual.equals(Yylex.ID)){
        bool();
        lista_param_1();            
    } else{
        error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
        " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
    }
    }  

    void lista_param_1()throws IOException{
        if( actual.equals(Yylex.COMA)){
                eat(Yylex.COMA);
                bool();
                lista_param_1();
            } 
    }
    
    void localizacion()throws IOException{
        if( actual.equals(Yylex.COR_L)){
                eat(Yylex.COR_L);
                bool();
                eat(Yylex.COR_R);
                localizacion_1();
            } else{
                error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
                " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
            }
    }

    void localizacion_1()throws IOException{
        if( actual.equals(Yylex.COR_L)){
                eat(Yylex.COR_L);
                bool();
                eat(Yylex.COR_R);
                localizacion_1();
            } 
    }

    /*Para imprimir msg de error */
    void error(String msg){
        System.out.println(msg);
    }

    //Para buscar si el id se encuentra en la tabla de simbolos
    boolean buscar(String id){
        for(Symbol s: tablaSimbolos){
            if(s.id.equals(id)){
                return true;
            }
        }
        return false;
    }

    //Retorna el tamaño de un tipo en la tabla de tipos
    int getTam(int id){
        for(Type t : tablaTipos){
            if(id== t.id){
                return t.tam;
            }
        }
        return -1;
    }

    //Para imprimir la tabla de tipos
    void printTT(){
        System.out.println("Tabla de tipos");
        for(Type t : tablaTipos){
            System.out.println(t.id+"\t"+t.type+"\t"+t.tam+"\t"+t.elem+"\t"+t.tipoBase);
        }    
    }

    //Para imprimir la tabla de simbolos
    void printTS(){
        System.out.println("Tabla de símbolos");
        int i=0;
        for(Symbol s : tablaSimbolos){
            System.out.println(""+i+"\t"+s.id+"\t"+s.dir+"\t"+s.type+"\t"+s.var);
            i++;
        }    
    }

}
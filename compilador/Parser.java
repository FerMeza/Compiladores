package compilador;

/*
Se importan la IOException
ArrayList para el kilo de listas que se ocupan
Y se utiliza el stack para conservar adecuadamente
la informacion de funcion en funcion*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/*Esta clase es la mas importante, 
se encarga de hacer el analisis sintactico con
ayuda de muchas clases auxiliares*/
public class Parser{

  //Creamos la pila que almacenara toda la tabla de simbolos
  Stack<TablaSimbolos> PilaTS;
 
  /*Se tiene una pila de las tablas de tipos*/
  Stack<TablaTipos> PilaTT;
  
  Stack<Integer> PilaDir;
  //Pila de direcciones
  
 /*Tenemos un atributo de la clase que es el lexer,
  este nos sirve para adquirir los tokens correspondientes*/
  Yylex lexer;
  
  /*El token actual es el token que se esta leyendo en ese momento,
  uno a uno los tokens se deben de reconocer*/
  public static Token actual = null; 
  
  /*Es un atributo para almacenar la direccion de esta manera podemos
  acceder a este atributo de manera "global" dentro de la clase*/
  int dir = 0;
  
  /*Se definen los tipos que ya estan definidos desde un inicio*/
  static public Type TVOID = new Type(0, "void", 0, -1, -1);
  static public Type TINT = new Type(1, "int", 4, -1, -1);
  static public Type TFLOAT = new Type(2, "float", 4, -1, -1);
  static public Type TCHAR = new Type(3, "char", 1, -1, -1);
  static public Type TDOUBLE = new Type(4, "double", 8, -1, -1);
  
  /*Se guarda el codigo en forma de cuadruplas*/
  ArrayList<Cuadruplas> Codigo;
  Stack<ArrayList<Type>> PilaLR;
  
  //Metodo para instaciar un Parser, recibe un lexer
  public Parser(Yylex lexer)throws IOException{
        //Toma el lexer pasado como argumento y lo asigna
        this.lexer = lexer;
        //Recibe el primer Token en la variable global y se convierte en el token actual
        actual = lexer.yylex();
        
        
        /*En este punto se inicializan estas pilas para poder ser utilizadas despues*/
        PilaTS = new Stack<>();
        PilaTT = new Stack<>();
        PilaDir = new Stack<>();
        Codigo = new ArrayList<>();
        PilaLR = new Stack<>();
        
    }

    /*Este metodo es el encargado de iniciar el analisis*/
	void parse()throws IOException{
        programa(); /*Se ejecuta esta funcion que es el simbolo inicial de la gramatica*/
        //printTS();
        //printTT();
    } 
    
    /*Eat nos sirve para consumir el token actual y en caso de que sea el token erroneo se mostrar
        un error de sintaxis sencillo, en teoria a futuro se deberia de mostrar el posible simbolo
        que se esperaba, por el momento simplemente nos indica que simbolo caso el error*/
    /*En caso de que no se genere el error entonces podemos retornar el token actual*/
    Token eat(int i) throws IOException{
        if(actual.equals(i)){
            actual = lexer.yylex();
        }else{
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
        return actual;
    }
    
    /*Funcion que detecta si se retorna el tipo que fue definido para la funcion*/
    public boolean equivalentesLista(ArrayList <Type> lista, Type tipo)
    {
        if (lista!=null){
            for (Type x: lista)
            {
            if (x != tipo)
                return false;
            }
        }
        return true;
    }
    
    /*A continuacion se definen las funciones que se relacionan directamente con los simbolos
    gramaticales que son no terminales*/
    
    /*Programa es el simbolo inicial de la gramatica, por ende es lo primero que se manda llamar
    indirectamente con otro metodo para iniciar el analisis sintactico y semantico*/
    
    /*PilaTS.push(nuevaTablaTS())
    PilaTT.push(nuevaTablaTT())
    dir  = 0
    */
    
    /*programa → {PilaTS.push(nuevaTablaTS())
    PilaTT.push(nuevaTablaTT())
    dir  = 0} declaraciones funciones*/
    
    void programa()throws IOException{
     	if(actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT) || actual.equals(Yylex.CHAR) 
        || actual.equals(Yylex.DOUBLE) || actual.equals(Yylex.VOID) || actual.equals(Yylex.FUNC)){
     	    
            /*Las acciones semanticas de esta funcion se traducen de una manera directa...*/
            PilaTS.push(new TablaSimbolos("Global"));
            PilaTT.push(new TablaTipos("Global"));
            dir = 0;

            declaraciones();
            funciones();
            PilaTS.peek().printTS();
            System.out.println();
            PilaTT.peek().printTT();
            PilaTS.pop();
            PilaTT.pop();
        }
          
    }
     
    void declaraciones()throws IOException{
        if (actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT)
        || actual.equals(Yylex.CHAR) || actual.equals(Yylex.DOUBLE)
        || actual.equals(Yylex.VOID)) {
            Type Tipotipo = tipo();
            lista_var(Tipotipo); 
            eat(Yylex.PUNCOMA);
            declaraciones();
        }
        else
        {
            /*Estas son las acciones semanticas cuando produce vacio :D*/
            /*...No hay acciones semanticas...*/
        }
    }

    Type tipo()throws IOException{
        Type retorno = null;
        if (actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT)
        || actual.equals(Yylex.CHAR) || actual.equals(Yylex.DOUBLE)
        || actual.equals(Yylex.VOID)){
            Type Tipobasico = basico();
            /*El atributo de "base" al final de cuentas es un tipo,
            es perfectamente valido pasarle un tipo de nuestra clase tipo*/
            retorno = compuesto(Tipobasico);
        }
        else{
            /*Manda error porque no hay produccion vacia!*/
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
        return retorno;
    }

    /*Retorna el tipo de dato dependiendo del token*/
    Type basico()throws IOException{
        if (actual.equals(Yylex.INT))
        {
            eat(Yylex.INT);
            return TINT;
        }
        else if (actual.equals(Yylex.FLOAT))
        {
            eat(Yylex.FLOAT);
            return TFLOAT;
        }
        else if (actual.equals(Yylex.CHAR))
        {
            eat(Yylex.CHAR);
            return TCHAR;
        }
        else if (actual.equals(Yylex.DOUBLE))
        {
            eat(Yylex.DOUBLE);
            return TDOUBLE;
        }
        else if (actual.equals(Yylex.VOID))
        {
            eat(Yylex.VOID);
            return TVOID;
        }
        else
        {
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
        return null;
    }
    
    Type compuesto(Type BaseH)throws IOException{
        if (actual.equals(Yylex.COR_L)){
            Token temporal = eat(Yylex.COR_L);
            /*Se guarda el numero para usarlo despues*/
            eat(Yylex.NUMEROS);
            eat(Yylex.COR_R);
            /*Se le pasa como parametro el tipo Base, ademas es heredado
            Retorna el tipo de compuesto 1 que sera pasado para crear el
            arreglo del numero de elementos que se indique*/
            Type com1Tipo = compuesto(BaseH);
            /*Se llama "a_insertar" porque se agrega como nuevo tipo, pero tambien
            se deberia de llamar "a_retornar" porque tambien se retorna!!!*/
            Type a_insertar = new Type(0, "array", com1Tipo.tam*Integer.parseInt(temporal.valor),
                    Integer.parseInt(temporal.valor), com1Tipo.id);
            PilaTT.peek().insertar(a_insertar);
            return a_insertar;
        } else{
            /*Cuando se produce epsilon simplemente se retorna lo que se pase como parametro*/
            return BaseH;
        }

    }

    void lista_var(Type listavTipo)throws IOException{
        if (actual.equals(Yylex.ID)){
            if(!PilaTS.peek().buscar(actual.valor)){
                Symbol sim = new Symbol(actual.valor, dir, listavTipo, "var", null);
                PilaTS.peek().insertar(sim);
                dir = dir + listavTipo.tam;
            } else{
                error("Error semantico en la linea " + Integer.toString(actual.linea) +
                    "y en la columna " + Integer.toString(actual.columna) + " el id ya fue declarado: " + actual.valor);
            }
            eat(Yylex.ID);
            lista_var_1(listavTipo);
        }
        else
        {
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
    }
    
    void lista_var_1(Type listav1Tipo)throws IOException{
        if (actual.equals(Yylex.COMA)){
            eat(Yylex.COMA);
            if(!PilaTS.peek().buscar(actual.valor)){
                Symbol sim = new Symbol(actual.valor, dir, listav1Tipo, "var", null);
                PilaTS.peek().insertar(sim);
                dir = dir + listav1Tipo.tam;
            } else{
                error("Error semantico en la linea " + Integer.toString(actual.linea) +
                   " y en la columna " + Integer.toString(actual.columna) + " el id ya fue declarado: " + actual.valor);
            }
            eat(Yylex.ID);
            lista_var_1(listav1Tipo);
        } else
        {
            /*Cuando se produce epsilon no hay reglas semanticas!!*/
        }
    }

    void funciones()throws IOException
    {
        if (actual.equals(Yylex.FUNC))
        {
            eat(Yylex.FUNC);
            
            ArrayList<Type> ListaRetorno = null;
            Type tipoTipo = tipo();
            Token id = new Token(actual);
            if (!PilaTS.peek().buscar(id.valor)) {
                eat(Yylex.ID);
                PilaTS.push(new TablaSimbolos(id.valor));
                PilaTT.push(new TablaTipos(id.valor));
                PilaDir.push(dir);
                PilaLR.push(new ArrayList<Type>());
                dir = 0;

                eat(Yylex.PAR_L);
                ArrayList<Type> argsLista = argumentos();
                eat(Yylex.PAR_R);
                Cuadruplas tempor = new Cuadruplas("label", "", "", id.valor);
                Codigo.add(tempor);
                Cuadruplas labBloque = new Cuadruplas("B");
                bloque();
                Codigo.add(labBloque);
                if (equivalentesLista(PilaLR.pop(), tipoTipo)) {
                    PilaTS.peek().printTS();
                    System.out.println();
                    PilaTT.peek().printTT();
                    PilaTS.pop();
                    PilaTT.pop();
                    PilaTS.peek().insertar(id.valor, tipoTipo, 0, "func", argsLista);
                } else{
                    error("Error semantico en la linea " + Integer.toString(id.linea) +
                   "y en la columna " + Integer.toString(id.columna) + " los tipos de"
                            + " retorno no coinciden con el de la funcion: " + id.valor);
                }
            } else {
                error("Error semantico en la linea " + Integer.toString(actual.linea) +
                " y en la columna " + Integer.toString(actual.columna) + " el id ya fue declarado: " + actual.valor);
            }
            dir = PilaDir.pop();
            funciones();
        } else
        {
            /*No se ejecuta ninguna accion semantica*/
        }
    }

    ArrayList<Type> argumentos()throws IOException{
        ArrayList<Type> argsLista = null;
        if (actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT)
        || actual.equals(Yylex.CHAR) || actual.equals(Yylex.DOUBLE)
        || actual.equals(Yylex.VOID)){
            argsLista = lista_args();
        }else{
            argsLista = null;
        }
        return argsLista;
    }

    ArrayList<Type> lista_args()throws IOException
    {
        ArrayList<Type> listaArgsS = null;
        if (actual.equals(Yylex.INT) || actual.equals(Yylex.FLOAT)
        || actual.equals(Yylex.CHAR) || actual.equals(Yylex.DOUBLE)
        || actual.equals(Yylex.VOID)){
            Type tipoTipo = tipo();
            if (!PilaTS.peek().buscar(actual.valor)) {
                PilaTS.peek().insertar(actual.valor, tipoTipo, dir, "param", null);
                dir = dir + tipoTipo.tam;
            }else{
                error("Error semantico en la linea " + Integer.toString(actual.linea) +
                    "y en la columna " + Integer.toString(actual.columna) + " el id ya fue declarado: " + actual.valor);
            }
            eat(Yylex.ID);
            ArrayList<Type> nuevaLista = new ArrayList<>();
            nuevaLista.add(tipoTipo);
            listaArgsS = lista_args_1(nuevaLista);
        }
        else
        {
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }
        return listaArgsS;
    }

    ArrayList<Type> lista_args_1(ArrayList<Type> ListaH)throws IOException{
        if (actual.equals(Yylex.COMA)){
            eat(Yylex.COMA);
            Type tipoTipo = tipo();
            if (!PilaTS.peek().buscar(actual.valor)) {
                PilaTS.peek().insertar(actual.valor, tipoTipo, dir, "param", null);
                dir = dir + tipoTipo.tam;
            }else{
                error("Error semantico en la linea " + Integer.toString(actual.linea) +
                        "y en la columna " + Integer.toString(actual.columna) + " el id ya fue declarado: " + actual.valor);
            }
            eat(Yylex.ID);
            ListaH.add(tipoTipo);
            ArrayList<Type> listaS = lista_args_1(ListaH);
            return listaS;
        }else{
            return ListaH;
        }
    }

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
    
    void instrucciones()throws IOException
    {
        if (actual.equals(Yylex.IF) || actual.equals(Yylex.ID) ||
            actual.equals(Yylex.WHILE) || actual.equals(Yylex.DO) ||
            actual.equals(Yylex.BREAK) || actual.equals(Yylex.LLA_L) ||
            actual.equals(Yylex.SWITCH) || actual.equals(Yylex.RETURN) ||
            actual.equals(Yylex.PRINT) || actual.equals(Yylex.SCAN))
        {      
            Cuadruplas labSentencia = new Cuadruplas("S");
            sentencia();
            Codigo.add(labSentencia);
            instrucciones_1();
        }
        else
        {
            error("Error de sintaxis en la linea " + Integer.toString(actual.linea) + 
            " y en la columna " + Integer.toString(actual.columna) + " del simbolo " + actual);
        }   
    }

    void instrucciones_1()throws IOException
    {
        if (actual.equals(Yylex.IF) || actual.equals(Yylex.ID) ||
            actual.equals(Yylex.WHILE) || actual.equals(Yylex.DO) ||
            actual.equals(Yylex.BREAK) || actual.equals(Yylex.LLA_L) ||
            actual.equals(Yylex.SWITCH) || actual.equals(Yylex.RETURN) ||
            actual.equals(Yylex.PRINT) || actual.equals(Yylex.SCAN))
        {
            Cuadruplas labSentencia = new Cuadruplas("S");
            sentencia();
            Codigo.add(labSentencia);
            instrucciones_1();
        }
    }

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
    boolean buscar(String id, ArrayList<Symbol> tablaSimbolos){
        for(Symbol s: tablaSimbolos){
            if(s.id.equals(id)){
                return true;
            }
        }
        return false;
    }

    //Retorna el tamaño de un tipo en la tabla de tipos
    int getTam(int id, ArrayList<Type> tablaTipos){
        for(Type t : tablaTipos){
            if(id== t.id){
                return t.tam;
            }
        }
        return -1;
    }



}
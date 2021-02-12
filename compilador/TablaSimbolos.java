package compilador;
import java.util.ArrayList;

public class TablaSimbolos
{
    public ArrayList<Symbol> contenido;
    String name;
    
    public TablaSimbolos(String name)
    {
        contenido = new ArrayList<Symbol>();
        this.name = name; 
    }

    public boolean buscar(String id)  //actual.valor
    {   
        boolean respuesta = false;
        for (Symbol x: contenido){
            if (id.equals(x.id))
                respuesta = true;
        }
        return respuesta;
    }
    
    public void insertar(String id, Type tipo, int dir, String clas, ArrayList<Type> args){
        Symbol sim = new Symbol (id, dir, tipo, clas, args);
        contenido.add(sim);
    }
    
    public void insertar(Symbol simbolo){
        contenido.add(simbolo);
    }
    
    //Para imprimir la tabla de simbolos
    void printTS(){
        System.out.println("Tabla de símbolos " + this.name);
        int i=0;
        for(Symbol s : contenido){
            System.out.println(Integer.toString(i) +" " + s);
            i++;
        }    
    }
    
}
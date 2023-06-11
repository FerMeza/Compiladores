package compilador;
import java.util.ArrayList;

public class TablaTipos{
    public ArrayList<Type> contenido;
    private int contador;
    public String name;

    public TablaTipos(String name)
    {
        /*Desde un principio se agregan los tipos primitivos del lenguaje
        no es la mejor forma de hacerlo muy seguramente...*/
        contenido = new ArrayList<Type>();
        contenido.add(Parser.TVOID);
        contenido.add(Parser.TINT);
        contenido.add(Parser.TFLOAT);
        contenido.add(Parser.TCHAR);
        contenido.add(Parser.TDOUBLE);
        /*Contador es para que incremente la cuenta de manera automatica*/
        contador = 5;
        this.name = name;
    }
        
    public Type buscar(Type tipo){   
        for (Type T: contenido)
        {
            if (tipo.id == T.id)
                return T;
        }
        return null;
    }

    public Type insertar(Type tipo){
        tipo.id = contador;
        contenido.add(tipo);
        contador++;
        return tipo;
    }
    
    public int getTam(Type tipo){
        for (Type t: contenido)
        {
            if (t.id == tipo.id)
                return t.tam;
        }
        return 0;
    }
    
    void printTT(){
        System.out.println("Tabla de tipos " + this.name);
        for(Type t : contenido){
            System.out.println(t);
        }     
    }
    
}
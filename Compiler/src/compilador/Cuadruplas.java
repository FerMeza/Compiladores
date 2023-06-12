/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author fernando
 */
public class Cuadruplas {
    String op;
    String arg1;
    String arg2;
    String res;
    static int contadorB = 0;
    static int contadorS = 0;
    
    public Cuadruplas(String op, String arg1, String arg2, String res)
    {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.res = res;
    }
    
    public Cuadruplas(String tipo)
    {
        if (tipo == "b" || tipo == "B")
        {
            this.arg1 = "";
            this.arg2 = "";
            this.op = "label";
            this.res = "BS" + Integer.toString(contadorB);
            contadorB++;
        }
        if (tipo == "s" || tipo == "S")
        {
            this.arg1 = "";
            this.arg2 = "";
            this.op = "label";
            this.res = "SS" + Integer.toString(contadorS);
            contadorS++;
        }
    }
    
    public String toString()
    {
        if (op.equals("label"))
        {
            return res+":";
        }
        return null;
    }
}

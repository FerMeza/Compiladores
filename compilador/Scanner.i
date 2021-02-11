package compilador; 

%%
%line 
%column
%unicode
%type Token

%{
		/*Constantes para identificar los tokens*/
    public static final int NUMEROS = 100;
    public static final int DECEX = 101;
    public static final int CADENAS =102;
    public static final int TRUE = 103;
    public static final int FALSE  = 104;
    public static final int INT = 105;
    public static final int FLOAT = 106;
    public static final int CHAR = 107;
    public static final int DOUBLE = 108;
    public static final int VOID = 109;
    public static final int IF = 110;
    public static final int WHILE = 111;
    public static final int DO = 112;
    public static final int BREAK = 113;
    public static final int SWITCH = 114;
    public static final int CASE= 115;
    public static final int FUNC = 116;
    public static final int DEFAULT = 117;
    public static final int PAR_L = 118;
    public static final int PAR_R = 119; 
    public static final int COR_L = 120;
    public static final int COR_R = 121;
    public static final int LLA_L = 122;
    public static final int LLA_R = 123;
    public static final int MAS = 124;
    public static final int MENOS = 125;
    public static final int ASTERISCO = 126;
    public static final int DIVISION = 127;
    public static final int MODULO = 128;
    public static final int NEGACION = 129;
    public static final int COMPARACION = 130;
    public static final int DIFERENTEDE = 131;
    public static final int MENOR = 132;
    public static final int MENORIGUAL = 133;
    public static final int MAYOR = 134;
    public static final int MAYORIGUAL = 135;
    public static final int OR = 136;
    public static final int AND = 137;
    public static final int PUNCOMA = 138;
    public static final int COMA = 139;
    public static final int DOSPUNTOS = 140;
    public static final int ID = 141;
    public static final int RETURN = 142;
    public static final int ASIGNACION = 143;
    public static final int ELSE = 144;
    public static final int PRINT = 145;
    public static final int SCAN = 146;

    public Token nextToken(String lex, int num){
        return new Token(num, lex, yyline, yycolumn);
    }

    public Token nextToken(String lex, int num, int type){
        return new Token(num, lex, type, yyline, yycolumn);
    }
%}

/*********************************************Detecta el final del archivo  %eofval{%eofval}******************************************/


%eofval{
    return nextToken("", 0);
%eofval}

/**********************			Creacion de expresiones regulares.
								Aguilar Paulina, Aviles Bryan, Meza Fernando: 15 Noviembre 2020  *************z***************/

digito = [0-9]
numeros = {digito}+(_{digito}+)*               
letras = [a-zA-Z]
ex=[Ee]([+-])?{numeros}+
dec= {numeros}*"."{numeros}+|{numeros}+"."{numeros}*
decex=({dec}{ex}?|{numeros}{ex})		
espacios = [\n\t ]+
cadenas=  ( (\") ( [^(\")\n]  | \\(\") )* (\") |  (\')([^(\')])*(\') ) 
true = true
false = false
int = int
float = float
char = char
double = double
void = void
if = if
while = while
do = do
break = break
switch = switch
case = case
func = func
default = default
par_L = "("
par_R = ")"
cor_L = "[" 
cor_R = "]"
lla_L = "{"
lla_R = "}"
mas  = "+"
menos  = "-"
asterisco = "*"
division = "/"
modulo = "%"
negacion = "!"
comparacion = "=="
diferentede = "!="
menor = "<"
menorigual = "<="
mayor = ">"
mayorigual = ">="
or = "||"
and = "&&"
puncoma = ";"
coma = ","
dospuntos = ":"
comenLinea = --[^\n]*\n
comenMulti =  ("<*")([^"*"]|"*"[^>])*("*>") 
id = ({letras}|_) ({digito}| {letras}|_) *
return = return
asignacion = "="
else = else
print = print
scan = scan
errores = [^]

/**********************Accion a realizar (retornar un numero) al encontrar un token, Hecho por 
											Aguilar Paulina, Aviles Bryan y Meza Ortega Fernando 15 Noviembre 2020 ***************************/


%%

{numeros} {return nextToken(yytext(), NUMEROS, 0);} /*Valor lex, tipo(numero), subtipo(entero)*/
{decex} {return nextToken(yytext(), DECEX, 1);} /*Valor lex, tipo(numero), subtipo(double)*/
{cadenas} {return nextToken(yytext(), CADENAS);} /*Valor lex, tipo(cadena)*/
{true} {return nextToken(yytext(), TRUE);} /*Valor lex, tipo(true)*/
{false} {return nextToken(yytext(), FALSE);} /*Valor lex, tipo(false)*/
{int} {return nextToken(yytext(), INT);} /*Valor lex, tipo(int)*/
{float} {return nextToken(yytext(), FLOAT);} /*Valor lex, tipo(float)*/
{char} {return nextToken(yytext(), CHAR);} /*Valor lex, tipo(char)*/
{double} {return nextToken(yytext(), DOUBLE);} /*Valor lex, tipo(void)*/
{void} {return nextToken(yytext(), VOID);} /*Valor lex, tipo(void)*/
{if} {return nextToken(yytext(), IF);} /*Valor lex, tipo(if)*/
{while} {return nextToken(yytext(), WHILE);} /*Valor lex, tipo(while)*/
{do} {return nextToken(yytext(), DO);} /*Valor lex, tipo(do)*/
{break} {return nextToken(yytext(), BREAK);} /*Valor lex, tipo(break)*/
{switch} {return nextToken(yytext(), SWITCH);} /*Valor lex, tipo(switch)*/
{case} {return nextToken(yytext(), CASE);} /*Valor lex, tipo(case)*/
{func} {return nextToken(yytext(), FUNC);} /*Valor lex, tipo(func)*/
{default} {return nextToken(yytext(), DEFAULT);} /*Valor lex, tipo(default)*/
{par_L} {return nextToken(yytext(), PAR_L);} /*Valor lex, tipo("(")*/
{par_R} {return nextToken(yytext(), PAR_R);} /*Valor lex, tipo(")")*/
{cor_L} {return nextToken(yytext(), COR_L);} /*Valor lex, tipo([)*/
{cor_R} {return nextToken(yytext(), COR_R);} /*Valor lex, tipo(])*/
{lla_L} {return nextToken(yytext(), LLA_L);} /*Valor lex, tipo({)*/
{lla_R} {return nextToken(yytext(), LLA_R);} /*Valor lex, tipo(})*/
{mas} {return nextToken(yytext(), MAS);} /*Valor lex, tipo(mas)*/
{menos} {return nextToken(yytext(), MENOS);} /*Valor lex, tipo(menos)*/
{asterisco} {return nextToken(yytext(), ASTERISCO);} /*Valor lex, tipo(multip)*/
{division} {return nextToken(yytext(), DIVISION);} /*Valor lex, tipo(division)*/
{modulo} {return nextToken(yytext(), MODULO);} /*Valor lex, tipo(modulo)*/
{negacion} {return nextToken(yytext(), NEGACION);} /*Valor lex, tipo(negado)*/
{comparacion} {return nextToken(yytext(), COMPARACION);} /*Valor lex, tipo(==)*/
{diferentede} {return nextToken(yytext(), DIFERENTEDE);} /*Valor lex, tipo(diferente)*/
{menor} {return nextToken(yytext(), MENOR);} /*Valor lex, tipo(menor)*/
{menorigual} {return nextToken(yytext(), MENORIGUAL);} /*Valor lex, tipo(menorigual)*/
{mayor} {return nextToken(yytext(), MAYOR);} /*Valor lex, tipo(mayor)*/
{mayorigual} {return nextToken(yytext(), MAYORIGUAL);} /*Valor lex, tipo(mayorigual)*/
{or} {return nextToken(yytext(), OR);} /*Valor lex, tipo(||)*/
{and} {return nextToken(yytext(), AND);} /*Valor lex, tipo(&&)*/
{puncoma} {return nextToken(yytext(), PUNCOMA);} /*Valor lex, tipo(puntoycoma)*/
{coma} {return nextToken(yytext(), COMA);} /*Valor lex, tipo(coma)*/
{dospuntos} {return nextToken(yytext(), DOSPUNTOS);} /*Valor lex, tipo(dospuntos)*/
{return} {return nextToken(yytext(),RETURN);} /*Valor lex, tipo(RETURN)*/
{asignacion} {return nextToken(yytext(), ASIGNACION);} /*Valor lex, tipo(asignacion)*/
{else} {return nextToken(yytext(), ELSE);}  /*Valor lex, tipo(else)*/
{print} {return nextToken(yytext(), PRINT);} /*Valor lex, tipo(print)*/
{scan} {return nextToken(yytext(), SCAN);} /*Valor lex, tipo(SCAN)*/
{espacios} {} /*No hace nada, persona lectora de comentarios*/	
{comenMulti} {}
{comenLinea} {}	
{id} {return nextToken(yytext(), ID);} /*Valor lex, tipo(id)*/
{errores}   {System.out.println("Error lexico en Token:" + yytext() + " no reconocido en columna " + yycolumn + " y linea " + yyline);}
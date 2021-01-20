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
    public static final int ADD_OP1 = 124;
    public static final int ADD_OP2 = 125;
    public static final int MUL_OP1 = 126;
    public static final int MUL_OP2 = 127;
    public static final int MUL_OP3 = 128;
    public static final int UNARY_OP1 = 129;
    public static final int REL_OP_1 = 130;
    public static final int REL_OP_2 = 131;
    public static final int REL_OP_3 = 132;
    public static final int REL_OP_4 = 133;
    public static final int REL_OP_5 = 134;
    public static final int REL_OP_6 = 135;
    public static final int OR = 136;
    public static final int AND = 137;
    public static final int PUNCOMA = 138;
    public static final int COMA = 139;
    public static final int DOSPUNTOS = 140;
    public static final int ID = 141;
    public static final int RETURN = 142;

    public Token nextToken(String lex, int num){
        return new Token(num, lex);
    }

    public Token nextToken(String lex, int num, int type){
        return new Token(num, lex, type);
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
add_op1  = "+"
add_op2  = "-"
mul_op1 = "*"
mul_op2 = "/"
mul_op3 = "%"
unary_op1 = "!"
rel_op_1 = "=="
rel_op_2 = "!="
rel_op_3 = "<"
rel_op_4 = "<="
rel_op_5 = ">"
rel_op_6 = ">="
or = "||"
and = "&&"
puncoma = ";"
coma = ","
dospuntos = ":"
comenLinea = --[^\n]*\n
comenMulti =  ("<*")([^"*"]|"*"[^>])*("*>") 
id = ({letras}|_) ({digito}| {letras}|_) *
return = return

/**********************Accion a realizar (retornar un numero) al encontrar un token, Hecho por 
											Aguilar Paulina, Aviles Bryan y Meza Ortega Fernando 15 Noviembre 2020 ***************************/


%%

espacios {} /*No hace nada, persona lectora de comentarios*/	
comentmulti {}
comentario {}	
numeros {return nextToken(yytext(), NUMEROS, 0);} /*Valor lex, tipo(numero), subtipo(entero)*/
decex {return nextToken(yytext(), DECEX, 1);} /*Valor lex, tipo(numero), subtipo(double)*/
cadenas {return nextToken(yytext(), CADENAS);} /*Valor lex, tipo(cadena)*/
true {return nextToken("", TRUE);} /*Valor lex, tipo(true)*/
false {return nextToken("", FALSE);} /*Valor lex, tipo(false)*/
int {return nextToken("", INT);} /*Valor lex, tipo(int)*/
float {return nextToken("", FLOAT);} /*Valor lex, tipo(float)*/
char {return nextToken("", CHAR);} /*Valor lex, tipo(char)*/
double {return nextToken("", DOUBLE);} /*Valor lex, tipo(void)*/
void {return nextToken("", VOID);} /*Valor lex, tipo(void)*/
if {return nextToken("", IF);} /*Valor lex, tipo(if)*/
while {return nextToken("", WHILE);} /*Valor lex, tipo(while)*/
do {return nextToken("", DO);} /*Valor lex, tipo(do)*/
break {return nextToken("", BREAK);} /*Valor lex, tipo(break)*/
switch {return nextToken("", SWITCH);} /*Valor lex, tipo(switch)*/
case {return nextToken("", CASE);} /*Valor lex, tipo(case)*/
func {return nextToken("", FUNC);} /*Valor lex, tipo(func)*/
default {return nextToken("", DEFAULT);} /*Valor lex, tipo(default)*/

par_L {return nextToken("", PAR_L);} /*Valor lex, tipo("(")*/
par_R {return nextToken("", PAR_R);} /*Valor lex, tipo(")")*/
cor_L {return nextToken("", COR_L);} /*Valor lex, tipo([)*/
cor_R {return nextToken("", COR_R);} /*Valor lex, tipo(])*/
lla_L {return nextToken("", LLA_L);} /*Valor lex, tipo({)*/
lla_R {return nextToken("", LLA_R);} /*Valor lex, tipo(})*/
add_op1 {return nextToken("", ADD_OP1);} /*Valor lex, tipo(mas)*/
add_op2 {return nextToken("", ADD_OP2);} /*Valor lex, tipo(menos)*/

mul_op1 {return nextToken("", MUL_OP1);} /*Valor lex, tipo(multip)*/
mul_op2 {return nextToken("", MUL_OP2);} /*Valor lex, tipo(division)*/
mul_op3 {return nextToken("", MUL_OP3);} /*Valor lex, tipo(modulo)*/
unary_op1 {return nextToken("", UNARY_OP1);} /*Valor lex, tipo(negado)*/
rel_op_1 {return nextToken("", REL_OP_1);} /*Valor lex, tipo(igual)*/
rel_op_2 {return nextToken("", REL_OP_2);} /*Valor lex, tipo(diferente)*/
rel_op_3 {return nextToken("", REL_OP_3);} /*Valor lex, tipo(menor)*/
rel_op_4 {return nextToken("", REL_OP_4);} /*Valor lex, tipo(menorigual)*/
rel_op_5 {return nextToken("", REL_OP_5);} /*Valor lex, tipo(mayor)*/
rel_op_6 {return nextToken("", REL_OP_6);} /*Valor lex, tipo(mayorigual)*/
or {return nextToken("", OR);} /*Valor lex, tipo(||)*/
and {return nextToken("", AND);} /*Valor lex, tipo(&&)*/
puncoma {return nextToken("", PUNCOMA);} /*Valor lex, tipo(puntoycoma)*/
coma {return nextToken("", COMA);} /*Valor lex, tipo(coma)*/
dospuntos {return nextToken("", DOSPUNTOS);} /*Valor lex, tipo(dospuntos)*/
id {return nextToken(yytext(), ID);} /*Valor lex, tipo(id)*/
return {return nextToken("",RETURN);} /*Valor lex, tipo(RETURN)*/
.   {System.out.println("Error");}
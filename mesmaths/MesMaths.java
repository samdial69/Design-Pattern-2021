package mesmaths;

import mesmaths.geometrie.base.Vecteur;
import complexes.Complexe;

/**
 * Quelques opérations mathématiques générales utiles
 * 
 * */
public class MesMaths
{

/**
 * 
 * @return une valeur aléatoire dans l'intervalle [xMin, xMax]
 * */
public static double créationAléatoire(double xMin, double xMax)
{
double dX = xMax - xMin;

return xMin + dX*Math.random();
}
/**
 * @param a : borne inférieure de l'intervalle [a,b]
 * @param b : borne supérieure de l'intervalle [a,b]
 * @return  la valeur de l'expression: x est dans [a,b]
 * 
 * */
public static boolean appartient(double x, double a, double b)
{
return (a <= x) && (x <= b);
}

/**
 * @param a : le coefficient de x^2
 * @param b : le coefficient de x
 * @param c : le coefficient du terme constant de l'équation
 * résout a * x^2 + b * x + c = 0 dans R
 * 
 * suppose a non nul
 * 
 * @return un tableau t contenant  soit 2 solutions réelles soit 0 solution
 * 
 * le tableau t renvoyé contient donc soit 2 double soit 0 élément
 * 
 * Les solutions sont  classées par ordre croissant telles que t[0] < = t[1]
 * 
 * */
public static double[] résoudre( double a, double b, double c)  // résout ax^2+bx+c=0. en sortie t[0] <= t[1]
{
double []t;
double delta;

delta = b*b-4*a*c;

if (delta >=0)
    {
    double racine, denom;
    racine = Math.sqrt(delta);
    denom = 2*a;
    t = new double[2];
    t[0] = (-b-racine)/denom;
    t[1] = (-b+racine)/denom;
    }
else
    t = new double[0];

return t;
}

/**
 * résout a * x^2 + b * x + c = 0 dans C
 * 
 * @param a : le coefficient de x^2
 * @param b : le coefficient de x
 * @param c : le coefficient du terme constant de l'équation
 * 
 * suppose a non nul
 * 
 * @return un tableau t contenant soit 2 solutions réelles soit 2 solutions complexes conjuguées
 * 
 * le tableau t renvoyé contient donc soit 2 Double soit 2 Complexe placées dans t[0] et t[1]
 * 
 * si les solutions sont réelles, elles sont classées par ordre croissant telles que t[0] < = t[1]
 * 
 * */
public static Object [] résoutEquationDegré2(double a, double b, double c)
{
if (a == 0) throw new ArithmeticException("l'équation à résoudre n'est pas du 2ème degré");

Object [] t = new Object[2];

double delta, a2;


delta = b*b - 4*a*c;
a2 = 2*a; 

if (delta > 0) /* les solutions sont réelles */
    {
    double racineDelta = Math.sqrt(delta);
    double x1, x2;
    
    x1 = (-b - racineDelta)/a2;
    x2 = (-b + racineDelta)/a2;
    t[0] = new Double(x1);
    t[1] = new Double(x2);
    }
else
    {
    double racineDelta1 = Math.sqrt(-delta);
    t[0] = new Complexe(-b/a2,-racineDelta1/a2);
    t[1] = new Complexe(-b/a2, racineDelta1/a2);
    }
return t;
}


/**
 * 
 * résout dans R, par la méthode de Cardan, l'équation de degré 3 réduite : x^3 + p * x + q = 0
 * 
 * @param p : le coefficient de x
 * @param q : le coefficient du terme constant
 *
 *  @return un tableau t contenant 1 ou 3 solutions 
 * */
public static double[] résoutEquationDegré3Réduite(double p, double q)
{
double [] t;

Object [] XY = MesMaths.résoutEquationDegré2(1, q, -p*p*p/27);

if (XY[0] instanceof Double) /* les solutions sont réelles : il y a donc une solution à l'équation */
    {
    t = new double[1];
    
    double X,Y, a, b;
    
    X = ((Double)(XY[0])).doubleValue();
    Y = ((Double)(XY[1])).doubleValue();
    
    a = Math.pow(X, 1./3);
    b = Math.pow(Y, 1./3);
    
    t[0] = a+b; // l'unique solution de l'équation
    }
else // les solutions sont complexes conjuguées
    {
    t = new double[3];
    
    Complexe [] racinesCubiquesX;
    Complexe [] racinesCubiquesY;
    
    racinesCubiquesX = ((Complexe)(XY[0])).racines(3);
    racinesCubiquesY = ((Complexe)(XY[1])).racines(3);
    
    Complexe x0,x1, x2;
    
    x0 = racinesCubiquesX[0].somme(racinesCubiquesY[0]);    // on sait que x0 est réel
    
    x1 = racinesCubiquesX[1].somme(racinesCubiquesY[2]);    // on sait que x1 est réel
    
    x2 = racinesCubiquesX[2].somme(racinesCubiquesY[1]);    // on sait que x2 est réel
    
    t[0] = x0.getX();
    t[1] = x1.getX();
    t[2] = x2.getX();
    
    }

return t;
}

/**
 * résout dans R, par la méthode de Cardan, l'équation a3*t^3 + a2*t^2 + a1*t + a0 = 0
 * 
 * @param a0 : coefficient du terme constant
 * @param a1 : coefficient de x
 * @param a2 : coefficient de x^2
 * @param a3 : coefficient de x^3
 * 
 * on suppose a3 non nul
 * 
 * @return un tableau t contenant 1 ou 3 solutions
 * 
 * */
public static double [] résoutEquationDegré3(double a0, double a1, double a2, double a3)
{
if (a3 == 0) throw new ArithmeticException("l'équation à résoudre n'est pas du 3ème degré");

double A, B, C;

A = a2/a3; B = a1/a3; C = a0/a3;

// on a réduit l'équation à : t^3 + A * t^2 + B * t + C = 0

double alfa = -A/3;  // changnement de variable : t = x + alfa

double u, p, q;

u = A*A/3; // u = A^2/3 
p = B-u;
q = 2*A*u/9 - A*B/3 + C;

// x est solution de x^3+ p * x + q = 0

double [] x = MesMaths.résoutEquationDegré3Réduite(p, q);

double [] t = new double[x.length];

int i;
for ( i = 0;  i < t.length; ++i)
    t[i] = x[i] + alfa;

return t;
}

/**
 * 
 * Résout dans R, l'équation a*x + b == 0
 * 
 * */
public static double résoutEquationDegré1(double a, double b)
{
if (a == 0) throw new ArithmeticException("l'équation à résoudre n'est pas du 1er degré");

return -b/a;

}


}

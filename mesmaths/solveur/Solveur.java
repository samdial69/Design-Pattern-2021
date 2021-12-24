package mesmaths.solveur;
import java.util.*;
/**
 * dédiée à la résolution des équations de la forme f(x)=0 avec x réel et f à valeurs réelles
 * 
 * f est supposée être une instance d'une classe dérivée de Courbe
 * 
 * On suppose que f est une fonction continue, mais c'est mieux si elle dérivable (c'est-à-dire si elle fournit un calcul de dérivée. cf. classe Courbe)
 * */
public class Solveur
{

private static final double PRECISION = 1.0e-6; // précision des résultats

/**
 * résout l'équation f(x) = 0 sur l'intervalle fini [a,b]
 * 
 * On suppose que f(a)*f(b) < 0 et que l'équation admet une unique solution sur [a,b]
 * 
 * @param f : telle que f(x) = 0 est l'équation à résoudre
 * @param a : borne inférieure de l'intervalle de recherche
 * @param b : borne supérieure de l'intervalle de recherche
 * 
 * @return la valeur de x telle que f(x) = 0 et x est dans [a,b]
 * 
 * 
 * */
public static double résout( Courbe f, double a, double b)
{
double fa, f1a, fb, f1b, u, fu, x, fx, f1x, v;

fa = f.évalue(a);
fb = f.évalue(b);

if (fa*fb > 0) throw new IllegalArgumentException("l'équation à résoudre n'a peut-être pas de solution sur [ " + a + ", " + b + "]");

u = a; fu = fa; v = b;
f1a = f.évalueDérivée(a); 
f1b = f.évalueDérivée(b);

if (Math.abs(f1a) < Math.abs(f1b))              // on part toujours de la borne de plus grande pente
    { x = b; fx = fb; f1x = f1b;}
else
    {x = a; fx = fa; f1x = f1a;}


// la boucle gère les suites Un, f(Un), Xn, f(Xn), f'(Xn) et Vn
while (true)
    {
    double r = fx/f1x;
    x -= r;                                     // itération Newton
    if (Math.abs(r)<PRECISION) return x;
    
    if (!(u <= x && x <= v)) x = 0.5*(u+v);     // dichotomie si échec de Newton
        
    fx = f.évalue(x);
    f1x = f.évalueDérivée(x);
    
    if (fu*fx<0)
        v = x;
    else
        {u=x; fu = fx;}
    
    }    
}

}

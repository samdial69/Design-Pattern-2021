package mesmaths.solveur;


/**
 * fonction de la forme : x |---> f(x) dont on va chercher les zéros, c'est-à-dire les valeurs de x pour les quelles on a f(x) = 0
 * 
 * On suppose que x est réel et que f(x) est réel
 * 
 * Les équations spécifiques à résoudre sont définies par les classes dérivées
 * 
 * 
 * 
 * */
public abstract class Courbe
{

private static final double EPSILON = 0.001;        // valeur petite définie arbitrairement

/**
 * @return la valeur de f(x) 
 * 
 * 
 * */
public abstract double évalue( double x);

/**
 * @return la valeur de f'(x) 
 * 
 * Les classes dérivées sont invitées (mais ce n'est pas obligatoire) à fournir un calcul plus précis 
 * 
 * car plus les calculs sont précis, plus l'algo du solveur est rapide
 * 
 * A ce niveau, on ne peut faire qu'un calcul approché.
 * 
 * */
public  double évalueDérivée( double x)
{
double x1 = x + EPSILON;

return (évalue(x1)-évalue(x))/EPSILON;  // approximation du calcul de la dérivée
}


}

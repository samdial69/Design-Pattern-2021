package mesmaths.geometrie.formes;

import java.util.Arrays;

import mesmaths.geometrie.ExceptionGeometrique;
import mesmaths.geometrie.base.Vecteur;


/**
 * Représente un arc de courbe dans le plan. 
 * 
 * de la forme t |---> p(t)
 * 
 * 
 * Le paramètre t (ou abscisse curviligne) décrit l'intervalle [0,1]
 * 
 * p(t) est dans RxR
 * 
 * */
public abstract class ArcCourbe
{

public ArcCourbe()
{
super();
}

/**
 * Calcule, s'il existe, l'abscisse curviligne du projeté orthogonal de p sur this
 * @return l'abscisse curviligne t du projeté orthogonal de p sur l'arc this. t est tel que 0 <= t <= 1
 * @throws une ExceptionGeometrique si le projeté orthogonal n'est pas défini
 * 
 * */
public abstract double projetéOrthogonal(Vecteur p) throws ExceptionGeometrique;

/**
 * calcule p(t), c'est-à-dire le point correspondant à t sur l'arc this
 * 
 * t doit être tel que 0 <= t <= 1
 * 
 * */
public abstract Vecteur évalue(double t);

/**
 * Calcule un vecteur tangent (pas forcément unitaire) dans le sens positif en p(t)
 * 
 * t doit être tel que 0 <= t <= 1
 * 
 * */
public abstract Vecteur vecteurTangent(double t);

/**
 * Calcule une base orthonormée directe en p(t) telle que :
 * 
 * base[0] est un vecteur unitaire tangent à la courbe en p(t) dans le sens croissant
 * base[1] est un vecteur orthogonal à la courbe en p(t) obtenu par rotation de +PI/2 par rapport à base[0]
 * 
 * t doit être tel que 0 <= t <= 1
 * 
 * */
public abstract Vecteur[] base(double t);


/**
 * calcule les éventuels points d'intersection avec la droite définie par {A,n}
 * où A est un point de passage et n un vecteur normal à la droite
 * 
 * @return le tableau trié contenant les abscisses curvilignes des points d'intersection entre this et {A,n}
 * 
 * toutes les valeurs t du tableau retourné sont  telles que 0 <= t <= 1
 * 
 * */
public abstract double[] intersectionDroite(Vecteur A, Vecteur n);

public static double [] filtre(double t[], double borneInf, double borneSup)
{
int l = t.length;
double r [] = new double[l];
int i, effectif;
for (i = 0, effectif = 0; i < l; ++i)
    if ( borneInf <=  t[i] && t[i] <= borneSup) r[effectif++] = t[i];

return Arrays.copyOf(r, effectif);
}

}
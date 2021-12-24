package mesmaths.geometrie.formes;

import java.util.Arrays;

import mesmaths.geometrie.ExceptionGeometrique;
import mesmaths.geometrie.base.Vecteur;


/**
 * Repr�sente un arc de courbe dans le plan. 
 * 
 * de la forme t |---> p(t)
 * 
 * 
 * Le param�tre t (ou abscisse curviligne) d�crit l'intervalle [0,1]
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
 * Calcule, s'il existe, l'abscisse curviligne du projet� orthogonal de p sur this
 * @return l'abscisse curviligne t du projet� orthogonal de p sur l'arc this. t est tel que 0 <= t <= 1
 * @throws une ExceptionGeometrique si le projet� orthogonal n'est pas d�fini
 * 
 * */
public abstract double projet�Orthogonal(Vecteur p) throws ExceptionGeometrique;

/**
 * calcule p(t), c'est-�-dire le point correspondant � t sur l'arc this
 * 
 * t doit �tre tel que 0 <= t <= 1
 * 
 * */
public abstract Vecteur �value(double t);

/**
 * Calcule un vecteur tangent (pas forc�ment unitaire) dans le sens positif en p(t)
 * 
 * t doit �tre tel que 0 <= t <= 1
 * 
 * */
public abstract Vecteur vecteurTangent(double t);

/**
 * Calcule une base orthonorm�e directe en p(t) telle que :
 * 
 * base[0] est un vecteur unitaire tangent � la courbe en p(t) dans le sens croissant
 * base[1] est un vecteur orthogonal � la courbe en p(t) obtenu par rotation de +PI/2 par rapport � base[0]
 * 
 * t doit �tre tel que 0 <= t <= 1
 * 
 * */
public abstract Vecteur[] base(double t);


/**
 * calcule les �ventuels points d'intersection avec la droite d�finie par {A,n}
 * o� A est un point de passage et n un vecteur normal � la droite
 * 
 * @return le tableau tri� contenant les abscisses curvilignes des points d'intersection entre this et {A,n}
 * 
 * toutes les valeurs t du tableau retourn� sont  telles que 0 <= t <= 1
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
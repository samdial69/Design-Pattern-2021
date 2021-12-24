package mesmaths.geometrie.formes;

import java.util.Arrays;

import mesmaths.MesMaths;
import mesmaths.geometrie.ExceptionGeometrique;
import mesmaths.geometrie.base.Vecteur;


/**
 * Arc de parabole d�fini comme une courbe de B�zier de degr� 2 : 
 * 
 *  t |--->p(t) = Po*(1-t)^2 + 2*P1*(1-t)*t + P2*t^2
 *  
 *  avec 0 <= t <= 1
 * 
 * */
public class BezierQuadratic extends ArcCourbe 
{
Vecteur P[];        // � 3 �l�ments exactement
Vecteur D[];        // � 2 �l�ments exactement : contient la d�finition de la d�riv�e d'ordre 1



public BezierQuadratic(Vecteur[] p)
{
if (p.length != 3) throw new ExceptionGeometrique(Arrays.toString(p) + " n'est pas un arc de parabole");
this.P = p;

D = new Vecteur[2]; 

// le tableau D contient la d�finition de la d�riv�e � un facteur pr�s. Il faut multiplier par 2 pour obtenir la d�riv�e

D[0] = Vecteur.difference(P[1], P[0]); //.produit(2);
D[1] = Vecteur.difference(P[2], P[1]); //.produit(2);

}



/**
 * @return i tel que t[i] <= t[j] pour tout j = 0...t.length-1
 * @return -1 si t est vide
 * 
 * */
public static int indiceDuMin(double t[])
{
int i, iMinimum;
double minimum;

for (i = 0, minimum = Double.POSITIVE_INFINITY, iMinimum = -1; i < t.length; ++i)
    if (t[i] < minimum) {minimum = t[i]; iMinimum = i; }


return iMinimum;
}
@Override
public double projet�Orthogonal(Vecteur p) throws ExceptionGeometrique
{
try
    {
    Vecteur P0_P, P1_P, P2_P;
    
    P0_P = Vecteur.difference(P[0], p);
    P1_P = Vecteur.difference(P[1], p);
    P2_P = Vecteur.difference(P[2], p);
    
    double q0, q1, q2, q3;
    
    q0 = Vecteur.produitScalaire(D[0], P0_P);
    
    q1 = ( 2*Vecteur.produitScalaire(D[0], P1_P) + Vecteur.produitScalaire(D[1], P0_P) )/3;
    
    q2 = ( Vecteur.produitScalaire(D[0], P2_P) + 2*Vecteur.produitScalaire(D[1], P1_P) )/3;
    
    q3 = Vecteur.produitScalaire(D[1], P2_P);
    
    double a0, a1, a2, a3;
    
    a0 = q0;
    a1 = 3*(q1-q0);
    a2 = 3*(q2-2*q1+q0);
    a3 = q3-3*q2+3*q1-q0;
    
    double x[] = MesMaths.r�soutEquationDegr�3(a0, a1, a2, a3);
    
    x = filtre(x, 0, 1);
    
    double distancesCarr�es[] = new double[x.length];
    
    int i;
    for( i = 0; i <x.length; ++i) distancesCarr�es[i] = Vecteur.difference(this.�value(x[i]), p).normeCarr�e();
    
    int iMin = indiceDuMin(distancesCarr�es);
    
    return x[iMin];
    }
catch (Exception e)
    {
    throw new ExceptionGeometrique("le projet� orthogonal n'existe pas");
    }
}

@Override
public Vecteur �value(double t)
{
double tbar = 1-t;

Vecteur T[] = new Vecteur[2];

T[0] = P[0].produit(tbar).somme(P[1].produit(t)); 
T[1] = P[1].produit(tbar).somme(P[2].produit(t));

return T[0].produit(tbar).somme(T[1].produit(t));
}

public Vecteur vecteurTangent(double t)
{
double tbar = 1-t;

return D[0].produit(tbar).somme(D[1].produit(t));
}

@Override
public Vecteur[] base(double t)
{
return this.vecteurTangent(t).base();
}

/**
 * calcule les �ventuels points d'intersection avec la droite d�finie par {A,n}
 * o� A est un point de passage et n un vecteur normal � la droite
 * 
 * @return le tableau tri� contenant les abscisses curvilignes des points d'intersection entre this et {A,n}
 * 
 * */
@Override
public double [] intersectionDroite(final Vecteur A, final Vecteur n)
{
Vecteur P0_A, P1_A, P2_A;

P0_A = Vecteur.difference(P[0], A);
P1_A = Vecteur.difference(P[1], A);
P2_A = Vecteur.difference(P[2], A);

double q0, q1, q2;

q0 = Vecteur.produitScalaire(P0_A, n);
q1 = Vecteur.produitScalaire(P1_A, n);
q2 = Vecteur.produitScalaire(P2_A, n);

double a0, a1, a2;

a0 = q0;
a1= 2*(q1-q0);
a2 = q2-2*q1+q0;

double t[] = MesMaths.r�soudre(a2, a1, a0);

t = filtre(t, 0, 1);

return t;

}
}

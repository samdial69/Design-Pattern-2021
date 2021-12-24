package mesmaths.cinematique;

import mesmaths.geometrie.ExceptionGeometrique;
import mesmaths.geometrie.base.Vecteur;
import mesmaths.geometrie.formes.ArcCourbe;

/**
 * ArcDansContour au sens large : rectiligne ou curviligne
 * Repr�sente une partie orient�e d'un contour ferm�.
 * Un arc dispose d'un sens de parcours indiqu� par la base. (cf. m�thode base())
 * 
 * A revoir
 * 
 * 
 * */
public abstract class ArcDansContour extends ArcCourbe
{
public ArcCourbe arcCourbe;

public ArcDansContour(ArcCourbe arcCourbe)
{
super();
this.arcCourbe = arcCourbe;
}

/**
 * Calcule l'abscisse curviligne t du projet� orthogonal de p sur this
 * l'abscisse curviligne t d�crit l'intervalle [0,1].
 * t == 0 <---> d�but de l' arc et t == 1 <---> fin de l' arc 
 * @throws une ExceptionGeometrique pour indiquer que le projet� orthogonal de p n'est pas d�fini
 * 
 * */
@Override
public double projet�Orthogonal(Vecteur p) throws ExceptionGeometrique
{
return this.arcCourbe.projet�Orthogonal(p);
}

/**
 * Calcule, sur l'arc this, le point de passage  correspondant � l'abscisse curviligne t.
 * 
 * */
@Override
public Vecteur �value(double t)
{
return this.arcCourbe.�value(t);
}


/**
 * Calcule un vecteur tangent (pas forc�ment unitaire) dans le sens positif en p(t)
 * 
 * t doit �tre tel que 0 <= t <= 1
 * 
 * */
@Override
public Vecteur vecteurTangent(double t)
{
return this.arcCourbe.vecteurTangent(t);
}


/**
 * Calcule une base orthonormale de 2 vecteurs {i,j} au point d'abscisse curviligne t.
 * On doit avoir 0 <= t <= 1. t == 0 <---> d�but de l' arc et t == 1 <---> fin de l' arc 
 * Cette base est telle que i soit un vecteur tangent � l'arc et j soit un vecteur rentrant dans le contour.
 * j indique o� se trouve l'int�rieur du contour ferm�. 
 * |i| == |j| == 1 et i.j == 0
 * @return un tableau t de 2 �l�ments tel que t[0] = i et tel que t[1] = j
 * */
@Override
public Vecteur[] base(double t)
{
return this.arcCourbe.base(t);
}

@Override
public double[] intersectionDroite(Vecteur A, Vecteur n)
{
return this.arcCourbe.intersectionDroite(A, n);
}



/**
 * fait la m�me chose que la 2�me m�thode oppos�() 
 * N est un point quelconque de this. 
 * u est le vecteur unitaire sur une droite D passant par N et perpendiculaire au contour. u est orient� vers l'ext�rieur.
 * D = {N,u}
 * On suppose qu'il existe un point p sur D � l'int�rieur du contour tel que l'abscisse de p sur {N,u} soit d (donc d < 0)
 * 
 * */
public abstract Vecteur oppos�(Vecteur N, Vecteur u, double d);

/**
 * pour factoriser du code dans les 2 m�thodes qui suivent
 * 
 * calcule l'abscisse curviligne t, le vecteur normal rentrant j, le projet� orthogonal et d, la distance sign�e de projet� orthogonal � p
 * 
 * */
public void f(final Vecteur p, double t[], Vecteur j, Vecteur projet�, double d[])
{
t[0] = this.projet�Orthogonal(p);
j.set( this.base(t[0]) [1]);
projet�.set( this.�value(t[0]) );
d[0] = Vecteur.difference(p, projet�).produitScalaire(j);
}

/**
 * Calcule la distance sign�e entre le point p et l'arc this. 
 * Si cette distance est > 0, p est � l'int�rieur du contour
 * Si cette distance est < 0, p est � l'ext�rieur du contour
 * Si cette distance est == 0, p est � sur l'arc
 * 
 * @throws une ExceptionGeometrique pour indiquer que la distance sign�e n'est pas d�finie (se produit lorsque le projet� orthogonal de p n'est pas d�fini)
 * 
 * */
public double distanceSign�e(Vecteur p) throws ExceptionGeometrique
{
double t[] = new double[1];
Vecteur j = new Vecteur();
Vecteur projet� = new Vecteur();
double d[] = new double[1];

this.f(p, t, j, projet�, d);
return d[0];
}


/**
 * calcule les �ventuels points d'intersection avec la droite d�finie par {A,n}
 * o� A est un point de passage et n un vecteur normal � la droite
 * 
 * @return le tableau des points d'intersection entre this et {A,n}
 * 
 * */
public  Vecteur [] intersectionArcDroite(final Vecteur A, final Vecteur n)
{
double t[] = this.intersectionDroite(A, n);

Vecteur [] r = new Vecteur[t.length];
int i;
for (i = 0; i < t.length; ++i) r[i] = �value(t[i]);

return r;
}

/**
 * Calcule l'oppos� de p suivant la direction et le sens d�fini par le projet� orthogonal N au contour C dont this fait partie.
 * Explication : Le point trouv� est unique. 
 * Il est d�fini comme suit :
 * 
 * Notons d la droite munie du rep�re orient� {p,n} avec p comme point de passage et n vecteur directeur de la droite orthogonale � this passant par p
 * On suppose que p est dans le contour. n orient� de p vers N.
 * La droite coupe le contour C en 2 points A et B d'�loignement maximal puisque C est ferm� et que p est � l'int�rieur de C.
 * Supposons que les points A, p et B soient orient�s dans le sens A < p < B (suivant n).
 * Notons xA, xp,xB les abscisses respectifs de A, p et B suivant le rep�re {p,n}
 * 
 * notons deltaX = xB - xp.
 * 
 * Alors retourne le point M tel que vecteur(M ->A) == deltaX * n
 * 
 * 
 * * @throws une ExceptionGeometrique pour indiquer que l'oppos� n'est pas d�fini (se produit lorsque le projet� orthogonal de p n'est pas d�fini)
 * 
 * */
public Vecteur oppos�(Vecteur p) throws ExceptionGeometrique
{
double t[] = new double[1];
Vecteur j = new Vecteur();
Vecteur projet� = new Vecteur();
double d[] = new double[1];

this.f(p, t, j, projet�, d);

return this.oppos�(projet�, j.oppos�(), -d[0]);
}







}

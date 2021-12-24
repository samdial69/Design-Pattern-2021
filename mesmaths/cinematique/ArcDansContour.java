package mesmaths.cinematique;

import mesmaths.geometrie.ExceptionGeometrique;
import mesmaths.geometrie.base.Vecteur;
import mesmaths.geometrie.formes.ArcCourbe;

/**
 * ArcDansContour au sens large : rectiligne ou curviligne
 * Représente une partie orientée d'un contour fermé.
 * Un arc dispose d'un sens de parcours indiqué par la base. (cf. méthode base())
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
 * Calcule l'abscisse curviligne t du projeté orthogonal de p sur this
 * l'abscisse curviligne t décrit l'intervalle [0,1].
 * t == 0 <---> début de l' arc et t == 1 <---> fin de l' arc 
 * @throws une ExceptionGeometrique pour indiquer que le projeté orthogonal de p n'est pas défini
 * 
 * */
@Override
public double projetéOrthogonal(Vecteur p) throws ExceptionGeometrique
{
return this.arcCourbe.projetéOrthogonal(p);
}

/**
 * Calcule, sur l'arc this, le point de passage  correspondant à l'abscisse curviligne t.
 * 
 * */
@Override
public Vecteur évalue(double t)
{
return this.arcCourbe.évalue(t);
}


/**
 * Calcule un vecteur tangent (pas forcément unitaire) dans le sens positif en p(t)
 * 
 * t doit être tel que 0 <= t <= 1
 * 
 * */
@Override
public Vecteur vecteurTangent(double t)
{
return this.arcCourbe.vecteurTangent(t);
}


/**
 * Calcule une base orthonormale de 2 vecteurs {i,j} au point d'abscisse curviligne t.
 * On doit avoir 0 <= t <= 1. t == 0 <---> début de l' arc et t == 1 <---> fin de l' arc 
 * Cette base est telle que i soit un vecteur tangent à l'arc et j soit un vecteur rentrant dans le contour.
 * j indique où se trouve l'intérieur du contour fermé. 
 * |i| == |j| == 1 et i.j == 0
 * @return un tableau t de 2 éléments tel que t[0] = i et tel que t[1] = j
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
 * fait la même chose que la 2ème méthode opposé() 
 * N est un point quelconque de this. 
 * u est le vecteur unitaire sur une droite D passant par N et perpendiculaire au contour. u est orienté vers l'extérieur.
 * D = {N,u}
 * On suppose qu'il existe un point p sur D à l'intérieur du contour tel que l'abscisse de p sur {N,u} soit d (donc d < 0)
 * 
 * */
public abstract Vecteur opposé(Vecteur N, Vecteur u, double d);

/**
 * pour factoriser du code dans les 2 méthodes qui suivent
 * 
 * calcule l'abscisse curviligne t, le vecteur normal rentrant j, le projeté orthogonal et d, la distance signée de projeté orthogonal à p
 * 
 * */
public void f(final Vecteur p, double t[], Vecteur j, Vecteur projeté, double d[])
{
t[0] = this.projetéOrthogonal(p);
j.set( this.base(t[0]) [1]);
projeté.set( this.évalue(t[0]) );
d[0] = Vecteur.difference(p, projeté).produitScalaire(j);
}

/**
 * Calcule la distance signée entre le point p et l'arc this. 
 * Si cette distance est > 0, p est à l'intérieur du contour
 * Si cette distance est < 0, p est à l'extérieur du contour
 * Si cette distance est == 0, p est à sur l'arc
 * 
 * @throws une ExceptionGeometrique pour indiquer que la distance signée n'est pas définie (se produit lorsque le projeté orthogonal de p n'est pas défini)
 * 
 * */
public double distanceSignée(Vecteur p) throws ExceptionGeometrique
{
double t[] = new double[1];
Vecteur j = new Vecteur();
Vecteur projeté = new Vecteur();
double d[] = new double[1];

this.f(p, t, j, projeté, d);
return d[0];
}


/**
 * calcule les éventuels points d'intersection avec la droite définie par {A,n}
 * où A est un point de passage et n un vecteur normal à la droite
 * 
 * @return le tableau des points d'intersection entre this et {A,n}
 * 
 * */
public  Vecteur [] intersectionArcDroite(final Vecteur A, final Vecteur n)
{
double t[] = this.intersectionDroite(A, n);

Vecteur [] r = new Vecteur[t.length];
int i;
for (i = 0; i < t.length; ++i) r[i] = évalue(t[i]);

return r;
}

/**
 * Calcule l'opposé de p suivant la direction et le sens défini par le projeté orthogonal N au contour C dont this fait partie.
 * Explication : Le point trouvé est unique. 
 * Il est défini comme suit :
 * 
 * Notons d la droite munie du repère orienté {p,n} avec p comme point de passage et n vecteur directeur de la droite orthogonale à this passant par p
 * On suppose que p est dans le contour. n orienté de p vers N.
 * La droite coupe le contour C en 2 points A et B d'éloignement maximal puisque C est fermé et que p est à l'intérieur de C.
 * Supposons que les points A, p et B soient orientés dans le sens A < p < B (suivant n).
 * Notons xA, xp,xB les abscisses respectifs de A, p et B suivant le repère {p,n}
 * 
 * notons deltaX = xB - xp.
 * 
 * Alors retourne le point M tel que vecteur(M ->A) == deltaX * n
 * 
 * 
 * * @throws une ExceptionGeometrique pour indiquer que l'opposé n'est pas défini (se produit lorsque le projeté orthogonal de p n'est pas défini)
 * 
 * */
public Vecteur opposé(Vecteur p) throws ExceptionGeometrique
{
double t[] = new double[1];
Vecteur j = new Vecteur();
Vecteur projeté = new Vecteur();
double d[] = new double[1];

this.f(p, t, j, projeté, d);

return this.opposé(projeté, j.opposé(), -d[0]);
}







}

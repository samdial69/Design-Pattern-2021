package mesmaths.geometrie.formes;

import mesmaths.geometrie.ExceptionGeometrique;
import mesmaths.geometrie.base.Geop;
import mesmaths.geometrie.base.Vecteur;

public class  Segment extends ArcCourbe
{
public Vecteur P0, P1;
double P0P1;            // distance de P0 à P1
Vecteur [] base;

public Segment(Vecteur p0, Vecteur p1)
{
this.P0 = p0;
this.P1 = p1;
Vecteur u = Vecteur.difference(P1, P0);
this.P0P1 = u.norme();

this.base = u.base();
}

@Override
public Vecteur vecteurTangent(double t)
{
return this.base[0];
}

@Override
public Vecteur[] base(double t)
{
return this.base;
}


@Override
public double projetéOrthogonal(Vecteur p)
{
double t = Vecteur.difference(p, P0).produitScalaire(base[0])/this.P0P1;

if ( !(0 <= t && t <=1) ) throw new ExceptionGeometrique("l'abscisse curviligne de " + p +" n'est pas défini pour le segment [ "+ P0+" "+ P1+"]");
return t;
}

@Override
public Vecteur évalue(double t)
{
return P0.somme(base[0].produit(t*P0P1));
}

@Override
public double[] intersectionDroite(Vecteur A, Vecteur n)
{
double den = this.P0P1*Vecteur.produitScalaire(base[0], n);
double num = Vecteur.difference(A, P0).produitScalaire(n);

double  x[] = new double[] {num/den};
 
return filtre(x,0,1);
}



//----------------- classe Segment-------------------------------------
}

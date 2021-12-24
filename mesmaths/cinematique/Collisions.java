package mesmaths.cinematique;


import java.io.IOException;
import java.util.Vector;

import mesmaths.MesMaths;
import mesmaths.geometrie.base.Geop;
import mesmaths.geometrie.base.Vecteur;


public class Collisions
{

static double EPSILON = 1.0E-6;

/**
 * gestion de la collision avec traversement de la paroi (et réapparition sur le coté opposé) de la bille définie par (position) 
 *  avec un contour rectangulaire de l'écran. 
 *  Ce rectangle est défini par (abscisseCoinHautGauche, OrdonnéeCoinHautGauche,largeur,hauteur)
 *  si il n'y a pas collision, le vecteur position n'est pas modifié
 * si il y a collision, le vecteur position est modifié : la bille "ressort" sur le bord opposé au bord qu'elle pénètre.
 * @param position : vecteur position de la bille
 * 
 * */
public static void collisionBilleContourPasseMuraille(Vecteur position, 
        double abscisseCoinHautGauche, double ordonnéeCoinHautGauche, double largeur, double hauteur)
{
position.x = Collisions.traverseBord(position.x,abscisseCoinHautGauche, largeur);
position.y = Collisions.traverseBord(position.y,ordonnéeCoinHautGauche, hauteur);
}

/*
 * utile à la méthode collisionBilleContourPasseMuraille
 *
 */
private static double traverseBord(double x, double xMin, double largeur)
{
double xMax = xMin + largeur;

if (x > xMax)
   {
   double d = (x-xMin)/largeur;
   double d1 = Math.floor(d);
   int q = (int)d1;
   return x - q*largeur;
   }
   
else
   if (x < xMin)
      {
      double d = (xMax-x)/largeur;
      double d1 = Math.floor(d);
      int q = (int)d1;
      return x + q*largeur;
      }
   else
      return x;
}

/**
 * gestion de la collision avec arrêt suivant la direction horizontale  de la bille définie par (position,rayon,vitesse) avec un contour rectangulaire de l'écran.
 * 
 *  Ce rectangle est défini partiellement par (abscisseCoinHautGauche,largeur)
 * 
 * si il n'y a pas de collision avec un bord vertical, position et vitesse sont inchangés par la méthode
 * 
 *  si il y a collision avec un bord vertical
 * alors vitesse est modifié et  position reste intact
 * 
 * Dès qu'il y a collision avec un bord vertical, la composante horizontale du vecteur vitesse est annulée, de sorte que la bille continue à glisser le long de
 * la bande qui l'a arrêtée
 * 
 */
public static void collisionBilleContourAvecArretHorizontal(Vecteur position, double rayon, Vecteur vitesse,
        double abscisseCoinHautGauche, double largeur)
{
//vitesse.x = Collisions.arretSurBord(vitesse.x, position.x, rayon, abscisseCoinHautGauche, largeur);
double t[] = Collisions.arretSurBord(vitesse.x, position.x, rayon, abscisseCoinHautGauche, largeur);
vitesse.x = t[0];
position.x = t[1];
}

/**
 * gestion de la collision avec arrêt suivant la direction verticale  de la bille définie par (position,rayon,vitesse) avec un contour rectangulaire de l'écran.
 * 
 *  Ce rectangle est défini partiellement par (ordonnéeCoinHautgauche,hauteur)
 * 
 * si il n'y a pas de collision avec un bord horizontal, position et vitesse sont inchangés par la méthode
 * 
 *  si il y a collision avec un bord horizontal
 * alors vitesse est modifié et  position reste intact
 * 
 * Dès qu'il y a collision avec un bord horizontal, la composante verticale du vecteur vitesse est annulée, de sorte que la bille continue à glisser le long de
 * la bande qui l'a arrêtée
 * 
 */
public static void collisionBilleContourAvecArretVertical(Vecteur position, double rayon, Vecteur vitesse,
        double ordonnéeCoinHautGauche, double hauteur)
{
//vitesse.y = Collisions.arretSurBord(vitesse.y, position.y, rayon, ordonnéeCoinHautGauche, hauteur);
double t[] = Collisions.arretSurBord(vitesse.y, position.y, rayon, ordonnéeCoinHautGauche, hauteur);
vitesse.y = t[0];
position.y = t[1];
}



/*
 * utile à collisionBilleContourAvecArretHorizontal et à collisionBilleContourAvecArretVertical
 * */
private static double [] arretSurBord(double v, double x, double rayon, double xMin, double largeur)
{
double [] t = new double[2]; //  nouvelle composante_vitesse dans t[0] et nouvelle composante_position dans t[1]

double xMax = xMin+largeur;


if (x+rayon > xMax)
    {
    t[0] = 0;
    t[1] = xMax-rayon-Collisions.EPSILON;
    }
else
    if (x-rayon < xMin)
        {
        t[0] = 0;
        t[1] = xMin + Collisions.EPSILON + rayon;
        } 
    else
        {
        t[0] = v; t[1] = x; 
        }

return t;
}

/**
 * gestion de la collision avec rebond de la bille définie par (position,rayon,vitesse) avec le segment orienté [P0P1]
 * 
 * @return false si il n'y a pas de collision
 * 
 * @return true si il y a collision et dans ce cas modifie vitesse
 * 
 * 
 * c-à-d que en entrée on considère que (position,vitesse) sont le vecteur position et le vecteur vitesse de la bille immédiatement avant le choc
 * et en sortie (position,vitesse) sont le vecteur position et le vecteur vitesse de la bille immédiatement après le choc
 * 
 * le vecteur vitesse est modifié par la collision (comme une boule de billard l'est par une bande)
 * La méthode laisse le vecteur position intact 
 * @param position : vecteur position de la bille immédiatement avant la collision avec le segment [P0P1]
 * @param rayon : rayon de la bille
 * @param vitesse : vecteur vitesse de la bille immédiatement avant la collision avec le segment [P0P1]
 * @param P0 : le début du segment
 * @param P1 : la fin du segment
 * 
 * */
public static boolean collisionBilleSegmentAvecRebond( final Vecteur position,  double rayon, Vecteur vitesse, final Vecteur P0, final Vecteur P1)
{
Vecteur[] base = Geop.base( P0,  P1);
//Vecteur I = base[0];
Vecteur N = base[1];

double distanceSignée = position.difference(P0).produitScalaire(N);

if (distanceSignée >= rayon)  return false; // il n'y a pas collision entre la bille (position,rayon) et le segment [P0P1]

double vN = vitesse.produitScalaire(N);

if (vN >= 0) return false; // la bille est à l'extérieur et revient vers le cadre ou est à l'intérieur et s'éloigne du bord

// à présent distanceSignée < rayon et vN < 0

// -vN est l'intensité du choc !!!!

Vecteur deltaV = N.produit(-2*vN);  // calcul du changement de trajectoire

vitesse.ajoute(deltaV); // la bille rebondit

return true;

}                           // collisionBilleSegmentAvecRebond

/**
 * gestion de la collision avec rebond de la bille définie par (position,rayon,vitesse) avec un contour rectangulaire de l'écran.
 * 
 *  Ce rectangle est défini par (abscisseCoinHautGauche, OrdonnéeCoinHautGauche,largeur,hauteur)
 * 
 * @return false si il n'y a pas de collision
 * 
 * @return true si il y a collision et dans ce cas modifie vitesse
 * 
 * c-à-d que en entrée on considère que (position,vitesse) sont le vecteur position et le vecteur vitesse de la bille immédiatement avant le choc
 * et en sortie (position,vitesse) sont le vecteur position et le vecteur vitesse de la bille immédiatement après le choc
 * 
 * le vecteur vitesse est modifié par la collision (comme une boule de billard l'est par une bande)
 * La méthode laisse le vecteur position intact 
 * 
 * @param position : vecteur position de la bille immédiatement avant la collision avec le contour
 * @param rayon : rayon de la bille
 * @param vitesse : vecteur vitesse de la bille immédiatement avant la collision avec le contour
 * @param abscisseCoinHautGauche : abscisse minimale du contour rectangulaire
 * @param ordonnéeCoinHautGauche : ordonnée minimale du contour rectangulaire
 * @param largeur : largeur du contour rectangulaire
 * @param hauteur : hauteur du contour rectangulaire
 * 
 * 
 * */
public static boolean collisionBilleContourAvecRebond( final Vecteur position,  double rayon, Vecteur vitesse, 
        double abscisseCoinHautGauche, double ordonnéeCoinHautGauche, double largeur, double hauteur)
{
Vecteur min = new Vecteur(abscisseCoinHautGauche,ordonnéeCoinHautGauche);
Vecteur diago = new Vecteur(largeur, hauteur);
Vecteur max = min.somme(diago); 


Vecteur coins[] = new Vecteur[5];

coins[0] = min;         // le coin haut gauche du rectangle défini par le composant
coins[1] = new Vecteur(max.x,min.y);
coins[2] = max;
coins[3] = new Vecteur(min.x, max.y);
coins[4] = coins[0];                    // pour refermer le contour !

int i;
for ( i = 1; i < coins.length; ++i)
    if (Collisions.collisionBilleSegmentAvecRebond(position, rayon, vitesse, coins[i-1], coins[i])) 
       return true;

return false;
}

/**
 * Effectue les calculs correspondants à la collision entre la bille b1(G1,rayon1,v1,m1) et la bille b2(G2,rayon2,v2,m2)
 * 
 * La méthode laisse toujours G1 et G2 intacts.
 *
 * @param G1 : centre de la bille b1
 * @param rayon1 : rayon de la bille b1
 * @param v1 : vecteur vitesse de la bille b1
 * @param m1 : masse de la bille b1
 * 
 * @param G2 : centre de la bille b2
 * @param rayon2 : rayon de la bille b2
 * @param v2 : vecteur vitesse de la bille b2
 * @param m2 : masse de la bille b2
 * 
 * @return true si il y a collision et false si il n'y a pas collision
 * 
 * si retourne true, modifie les vecteurs vitesse v1 et v2
 */
public static boolean CollisionBilleBille( final Vecteur G1, double rayon1, Vecteur v1, double m1, 
                                           final Vecteur G2, double rayon2, Vecteur v2, double m2)   
//modifie b1 et b2
//données : b1 et b2 avant le choc
//résultats : b1 et b2 après le choc
{
Vecteur G1G2 = Vecteur.difference(G2, G1);

double nG1G2_2 = G1G2.normeCarrée();

double r = rayon1+rayon2;
double r2 = r*r;

if ( nG1G2_2 >= r2 )  return false;   // il n'y a pas de collision entre les 2 billes car elles sont trop éloignées l'une de l'autre

double  nG1G2 = Math.sqrt(nG1G2_2);

Vecteur N = G1G2.produit(1 / nG1G2);    // vecteur unitaire sur le segment [G1 G2]

double v1N, v2N;

v1N = v1.produitScalaire(N);
v2N = v2.produitScalaire(N);

double a = v1N - v2N;
   
if (a <= 0) return false;       // les billes séloignent l'une de l'autre, la collision a donc déjà été traitée

// à présent a > 0. a représente l'intensité du choc entre les 2 billes !!!!

// calculons les nouveaux vecteurs vitesse immédiatement après le choc

double masseTotale = m1+m2;
double alfa = (m1-m2)/masseTotale;
double deuxSurM = 2/masseTotale;

double v1Np =          alfa * v1N + m2 * deuxSurM * v2N;
double v2Np = m1 * deuxSurM * v1N -          alfa * v2N;

Vecteur U = N.rotationQuartDeTour();

double v1T = v1.produitScalaire(U);
double v2T = v2.produitScalaire(U);

v1.set(Vecteur.combinaisonLinéaire(v1Np,  N , v1T, U));
v2.set(Vecteur.combinaisonLinéaire(v2Np,  N , v2T, U));

return true;

} // collisionBilleBille

} // Collisions

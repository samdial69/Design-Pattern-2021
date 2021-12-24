package mesmaths.cinematique;

import java.util.Observable;
import java.util.Vector;

import mesmaths.geometrie.ExceptionGeometrique;
import mesmaths.geometrie.base.Geop;
import mesmaths.geometrie.base.Vecteur;
import mesmaths.geometrie.formes.ArcCourbe;

public class Collisions1 extends Observable
{
public static final Collisions1 collisions1;    // pour mettre en oeuvre Observable, il nous faut au moins une instance de Collisions1
static
{
collisions1 = new Collisions1();
}

//static double EPSILON = 1.0E-6;
static final double EPSILON_CHOC_BILLE = 1.0E-2;  // en deça de cette valeur pour |u.(v1-v2)| le choc entre 2 billes est considéré statique
static final double EPSILON_CHOC_PAROI = 1.0E-2;  // en deça de cette valeur pour |J.v| le choc entre une bille et la paroi est considéré statique 
public static final int PAS_DE_CHOC = 0;
public static final int CHOC_DYNAMIQUE = 1;
public static final int CHOC_MOU = 2;

public static final double g = 0.005;
public final static double ro = 500;        // masse volumique de la paroi : pour le calcul de la poussée d'Archimède

public static  double COEFF_ELASTICITE_BILLE = 3;
public static  double COEFF_ELASTICITE_PAROI = 1; // quantité >= 0. si égal à zéro alors minimum de réaction de la part de la paroi
public static double COEFF_PAROI = 2*(1+COEFF_ELASTICITE_PAROI)*g;
public static double COEFF_BILLE_BILLE = 2*(1+COEFF_ELASTICITE_BILLE)*g;




/**
 * 
 * pour gérer les chocs mous avec la paroi.
 * hypothèses : rayon > distanceSignée > -rayon
 * 
 * 
 * */
public static Vecteur  pousséeArchimède(final Vecteur N, double distanceSignée, double rayon)
{
double e = rayon-distanceSignée; // profondeur de la pénétration de la bille dans le mur

double v = Geop.volumeSphèreTronquée(rayon, e);
double intensitéPousséeArchimède = v*ro*g;
return N.produit(intensitéPousséeArchimède);
}


/**
 * Vérifie si il y a collision entre la bille (position, rayon, vitesse) et arc
 * 
 * @return false si il n'y a pas de collision et true si il y a collision
 * 
 * Si il y a collision alors calcule aussi :
 * t l'abscisse curviligne du projeté orthogonal de position sur arc
 * {i,j} la base orthonormale locale en t sur arc orientée telle que i vecteur tangent dans le sens croissant sur arc et j orienté vers l'intérieur 
 * le projeté orthogonal de position sur arc
 * vN : composante normale à l'arc du vecteur vitesse telle que vN < 0
 * d : distance signée de position à l'arc, telle que d < rayon
 * 
 * */
public static boolean  toutesCollisionsBord( final Vecteur position, double rayon, final Vecteur vitesse, final ArcCourbe arc,
        double t[], Vecteur i, Vecteur j, Vecteur projeté, double vN[], double d[])
{
try
{
t[0] = arc.projetéOrthogonal(position);

Vecteur [] base = arc.base(t[0]);
i.set(base[0]);
j.set(base[1]);

vN[0] = vitesse.produitScalaire(j);

if (vN[0] >= 0) return false;            // la bille est à l'extérieur du contour et revient vers celui-ci ou bien 
                                         // la bille est à l'intérieur du contour et s'éloigne du bord

// à présent vN < 0

projeté.set( arc.évalue(t[0]));
d[0] = Vecteur.difference(position, projeté).produitScalaire(j);      // d est la distance signée de position à l'arc

if (d[0] >= rayon) return false;

// à présent vN < 0 et d < rayon, il y a donc collision avec le bord
   
return true;
   
}
catch (ExceptionGeometrique e)      // le projeté orthogonal de position n'existe pas, cela signifie qu'il existe un autre arc du contour qui est plus 
{                       // proche de position que ne l'est this
return false;
}

} // toutesCollisionsBord



/**
 * gestion de la collision avec traversée de la paroi (et réapparition sur le coté opposé) de la bille définie par (position, rayon, vitesse) 
 *  avec un segment curviligne (arc) du billard (le billard est un contour fermé constitué d'arcs). 
 *  
 *  si il n'y a pas collision, le vecteur position n'est pas modifié
 * si il y a collision, le vecteur position est modifié : la bille "ressort" sur le bord opposé au bord qu'elle pénètre.
 * @param position : vecteur position de la bille
 * 
 * */
public static boolean actionReactionArcPasseMuraille(Vecteur position, double rayon, final Vecteur vitesse, final ArcDansContour arc)
{
double t[] = new double[1];
Vecteur i = new Vecteur();
Vecteur j = new Vecteur();
Vecteur projeté = new Vecteur();
double vN[] = new double[1];
double d[] = new double[1];
    
if (toutesCollisionsBord(position, rayon, vitesse, arc, t, i, j, projeté, vN, d))
    {
    // à présent vN < 0 et d < rayon, il y a donc collision avec le bord
    
    position.set(arc.opposé(projeté,j.opposé(),-d[0]));
    return true;
    } 
    
else return false;
}


/**
 * gestion de la collision avec arrêt suivant la direction normale  de la bille définie par (position,rayon,vitesse) avec un arc du billard 
 * (le billard est un contour fermé constitué d'arcs).
 * 
 * si il n'y a pas de collision avec l'arc, position et vitesse sont inchangés par la méthode
 * 
 *  si il y a collision avec l'arc
 * alors vitesse est modifié et  position reste intact
 * 
 * Dès qu'il y a collision avec l'arc, la composante normale  du vecteur vitesse est annulée, de sorte que la bille continue à glisser le long de
 * la bande qui l'a arrêtée
 * 
 */
public static boolean actionReactionArcArret( final Vecteur position, double rayon, Vecteur vitesse, final ArcCourbe arc) 
{
double t[] = new double[1];
Vecteur i = new Vecteur();
Vecteur j = new Vecteur();
Vecteur projeté = new Vecteur();
double vN[] = new double[1];
double d[] = new double[1];
    
if (toutesCollisionsBord(position, rayon, vitesse, arc, t, i, j, projeté, vN, d))
    {
    // à présent vN < 0 et d < rayon, il y a donc collision avec le bord
    
    Vecteur vs1 = j.produit(vN[0]);
    vitesse.retire(vs1);
    return true;
    } 
    
else return false;
}

/**
 * Effectue une partie des calculs concernant la collision d'une bille contre un arc,
 * 
 * qu'il y ait effet ou non. Permet de factoriser les calcules des 2 méthodes qui suivent.
 * 
 * DONNEES : la bille (position, rayon, vitesse, w, accélération,  masse), arc
 * 
 * si retourne PAS_DE_CHOC, ne calcule rien, cela signifie qu'il n'y a pas de collision
 * 
 * si retourne CHOC_MOU alors met à jour le vecteur accélération (ajoute une contribution) et calcule i, N, vT, vTplusrw
 * 
 * si retourne CHOC_DYNAMIQUE, calcule vNp la composante du vecteur vitesse normale à arc après la collision et calcule aussi 
 *  i, N, vT, vTplusrw et met à jour intensitéChoc en ajoutant (position du choc, intensité du choc). Cela sert à gérer les sons d'impact.
 * 
 * où vT est la composante normale à l'arc du vecteur vitesse incident 
 * et {i,N} est la base associée au projeté orthogonal de position sur l'arc
 * et vTplusrw = vT + r*w
 * 
 * */
public static int actionReactionArcTousRebonds( final Vecteur position, final double rayon, final Vecteur vitesse, final double w [], 
        Vecteur accélération, final double masse, 
       final ArcCourbe arc, Vector<Choc> intensitéChoc, Vecteur i, Vecteur N, double vNp[], double vT[], double vTplusrw[])
{
double t[] = new double[1];

Vecteur projeté = new Vecteur();
double vN[] = new double[1];
double d[] = new double[1];
    
if (!toutesCollisionsBord(position, rayon, vitesse, arc, t, i, N, projeté, vN, d)) return PAS_DE_CHOC;

//à présent, on a forcément : vN < 0 et d < rayon

vT[0] = vitesse.produitScalaire(i);
double rw = rayon*w[0];
vTplusrw[0] = vT[0]+rw; 

if (vN[0] >= -EPSILON_CHOC_PAROI && Math.abs(vTplusrw[0]) <= EPSILON_CHOC_PAROI)          // -EPSILON_CHOC_PAROI <= vN[0] < 0  : le choc est mou
   {
   // calcul de la force de rappel
    
   double e = rayon - d[0];                    // profondeur de la prénétration de la bille dans le mur, on a e > 0
    
   //double forceRappel = COEFF_ELASTICITE_PAROI*e;
   
   Vecteur pousséeArchi = Collisions1.pousséeArchimède(N, d[0], rayon);
   
   //Vecteur a = N.produit(forceRappel/masse);
   Vecteur a = pousséeArchi.produit(1/masse);
   //Vecteur a = N.produit(2*g);
   //Vecteur a = N.produit(COEFF_PAROI);
   accélération.ajoute(a);              
   //System.err.println("choc mou : N = " + N +", e = " + e);
   //position.ajoute(N.produit(e));   //  Avec une translation, on remet la bille à l'intérieur du contour 
   return CHOC_MOU;
   }

else                // le choc est dynamique
   {
   // d'abord calcul du vecteur vitesse réfléchi
   vNp[0] = -vN[0];
   //intensitéChoc.add( vNp[0]);
   intensitéChoc.add( new Choc( projeté,vNp[0]));
   return CHOC_DYNAMIQUE;
   }
}               // actionReactionArcTousRebonds


/**
 * gestion de la collision dynamique (avec rebond) ou statique  de la bille définie par (position,rayon,vitesse,accélération, masse) avec arc
 * La bille ne tourne PAS sur elle-même
 * @return false si il n'y a pas de collision
 * 
 * @return true si il y a collision et dans ce cas modifie vitesse ou (xor) accélération. 
 * Si il y choc mou, met à jour le vecteur accélération (ajoute une contribution)
 * Si il y a choc dynamique, modifie le vecteur vitesse (écrase les anciennes coordonnées), redirige le vecteur vitesse vers l'intérieur du contour
 * Si il y a choc dynamique, met à jour intensitéChoc. ajoute (position du choc, intensité du choc). Cela est nécessaire pour la gestion 
 * des sons d'impacts.
 * 
 * c-à-d que en entrée on considère que (position,vitesse,accélération) sont le vecteur position, le vecteur vitesse et le vecteur accélération
 *  de la bille immédiatement avant le choc
 * et en sortie (position,vitesse,accélération) sont le vecteur position, le vecteur vitesse et le vecteur accélération de 
 * la bille immédiatement après le choc
 * 
 * si le choc est parfaitement dynamique, le vecteur vitesse est modifié par la collision (comme une boule de billard l'est par une bande)
 * si le choc est parfaitement statique, le vecteur accélération est modifié (on considère que le choc est élastique et une force de rappel 
 * est appliquée à la bille)
 * 
 * @param position : vecteur position de la bille immédiatement avant la collision avec le contour
 * @param rayon : rayon de la bille
 * @param vitesse : vecteur vitesse de la bille immédiatement avant la collision avec le contour
 * @param accélération : vecteur accélération de la bille immédiatement avant la collision avec le contour
 *
 * */
public static boolean actionReactionArcAvecRebond( final Vecteur position, double rayon, Vecteur vitesse, Vecteur accélération, double masse, 
        final ArcCourbe arc, Vector<Choc> intensitéChoc)
{

double [] w = {0};
Vecteur i = new Vecteur();
Vecteur N = new Vecteur();
double [] vNp = new double[1];
double [] vT = new double[1];
double [] vTplusrw = new double[1];

int ok = actionReactionArcTousRebonds(position, rayon, vitesse, w, accélération, masse, arc, intensitéChoc, i, N, vNp, vT, vTplusrw);

if (ok == PAS_DE_CHOC) return false;
else
   if (ok == CHOC_MOU) return true;
   else
      {
      Vecteur deltaV = N.produit(2*vNp[0]);      // deltaV = vecteur vitesse réfléchi - vecteur vitesse incident
      vitesse.ajoute(deltaV);
      return true;
      }
}                   // actionReactionBilleContourAvecRebond


/**
 * gestion de la collision dynamique (avec rebond) ou statique  de la bille définie par (position,rayon,vitesse,w,accélération, masse, J, Jplusmr2)
 *  avec arc
 * La bille tourne sur elle-même :
 * w est la vitesse angulaire
 * J est le moment d'inertie,
 * Jplusmr2 est J + masse*rayon^2
 * 
 * 
 * @return false si il n'y a pas de collision
 * 
 * @return true si il y a collision et dans ce cas modifie (vitesse et w)  ou (xor) accélération. 
 * Si il y choc mou, met à jour le vecteur accélération (ajoute une contribution)
 * Si il y a choc dynamique, calcule le nouveau vecteur vitesse {vT,vN} et calcule la nouvelle vitesse angulaire w.
 *  
 * Si il y a choc dynamique, met à jour intensitéChoc. ajoute (position du choc, intensité du choc). Cela est nécessaire pour la gestion 
 * des sons d'impacts.
 * 
 * c-à-d que en entrée on considère que (position,vitesse,w, accélération) sont le vecteur position, le vecteur vitesse, la vitesse angulaire
 *  et le vecteur accélération de la bille immédiatement avant le choc
 * et en sortie (position,vitesse,w, accélération) sont le vecteur position, le vecteur vitesse, la vitesse angulaire et le vecteur accélération de 
 * la bille immédiatement après le choc
 * 
 * si le choc est parfaitement dynamique, le vecteur vitesse et la vitesse angulaire sont modifiés par la collision
 *  (comme une boule de billard l'est par une bande)
 * si le choc est parfaitement statique, le vecteur accélération est modifié (on considère que le choc est élastique et une force de rappel 
 * est appliquée à la bille)
 * 
 * @param position : vecteur position de la bille immédiatement avant la collision avec le contour
 * @param rayon : rayon de la bille
 * @param vitesse : vecteur vitesse de la bille immédiatement avant la collision avec le contour
 * @param w : vitesse angulaire de la bille immédiatement avant le choc
 * @param accélération : vecteur accélération de la bille immédiatement avant la collision avec le contour
 * @param coeffFrottement : frottement de Coulomb contre la paroi tel que coeffFrottement >= 0.
 * Si coeffFrottement == 0, la bille glisse parfaitement sur l'arc, ni la vitesse tangentielle vT, ni la vitesse angulaire ne sont modifiées
 * Plus coeffFrottement augmente, plus on s'approche des conditions de roulement sans glisement. 
 * On conseille de fixer le coeff entre 0 et 1 pour des conditions réalistes.
 *
 * */

public static boolean actionReactionBilleArcAvecRebondEtEffet( final Vecteur position, final double rayon, Vecteur vitesse, double w [], 
        Vecteur accélération, final double masse, final double J, final double JplusMr2,
       final ArcCourbe arc, final double coeffFrottement, Vector<Choc> intensitéChoc)
{
Vecteur i = new Vecteur();
Vecteur N = new Vecteur();
double [] vNp = new double[1];
double [] vT = new double[1];
double [] vTplusrw = new double[1];

int ok = actionReactionArcTousRebonds(position, rayon, vitesse, w, accélération, masse, arc, intensitéChoc, i, N, vNp, vT, vTplusrw);

if (ok == PAS_DE_CHOC) return false;
else
  if (ok == CHOC_MOU) return true;
  else                              // choc dynamique
     {                                 //Collisions1.collisions1.setChanged(); 
     
     if ( Math.abs(vTplusrw[0]) <= coeffFrottement * (JplusMr2/J) * vNp[0])    // condition A de frottement (cf. frottement de Coulomb)
        {
        vT[0] -= 2 *             (J/JplusMr2) * vTplusrw[0];
        w[0]  -= 2 * rayon * (masse/JplusMr2) * vTplusrw[0];
        }
     
     else   // condition non A , ou de glissement (cf. frottements de Coulomb)
        {
        double beta = ( vTplusrw[0] > 0 ? -coeffFrottement : coeffFrottement);
        double deuxBeta = 2*beta;
        
        vT[0] += deuxBeta*vNp[0];
        w[0]  += deuxBeta*(masse*rayon/J)*vNp[0];
        }
     
     Vecteur vS = Vecteur.combinaisonLinéaire(vT[0], i, vNp[0], N);
     vitesse.set(vS);  
  
     return true;
     }

}                           // actionReactionBilleArcAvecRebondEtEffet




//-----------------------------------------------------------------------------------------------------------------
//---------------------------- à présent gestion des collisions bille - bille -------------------------------------
//-----------------------------------------------------------------------------------------------------------------


/**
 * Factorise une partie des calculs impliqués dans la collision bille-bille.
 * 
 *  DONNEES : 
 *  bille1 (position1, rayon1, vitesse1, w1, accélération1, masse1)
 *  bille2 (position2, rayon2, vitesse2, w2, accélération2, masse2)
 *  
 *  @return PAS_DE_CHOC si il n'y pas de collision
 *  
 *  @return CHOC_MOU si il y a un choc mou (sans vitesse). Dans ce cas met à jour accélération1 et accélération2 
 *  (ajoute aux 2 vecteurs une contribution)
 *  Dans ce cas calcule aussi nG1G2 (distance entre les centres des 2 billes G1 et G2), a = (v1N-v2N), base {N,u}, v1T, v2T, 
 *  deltavT = (v1T + r1*w1) - (v2T +r2*w2)
 *  
 *  @return CHOC_DYNAMIQUE si il y a un choc dynamique (supposé parfaitement élastique).
 *  Dans ce cas calcule aussi nG1G2 (distance entre les centres des 2 billes G1 et G2), a = (v1N-v2N), base {N,u}, v1T, v2T, 
 *  deltavT = (v1T + r1*w1) - (v2T +r2*w2), M
 *  v1N, v2N composantes de v1 et v2 normales immédiatement après le choc
 *  Dans ce cas, envoie aussi à un éventuel Observer un objet Choc (position du choc, intensité du choc) pour gérer les sons d'impact
 *  
 *  CONVENTIONS :
 *  
 *  {N,u} est la base orthonormée directe du segment orienté [G1,G2]
 *  telle que v1 = v1T*u + v1N * N et v2 = v2T*u + v2N * N
 *  M = m1+m2
 * 
 * */

public /*static*/ int actionReactionBilleBilleTousRebonds(
  final Vecteur position1, final double rayon1, final Vecteur vitesse1, final double w1[], Vecteur accélération1, final double masse1,
  final Vecteur position2, final double rayon2, final Vecteur vitesse2, final double w2[], Vecteur accélération2, final double masse2,
  double [] nG1G2, double [] a, Vecteur [] base, double [] v1T, double v2T[], double [] deltavT, double [] M, double [] v1Np, double [] v2Np)
{
Vecteur G1G2;
double nG1G2_2;
G1G2 = Vecteur.difference(position2, position1);
nG1G2_2 = G1G2.normeCarrée();

double r = rayon1+rayon2;

double r2 = r*r;

if (nG1G2_2 >= r2) return PAS_DE_CHOC;           // on a donc nG1G2 >= rayon1 + rayon2, il n'y a donc pas de choc

// à présent nG1G2_2 < r2

nG1G2[0] = Math.sqrt(nG1G2_2);

// on a donc nG1G2 < r

Vecteur N = base[0] = G1G2.produit(1 / nG1G2[0]);

double v1N, v2N;

v1N = vitesse1.produitScalaire(N);
v2N = vitesse2.produitScalaire(N);

a[0] = v1N - v2N;

if (a[0] <= 0) return PAS_DE_CHOC;       // il n'y a pas de collision, les billes s'éloignent l'une de l'autre

// à présent,  a[0] > 0 et nG1G2 < r 

Vecteur u = base[1] = N.rotationQuartDeTour();

v1T[0] = vitesse1.produitScalaire(u);
v2T[0] = vitesse2.produitScalaire(u);

deltavT[0] = (v1T[0] + rayon1*w1[0]) - ( v2T[0]+ rayon2*w2[0]); 


if (a[0] < EPSILON_CHOC_BILLE && Math.abs(deltavT[0]) < EPSILON_CHOC_BILLE) // le choc est mou car a[0] ~= 0
   {
   double e = r - nG1G2[0];       // e est la profondeur de la pénétration d'une bille dans l'autre

   //double forceRappel = COEFF_ELASTICITE_BILLE * e;

   Vecteur a12, a21;
   //a12 = N.produit(forceRappel / masse2);
   //a21 = N.produit(-forceRappel / masse1);
   
   
   //a12 = N.produit(2*g);
   //a21 = N.produit(-2*g);
   
   a12 = N.produit(COEFF_BILLE_BILLE);
   a21 = N.produit(-COEFF_BILLE_BILLE);
   
   accélération1.ajoute(a21);
   accélération2.ajoute(a12);
   
   //double kk = e*rayon1*rayon2/r;
   //double d1 = kk/rayon1, d2 = kk/rayon2;
   
   //position1.ajoute(N.produit(-d1));// on remet la bille n°1 à l'extérieur de la bille n°2 avec une translation propotionnelle à 1/r1
   //position2.ajoute(N.produit(d2)); // on remet la bille n°2 à l'extérieur de la bille n°1 avec une translation propotionnelle à 1/r2
   
   //System.err.println("choc mou entre 2 billes: N = " + N +", d1 = " + d1 +", d2 = " + d2);
   return CHOC_MOU;
   }
else        // à présent, a[0] >= EPSILON_CHOC_BILLE et nG1G2 < r, le choc est donc dynamique
   { 
   double m1 = masse1;
   double m2 = masse2;
   M[0] = m1+m2;
    
   double alfa = (m1-m2)/M[0];
   double deuxSurM = 2/M[0];
    
   v1Np[0] =          alfa * v1N + deuxSurM *m2 * v2N; 
   v2Np[0] = deuxSurM * m1 * v1N -         alfa * v2N;
   
   //intensitéChoc.add( a[0]);
   //intensitéChoc.add( new Choc( position1.somme(N.produit(rayon1)), a[0]));
   this.setChanged();
   this.notifyObservers(new Choc( position1.somme(N.produit(rayon1)), a[0]));
   return CHOC_DYNAMIQUE;
   }

} //actionReactionBilleBilleTousRebonds


/**
 * gère la collision dynamique ou statique des 2 billes définies respectivement par 
 * ( position1, rayon1, vitesse1, accélération1, masse1 ) et par ( position2, rayon2, vitesse2, accélération1, masse2)
 * 
 * si il n'y a pas de collision
 * renvoie false et ne modifie rien
 *
 * si il y a collision statique :
 * renvoie true et modifie  accélération1 et accélération2. (une force de rappel élastique est appliquée aux deux billes). 
 * Ajoute une contribution aux 2 vecteurs accélération.
 * 
 * si il y a collision dynamique :
 * renvoie true  et calcule les nouveaux vecteurs vitesse  vitesse1 et vitesse2 immédiatement après le choc
 *  Dans ce cas, envoie aussi à un éventuel Observer un objet Choc (position du choc, intensité du choc) pour gérer les sons d'impact
 * 
 * Les nouvelles accélérations ou les nouveaux vecteurs vitesses sont calculés pour représenter l'état des billes immédiatement après le choc  
 * 
 * */

public static boolean actionReactionBilleBille( Vecteur position1, double rayon1, Vecteur vitesse1, Vecteur accélération1, double masse1, 
                                                Vecteur position2, double rayon2, Vecteur vitesse2, Vecteur accélération2, double masse2)   
//modifie b1 et b2
//données : b1 et b2 avant le choc
//résultats : b1 et b2 après le choc
{
double [] w1 = {0};
double [] w2 = {0};
double [] nG1G2 = new double[1];
double [] a = new double[1];
Vecteur [] base = new Vecteur[2];
double [] v1T = new double[1];
double [] v2T = new double[1];
double [] deltavT = new double[1];
double [] M = new double[1];
double [] v1Np = new double[1];
double [] v2Np = new double[1];
int ok = Collisions1.collisions1.actionReactionBilleBilleTousRebonds( position1, rayon1, vitesse1, w1, accélération1, masse1, 
                                              position2, rayon2, vitesse2, w2, accélération2, masse2, 
                                                 nG1G2, a, base, v1T, v2T, deltavT, M, v1Np, v2Np);

if (ok == PAS_DE_CHOC) return false;
else
   if (ok == CHOC_MOU) return true;
   else
      { 
      Vecteur v1S = Vecteur.combinaisonLinéaire(v1Np[0], base[0], v1T[0], base[1]);
      Vecteur v2S = Vecteur.combinaisonLinéaire(v2Np[0], base[0], v2T[0], base[1]);
   
      vitesse1.set(v1S);                  // vecteur vitesse de la bille 1 après le choc
      vitesse2.set(v2S);                  // vecteur vitesse de la bille 2 après le choc
    
      return true;
      }
   
} // actionReactionBilleBille



/**
 * gère la collision dynamique ou statique des 2 billes b1 et b 2 définies respectivement par 
 * ( position1, rayon1, vitesse1, w1, accélération1, m1, J1, J1plusM1r12 ) et par 
 * ( position2, rayon2, vitesse2, w2, accélération2, m2, J2, J2plusM2r22)
 * 
 * Les billes tournent sur elles-mêmes !
 * w1 est la vitesse angulaire
 * J1 est le moment d'inertie
 * J1plusM1r12 = J1 + m1*r1^2
 * 
 * même chose pour b2
 * 
 * si il n'y a pas de collision
 * renvoie false et ne modifie rien
 *
 * si il y a collision statique :
 * renvoie true et modifie  accélération1 et accélération2. (une force de rappel élastique est appliquée aux deux billes). 
 * Ajoute une contribution aux 2 vecteurs accélération.
 * 
 * si il y a collision dynamique :
 * renvoie true  et calcule les nouveaux vecteurs vitesse  vitesse1 et vitesse2 et les nouvelles vitesses angulaires w1 et w2 
 * immédiatement après le choc
 *  Dans ce cas, envoie aussi à un éventuel Observer un objet Choc (position du choc, intensité du choc) pour gérer les sons d'impact
 * 
 * Les nouvelles accélérations ou les nouveaux vecteurs vitesses et vitesses angulaires w1 et w2 sont calculés
 *  pour représenter l'état des billes immédiatement après le choc
 *  
 * @param coeffFrottement : frottement de Coulomb entre 2 billes  tel que coeffFrottement >= 0.
 * Si coeffFrottement == 0, les billes glissent parfaitement l'une sur l'autre, ni les vitesses tangentielles v1T, v2T,
 *  ni les vitesses angulaires w1 et w2  ne sont modifiées
 * Plus coeffFrottement augmente, plus on s'approche des conditions de roulement sans glisement. 
 * On conseille de fixer le coeff entre 0 et 1 pour des conditions réalistes.
 * 
 * */

public static boolean actionReactionBilleBilleAvecEffet( 
       Vecteur position1, double rayon1, Vecteur vitesse1, double w1[], Vecteur accélération1, double m1, final double J1, final double J1plusM1r12,
       Vecteur position2, double rayon2, Vecteur vitesse2, double w2[], Vecteur accélération2, double m2, final double J2, final double J2plusM2r22,
                                                 final double coeffFrottement)   
//modifie b1 et b2
//données : b1 et b2 avant le choc
//résultats : b1 et b2 après le choc
{
                            ////System.err.println("actionReactionBilleBilleAvecEffet, début ");

double [] nG1G2 = new double[1];
double [] a = new double[1];
Vecteur [] base = new Vecteur[2];
double [] v1T = new double[1];
double [] v2T = new double[1];
double [] deltavT = new double[1];
double [] M = new double[1];
double [] v1Np = new double[1];
double [] v2Np = new double[1];
int ok = Collisions1.collisions1.actionReactionBilleBilleTousRebonds( position1, rayon1, vitesse1, w1, accélération1, m1, 
                                              position2, rayon2, vitesse2, w2, accélération2, m2, 
                                                 nG1G2, a, base, v1T, v2T, deltavT, M, v1Np, v2Np);

if (ok == PAS_DE_CHOC) return false;
else
   if (ok == CHOC_MOU) return true;
   else
      {                                     // choc dynamique
      double ki = J1plusM1r12/(J1*m1) + J2plusM2r22/(J2*m2);
      double mu = 1/ki;
      double m1m2 = m1*m2;
      
      double r1SurJ1 = rayon1/J1;
      double r2SurJ2 = rayon2/J2;
      
      if ( Math.abs(deltavT[0]) <= coeffFrottement * m1m2 /(M[0]*mu) * Math.abs(a[0]) ) // il y a frottement (cf. frottement de coulomb)
          {
          double deuxmuvT = 2*mu*deltavT[0];
          v1T[0] -= deuxmuvT/m1;
          w1[0]  -= deuxmuvT*r1SurJ1;
          v2T[0] += deuxmuvT/m2;
          w2[0]  += deuxmuvT*r2SurJ2;
          }
        
        
      else                                          // il y a glissement (cf. frottement de Coulomb)
         {
         
         
         double beta = ( deltavT[0] > 0 ? coeffFrottement : -coeffFrottement );
         
         double x = 2*beta*(-a[0])/M[0];        // x = 2 * beta * (vN2 - vN1)/m    où m = m1+m2
         
         double xm1m2 = x*m1m2;
          
         v1T[0] += x*m2;
         w1[0]  += xm1m2*r1SurJ1;
         v2T[0] -= x*m1;
         w2[0]  -= xm1m2*r2SurJ2;
         }                          // roulement pur
      
      Vecteur v1S = Vecteur.combinaisonLinéaire(v1Np[0], base[0], v1T[0], base[1]);
      Vecteur v2S = Vecteur.combinaisonLinéaire(v2Np[0], base[0], v2T[0], base[1]);
   
      vitesse1.set(v1S);                  // vecteur vitesse de la bille 1 après le choc
      vitesse2.set(v2S);                  // vecteur vitesse de la bille 2 après le choc
    
      return true;
      }                     // choc dynamique

} // actionReactionBilleBilleAvecEffet

} // Collisions1



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
static final double EPSILON_CHOC_BILLE = 1.0E-2;  // en de�a de cette valeur pour |u.(v1-v2)| le choc entre 2 billes est consid�r� statique
static final double EPSILON_CHOC_PAROI = 1.0E-2;  // en de�a de cette valeur pour |J.v| le choc entre une bille et la paroi est consid�r� statique 
public static final int PAS_DE_CHOC = 0;
public static final int CHOC_DYNAMIQUE = 1;
public static final int CHOC_MOU = 2;

public static final double g = 0.005;
public final static double ro = 500;        // masse volumique de la paroi : pour le calcul de la pouss�e d'Archim�de

public static  double COEFF_ELASTICITE_BILLE = 3;
public static  double COEFF_ELASTICITE_PAROI = 1; // quantit� >= 0. si �gal � z�ro alors minimum de r�action de la part de la paroi
public static double COEFF_PAROI = 2*(1+COEFF_ELASTICITE_PAROI)*g;
public static double COEFF_BILLE_BILLE = 2*(1+COEFF_ELASTICITE_BILLE)*g;




/**
 * 
 * pour g�rer les chocs mous avec la paroi.
 * hypoth�ses : rayon > distanceSign�e > -rayon
 * 
 * 
 * */
public static Vecteur  pouss�eArchim�de(final Vecteur N, double distanceSign�e, double rayon)
{
double e = rayon-distanceSign�e; // profondeur de la p�n�tration de la bille dans le mur

double v = Geop.volumeSph�reTronqu�e(rayon, e);
double intensit�Pouss�eArchim�de = v*ro*g;
return N.produit(intensit�Pouss�eArchim�de);
}


/**
 * V�rifie si il y a collision entre la bille (position, rayon, vitesse) et arc
 * 
 * @return false si il n'y a pas de collision et true si il y a collision
 * 
 * Si il y a collision alors calcule aussi :
 * t l'abscisse curviligne du projet� orthogonal de position sur arc
 * {i,j} la base orthonormale locale en t sur arc orient�e telle que i vecteur tangent dans le sens croissant sur arc et j orient� vers l'int�rieur 
 * le projet� orthogonal de position sur arc
 * vN : composante normale � l'arc du vecteur vitesse telle que vN < 0
 * d : distance sign�e de position � l'arc, telle que d < rayon
 * 
 * */
public static boolean  toutesCollisionsBord( final Vecteur position, double rayon, final Vecteur vitesse, final ArcCourbe arc,
        double t[], Vecteur i, Vecteur j, Vecteur projet�, double vN[], double d[])
{
try
{
t[0] = arc.projet�Orthogonal(position);

Vecteur [] base = arc.base(t[0]);
i.set(base[0]);
j.set(base[1]);

vN[0] = vitesse.produitScalaire(j);

if (vN[0] >= 0) return false;            // la bille est � l'ext�rieur du contour et revient vers celui-ci ou bien 
                                         // la bille est � l'int�rieur du contour et s'�loigne du bord

// � pr�sent vN < 0

projet�.set( arc.�value(t[0]));
d[0] = Vecteur.difference(position, projet�).produitScalaire(j);      // d est la distance sign�e de position � l'arc

if (d[0] >= rayon) return false;

// � pr�sent vN < 0 et d < rayon, il y a donc collision avec le bord
   
return true;
   
}
catch (ExceptionGeometrique e)      // le projet� orthogonal de position n'existe pas, cela signifie qu'il existe un autre arc du contour qui est plus 
{                       // proche de position que ne l'est this
return false;
}

} // toutesCollisionsBord



/**
 * gestion de la collision avec travers�e de la paroi (et r�apparition sur le cot� oppos�) de la bille d�finie par (position, rayon, vitesse) 
 *  avec un segment curviligne (arc) du billard (le billard est un contour ferm� constitu� d'arcs). 
 *  
 *  si il n'y a pas collision, le vecteur position n'est pas modifi�
 * si il y a collision, le vecteur position est modifi� : la bille "ressort" sur le bord oppos� au bord qu'elle p�n�tre.
 * @param position : vecteur position de la bille
 * 
 * */
public static boolean actionReactionArcPasseMuraille(Vecteur position, double rayon, final Vecteur vitesse, final ArcDansContour arc)
{
double t[] = new double[1];
Vecteur i = new Vecteur();
Vecteur j = new Vecteur();
Vecteur projet� = new Vecteur();
double vN[] = new double[1];
double d[] = new double[1];
    
if (toutesCollisionsBord(position, rayon, vitesse, arc, t, i, j, projet�, vN, d))
    {
    // � pr�sent vN < 0 et d < rayon, il y a donc collision avec le bord
    
    position.set(arc.oppos�(projet�,j.oppos�(),-d[0]));
    return true;
    } 
    
else return false;
}


/**
 * gestion de la collision avec arr�t suivant la direction normale  de la bille d�finie par (position,rayon,vitesse) avec un arc du billard 
 * (le billard est un contour ferm� constitu� d'arcs).
 * 
 * si il n'y a pas de collision avec l'arc, position et vitesse sont inchang�s par la m�thode
 * 
 *  si il y a collision avec l'arc
 * alors vitesse est modifi� et  position reste intact
 * 
 * D�s qu'il y a collision avec l'arc, la composante normale  du vecteur vitesse est annul�e, de sorte que la bille continue � glisser le long de
 * la bande qui l'a arr�t�e
 * 
 */
public static boolean actionReactionArcArret( final Vecteur position, double rayon, Vecteur vitesse, final ArcCourbe arc) 
{
double t[] = new double[1];
Vecteur i = new Vecteur();
Vecteur j = new Vecteur();
Vecteur projet� = new Vecteur();
double vN[] = new double[1];
double d[] = new double[1];
    
if (toutesCollisionsBord(position, rayon, vitesse, arc, t, i, j, projet�, vN, d))
    {
    // � pr�sent vN < 0 et d < rayon, il y a donc collision avec le bord
    
    Vecteur vs1 = j.produit(vN[0]);
    vitesse.retire(vs1);
    return true;
    } 
    
else return false;
}

/**
 * Effectue une partie des calculs concernant la collision d'une bille contre un arc,
 * 
 * qu'il y ait effet ou non. Permet de factoriser les calcules des 2 m�thodes qui suivent.
 * 
 * DONNEES : la bille (position, rayon, vitesse, w, acc�l�ration,  masse), arc
 * 
 * si retourne PAS_DE_CHOC, ne calcule rien, cela signifie qu'il n'y a pas de collision
 * 
 * si retourne CHOC_MOU alors met � jour le vecteur acc�l�ration (ajoute une contribution) et calcule i, N, vT, vTplusrw
 * 
 * si retourne CHOC_DYNAMIQUE, calcule vNp la composante du vecteur vitesse normale � arc apr�s la collision et calcule aussi 
 *  i, N, vT, vTplusrw et met � jour intensit�Choc en ajoutant (position du choc, intensit� du choc). Cela sert � g�rer les sons d'impact.
 * 
 * o� vT est la composante normale � l'arc du vecteur vitesse incident 
 * et {i,N} est la base associ�e au projet� orthogonal de position sur l'arc
 * et vTplusrw = vT + r*w
 * 
 * */
public static int actionReactionArcTousRebonds( final Vecteur position, final double rayon, final Vecteur vitesse, final double w [], 
        Vecteur acc�l�ration, final double masse, 
       final ArcCourbe arc, Vector<Choc> intensit�Choc, Vecteur i, Vecteur N, double vNp[], double vT[], double vTplusrw[])
{
double t[] = new double[1];

Vecteur projet� = new Vecteur();
double vN[] = new double[1];
double d[] = new double[1];
    
if (!toutesCollisionsBord(position, rayon, vitesse, arc, t, i, N, projet�, vN, d)) return PAS_DE_CHOC;

//� pr�sent, on a forc�ment : vN < 0 et d < rayon

vT[0] = vitesse.produitScalaire(i);
double rw = rayon*w[0];
vTplusrw[0] = vT[0]+rw; 

if (vN[0] >= -EPSILON_CHOC_PAROI && Math.abs(vTplusrw[0]) <= EPSILON_CHOC_PAROI)          // -EPSILON_CHOC_PAROI <= vN[0] < 0  : le choc est mou
   {
   // calcul de la force de rappel
    
   double e = rayon - d[0];                    // profondeur de la pr�n�tration de la bille dans le mur, on a e > 0
    
   //double forceRappel = COEFF_ELASTICITE_PAROI*e;
   
   Vecteur pouss�eArchi = Collisions1.pouss�eArchim�de(N, d[0], rayon);
   
   //Vecteur a = N.produit(forceRappel/masse);
   Vecteur a = pouss�eArchi.produit(1/masse);
   //Vecteur a = N.produit(2*g);
   //Vecteur a = N.produit(COEFF_PAROI);
   acc�l�ration.ajoute(a);              
   //System.err.println("choc mou : N = " + N +", e = " + e);
   //position.ajoute(N.produit(e));   //  Avec une translation, on remet la bille � l'int�rieur du contour 
   return CHOC_MOU;
   }

else                // le choc est dynamique
   {
   // d'abord calcul du vecteur vitesse r�fl�chi
   vNp[0] = -vN[0];
   //intensit�Choc.add( vNp[0]);
   intensit�Choc.add( new Choc( projet�,vNp[0]));
   return CHOC_DYNAMIQUE;
   }
}               // actionReactionArcTousRebonds


/**
 * gestion de la collision dynamique (avec rebond) ou statique  de la bille d�finie par (position,rayon,vitesse,acc�l�ration, masse) avec arc
 * La bille ne tourne PAS sur elle-m�me
 * @return false si il n'y a pas de collision
 * 
 * @return true si il y a collision et dans ce cas modifie vitesse ou (xor) acc�l�ration. 
 * Si il y choc mou, met � jour le vecteur acc�l�ration (ajoute une contribution)
 * Si il y a choc dynamique, modifie le vecteur vitesse (�crase les anciennes coordonn�es), redirige le vecteur vitesse vers l'int�rieur du contour
 * Si il y a choc dynamique, met � jour intensit�Choc. ajoute (position du choc, intensit� du choc). Cela est n�cessaire pour la gestion 
 * des sons d'impacts.
 * 
 * c-�-d que en entr�e on consid�re que (position,vitesse,acc�l�ration) sont le vecteur position, le vecteur vitesse et le vecteur acc�l�ration
 *  de la bille imm�diatement avant le choc
 * et en sortie (position,vitesse,acc�l�ration) sont le vecteur position, le vecteur vitesse et le vecteur acc�l�ration de 
 * la bille imm�diatement apr�s le choc
 * 
 * si le choc est parfaitement dynamique, le vecteur vitesse est modifi� par la collision (comme une boule de billard l'est par une bande)
 * si le choc est parfaitement statique, le vecteur acc�l�ration est modifi� (on consid�re que le choc est �lastique et une force de rappel 
 * est appliqu�e � la bille)
 * 
 * @param position : vecteur position de la bille imm�diatement avant la collision avec le contour
 * @param rayon : rayon de la bille
 * @param vitesse : vecteur vitesse de la bille imm�diatement avant la collision avec le contour
 * @param acc�l�ration : vecteur acc�l�ration de la bille imm�diatement avant la collision avec le contour
 *
 * */
public static boolean actionReactionArcAvecRebond( final Vecteur position, double rayon, Vecteur vitesse, Vecteur acc�l�ration, double masse, 
        final ArcCourbe arc, Vector<Choc> intensit�Choc)
{

double [] w = {0};
Vecteur i = new Vecteur();
Vecteur N = new Vecteur();
double [] vNp = new double[1];
double [] vT = new double[1];
double [] vTplusrw = new double[1];

int ok = actionReactionArcTousRebonds(position, rayon, vitesse, w, acc�l�ration, masse, arc, intensit�Choc, i, N, vNp, vT, vTplusrw);

if (ok == PAS_DE_CHOC) return false;
else
   if (ok == CHOC_MOU) return true;
   else
      {
      Vecteur deltaV = N.produit(2*vNp[0]);      // deltaV = vecteur vitesse r�fl�chi - vecteur vitesse incident
      vitesse.ajoute(deltaV);
      return true;
      }
}                   // actionReactionBilleContourAvecRebond


/**
 * gestion de la collision dynamique (avec rebond) ou statique  de la bille d�finie par (position,rayon,vitesse,w,acc�l�ration, masse, J, Jplusmr2)
 *  avec arc
 * La bille tourne sur elle-m�me :
 * w est la vitesse angulaire
 * J est le moment d'inertie,
 * Jplusmr2 est J + masse*rayon^2
 * 
 * 
 * @return false si il n'y a pas de collision
 * 
 * @return true si il y a collision et dans ce cas modifie (vitesse et w)  ou (xor) acc�l�ration. 
 * Si il y choc mou, met � jour le vecteur acc�l�ration (ajoute une contribution)
 * Si il y a choc dynamique, calcule le nouveau vecteur vitesse {vT,vN} et calcule la nouvelle vitesse angulaire w.
 *  
 * Si il y a choc dynamique, met � jour intensit�Choc. ajoute (position du choc, intensit� du choc). Cela est n�cessaire pour la gestion 
 * des sons d'impacts.
 * 
 * c-�-d que en entr�e on consid�re que (position,vitesse,w, acc�l�ration) sont le vecteur position, le vecteur vitesse, la vitesse angulaire
 *  et le vecteur acc�l�ration de la bille imm�diatement avant le choc
 * et en sortie (position,vitesse,w, acc�l�ration) sont le vecteur position, le vecteur vitesse, la vitesse angulaire et le vecteur acc�l�ration de 
 * la bille imm�diatement apr�s le choc
 * 
 * si le choc est parfaitement dynamique, le vecteur vitesse et la vitesse angulaire sont modifi�s par la collision
 *  (comme une boule de billard l'est par une bande)
 * si le choc est parfaitement statique, le vecteur acc�l�ration est modifi� (on consid�re que le choc est �lastique et une force de rappel 
 * est appliqu�e � la bille)
 * 
 * @param position : vecteur position de la bille imm�diatement avant la collision avec le contour
 * @param rayon : rayon de la bille
 * @param vitesse : vecteur vitesse de la bille imm�diatement avant la collision avec le contour
 * @param w : vitesse angulaire de la bille imm�diatement avant le choc
 * @param acc�l�ration : vecteur acc�l�ration de la bille imm�diatement avant la collision avec le contour
 * @param coeffFrottement : frottement de Coulomb contre la paroi tel que coeffFrottement >= 0.
 * Si coeffFrottement == 0, la bille glisse parfaitement sur l'arc, ni la vitesse tangentielle vT, ni la vitesse angulaire ne sont modifi�es
 * Plus coeffFrottement augmente, plus on s'approche des conditions de roulement sans glisement. 
 * On conseille de fixer le coeff entre 0 et 1 pour des conditions r�alistes.
 *
 * */

public static boolean actionReactionBilleArcAvecRebondEtEffet( final Vecteur position, final double rayon, Vecteur vitesse, double w [], 
        Vecteur acc�l�ration, final double masse, final double J, final double JplusMr2,
       final ArcCourbe arc, final double coeffFrottement, Vector<Choc> intensit�Choc)
{
Vecteur i = new Vecteur();
Vecteur N = new Vecteur();
double [] vNp = new double[1];
double [] vT = new double[1];
double [] vTplusrw = new double[1];

int ok = actionReactionArcTousRebonds(position, rayon, vitesse, w, acc�l�ration, masse, arc, intensit�Choc, i, N, vNp, vT, vTplusrw);

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
     
     Vecteur vS = Vecteur.combinaisonLin�aire(vT[0], i, vNp[0], N);
     vitesse.set(vS);  
  
     return true;
     }

}                           // actionReactionBilleArcAvecRebondEtEffet




//-----------------------------------------------------------------------------------------------------------------
//---------------------------- � pr�sent gestion des collisions bille - bille -------------------------------------
//-----------------------------------------------------------------------------------------------------------------


/**
 * Factorise une partie des calculs impliqu�s dans la collision bille-bille.
 * 
 *  DONNEES : 
 *  bille1 (position1, rayon1, vitesse1, w1, acc�l�ration1, masse1)
 *  bille2 (position2, rayon2, vitesse2, w2, acc�l�ration2, masse2)
 *  
 *  @return PAS_DE_CHOC si il n'y pas de collision
 *  
 *  @return CHOC_MOU si il y a un choc mou (sans vitesse). Dans ce cas met � jour acc�l�ration1 et acc�l�ration2 
 *  (ajoute aux 2 vecteurs une contribution)
 *  Dans ce cas calcule aussi nG1G2 (distance entre les centres des 2 billes G1 et G2), a = (v1N-v2N), base {N,u}, v1T, v2T, 
 *  deltavT = (v1T + r1*w1) - (v2T +r2*w2)
 *  
 *  @return CHOC_DYNAMIQUE si il y a un choc dynamique (suppos� parfaitement �lastique).
 *  Dans ce cas calcule aussi nG1G2 (distance entre les centres des 2 billes G1 et G2), a = (v1N-v2N), base {N,u}, v1T, v2T, 
 *  deltavT = (v1T + r1*w1) - (v2T +r2*w2), M
 *  v1N, v2N composantes de v1 et v2 normales imm�diatement apr�s le choc
 *  Dans ce cas, envoie aussi � un �ventuel Observer un objet Choc (position du choc, intensit� du choc) pour g�rer les sons d'impact
 *  
 *  CONVENTIONS :
 *  
 *  {N,u} est la base orthonorm�e directe du segment orient� [G1,G2]
 *  telle que v1 = v1T*u + v1N * N et v2 = v2T*u + v2N * N
 *  M = m1+m2
 * 
 * */

public /*static*/ int actionReactionBilleBilleTousRebonds(
  final Vecteur position1, final double rayon1, final Vecteur vitesse1, final double w1[], Vecteur acc�l�ration1, final double masse1,
  final Vecteur position2, final double rayon2, final Vecteur vitesse2, final double w2[], Vecteur acc�l�ration2, final double masse2,
  double [] nG1G2, double [] a, Vecteur [] base, double [] v1T, double v2T[], double [] deltavT, double [] M, double [] v1Np, double [] v2Np)
{
Vecteur G1G2;
double nG1G2_2;
G1G2 = Vecteur.difference(position2, position1);
nG1G2_2 = G1G2.normeCarr�e();

double r = rayon1+rayon2;

double r2 = r*r;

if (nG1G2_2 >= r2) return PAS_DE_CHOC;           // on a donc nG1G2 >= rayon1 + rayon2, il n'y a donc pas de choc

// � pr�sent nG1G2_2 < r2

nG1G2[0] = Math.sqrt(nG1G2_2);

// on a donc nG1G2 < r

Vecteur N = base[0] = G1G2.produit(1 / nG1G2[0]);

double v1N, v2N;

v1N = vitesse1.produitScalaire(N);
v2N = vitesse2.produitScalaire(N);

a[0] = v1N - v2N;

if (a[0] <= 0) return PAS_DE_CHOC;       // il n'y a pas de collision, les billes s'�loignent l'une de l'autre

// � pr�sent,  a[0] > 0 et nG1G2 < r 

Vecteur u = base[1] = N.rotationQuartDeTour();

v1T[0] = vitesse1.produitScalaire(u);
v2T[0] = vitesse2.produitScalaire(u);

deltavT[0] = (v1T[0] + rayon1*w1[0]) - ( v2T[0]+ rayon2*w2[0]); 


if (a[0] < EPSILON_CHOC_BILLE && Math.abs(deltavT[0]) < EPSILON_CHOC_BILLE) // le choc est mou car a[0] ~= 0
   {
   double e = r - nG1G2[0];       // e est la profondeur de la p�n�tration d'une bille dans l'autre

   //double forceRappel = COEFF_ELASTICITE_BILLE * e;

   Vecteur a12, a21;
   //a12 = N.produit(forceRappel / masse2);
   //a21 = N.produit(-forceRappel / masse1);
   
   
   //a12 = N.produit(2*g);
   //a21 = N.produit(-2*g);
   
   a12 = N.produit(COEFF_BILLE_BILLE);
   a21 = N.produit(-COEFF_BILLE_BILLE);
   
   acc�l�ration1.ajoute(a21);
   acc�l�ration2.ajoute(a12);
   
   //double kk = e*rayon1*rayon2/r;
   //double d1 = kk/rayon1, d2 = kk/rayon2;
   
   //position1.ajoute(N.produit(-d1));// on remet la bille n�1 � l'ext�rieur de la bille n�2 avec une translation propotionnelle � 1/r1
   //position2.ajoute(N.produit(d2)); // on remet la bille n�2 � l'ext�rieur de la bille n�1 avec une translation propotionnelle � 1/r2
   
   //System.err.println("choc mou entre 2 billes: N = " + N +", d1 = " + d1 +", d2 = " + d2);
   return CHOC_MOU;
   }
else        // � pr�sent, a[0] >= EPSILON_CHOC_BILLE et nG1G2 < r, le choc est donc dynamique
   { 
   double m1 = masse1;
   double m2 = masse2;
   M[0] = m1+m2;
    
   double alfa = (m1-m2)/M[0];
   double deuxSurM = 2/M[0];
    
   v1Np[0] =          alfa * v1N + deuxSurM *m2 * v2N; 
   v2Np[0] = deuxSurM * m1 * v1N -         alfa * v2N;
   
   //intensit�Choc.add( a[0]);
   //intensit�Choc.add( new Choc( position1.somme(N.produit(rayon1)), a[0]));
   this.setChanged();
   this.notifyObservers(new Choc( position1.somme(N.produit(rayon1)), a[0]));
   return CHOC_DYNAMIQUE;
   }

} //actionReactionBilleBilleTousRebonds


/**
 * g�re la collision dynamique ou statique des 2 billes d�finies respectivement par 
 * ( position1, rayon1, vitesse1, acc�l�ration1, masse1 ) et par ( position2, rayon2, vitesse2, acc�l�ration1, masse2)
 * 
 * si il n'y a pas de collision
 * renvoie false et ne modifie rien
 *
 * si il y a collision statique :
 * renvoie true et modifie  acc�l�ration1 et acc�l�ration2. (une force de rappel �lastique est appliqu�e aux deux billes). 
 * Ajoute une contribution aux 2 vecteurs acc�l�ration.
 * 
 * si il y a collision dynamique :
 * renvoie true  et calcule les nouveaux vecteurs vitesse  vitesse1 et vitesse2 imm�diatement apr�s le choc
 *  Dans ce cas, envoie aussi � un �ventuel Observer un objet Choc (position du choc, intensit� du choc) pour g�rer les sons d'impact
 * 
 * Les nouvelles acc�l�rations ou les nouveaux vecteurs vitesses sont calcul�s pour repr�senter l'�tat des billes imm�diatement apr�s le choc  
 * 
 * */

public static boolean actionReactionBilleBille( Vecteur position1, double rayon1, Vecteur vitesse1, Vecteur acc�l�ration1, double masse1, 
                                                Vecteur position2, double rayon2, Vecteur vitesse2, Vecteur acc�l�ration2, double masse2)   
//modifie b1 et b2
//donn�es : b1 et b2 avant le choc
//r�sultats : b1 et b2 apr�s le choc
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
int ok = Collisions1.collisions1.actionReactionBilleBilleTousRebonds( position1, rayon1, vitesse1, w1, acc�l�ration1, masse1, 
                                              position2, rayon2, vitesse2, w2, acc�l�ration2, masse2, 
                                                 nG1G2, a, base, v1T, v2T, deltavT, M, v1Np, v2Np);

if (ok == PAS_DE_CHOC) return false;
else
   if (ok == CHOC_MOU) return true;
   else
      { 
      Vecteur v1S = Vecteur.combinaisonLin�aire(v1Np[0], base[0], v1T[0], base[1]);
      Vecteur v2S = Vecteur.combinaisonLin�aire(v2Np[0], base[0], v2T[0], base[1]);
   
      vitesse1.set(v1S);                  // vecteur vitesse de la bille 1 apr�s le choc
      vitesse2.set(v2S);                  // vecteur vitesse de la bille 2 apr�s le choc
    
      return true;
      }
   
} // actionReactionBilleBille



/**
 * g�re la collision dynamique ou statique des 2 billes b1 et b 2 d�finies respectivement par 
 * ( position1, rayon1, vitesse1, w1, acc�l�ration1, m1, J1, J1plusM1r12 ) et par 
 * ( position2, rayon2, vitesse2, w2, acc�l�ration2, m2, J2, J2plusM2r22)
 * 
 * Les billes tournent sur elles-m�mes !
 * w1 est la vitesse angulaire
 * J1 est le moment d'inertie
 * J1plusM1r12 = J1 + m1*r1^2
 * 
 * m�me chose pour b2
 * 
 * si il n'y a pas de collision
 * renvoie false et ne modifie rien
 *
 * si il y a collision statique :
 * renvoie true et modifie  acc�l�ration1 et acc�l�ration2. (une force de rappel �lastique est appliqu�e aux deux billes). 
 * Ajoute une contribution aux 2 vecteurs acc�l�ration.
 * 
 * si il y a collision dynamique :
 * renvoie true  et calcule les nouveaux vecteurs vitesse  vitesse1 et vitesse2 et les nouvelles vitesses angulaires w1 et w2 
 * imm�diatement apr�s le choc
 *  Dans ce cas, envoie aussi � un �ventuel Observer un objet Choc (position du choc, intensit� du choc) pour g�rer les sons d'impact
 * 
 * Les nouvelles acc�l�rations ou les nouveaux vecteurs vitesses et vitesses angulaires w1 et w2 sont calcul�s
 *  pour repr�senter l'�tat des billes imm�diatement apr�s le choc
 *  
 * @param coeffFrottement : frottement de Coulomb entre 2 billes  tel que coeffFrottement >= 0.
 * Si coeffFrottement == 0, les billes glissent parfaitement l'une sur l'autre, ni les vitesses tangentielles v1T, v2T,
 *  ni les vitesses angulaires w1 et w2  ne sont modifi�es
 * Plus coeffFrottement augmente, plus on s'approche des conditions de roulement sans glisement. 
 * On conseille de fixer le coeff entre 0 et 1 pour des conditions r�alistes.
 * 
 * */

public static boolean actionReactionBilleBilleAvecEffet( 
       Vecteur position1, double rayon1, Vecteur vitesse1, double w1[], Vecteur acc�l�ration1, double m1, final double J1, final double J1plusM1r12,
       Vecteur position2, double rayon2, Vecteur vitesse2, double w2[], Vecteur acc�l�ration2, double m2, final double J2, final double J2plusM2r22,
                                                 final double coeffFrottement)   
//modifie b1 et b2
//donn�es : b1 et b2 avant le choc
//r�sultats : b1 et b2 apr�s le choc
{
                            ////System.err.println("actionReactionBilleBilleAvecEffet, d�but ");

double [] nG1G2 = new double[1];
double [] a = new double[1];
Vecteur [] base = new Vecteur[2];
double [] v1T = new double[1];
double [] v2T = new double[1];
double [] deltavT = new double[1];
double [] M = new double[1];
double [] v1Np = new double[1];
double [] v2Np = new double[1];
int ok = Collisions1.collisions1.actionReactionBilleBilleTousRebonds( position1, rayon1, vitesse1, w1, acc�l�ration1, m1, 
                                              position2, rayon2, vitesse2, w2, acc�l�ration2, m2, 
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
         
         double x = 2*beta*(-a[0])/M[0];        // x = 2 * beta * (vN2 - vN1)/m    o� m = m1+m2
         
         double xm1m2 = x*m1m2;
          
         v1T[0] += x*m2;
         w1[0]  += xm1m2*r1SurJ1;
         v2T[0] -= x*m1;
         w2[0]  -= xm1m2*r2SurJ2;
         }                          // roulement pur
      
      Vecteur v1S = Vecteur.combinaisonLin�aire(v1Np[0], base[0], v1T[0], base[1]);
      Vecteur v2S = Vecteur.combinaisonLin�aire(v2Np[0], base[0], v2T[0], base[1]);
   
      vitesse1.set(v1S);                  // vecteur vitesse de la bille 1 apr�s le choc
      vitesse2.set(v2S);                  // vecteur vitesse de la bille 2 apr�s le choc
    
      return true;
      }                     // choc dynamique

} // actionReactionBilleBilleAvecEffet

} // Collisions1



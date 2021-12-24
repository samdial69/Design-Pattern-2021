package exodecorateur_angryballs.maladroit.modele;

import java.awt.Color;
import java.util.Vector;

import mesmaths.cinematique.Collisions;
import mesmaths.geometrie.base.Vecteur;
import mesmaths.mecanique.MecaniquePoint;

/**
 * 
 * Bille s'arrêtant sur les bords et subissant l'attraction des autres billes
 * 
 * 
 *  A MODIFIER
 *  
 *  */

public class BilleMvtNewtonArret extends Bille
{


public BilleMvtNewtonArret(Vecteur position, double rayon, Vecteur vitesse,
        Color couleur)
{
super(position, rayon, vitesse, couleur);
}

/* (non-Javadoc)
 * @see decorateur_angryballs.modele.Bille#gestionAccélération(java.util.Vector)
 */
@Override
public void gestionAccélération(Vector<Bille> billes)
{
super.gestionAccélération(billes);                              // remise à zéro du vecteur accélération
this.getAccélération().ajoute(OutilsBille.gestionAccélérationNewton(this, billes));     // contribution de l'accélération due à l'attraction des autres billes
}

@Override
public void collisionContour(double abscisseCoinHautGauche,
        double ordonnéeCoinHautGauche, double largeur, double hauteur)
{
Collisions.collisionBilleContourAvecArretHorizontal(this.getPosition(), this.getRayon(), this.getVitesse(), abscisseCoinHautGauche, largeur);
Collisions.collisionBilleContourAvecArretVertical(this.getPosition(), this.getRayon(), this.getVitesse(), ordonnéeCoinHautGauche, hauteur);
}

}

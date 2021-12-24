package exodecorateur_angryballs.maladroit.modele;

import mesmaths.cinematique.Collisions;

public class BilleBloquerParUnBord extends DecorateurBille{

    public BilleBloquerParUnBord(Bille bille) {
        super(bille);
    }

    @Override
    public void collisionContour(double abscisseCoinHautGauche, double ordonneeCoinHautGauche, double largeur, double hauteur) {
        Collisions.collisionBilleContourAvecArretHorizontal(this.getPosition(),this.getRayon(),this.getVitesse(),abscisseCoinHautGauche,largeur);
        Collisions.collisionBilleContourAvecArretVertical(this.getPosition(),this.getRayon(),this.getVitesse(),ordonneeCoinHautGauche,hauteur);
    }
}

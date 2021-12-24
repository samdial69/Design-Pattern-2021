package exodecorateur_angryballs.maladroit.modele;

import mesmaths.cinematique.Collisions;

public class BilleRebondSurBord extends DecorateurBille{
    public BilleRebondSurBord(Bille bille) {
        super(bille);
    }

    @Override
    public void collisionContour(double abscisseCoinHautGauche, double ordonneeCoinHautGauche, double largeur, double hauteur) {
        billeADecorer.collisionContour(abscisseCoinHautGauche,ordonneeCoinHautGauche,largeur,hauteur);
        Collisions.collisionBilleContourAvecRebond(this.getPosition(),this.getRayon(),this.getVitesse(),
                abscisseCoinHautGauche,ordonneeCoinHautGauche,largeur,hauteur);
    }
}

package exodecorateur_angryballs.maladroit.modele;

import mesmaths.cinematique.Collisions;

public class BillePasseMuraille extends DecorateurBille{
    public BillePasseMuraille(Bille bille) {
        super(bille);
    }

    @Override
    public void collisionContour(double abscisseCoinHautGauche, double ordonneeCoinHautGauche, double largeur, double hauteur) {
        Collisions.collisionBilleContourPasseMuraille(this.getPosition(),abscisseCoinHautGauche,
                ordonneeCoinHautGauche,largeur,hauteur);
    }
}

package exodecorateur_angryballs.maladroit.modele;

import java.util.Vector;

public abstract class BilleAvecAcceleration extends DecorateurBille{

    public BilleAvecAcceleration(Bille billeADecorer) {
        super(billeADecorer);
    }

    @Override
    public void collisionContour(double abscisseCoinHautGauche, double ordonneeCoinHautGauche, double largeur, double hauteur) {
        billeADecorer.collisionContour(abscisseCoinHautGauche,ordonneeCoinHautGauche,largeur,hauteur);
    }

    @Override
    public void gestionAcceleration(Vector<Bille> billes) {
       this.billeADecorer.gestionAcceleration(billes);
       this.contributionAcceleration(billes);
    }

    public abstract void contributionAcceleration(Vector<Bille> billes);
}

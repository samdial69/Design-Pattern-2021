package exodecorateur_angryballs.maladroit.modele;

import java.awt.*;
import java.util.Vector;

import mesmaths.cinematique.Cinematique;
import mesmaths.cinematique.Collisions;
import mesmaths.geometrie.base.Geop;
import mesmaths.geometrie.base.Vecteur;


/**
 * Cas général d'une bille de billard
 * 
 *  A MODIFIER
 *  
 * 
 * */
public abstract class Bille {
    //----------------- classe Bille-------------------------------------
    private static int prochaineClef = 0;

    public static double ro = 1;        // masse volumique

    public abstract Vecteur getPosition();

    public abstract double getRayon();

    public abstract Vecteur getVitesse();

    public abstract Vecteur getAcceleration();

    public abstract Color getCouleur();

    public abstract int getClef();

    public double masse() {
        return ro * Geop.volumeSphère(this.getRayon());
    }

    public void dessine(Graphics g) {
        int width, height;
        int xMin, yMin;

        xMin = (int) Math.round(this.getPosition().x - this.getRayon());
        yMin = (int) Math.round(this.getPosition().y - this.getRayon());

        width = height = 2 * (int) Math.round(this.getRayon());

        g.setColor(this.getCouleur());
        g.fillOval(xMin, yMin, width, height);
        g.setColor(Color.CYAN);
        g.drawOval(xMin, yMin, width, height);
    }

    /**
     * mise à jour de position et vitesse à t+deltaT à partir de position et vitesse à l'instant t
     * modifie le vecteur position et le vecteur vitesse
     * laisse le vecteur accélération intact
     * La bille subit par défaut un mouvement uniformément accéléré
     */
    public void deplacer(double deltaT) {
        Cinematique.mouvementUniformementAccelere(this.getPosition(), this.getVitesse(), this.getAcceleration(), deltaT);
    }

    /**
     * calcul (c-à-d mise à jour) éventuel  du vecteur accélération.
     * billes est la liste de toutes les billes en mouvement
     * Cette méthode peut avoir besoin de "billes" si this subit l'attraction gravitationnelle des autres billes
     * La nature du calcul du vecteur accélération de la bille  est définie dans les classes dérivées
     * A ce niveau le vecteur accélération est mis à zéro (c'est à dire pas d'accélération)
     */
    public void gestionAcceleration(Vector<Bille> billes) {
        this.getAcceleration().set(Vecteur.VECTEURNUL);
    }

    /**
     * gestion de l'éventuelle collision de la bille (this) avec le contour rectangulaire de l'écran défini par (abscisseCoinHautGauche, ordonnéeCoinHautGauche, largeur, hauteur)
     * détecte si il y a collision et le cas échéant met à jour position et vitesse
     * La nature du comportement de la bille en réponse à cette collision est définie dans les classes dérivées
     */
    public abstract void collisionContour(double abscisseCoinHautGauche, double ordonneeCoinHautGauche, double largeur, double hauteur);


    public String toString() {
        return "centre = " + this.getPosition() + " rayon = " + this.getRayon() + " vitesse = " + this.getVitesse() + " accélération = " + this.getAcceleration() + " couleur = " + this.getCouleur() + "clef = " + this.getClef();

    }

    /**
     * gestion de l'éventuelle  collision de cette bille avec les autres billes
     * billes est la liste de toutes les billes en mouvement
     * Le comportement par défaut est le choc parfaitement élastique (c-à-d rebond sans amortissement)
     * @return true
     * si il y a collision et dans ce cas les positions et vecteurs vitesses des 2 billes impliquées dans le choc sont modifiées
     * si renvoie false, il n'y a pas de collision et les billes sont laissées intactes
     */
    public boolean gestionCollisionBilleBille(Vector<Bille> billes) {
        return OutilsBille.gestionCollisionBilleBille(this, billes);
    }
}


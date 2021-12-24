package exodecorateur_angryballs.maladroit.modele;

import java.awt.*;
import java.util.Vector;

import mesmaths.cinematique.Cinematique;
import mesmaths.cinematique.Collisions;
import mesmaths.geometrie.base.Geop;
import mesmaths.geometrie.base.Vecteur;


/**
 * Cas g�n�ral d'une bille de billard
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
        return ro * Geop.volumeSph�re(this.getRayon());
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
     * mise � jour de position et vitesse � t+deltaT � partir de position et vitesse � l'instant t
     * modifie le vecteur position et le vecteur vitesse
     * laisse le vecteur acc�l�ration intact
     * La bille subit par d�faut un mouvement uniform�ment acc�l�r�
     */
    public void deplacer(double deltaT) {
        Cinematique.mouvementUniformementAccelere(this.getPosition(), this.getVitesse(), this.getAcceleration(), deltaT);
    }

    /**
     * calcul (c-�-d mise � jour) �ventuel  du vecteur acc�l�ration.
     * billes est la liste de toutes les billes en mouvement
     * Cette m�thode peut avoir besoin de "billes" si this subit l'attraction gravitationnelle des autres billes
     * La nature du calcul du vecteur acc�l�ration de la bille  est d�finie dans les classes d�riv�es
     * A ce niveau le vecteur acc�l�ration est mis � z�ro (c'est � dire pas d'acc�l�ration)
     */
    public void gestionAcceleration(Vector<Bille> billes) {
        this.getAcceleration().set(Vecteur.VECTEURNUL);
    }

    /**
     * gestion de l'�ventuelle collision de la bille (this) avec le contour rectangulaire de l'�cran d�fini par (abscisseCoinHautGauche, ordonn�eCoinHautGauche, largeur, hauteur)
     * d�tecte si il y a collision et le cas �ch�ant met � jour position et vitesse
     * La nature du comportement de la bille en r�ponse � cette collision est d�finie dans les classes d�riv�es
     */
    public abstract void collisionContour(double abscisseCoinHautGauche, double ordonneeCoinHautGauche, double largeur, double hauteur);


    public String toString() {
        return "centre = " + this.getPosition() + " rayon = " + this.getRayon() + " vitesse = " + this.getVitesse() + " acc�l�ration = " + this.getAcceleration() + " couleur = " + this.getCouleur() + "clef = " + this.getClef();

    }

    /**
     * gestion de l'�ventuelle  collision de cette bille avec les autres billes
     * billes est la liste de toutes les billes en mouvement
     * Le comportement par d�faut est le choc parfaitement �lastique (c-�-d rebond sans amortissement)
     * @return true
     * si il y a collision et dans ce cas les positions et vecteurs vitesses des 2 billes impliqu�es dans le choc sont modifi�es
     * si renvoie false, il n'y a pas de collision et les billes sont laiss�es intactes
     */
    public boolean gestionCollisionBilleBille(Vector<Bille> billes) {
        return OutilsBille.gestionCollisionBilleBille(this, billes);
    }
}


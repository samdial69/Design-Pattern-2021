package exodecorateur_angryballs.maladroit.modele;

import exodecorateur_angryballs.maladroit.modele.visitor.VisitorDessine;
import mesmaths.geometrie.base.Vecteur;

import java.awt.*;

public class BilleNormale extends Bille {
    //----------------- classe Bille-------------------------------------

    public Vecteur position;   // centre de la bille
    public  double rayon;            // rayon > 0
    public  Vecteur vitesse;
    public  Vecteur acceleration;
    public int clef;                // identifiant unique de cette bille
    private Color couleur;

    private static int prochaineClef = 0;

    /**
     * @param centre
     * @param rayon
     * @param vitesse
     * @param acceleration
     * @param couleur
     */
    protected BilleNormale(Vecteur centre, double rayon, Vecteur vitesse,
                    Vecteur acceleration, Color couleur)
    {
        this.position = centre;
        this.rayon = rayon;
        this.vitesse = vitesse;
        this.acceleration = acceleration;
        this.couleur = couleur;
        this.clef = BilleNormale.prochaineClef ++;
    }

    /**
     * @param position
     * @param rayon
     * @param vitesse
     * @param couleur
     */
    public BilleNormale(Vecteur position, double rayon, Vecteur vitesse, Color couleur)
    {
        this(position,rayon,vitesse,new Vecteur(),couleur);
    }

    /**
     * @return the position
     */
    public Vecteur getPosition(){return this.position;}

    /**
     * @return the rayon
     */
    public double getRayon(){return this.rayon;}

    /**
     * @return the vitesse
     */
    public Vecteur getVitesse() { return this.vitesse; }

    /**
     * @return the acceleration
     */
    public Vecteur getAcceleration() { return this.acceleration;}

    /**
     * @return the clef
     */
    public int getClef(){ return this.clef;  }

    @Override
    public void accept(VisitorDessine visitorDessine) {
        visitorDessine.visite(this);
    }

    public Color getCouleur() { return couleur; }

    //TODO à faire quand on saura le comportement que doit avoir une bille normale comme collisionContour
    @Override
    public void collisionContour(double abscisseCoinHautGauche, double ordonneeCoinHautGauche, double largeur, double hauteur) {

    }
}

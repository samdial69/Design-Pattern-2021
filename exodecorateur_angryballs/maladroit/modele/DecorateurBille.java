package exodecorateur_angryballs.maladroit.modele;

import mesmaths.geometrie.base.Vecteur;

import java.awt.*;

public abstract class DecorateurBille extends Bille {
    protected Bille billeADecorer;

    public DecorateurBille(Bille billeADecorer) {
        this.billeADecorer = billeADecorer;
    }

    @Override
    public Vecteur getPosition() {
        return this.billeADecorer.getPosition();
    }

    @Override
    public double getRayon() {
        return this.billeADecorer.getRayon();
    }

    @Override
    public Vecteur getVitesse() {
        return this.billeADecorer.getVitesse();
    }

    @Override
    public Vecteur getAcceleration() {
        return this.billeADecorer.getAcceleration();
    }

    @Override
    public Color getCouleur() {
        return this.billeADecorer.getCouleur();
    }

    @Override
    public int getClef() {
        return this.billeADecorer.getClef();
    }

    public Bille getBilleADecorer() {
        return billeADecorer;
    }

    public void setBilleADecorer(Bille billeADecorer) {
        this.billeADecorer = billeADecorer;
    }
}

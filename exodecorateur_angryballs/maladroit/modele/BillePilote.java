package exodecorateur_angryballs.maladroit.modele;

import mesmaths.geometrie.base.Vecteur;

import java.util.Vector;

public class BillePilote extends BilleAvecAcceleration{
    private Vecteur force;


    public BillePilote(Bille billeADecorer, Vecteur force) {
        super(billeADecorer);
        this.force = force;
    }

    @Override
    public void contributionAcceleration(Vector<Bille> billes) {
        this.getAcceleration().ajoute(force);
    }

    public Vecteur getForce() {
        return force;
    }

    public void setForce(Vecteur force) {
        this.force = force;
    }
}

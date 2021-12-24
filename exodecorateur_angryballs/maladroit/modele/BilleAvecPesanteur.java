package exodecorateur_angryballs.maladroit.modele;

import mesmaths.geometrie.base.Vecteur;

import java.util.Vector;

public class BilleAvecPesanteur extends BilleAvecAcceleration{
    Vecteur pesanteur;
    public BilleAvecPesanteur(Bille bille,Vecteur pesanteur) {
        super(bille);
        this.pesanteur = pesanteur;
    }

    @Override
    public void contributionAcceleration(Vector<Bille> billes) {
        this.getAcceleration().ajoute(this.pesanteur);
    }
}

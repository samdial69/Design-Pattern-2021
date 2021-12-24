package exodecorateur_angryballs.maladroit.modele;

import mesmaths.mecanique.MecaniquePoint;

import java.util.Vector;

public class BilleAvecFrottementVisqueux extends BilleAvecAcceleration{
    public BilleAvecFrottementVisqueux(Bille bille) {
        super(bille);
    }

    @Override
    public void contributionAcceleration(Vector<Bille> billes) {
        this.getAcceleration().ajoute(MecaniquePoint.freinageFrottement(this.billeADecorer.masse(),this.getVitesse()));
    }
}

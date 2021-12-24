package exodecorateur_angryballs.maladroit.modele;

import java.util.Vector;

public class BilleAttirerParLesAutres extends BilleAvecAcceleration{
    public BilleAttirerParLesAutres(Bille bille) {
        super(bille);
    }

    @Override
    public void contributionAcceleration(Vector<Bille> billes) {
        this.getAcceleration().ajoute(OutilsBille.gestionAccelerationNewton(this.billeADecorer,billes)); // contribution de l'acceleration due à l'attraction des autres billes
    }
}

package exodecorateur_angryballs.maladroit.modele;

import java.util.Vector;

public class BilleAbsenceAcceleration extends BilleAvecAcceleration{
    public BilleAbsenceAcceleration(Bille billeADecorer) {
        super(billeADecorer);
    }

    @Override
    public void contributionAcceleration(Vector<Bille> billes) {
        //Cette bille ne possède pas de vitesse du coup elle ne contribue à l'accélération des autres billes
    }
}

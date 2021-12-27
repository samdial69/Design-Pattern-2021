package exodecorateur_angryballs.maladroit.modele.visitor;

import exodecorateur_angryballs.maladroit.modele.BilleNormale;
import exodecorateur_angryballs.maladroit.modele.DecorateurBille;

public interface Visitor {
    public void visite(BilleNormale bille);
    public void visite(DecorateurBille decorateurBille);
}

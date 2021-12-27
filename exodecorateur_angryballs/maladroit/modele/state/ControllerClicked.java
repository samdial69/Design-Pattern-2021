package exodecorateur_angryballs.maladroit.modele.state;

import exodecorateur_angryballs.maladroit.modele.Bille;
import exodecorateur_angryballs.maladroit.modele.BillePilote;
import mesmaths.geometrie.base.Geop;
import mesmaths.geometrie.base.Vecteur;

import java.awt.event.MouseEvent;

public class ControllerClicked extends StateController{

    public ControllerClicked(StateController nextController, StateManager manager) {
        super(nextController, manager);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        for ( Bille bille: manager.billes) {
            Vecteur positionSouris = new Vecteur(event.getX(),event.getY());
            if (Geop.appartientDisque(positionSouris,bille.getPosition(),bille.getRayon())){
                this.billeAPiloter = new BillePilote(bille,Vecteur.VECTEURNUL);//Ajout du comportement bille pilotée
                manager.billes.remove(bille);
                manager.billes.add(this.billeAPiloter);
                manager.setCurrentController(this.nextController);
                return;//A tester si on sort de la boucle
            }
        }
    }
}

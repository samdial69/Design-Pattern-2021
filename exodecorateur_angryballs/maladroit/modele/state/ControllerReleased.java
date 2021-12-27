package exodecorateur_angryballs.maladroit.modele.state;

import exodecorateur_angryballs.maladroit.modele.Bille;
import mesmaths.geometrie.base.Vecteur;

import java.awt.event.MouseEvent;

public class ControllerReleased extends StateController{

    private static final double SENSIBILITY = 10;

    public ControllerReleased(StateController nextController, StateManager manager) {
        super(nextController, manager);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        Vecteur positionSouris = new Vecteur(event.getX(),event.getY());
        Vecteur force = positionSouris.difference(this.nextController.billeAPiloter.getPosition()); //distance entre l souris et la bille pilotée
        force.multiplie(1/this.nextController.billeAPiloter.masse());
        force.multiplie(SENSIBILITY);
        this.nextController.billeAPiloter.setForce(force);

    }

    @Override
    public void mouseReleased(MouseEvent event) {
        Bille billeSuivante = this.nextController.billeAPiloter.getBilleADecorer();//on récupère l'ancienne décoration de la bille
        manager.billes.remove(this.nextController.billeAPiloter); //on supprime la bille décorer BilleAPiloter = enlever le comportement à décorer
        manager.billes.add(billeSuivante);  // on ajoute la bille decorée de notre billePilotée dans la liste des billes

        this.manager.setCurrentController(this.nextController); // la bille est relachée on passe à l'état initial

    }
}

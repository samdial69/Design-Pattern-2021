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
        Vecteur force = positionSouris.difference(this.nextController.billeAPiloter.getPosition()); //distance entre l souris et la bille pilot�e
        force.multiplie(1/this.nextController.billeAPiloter.masse());
        force.multiplie(SENSIBILITY);
        this.nextController.billeAPiloter.setForce(force);

    }

    @Override
    public void mouseReleased(MouseEvent event) {
        Bille billeSuivante = this.nextController.billeAPiloter.getBilleADecorer();//on r�cup�re l'ancienne d�coration de la bille
        manager.billes.remove(this.nextController.billeAPiloter); //on supprime la bille d�corer BilleAPiloter = enlever le comportement � d�corer
        manager.billes.add(billeSuivante);  // on ajoute la bille decor�e de notre billePilot�e dans la liste des billes

        this.manager.setCurrentController(this.nextController); // la bille est relach�e on passe � l'�tat initial

    }
}

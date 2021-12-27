package exodecorateur_angryballs.maladroit.modele.state;

import exodecorateur_angryballs.maladroit.modele.BillePilote;

import java.awt.event.MouseEvent;

public class StateController {
    protected StateController nextController; //Lien vers le prochain controller
    protected StateManager manager; //le contexte

    protected BillePilote billeAPiloter;

    public StateController(StateController nextController, StateManager manager) {
        this.nextController = nextController;
        this.manager = manager;
    }

    public void mousePressed(MouseEvent event){}
    public void mouseDragged(MouseEvent event){}
    public void mouseReleased(MouseEvent event){}


}

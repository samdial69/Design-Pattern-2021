package exodecorateur_angryballs.maladroit.modele.state;

import exodecorateur_angryballs.maladroit.modele.Bille;
import exodecorateur_angryballs.maladroit.modele.BillePilote;
import exodecorateur_angryballs.maladroit.vues.Billard;
import mesmaths.geometrie.base.Vecteur;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

public class StateManager implements MouseListener, MouseMotionListener {
    private Billard billard;
    protected Vector<Bille> billes;

    private StateController currentController; //Etat courant
    private ControllerClicked controllerClicked; //Etat du click(attraper) = en attente d'etre attraper
    private ControllerReleased controllerReleased; //Etat du click(relâché) = en attente d'etre libre

    public StateManager(Billard billard, Vector<Bille> billes) {
        this.billard = billard;
        this.billes = billes;

        this.billard.addMouseListener(this);
        this.billard.addMouseMotionListener(this);

        this.initializeController();
    }

    private void initializeController() { //Fonction qui initialise les états de transition
        this.controllerClicked = new ControllerClicked(null,this);
        this.controllerReleased = new ControllerReleased(this.controllerClicked,this);//A revoir

        this.controllerClicked.nextController = controllerReleased;
        this.controllerReleased.nextController = controllerClicked;//A revoir

        setCurrentController(this.controllerClicked);


    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.currentController.mouseDragged(e);
    }
    @Override
    public void mousePressed(MouseEvent e) {
        this.currentController.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.currentController.mouseReleased(e);
    }


    public StateController getCurrentController() {
        return currentController;
    }

    public void setCurrentController(StateController currentController) {
        this.currentController = currentController;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}//Rien à faire

    @Override
    public void mouseExited(MouseEvent e) {}//Rien à faire

    @Override
    public void mouseMoved(MouseEvent e) {}//Rien à faire

    @Override
    public void mouseClicked(MouseEvent e) {}//Rien à faire
}

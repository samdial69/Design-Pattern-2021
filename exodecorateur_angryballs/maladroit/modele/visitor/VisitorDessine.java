package exodecorateur_angryballs.maladroit.modele.visitor;

import exodecorateur_angryballs.maladroit.modele.BilleNormale;
import exodecorateur_angryballs.maladroit.modele.DecorateurBille;

import java.awt.*;

public class VisitorDessine implements Visitor{
    public Graphics graphics;
    @Override
    public void visite(BilleNormale bille) {
        int width, height;
        int xMin, yMin;

        xMin = (int) Math.round(bille.getPosition().x - bille.getRayon());
        yMin = (int) Math.round(bille.getPosition().y - bille.getRayon());

        width = height = 2 * (int) Math.round(bille.getRayon());

        graphics.setColor(bille.getCouleur());
        graphics.fillOval(xMin, yMin, width, height);
        graphics.setColor(Color.CYAN);
        graphics.drawOval(xMin, yMin, width, height);

    }

    @Override
    public void visite(DecorateurBille decorateurBille) {
        decorateurBille.getBilleADecorer().accept(this);
    }
}

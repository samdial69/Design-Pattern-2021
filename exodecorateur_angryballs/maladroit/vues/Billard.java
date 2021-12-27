package exodecorateur_angryballs.maladroit.vues;

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.Vector;

import exodecorateur_angryballs.maladroit.modele.Bille;
import exodecorateur_angryballs.maladroit.modele.visitor.VisitorDessine;


/**
 * responsable du dessin des billes
 * 
 *  ICI : IL N'Y A RIEN A CHANGER 
 *  
 * 
 * */
public class Billard extends Canvas
{
    Vector<Bille> billes;
    VisitorDessine visitorDessine = new VisitorDessine();

    public Billard(Vector<Bille> billes)
    {
this.billes = billes;
    }
    /* (non-Javadoc)
     * @see java.awt.Canvas#paint(java.awt.Graphics)
     */

    public void paint()
    {
        if (this.getBufferStrategy() == null){
            try{
                createBufferStrategy(2);
            }catch (Exception exception){
                return;
            }
        }
        Graphics graphics = this.getBufferStrategy().getDrawGraphics();
        graphics.clearRect(0,0,this.getWidth(),this.getHeight());
        int i;

        this.visitorDessine.graphics = graphics;
    
        for ( i = 0; i < this.billes.size(); ++i)
            this.billes.get(i).accept(visitorDessine);
    
        this.getBufferStrategy().show();
        graphics.dispose();
    }

    
 
}

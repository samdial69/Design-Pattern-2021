package exodecorateur_angryballs.maladroit;

import java.util.Observable;
import java.util.Vector;

import exodecorateur_angryballs.maladroit.modele.Bille;
import exodecorateur_angryballs.maladroit.vues.CadreAngryBalls;
import exodecorateur_angryballs.maladroit.vues.VueBillard;

/**
 * responsable de l'animation des billes, c-�-d responsable du mouvement de la liste des billes. met perp�tuellement � jour les billes. 
 * g�re le d�lai entre 2 mises � jour (deltaT) et pr�vient la vue responsable du dessin des billes qu'il faut mettre � jour la sc�ne
 * 
 * ICI : IL N'Y A RIEN A CHANGER
 * */
public class AnimationBilles extends Observable implements Runnable
{


Vector<Bille> billes;   // la liste de toutes les billes en mouvement 
CadreAngryBalls cadreAngryBalls;    // la vue responsable du dessin des billes du billard
private Thread thread;    // pour lancer et arr�ter les billes


private static final double COEFF = 0.5;

/**
 * @param billes
 * @param cadreAngryBalls
 */
public AnimationBilles(Vector<Bille> billes, CadreAngryBalls cadreAngryBalls)
{
this.billes = billes;
this.cadreAngryBalls = cadreAngryBalls;
this.thread = null;     //est-ce utile ?

    /* Observable mise en place */
    EcouteurBoutonLancer �couteurBoutonLancer = new EcouteurBoutonLancer(this);
    EcouteurBoutonArreter �couteurBoutonArr�ter = new EcouteurBoutonArreter(this);

    this.cadreAngryBalls.lancerBilles.addActionListener(�couteurBoutonLancer);             // pourrait �tre remplac� par Observable - Observer
    this.cadreAngryBalls.arr�terBilles.addActionListener(�couteurBoutonArr�ter);           // pourrait �tre remplac� par Observable - Observer
}

@Override
public void run()
{
try
    {
    double deltaT;  // d�lai entre 2 mises � jour de la liste des billes
    Bille billeCourante;
    
    double minRayons = AnimationBilles.minRayons(billes);   //n�cessaire au calcul de deltaT
    double minRayons2 = minRayons*minRayons;                //n�cessaire au calcul de deltaT
    
    while (!Thread.interrupted())                           // gestion du mouvement
        {
        //deltaT = COEFF*minRayons2/(1+maxVitessesCarr�es(billes));       // mise � jour deltaT. L'addition + 1 est une astuce pour �viter les divisions par z�ro
        
                                                                        //System.err.println("deltaT = " + deltaT);
        deltaT = 10;
        
        int i;
        for ( i = 0; i < billes.size(); ++i)    // mise � jour de la liste des billes
            {
            billeCourante = billes.get(i);
            billeCourante.deplacer(deltaT);                 // mise � jour position et vitesse de cette bille
            billeCourante.gestionAcceleration(billes);      // calcul de l'acc�l�ration subie par cette bille
            billeCourante.gestionCollisionBilleBille(billes);
            billeCourante.collisionContour( 0, 0, cadreAngryBalls.largeurBillard(), cadreAngryBalls.hauteurBillard());        //System.err.println("billes = " + billes);
            }

        // on pr�vient la vue qu'il faut redessiner les billes
        this.setChanged();
        this.notifyObservers(this);
      
       
        Thread.sleep((int)deltaT);                          // deltaT peut �tre consid�r� comme le d�lai entre 2 flashes d'un stroboscope qui �clairerait la sc�ne
        }
    }

catch (InterruptedException e)
    {
    /* arr�t normal, il n'y a rien � faire dans ce cas */
    }

}

/**
 * calcule le maximum de de la norme carr�e (pour faire moins de calcul) des vecteurs vitesse de la liste de billes
 * 
 * */
static double maxVitessesCarr�es(Vector<Bille> billes)
{
double vitesse2Max = 0;

int i;
double vitesse2Courante;

for ( i = 0; i < billes.size(); ++i)
    if ( (vitesse2Courante = billes.get(i).getVitesse().normeCarr�e()) > vitesse2Max)
       vitesse2Max = vitesse2Courante; 

return vitesse2Max;
}

/**
 * calcule le minimum  des rayons de a liste des billes
 * 
 * 
 * */
static double minRayons(Vector<Bille> billes)
{
double rayonMin, rayonCourant;

rayonMin = Double.MAX_VALUE;

int i;
for ( i = 0; i < billes.size(); ++i)
    if ( ( rayonCourant = billes.get(i).getRayon()) < rayonMin)
       rayonMin = rayonCourant;

return rayonMin;
}


public void lancerAnimation()
{
if (this.thread == null)
    { 
    this.thread = new Thread(this);
    thread.start();
    }
}

public void arr�terAnimation()
{
if (thread != null)
    {
    this.thread.interrupt();
    this.thread = null;
    }
}
}

package mesmaths.geometrie.base.brouillon;

import mesmaths.geometrie.base.Geop;
import mesmaths.geometrie.base.Vecteur;

public class Segment
{
//----------------- classe Segment-------------------------------------

private Vecteur debut, fin;

/**
 * @param debut
 * @param fin
 */
public Segment(Vecteur debut, Vecteur fin)
{
this.debut = debut;
this.fin = fin;
}


public Vecteur[] base()   // fournit une base orthornormée directe associée au segment orienté
                // base[0] est un vecteur unitaire colineaire et 
                // de meme sens que le vecteur debut->fin
    {
    Vecteur u = fin.difference(debut);
    return u.base();
    }


public Vecteur reflechi(Vecteur v) // calcule le vecteur reflechi de v par rapport a this
    {
    Vecteur I,J;
    Vecteur[] t = this.base();
    
    I = t[0]; J = t[1];
    
    return Geop.reflechi( v, I, J); 
    }

//----------------- classe Segment-------------------------------------
}

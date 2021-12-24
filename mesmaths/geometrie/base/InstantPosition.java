package mesmaths.geometrie.base;

import java.io.Serializable;

/**
 * Définit un triplet (t,x(t),y(t)) représentant la position à un instant t d'un point mobile dans le plan
 * 
 * */
public class InstantPosition implements Serializable
{
public double instant; // instant t
public Vecteur position; // position sur la courbe à l'instant t. cf. trajectoire
/**
 * @param instant
 * @param position
 */
public InstantPosition(double instant, Vecteur position)
{
this.instant = instant;
this.position = position;
}
/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString()
{
return "(" + this.instant + ", "
        + this.position + ")";
}



}

package mesmaths.cinematique;

import mesmaths.geometrie.base.Vecteur;

/**
 * représente un choc. Défini par une position et une intensité.
 * Cette classe a pour but de diffuser un son occasionné par un choc
 * */
public class Choc
{
Vecteur position;
double intensité; // quantité > 0

/**
 * @param position
 * @param intensité
 */
public Choc(Vecteur position, double intensité)
{
this.position = position;
this.intensité = intensité;
}

@Override
public String toString()
{
return "Choc [position=" + this.position + ", intensité=" + this.intensité
        + "]";
}

public Vecteur getPosition()
{
return this.position;
}

public double getIntensité()
{
return this.intensité;
}


}

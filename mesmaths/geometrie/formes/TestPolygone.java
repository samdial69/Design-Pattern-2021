package mesmaths.geometrie.formes;

import mesmaths.geometrie.base.Matrice;
import mesmaths.geometrie.base.TransformationAffine;
import mesmaths.geometrie.base.Vecteur;

public class TestPolygone
{

public static void main(String[] args)
{
Vecteur t[] = {new Vecteur(1,0),new Vecteur(1,1),new Vecteur(0,1)};

Polygone p = new Polygone(t);

System.out.println("p = " + p);

p.dessine(new DessinateurMock());

Vecteur déplacement = new Vecteur(4,3);
TransformationAffine translation = new TransformationAffine(new Matrice(-3,2,7,-2), déplacement);

System.out.println("translation = \n"+ translation);

p.applique(translation);

System.out.println("p = " + p);

}

}

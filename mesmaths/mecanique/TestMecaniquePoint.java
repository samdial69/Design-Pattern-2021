package mesmaths.mecanique;

import mesmaths.geometrie.base.Vecteur;

public class TestMecaniquePoint
{

/**
 * @param args
 */
public static void main(String[] args)
{
Vecteur v = new Vecteur(-40,88);

Vecteur f = MecaniquePoint.frottementVisqueux(v);

System.out.println("v = " + v +  " f = " + f);


}

}

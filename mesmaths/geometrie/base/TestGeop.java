package mesmaths.geometrie.base;

public class TestGeop
{

/**
 * @param args
 */
public static void main(String[] args)
{
Vecteur P0 = new Vecteur(3,2);
Vecteur P1 = new Vecteur(0,-1);
Vecteur v1 = new Vecteur(0,10);
Vecteur t [] = Geop.base(P0, P1);
Vecteur I = t[0];
Vecteur J = t[1];
Vecteur v2 = Geop.reflechi(v1, I, J);
System.out.println("v1 = " + v1);
System.out.println("v2 = " + v2);

}

}

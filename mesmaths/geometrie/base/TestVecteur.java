package mesmaths.geometrie.base;

public class TestVecteur
{

/**
 * @param args
 */
public static void main(String[] args)
{
Vecteur v1 = new Vecteur(3,7);
Vecteur v2 = new Vecteur(7,3);
Vecteur v3 = v1.somme(v2);
System.out.println("v1 = " + v1 +" v2 = "+ v2 + " v3 = "+ v3);

Vecteur v4 = new Vecteur(-4,-4);
Vecteur t [] = v4.base();
Vecteur v5 = new Vecteur(0,8);
Vecteur v6 = Geop.reflechi(v5, t[0], t[1]);

System.out.println("v4 = " + v4);
System.out.println("t[0] = " + t[0]);
System.out.println("t[1] = " + t[1]);
System.out.println("v5 = " + v5);
System.out.println("v6 = " + v6);
{
StringBuffer s = new StringBuffer("(-1.5,2.5) (12.5,-32.5) (-0.25,-1.5)blabla");
Vecteur v7 = new Vecteur(s);

System.out.println("v7 = " + v7);

Vecteur v8 = new Vecteur(s);

System.out.println("v8 = " + v8);

Vecteur v9 = new Vecteur(s);

System.out.println("v9 = " + v9);

System.out.println("s = " + s);
}

}

}

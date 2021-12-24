package mesmaths.solveur;

public class TestSolveur
{

public static void main(String[] args)
{
double x1, x2, x3;

System.out.println("On résout l'équation (x-1)*(x-2)*(x-3) = 0, qui a 3 solutions x1 = 1, x2 = 2 et x3 = 3 \n");

Courbe c1 = new Equation1();            //  Permet de résoudre l'équation (x-1)*(x-2)*(x-3) = 0, qui a 3 solutions x1 = 1, x2 = 2 et x3 = 3

x1 = Solveur.résout(c1, 0, 1.5);          // recherche de la solution sur l'intervalle [0,1.5], doit trouver x1 = 1

System.out.println("x1 = " + x1);

x2 = Solveur.résout(c1, 1.5, 2.5);          // recherche de la solution sur l'intervalle [1.5,2.5], doit trouver x2 = 2

System.out.println("x2 = " + x2);

x3 = Solveur.résout(c1, 2.5, 10);          // recherche de la solution sur l'intervalle [2.5,10], doit trouver x3 = 3

System.out.println("x3 = " + x3 + "\n");


System.out.println("On résout l'équation ln(x) - (x-5)^2 = 0, qui a 2 solutions x1 et x2 avec x1 < 5 et x2 > 5 \n");

Courbe c2 = new Equation2();            // Permet de résoudre l'équation ln(x) - (x-5)^2 = 0, qui a 2 solutions x1 et x2 avec x1 < 5 et x2 > 5

x1 = Solveur.résout(c2, 0.1, 4.9);      // recherche de la solution sur l'intervalle [0.1,4.9], doit trouver x1

System.out.println("x1 = " + x1);

x2 = Solveur.résout(c2, 4.9, 10);      // recherche de la solution sur l'intervalle [4.9,10], doit trouver x2

System.out.println("x2 = " + x2 + "\n");

System.out.println("On résout l'équation (x-1)*(x-2)*(x-3) + 1 = 0, qui a une unique solution x1 telle que x1 < 1 \n");

Courbe c3 = new Equation3();            // Permet de résoudre l'équation (x-1)*(x-2)*(x-3) + 1 = 0, qui a une unique solution x1 telle que x1 < 1

x1 = Solveur.résout(c3, 0, 10);      // recherche de la solution sur l'intervalle [0,10], doit trouver x1

System.out.println("x1 = " + x1);

try
    {
    x2 = Solveur.résout(c3, 10, 11);      // recherche de la solution sur l'intervalle [10,11], doit échouer
    
    System.out.println("x2 = " + x2);
    }
catch (Exception e)
    {
    // c'est tout à fait normal qu'on arrive ici
    System.out.println(e + "\n");
    }


System.out.println("Pour a = 2/3, b = 1/2 et c = 1/5, on résout l'équation a^x + b^x + c^x = 1, qui a une unique solution x1 telle que 1 < x1 < 2 \n");

double a = 2/3.0, b = 0.5, c = 0.2;

Courbe c4 = new EquationFractale(a,b,c);            // Permet de résoudre l'équation a^x + b^x + c^x = 1, qui a une unique solution x1 telle que 1 < x1 < 2

x1 = Solveur.résout(c4, 0, 5);      // recherche de la solution sur l'intervalle [0,5], doit trouver x1

System.out.println("x1 = " + x1);
}

}

package mesmaths.solveur;

/**
 * Permet de résoudre l'équation (x-1)*(x-2)*(x-3) + 1 = 0, qui a une unique solution x1 telle que x1 < 1
 * 
 * 
 * */
public class Equation3 extends Courbe
{

@Override
public double évalue(double x)
{
return (x-1)*(x-2)*(x-3) + 1;
}

}

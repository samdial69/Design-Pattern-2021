package mesmaths.solveur;

/**
 * 
 * Permet de résoudre l'équation (x-1)*(x-2)*(x-3) = 0
 * 
 * */
public class Equation1 extends Courbe
{

@Override
public double évalue(double x)
{
return (x-1)*(x-2)*(x-3);
}

@Override
public double évalueDérivée(double x)
{
//return super.évalueDérivée(x);
return 11 + x*(-12 + 3*x);                           // l'algo de Solveur fonctionne aussi si cette classe ne fournit pas ce calcul mais il sera moins précis et plus lent
}



}

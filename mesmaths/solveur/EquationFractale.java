package mesmaths.solveur;

/**
 * Permet de résoudre l'équation a^x + b^x + c^x = 1
 * 
 * avec a, b et c réels strictement positifs 
 * 
 * */
public class EquationFractale extends Courbe
{
double a, b, c;
double lna, lnb, lnc; // ln est la fonction logarithme népérien ln


/**
 * @param a > 0
 * @param b > 0
 * @param c > 0
 */
public EquationFractale(double a, double b, double c)
{
this.a = a; this.lna = Math.log(a);
this.b = b; this.lnb = Math.log(b);
this.c = c; this.lnc = Math.log(c);
}



@Override
public double évalue(double x)
{
return Math.pow(a, x) + Math.pow(b, x) + Math.pow(c, x) - 1; // calcule a^x + b^x + c^x - 1
}



@Override
public double évalueDérivée(double x)
{
return lna * Math.pow(a, x) + lnb * Math.pow(b, x) + lnc * Math.pow(c, x);
}

}

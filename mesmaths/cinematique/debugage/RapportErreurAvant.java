package mesmaths.cinematique.debugage;

/**
 * Pour aider la mise au point des calculs de collision bille-arc avec effet
 * 
 * 
 * */
public class RapportErreurAvant implements RapportErreur
{
double[] vNp; double[] vT; double[] w; double[] vC;

public RapportErreurAvant(double[] vNp, double[] vT, double[] w, double[] vC)
{
super();
this.vNp = vNp;
this.vT = vT;
this.w = w;
this.vC = vC;
}

@Override
public String toString()
{
return "RapportErreurAvant [vNp = " + this.vNp[0] + ", vT = "
        + this.vT[0] + ", w = " + this.w[0] + ", vC = "
        + this.vC[0] + "]";
}

}

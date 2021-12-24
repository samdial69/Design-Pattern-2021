package complexes;
import java.io.*;
/**
 * 
 * représente un nombre complexe z sous forme cartésienne z = x + y*i
 * 
 * utilisée pour tester les junits
 * 
 * dans l'ordre : écrire d'abord les tests PUIS implémenter les méthodes
 * 
 * */
public class Complexe implements Serializable
{
/**
 * le nombre complexe 0 + 0*i
 */
public static final Complexe zéro = new Complexe(0,0);

/**
 * le nombre complexe 1 + 0*i
 * 
 * */
public static final Complexe un = new Complexe(1,0);

/**
 * le nombre imaginaire pur i tel que i^2 = -1
 * 
 * */
public static final Complexe i = new Complexe(0,1);

private double x,y;// z = x + y*i

/**
 * crée 0+0*i
 * */
public Complexe(){this.x = this.y = 0;}

/**
 * crée x+0*i
 * @param x : partie réelle
 * */
public Complexe(double x){this.x = x; this.y = 0;}
/**
 * 
 * crée x+y*i
 * 
 * @param x : partie réelle
 * @param y : partie imaginaire
 * 
 * */
public Complexe(double x, double y){this.x=x; this.y=y;}


/**
 * 
 * crée x+y*i
 * 
 * @param string respectant le format de toString
 *  * 
 * */
public Complexe(String string)
{
int p;
String debut, reste;

reste = string;

p = reste.indexOf('+');
debut = reste.substring(0, p);

this.x = Double.parseDouble(debut.trim());

reste = reste.substring(p+1);
p = reste.indexOf('*');
debut = reste.substring(0, p);
this.y = Double.parseDouble(debut.trim());
}

/**
 * @return la partie réelle de this
 * 
 * */
public double getX() {return x;}

/**
 * @return la partie imaginaire de this
 * 
 * */
public double getY() {return y;}

/**
 * Calcule this + z
 * 
 * @return  this + z
 * 
 * */
public Complexe somme(Complexe z) {return new Complexe( this.x+z.x,this.y+z.y);}





/**
 * 
 * calcule this-z
 * @return this - z
 * 
 * */
public Complexe différence(Complexe z) {return this.somme(z.opposé());}

/**
 * 
 * calcule this*z en effectuant la multiplication au sens des nombres complexes
 * @return this*z
 * 
 * */
public Complexe produit(Complexe z) {return new Complexe( this.x*z.x - this.y*z.y, this.x*z.y + this.y*z.x );}

/**
 * 
 * calcule this*a
 * @return this *a
 * */
public Complexe produit(double a) {return new Complexe( a*this.x, a*this.y);}

/**
 * 
 * calcule -this
 * @return -this
 * */
public Complexe opposé() {return new Complexe(-this.x,-this.y);}

/**
 * 
 * calcule 1/this
 * @return 1/this
 * */
public Complexe inverse() {return this.conjugué().produit(1/this.module2());}


/**
 * 
 * calcule le complexe conjugué de this
 * rappel : si this = x + yi alors this* = x - yi
 * 
 * @return le nombre complexe conjugué de this
 * 
 * */
public Complexe conjugué() {return new Complexe( this.x,-this.y);}


/**
 * 
 * calcule |this| (norme euclidienne)
 * 
 * @return la norme euclidienne de this (appelée aussi module de this)
 * 
 * */
public double module() {return Math.sqrt(this.module2());}


/**
 * @return la norme sup de this. Rappel : norme sup de x + y*i = max(|x|,|y|)
 * 
 * 
 * */
public double moduleInfini() {return Math.max( Math.abs(this.x), Math.abs(this.y));}

/**
 * @return arg(this) dans l'intervalle ]-PI,+PI]
 * */
public double arg() {return Math.atan2(this.y, this.x);}


/**
 * @return |this|^2 (c'est-à dire x^2 +y^2)
 * 
 * */
public double module2() {return this.x*this.x + this.y*this.y;}

/**
 * 
 * calcule les n racines n-ièmes de this
 * 
 * rappel : si z = R * e^(i * theta) alors z a n racines n-èmes Z0, ..., Zn-1 
 * 
 * telles que Zk = r * e^(i (alfa + k*2* PI/n)) pour 0 <= k <= n-1
 * 
 * où r = R^(1/n) et alfa = theta/n
 * 
 * on a aussi Zk+1 = Zk * e^(2*i*PI/n)
 * 
 * */
public Complexe[] racines(int n)
{
Complexe [] rac = new Complexe[n];
double R, r, theta, alfa;
Complexe u;
int k;

theta = this.arg();
R = this.module();

r = Math.pow(R, 1./n);
alfa = theta/n;

u = Complexe.créePolaire(1,2*Math.PI/n);

for ( k = 1, rac[0] = Complexe.créePolaire(r,alfa); k < n; ++k)
    rac[k] = rac[k-1].produit(u);

return rac;
}

/**
 * 
 * @return  le nombre complexe Z = r * e^(i * theta)
 * 
 * */
public static Complexe créePolaire(double r, double theta)
{

return new Complexe(r*Math.cos(theta),r*Math.sin(theta));
}

public boolean equals(Object o)
{
if (!(o instanceof Complexe))
    return false;
else
    {
    Complexe z = (Complexe)o;
    double d, eps = 1.E-6;
    d = this.différence(z).moduleInfini();
    return d < eps;
    }
}


/**
 * @return la string composée de la partie réelle et de la partie imaginaire suivant le format classique en maths
 * 
 * Exemple pour this = (3,-5), la string retournée est "3 + -5 * i"
 * 
 * */
public String toString() 
{
return Double.toString(this.x)+"+"+Double.toString(this.y)+"*i";
}
} //Complexe

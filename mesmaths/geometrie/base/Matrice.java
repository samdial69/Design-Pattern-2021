package mesmaths.geometrie.base;

/**
 * représente une matrice carrée à 2 lignes et 2 colonnes dont les éléments sont des réels
 * 
 * Elle est notée (m11 m12
 *                 m21 m22)
 * 
 * Convention : 1er indice = indice de ligne et 2ème indice = indice de colonne
 * */
public class Matrice
{
double m11, m12, m21, m22;

/**
 * matrice identité, c'est-à-dire matrice diagonale telle que m11 == m22 == 1 et m12 == m21 == 0
 * 
 * 
 * */
public static Matrice IDENTITE = Matrice.créeMatriceDiagonale(1, 1);

/**
 * @param m11
 * @param m12
 * @param m21
 * @param m22
 * 
 * tels que m = |m11 m12|
 *              |m21 m22|
 */
public Matrice( double m11, double m12, 
                double m21, double m22)
{
this.m11 = m11;
this.m12 = m12;
this.m21 = m21;
this.m22 = m22;
}

/**
 * @param angle en radians
 * */
public static Matrice créeRotation(double angle)
{
double costeta, sinteta;
costeta = Math.cos(angle);
sinteta = Math.sin(angle);
return new Matrice( costeta,-sinteta,
                    sinteta, costeta);

}

public static Matrice créeMatriceDiagonale(double m11, double m22)
{
return new Matrice( m11,  0,
                      0,m22);
}

public Matrice inverse()
{
double det = m11*m22-m21*m12;
return new Matrice( m22/det,-m12/det ,
                    -m21/det,m11/det);
}

public Vecteur produit(Vecteur u)
{
return new Vecteur( m11*u.x+m12*u.y,
                    m21*u.x+m22*u.y);
}

public Matrice produit(Matrice a)
{
return new Matrice( m11*a.m11 + m12*a.m21,m11*a.m12+m12*a.m22,
                    m21*a.m11+m22*a.m21,m21*a.m12+m22*a.m22);
}

public String toString()
{
return Double.toString(m11)+" "+m12 +"\n"+m21+" "+m22;
}

public double getM11()
{
return this.m11;
}

}

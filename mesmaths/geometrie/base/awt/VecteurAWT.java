package mesmaths.geometrie.base.awt;

import java.awt.Point;
import java.awt.Polygon;

import mesmaths.geometrie.base.Vecteur;

public class VecteurAWT extends Vecteur
{

/**
 * 
 */
public VecteurAWT()
{
super();
// TODO Auto-generated constructor stub
}

/**
 * @param x
 * @param y
 */
public VecteurAWT(double x, double y)
{
super(x, y);
// TODO Auto-generated constructor stub
}

/**
 * @param string
 */
public VecteurAWT(String string)
{
super(string);
// TODO Auto-generated constructor stub
}

/**
 * @param v
 */
public VecteurAWT(Vecteur v)
{
super(v);
// TODO Auto-generated constructor stub
}

public Point toPoint()
{
return new Point((int)x,(int)y); 
}



public static Polygon toPolygon( Vecteur [] tv)
{
int tx[];
int ty[];
int l = tv.length;

tx = new int[l];
ty = new int[l];

int i;
for ( i = 0; i < l; ++i)
    {
    tx[i] = (int)(tv[i].x);
    ty[i] = (int)(tv[i].y);
    }

return new Polygon(tx,ty,l);
}

}

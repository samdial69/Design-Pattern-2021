package mesmaths.geometrie.awt;

import java.awt.Graphics;

import mesmaths.geometrie.base.Vecteur;
import mesmaths.geometrie.formes.Dessinateur;

public class DessinateurAWT /*implements*/ extends Dessinateur
{
Graphics graphics;


/**
 * @param graphics
 */
public DessinateurAWT(Graphics graphics)
{
this.graphics = graphics;
}


@Override
public void dessineSegment(Vecteur v1, Vecteur v2)
{
graphics.drawLine( (int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y);
}

}

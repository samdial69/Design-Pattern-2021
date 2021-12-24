package mesmaths.geometrie.formes;

import mesmaths.geometrie.base.Vecteur;

public class DessinateurMock /*implements*/ extends Dessinateur
{

@Override
public void dessineSegment(Vecteur v1, Vecteur v2)
{
System.out.println( "["+v1+", " + v2 +"]");
}

}

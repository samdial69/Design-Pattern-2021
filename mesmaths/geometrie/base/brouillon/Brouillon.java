package mesmaths.geometrie.base.brouillon;

public class Brouillon
{
private double x;
private double y;




/**
 * @param string respectant le format " ( nombre réel , nombre réel )"
 * c'est le même format que celui du résultat de toString()
 * Accepte un nombre quelconque d'espaces de part et d'autre des symboles '('  et ',' et  ')'
 *
 * efface dans string tout caractère analysé avec succès
 *
 * */
public void Vecteur(StringBuffer string)
{
int p0 = string.indexOf("(");

if (p0 < 0) throw new NumberFormatException("dans Vecteur(StringBuffer) : \"(\" attendue");

int p1 = string.indexOf(",");
int p2 = string.indexOf(")");
String sX, sY;

sX = string.substring(1+p0, p1).trim();
sY = string.substring(1+p1, p2).trim();

this.x = Double.parseDouble(sX);
this.y = Double.parseDouble(sY);
string.delete(0, p2+1);
}





}

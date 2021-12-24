package mesmaths.mecanique;

import mesmaths.geometrie.base.Geop;
import mesmaths.geometrie.base.Vecteur;



/**
 * gestion des opérations de mécanique du point matériel
 * 
 * */
public class MecaniquePoint
{
/**
 * constante G de la gravitation universelle
 * 
 * */
public static double G = 0.0002;     // constante G de la gravitation universelle

/**
 * coefficient de frottement visqueux (dans l'air)
 * 
 * */
public static double COEFFICIENTFROTTEMENT = 4.5; // coefficient de frottement visqueux (dans l'air)

/**
 * 
 * calcule la force de frottement visqueux proportionnelle au vecteur vitesse v et au coefficient de frottement coefficientFrottement >= 0
 * 
 * Un frottement visqueux est un frottement proportionnel à la vitesse comme le frottement dans l'air par exemple.
 * 
 * @return la force de frottement
 * */
private static Vecteur frottementVisqueux(Vecteur v, double coefficientFrottement)
{
return v.produit(-coefficientFrottement);
}

/**
 * 
 * calcule la force de frottement visqueux proportionnelle au vecteur vitesse v et au coefficient de frottement COEFFICIENTFROTTEMENT
 * 
 * Un frottement visqueux est un frottement proportionnel à la vitesse comme le frottement dans l'air par exemple.
 * 
 * @return la force de frottement
 * */
protected static Vecteur frottementVisqueux(Vecteur v)
{
return MecaniquePoint.frottementVisqueux(v, COEFFICIENTFROTTEMENT);
}

/**
 * 
 * calcule l'accélération (en fait freinage dans le langage courant) due à la force de frottement visqueux proportionnelle au vecteur vitesse v et au coefficient de frottement COEFFICIENTFROTTEMENT
 * 
 * Un frottement visqueux est un frottement proportionnel à la vitesse comme le frottement dans l'air par exemple (et cela freine l'objet en mouvement)
 * 
 * Ce freinage est calculé pour un objet de masse "masse" et de vecteur vitesse "vitesse"
 * @param masse : masse de l'objet subissant le frottement
 * @param vitesse : vecteur vitesse de l'objet subissant le frottement
 * @return l'accélération due au  frottement
 * */
public  static Vecteur freinageFrottement( double masse, Vecteur vitesse)
{
Vecteur forceFrottement = MecaniquePoint.frottementVisqueux(vitesse);
return forceFrottement.produit(1/masse);
}

/**
 * 
 * calcule l'accélération (en fait freinage dans le langage courant) due à la force de frottement visqueux proportionnelle 
 * au vecteur vitesse v et au coefficient de frottement "coefficientFrottement"
 * 
 * Un frottement visqueux est un frottement proportionnel à la vitesse comme le frottement dans l'air par exemple (et cela freine l'objet en mouvement)
 * 
 * Ce freinage est calculé pour un objet de masse "masse" et de vecteur vitesse "vitesse"
 * @param masse : masse de l'objet subissant le frottement
 * @param vitesse : vecteur vitesse de l'objet subissant le frottement
 * @param coefficientFrottement : tel que coefficientFrottement >= 0, si coefficientFrottement == 0, il n'y a pas de frottement
 * @return l'accélération due au  frottement
 * */
public  static Vecteur freinageFrottement( double masse, Vecteur vitesse, double coefficientFrottement)
{
Vecteur forceFrottement = MecaniquePoint.frottementVisqueux(vitesse, coefficientFrottement);
return forceFrottement.produit(1/masse);
}

/**
 *  calcule et renvoie le champ de gravite au point P du à la masse m placée en C 
ne tient pas compte de la constante de gravitation G 
*/
private static Vecteur champGravité(Vecteur P, double m, Vecteur C) 
{
Vecteur PC;  /* le vecteur P-->C */
double PC2; /* |PC|² */
double coef;

PC = Vecteur.difference( C, P);
PC2 = Vecteur.produitScalaire(PC,PC);

coef = m/(PC2*Math.sqrt(PC2));
return PC.produit(coef);
} // champGravité


/**
 * calcule et renvoie le champ de gravité au point P dues aux masses masses[0]...masses[d-1] placées respectivement aux points C[0]...C[d-1]
 * 
 * constanteGravitationUniverselle est la constante de gravitation universelle  prévue par la loi de gravitation universelle de Newton
 * 
 * constanteGravitationUniverselle > 0
 * 
 * On suppose que taille de masses[] == taille de C[]
 * */
private static  Vecteur champGravitéGlobal(Vecteur P, double masses[], Vecteur C[], double constanteGravitationUniverselle)
{
int d = masses.length;

int i;

Vecteur g = new Vecteur();

for( i = 0; i <d; ++i)
   g.ajoute(champGravité(P, masses[i], C[i])); 

return g.produit(constanteGravitationUniverselle);
}

/**
 * calcule et renvoie le champ de gravité au point P dues aux masses masses[0]...masses[d-1] placées respectivement aux points C[0]...C[d-1]
 * 
 * utilise la constante de gravitation universelle  (prévue par la loi de gravitation universelle de Newton) définie dans cette classe
 * 
 * On suppose que taille de masses[] == taille de C[] == d
 * 
 * @return le vecteur accélération en P  due à l'attraction gravitationnelle de toutes les masses masses[i] placées en C[i] pour i = 0...d-1   
 * */
public static  Vecteur champGravitéGlobal(Vecteur P, double masses[], Vecteur C[])
{
return champGravitéGlobal( P,  masses,  C, MecaniquePoint.G);
}
}

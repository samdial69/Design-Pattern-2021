package mesmaths.cinematique;

import mesmaths.geometrie.base.Vecteur;

public class Cinematique
{
/**
 * suppose qu'un objet mobile subit un mouvement rectiligne uniforme (c'à-d un déplacement en ligne droite à vitesse constante)
 * 
 * le mouvement de l'objet est à tout instant défini par le vecteur position et le vecteur vitesse.
 * 
 *  DONNEES : position et vitesse à l'instant t, deltaT (deltaT est le délai entre 2 mises à jour, deltaT est supposé petit) 
 * 
 * RESULTATS : position à l'instant t + deltaT
 * 
 * vitesse est laissé inchangé par la méthode
 * 
 * 
 * Le calcul peut être noté comme suit :
 * 
 * p(t+deltaT) = p(t) + deltaT * v(t)
 * v(t+deltaT) = v(t)
 * 
 * */
private static void mouvementRectiligneUniforme(Vecteur position, Vecteur vitesse, double deltaT)
{
position.ajoute(vitesse.produit(deltaT));   
}

/**
 * suppose qu'un objet mobile subit un mouvement uniformément accéléré entre t et t+deltaT (c'à-d un déplacement comme la chute libre)
 * 
 * le mouvement de l'objet est à tout instant défini par le vecteur position, le vecteur vitesse et le vecteur accélération
 * 
 *  DONNEES : position, vitesse et accélération à l'instant t, deltaT (deltaT est le délai entre 2 mises à jour, deltaT est supposé petit) 
 * 
 * RESULTATS : position et  vitesse  à l'instant t + deltaT. 
 * 
 * accélération reste inchangé par la méthode. Il faudra le recalculer  l'instant t+deltaT avec une autre méthode 
 * 
 * 
 * * Les calculs peuvent être notés comme suit :
 * 
 * p(t+deltaT) = p(t) + deltaT * v(t) + (1/2) * deltaT^2 * a(t)
 * v(t+deltaT) = v(t) + deltaT * a(t)
 * a(t + deltaT) = a(t)
 * 
 * @param position : vecteur position du mobile à l'instant t. ce paramètre est modifié par la méthode : en sortie il représente la position à l'instant t+deltaT 
 * @param vitesse : vecteur vitesse du mobile à l'instant t. ce paramètre est modifié par la méthode : en sortie il représente la vitesse à l'instant t+deltaT
 * @param accélération : vecteur accélération du mobile à l'instant t
 * @param deltaT : une durée très courte
 * 
 * */
public static void mouvementUniformémentAccéléré(Vecteur position, Vecteur vitesse, Vecteur accélération, double deltaT)
{
mouvementRectiligneUniforme( position, vitesse, deltaT);
position.ajoute(accélération.produit(0.5*deltaT*deltaT));
mouvementRectiligneUniforme(vitesse, accélération, deltaT);
//accélération.set(Vecteur.VECTEURNUL);
}
}

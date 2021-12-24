
#include <jni.h>
#include "mesmaths_cinematique_Collisions.h"
#include <Reels.h>
#include <OutilsTableaux.h>
#include <Vecteur2D.h>
#include <Newton.h>
#include <CalculsCollisionBille1.h>

using namespace std;
/*
 Header for class mesmaths_cinematique_Collisions

#ifndef _Included_mesmaths_cinematique_Collisions
#define _Included_mesmaths_cinematique_Collisions
#ifdef __cplusplus
extern "C" {
#endif
#undef mesmaths_cinematique_Collisions_EPSILON_CHOC_BILLE
#define mesmaths_cinematique_Collisions_EPSILON_CHOC_BILLE 0.01
#undef mesmaths_cinematique_Collisions_EPSILON_CHOC_PAROI
#define mesmaths_cinematique_Collisions_EPSILON_CHOC_PAROI 0.01
#undef mesmaths_cinematique_Collisions_PAS_DE_CHOC
#define mesmaths_cinematique_Collisions_PAS_DE_CHOC 0L
#undef mesmaths_cinematique_Collisions_CHOC_DYNAMIQUE
#define mesmaths_cinematique_Collisions_CHOC_DYNAMIQUE 1L
#undef mesmaths_cinematique_Collisions_CHOC_STATIQUE
#define mesmaths_cinematique_Collisions_CHOC_STATIQUE 2L
*/
/*
 * Class:     mesmaths_cinematique_Collisions
 * Method:    actionReactionBilleBilleAvecEffet
 * Signature: ([DDDDDDDD)V
 */

/*
 * Class:     mesmaths_cinematique_Collisions
 * Method:    actionReactionBilleBilleAvecEffet
 * Signature: ([DDDDDDDD)V
 */
JNIEXPORT void JNICALL Java_mesmaths_cinematique_Collisions_actionReactionBilleBilleAvecEffet
  (JNIEnv * e, jclass, jdoubleArray vitesses , jdouble rayon1, jdouble masse1, jdouble J1, jdouble rayon2, jdouble masse2, jdouble J2, jdouble G1G2)
{
                                                            cerr<<"début méthode native"<<endl;
jboolean isCopyDonnees;
double * d = e->GetDoubleArrayElements( vitesses, &isCopyDonnees);              // pour récupérer les données du tableau JAVA "vitesses"

double v1N = d[0], v1T = d[1], w1 = d[2], v2N = d[3], v2T = d[4], w2 = d[5];
// vitesses contient, dans l'ordre, v1N', v1T', w1', v2N', v2T', w2'

CalculsCollisionBille1 calculsCollisionBille( v1N,  v1T,  w1,  rayon1,  masse1,  J1,
    v2N,  v2T,  w2,  rayon2,  masse2,  J2,
                        G1G2);

                                                  cerr<<"méthode native, avant Newton"<<endl;
/*CalculsCollisionBille1( const double & v1N, const double & v1T, const double & w1, const double & rayon1, const double & masse1, const double & J1,
                       const double & v2N, const double & v2T, const double & w2, const double & rayon2, const double & masse2, const double & J2,
                       const double & G1G2);*/

double tXo []= { -v1N, v1T, w1, -v2N, v2T, w2};

Vecteur<double,6> Xo( tXo, 5);

Intervalle grandR(-INFINITY,INFINITY);
Intervalle t1[6];
copie1( grandR, t1,5); //T * copie1(const T & a, T q[], int m)
Vecteur<Intervalle,6> domaine1(t1,5);

int ok;
Vecteur<double,6> r0, r1;

ok = Newton::pointFixe(Xo, domaine1, &calculsCollisionBille, r0, r1);
                                                                        cerr<<"méthode native, après  Newton"<<endl;
e->SetDoubleArrayRegion( vitesses, 0,  6,  r1.getT());       // on remplit le tableau JAVA "resultats" avec les données du tableau C++

if (ok == Newton::SUCCES)
  cerr << "Solution = " << r1 << endl;
else
  if (ok >= 0)
      cerr << "frontière franchie : ok = " << ok << ", r0 = " << r0 << ", r1 = " << r1 << endl;
  else
      cerr << "échec: "<< ok << endl;

}


/* Collisions.actionReactionBilleBilleAvecEffet( vitesses, rayon1, masse1, J1, rayon2, masse2, J2, coeffFrottement); // appel méthode native */



#création de la dll
CC=g++
LINK = $(CC)
INCLUDE1 = -IF:\Dom\Recherche\courbes\intervalles -IF:\Dom\Recherche\courbes\vecteurs -IF:\Dom\Recherche\courbes\reels -IF:\Dom\Recherche\courbes\chainescar -IF:\Dom\Recherche\courbes\algebre -IF:\Dom\Recherche\courbes\entiers -IF:\Dom\Recherche\courbes\listes -IF:\Dom\Recherche\courbes\outils  -IF:\Dom\Recherche\courbes\gradients -IF:\Dom\Recherche\courbes\matrices -I.
INCLUDE2 = -IF:\Dom\Recherche\courbes\bsplinesbrep -IF:\Dom\Recherche\courbes\interpolation -IF:\Dom\Recherche\courbes\maths -IF:\Dom\Recherche\courbes\newtonraphson -IF:\Dom\Recherche\courbes\BSplines 
INCLUDE3 = -IC:\Java\jdk1.7.0_05\include -IC:\Java\jdk1.7.0_05\include\win32 -IF:\Dom\Recherche\courbes\collisionsbilles

OBJ1 = F:\Dom\Recherche\courbes\reels\Reels.o F:\Dom\Recherche\courbes\chainescar\ChainesCar.o F:\Dom\Recherche\courbes\intervalles\Intervalle.o F:\Dom\Recherche\courbes\entiers\Entiers.o F:\Dom\Recherche\courbes\maths\Maths.o F:\Dom\Recherche\courbes\newtonraphson\Newton.o
OBJ2 = F:\Dom\Recherche\courbes\vecteurs\Vecteur2D.o
all: mesmaths_cinematique_Collisions.o 
	$(LINK) $(OBJ1)  $(OBJ2) mesmaths_cinematique_Collisions.o -shared -o CalculsCompliquesCollisions.dll mesmaths_cinematique_Collisions.def
mesmaths_cinematique_Collisions.o :
	$(CC) $(INCLUDE1) $(INCLUDE2) $(INCLUDE3) -c -Wall -std=c++11 mesmaths_cinematique_Collisions.cpp
	
	
	
	
#g++  proxys_ProxyJNI1.o -shared -o ClasseNative1.dll proxys_ProxyJNI1.def
	   
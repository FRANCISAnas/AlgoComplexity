# AlgoComplexity

Anas FRANCIS, Lucas DOMINGUEZ, Thibaut ESTEVE, Angele BADIA

JAVA : 
Lancement rapide (en utilisant que le jar) :
	java -jar Simulation.jar ExempleProf.txt
	cliquer sur init
	cliquer droit sur un sommet rouge et Delete Red

	Pour recommencer cliquer sur clear et à nouveau init.

Construire un graphe :
    Première ligne : nombre de sommets N
    N lignes suivantes : couleurs de chaque sommet BLUE ou RED, ils sont numérotés automatiquement de 0 à N-1 dans l'ordre donné
    N lignes suivantes :
        Couleur de l'arête sortante du sommet N (BLUE ou RED) puis ' ' puis  numéro du sommet pointé
        continuer en ajoutant ', ' et les autres voisins
        (si il y a 0 voisins mettre 'none')


Pour ajouter une Simulation il suffit de faire une nouvelle classe StrategyN qui implemente Strategy.

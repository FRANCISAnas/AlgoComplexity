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


Pour ajouter une Simulation il suffit de faire une nouvelle classe StrategyN qui implemente Strategy.(N<100 ^^)



Lancement manuel avec le code
Installation :
    1- Dézipper les fichiers sources et txt qqpart
    2- Faire new project dans intellj
    3- copier les fichiers dans le dossier du projet créé (au même endroit que le src)

Pour charger un graphe :
    1- Click droit sur Main
    2- Cliquer sur "Edit Main.main()"
    3- Dans Program arguments écrire :  ExempleProf.txt par exemple
    4- Lancer le main

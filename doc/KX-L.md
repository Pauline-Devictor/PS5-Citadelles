
# Knowledge Exchange - Groupe L
---

## Architecture du Projet
Le projet est divisé en 4 parties principales : 
 - Le Game Engine et l'affichage, qui s'occupe de la gestion du Jeu et de l'affichage du déroulement de la partie
 - Le package [strategies][packStrat] qui gère les comportements des joueurs en fonction de leur stratégie
 - Le package [characters][packChar] qui implémente les personnages et leurs effets
 - Le package [buildings][packBuild] qui implémente les bâtiments et leurs effets 

#### Game Engine et Classes utiles
Le package suivant construit le game engine, crée une partie en initialisant les joueurs, la pioche et la banque. Il s'occupe également de la mise en forme et de l'affichage.

##### _Game_
La classe _Game_ est à proprement parler le game engine de ce projet. Elle crée un plateau et des joueurs, et possède les méthodes pour jouer une partie.
##### _Banque_
La classe _Bank_ gère tous les aspects du jeu, lié aux pièces en s'assurant que l'or dans le jeu est toujours égal à 30 pièces.
##### _Board_
La classe _Board_ contient la liste des joueurs et des personnages de la partie. Cette classe est notamment utilisée pour l'affichage de la partie, du choix des cartes jusqu'à l'affichage des résultats.
##### _Deck de Cartes_
La classe _Deck_ permet de créer un deck avec les cartes nécessaires au jeu, et à gérer la pioche et la défausse des cartes.

#### Joueurs et leurs stratégies

Dans ce package se trouve une classe par Strategie, toutes étendues de la classe _Player_ qui définit les comportements par défaut. Chaque classe héritée définit donc un comportement de jeu différent, avec sa propre stratégie : 

| Classe | Strategie | Roles |
| ------ | ------ | ------ |
| HighScoreArchi | Cherche à poser des bâtiments onéreux | Préfère l'Architecte, pour pouvoir piocher
| HighScoreThief | Cherche à poser des bâtiments onéreux | Prefere le Voleur, pour voler l'or des Joueurs 
| RushArchi | Cherche à finir la partie au plus vite | Prefere l'Architecte, pour poser beaucoup de bâtiments
| RushMerch | Cherche a finir la partie au plus vite | Prefere le marchand, pour accumuler de l'or en vu de construire

### Bâtiments et Effets liés

Les bâtiments sont tous définis dans _BuildingEnum_, avec leur nom, leur coût et leur quartier. Les bâtiments sont donc créés à partir de cette énumération. Les Merveilles sont ensuite étendues des Bâtiments, avec une classe par Merveille, étendue de la classe abstraite Prestige. Chaque Merveille implémente son pouvoir et les méthodes nécessaires au bon déroulement de celui-ci. Cela permet d'utiliser un bâtiment Prestige de façon générique, sans avoir à vérifier son type systématiquement.

### Personnages et leurs Pouvoirs

Les Personnages sont tous définis à partir de la classe abstraite _Character_, et définissent chacun leur pouvoir, avec un effet spécifique pour certains, et permet d'inclure le système de collectes liés aux Rôles. Il est donc possible de déclarer un Joueur et de lui attribuer la stratégie de notre choix.

## Responsabilités et Répartition du Travail

Nous avons réparti le travail autour de trois axes majeurs :
- Les strategies des robots
- Les rôles et leurs effets
- La création du game engine et des bâtiments


Ambre et Quentin se sont occupés de ce qui était lié aux stratégies, de la création des stratégies aux appel de l'affichage dans la classe _Player_, ce qui peut se retrouver entre autre dans l'issue [#17][issueStrat]. Ils se sont également occupé de la mise en place de la gestion de l'or, ce qu'on peut retrouver dans l'issue [#29][issueGold].

Pauline s'est quand à elle occupée de ce qui concerne les rôles, de leur choix par les joueurs à leur utilisation en
fonction de la stratégie, ce qui se retrouve dans les issues [#15][issueRole1] et [#21][issueRole2] par exemple.

Théo a majoritairement travaillé sur les bâtiments, de leur ajout dans la pioche à l'application des effets des
merveilles par les joueurs, dans l'issue [#16][issuePrestigeA]. Il a contribué à l'élaboration du game engine avec la
fin de partie et la détection du vainqueur dans l'issue [#18][issueTours] par exemple. Il a également travaillé sur
l'ajout et le calcul des points, de celui des bâtiments aux bonus de fin de partie, dans l'issue [#24][issuePoints].

## L'équipe
##### Git et Github
Le projet a nécessité l'usage de Git et Github. Afin de faciliter notre avancée, nous avons tiré profit des fonctionnalités de Github telles que la possibilité de définir des issues, d'en assigner des responsables et ainsi que de créer des labels pour déterminer l'utilité de chaque tâche. Les différents labels utilisés sont : enhancement, feature, documentation. Nous avons aussi utilisé deux branches différentes pour le projet : une branche master pour déposer nos avancées fonctionnelles et une branche conflit qui a servi à régler certains conflits entre nos versions. 
##### Communication et le groupe
Pour l'avancée du projet, nous avons developpé une bonne communication et utilisé le logiciel Discord, nous sommes
restés soudés et n'avons pas eu de problème de communication. Nous avons aussi choisi de commenter notre code autant que
possible pour être plus indépendant dans notre compréhension du code lors de nouvelles implémentations, notamment avec
l'issue [#31][RenduDeNoel] nous avons aussi profité de la fonctionnalité "TODO" de IntelliJ afin de marquer les points
importants à améliorer.

## Etat actuel du projet

### Ce qui est fait

##### Fonctionnement général
Le projet possède actuellement une version presque complète du jeu Citadelles, durant une partie, 4 bots s'affrontent avec des stratégies différentes prédéfinies : RushMerch, RushArchi, HighScoreArchi et HighScoreThief.
A chaque tour, les joueurs peuvent choisir un rôle, l'ordre de sélection se base sur le joueur ayant la couronne, puis ceux qui le suivent dans la liste choississent à leur tour (dans la méthode _newGame_ de la classe _Game_).
##### Un tour de jeu
Lors de leur tour de jeu, il est vérifié si le joueur n'a pas été tué par l'assassin, dans ce cas là son tour est passé, puis on vérifie s'il n'a pas été volé par le voleur, dans quel cas son argent est transféré au voleur. Ensuite le joueur peut jouer : il choisit entre piocher deux cartes et en garder une ou prendre 2 golds. Le joueur choisit de prendre une carte s'il n'a rien à construire ou si la banque est vide puis les effets de ses bâtiments s'activent. Enfin, le joueur peut construire des bâtiments s'il le souhaite. A la fin d'un tour _showPlay_ affiche tous les choix qui ont eu lieu. 
##### Roles
Pour les rôles : tous les pouvoirs ont été implementés et s'utilisent grâce à la méthode _usePower_. Si le pouvoir demande de cibler un joueur, _usePower_ appelera les méthodes _chooseTarget_, ou _chooseVictim_ dans le cas de l'assassin.
##### Score
Nous avons mis au point deux méthodes pour calculer les scores des joueurs et leurs bonus : _calculBonus_ et _calculScore_.
La première, _calculBonus_, regarde si le joueur a 5 bâtiments de la même couleur ou au moins 2 bâtiments de classe Prestige ou si le joueur a fini sa ville.
La seconde, _calculScore_, recupère les informations de _calculBonus_ et ajoute au score les effets du Dracoport et/ou de l'Université s'ils sont possédés. 
##### Bâtiments
Tous les bâtiments font partis de l'enumération _BuildingEnum_, les bâtiments ont un nom, un coût et un District. Il y a 5 districts possibles : Prestige, Commercial, Military, Noble et Religion. Tous, à l'exception du district Prestige, peuvent rapporter de l'argent aux joueurs grâce aux taxes. Les bâtiments ont chacun une classe individuelle qui est une sous-classe de _Prestige_, elle-même sous classe de _Building_, ce qui leur permet d'avoir leurs effets. 
 
### Ce qui reste à faire
Le jeu est presque complet, mais certaines fonctionnalités sont manquantes : 
- Système de pioche avec des cartes face visible et d'autres face cachée en fonction du nombre de joueurs dans la partie
- Implémentation du cimetière 
- Stratégies plus développées
Ce dernier point est à notre sens le plus important, nous aurions aimé faire plus de stratégies prenant en compte plus de rôles et de pouvoirs différents.

## Base de Code et Changements Nécessaires

La plupart du code est propre et peut être utilisé pour ajouter plus de fonctionnalités sans changements majeurs dans le code existant, notamment grâce aux Issues [#16][refactorBuildings] et [#25][refactorPersonnages] qui ont permis de minimiser le couplage, respectivement celui des Bâtiments et celui des personnages avec le reste du projet.

#### Bonnes Pratiques
##### -Definition des Strategies
Les stratégies des joueurs fonctionnent grâce à l'héritage des stratégies plus basiques. Cette architecture couplée à la méthode __compare__ qui permet de comparer deux bâtiments, permet de facilement modifier ou ajouter des stratégies.
##### -Gestion de l'affichage
Tout l'affichage du projet est fait dans la classe _Board_, à travers les méthodes __show__ qui s'occupent de l'affichage en fonction des variables en paramètre, et les méthodes __print__ qui formatent des données spécifiques. Cette pratique serait intéressante à conserver, et permettrait éventuellement de se diriger vers un niveau de détails modulables en fonction d'un paramètre défini par l'utilisateur.
#### Modifications à Faire
##### -Accesseurs et Listes
Un certain nombre d'assesseurs sur des listes renvoient la liste, et non pas des copies de ces listes. Il pourrait être intéressant de renvoyer des copies des listes, pour éviter les modifications involontaires sur les listes récupérées
##### -Responsabilité de la classe Player
La classe _Player_ qui définit les interactions avec le joueur, et ses stratégies a tendance à se diriger vers une classe-dieu avec trop de méthodes, et de responsabilités. Cela devrait être limité en déléguant des responsabilités à d'autres Classes, voir en créant de nouvelles classes pour prendre en charge ces responsabilitées


### Liens utiles

Nous avons utilisé les regles suivantes pour developper notre version de citadelles,ainsi que les regles et la Foire Aux Questions, dans le dossier [doc][docRegles] :
http://jeuxstrategie.free.fr/Citadelles_complet.php

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [packStrat]: <https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/tree/master/src/main/java/fr/unice/polytech/startingpoint/strategies>
   [packChar]: <https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/tree/master/src/main/java/fr/unice/polytech/startingpoint/characters>
   [packBuild]: <https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/tree/master/src/main/java/fr/unice/polytech/startingpoint/buildings>
   [refactorPersonnages]: <https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/issues/25>
   [refactorBuildings]: <https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/issues/16>
   [issueGold]: <https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/issues/29>
   [issueStrat]:<https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/issues/17>
   [issueRole1]:<https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/issues/15>
   [issueRole2]:<https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/issues/21>
   [issuePrestigeA]:<https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/issues/16>
   [issueTours]:<https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/issues/18>
   [issuePoints]:<https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/issues/24>

[docRegles]:<https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-l/tree/master/doc>

[RenduDeNoel]: <>

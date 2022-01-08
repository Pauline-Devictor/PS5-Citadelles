# Citadelles - Groupe L
---

### Fonctionnalités

Les seules fonctionnalités qui ne sont pas présentes sont l'effet de la carte _Cimetière_ , et la mécanique des cartes
Personnages cachées en début de tour. Pour l'instant, le jeu propose les 8 Rôles au premier joueur, qui choisit et passe
les cartes restantes au second, sans qu'il n'y ai de cartes écartées, ni face cachée ni face visible. Le tableau ci
dessous résume notre couverture fonctionnelle du jeu de citadelles

| Catégorie             | Fonctionnalités présentes                                                                                                                           | Détails                                                                                                                                                                                        |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Personnages           | Pouvoirs et Effets                                                                                                                                  | Effet des Personnages et Choix d'une cible selon la stratégie du Joueur                                                                                                                        |
| Stratégies des Robots | 7 Stratégies différentes                                                                                                                            | <ul><li>Choix du Rôle</li><li>Choix d'une Carte</li><li> Piocher ou prendre de l'or</li><li>Choix d'un bâtiment à poser</li><li>Stratégie pour utiliser le pouvoir de son personnage</li></ul> |
| Bâtiments             | Bâtiments et Contraintes liés                                                                                                                       | <ul><li> Couleur des Bâtiments<li> Coût des Bâtiments<li> Effet des Merveilles</ul>                                                                                                            |
| Fin de Partie         | Calcul des Points et Affichage                                                                                                                      | Détail d'affichage possible, Calcul du Score incluant les bonus de chaque Joueur                                                                                                               |
| Règles Générales      | La quasi-totalité des règles <ul><li>Existence d'une banque : 30 Pièces Maximum </li><li>Nombre et Type de Cartes pré-défini dans un deck</li></ul> | Mécanique de choix des personnages incomplète                                                                                                                                                  |

#### Conception des Logs

Les logs ont été réalisés en remplaçant tous les 'System.out' par la création d'un log, qui est envoyé vers un handler
personnalisé qui le met en forme et l'affiche si besoin. Les statistiques de parties ont donc le niveau _CONFIG_,
l'affichage d'une partie est entre _FINE_ et _FINEST_ selon le niveau de détails souhaités, et d'éventuelles erreurs ont
le niveau _SEVERE_.

#### Conception de la sauvegarde et des Statistiques

Chaque exécution de 1000 parties génère des résultats, avec les statistiques de chaque robot inscrit dans le fichier _
results.csv_, avec un timestamp correspondant aux parties jouées. Ces données sont ensuite agrégées, pour former les
statistiques de chaque stratégie, que l'on peut retrouver au début du fichier de résultats
|Nom du bot|Score Moyen | Winrate|Nombre de Parties|Victoires|Égalités|Défaites

#### Conception d'un SuperBot

Nous avons choisi d'implémenter le robot _Opportuniste_, stratégie qui était la plus éloignée de celles déjà existantes
dans notre jeu. Nous n'avons pas eu besoin de faire de choix d'architectures, il a suffi d'utiliser l'architecture
existante, et de redéfinir les méthodes héritées.

Notre _Opportuniste_ à un taux de victoire de 5%, lorsqu'il joue seul contre d'autres robots. Cela s'explique d'une part
par le fait qu'il pense que les autres robots suivent la même stratégie, et d'autre part parce que les conseils du forum
s'appliquent à des joueurs humains et non à nos robots qui ne suivent pas les mêmes principes qu'un humain pour jouer.
Lorsque que le robot joue avec d'autres _Opportunistes_ son taux de victoire peut monter jusqu'à 25%, preuve que la
coopération de sa stratégie fonctionne.

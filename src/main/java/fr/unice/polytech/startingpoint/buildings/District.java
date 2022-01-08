package fr.unice.polytech.startingpoint.buildings;

public enum District {

    //La valeur correspond à la valeur du personnage (Ordre de Jeu : Assassin -> 1, Condottiere -> 8)
    Commercial(5),
    Noble(3),
    Military(7),
    //Non associé à un personnage
    Prestige(-1),
    Religion(4);

    private final int taxCollector;

    District(int taxCollector) {
        this.taxCollector = taxCollector;
    }

    public int getTaxCollector() {
        return taxCollector;
    }
}

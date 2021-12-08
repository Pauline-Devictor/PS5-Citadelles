package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.characters.Character;

public enum District {

    Commercial(5),
    Noble(3),
    Military(7),
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

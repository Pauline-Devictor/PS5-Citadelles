package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.District;

import java.util.Optional;

import static fr.unice.polytech.startingpoint.District.Military;
import static fr.unice.polytech.startingpoint.District.Religion;

public class Condottiere extends Character {
    public Condottiere() {
        super(8, "Condottiere");
    }

    /*public void destroyBuilding(Player victim) {
        victim.getCardHand();
    }*/

    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            collectTaxes(p.get(), Military);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    //Condo
    public void chooseTargetToDestroy() {

    }
}
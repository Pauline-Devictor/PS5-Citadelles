package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Player;

public class Magician extends Character {
    public Magician() {
        super(3, "Magician");
    }
    public void power(Player p){
        p.deckAfterMagician();
    }

    @Override
    public void usePower(Player p) {
        setPlayer(p);
        if(p.getCardHand().size()!=0)
            power(p);

    }
}

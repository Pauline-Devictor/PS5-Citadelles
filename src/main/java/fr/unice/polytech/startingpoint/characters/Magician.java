package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.List;
import java.util.Optional;

public class Magician extends Character {
    public Magician() {
        super(CharacterEnum.Magician);
    }

    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            if (p.get().getCardHand().size() != 0) {
                //TODO Changer Implementation
                Optional<Player> target = p.get().chooseTarget();
                if (target.isPresent())
                    swapHandPlayer(p.get(), target.get());
                else
                    swapHandDeck(p.get());
            }
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    public void swapHandDeck(Player magician) {
        System.out.println("Le Magicien change ses cartes");
        int n = magician.getCardHand().size();
        for (int i = 0; i < n; i++)
            magician.discardCard();
        magician.drawCards(n);
    }

    public void swapHandPlayer(Player magician, Player swapPerson) {
        System.out.println("Le Magicien echange ses cartes avec " + swapPerson.getName());
        List<Building> tempHand = magician.getCardHand();
        magician.setCardHand(swapPerson.getCardHand());
        swapPerson.setCardHand(tempHand);
    }

}

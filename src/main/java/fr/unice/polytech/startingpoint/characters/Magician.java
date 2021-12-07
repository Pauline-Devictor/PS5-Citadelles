package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.List;
import java.util.Optional;

public class Magician extends Character {
    public Magician() {
        super(3, "Magician");
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
            magician.discardCard(Optional.empty());
        magician.drawCards(n);
    }

    public void swapHandPlayer(Player magician, Player swapPerson) {
        System.out.println("Le Magicien echange ses cartes avec " + swapPerson.getName());
        List<Building> tempHand = magician.getCardHand();
        magician.setCardHand(swapPerson.getCardHand());
        swapPerson.setCardHand(tempHand);
    }


    public void deckAfterMagician(int choice, Player magician, Optional<Player> target) {
        if (choice == 1) {
            swapHandDeck(magician);
        } else if (choice == 2) {
            target.ifPresent(player -> swapHandPlayer(magician, player));
        }
        /*Random random = new Random();
        int number = random.nextInt(2);
        if(number == 0){//Remplacement des cartes
            System.out.println("Le Magicien change ses cartes");
            int n = cardHand.size();
            for (Building cards:cardHand) {board.getPile().putCard(cards);}
            drawCards(n);
        }
        else {
            System.out.println("Le Magicien echange ses cartes");
            Random random2 = new Random();
            int number2 = random2.nextInt(board.getNbPlayer());
            Player p = board.getPlayers().get(number2);// echange de la main avec un autre joueur
            List<Building> tempHand = cardHand;
            cardHand = p.getCardHand();
            p.setCardHand( tempHand);
        }*/
    }
}

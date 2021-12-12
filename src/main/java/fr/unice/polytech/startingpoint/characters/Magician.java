package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

public class Magician extends Character {
    private Player target;

    public Magician() {
        super(CharacterEnum.Magician);
        target = null;
    }

    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            if (p.get().getCardHand().size() != 0) {
                Optional<Player> target = chooseTarget(b, p);
                if (target.isPresent()) {
                    swapHandPlayer(p.get(), target.get());
                    this.target = target.get();
                } else
                    swapHandDeck(p.get());
                System.out.println(printEffect(p.get()));
            }
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    public void swapHandDeck(Player magician) {
        int n = magician.getCardHand().size();
        for (int i = 0; i < n; i++)
            magician.discardCard();
        magician.drawCards(n);
    }

    public void swapHandPlayer(Player magician, Player swapPerson) {
        //System.out.println("Le Magicien echange ses cartes avec " + swapPerson.getName());
        List<Building> tempHand = magician.getCardHand();
        magician.setCardHand(swapPerson.getCardHand());
        swapPerson.setCardHand(tempHand);
    }

    @Override
    public String printEffect(Player p) {
        StringBuilder res = new StringBuilder(super.printEffect(p));
        if (isNull(target)) {
            res.append(" Il echange ses cartes avec la pioche. Sa main se compose maintenant de ");
            for (Building b : p.getCardHand()) {
                res.append(b.getName()).append(", ");
            }
        } else
            res.append(" Il echange ses cartes avec ").append(target.getName());
        return res.toString();
    }

    public Optional<Player> chooseTarget(Board board, Optional<Player> player) {
        Player biggestCity = board.getPlayers().get(0);
        if (player.get().getCardHand().size() < 3) {
            for (Player p : board.getPlayers()) {
                if (p.getCity().size() > biggestCity.getCity().size()) biggestCity = p;
            }
            if (biggestCity.getCity().size() > 5) return Optional.of(biggestCity);
        }
        Player biggestHand;
        biggestHand = board.getPlayers().get(0);
        for (Player p : board.getPlayers()) {
            if (p.getCardHand().size() > biggestHand.getCardHand().size()) biggestHand = p;
        }
        return Optional.of(biggestHand);
    }
}

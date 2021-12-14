package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;
import java.util.Random;

public class Assassin extends Character {

    public Assassin() {
        super(CharacterEnum.Assassin);
    }

    /**
     * uses the Assassin's power
     * @param b the current game's board
     */
    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            chooseVictim(b, p).setMurdered(true);
            System.out.println(printEffect(p.get()));
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    /**
     * Chooses the Assassin's victim
     * @param board the current game's board
     * @param player the Assassin's player
     * @return the Assassin's victim
     */
    public Character chooseVictim(Board board, Optional<Player> player){
        if (player.get().getCardHand().size() > 4) return new Magician();
        for (Player p : board.getPlayers()) {
            if (p.getCity().size() > 5) {
                District colour = p.getMajority();
                switch (colour) {
                    case Commercial -> {
                        return new Merchant();
                    }
                    case Noble -> {
                        return new King();
                    }
                    case Military -> {
                        return new Condottiere();
                    }
                    case Religion -> {
                        return new Bishop();
                    }
                }
            }
        }
        Random random = new Random();
        int victim = random.nextInt(7) + 1;
        return board.getCharacters().get(victim);
    }
}

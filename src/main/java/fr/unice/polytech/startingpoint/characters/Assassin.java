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
     * uses the Assassin's power :
     * Kill a role
     *
     * @param b the current game's board
     */
    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            chooseVictim(b, p.get()).kill();
            printEffect(p.get());
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    /**
     * Chooses the Assassin's victim
     * if the hand is full, kill the magician,
     * else kill the player which the most Building of a Color
     * @param board  the current game's board
     * @param player the Assassin's player
     * @return the Assassin's victim
     */
    public Character chooseVictim(Board board, Player player) {
        if (player.getCardHand().size() > 4)
            return board.getCharactersInfos(2);

        //condition archi
        for (Player p : board.getPlayers()) {
            if (p.getGold() >= 4 && p.getCardHand().size() >= 1 && p.getCity().size() >= 5 && !p.equals(player))
                return board.getCharacters().get(CharacterEnum.Architect.getOrder() - 1);
        }

        for (Player p : board.getPlayers()) {
            if (p.getCity().size() > 5) {
                District color = p.getMajority();
                switch (color) {
                    case Commercial -> {
                        return board.getCharactersInfos(4);
                    }
                    case Noble -> {
                        return board.getCharactersInfos(3);
                    }
                    case Military -> {
                        return board.getCharactersInfos(7);
                    }
                    case Religion -> {
                        return board.getCharactersInfos(5);
                    }
                }
            }
        }

        Random random = new Random();
        int victim = random.nextInt(6) + 2;
        return board.getCharacters().get(victim);
    }
}

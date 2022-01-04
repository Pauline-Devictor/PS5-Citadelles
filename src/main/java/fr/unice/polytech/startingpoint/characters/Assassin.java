package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;
import java.util.Random;

public class Assassin extends Character {
    Optional<Integer> priorityTarget;

    public Assassin() {
        super(CharacterEnum.Assassin);
        priorityTarget = Optional.empty();
    }

    public void setPriorityTarget(int priorityTarget) {
        this.priorityTarget = Optional.of(priorityTarget);
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
        if(priorityTarget.isPresent()){
            int copyOfTarget = priorityTarget.get();
            priorityTarget = Optional.empty();
            return board.getCharactersInfos(copyOfTarget);
        }
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
                        //return new Merchant();
                    }
                    case Noble -> {
                        return board.getCharactersInfos(3);
                        //return new King();
                    }
                    case Military -> {
                        return board.getCharactersInfos(7);
                        //return new Condottiere();
                    }
                    case Religion -> {
                        return board.getCharactersInfos(5);
                        //return new Bishop();
                    }
                }
            }
        }

        Random random = new Random();
        int victim = random.nextInt(6) + 2;
        return board.getCharacters().get(victim);
    }
}

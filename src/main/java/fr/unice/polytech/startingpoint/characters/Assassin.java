package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
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
                return board.getCharacters().get(CharacterEnum.Architect.getOrder());
        }

        Optional<Player> p = findPlayer(board);
        if(p.isPresent()){
            boolean contains1gold = false;
            for (Building b: p.get().getCity()) {
                if (b.getCost() == 1) contains1gold = true; break;
            }
            if(p.get().getCity().size() == 6 || contains1gold){
                return board.getCharacters().get(CharacterEnum.Condottiere.getOrder());
            }
        }

        int riches = 0;
        for (Player enemies : board.getPlayers()) {
            if (enemies.getGold() > 4) riches++;
        }
        if(riches > 1){
            return  board.getCharactersInfos(CharacterEnum.Thief.getOrder());
        }





        for (Player enemies : board.getPlayers()) {
            if (enemies.getCity().size() > 5) {
                District color = enemies.getMajority();
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

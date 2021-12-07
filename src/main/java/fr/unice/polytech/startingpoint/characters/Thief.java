package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;
import java.util.Random;

public class Thief extends Character {
    public Thief() {
        super(2, "Thief");
    }

    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            Character c = p.get().chooseVictim();
            if (!c.isMurdered() && c.getClass() != Assassin.class)
                c.setThief(p);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    public void stolen() {
        /*if (getRole().orElse(null).gotStolen()){
            board.getBank().transferGold(getGold(),board.getCharactersInfos(1).getPlayer());
            System.out.println(ANSI_ITALIC + getName() + " has been robbed."+ getGold() +" gold has been stolen." + ANSI_RESET);
            board.getCharactersInfos(1).getPlayer().setAmountStolen(getGold());
            board.getBank().refundGold(gold);
            gold = 0;
        }*/
    }
}
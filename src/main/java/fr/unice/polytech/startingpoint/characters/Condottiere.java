package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;

import static fr.unice.polytech.startingpoint.buildings.District.Military;

public class Condottiere extends Character {
    public Condottiere() {
        super(CharacterEnum.Condottiere);
    }

    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            collectTaxes(p.get(), Military);
            selectTarget(p.get());
            p.get().chooseBuild(selectTarget(p.get()),p.get());
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    //Condo
    public Optional<Player> selectTarget(Player p) {
        Optional<Player> target;
        target = p.chooseTarget();
        if (target.get().getRole().get().getClass() == Bishop.class)
            return Optional.empty();
        return target;
    }
}
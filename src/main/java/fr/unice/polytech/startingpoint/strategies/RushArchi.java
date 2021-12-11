package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RushArchi extends Player {

    public RushArchi(Board b) {
        super(b, "RushArchitect");
    }


    public void roleEffects() {
        if (getRole().isPresent()) {
            getRole().get().usePower(board);
        }
    }

    public List<Building> buildDecision() {
        //Scale of cost ok for building
        return buildDecision(0, 3);
    }

    public void chooseRole() {
        int index;
        do {
            index = new Random().nextInt(8);
        } while (!board.getCharactersInfos(index).isAvailable());
        role = Optional.of(board.getCharactersInfos(index));
        board.getCharactersInfos(index).setAvailable(false);
    }

    @Override
    public int compare(Building b1, Building b2) {
        //Positive if o2>o1
        return b1.getCost() - b2.getCost();
    }

}

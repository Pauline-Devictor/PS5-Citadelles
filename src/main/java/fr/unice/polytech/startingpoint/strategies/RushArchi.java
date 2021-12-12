package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class RushArchi extends Player {
    private final int costMax = 3;
    private final int costMin = 1;

    public RushArchi(Board b) {
        super(b, "RushArchitect");
    }

    @Override
    public void roleEffects() {
        if (getRole().isPresent()) {
            getRole().get().usePower(board);
        }
    }

    @Override
    public void chooseRole() {
        ArrayList<Integer> taxList = new ArrayList<>();

        //prioritize architect
        taxList.add(6);

        //if has 6+ buildings, Bishop
        if(getCity().size() > 5) taxList.add(0, 4);
        else taxList.add(4);

        taxList.addAll(List.of(
                2, 3, 5, 1, 0, 7
        ));

        for (int elem : taxList) {
            if (pickRole(elem)) {
                return;
            }
        }
    }

    @Override
    public int compare(Building b1, Building b2) {
        //return -1 pour b1, 1 pour b2
        if (isNull(b1))
            return 1;
        else if (isNull(b2))
            return -1;
        else if (getCardHand().contains(b1))
            return 1;
        else if (getCardHand().contains(b2))
            return -1;
        else if (b1.getCost() <= costMax && b1.getCost() >= costMin && b2.getCost() <= costMax && b2.getCost() >= costMin) {
            if (b1.getDistrict() == District.Religion) return -1;
            if (b2.getDistrict() == District.Religion) return 1;
        }
        return (b1.getCost() < b2.getCost()) ? -1 : 1;
    }
}

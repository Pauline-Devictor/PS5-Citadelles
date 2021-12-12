package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class RushMerch extends Player {
    private final int costMax = 3;
    private final int costMin = 1;

    public RushMerch(Board b) {
        super(b, "RushMarchand");
    }

    @Override
    public void chooseRole() {
        ArrayList<Integer> taxList = new ArrayList<>();

        taxList.addAll(List.of(
                6, 5, 2, 3, 1, 0, 7
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
            if (b1.getDistrict() == District.Commercial) return -1;
            if (b2.getDistrict() == District.Commercial) return 1;
        }
        return (b1.getCost() < b2.getCost()) ? -1 : 1;
    }
}

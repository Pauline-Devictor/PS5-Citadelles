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

public class HighScoreThief extends Player {
    private final int costMax = 6;
    private final int costMin = 3;

    public HighScoreThief(Board b) {
        super(b, "HautScoreVoleur");
    }

    public void roleEffects() {
        if (getRole().isPresent()) {
            getRole().get().usePower(board);
        }
    }

    public List<Building> buildDecision() {
        //Scale of cost ok for building
        return buildDecision(costMin, costMax);
    }

    public List<Building> buildDecision(int costMin, int costMax) {
        List<Building> checkBuilding = new ArrayList<>();
        for (Building b : getCardHand()) {
            //prioritize noble district buildings
            if (isBuildable(b) && nbBuildable > 0) {
                if ((b.getCost() <= costMax && b.getCost() >= costMin && b.getDistrict() == District.Noble)
                        || (board.getPile().isEmpty() && board.getBank().getGold() == 0)) {
                    nbBuildable--;
                    checkBuilding.add(b);
                }
            }
        }
        if (!checkBuilding.isEmpty()) {
            checkBuilding.forEach(this::build);
            return checkBuilding;
        }
        //else
        for (Building b : getCardHand()) {
            if (isBuildable(b) && nbBuildable > 0) {
                if ((b.getCost() <= costMax && b.getCost() >= costMin)
                        || (board.getPile().isEmpty() && board.getBank().getGold() == 0)) {
                    nbBuildable--;
                    checkBuilding.add(b);
                }
            }
        }
        checkBuilding.forEach(this::build);
        return checkBuilding;
    }

    public Building chooseBuilding(Building b1, Building b2) {
        if (isNull(b1))
            return b2;
        else if (isNull(b2))
            return b1;
        else if (getCardHand().contains(b1))
            return b2;
        else if (getCardHand().contains(b2))
            return b1;
        else if (b1.getCost() <= costMax && b1.getCost() >= costMin && b2.getCost() <= costMax && b2.getCost() >= costMin) {
            if (b1.getDistrict() == District.Noble) return b1;
            if (b2.getDistrict() == District.Noble) return b2;
        }

        return (b1.getCost() < b2.getCost()) ? b2 : b1;
    }

    public void chooseRole() {
        TreeMap<District, Integer> taxmap = new TreeMap<>();
        for (District d : District.values()) {
            taxmap.put(d, 0);
        }
        for (Building b : getCity()) {
            taxmap.put(b.getDistrict(), taxmap.get(b.getDistrict()) + 1);
        }
        taxmap.remove(District.Prestige);
        taxmap.remove(District.Noble);
        ArrayList<Integer> taxList = (ArrayList<Integer>) taxmap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .map(District::getTaxCollector)
                .collect(Collectors.toList());

        taxList.add(0, 3);
        taxList.add(0, 1);
        taxList.addAll(List.of(
                0, 2, 6
        ));

        for (int elem : taxList) {
            if (pickRole(elem)) {
                return;
            }
        }
    }

    @Override
    public int compare(Building b1, Building b2) {
        //Positive if o2>o1
        return b2.getCost() - b1.getCost();
    }

}

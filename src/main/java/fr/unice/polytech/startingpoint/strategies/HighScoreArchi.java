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

public class HighScoreArchi extends Player {

    public HighScoreArchi(Board b) {
        super(b, "HautScoreArchitect");
    }

    @Override
    public void chooseRole() {
        //Taxes priority
        TreeMap<District, Integer> taxmap = new TreeMap<>();
        for (District d : District.values()) {
            taxmap.put(d, 0);
        }
        for (Building b : getCity()) {
            taxmap.put(b.getDistrict(), taxmap.get(b.getDistrict()) + 1);
        }
        taxmap.remove(District.Prestige);
        taxmap.remove(District.Military);
        ArrayList<Integer> taxList = (ArrayList<Integer>) taxmap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .map(District::getTaxCollector)
                .collect(Collectors.toList());

        //if rich, architect
        if (gold > 10) taxList.add(0, 6);
        else taxList.add(6);

        //if a player has too much advance, condottiere
        Player biggestCity = board.getPlayers().get(0);
        for (Player p : board.getPlayers()) {
            if (p.getCity().size() > biggestCity.getCity().size()) biggestCity = p;
        }
        if ((biggestCity.getCity().size() - city.size() > 4)) taxList.add(0, 7);
        else taxList.add(7);

        taxList.addAll(List.of(
                1, 0, 2
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
        int costMax = 6;
        int costMin = 3;
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
            if (b1.getDistrict() == District.Noble) return -1;
            if (b2.getDistrict() == District.Noble) return 1;
            if (b1.getDistrict() == District.Religion) return -1;
            if (b2.getDistrict() == District.Religion) return 1;
        }
        return (b1.getCost() < b2.getCost()) ? 1 : -1;
    }
}

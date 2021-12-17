package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class HighScoreArchi extends Player {
    private final int costMax = 6;
    private final int costMin = 3;

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

        Collections.reverse(taxList);

        //if rich, architect
        if (gold > 10) taxList.add(0, CharacterEnum.Architect.getOrder() - 1);
        else taxList.add(CharacterEnum.Architect.getOrder() - 1);

        //if a player has too much advance, condottiere
        Player biggestCity = board.getPlayers().get(0);
        for (Player p : board.getPlayers()) {
            if (p.getCity().size() > biggestCity.getCity().size()) biggestCity = p;
        }
        if ((biggestCity.getCity().size() - city.size() > 5)) taxList.add(0, CharacterEnum.Condottiere.getOrder() - 1);
        else taxList.add(CharacterEnum.Condottiere.getOrder() - 1);

        taxList.addAll(List.of(
                CharacterEnum.Magician.getOrder() - 1,
                CharacterEnum.Assassin.getOrder() - 1,
                CharacterEnum.King.getOrder() - 1
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
            if (b1.getDistrict() == District.Commercial && b2.getDistrict() == District.Commercial) return b2.getCost() - b1.getCost();
            if (b1.getDistrict() == District.Commercial) return -1;
            if (b2.getDistrict() == District.Commercial) return 1;
            if (b1.getDistrict() == District.Noble && b2.getDistrict() == District.Noble) return (b2.getCost() - b1.getCost());
            if (b1.getDistrict() == District.Noble) return -1;
            if (b2.getDistrict() == District.Noble) return 1;
            if (b1.getDistrict() == District.Religion && b2.getDistrict() == District.Religion) return b2.getCost() - b1.getCost();
            if (b1.getDistrict() == District.Religion) return -1;
            if (b2.getDistrict() == District.Religion) return 1;
        }
        return (b2.getCost() - b1.getCost());
    }
}

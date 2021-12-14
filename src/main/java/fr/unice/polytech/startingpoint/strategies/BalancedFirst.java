package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class BalancedFirst extends Player{

    public BalancedFirst(Board b){super(b, "BalancedFisrt");}

    @Override
    public void chooseRole() {
        //taxes priority
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

        Collections.reverse(taxList);

        //prioritize Assassin, King
        taxList.add(0, CharacterEnum.King.getOrder() - 1);
        taxList.add(0, CharacterEnum.Assassin.getOrder() - 1);
        taxList.addAll(List.of(
                CharacterEnum.Thief.getOrder() - 1,
                CharacterEnum.Magician.getOrder() - 1,
                CharacterEnum.Architect.getOrder() - 1
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
        if (b1.getDistrict() == District.Noble) return -1;
        else return 1;
    }
}

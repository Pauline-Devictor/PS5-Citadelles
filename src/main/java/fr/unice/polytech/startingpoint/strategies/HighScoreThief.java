package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Prestige;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class HighScoreThief extends Player {

    public HighScoreThief(Board b) {
        super(b, "HautScoreVoleur");
    }

    public void cityEffects() {
        getCity().forEach(e -> {
            if (e instanceof Prestige)
                ((Prestige) e).useEffect(this);
        });
    }

    public void roleEffects() {
        if (getRole().isPresent()) {
            getRole().get().usePower(board);
        }
    }

    public List<Building> buildDecision() {
        //Scale of cost ok for building
        return buildDecision(3, 6);
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
        return (b1.getCost() < b2.getCost()) ? b2 : b1;
    }

    public void chooseRole() {
        TreeMap<District,Integer> taxmap = new TreeMap<>();
        for (District d : District.values()) {
            taxmap.put(d, 0);
        }
        for (Building b : getCity()) {
            taxmap.put(b.getDistrict(), taxmap.get(b.getDistrict())+1);
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
                0,2,6
        ));

        for(int elem : taxList){
            if(pickRole(elem)){
                return;
            }
        }


    }

}

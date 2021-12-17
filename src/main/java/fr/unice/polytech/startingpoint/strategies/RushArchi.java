package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.*;

import static java.util.Objects.isNull;

public class RushArchi extends Player {

    public RushArchi(Board b) {
        super(b, "RushArchitect");
    }

    @Override
    public void chooseRole() {
        ArrayList<Integer> taxList = new ArrayList<>();

        //prioritize architect
        taxList.add(CharacterEnum.Architect.getOrder() - 1);

        taxList.addAll(List.of(
                CharacterEnum.Magician.getOrder() - 1,
                CharacterEnum.King.getOrder() - 1,
                CharacterEnum.Merchant.getOrder() - 1,
                CharacterEnum.Thief.getOrder() - 1,
                CharacterEnum.Assassin.getOrder() - 1,
                CharacterEnum.Condottiere.getOrder() - 1
        ));

        //if has 6+ buildings, Bishop
        if(getCity().size() > 5) taxList.add(0, CharacterEnum.Bishop.getOrder() - 1);
        else taxList.add(CharacterEnum.Bishop.getOrder() - 1);


        for (int elem : taxList) {
            if (pickRole(elem)) {
                return;
            }
        }
    }

    @Override
    public int compare(Building b1, Building b2) {
        //return -1 pour b1, 1 pour b2
        int costMin = 1;
        int costMax = 3;
        if (isNull(b1))
            return 1;
        else if (isNull(b2))
            return -1;
        else if (getCardHand().contains(b1))
            return 1;
        else if (getCardHand().contains(b2))
            return -1;
        else if (b1.getCost() <= costMax && b1.getCost() >= costMin && b2.getCost() <= costMax && b2.getCost() >= costMin) {
            if (b1.getDistrict() == District.Religion && b2.getDistrict() == District.Religion) return (b1.getCost() - b2.getCost());
            if (b1.getDistrict() == District.Religion) return -1;
            if (b2.getDistrict() == District.Religion) return 1;
        }
        return (b1.getCost() - b2.getCost());
    }
}

package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.CharacterEnum;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class RushMerch extends Player {

    public RushMerch(Board b) {
        super(b, "RushMarchand");
    }

    @Override
    public void chooseRole() {

        //Marchand puis Archi
        ArrayList<Integer> taxList = new ArrayList<>(List.of(
                CharacterEnum.Merchant.getOrder() - 1,
                CharacterEnum.Architect.getOrder() - 1,
                CharacterEnum.Magician.getOrder() - 1,
                CharacterEnum.King.getOrder() - 1,
                CharacterEnum.Thief.getOrder() - 1,
                CharacterEnum.Bishop.getOrder() - 1,
                CharacterEnum.Assassin.getOrder() - 1,
                CharacterEnum.Condottiere.getOrder() - 1
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
        int costMax = 3;
        int costMin = 1;
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

package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Prestige;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.characters.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.Objects.isNull;

public class HighScoreArchi extends Player {
    private final int costMax = 6;
    private final int costMin = 3;

    public HighScoreArchi(Board b, String name) {
        super(b, name);
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
        return buildDecision(costMin, costMax);
    }

    public List<Building> buildDecision(int costMin, int costMax) {
        List<Building> checkBuilding = new ArrayList<>();
        for (Building b : getCardHand()) {
            if (isBuildable(b) && nbBuildable > 0) {
                if ((b.getCost() <= costMax && b.getCost() >= costMin && (b.getDistrict() == District.Noble || b.getDistrict() == District.Religion))
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
        else if (b1.getCost() <= costMax && b1.getCost() >= costMin && b2.getCost() <= costMax && b2.getCost() >= costMin){
            if(b1.getDistrict() == District.Commercial) return b1;
            if(b2.getDistrict() == District.Commercial) return b2;
            if(b1.getDistrict() == District.Noble) return b1;
            if(b2.getDistrict() == District.Noble) return b2;
            if(b1.getDistrict() == District.Religion) return b1;
            if(b2.getDistrict() == District.Religion) return b2;
        }
        else if(b1.getCost() <= costMax && b1.getCost() >= costMin) return b1;
        else if(b2.getCost() <= costMax && b2.getCost() >= costMin) return b2;

        return (b1.getCost() < b2.getCost()) ? b2 : b1;
    }

    public void chooseRole () {
        int index;
        do {
            index = new Random().nextInt(8);
        } while (!board.getCharactersInfos(index).isAvailable());
        role = Optional.of(board.getCharactersInfos(index));
        board.getCharactersInfos(index).setAvailable(false);
    }
}

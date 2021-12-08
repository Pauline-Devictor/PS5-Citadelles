package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Prestige;
import fr.unice.polytech.startingpoint.characters.*;

import java.util.*;

import static java.util.Objects.isNull;

public class HighScoreArchi extends Player {
    private final int costMax = 6;
    private final int costMin = 3;
    private final ArrayList<Integer> priority = new ArrayList<Integer>(Arrays.asList(5, 3, 4, 6, 1, 0, 7, 2));

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
            //prioritize noble, commercial, religious district buildings
            if (isBuildable(b) && nbBuildable > 0) {
                if ((b.getCost() <= costMax && b.getCost() >= costMin && (b.getDistrict() == District.Noble || b.getDistrict() == District.Religion || b.getDistrict()== District.Commercial))
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
        //if rich, architect
        if(gold > 5 ) {
            if(pickRole(6)) return;
        }

        //if a player has too much advance, condottiere
        Player biggestCity = board.getPlayers().get(0);
        for (Player p : board.getPlayers()) {
            if (p.getCity().size() > biggestCity.getCity().size()) biggestCity = p;
        }
        if((biggestCity.getCity().size() - city.size() > 4)){
            if(pickRole(7)) return;
        }

        //else, district priority
        District colour = getMajority(this);
        switch (colour){
            //Merchant
            case Commercial, Prestige -> {
                if(pickRole(5)) return;
            }
            //King
            case Noble -> {
                if (pickRole(3)) return;
            }
            //Condottiere
            case Military -> {
                if (pickRole(7)) return;
            }
            //Bishop
            case Religion -> {
                if (pickRole(4)) return;
            }
        }

        //default priority
        for (Integer i: priority) {
            if (pickRole(i)) return;
        }
    }
}

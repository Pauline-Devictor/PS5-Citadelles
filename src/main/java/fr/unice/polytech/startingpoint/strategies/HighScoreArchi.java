package fr.unice.polytech.startingpoint.strategies;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.buildings.Prestige;
import fr.unice.polytech.startingpoint.characters.Assassin;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.Magician;

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

    void cityEffects() {
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

    public List<Building> buildDecision(int costMin, int costMax){
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
        if(!checkBuilding.isEmpty()){
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
        else if (b1.getCost() <= costMax && b1.getCost() >= costMin && b1.getDistrict() == District.Noble)
            return b1;
        else if (b2.getCost() <= costMax && b2.getCost() >= costMin && b2.getDistrict() == District.Noble)
            return b2;
        else if (b1.getCost() <= costMax && b1.getCost() >= costMin && b1.getDistrict() == District.Religion)
            return b1;
        else if (b2.getCost() <= costMax && b2.getCost() >= costMin && b2.getDistrict() == District.Religion)
            return b2;
        return (b1.getCost() < b2.getCost()) ? b2 : b1;
    }

    public Character chooseVictim() {
        //Random random = new Random();
        //int victim = random.nextInt(7) + 1;
        //return board.getCharacters().get(victim);
        if(role.isPresent()) {
            if (role.get().getClass() != Assassin.class) {
                if(cardHand.size() > 3) return new Magician();
                for (Player player : board.getPlayers()){
                   // if(player.getCity().size() > 5)
                }
        }
        return null;
    }

    public Optional<Player> chooseTarget() {
        Random random = new Random();
        int victim = random.nextInt(board.getPlayers().size());
        return Optional.of(board.getPlayers().get(victim));
    }

    public void chooseRole() {
        int index;
        do {
            index = new Random().nextInt(8);
        } while (!board.getCharactersInfos(index).isAvailable());
        role = Optional.of(board.getCharactersInfos(index));
        board.getCharactersInfos(index).setAvailable(false);
    }
}

package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.Graveyard;
import fr.unice.polytech.startingpoint.buildings.Laboratory;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static fr.unice.polytech.startingpoint.buildings.District.Military;
import static java.util.Objects.isNull;

public class Condottiere extends Character {
    Player target;
    Building build;

    public Condottiere() {
        super(CharacterEnum.Condottiere);
        target = null;
        build = null;
    }

    /**
     * uses the Condottiere's power
     * @param b the current game's board
     */
    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            collectTaxes(p.get(), Military);
            target = chooseTarget(b, p.get());
            if (!isNull(target))
                chooseBuild(b, target, p.get());
            printEffect(p.get());
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    /**
     * Chooses the Condottiere's target
     *
     * @param board  the current game's board
     * @param player the Condottiere's player
     * @return the Condottiere's target
     */
    public Player chooseTarget(Board board, Player player) {
        TreeMap<Integer, Player> cityMap = new TreeMap<>();
        for (Player p : board.getPlayers()) {
            cityMap.put(p.getCity().size(), p);
        }
        ArrayList<Player> cityList = (ArrayList<Player>) cityMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        //Verification que le personnage est un role qui ne soit pas Eveque, ou que ce ne soit pas le condottiere
        cityList.removeIf(c -> c.equals(player) || isNull(c.getRole()) || c.getRole().getClass() == Bishop.class || c.getCity().size()>=8);
        return cityList.isEmpty() ? null : cityList.get(cityList.size() - 1);
    }

    /**
     * Chooses the target's building to destroy
     *
     * @param board  the current game's board
     * @param target the Condottiere's target
     * @param condo  the Condottiere's player
     */
    public void chooseBuild(Board board, Player target, Player condo) {
        TreeMap<Integer, Building> costMap = new TreeMap<>();
        for (Building b : target.getCity()) {
            costMap.put(b.getCost(), b);
        }
        ArrayList<Building> costList = (ArrayList<Building>) costMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.<Building>toList());

        costList.removeIf(c -> c instanceof Graveyard);

        if(!costList.isEmpty()) {
            if (condo.getGold() > 10)
                this.build = costList.get(costList.size() - 1);
            else
                this.build = costList.get(0);
            if (condo.getGold() >= build.getCost()) {
                board.getPile().putCard(build);
                target.getCity().remove(build);
                condo.refundGold(build.getCost() - 1);
                this.target = target;
            }
        }
    }

    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        p.getBoard().showCondottiereEffect(target, build, p);
    }
}
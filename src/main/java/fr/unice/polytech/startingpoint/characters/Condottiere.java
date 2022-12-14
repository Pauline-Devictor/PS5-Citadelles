package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.Donjon;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.*;
import java.util.stream.Collectors;

import static fr.unice.polytech.startingpoint.Display.showCondottiereEffect;
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
     *
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
     * If a player has more than 5 buildings, targets him
     * Else, targets a player which has a 1 gold building
     *
     * @param board  the current game's board
     * @param player the Condottiere's player
     * @return the Condottiere's target
     */
    public Player chooseTarget(Board board, Player player) {
        List<Player> cityList = map(board);
        //Verification que le personnage est un role qui ne soit pas Eveque, ou que ce ne soit pas le condottiere
        cityList.removeIf(c -> c.equals(player) || isNull(c.getRole()) || c.getRole().getClass() == Bishop.class || c.getCity().size() >= 8);
        if ((!cityList.isEmpty() && cityList.get(cityList.size() - 1).getCity().size() > 5))
            return cityList.get(cityList.size() - 1);

        else if (!cityList.isEmpty()) {
            Collections.reverse(cityList);
            for (Player p : cityList) {
                for (Building b : p.getCity()) {
                    if (b.getCost() == 1) return p;
                }
            }
        }
        return null;
    }

    /**
     * Chooses the target's building to destroy
     * If more than 10 gold, the most expensive
     * Else the less expensive
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

        costList.removeIf(c -> c instanceof Donjon);

        if (!costList.isEmpty()) {
            if (condo.getGold() > 10 && target.getCity().size() > 5)
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

    /**
     * Prints the power effect
     *
     * @param p the Merchant's player
     */

    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        showCondottiereEffect(target, build, p);
    }
}
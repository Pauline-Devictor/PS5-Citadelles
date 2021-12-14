package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static fr.unice.polytech.startingpoint.buildings.District.Military;

public class Condottiere extends Character {

    public Condottiere() {
        super(CharacterEnum.Condottiere);
    }

    /**
     * uses the Condottiere's power
     * @param b the current game's board
     */
    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            System.out.println(printEffect(p.get()));
            collectTaxes(p.get(), Military);
            chooseBuild(b, chooseTarget(b, p.get()), p.get());
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

        cityList.removeIf(c -> c.equals(player) || c.getRole().getClass() == Bishop.class);

        return cityList.get(cityList.size() - 1);
    }

    /**
     * Chooses the target's building to destroy
     *
     * @param board  the current game's board
     * @param target the Condotiiere's target
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

        Building build;
        if (condo.getGold() > 10) build = costList.get(costList.size() - 1);
        else build = costList.get(0);

        if (condo.getGold() >= build.getCost()) {
            board.getPile().putCard(build);
            target.getCity().remove(build);
            condo.refundGold(build.getCost() - 1);
            System.out.println("Le batiment " + build.getName() + " du joueur " + target.getName() + " a été détruit.");
        }
        //TODO affichage
    }
}
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

    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            System.out.println(printEffect(p.get()));
            collectTaxes(p.get(), Military);
            chooseBuild(b, chooseTarget(b, p), p);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    public Optional<Player> chooseTarget(Board board, Optional<Player> player) {
        TreeMap<Integer, Player> cityMap = new TreeMap<>();
        for (Player p : board.getPlayers()) {
            cityMap.put(p.getCity().size(), p);
        }
        ArrayList<Player> cityList = (ArrayList<Player>) cityMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();

        for(int i = cityList.size() -1; i >= 0; i--){
            if (cityList.get(i).getRole().isPresent() && !cityList.get(i).equals(player.get()) &&
                    (cityList.get(i).getRole().get().getClass() != Bishop.class)) return Optional.ofNullable(cityList.get(i));
        }
        return Optional.empty();
    }

    public void chooseBuild(Board board, Optional<Player> target, Optional<Player> condo) {
        /*if (target.isPresent()) {
            TreeMap<Integer, Building> costMap = new TreeMap<>();
            for (Building b : target.get().getCity()) {
                costMap.put(b.getCost(), b);
            }
            ArrayList<Building> costList = (ArrayList<Building>) costMap
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            Building build;
            if(condo.get().getGold() > 10) build = costList.get(costList.size() - 1);
            else build = costList.get(0);

            if (condo.get().getGold() >= build.getCost()) {
                board.getPile().putCard(build);
                target.get().getCity().remove(build);
                condo.get().refundGold(build.getCost() - 1);
                System.out.println("Le batiment " + build.getName() + " du joueur " + target.get().getName() + " a été détruit.");
            }
            //TODO affichage
        }*/
    }
}
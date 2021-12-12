package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.Optional;
import java.util.Random;

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
            chooseBuilding(b, chooseTarget(b), p);
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    public Optional<Player> chooseTarget(Board board) {
        Player biggestCity;
        if (board.getPlayers().get(0).getRole().get().getClass() == Bishop.class) {
            biggestCity = board.getPlayers().get(1);
        } else {
            biggestCity = board.getPlayers().get(0);
        }
        //TODO ne pas peter ses propres batiments
        for (Player p : board.getPlayers()) {
            if (p.getRole().isPresent())
                if (p.getCity().size() >= biggestCity.getCity().size() && (p.getRole().get().getClass() != Bishop.class))
                    biggestCity = p;
        }
        return Optional.ofNullable(biggestCity);
    }

    public void chooseBuilding(Board board, Optional<Player> playerTarget, Optional<Player> condo) {
        if (playerTarget.isPresent()) {
            //TODO pas un random, si riche (gold > 10) il pète le building le plus cher, sinon, il pète le moins cher
            //conseil: fait une treemap pour trier :)
            Random random = new Random();
            int nbBuild = playerTarget.get().getCity().size();
            if (nbBuild > 0) {
                int toDestroy = random.nextInt(nbBuild);
                //Retire le build de la liste des construit & ajoute le build au deck
                Building build = playerTarget.get().getCity().get(toDestroy);
                //TODO boucle jusqu a avoir un building qui peut etre detruit
                if (condo.get().getGold() >= build.getCost()) {
                    board.getPile().putCard(build);
                    playerTarget.get().getCity().remove(build);
                    condo.get().refundGold(build.getCost() - 1);
                    System.out.println("Le batiment " + build.getName() + " du joueur " + playerTarget.get().getName() + " a été détruit.");
                    //TODO affichage
                }
            }
        }
    }
}
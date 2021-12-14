package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.*;

import static java.util.Objects.isNull;

public class Magician extends Character {
    private Player target;

    public Magician() {
        super(CharacterEnum.Magician);
        target = null;
    }

    /**
     * Uses the Magician's power
     *
     * @param b the current game's board
     */
    @Override
    public void usePower(Board b) {
        Optional<Player> p = findPlayer(b);
        if (p.isPresent()) {
            if (p.get().getCardHand().size() != 0) {
                Optional<Player> target = chooseTarget(b, p.get());
                if (target.isPresent()) {
                    swapHandPlayer(p.get(), target.get());
                    this.target = target.get();
                } else
                    swapHandDeck(p.get());
                System.out.println(printEffect(p.get()));
            }
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    /**
     * If there's no one to swap with
     *
     * @param magician the Magician's player
     */
    public void swapHandDeck(Player magician) {
        int n = magician.getCardHand().size();
        for (int i = 0; i < n; i++)
            magician.discardCard();
        magician.drawAndChoose(n, n);
    }

    /**
     * Swaps the player's hand
     *
     * @param magician   the Magician's player
     * @param swapPerson The player to swap with
     */
    public void swapHandPlayer(Player magician, Player swapPerson) {
        //TODO Eviter les setters ???
        //System.out.println("Le Magicien echange ses cartes avec " + swapPerson.getName());
        List<Building> tempHand = magician.getCardHand();
        magician.setCardHand(swapPerson.getCardHand());
        swapPerson.setCardHand(tempHand);
    }

    /**
     * Prints the power effect
     *
     * @param p the Magician's player
     * @return the String to print
     */
    @Override
    public String printEffect(Player p) {
        StringBuilder res = new StringBuilder(super.printEffect(p));
        if (isNull(target)) {
            res.append(" Il echange ses cartes avec la pioche. Sa main se compose maintenant de ");
            for (Building b : p.getCardHand()) {
                res.append(b.getName()).append(", ");
            }
        } else
            res.append(" Il echange ses cartes avec ").append(target.getName());
        return res.toString();
    }

    /**
     * Chooses the Magician's target
     *
     * @param board  the current game's board
     * @param player the Magician's player
     * @return the Magician's target
     */
    public Optional<Player> chooseTarget(Board board, Player player) {
        if (player.getCardHand().size() < 3) {
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

            cityList.removeIf(c -> c.equals(player) || c.getCity().size() <= 5);
            if (!cityList.isEmpty())
                return Optional.ofNullable(cityList.get(cityList.size() - 1));
        }

        TreeMap<Integer, Player> handMap = new TreeMap<>();
        for (Player p : board.getPlayers()) {
            handMap.put(p.getCardHand().size(), p);
        }
        ArrayList<Player> handList = (ArrayList<Player>) handMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();

        handList.removeIf(c -> c.equals(player));
        return Optional.ofNullable(handList.get(handList.size() - 1));
    }
}

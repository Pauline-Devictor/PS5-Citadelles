package fr.unice.polytech.startingpoint.characters;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Magician extends Character {
    private Player target;

    public Magician() {
        super(CharacterEnum.Magician);
        target = null;
    }

    /**
     * Uses the Magician's power :
     * Swap his card with someone or with the deck
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
                printEffect(p.get());
            }
        } else
            throw new IllegalArgumentException("No Role " + getName() + " in this board");
    }

    /**
     * Discard all the hand, and draw the same amount of card
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
        List<Building> tempHand = magician.getCardHand();
        magician.setCardHand(swapPerson.getCardHand());
        swapPerson.setCardHand(tempHand);
    }

    /**
     * Prints the power effect
     *
     * @param p the Magician's player
     */
    @Override
    public void printEffect(Player p) {
        super.printEffect(p);
        p.getBoard().showMagicianEffect(p, target);
    }

    /**
     * Chooses the target with the most of cards
     *
     * @param board  the current game's board
     * @param player the Magician's player
     * @return the Magician's target
     */
    public Optional<Player> chooseTarget(Board board, Player player) {
        if (player.getCardHand().size() < 3) {
            List<Player> cityList = map(board);

            cityList.removeIf(c -> c.equals(player) || c.getCity().size() <= 5);
            if (!cityList.isEmpty())
                return Optional.ofNullable(cityList.get(cityList.size() - 1));
            return Optional.empty();
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
                .collect(Collectors.toList());

        handList.removeIf(c -> c.equals(player));
        if (!handList.isEmpty())
            return Optional.ofNullable(handList.get(handList.size() - 1));
        else
            return Optional.empty();
    }
}

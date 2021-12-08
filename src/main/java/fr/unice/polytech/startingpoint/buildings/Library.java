package fr.unice.polytech.startingpoint.buildings;

import fr.unice.polytech.startingpoint.strategies.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

public class Library extends Prestige {
    private List<Building> cards;

    public Library() {
        super(BuildingEnum.Bibiliotheque);
        cards = new ArrayList<>();
    }

    public void useEffect(Player p) {
        cards = p.drawCards(2);
        System.out.println(printEffect(p));
    }

    @Override
    public String printEffect(Player p) {
        StringBuilder res = Optional.ofNullable(super.printEffect(p)).map(StringBuilder::new).orElse(null);
        if (!cards.isEmpty()) {
            res = (res == null ? new StringBuilder("null") : res).append(" Il a pioché ");
            for (Building b : cards) {
                res.append(b.getName()).append(", ");
            }
        }
        return res == null ? null : res.toString();
    }
}

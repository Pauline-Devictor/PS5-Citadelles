package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestCharacter {
    Board b;
    Character c;
    Player p;
    Player archi;
    Character archiCharacter;

    @BeforeEach
    void setUp() {
        b = spy(new Board());
        c = new King();
        p = spy(new Player(b));
        archi = spy(new Player(b));
        archiCharacter = spy(Architect.class);
    }

    @Test
    void setTaxes() {
        when(p.getCity()).thenReturn(List.of(new Building(BuildingEnum.Manoir),
                new Building(BuildingEnum.TourDeGuet),
                new Building(BuildingEnum.Port),
                new Building(BuildingEnum.Chateau)));
        c.collectTaxes(p, District.Noble);
        assertEquals(2, p.getTaxes());
    }

    @Test
    void setTaxesEmpty() {
        when(p.getCity()).thenReturn(new ArrayList<>());
        c.collectTaxes(p, District.Noble);
        assertEquals(0, p.getTaxes());
    }

    void killSomeone() {

    }

    void stealSomeone() {

    }

    void swapHandsDeck() {

    }

    void swapEmptyHands() {

    }

    void swapHands() {

    }

    @Test
    void buildArchitect() {
        p.setRole(Optional.of(archiCharacter));
        when(b.getPlayers()).thenReturn(List.of(p));
        when(b.getCharacters()).thenReturn(List.of(archiCharacter));
        archiCharacter.usePower(b);
        assertEquals(3, p.getNbBuildable());
    }

    @Test
    void findPlayer() {
        p.setRole(Optional.of(archiCharacter));
        when(b.getPlayers()).thenReturn(List.of(p));
        when(b.getCharacters()).thenReturn(List.of(archiCharacter));
        archiCharacter.usePower(b);
        assertEquals(3, p.getNbBuildable());
    }

    @Test
    void findPlayerEmpty() {
        p.setRole(Optional.of(archiCharacter));
        when(b.getPlayers()).thenReturn(new ArrayList<>());
        assertEquals(Optional.empty(), archiCharacter.findPlayer(b));
    }

    @Test
    void findPlayerMatchingRole() {
        p.setRole(Optional.of(archiCharacter));
        Player a = new Player(b);
        a.setRole(Optional.of(new Assassin()));
        Player m = new Player(b);
        m.setRole(Optional.of(new Merchant()));
        Player t = new Player(b);
        t.setRole(Optional.of(new Thief()));
        when(b.getPlayers()).thenReturn(List.of(a, m, t));
        assertEquals(Optional.empty(), archiCharacter.findPlayer(b));
    }
}

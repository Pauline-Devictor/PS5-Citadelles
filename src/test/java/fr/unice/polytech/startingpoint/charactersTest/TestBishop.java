package fr.unice.polytech.startingpoint.charactersTest;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.Bishop;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestBishop {
    Board board;
    Character bishop;
    Player player;

    @BeforeEach
    void setUp() {
        //LOGGER.setLevel(Level.OFF);
        board = spy(new Board());
        bishop = new Bishop();
        player = spy(new Player(board));
    }

    @Test
    void setTaxes() {
        player.refundGold(player.getGold());
        when(player.getCity()).thenReturn(List.of(new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Eglise),
                new Building(BuildingEnum.Port),
                new Building(BuildingEnum.Chateau)));
        bishop.collectTaxes(player, District.Religion);
        assertEquals(2, player.getGold());
    }
}


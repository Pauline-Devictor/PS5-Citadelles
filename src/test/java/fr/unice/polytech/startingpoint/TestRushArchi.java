package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.Library;
import fr.unice.polytech.startingpoint.buildings.Observatory;
import fr.unice.polytech.startingpoint.strategies.HighScoreThief;
import fr.unice.polytech.startingpoint.strategies.RushArchi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestRushArchi {

    Board board;
    RushArchi bot;


    @BeforeEach
    void setUp() {
        board = spy(new Board());
        bot = spy(new RushArchi(board));
        for (int i = 0; i < bot.getCardHand().size(); i++) {
            bot.discardCard();
        }
        when(bot.getCardHand()).thenReturn(new ArrayList<>(
                Arrays.asList(
                        new Building(BuildingEnum.Chateau),
                        new Building(BuildingEnum.Port),
                        new Observatory(),
                        new Library()
                )
        ));
    }

    @Test
    void compare_same_cost_military_vs_noble(){
        assertEquals(0, bot.compare(new Building(BuildingEnum.Manoir), new Building(BuildingEnum.Caserne)));
    }

    @Test
    void compare_same_cost_bishop_vs_noble(){
        assertTrue(bot.compare(new Building(BuildingEnum.Monastere), new Building(BuildingEnum.Manoir)) < 0);
    }

    @Test
    void compare_same_cost_noble_vs_commercial(){
        assertEquals(0, bot.compare(new Building(BuildingEnum.Manoir), new Building(BuildingEnum.Comptoir)));
    }

    @Test
    void compare_3golds_to_1gold(){
        assertTrue(bot.compare(new Building(BuildingEnum.Manoir), new Building(BuildingEnum.Taverne)) > 0);
    }

    @Test
    void compare_5golds_to_3gold(){
        assertTrue(bot.compare(new Building(BuildingEnum.Caserne), new Building(BuildingEnum.Forteresse)) < 0);
    }

    @Test
    void alreadyOwnsCard(){
        assertTrue(bot.compare(new Building(BuildingEnum.Port), new Building(BuildingEnum.Forteresse)) > 0);
    }

    @Test
    void compare_noble_to_noble(){
        assertTrue(bot.compare(new Building(BuildingEnum.Manoir), new Building(BuildingEnum.Palais)) < 0);
    }

    @Test
    void compare_cards_with_same_value(){
        assertEquals(0, bot.compare(new Building(BuildingEnum.Marche), new Building(BuildingEnum.Echoppe)));
    }
}

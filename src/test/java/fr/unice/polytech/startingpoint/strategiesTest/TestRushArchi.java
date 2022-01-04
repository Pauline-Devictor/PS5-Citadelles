package fr.unice.polytech.startingpoint.strategiesTest;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.Library;
import fr.unice.polytech.startingpoint.buildings.Observatory;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.strategies.HighScoreThief;
import fr.unice.polytech.startingpoint.strategies.RushArchi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TestRushArchi {

    Board board;
    RushArchi bot;
    Assassin mockAssassin;
    Thief mockThief;
    Magician mockMagician;
    King mockKing;
    Bishop mockBishop;
    Merchant mockMerchant;
    Architect mockArchitect;
    Condottiere mockCondottiere;


    @BeforeEach
    void setUp() {
        LOGGER.setLevel(Level.OFF);
        board = spy(new Board());

        mockAssassin = spy(new Assassin());
        when(mockAssassin.isAvailable()).thenReturn(true);
        mockThief = spy(new Thief());
        when(mockThief.isAvailable()).thenReturn(true);
        mockMagician = spy(new Magician());
        when(mockMagician.isAvailable()).thenReturn(true);
        mockKing = spy(new King());
        when(mockKing.isAvailable()).thenReturn(true);
        mockBishop = spy(new Bishop());
        when(mockBishop.isAvailable()).thenReturn(true);
        mockMerchant = spy(new Merchant());
        when(mockMerchant.isAvailable()).thenReturn(true);
        mockArchitect = spy(new Architect());
        when(mockArchitect.isAvailable()).thenReturn(true);
        mockCondottiere = spy(new Condottiere());
        when(mockCondottiere.isAvailable()).thenReturn(true);

        when(board.getCharacters()).thenReturn(List.of(
                mockAssassin,
                mockThief,
                mockMagician,
                mockKing,
                mockBishop,
                mockMerchant,
                mockArchitect,
                mockCondottiere));

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
    void choose_role_everything_available(){
        bot.chooseRole();
        assertEquals(bot.getRole(), mockArchitect);
    }

    @Test
    void choose_role_default_magician(){
        when(mockArchitect.isAvailable()).thenReturn(false);
        bot.chooseRole();
        assertEquals(bot.getRole(), mockMagician);
    }

    @Test
    void choose_role_default_king(){
        when(mockArchitect.isAvailable()).thenReturn(false);
        when(mockMagician.isAvailable()).thenReturn(false);
        bot.chooseRole();
        assertEquals(bot.getRole(), mockKing);
    }

    @Test
    void choose_role_default_merchant(){
        when(mockArchitect.isAvailable()).thenReturn(false);
        when(mockMagician.isAvailable()).thenReturn(false);
        when(mockKing.isAvailable()).thenReturn(false);
        bot.chooseRole();
        assertEquals(bot.getRole(), mockMerchant);
    }

    @Test
    void choose_role_default_thief(){
        when(mockArchitect.isAvailable()).thenReturn(false);
        when(mockMagician.isAvailable()).thenReturn(false);
        when(mockKing.isAvailable()).thenReturn(false);
        when(mockMerchant.isAvailable()).thenReturn(false);
        bot.chooseRole();
        assertEquals(bot.getRole(), mockThief);
    }

    @Test
    void choose_role_default_assassin(){
        when(mockArchitect.isAvailable()).thenReturn(false);
        when(mockMagician.isAvailable()).thenReturn(false);
        when(mockKing.isAvailable()).thenReturn(false);
        when(mockMerchant.isAvailable()).thenReturn(false);
        when(mockThief.isAvailable()).thenReturn(false);
        bot.chooseRole();
        assertEquals(bot.getRole(), mockAssassin);
    }

    @Test
    void choose_role_default_condottiere(){
        when(mockArchitect.isAvailable()).thenReturn(false);
        when(mockMagician.isAvailable()).thenReturn(false);
        when(mockKing.isAvailable()).thenReturn(false);
        when(mockMerchant.isAvailable()).thenReturn(false);
        when(mockThief.isAvailable()).thenReturn(false);
        when(mockAssassin.isAvailable()).thenReturn(false);
        bot.chooseRole();
        assertEquals(bot.getRole(), mockCondottiere);
    }

    @Test
    void choose_role_default_bishop(){
        when(mockArchitect.isAvailable()).thenReturn(false);
        when(mockMagician.isAvailable()).thenReturn(false);
        when(mockKing.isAvailable()).thenReturn(false);
        when(mockMerchant.isAvailable()).thenReturn(false);
        when(mockThief.isAvailable()).thenReturn(false);
        when(mockAssassin.isAvailable()).thenReturn(false);
        when(mockCondottiere.isAvailable()).thenReturn(false);
        bot.chooseRole();
        assertEquals(bot.getRole(), mockBishop);
    }

    @Test
    void choose_role_defensive_bishop(){
        when(bot.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Monastere),
                new Building(BuildingEnum.Palais),
                new Building(BuildingEnum.Cathedrale),
                new Building(BuildingEnum.Forteresse),
                new Building(BuildingEnum.Marche),
                new Building(BuildingEnum.Comptoir)
        ));
        bot.chooseRole();
        assertEquals(bot.getRole(), mockBishop);
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

package fr.unice.polytech.startingpoint.strategiesTest;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.Library;
import fr.unice.polytech.startingpoint.buildings.Observatory;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.strategies.HighScoreArchi;
import fr.unice.polytech.startingpoint.strategies.Opportuniste;
import fr.unice.polytech.startingpoint.strategies.Opportuniste;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestOpportuniste {
    Board board;
    HighScoreArchi opponent;
    Opportuniste bot;
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

        opponent = spy(new HighScoreArchi(board));

        bot = spy(new Opportuniste(board));
        for(int i = 0; i < bot.getCardHand().size(); i++){
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
    void chooseRoleDefaultBishop(){
        bot.chooseRole();
        assertEquals(mockBishop, bot.getRole());
    }

    @Test
    void chooseRoleDefaultCondottiere(){
        when(mockBishop.isAvailable()).thenReturn(false);
        bot.chooseRole();
        assertEquals(mockCondottiere, bot.getRole());
    }

    @Test
    void chooseRoleDefaultThief(){
        when(mockBishop.isAvailable()).thenReturn(false);
        when(mockCondottiere.isAvailable()).thenReturn(false);
        bot.chooseRole();
        assertEquals(mockThief, bot.getRole());
    }

    @Test
    void chooseRoleAssassin1(){
        when(opponent.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Cathedrale),
                new Building(BuildingEnum.Chateau),
                new Building(BuildingEnum.Taverne),
                new Building(BuildingEnum.TourDeGuet)
        ));
        when(opponent.getCardHand()).thenReturn(List.of(new Building(BuildingEnum.Cathedrale)));
        when(opponent.getGold()).thenReturn(4);
        when(board.getPlayers()).thenReturn(List.of(opponent));

        bot.chooseRole();
        assertEquals(mockAssassin, bot.getRole());
    }

    @Test
    void chooseRoleArchitect1(){
        when(opponent.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Cathedrale),
                new Building(BuildingEnum.Chateau),
                new Building(BuildingEnum.Taverne),
                new Building(BuildingEnum.TourDeGuet)
        ));
        when(opponent.getCardHand()).thenReturn(List.of(new Building(BuildingEnum.Cathedrale)));
        when(opponent.getGold()).thenReturn(4);
        when(board.getPlayers()).thenReturn(List.of(opponent));

        when((board.getCharactersInfos(0).isAvailable())).thenReturn(false);

        bot.chooseRole();
        assertEquals(mockArchitect, bot.getRole());
    }

    @Test
    void chooseRoleKing(){
        when(opponent.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Cathedrale),
                new Building(BuildingEnum.Chateau),
                new Building(BuildingEnum.Taverne),
                new Building(BuildingEnum.TourDeGuet),
                new Building(BuildingEnum.Marche)
        ));
        when(board.getPlayers()).thenReturn(List.of(opponent));

        bot.chooseRole();
        assertEquals(mockKing, bot.getRole());
    }

    @Test
    void chooseRoleAssassin2(){
        when(opponent.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Cathedrale),
                new Building(BuildingEnum.Chateau),
                new Building(BuildingEnum.Taverne),
                new Building(BuildingEnum.TourDeGuet),
                new Building(BuildingEnum.Marche)
        ));
        when(board.getPlayers()).thenReturn(List.of(opponent));
        when(board.getCharactersInfos(3).isAvailable()).thenReturn(false);

        bot.chooseRole();
        assertEquals(mockAssassin, bot.getRole());
    }

    @Test
    void chooseRoleCondottiere2(){
        when(opponent.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Cathedrale),
                new Building(BuildingEnum.Chateau),
                new Building(BuildingEnum.Taverne),
                new Building(BuildingEnum.TourDeGuet),
                new Building(BuildingEnum.Marche)
        ));
        when(board.getPlayers()).thenReturn(List.of(opponent));
        when(board.getCharactersInfos(3).isAvailable()).thenReturn(false);
        when(board.getCharactersInfos(0).isAvailable()).thenReturn(false);

        bot.chooseRole();
        assertEquals(mockCondottiere, bot.getRole());
    }

    @Test
    void chooseRoleBishop2(){
        when(opponent.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Cathedrale),
                new Building(BuildingEnum.Chateau),
                new Building(BuildingEnum.Taverne),
                new Building(BuildingEnum.TourDeGuet),
                new Building(BuildingEnum.Marche)
        ));
        when(board.getPlayers()).thenReturn(List.of(opponent));
        when(board.getCharactersInfos(3).isAvailable()).thenReturn(false);
        when(board.getCharactersInfos(0).isAvailable()).thenReturn(false);
        when(board.getCharactersInfos(7).isAvailable()).thenReturn(false);

        bot.chooseRole();
        assertEquals(mockBishop, bot.getRole());
    }

    @Test
    void chooseRoleBishop3(){
        when(bot.getCity()).thenReturn(List.of(
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Cathedrale),
                new Building(BuildingEnum.Chateau),
                new Building(BuildingEnum.Taverne),
                new Building(BuildingEnum.TourDeGuet),
                new Building(BuildingEnum.Marche)
        ));
        when(board.getPlayers()).thenReturn(List.of(bot));

        bot.chooseRole();
        assertEquals(mockBishop, bot.getRole());
    }

    @Test
    void compareSameCostMilitaryVsReligion(){
        assertTrue(bot.compare(new Building(BuildingEnum.Caserne), new Building(BuildingEnum.Monastere)) > 0);
    }

    @Test
    void compareSameCostMilitaryVsCommercial(){
        assertTrue(bot.compare(new Building(BuildingEnum.Caserne), new Building(BuildingEnum.Comptoir)) < 0);
    }

    @Test
    void compareSameCostBishopVsCommercial(){
        assertTrue(bot.compare(new Building(BuildingEnum.Monastere), new Building(BuildingEnum.Comptoir)) < 0);
    }

    @Test
    void compareSameCostNobleVsCommercial(){
        assertTrue(bot.compare(new Building(BuildingEnum.Manoir), new Building(BuildingEnum.Comptoir)) == 0);
    }

    @Test
    void compare3goldMilitaryVs1goldReligion(){
        assertTrue(bot.compare(new Building(BuildingEnum.Caserne), new Building(BuildingEnum.Temple)) > 0);
    }

    @Test
    void compare3goldVs1gold(){
        assertTrue(bot.compare(new Building(BuildingEnum.Manoir), new Building(BuildingEnum.Taverne)) < 0);
    }
}

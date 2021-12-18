package fr.unice.polytech.startingpoint.charactersTest;

import fr.unice.polytech.startingpoint.Board;
import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;
import fr.unice.polytech.startingpoint.buildings.District;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestMerchant {
    Board board;
    Character king;
    Player player;
    Player archi;
    Player bishop;
    Player condo;
    Player merchant;
    Player magician;
    Character archiCharacter;
    Assassin assassinCharacter;
    Character thiefCharacter;
    Character condottiereCharacter;
    Magician magicianCharacter;
    Bishop bishopCharacter;
    Character merchantCharacter;

    @BeforeEach
    void setUp() {
        board = spy(new Board());
        king = new King();
        player = spy(new Player(board));
        archi = spy(new Player(board));
        condo = spy(new Player(board));
        bishop = spy(new Player(board));
        merchant = spy(new Player(board));
        magician = spy(new Player(board));
        archiCharacter = spy(Architect.class);
        assassinCharacter = spy(Assassin.class);
        thiefCharacter = spy(Thief.class);
        magicianCharacter = spy(Magician.class);
        condottiereCharacter = spy(Condottiere.class);
        bishopCharacter = spy(Bishop.class);
        merchantCharacter = spy(Merchant.class);
    }

    @Test
    void setTaxes() {
        player.refundGold(player.getGold());
        when(player.getCity()).thenReturn(List.of(new Building(BuildingEnum.Echoppe),
                new Building(BuildingEnum.Taverne),
                new Building(BuildingEnum.Marche)));
        merchantCharacter.collectTaxes(player, District.Commercial);
        assertEquals(3, player.getGold());
    }

    @Test
    void merchantGold() { //Merchant get 1 gold when play
        merchant.pickRole(5);
        when(board.getPlayers()).thenReturn(List.of(merchant));
        merchantCharacter.usePower(board);
        assertEquals(3,merchant.getGold());
    }

    @Test
    void merchantEmptyBank() { //Merchant don't get more gold bc empty bank
        merchant.pickRole(5);
        int moneyBank = board.getBank().getGold();
        merchant.takeMoney(moneyBank);
        when(board.getPlayers()).thenReturn(List.of(merchant));
        merchantCharacter.usePower(board);
        assertEquals(moneyBank+2, merchant.getGold());
        //Money Bank + 2 bc player starts with 2 golds
    }
}

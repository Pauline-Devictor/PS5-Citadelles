package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.Building;
import fr.unice.polytech.startingpoint.characters.*;
import fr.unice.polytech.startingpoint.buildings.BuildingEnum;

import fr.unice.polytech.startingpoint.characters.Character;
import fr.unice.polytech.startingpoint.strategies.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestCharacter {
    Player architectOne;
    Player architectTwo;
    Player p2;
    Player assassinOne;
    Player merchantOne;
    Player kingOne;
    Player bishopOne;
    Player condottiereOne;
    Player magicianOne;
    Player thiefOne;
    Character thiefRole;
    Board b;
    List<Building> buildings;
    Character magician = new Magician();
    Character merchant = new Merchant();

    @BeforeEach
    void setUp() {
        b = new Board();
        buildings = List.of(
                new Building(BuildingEnum.Manoir),
                new Building(BuildingEnum.Temple),
                new Building(BuildingEnum.Eglise),
                new Building(BuildingEnum.Taverne),
                new Building(BuildingEnum.Echoppe),
                new Building(BuildingEnum.Marche),
                new Building(BuildingEnum.TourDeGuet),//Military
                new Building(BuildingEnum.Prison),
                new Building(BuildingEnum.Caserne),
                new Building(BuildingEnum.Forteresse)
        );

        architectOne = spy(new Player(b));
        when(architectOne.getRole()).thenReturn(Optional.of(new Architect()));
        when(architectOne.getGold()).thenReturn(26);
        when(architectOne.getCardHand()).thenReturn(new ArrayList<>());
        p2 = new Player(b);

        assassinOne = spy(new Player(b));
        when(assassinOne.getRole()).thenReturn(Optional.of(new Assassin()));
        when(assassinOne.chooseVictim()).thenReturn(b.getCharacters().get(6));

        kingOne = spy(new Player(b));
        when(kingOne.getRole()).thenReturn(Optional.of(new King()));
        when(kingOne.getCardHand()).thenReturn(buildings);

        bishopOne = spy(new Player(b));
        when(bishopOne.getRole()).thenReturn(Optional.of(new Bishop()));
        when(bishopOne.getCardHand()).thenReturn(buildings);

        merchantOne = spy(new Player(b));
        when(merchantOne.getRole()).thenReturn(Optional.of(merchant));
        when(merchantOne.getCardHand()).thenReturn(buildings);

        condottiereOne = spy(new Player(b));
        when(condottiereOne.getRole()).thenReturn(Optional.of(new Condottiere()));
        when(condottiereOne.getCardHand()).thenReturn(buildings);

        thiefOne = spy(new Player(b));
        thiefRole = new Thief();
        when(thiefOne.getRole()).thenReturn(Optional.of(thiefRole));

        magicianOne = spy(new Player(b));
        //magician.setStolen(true);
        when(magicianOne.getRole()).thenReturn(Optional.of(magician));


        architectTwo = spy(new Player(b));
        when(architectTwo.getRole()).thenReturn(Optional.of(new Architect()));
    }

    @Test
    void build3() {//test that the Architect can build up to 3 buildings
        architectOne.play();
        assertEquals(3, architectOne.getNbBuildable());
    }

    @Test
    void draw2Cards() {
        int numberBuild = architectTwo.getCardHand().size();
        architectTwo.drawCards(2);
        assertEquals(numberBuild + 2, architectTwo.getCardHand().size());
    }

    @Test
    public void murder() {
        assassinOne.getRole().get().usePower(b);
        assertTrue(b.getCharactersInfos(6).isMurdered());
    }
    @Test
    public void taxesKing(){
        kingOne.play();
        assertEquals(kingOne.getTaxes(), 1);
    }
    @Test
    public void taxesBishop(){
        bishopOne.play();
        assertEquals(bishopOne.getTaxes(),2);
    }
    @Test
    public void taxesMerchant(){
        merchantOne.play();
        assertEquals(merchantOne.getTaxes(),3);
    }
    @Test
    public void taxesCondottiere(){
        condottiereOne.play();
        assertEquals(condottiereOne.getTaxes(),4);
    }

    @Test
    public void steal() {
        thiefOne.getRole().get().usePower(b);
        magicianOne.play();
        //assertEquals(thiefOne.getAmountStolen(),2);
    }

    @Test
    public void merchantGetOneGold() {
        System.out.println(merchantOne.getGold());
        merchant.usePower(b);
        assertEquals(merchantOne.getGold(), 3);
    }
}

package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.buildings.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Deck {
    private final ArrayList<Building> buildings;

    Deck(){
        buildings = new ArrayList<>();
        buildings.add(new MiracleCourtyard(BuildingEnum.CourDesMiracles));
        buildings.add(new Laboratory());
        buildings.add(new Manufactory());
        buildings.add(new Observatory());
        buildings.add(new Building(BuildingEnum.Cimetiere));
        buildings.add(new Library());
        buildings.add(new Building(BuildingEnum.EcoleDeMagie));
        buildings.add(new Building(BuildingEnum.Universite));
        buildings.add(new Building(BuildingEnum.Dracoport));
        for (int i = 0; i < 2; i++) {
            buildings.add(new Building(BuildingEnum.Cathedrale));
            buildings.add(new Building(BuildingEnum.Palais));
            buildings.add(new Building(BuildingEnum.HotelDeVille));
            buildings.add(new Building(BuildingEnum.Forteresse));
            buildings.add(new Building(BuildingEnum.Donjon));
        }
        for (int i = 0; i < 3; i++) {
            buildings.add(new Building(BuildingEnum.Temple));
            buildings.add(new Building(BuildingEnum.Monastere));
            buildings.add(new Building(BuildingEnum.Echoppe));
            buildings.add(new Building(BuildingEnum.Comptoir));
            buildings.add(new Building(BuildingEnum.Port));
            buildings.add(new Building(BuildingEnum.TourDeGuet));
            buildings.add(new Building(BuildingEnum.Prison));
            buildings.add(new Building(BuildingEnum.Caserne));
        }
        for (int i = 0; i < 4; i++) {
            buildings.add(new Building(BuildingEnum.Eglise));
            buildings.add(new Building(BuildingEnum.Chateau));
            buildings.add(new Building(BuildingEnum.Marche));
        }
        for (int i = 0; i < 5; i++) {
            buildings.add(new Building(BuildingEnum.Manoir));
            buildings.add(new Building(BuildingEnum.Taverne));
        }
    }

    public Optional<Building> drawACard() {
        if (buildings.size() > 0) {
            int index = new Random().nextInt(buildings.size());
            Optional<Building> b = Optional.of(buildings.get(index));
            buildings.remove(index);
            return b;
        }
        return Optional.empty();
    }

    public boolean isEmpty() {
        return buildings.size() <= 0;
    }

    public int numberOfCards() {
        return buildings.size();
    }

    public void putCard(Building b) {
        buildings.add(b);
    }
}

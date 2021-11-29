package fr.unice.polytech.startingpoint;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Deck {
    private final ArrayList<Building> buildings;

    Deck(){
        buildings = new ArrayList<>();
        buildings.add(new Building(BuildingEnum.CourDesMiracles));
        buildings.add(new Building(BuildingEnum.Laboratoire));
        buildings.add(new Building(BuildingEnum.Manufacture));
        buildings.add(new Building(BuildingEnum.Observatoire));
        buildings.add(new Building(BuildingEnum.Cimetiere));
        buildings.add(new Building(BuildingEnum.Bibiliotheque));
        buildings.add(new Building(BuildingEnum.EcoleDeMagie));
        buildings.add(new Building(BuildingEnum.Universite));
        buildings.add(new Building(BuildingEnum.Dracoport));
        for(int i=0;i<2;i++){
            buildings.add(new Building(BuildingEnum.Cathedrale));
            buildings.add(new Building(BuildingEnum.Palais));
            buildings.add(new Building(BuildingEnum.HotelDeVille));
            buildings.add(new Building(BuildingEnum.Forteresse));
            buildings.add(new Building(BuildingEnum.Donjon));
        }
        for(int i=0;i<3;i++){
            buildings.add(new Building(BuildingEnum.Temple));
            buildings.add(new Building(BuildingEnum.Monastere));
            buildings.add(new Building(BuildingEnum.Echoppe));
            buildings.add(new Building(BuildingEnum.Comptoir));
            buildings.add(new Building(BuildingEnum.Port));
            buildings.add(new Building(BuildingEnum.TourDeGuet));
            buildings.add(new Building(BuildingEnum.Prison));
            buildings.add(new Building(BuildingEnum.Caserne));
        }
        for(int i=0;i<4;i++){
            buildings.add(new Building(BuildingEnum.Eglise));
            buildings.add(new Building(BuildingEnum.Chateau));
            buildings.add(new Building(BuildingEnum.Marche));
        }
        for(int i=0;i<5;i++){
            buildings.add(new Building(BuildingEnum.Manoir));
            buildings.add(new Building(BuildingEnum.Taverne));
        }
    }

     Building drawACard(){
        if (buildings.size()>0) {
            int index = new Random().nextInt(buildings.size());
            Building b = buildings.get(index);
            buildings.remove(index);
            return (b);
        }
        return null;
    }

    boolean isEmpty(){
        return buildings.size()<=0;
    }

    int numberOfCards() {
        return buildings.size();
    }
}

package fr.unice.polytech.startingpoint;

public enum BuildingEnum {
    Temple("Temple",1,District.Religion),
    Eglise("Eglise",2,District.Religion),
    Cathedrale("Cathédrale",5,District.Religion),
    Monastere("Monastère",3,District.Religion),
    Manoir("Manoir",3,District.Noble),
    Chateau("Chateau",4,District.Noble),
    Palais("Palais",5,District.Noble),
    Taverne("Taverne",1,District.Commercial),
    Echoppe("Échoppe",2,District.Commercial),
    Marche("Marche",2,District.Commercial),
    Comptoir("Comptoir",3,District.Commercial),
    Port("Port",4,District.Commercial),
    HotelDeVille("Hotel De Ville",5,District.Commercial),
    TourDeGuet("Tour De Guet",1,District.Military),
    Prison("Prison",2,District.Military),
    Caserne("Caserne",3,District.Military),
    Forteresse("Forteresse",5,District.Military),
    CourDesMiracles("Cour Des Miracles",2,District.Prestige),
    Donjon("Donjon",3,District.Prestige),
    Laboratoire("Laboratoire",5,District.Prestige),
    Manufacture("Manufacture",5,District.Prestige),
    Observatoire("Observatoire",5,District.Prestige),
    Cimetiere("Cimetière",5,District.Prestige),
    Bibiliotheque("Bibliothèque",6,District.Prestige),
    Universite("Université",6,District.Prestige),
    Dracoport("Dracoport",6,District.Prestige),
    EcoleDeMagie("École De Magie",6,District.Prestige);

    private final String name;
    private final int cost;
    private final District district;

    BuildingEnum(String n,int c,District d){
        name=n;
        cost=c;
        district=d;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public District getDistrict() {
        return district;
    }

    @Override
    public String toString() {
        return name+", Cout : "+cost+", Quartier : "+district;
    }



}

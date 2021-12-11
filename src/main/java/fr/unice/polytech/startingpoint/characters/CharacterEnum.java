package fr.unice.polytech.startingpoint.characters;

public enum CharacterEnum {

    Assassin("Assassin", 1),
    Thief("Voleur", 2),
    Magician("Magicien", 3),
    King("Roi", 4),
    Bishop("Eveque", 5),
    Merchant("Marchand", 6),
    Architect("Architecte", 7),
    Condottiere("Condottiere", 8);

    private final String name;
    private final int order;

    CharacterEnum(String name, int order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }
}

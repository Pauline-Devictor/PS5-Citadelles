package fr.unice.polytech.startingpoint.characters;

public enum CharacterEnum {

    Assassin("Assassin", 0),
    Thief("Voleur", 1),
    Magician("Magicien", 2),
    King("Roi", 3),
    Bishop("Eveque", 4),
    Merchant("Marchand", 5),
    Architect("Architecte", 6),
    Condottiere("Condottiere", 7);

    private final String name;
    private final int order;

    CharacterEnum(String name, int order) {
        this.name = name;
        this.order = order;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return order
     */
    public int getOrder() {
        return order;
    }
}

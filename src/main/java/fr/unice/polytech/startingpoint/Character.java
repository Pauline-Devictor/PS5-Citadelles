package fr.unice.polytech.startingpoint;

public abstract class Character {
     static int order;

}
class Assassin extends Character{
    private static final int order = 1;

}
class Thief extends Character{
    private static final int order =2;
}
class Magician extends Character{
    private static final int order =3;
}
class King extends Character{
    private static final int order =4;
}
class Bishop extends Character{
    private static final int order =5;
}
class Merchant extends Character{
    private static final int order =6;
}
class Architect extends Character{
    private static final int order =7;
}
class Condottiere extends Character{
    private static final int order =8;
}


package fr.unice.polytech.startingpoint;

public enum Strategies {
    balanced("Default",0), //Comportement par defaut, pose le premier batiment possible a chaque fois
    lowGold("Zerg",1),//Pose des petits batiments pour finir le plus vite possible
    highGold("Terran",1); //Pose uniquement des gros batiments pour accumuler des points

    Strategies(String name,int i) {
        this.name=name;
        strategie=i;
    }

    private final String name;
    private final int strategie;

    static Strategies pickAStrat(int nbStrat){
        switch (nbStrat){
            case 1 -> {
                return lowGold;
            }
            case 2 -> {
                return highGold;
            }
            default -> {
                return balanced;
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getStrategie() {
        return strategie;
    }

    @Override
    public String toString() {
        return name;
    }
}

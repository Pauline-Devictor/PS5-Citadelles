package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class SaveTest {
    Save save;

    @BeforeEach
    void setUp() {
        save = new Save("test");
        save.writeLine("Tests de la sauvegarde", true);
    }

    @Test
    void saveGame() {
        List<String[]> tmp = save.readFile();
        Map<String, int[]> map = new TreeMap<>();
        map.put("test", new int[]{1, 2, 3, 4});
        save.saveGame(map);
        assertNotEquals(tmp.size(), save.readFile().size());
    }

    @Test
    void writeLine() {
        List<String[]> tmp = save.readFile();
        save.writeLine("test", false);
        assertNotEquals(tmp.size(), save.readFile().size() + 1);
    }

    @Test
    void readFile() {
        save.writeLine("header", true);
        save.writeLine("test", false);
        List<String[]> tmp = save.readFile();
        assertEquals(1, tmp.size());
        assertEquals(tmp.get(0)[0], "test");
    }

    @Test
    void computeDataSingleInfos() {
        List<String[]> data = List.of(
                new String[]{"Nom1-X", "1", "2", "3", "4", "5", "6"},
                new String[]{"Nom2-X", "1", "2", "3", "4", "5", "6"},
                new String[]{"Nom3-X", "1", "2", "3", "4", "5", "6"},
                new String[]{"Nom4-X", "1", "2", "3", "4", "5", "6"}
        );
        List<String> res = List.of(
                "Nom1,1.0,2.0,3,4,5,6,",
                "Nom2,1.0,2.0,3,4,5,6,",
                "Nom3,1.0,2.0,3,4,5,6,",
                "Nom4,1.0,2.0,3,4,5,6,"
        );
        assertEquals(res, save.computeData(data));
    }

    @Test
    void computeDataDoubleInfos() {
        List<String[]> data = List.of(
                new String[]{"Nom1-X", "1", "2", "3", "4", "5", "6"},
                new String[]{"Nom2-X", "1", "2", "3", "4", "5", "6"},
                new String[]{"Nom2-X", "1", "2", "3", "4", "5", "6"},
                new String[]{"Nom1-X", "1", "2", "3", "4", "5", "6"}
        );
        List<String> res = List.of(
                "Nom1,1.0,2.0,6,8,10,12,",
                "Nom2,1.0,2.0,6,8,10,12,"
        );
        assertEquals(res, save.computeData(data));
    }

    @Test
    void computeDataDoubleValuesInfos() {
        List<String[]> data = List.of(
                new String[]{"Nom1-X", "1", "3", "3", "4", "5", "8"},
                new String[]{"Nom1-X", "1", "4", "3", "4", "5", "4"},
                new String[]{"Nom2-X", "1", "2", "3", "7", "5", "2"},
                new String[]{"Nom2-X", "2", "2", "3", "4", "5", "6"}
        );
        List<String> res = List.of(
                "Nom1,1.0,3.5,6,8,10,12,",
                "Nom2,1.5,2.0,6,11,10,8,"
        );
        assertEquals(res, save.computeData(data));
    }

    @Test
    void computeDataAllInfos() {
        List<String[]> data = List.of(
                new String[]{"Nom1-X", "1", "3", "3", "4", "5", "8"},
                new String[]{"Nom1-X", "1", "4", "3", "4", "5", "4"},
                new String[]{"Nom1-X", "1", "2", "3", "7", "5", "2"},
                new String[]{"Nom1-X", "2", "2", "3", "4", "5", "6"}
        );
        List<String> res = List.of("Nom1,1.25,2.75,12,19,20,20,");
        assertEquals(res, save.computeData(data));
    }

    @Test
    void computeDataEmpty() {
        List<String[]> data = new ArrayList<>();
        assertEquals(new ArrayList<>(), save.computeData(data));
    }

}
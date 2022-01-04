package fr.unice.polytech.startingpoint.csv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCsvRead {
    CsvWrite writer;
    CsvRead reader;
    PrintStream outMock;

    @BeforeEach
    void setup(){
        LOGGER.setLevel(Level.OFF);
        writer = new CsvWrite();
        writer.write("");
        reader = new CsvRead();
        outMock = Mockito.mock(PrintStream.class);
        System.setOut(outMock);
    }
    @Test
    void printNotEmpty(){
        writer.write("A message to add");
        reader.printCsv("src/main/resources/save/stats.csv");
        Mockito.verify(outMock).println("[A message to add]");
    }
    @Test
    void printEmpty(){
        reader.printCsv("src/main/resources/save/stats.csv");
        Mockito.verify(outMock).println("[]");
    }
    @Test
    void readNotEmpty(){
        writer.write("A message to add");
        ArrayList<String> data;
        data = (ArrayList<String>) reader.readCsv("src/main/resources/save/stats.csv");
        assertEquals(List.of("[A message to add]"),data);
    }
    @Test
    void readEmpty(){
        ArrayList<String> data;
        data = (ArrayList<String>) reader.readCsv("src/main/resources/save/stats.csv");
        assertEquals(List.of("[]"),data);
    }
}

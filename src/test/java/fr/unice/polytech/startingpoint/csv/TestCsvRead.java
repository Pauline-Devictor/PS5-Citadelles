package fr.unice.polytech.startingpoint.csv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintStream;
import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;

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
    void readNotEmpty(){
        writer.write("A message to add");
        reader.read();
        Mockito.verify(outMock).println("[A message to add]");
    }
    @Test
    void readEmpty(){
        reader.read();
        Mockito.verify(outMock).println("[]");
    }
}

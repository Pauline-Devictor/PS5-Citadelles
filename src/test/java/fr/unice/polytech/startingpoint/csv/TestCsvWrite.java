package fr.unice.polytech.startingpoint.csv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintStream;
import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static org.mockito.Mockito.*;

public class TestCsvWrite {
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
    void writeEmpty(){
        writer.write("");
        reader.printCsv("src/main/resources/save/stats.csv");
        Mockito.verify(outMock).println("[]");
    }
    @Test
    void writeMessage(){
        writer.write("a message");
        reader.printCsv("src/main/resources/save/stats.csv");
        Mockito.verify(outMock).println("[a message]");
    }
    @Test
    void appendVoid(){
        writer.append("");
        reader.printCsv("src/main/resources/save/stats.csv");
        verify(outMock,times(2)).println("[]");
    }
    @Test
    void appendSomething(){
        writer.append("Here some text to test");
        reader.printCsv("src/main/resources/save/stats.csv");
        Mockito.verify(outMock).println("[Here some text to test]");
    }
    @Test
    void appendAfterWriting(){
        writer.write("write");
        writer.append("append");
        reader.printCsv("src/main/resources/save/stats.csv");
        verify(outMock,times(2)).println(anyString());
    }
}

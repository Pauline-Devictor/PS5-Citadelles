package fr.unice.polytech.startingpoint.csv;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static org.mockito.Mockito.*;

/*public class TestCsvWrite {
    CsvWrite writer;
    CsvRead reader;
    PrintStream outMock;
    Map<String, int[]> data;

    @BeforeEach
    void setup() {
        LOGGER.setLevel(Level.OFF);
        writer = new CsvWrite();
        writer.write("");
        reader = new CsvRead();

        outMock = Mockito.mock(PrintStream.class);
        System.setOut(outMock);

        data = new TreeMap<>();
    }

    //@Test
    void writeEmpty() {
        writer.write("");
        reader.printCsv("save/stats.csv");
        Mockito.verify(outMock).println("[]");
    }

    //@Test
    void writeMessage() {
        writer.write("a message");
        reader.printCsv("save/stats.csv");
        Mockito.verify(outMock).println("[a message]");
    }

    //@Test
    void appendVoid() {
        writer.append("");
        reader.printCsv("save/stats.csv");
        verify(outMock, times(2)).println("[]");
    }

    //@Test
    void appendSomething() {
        writer.append("Here some text to test");
        reader.printCsv("save/stats.csv");
        Mockito.verify(outMock).println("[Here some text to test]");
    }

    //@Test
    void appendAfterWriting() {
        writer.write("write");
        writer.append("append");
        reader.printCsv("save/stats.csv");
        verify(outMock, times(2)).println(anyString());
    }

    // @Test
    void appendStatsEmpty() {
        writer.appendStats(data);
        reader.printCsv("save/stats.csv");
        Mockito.verify(outMock).println("[]");
    }

    //@Test
    void appendStatsOneLine() {
        data.put("Tester", new int[4]);
        writer.appendStats(data);

        reader.printCsv("save/stats.csv");
        Mockito.verify(outMock).println("[]");
        Mockito.verify(outMock).println("[Tester\",\"0\",\"0.0%\",\"0\",\"0\",\"0\",\"1000]");
    }

    //@Test
    void appendStatsManyLine() {
        data.put("AlphaTester", new int[4]);
        data.put("BetaTester", new int[4]);
        data.put("GammaTester", new int[4]);
        writer.appendStats(data);
        reader.printCsv("save/stats.csv");
        Mockito.verify(outMock).println("[]");
        Mockito.verify(outMock).println("[AlphaTester\",\"0\",\"0.0%\",\"0\",\"0\",\"0\",\"1000]");
        Mockito.verify(outMock).println("[BetaTester\",\"0\",\"0.0%\",\"0\",\"0\",\"0\",\"1000]");
        Mockito.verify(outMock).println("[GammaTester\",\"0\",\"0.0%\",\"0\",\"0\",\"0\",\"1000]");
    }
}*/

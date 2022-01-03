package fr.unice.polytech.startingpoint.csv;
import fr.unice.polytech.startingpoint.Csv.CsvRead;
import fr.unice.polytech.startingpoint.Csv.CsvWrite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintStream;

public class TestCsvRead {
    CsvWrite writer;
    CsvRead reader;
    PrintStream outMock;

    @BeforeEach
    void setup(){
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

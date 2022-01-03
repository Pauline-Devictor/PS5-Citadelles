package fr.unice.polytech.startingpoint.csv;
import fr.unice.polytech.startingpoint.Csv.CsvRead;
import fr.unice.polytech.startingpoint.Csv.CsvWrite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintStream;
import static org.mockito.Mockito.*;

public class TestCsvWrite {
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
    void writeEmpty(){
        writer.write("");
        reader.read();
        Mockito.verify(outMock).println("[]");
    }
    @Test
    void writeMessage(){
        writer.write("a message");
        reader.read();
        Mockito.verify(outMock).println("[a message]");
    }
    @Test
    void appendVoid(){
        writer.append("");
        reader.read();
        verify(outMock,times(2)).println("[]");
    }
    @Test
    void appendSomething(){
        writer.append("Here some text to test");
        reader.read();
        Mockito.verify(outMock).println("[Here some text to test]");
    }
    @Test
    void appendAfterWriting(){
        writer.write("write");
        writer.append("append");
        reader.read();
        verify(outMock,times(2)).println(anyString());
    }
}

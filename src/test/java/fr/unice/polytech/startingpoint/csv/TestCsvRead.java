package fr.unice.polytech.startingpoint.csv;

/*public class TestCsvRead {
    CsvWrite writer;
    CsvRead reader;
    PrintStream outMock;

    @BeforeEach
    void setup() {
        LOGGER.setLevel(Level.OFF);
        writer = new CsvWrite();
        writer.write("");
        reader = new CsvRead();
        outMock = Mockito.mock(PrintStream.class);
        System.setOut(outMock);
    }

    //@Test
    void printNotEmpty() {
        writer.write("A message to add");
        reader.printCsv("save/stats.csv");
        Mockito.verify(outMock).println("[A message to add]");
    }

    //@Test
    void printEmpty() {
        reader.printCsv("save/stats.csv");
        Mockito.verify(outMock).println("[]");
    }

    @Test
    void readNotEmpty() {
        writer.write("A message to add");
        ArrayList<String> data;
        data = reader.readCsv("save/stats.csv");
        assertEquals(List.of("[A message to add]"), data);
    }

    @Test
    void readEmpty() {
        ArrayList<String> data;
        data = reader.readCsv("save/stats.csv");
        assertEquals(List.of("[]"), data);
    }
}*/

package fr.unice.polytech.startingpoint.csv;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.util.Arrays;

import static fr.unice.polytech.startingpoint.Game.LOGGER;

public class CsvRead {
    public void read() {
        //Build reader instance
        //Read stats.csv
        //Default seperator is comma
        //Default quote character is double quote
        //Start reading from line number 1 (line numbers start from zero)
        try {
            CSVReader reader = new CSVReader(new FileReader("src/main/resources/save/stats.csv"), ';', '"', 0);

            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                //Verifying the read data here
                System.out.println(Arrays.toString(nextLine));
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to read :" + e.getMessage());
        }
    }
}

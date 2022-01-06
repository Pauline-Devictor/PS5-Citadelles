package fr.unice.polytech.startingpoint.csv;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import static fr.unice.polytech.startingpoint.Game.LOGGER;

public class CsvRead {

    /**
     * print the data from the file of the given file
     *
     * @param path of the file to be read
     */
    public void printCsv(String path) {
        try {
            CSVReader reader = new CSVReader(new FileReader(path), ';', '"', 0);

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                //TODO Affichage
                //LOGGER.finest(Arrays.toString(nextLine));
                //System.out.println(Arrays.toString(nextLine));
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to print :" + e.getMessage());
        }
    }

    /**
     * Stock datas from a file with the given path
     *
     * @param path of the file
     * @return data contain in target csv
     */
    public ArrayList<String> readCsv(String path) {
        ArrayList<String> data = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(path), ';', '"', 0);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                data.add(Arrays.toString(nextLine));

            }
            return data;
        } catch (Exception e) {
            LOGGER.severe("Failed to read :" + e.getMessage());
        }
        return null;
    }
}

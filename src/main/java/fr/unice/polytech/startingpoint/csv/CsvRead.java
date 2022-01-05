package fr.unice.polytech.startingpoint.csv;

import au.com.bytecode.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static fr.unice.polytech.startingpoint.Game.LOGGER;

public class CsvRead {
    /**
     *
     * @param path
     */
    public void printCsv(String path) {
        //Build reader instance
        //Read stats.csv
        //Default seperator is comma
        //Default quote character is double quote
        //Start reading from line number 1 (line numbers start from zero)
        try {
            CSVReader reader = new CSVReader(new FileReader(path), ';', '"', 0);

            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                //Verifying the read data here
                System.out.println(Arrays.toString(nextLine));
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to print :" + e.getMessage());
        }
    }

    /**
     * Stock datas in variable data
     * @param path
     * @return data contain in target csv
     */
    //utilisable pour recuperer les donnes de results csv pour actualiser les stats
    public ArrayList<String> readCsv(String path) {
        ArrayList<String> data = new ArrayList<String>();
        //Build reader instance
        //Read stats.csv
        //Default seperator is comma
        //Default quote character is double quote
        //Start reading from line number 1 (line numbers start from zero)
        try {
            CSVReader reader = new CSVReader(new FileReader(path), ';', '"', 0);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                //Verifying the read data here
                data.add(Arrays.toString(nextLine));

            }
            return data;
        } catch (Exception e) {
            LOGGER.severe("Failed to read :" + e.getMessage());
        }
        return null;
    }
    public void resetStats(){

    }
}

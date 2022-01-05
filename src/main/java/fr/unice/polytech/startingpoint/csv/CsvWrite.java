package fr.unice.polytech.startingpoint.csv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import static fr.unice.polytech.startingpoint.Game.LOGGER;

public class CsvWrite {
    //TODO Path
    public void write(String data) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("save/stats.csv"));

            //Create record
            String[] record = data.split(",");
            //Write the record to file
            writer.writeNext(record);

            //close the writer
            writer.close();
        } catch (Exception e) {
            LOGGER.severe("Error during writing : " + e.getMessage());
        }
    }

    public void append(String data) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("save/stats.csv", true));

            String[] record = data.split(",");
            writer.writeNext(record);
            writer.close();

        } catch (Exception e) {
            LOGGER.severe("Error during append : " + e.getMessage());
        }
    }

    public void save(){
        try {
            String csv = "save/results.csv";
            CSVWriter writer = new CSVWriter(new FileWriter(csv, true));

            CSVReader reader = new CSVReader(new FileReader("save/stats.csv"), ',', '"', 1);

            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                writer.writeNext(nextLine);
            }
            writer.close();
        } catch (Exception e) {
            LOGGER.severe("Error during save : " + e.getMessage());
        }
    }

    public void appendStats(Map<String, int[]> stats) {
            stats.forEach((k, v) -> {
                int defaites = 1000 - v[1] - v[2];
                float winrate = (float) v[1]/10;
                int averageScore = v[0]/1000;

                //Nom;ScoreMoyen;Pourcentage Victoire;NbParties;Victoire;Egalite;Defaite
                append(k+","+averageScore+","+ winrate +"%,"+v[3]+","+v[1]+","+v[2]+","+ defaites);

                //Nom du bot, Score moyen, Victoires, Egalites,Defaites Nb de parties
                // append(k+","+v[1]+","+v[2]+","+ defaites + ","+v[3]);
            });
    }

    //TODO methode pour reset le fichier stats.csv proprement (sans write("") qui ajoute une ligne vide)
    public void resetStats(){

    }
}

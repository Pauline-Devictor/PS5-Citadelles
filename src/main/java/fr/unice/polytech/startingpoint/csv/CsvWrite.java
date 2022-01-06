package fr.unice.polytech.startingpoint.csv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

import static fr.unice.polytech.startingpoint.Game.LOGGER;

public class CsvWrite {
    //TODO Path & Buffer
    //TODO Verifier si stats.csv est essentiel

    /**
     * overwrite the file stats.csv with the given data
     *
     * @param data that have to be written
     */
    public void write(String data) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("save/stats.csv"));

            String[] record = data.split(",");
            writer.writeNext(record);

            writer.close();
        } catch (Exception e) {
            LOGGER.severe("Error during writing : " + e.getMessage());
        }
    }

    /**
     * append in the file stats.csv the given data
     *
     * @param data that have to be appended
     */
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

    /**
     * save all stats from stats.csv in results.csv
     */
    public void save() {
        try {
            String csv = "save/results.csv";
            CSVWriter writer = new CSVWriter(new FileWriter(csv, true));
            CSVReader reader = new CSVReader(new FileReader("save/stats.csv"), ',', '"', 1);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                writer.writeNext(nextLine);
            }
            writer.close();
        } catch (Exception e) {
            LOGGER.severe("Error during save : " + e.getMessage());
        }
    }

    /**
     * append all the given stats data in the file stats
     *
     * @param stats of some games
     */
    public void appendStats(Map<String, int[]> stats) {
        stats.forEach((k, v) -> {
            int defeat = 1000 - v[1] - v[2];
            float winrate = (float) v[1] / 10;
            int averageScore = v[0] / 1000;

            //Nom;ScoreMoyen;PourcentageVictoire;NbParties;Victoire;Egalite;Defaite
            append(k + "," + averageScore + "," + winrate + "%," + v[3] + "," + v[1] + "," + v[2] + "," + defeat);

            //Nom du bot, Score moyen, Victoires, Egalites,Defaites Nb de parties
            // append(k+","+v[1]+","+v[2]+","+ defeat + ","+v[3]);
        });
    }
}

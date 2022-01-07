package fr.unice.polytech.startingpoint;

import au.com.bytecode.opencsv.CSVWriter;
import fr.unice.polytech.startingpoint.csv.CsvWrite;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

public class Save {
    private final String fileName;

    Save(String fileName) {
        this.fileName = fileName;
    }

    public void saveGame(Map<String, int[]> stats) {
        writeLine(getDate());
        for (Map.Entry<String, int[]> entry : stats.entrySet()) {
            String k = entry.getKey();
            int[] v = entry.getValue();
            int defeat = 1000 - v[1] - v[2];
            float winRate = (float) v[1] / 10;
            int averageScore = v[0] / 1000;

            //Nom;ScoreMoyen;PourcentageVictoire;NbParties;Victoire;Egalite;Defaite
            writeLine(k + "," + averageScore + "," + winRate + "%," + v[3] + "," + v[1] + "," + v[2] + "," + defeat + ",");

        }
    }

    private void writeLine(String data) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName, true));
            String[] record = data.split(",");
            writer.writeNext(record);
            writer.close();
        } catch (Exception e) {
            System.out.println("Les données n'ont pas été sauvegardés : " + data);
        }
    }

    /**
     * Get the date time
     *
     * @return current Date
     */
    public String getDate() {
        DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.MEDIUM);
        return (mediumDateFormat.format(new Date()));
    }
}

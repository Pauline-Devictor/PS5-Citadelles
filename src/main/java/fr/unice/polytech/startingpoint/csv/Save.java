package fr.unice.polytech.startingpoint.csv;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import static fr.unice.polytech.startingpoint.Game.LOGGER;

public class Save {
    private final String fileName;

    public Save(String fileName) {
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
            writeLine(k + "," + averageScore + "," + winRate + "%," + v[3] + "," + v[1] + "," + v[2] + "," + defeat + ",");

        }
    }

    private void writeLine(String data) {
        try {
            Path file = Path.of("save", fileName + ".csv");
            boolean exist = Files.exists(file);
            CSVWriter writer = new CSVWriter(new FileWriter(file.toString(), true));
            if (!exist)
                writer.writeNext(("Nom,ScoreMoyen,PourcentageVictoire,NbParties,Victoire,Egalite,Defaite").split(","));
            String[] record = data.split(",");
            writer.writeNext(record);
            writer.close();
        } catch (Exception e) {
            LOGGER.severe("Les données n'ont pas été sauvegardés : " + data);
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
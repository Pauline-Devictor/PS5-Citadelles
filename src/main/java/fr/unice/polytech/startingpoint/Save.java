package fr.unice.polytech.startingpoint;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static fr.unice.polytech.startingpoint.Game.LOGGER;
import static java.lang.Math.min;

/**
 * Write and computed results in order to save data from this game
 * Average winning rate, number of parties, average score...
 */
public final class Save {
    private final Path path;

    /**
     * Create a path with the name of the file
     *
     * @param fileName name of the file
     */
    public Save(String fileName) {
        path = Path.of("save", fileName + ".csv");
    }

    /**
     * Save the game in the file in attribute
     *
     * @param stats Stats of few games
     */
    public void saveGame(Map<String, int[]> stats) {
        writeLine(getDate(), false);
        for (Map.Entry<String, int[]> entry : stats.entrySet()) {
            String k = entry.getKey();
            int[] v = entry.getValue();
            int defeat = 1000 - v[1] - v[2];
            float winRate = (float) v[1] / 10;
            int averageScore = v[0] / 1000;
            writeLine(k + "," + averageScore + "," + winRate + "," + v[3] + "," + v[1] + "," + v[2] + "," + defeat + ",", false);
        }
        updateData();
    }

    /**
     * Update overall stats
     */
    private void updateData() {
        List<String[]> save = readFile(); //Read the file
        Pattern digitFind = Pattern.compile("[0-9]+.......[0-9]+");
        int startLogs = 0;
        for (int i = 0; i < save.size(); i++) {
            if (save.get(i).length <= 1) {
                startLogs = i;
                break;
            }
        }
        List<String[]> logs = save.subList(min(startLogs + 1, save.size()), save.size());//Remove Overall data from it
        List<String> header = computeData(logs.stream().filter(e -> !digitFind.matcher(e[0]).find()).toList());//Take out dates, and compute the data
        rewriteFile(logs, header);
    }

    /**
     * Writing on the file header then save, with the correct layout
     *
     * @param save   logs of games
     * @param header overall stats
     */
    private void rewriteFile(List<String[]> save, List<String> header) {
        writeLine("Nom,ScoreMoyen,PourcentageVictoire,NbParties,Victoire,Égalité,Défaite", true);
        for (String value : header) {
            writeLine(value, false);
        }
        writeLine("Logs Complets", false);
        save.forEach(e -> {
            StringBuilder line = new StringBuilder();
            for (String s : e)
                line.append(s).append(",");
            writeLine(line.toString(), false);
        });
    }

    /**
     * @param data any logs corresponding to Name, stats in at least 5 cases
     * @return List of Strategies with their results
     */
    public List<String> computeData(List<String[]> data) {
        List<String> computedData = new ArrayList<>();
        List<String[]> mutableData = new ArrayList<>();
        for (String[] s : List.copyOf(data))
            mutableData.add(s.clone());
        mutableData.forEach(e -> e[0] = e[0].substring(0, e[0].length() - 2));
        List<String[]> computed;
        while (!mutableData.isEmpty()) {
            String[] sample = mutableData.get(0);
            computed = mutableData.stream().filter(e -> e[0].equals(sample[0])).toList();
            //Si le nom correspond, on retire l'échantillon
            mutableData.removeIf(e -> e[0].equals(sample[0]));
            computedData.add(newStatsLine(computed));
        }
        return computedData;
    }

    /**
     * Compute logs from a strategy
     *
     * @param computed Logs of a Strategy
     * @return results of this strategy, ready to written down
     */
    private String newStatsLine(List<String[]> computed) {
        String name = computed.get(0)[0];
        float scoreMoyen = computed.stream().map(e -> Float.valueOf(e[1])).reduce((float) 0, Float::sum) / computed.size();
        float pourcentageMoyen = computed.stream().map(e -> Float.valueOf(e[2])).reduce((float) 0, Float::sum) / computed.size();
        int nbPartiesTotales = computed.stream().map(e -> Integer.valueOf(e[3])).reduce(0, Integer::sum);
        int totalWins = computed.stream().map(e -> Integer.valueOf(e[4])).reduce(0, Integer::sum);
        int totalDraws = computed.stream().map(e -> Integer.valueOf(e[5])).reduce(0, Integer::sum);
        int totalLosses = computed.stream().map(e -> Integer.valueOf(e[6])).reduce(0, Integer::sum);
        return (name + "," + scoreMoyen + "," + pourcentageMoyen + "," + nbPartiesTotales + "," + totalWins + "," + totalDraws + "," + totalLosses + ",");
    }

    /**
     * Read the file
     *
     * @return array of string-array with one line in each String array
     */
    public List<String[]> readFile() {
        List<String[]> allRows = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(path.toString()), ',', '"', 1);
            allRows = reader.readAll(); //Read all rows at once
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        return allRows;
    }

    /**
     * Write the given line on the file in attribute
     * reset the file before writing if overwrite is true
     *
     * @param data      line to be written
     * @param overwrite is the reset of the file needed ?
     */
    public void writeLine(String data, boolean overwrite) {
        try {
            boolean exist = Files.exists(path);
            CSVWriter writer = new CSVWriter(new FileWriter(path.toString(), !overwrite));
            if (!exist)// if the file is new, write the first line
                writer.writeNext(("Nom,ScoreMoyen,PourcentageVictoire,NbParties,Victoire,Égalité,Défaite").split(","));
            String[] record = data.split(",");
            writer.writeNext(record); //Split and write the line
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
    private String getDate() {
        DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.MEDIUM);
        return (mediumDateFormat.format(new Date()));
    }
}
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

public class Save {
    private final Path path;

    public Save(String fileName) {
        path = Path.of("save", fileName + ".csv");
    }

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
        List<String[]> save = readFile();
        Pattern digitFind = Pattern.compile("[0-9]+.......[0-9]+");
        int startLogs = 0;
        for (int i = 0; i < save.size(); i++) {
            if (save.get(i).length <= 1) {
                startLogs = i;
                break;
            }
        }
        List<String[]> logs = save.subList(startLogs + 1, save.size());
        logs = logs.stream().filter(e -> !digitFind.matcher(e[0]).find()).toList();
        List<String> header = computeData(logs);
        header.forEach(System.out::println);
        System.out.println("Split");
        logs.forEach(e -> System.out.println(e[0]));
        rewriteFile(logs, header);
    }

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

    private List<String> computeData(List<String[]> data) {
        List<String> computedData = new ArrayList<>();
        List<String[]> mutableData = new ArrayList<>();
        for (String[] s : List.copyOf(data))
            mutableData.add(s.clone());
        mutableData.forEach(e -> e[0] = e[0].substring(0, e[0].length() - 2));
        System.out.println(data + "\n" + mutableData);
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


    private List<String[]> readFile() {
        //Build reader instance
        List<String[]> allRows = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(path.toString()), ',', '"', 1);
            allRows = reader.readAll(); //Read all rows at once
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        return allRows;
    }

    private void writeLine(String data, boolean overwrite) {
        try {
            boolean exist = Files.exists(path);
            CSVWriter writer = new CSVWriter(new FileWriter(path.toString(), !overwrite));
            if (!exist)
                writer.writeNext(("Nom,ScoreMoyen,PourcentageVictoire,NbParties,Victoire,Égalité,Défaite").split(","));
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
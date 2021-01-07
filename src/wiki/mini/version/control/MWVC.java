package wiki.mini.version.control;

import wiki.mini.BasicFunctionLibrary;
import wiki.mini.Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.HashMap;

public class MWVC {

    public static HashMap<Integer, String> versions = new HashMap<>();

    public static int getCurrentVersion() {
        readVersions(Main.currentFile.getAbsolutePath());
        return versions.size();
    }

    public static void readVersions(String file) {
        try (BufferedReader rd = Files.newBufferedReader(Paths.get(file))) {
            String line;
            int version = 0;
            while ((line = rd.readLine()) != null) {
                versions.put(version, line.trim().split(":\\d+:")[1]);
                version++;
            }
        } catch (IOException e) {
            BasicFunctionLibrary.createRequest("Error", "File IO", "There was an error during the process of reading the versions!");
        }
    }

    public static void createVersion() {
        String[] content = Main.textArea.getText().split("\n+");
        StringBuilder toWrite = new StringBuilder(Integer.toString(getCurrentVersion() + 1));
        readVersions(Main.currentFile.getAbsolutePath());
        for (int i = 0; i < content.length; i++) {
            for (String version : versions.values()) {
                for (String change : version.split(";")) {
                    if (!(content[i].equals(change.split(",")[1])
                            && i == Integer.parseInt(change.split(",")[0]))) {
                        toWrite.append(i).append(",\"").append(content[i]).append("\"");
                    }
                }
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(Main.currentFile, true);
            fileWriter.write(toWrite.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

package wiki.mini.version.control;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import wiki.mini.BasicFunctionLibrary;
import wiki.mini.Main;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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
            outer:
            while ((line = rd.readLine()) != null) {
                try {
                    versions.put(version, line.trim().split(":\\d+:")[1]);
                    version++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue outer;
                }
            }
        } catch (IOException e) {
            BasicFunctionLibrary.createRequest("Error", "File IO", "There was an error during the process of reading the versions!");
        }
    }

    public static void createVersion() {
        if (Main.currentFile == null) {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(Main.stage);
            try {
                file.createNewFile();
                Main.currentFile = file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        readVersions(Main.currentFile.getAbsolutePath());
        String toWrite = diffVersions();
        try {
            FileWriter fileWriter = new FileWriter(Main.currentFile, true);
            fileWriter.write(toWrite);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createOldLastVersion() {
        String[] content = new String[12345];
        for (String line : MWVC.versions.values()) {
            for (String change : line.split(";")) {
                content[Integer.parseInt(change.split(",")[0])] = change.split(",")[1];
            }
        }
        return BasicFunctionLibrary.ArrayToString(content);
    }

    public static String diffVersions() {
        String[] content = Main.textArea.getText().split("\n");
        String[] oldContent = createOldLastVersion().split("\n");
        StringBuilder toWrite = new StringBuilder("\n:" + (getCurrentVersion() + 1) + ":");
        for (int i = 0; i < Math.max(content.length, oldContent.length); i++) {
            try {
                if (!(content[i].equals(oldContent[i].replace("\r", "")))) {
                    toWrite.append(i).append(",\"").append(content[i]).append("\"");
                    toWrite.append(";");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                if (content.length >= oldContent.length) {
                    toWrite.append(i).append(",\"").append(content[i]).append("\"");
                } else {
                    toWrite.append(i).append(",\"").append("\"");
                }
                toWrite.append(";");
            }
        }
        return toWrite.toString();
    }

}
